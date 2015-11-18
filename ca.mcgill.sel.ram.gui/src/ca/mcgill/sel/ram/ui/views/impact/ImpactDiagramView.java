package ca.mcgill.sel.ram.ui.views.impact;

import java.awt.geom.Rectangle2D;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.abego.treelayout.NodeExtentProvider;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEMap.Entry;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.mt4j.input.inputProcessors.componentProcessors.panProcessor.PanProcessorTwoFingers;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils.Direction;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils.UnistrokeGesture;
import org.mt4j.input.inputProcessors.componentProcessors.zoomProcessor.ZoomProcessor;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.commons.emf.util.EMFModelUtil;
import ca.mcgill.sel.core.COREContribution;
import ca.mcgill.sel.core.COREFeatureImpactNode;
import ca.mcgill.sel.core.COREImpactModel;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.core.LayoutElement;
import ca.mcgill.sel.core.controller.ControllerFactory;
import ca.mcgill.sel.core.impl.LayoutContainerMapImpl;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.events.MouseWheelProcessor;
import ca.mcgill.sel.ram.ui.events.RightClickDragProcessor;
import ca.mcgill.sel.ram.ui.events.UnistrokeProcessorLeftClick;
import ca.mcgill.sel.ram.ui.utils.Constants;
import ca.mcgill.sel.ram.ui.views.AbstractView;
import ca.mcgill.sel.ram.ui.views.handler.IAbstractViewHandler;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.FeatureImpactViewHandler;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.GoalImpactViewHandler;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.ImpactModelElementViewHandler;

/**
 * Class used to represent an impact model. Has a root goal and builds the model from it
 *
 * @param <H> the handler for this view
 * @author Romain
 *
 */
public abstract class ImpactDiagramView<H extends IAbstractViewHandler> extends AbstractView<H> implements
        INotifyChangedListener {
    // The initial level / depth to be shown based on screen resolution
    private static final float INITIAL_NUMBER_OF_LEVELS = 4;

    // The maximum distance between two elements while being displayed.
    private static final float MAXIMUM_VERTICAL_DISTANCE = 100;

    /**
     * The root {@link COREImpactNode} of this scene.
     */
    protected COREImpactNode root;

    /**
     * The {@link COREImpactModel} of this concern.
     */
    protected COREImpactModel impactModel;

    /**
     * A map that associate the {@link COREImpactNode} with their corresponding view (
     * {@link ImpactModelElementView}).
     */
    protected Map<COREImpactNode, ImpactModelElementView<? extends ImpactModelElementViewHandler>> impactMap;

    /**
     * A map that associate the {@link COREContribution} with their corresponding view ( {@link ContributionView} ).
     */
    protected Map<COREContribution, ContributionView> contributionMap;

    // For default Layout purpose
    /**
     * An tree used to build a default layout.
     */
    protected DefaultTreeForTreeLayout<ImpactModelElementView<? extends ImpactModelElementViewHandler>> impactModelTree;

    /**
     * The default layout of this view of the impact model.
     */
    protected TreeLayout<ImpactModelElementView<? extends ImpactModelElementViewHandler>> treeLayout;

    /**
     * The {@link LayoutContainerMapImpl} for this root.
     */
    private LayoutContainerMapImpl mapLayoutEntry;

    /**
     * Create a {@link ImpactDiagramView}. It has a root goal and builds the model from it.
     *
     * @param im the {@link COREImpactModel} of this view
     * @param rootNode the root {@link COREImpactNode}
     * @param width the width of this view
     * @param height the height of this view
     */
    public ImpactDiagramView(COREImpactModel im, COREImpactNode rootNode, float width, float height) {
        super(width, height);

        this.root = rootNode;

        this.impactModel = im;

        this.impactMap =
                new HashMap<COREImpactNode, ImpactModelElementView<? extends ImpactModelElementViewHandler>>();
        this.contributionMap = new HashMap<COREContribution, ContributionView>();

        EMFEditUtil.addListenerFor(impactModel, this);

        displayImpactModel();
    }

    /**
     * Display the impact model using {@link ImpactModelElementView}.
     */
    private void displayImpactModel() {
        // Check if there is a MapLayout for the root element, Otherwise create one
        mapLayoutEntry = EMFModelUtil.getEntryFromMap(impactModel.getLayouts(), root);

        // register to the ContainerMap to receive adds/removes of ElementMaps
        EMFEditUtil.addListenerFor(mapLayoutEntry, this);

        // We create a view for this root and we set his layout element. A root will always have a layout element.
        ImpactModelElementView<GoalImpactViewHandler> rootView = this.addGoalImpactView(root);
        rootView.setLayoutElement(mapLayoutEntry.getValue().get(root));

        // Initialize all the views
        this.initializeImpactElementViews();

        // Set elements position
        if (mapLayoutEntry.getValue().size() == 1) {
            // Create a default layout for this impact model
            this.createDefaultLayout();
        } else {
            this.setElementsPosition();
        }

        // Add Contributions linked from COREImpactModelElement
        for (COREContribution contribution : impactModel.getContributions()) {
            this.addContributionView(contribution);
        }
    }

    /**
     * Create the {@link ImpactModelElementView}s that are in the layoutcontainerMap. If one child of an element is not
     * in this container layout, we will also create it.
     *
     */
    private void initializeImpactElementViews() {
        LayoutContainerMapImpl layoutContainerMap = EMFModelUtil.getEntryFromMap(impactModel.getLayouts(), root);
        EMap<EObject, LayoutElement> layoutMap = layoutContainerMap.getValue();

        // Create all the element using the map of layouts.
        for (EObject key : layoutMap.keySet()) {
            if (key instanceof COREImpactNode) {
                COREImpactNode impactModelElement = (COREImpactNode) key;
                createElementView(impactModelElement);

                for (COREContribution contribution : impactModelElement.getIncoming()) {
                    if (!layoutMap.containsKey(contribution.getSource())) {
                        initializeChildViews(contribution.getSource());
                    }
                }
            }
        }

        // Then create all the children of the root if their are not in the layout map.
        for (COREContribution contribution : root.getIncoming()) {
            if (!layoutMap.containsKey(contribution.getSource())) {
                initializeChildViews(contribution.getSource());
            }
        }
    }

    /**
     * Function called by "initializeImpactElementViews" in order to create all the children of the selected Goal. It
     * will create {@link ImpactModelElementView} on the scene. It will not create a new {@link COREImpactNode}.
     *
     * @param child - the {@link COREImpactNode} that will be associated to the view
     */
    private void initializeChildViews(COREImpactNode child) {
        if (impactMap.containsKey(child)) {
            return;
        }

        createElementView(child);

        for (COREContribution contribution : child.getIncoming()) {
            initializeChildViews(contribution.getSource());
        }
    }

    /**
     * Set the position of all the elements previously created using the map layout. If there is no layout for one
     * element, it will be displayed randomly.
     */
    private void setElementsPosition() {
        EMap<EObject, LayoutElement> mapLayout = impactModel.getLayouts().get(root);
        for (ImpactModelElementView<? extends ImpactModelElementViewHandler> impactModelElementView : impactMap
                .values()) {

            COREImpactNode element = impactModelElementView.getImpactModelElement();
            LayoutElement layoutElement = mapLayout.get(element);

            if (layoutElement != null) {
                impactModelElementView.setLayoutElement(layoutElement);
            } else {
                Random r = new Random();

                float x = getWidth() * r.nextFloat();
                float y = getHeight() * 0.75f;
                updateLayoutElement(impactModelElementView, x, y);
            }
        }
    }

    /**
     * Create a default layout with no offsetValue for the Y axe.
     */
    private void createDefaultLayout() {
        ImpactModelElementView<? extends ImpactModelElementViewHandler> rootGoal = this.impactMap.get(root);

        // // First, if the mapLayout is null, we will need to create a default layout.
        // // So we create here a tree, and we will build it during the operation initializeImpactElementViews.
        impactModelTree =
                new DefaultTreeForTreeLayout<ImpactModelElementView<? extends ImpactModelElementViewHandler>>(rootGoal);

        for (COREContribution contribution : root.getIncoming()) {
            initializeTreeLayout(rootGoal, contribution.getSource());
        }

        this.createDefaultLayout(0);
    }

    /**
     * Function called by "initializeImpactElementViews" in order to create all the children of the selected Goal. It
     * will create {@link ImpactModelElementView} on the scene. It will not create a new {@link COREImpactNode}.
     *
     * @param parentView - the {@link ImpactModelElementView} that is the parent of this new view in the impact model
     * @param child - the {@link COREImpactNode} that will be associated to the view
     */
    private void initializeTreeLayout(ImpactModelElementView<? extends ImpactModelElementViewHandler> parentView,
            COREImpactNode child) {
        ImpactModelElementView<? extends ImpactModelElementViewHandler> childView = this.impactMap.get(child);

        if (!impactModelTree.hasNode(childView)) {
            impactModelTree.addChild(parentView, childView);

            for (COREContribution contribution : child.getIncoming()) {
                initializeTreeLayout(childView, contribution.getSource());
            }
        }
    }

    /**
     * Create a default layout with an offsetValue for the Y axe.
     *
     * @param heightValueOffset the value for the offset of the Y axe
     */
    protected final void createDefaultLayout(float heightValueOffset) {
        // Compute the gap between the levels based on the height of the screen, divided by the desired value of user.
        double gapBetweenLevels = getHeight() / INITIAL_NUMBER_OF_LEVELS;
        if (gapBetweenLevels >= MAXIMUM_VERTICAL_DISTANCE) {
            gapBetweenLevels = MAXIMUM_VERTICAL_DISTANCE;
        }

        // Copy the horizontal minimum distance.
        double gapBetweenNodes = Constants.ROOT_GOAL_INITIAL_HEIGHT;

        // A configuration is created according to the data structure provided in the jar, passing the above two values.
        DefaultConfiguration<ImpactModelElementView<? extends ImpactModelElementViewHandler>> configuration =
                new DefaultConfiguration<ImpactModelElementView<? extends ImpactModelElementViewHandler>>(
                        gapBetweenLevels, gapBetweenNodes);

        // Create the NodeExtentProvider for TextInBox nodes
        NodeExtentProvider<ImpactModelElementView<? extends ImpactModelElementViewHandler>> nodeExtentProvider =
                new NodeExtentProvider<ImpactModelElementView<? extends ImpactModelElementViewHandler>>() {
            @Override
            public double getHeight(
                    ImpactModelElementView<? extends ImpactModelElementViewHandler> impactModelElementView) {
                return impactModelElementView.getHeight();
            }

            @Override
            public double getWidth(
                    ImpactModelElementView<? extends ImpactModelElementViewHandler> impactModelElementView) {
                return impactModelElementView.getWidth();
            }
        };

        // Create the layout - Created by passing the tree, the configuration and the nodeExtentProvider.
        treeLayout =
                new TreeLayout<ImpactModelElementView<? extends ImpactModelElementViewHandler>>(impactModelTree,
                        nodeExtentProvider, configuration);

        ImpactModelElementView<? extends ImpactModelElementViewHandler> rootView = treeLayout.getTree().getRoot();
        float offsetValueX = -rootView.getCenterPointGlobal().x;

        float offsetValueY = 0;
        if (heightValueOffset > 0) {
            offsetValueY = -rootView.getCenterPointGlobal().y - heightValueOffset;
        }

        Map<COREImpactNode, AbstractMap.SimpleEntry<Float, Float>> elementMap =
                new HashMap<COREImpactNode, SimpleEntry<Float, Float>>();

        elementMap.putAll(setPositionFromDefaultLayout(rootView, offsetValueX, offsetValueY, (float) gapBetweenLevels,
                0));

        updateLayoutElements(elementMap);

        // Set to null to free the space
        impactModelTree = null;
        treeLayout = null;
    }

    /**
     * Function called to adjust all the final positions of the elements. Recursive function. Calls all its children by
     * itself.
     *
     * @param impactModelElementView - The element
     * @param offsetValueX - The offsetValue for the x axe.
     * @param offsetValueY - The offsetValue for the y axe.
     * @param gapBetweenYLevels - The gap between each level of the Impact Model.
     * @param position - THe horizontal position of the node.
     * @return the map of Element to update with their new position
     */
    private Map<COREImpactNode, SimpleEntry<Float, Float>> setPositionFromDefaultLayout(
            ImpactModelElementView<? extends ImpactModelElementViewHandler> impactModelElementView, float offsetValueX,
            float offsetValueY, float gapBetweenYLevels, int position) {
        Map<COREImpactNode, AbstractMap.SimpleEntry<Float, Float>> result =
                new HashMap<COREImpactNode, SimpleEntry<Float, Float>>();

        Rectangle2D.Double elementBounds = treeLayout.getNodeBounds().get(impactModelElementView);
        float newX = (float) (elementBounds.getCenterX() - offsetValueX);
        float newY = (((position * gapBetweenYLevels) + ((position + 1) * gapBetweenYLevels)) / 2) - offsetValueY;

        SimpleEntry<Float, Float> positionEntry = new SimpleEntry<Float, Float>(newX, newY);

        if (impactModelElementView.getImpactModelElement() != root) {
            result.put(impactModelElementView.getImpactModelElement(), positionEntry);
        }

        for (ImpactModelElementView<? extends ImpactModelElementViewHandler> child : impactModelTree
                .getChildren(impactModelElementView)) {
            result.putAll(setPositionFromDefaultLayout(child, offsetValueX, offsetValueY, gapBetweenYLevels,
                    position + 1));
        }

        return result;
    }

    /**
     * Create a new LayoutElement for this {@link COREImpactNode}.
     *
     * @param impactModelElementView the {@link ImpactModelElementView} that contains the {@link COREImpactNode}
     * @param x the x of the new position
     * @param y the y of the new position
     */
    private void updateLayoutElement(
            ImpactModelElementView<? extends ImpactModelElementViewHandler> impactModelElementView, float x, float y) {
        // Create a MapLayout for the element
        ControllerFactory.INSTANCE.getImpactModelElementController().createLayoutElement(root,
                impactModelElementView.getImpactModelElement(), x, y);
    }

    /**
     * Create a new LayoutElement for these {@link COREImpactNode}s.
     *
     * @param elementMap the map of Element to update with their new position
     */
    private void updateLayoutElements(Map<COREImpactNode, AbstractMap.SimpleEntry<Float, Float>> elementMap) {
        // LayoutContainerMapImpl layoutContainerEntry = EMFModelUtil.getEntryFromMap(impactModel.getLayouts(), root);
        //
        // EMap<EObject, LayoutElement> layoutMapMap = layoutContainerEntry.getValue();
        //
        // for (COREImpactModelElement impactModelElement : elementMap.keySet()) {
        // if (impactModelElement == root) {
        // continue;
        // }
        // SimpleEntry<Float, Float> positionEntry = elementMap.get(impactModelElement);
        // float x = positionEntry.getKey();
        // float y = positionEntry.getValue();
        //
        // LayoutElement layoutElement = CoreFactory.eINSTANCE.createLayoutElement();
        // layoutElement.setX(x);
        // layoutElement.setY(y);
        //
        // LayoutMapImpl layoutEntry = (LayoutMapImpl) CoreFactory.eINSTANCE.create(CorePackage.Literals.LAYOUT_MAP);
        // layoutEntry.setKey(impactModelElement);
        // layoutEntry.setValue(layoutElement);
        //
        // layoutMapMap.add(layoutEntry);
        //
        // createNodeView(layoutEntry);
        // }

        ControllerFactory.INSTANCE.getImpactModelElementController().createLayoutElements(this.root, elementMap);
    }

    @Override
    public void notifyChanged(Notification notification) {
        if (notification.getFeature() == CorePackage.Literals.CORE_IMPACT_MODEL__CONTRIBUTIONS) {
            COREContribution contribution;
            switch (notification.getEventType()) {
                case Notification.REMOVE:
                    contribution = (COREContribution) notification.getOldValue();
                    removeContributionView(contribution);
                    break;
                case Notification.ADD:
                    contribution = (COREContribution) notification.getNewValue();
                    addContributionView(contribution);
                    break;
            }
        } else if (notification.getFeature() == CorePackage.Literals.LAYOUT_CONTAINER_MAP__VALUE
                && notification.getNotifier() == this.mapLayoutEntry) {
            Entry<?, ?> entry;
            switch (notification.getEventType()) {
                case Notification.ADD:
                    entry = (Entry<?, ?>) notification.getNewValue();
                    createNodeView(entry);
                    break;
                case Notification.REMOVE:
                    entry = (Entry<?, ?>) notification.getOldValue();

                    COREImpactNode impactModelElement = (COREImpactNode) entry.getKey();
                    deleteNodeView(impactModelElement);
                    break;
            }
        }
    }

    @Override
    protected void registerInputProcessors() {
        registerInputProcessor(new PanProcessorTwoFingers(RamApp.getApplication()));
        registerInputProcessor(new RightClickDragProcessor(RamApp.getApplication()));
        registerInputProcessor(new ZoomProcessor(RamApp.getApplication()));
        registerInputProcessor(new MouseWheelProcessor(RamApp.getApplication()));

        UnistrokeProcessorLeftClick up = new UnistrokeProcessorLeftClick(RamApp.getApplication());
        up.addTemplate(UnistrokeGesture.CIRCLE, Direction.CLOCKWISE);
        up.addTemplate(UnistrokeGesture.CIRCLE, Direction.COUNTERCLOCKWISE);
        up.addTemplate(UnistrokeGesture.X, Direction.CLOCKWISE);
        up.addTemplate(UnistrokeGesture.X, Direction.COUNTERCLOCKWISE);

        registerInputProcessor(up);
    }

    /**
     * Create a view for this {@link COREImpactNode}. It will create a {@link FeatureImpactView} or
     * {@link GoalImpactView}. If a view already exists for this {@link COREImpactNode}, it will return it.
     *
     * @param impactModelElement the {@link COREImpactNode}
     * @return the view created.
     */
    private ImpactModelElementView<? extends ImpactModelElementViewHandler> createElementView(
            COREImpactNode impactModelElement) {
        if (this.impactMap.containsKey(impactModelElement)) {
            return this.impactMap.get(impactModelElement);
        }
        ImpactModelElementView<? extends ImpactModelElementViewHandler> elementView;

        if (impactModelElement instanceof COREFeatureImpactNode) {
            elementView = this.addFeatureImpactView((COREFeatureImpactNode) impactModelElement);
        } else {
            elementView = this.addGoalImpactView(impactModelElement);
        }

        return elementView;
    }

    /**
     * Create a new view for the key of this entry and after set his layout with the value of this entry.
     *
     * @param entry the {@link java.util.Map.Entry} of {@link COREImpactNode} and {@link LayoutElement}.
     */
    private void createNodeView(Entry<?, ?> entry) {
        COREImpactNode impactModelElement = (COREImpactNode) entry.getKey();
        LayoutElement layoutElement = (LayoutElement) entry.getValue();

        // Create the view for this element
        ImpactModelElementView<? extends ImpactModelElementViewHandler> modelElementView =
                createElementView(impactModelElement);

        // Create a Contribution for incoming link
        for (COREContribution contribution : impactModelElement.getIncoming()) {
            this.addContributionView(contribution);
        }

        // Create a Contribution for outgoing link
        for (COREContribution contribution : impactModelElement.getOutgoing()) {
            this.addContributionView(contribution);
        }

        modelElementView.setLayoutElement(layoutElement);
    }

    /**
     * Delete a view for the key of this entry and after set his layout with the value of this entry.
     *
     * @param impactModelElement the {@link COREImpactNode} for which we need to delete the view
     */
    private void deleteNodeView(COREImpactNode impactModelElement) {

        ImpactModelElementView<? extends ImpactModelElementViewHandler> modelElementView =
                this.impactMap.remove(impactModelElement);

        // Remove incoming contribution
        for (COREContribution contribution : impactModelElement.getIncoming()) {
            this.removeContributionView(contribution);
        }

        // Remove outgoing contribution
        for (COREContribution contribution : impactModelElement.getOutgoing()) {
            this.removeContributionView(contribution);
        }

        if (modelElementView != null) {
            modelElementView.destroy();
        }
    }

    /**
     * Add a view to this {@link COREContribution}.
     *
     * @param contribution the {@link COREContribution} which will have a view
     * @return the view created
     */
    private ContributionView addContributionView(COREContribution contribution) {
        ImpactModelElementView<? extends ImpactModelElementViewHandler> source =
                this.impactMap.get(contribution.getSource());
        ImpactModelElementView<? extends ImpactModelElementViewHandler> target =
                this.impactMap.get(contribution.getImpacts());

        if (source == null || target == null || contributionMap.containsKey(contribution)) {
            return null;
        }

        ContributionView contributionView = new ContributionView(source, target, contribution);
        contributionView.updateLines();

        contributionMap.put(contribution, contributionView);
        this.addChild(contributionView);

        this.setContributionViewHandler(contributionView);

        return contributionView;
    }

    /**
     * Remove the view linked to this {@link COREContribution}.
     *
     * @param contribution the {@link COREContribution} associated to the view to delete
     */
    private void removeContributionView(COREContribution contribution) {
        ContributionView link = this.contributionMap.remove(contribution);
        if (link != null) {
            link.destroy();
        }
    }

    /**
     * Add a view to this {@link COREImpactNode}.
     *
     * @param imptModelElem the {@link COREImpactNode} which will have a view
     * @return the view created
     */
    private ImpactModelElementView<GoalImpactViewHandler> addGoalImpactView(COREImpactNode imptModelElem) {
        ImpactModelElementView<GoalImpactViewHandler> view = new GoalImpactView(root, imptModelElem, this);
        impactMap.put(imptModelElem, view);
        this.addChild(view);
        this.setGoalImpactViewHandler(view);

        return view;
    }

    /**
     * Add a view to this {@link COREFeatureImpactNode}.
     *
     * @param featureImpact the {@link COREFeatureImpactNode} which will have a view
     * @return the view created
     */
    private ImpactModelElementView<FeatureImpactViewHandler> addFeatureImpactView(
            COREFeatureImpactNode featureImpact) {
        ImpactModelElementView<FeatureImpactViewHandler> view = new FeatureImpactView(root, featureImpact, this);
        impactMap.put(featureImpact, view);
        this.addChild(view);
        this.setFeatureImpactViewHandler(view);

        return view;
    }

    /**
     * Set the handler for the {@link ContributionView}.
     *
     * @param contributionView the {@link ContributionView} that need a handler.
     */
    protected abstract void setContributionViewHandler(ContributionView contributionView);

    /**
     * Set the handler for the {@link GoalImpactView}.
     *
     * @param view the {@link GoalImpactView} that need a handler.
     */
    protected abstract void setGoalImpactViewHandler(ImpactModelElementView<GoalImpactViewHandler> view);

    /**
     * Set the handler for the {@link FeatureImpactView}.
     *
     * @param v the {@link FeatureImpactView} that need a handler.
     */
    protected abstract void setFeatureImpactViewHandler(ImpactModelElementView<FeatureImpactViewHandler> v);

    /**
     * Function called to create a {@link GoalImpactView} on the scene. It will not create a new
     * {@link COREImpactNode}.
     *
     * @param impactModelElement - the {@link COREImpactNode} that will be associated to the goal
     */
    public void addGoalReference(COREImpactNode impactModelElement) {
        ImpactModelElementView<? extends ImpactModelElementViewHandler> impactModelElementView =
                new GoalImpactView(root, impactModelElement, this);

        impactModelTree =
                new DefaultTreeForTreeLayout<ImpactModelElementView<? extends ImpactModelElementViewHandler>>(
                        impactModelElementView);

        for (COREContribution contribution : impactModelElement.getIncoming()) {
            addChildGoalReference(impactModelElementView, contribution.getSource());
        }

        this.createDefaultLayout((float) (getHeight() / 2));
    }

    /**
     * Function called by "addGoalReference" in order to create all the children of the selected Goal. It will create
     * {@link ImpactModelElementView} on the scene. It will not create a new {@link COREImpactNode}.
     *
     * @param parentView - the {@link ImpactModelElementView} that is the parent of this new view in the impact model
     * @param child - the {@link COREImpactNode} that will be associated to the view
     */
    private void addChildGoalReference(ImpactModelElementView<? extends ImpactModelElementViewHandler> parentView,
            COREImpactNode child) {
        ImpactModelElementView<? extends ImpactModelElementViewHandler> childView;
        if (child instanceof COREFeatureImpactNode) {
            childView = new FeatureImpactView(root, (COREFeatureImpactNode) child, this);
        } else {
            childView = new GoalImpactView(root, child, this);
        }

        impactModelTree.addChild(parentView, childView);

        for (COREContribution contribution : child.getIncoming()) {
            addChildGoalReference(childView, contribution.getSource());
        }
    }

    /**
     * Retrieve the impact model.
     *
     * @return the impact model
     */
    public COREImpactModel getImpactModel() {
        return this.impactModel;
    }

    /**
     * Retrieve the root {@link COREImpactNode} of this {@link ImpactDiagramView}.
     *
     * @return the root {@link COREImpactNode}
     */
    public COREImpactNode getRootImpactModelElement() {
        return root;
    }

    /**
     * Retrieve the collection of {@link COREImpactNode}.
     *
     * @return the collection of {@link COREImpactNode}
     */
    public Collection<COREImpactNode> getImpactModelElements() {
        return impactMap.keySet();
    }

    /**
     * Retrieve the collection of {@link ImpactModelElementView}.
     *
     * @return the collection of {@link ImpactModelElementView}
     */
    public Collection<ImpactModelElementView<? extends ImpactModelElementViewHandler>> getImpactModelElementsView() {
        return impactMap.values();
    }

    /**
     * Retrieve the collection of {@link COREContribution}.
     *
     * @return the collection of {@link COREContribution}
     */
    public Collection<COREContribution> getContributions() {
        return contributionMap.keySet();
    }

    /**
     * Retrieve the collection of {@link ContributionView}.
     *
     * @return the collection of {@link ContributionView}
     */
    public Collection<ContributionView> getContributionsView() {
        return contributionMap.values();
    }

    @Override
    public void destroy() {
        // remove listeners
        EMFEditUtil.removeListenerFor(mapLayoutEntry, this);
        EMFEditUtil.removeListenerFor(impactModel, this);

        super.destroy();
    }

}
