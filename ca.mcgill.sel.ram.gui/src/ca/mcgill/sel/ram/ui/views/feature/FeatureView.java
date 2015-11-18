package ca.mcgill.sel.ram.ui.views.feature;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.input.inputProcessors.componentProcessors.AbstractComponentProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.core.COREFeatureRelationshipType;
import ca.mcgill.sel.core.COREModel;
import ca.mcgill.sel.core.COREReuse;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamImageComponent;
import ca.mcgill.sel.ram.ui.components.RamLineComponent;
import ca.mcgill.sel.ram.ui.components.RamRectangleComponent;
import ca.mcgill.sel.ram.ui.layouts.VerticalLayout;
import ca.mcgill.sel.ram.ui.utils.Colors;
import ca.mcgill.sel.ram.ui.utils.Constants;
import ca.mcgill.sel.ram.ui.views.TextView;
import ca.mcgill.sel.ram.ui.views.feature.FeatureDiagramSelectView.SelectionStatus;
import ca.mcgill.sel.ram.ui.views.feature.handler.IFeatureHandler;
import ca.mcgill.sel.ram.ui.views.feature.handler.impl.FeatureRelationShipHandler;
import ca.mcgill.sel.ram.ui.views.feature.handler.impl.FeatureSelectModeHandler;
import ca.mcgill.sel.ram.ui.views.handler.HandlerFactory;

/**
 * Class used to represent each of the COREFeature in TouchCORE.
 * 
 * @author Nishanth
 */
public class FeatureView extends RamRectangleComponent {

    private TextView textView;
    private MTEllipse circle;
    private float xposition;
    private float yposition;
    private FeatureView parent;
    private COREFeatureRelationshipType childrenRelationship;
    private COREFeatureRelationshipType parentRelationship;
    private List<FeatureView> childrenFeatures = new ArrayList<FeatureView>();
    private boolean isRoot;
    private float angleWithRespectToParent;
    private RamLineComponent lineToParent;
    private boolean reuse;

    private SelectionStatus selectionStatus;

    private COREFeature feature;
    private IFeatureHandler handler;
    private COREReuse coreReuse;

    private RamImageComponent image;
    private MTPolygon reuseArrow;
    private MTPolygon triangle;

    /**
     * Default contructor.
     */
    public FeatureView() {
        super(0, 0, 50, 50);
        setNoStroke(false);
        setNoFill(false);

        setAnchor(PositionAnchor.CENTER);
        setLayout(new VerticalLayout());
        setAutoMaximizes(true);
        setAutoMinimizes(true);
        setPickable(true);
    }

    /**
     * Constructor called when a Feature display is to be created.
     * 
     * @param coreFeature - the {@link COREFeature} that the FeatureView will represent.
     * @param parent - the parent {@link FeatureView}, if any
     * @param type - the {@link COREFeatureRelationshipType} to the parent feature
     * @param isReuse - whether the FeatureView represents a reuse or not
     * @param reuse - the {@link COREReuse} associated represented by the feature
     */
    public FeatureView(COREFeature coreFeature, FeatureView parent, COREFeatureRelationshipType type, boolean isReuse,
            COREReuse reuse) {
        this(coreFeature, parent, type, isReuse, reuse, false);
    }

    /**
     * Constructor called when a Feature display is to be created.
     * 
     * @param coreFeature - the {@link COREFeature} that the FeatureView will represent.
     * @param parent - the parent {@link FeatureView}, if any
     * @param type - the {@link COREFeatureRelationshipType} to the parent feature
     * @param isReuse - whether the FeatureView represents a reuse or not
     * @param reuse - the {@link COREReuse} associated represented by the feature
     * @param displayReuse - Whether we display the reuse name (true) or the feature name (false).
     *            If it's the reuse name, it can be updated by double-clicking on the View
     */
    public FeatureView(COREFeature coreFeature, FeatureView parent, COREFeatureRelationshipType type, boolean isReuse,
            COREReuse reuse, boolean displayReuse) {
        this();

        this.parentRelationship = type;

        this.selectionStatus = SelectionStatus.NOT_SELECTED;

        this.setFeatureParent(parent);
        if (parent == null) {
            setIsRoot(true);
        }

        if (type == COREFeatureRelationshipType.OR) {
            parent.setChildrenRelationship(type);
        } else if (type == COREFeatureRelationshipType.XOR) {
            parent.setChildrenRelationship(type);
        }

        setBuffers(3);
        setBufferSize(Cardinal.SOUTH, 0);
        if (reuse != null && displayReuse) {
            textView = new TextView(reuse, CorePackage.Literals.CORE_NAMED_ELEMENT__NAME);
            textView.registerTapProcessor(HandlerFactory.INSTANCE.getTextViewHandler());
            textView.setUniqueName(true);
        } else {
            textView = new TextView(coreFeature, CorePackage.Literals.CORE_NAMED_ELEMENT__NAME);
            textView.setHandler(HandlerFactory.INSTANCE.getTextViewHandler());
        }
        addChild(textView);
        setFeature(coreFeature);
        // Set the right color for the feature
        reAssignColors(null);

        if (!isRoot && (type == COREFeatureRelationshipType.MANDATORY
                || type == COREFeatureRelationshipType.OPTIONAL)) {
            MTEllipse relCircle = new MTEllipse(RamApp.getApplication(), new Vector3D(getWidth() / 2, -6, 0), 8, 8);
            relCircle.removeAllGestureEventListeners();
            relCircle.setNoStroke(true);
            this.setCircle(relCircle);
            updateRelationshipColor();
        }
        if (isReuse) {
            setReuse(true);
            this.coreReuse = reuse;
        }

    }

    /**
     * Function to set the handler to the feature.
     * 
     * @param handler - the handler to set
     */
    public void setHandler(IFeatureHandler handler) {
        this.handler = handler;
    }

    /**
     * Function to return the Feature Handler.
     * 
     * @return handler
     */
    public IFeatureHandler getHandler() {
        return this.handler;
    }

    /**
     * Get the image associated with the view.
     * 
     * @return - The image
     */
    public RamImageComponent getImage() {
        return image;
    }

    /**
     * Set the image associated with the view.
     * 
     * @param image - The {@link RamImageComponent} to set
     */
    public void setImage(RamImageComponent image) {
        this.image = image;
    }

    /**
     * Getter for the selectionStatus attribute.
     * 
     * @return - The {@link SelectionStatus} of the view
     */
    public SelectionStatus getSelectionStatus() {
        return selectionStatus;
    }

    /**
     * Update for the selectionStatus attribute and its color accordingly.
     * 
     * @param selectionStatus - The new {@link SelectionStatus} of the view
     */
    public void setSelectionStatus(SelectionStatus selectionStatus) {
        this.selectionStatus = selectionStatus;
        updateColors();
    }

    /**
     * Function used to set the angle of the Feature with respect to parent.
     * 
     * @param degrees - the angle to set
     */
    public void setAngleWithRespectToParent(float degrees) {
        angleWithRespectToParent = degrees;
    }

    /**
     * Function to retrieve the x position calculated.
     * 
     * @return Xposition
     */
    public float getXposition() {
        return xposition;
    }

    /**
     * Function to set the x position calculated.
     * 
     * @param xposition - position to set
     */
    public void setXposition(float xposition) {
        this.xposition = xposition;
    }

    /**
     * Function to retrieve the y position calculated.
     * 
     * @return Yposition
     */
    public float getYposition() {
        return yposition;
    }

    /**
     * Function to set the y position calculated.
     * 
     * @param yposition - position to set
     */
    public void setYposition(float yposition) {
        this.yposition = yposition;
    }

    /**
     * Function called to return the parent of the FeatureDisplay.
     * 
     * @return FeatureParent
     */
    public FeatureView getParentFeatureView() {
        return parent;
    }

    /**
     * Function called to set the parent of the FeatureDisplay.
     * 
     * @param newParent - FeatureParent
     */
    public void setParentFeatureView(FeatureView newParent) {
        this.parent = newParent;
    }

    /**
     * Function called to set the parent as the Feature's parent.
     * 
     * @param parentPassed - the parent feature
     */
    public void setFeatureParent(FeatureView parentPassed) {
        this.parent = parentPassed;
    }

    /**
     * Function called to return all the children of the FeatureDisplay object.
     * 
     * @return children
     */
    public List<FeatureView> getChildrenFeatureViews() {
        return childrenFeatures;
    }

    /**
     * Function to check if the FeatureDisplay is the root.
     * 
     * @return isRoot
     */
    public boolean getIsRoot() {
        return isRoot;
    }

    /**
     * Function to set the current FeatureDisplay as the Root.
     * 
     * @param isRoot - root or not
     */
    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    /**
     * Function used to retrieved the COREFeature.
     * 
     * @return feature.
     */
    public COREFeature getFeature() {
        return feature;
    }

    /**
     * Function used to set the COREFeature.
     * 
     * @param feature - the feature
     */
    public void setFeature(COREFeature feature) {
        this.feature = feature;
    }

    /**
     * Function which returns the circle.
     * 
     * @return MTcomponent
     */
    public MTEllipse getRelationshipCircle() {
        return circle;
    }

    /**
     * Function to set the circle.
     * 
     * @param newCircle (MTComponent)
     */
    public void setCircle(MTEllipse newCircle) {
        if (circle != null) {
            circle.unregisterAllInputProcessors();
            circle.removeAllGestureEventListeners();
        }
        this.circle = newCircle;
    }

    /**
     * Function to get the triangle.
     * 
     * @return {@link MTPolygon}
     */
    public MTPolygon getRelationshipTriangle() {
        return triangle;
    }

    /**
     * Function to set the triangle.
     * 
     * @param newTriangle (MTPolygon)
     */
    public void setRelationshipTriangle(MTPolygon newTriangle) {
        if (triangle != null) {
            triangle.unregisterAllInputProcessors();
            triangle.removeAllGestureEventListeners();
        }

        this.triangle = newTriangle;
    }

    /**
     * Function used to get the relationship set in the Feature View.
     * 
     * @return relationship - The relationship of the Feature, XOR / OR / Null(in case of Optional/ Mandatory)
     */
    public COREFeatureRelationshipType getChildrenRelationship() {
        return childrenRelationship;
    }

    /**
     * Function used to get the view relationship set in the Feature View. This relationship represents the Feature
     * relationship as with its parent
     * 
     * @return viewRelationship - The relationship of the Feature, XOR / OR / Mandatory / Optional
     */
    public COREFeatureRelationshipType getParentRelationship() {
        return parentRelationship;
    }

    /**
     * Function used to get the Line Component to parent.
     * 
     * @return RamLineComponent - The line component to the parent of the feature.(null if it is root).
     */
    public RamLineComponent getLineToParent() {
        return lineToParent;
    }

    /**
     * Function used to set the line of the feature to the parent.
     * 
     * @param lineToParent - The line component to the parent.
     */
    public void setLineToParent(RamLineComponent lineToParent) {
        this.lineToParent = lineToParent;
    }

    /**
     * Get the arrow to itself reuse.
     * 
     * @return - The polygon for this reuse
     */
    public MTPolygon getReuseArrow() {
        return reuseArrow;
    }

    /**
     * Associate the view with the arrow to itself reuse.
     * 
     * @param polygon - The {@link MTPolygon} representing the arrow
     */
    public void setReuseArrow(MTPolygon polygon) {
        this.reuseArrow = polygon;
    }

    /**
     * Function used to get the current position of the Feature with respect to its siblings.
     * 
     * @return position - The current position of the Feature.
     */
    public int getCurrentPosition() {
        return parent.getChildrenFeatureViews().indexOf(this);
    }

    /**
     * Function used to check whether the current feature view is a reuse or not.
     * 
     * @return - Boolean - whether is it a reuse or not.
     */
    public boolean isReuse() {
        return reuse;
    }

    /**
     * Function used to set the set whether the Feature View is a reuse or not.
     * 
     * @param reuse - True / False.
     */

    public void setReuse(boolean reuse) {
        this.reuse = reuse;
    }

    /**
     * Function to set the relationship of the feature to its children (XOR or OR).
     * 
     * @param type - The type to be set to the feature.
     */
    public void setChildrenRelationship(COREFeatureRelationshipType type) {
        this.childrenRelationship = type;
    }

    /**
     * Function to set the relationship of the feature to the parent.
     * 
     * @param type - The type to be set to the feature.
     */
    public void setParentRelationship(COREFeatureRelationshipType type) {
        this.parentRelationship = type;
    }

    /**
     * Getter for the coreReuse assoicated with the view.
     * 
     * @return the coreReuse
     */
    public COREReuse getCoreReuse() {
        return coreReuse;
    }

    /**
     * Setter for the coreReuse associated with the view.
     * 
     * @param coreReuse the coreReuse to set
     */
    public void setCoreReuse(COREReuse coreReuse) {
        this.coreReuse = coreReuse;
    }

    /**
     * Function to add a child.
     * 
     * @param child - The {@link FeatureView} to add as a child
     */
    public void addChild(FeatureView child) {
        getChildrenFeatureViews().add(child);
    }

    /**
     * Function used to open the keyBoard of the textView.
     */
    public void showKeyboard() {
        textView.showKeyboard();
    }

    /*
     * --------------------- DISPLAY ------------------------------
     */

    /**
     * Set the color of the view depending of its {@link SelectionStatus}.
     */
    private void updateColors() {
        if (selectionStatus.hasFillColor()) {
            setFillColor(selectionStatus.getFillColor());
        }
        setStrokeColor(selectionStatus.getStrokeColor());
        if (parent != null && lineToParent != null) {
            lineToParent.setStrokeColor(selectionStatus.getParentLinkColor());
            if (parent.getRelationshipTriangle() != null) {
                parent.getRelationshipTriangle().setStrokeColor(selectionStatus.getParentLinkColor());
            }
        }
    }

    /**
     * Put back default color to feature.
     * 
     * @param selected - coreModel. If not null, and it realizes the feature, the view is highlighted
     */
    public void reAssignColors(COREModel selected) {
        if (selected != null && feature.getRealizedBy().contains(selected)) {
            setFillColor(Colors.FEATURE_ASSIGNEMENT_FILL_COLOR);
        } else if (isReuse() || isParentReuse()) {
            if (selectionStatus == SelectionStatus.RE_EXPOSED) {
                setFillColor(Colors.FEATURE_REUSE_REEXPOSE_FILL_COLOR);
            } else {
                setFillColor(Colors.FEATURE_REUSE_FILL_COLOR);
            }
        } else if (getFeature().getRealizedBy().size() > 0) {
            setFillColor(Colors.FEATURE_ASSIGNED_FILL_COLOR);
        } else if (getFeature().getRealizedBy().size() == 0) {
            setFillColor(Colors.FEATURE_UNASSIGNED_FILL_COLOR);
        }
    }

    /**
     * Highlight the feature.
     * 
     * @param highlight - whether we want to highlight the feature or not.
     */
    public void highlight(boolean highlight) {
        MTColor color = highlight ? Colors.HIGHLIGHT_ELEMENT_STROKE_COLOR : getSelectionStatus().getStrokeColor();
        this.setStrokeColor(color);
    }

    /**
     * Update the color of the views representing the relationships for the feature.
     */
    void updateRelationshipColor() {
        if (circle != null) {
            if (parentRelationship == COREFeatureRelationshipType.MANDATORY) {
                circle.setFillColor(Colors.FEATURE_MANDATORY_RELATIONSHIP_FILL_COLOR);
            } else if (parentRelationship == COREFeatureRelationshipType.OPTIONAL) {
                circle.setFillColor(Colors.FEATURE_OPTIONAL_RELATIONSHIP_FILL_COLOR);
            }
        }

        if (triangle != null) {
            if (childrenRelationship == COREFeatureRelationshipType.OR) {
                triangle.setFillColor(Colors.FEATURE_OR_RELATIONSHIP_COLOR);
                triangle.setNoFill(false);
            } else if (childrenRelationship == COREFeatureRelationshipType.XOR) {
                triangle.setNoFill(true);
            }
        }
    }

    /*
     * --------------------- BEHAVIOR ------------------------------
     */

    /**
     * Function called to deselect all selection. Called when clear button is pressed.
     */
    public void deSelectAll() {
        setSelectionStatus(SelectionStatus.NOT_SELECTED);
        if (lineToParent != null) {
            lineToParent.setStrokeColor(selectionStatus.getParentLinkColor());
        }
        for (FeatureView child : childrenFeatures) {
            child.deSelectAll();
        }
    }

    /**
     * Function to add Listeners in select mode.
     */
    public void setListenersSelectMode() {
        unregisterAllInputProcessors();
        removeAllGestureEventListeners();

        FeatureSelectModeHandler handlerCreated = HandlerFactory.INSTANCE.getFeatureSelectModeHandler();
        AbstractComponentProcessor tapProcessor = new TapProcessor(RamApp.getApplication(),
                Constants.TAP_MAX_FINGER_UP, true, Constants.TAP_DOUBLE_TAP_TIME);
        tapProcessor.setBubbledEventsEnabled(true);
        registerInputProcessor(tapProcessor);
        addGestureListener(TapProcessor.class, handlerCreated);

        if (!isRoot) {
            AbstractComponentProcessor tapAndHoldProcessor = new TapAndHoldProcessor(RamApp.getApplication(),
                    Constants.TAP_AND_HOLD_DURATION);
            tapAndHoldProcessor.setBubbledEventsEnabled(true);
            registerInputProcessor(tapAndHoldProcessor);
            addGestureListener(TapAndHoldProcessor.class, handlerCreated);
        }
    }

    /**
     * Function to add all the listeners to to the components.
     * 
     * @param featureHandler - The handler to set for the feature
     */
    public void setListenersEditMode(IFeatureHandler featureHandler) {
        unregisterAllInputProcessors();
        removeAllGestureEventListeners();

        // TapAndHoldProcessor
        AbstractComponentProcessor tapAndHoldProcessor = new TapAndHoldProcessor(RamApp.getApplication(),
                Constants.TAP_AND_HOLD_DURATION);
        tapAndHoldProcessor.setBubbledEventsEnabled(true);
        registerInputProcessor(tapAndHoldProcessor);

        if (isReuse() || isParentReuse()) {
            IFeatureHandler reuseHandler = HandlerFactory.INSTANCE.getReuseEditModeHandler();
            setHandler(reuseHandler);
            addGestureListener(TapAndHoldProcessor.class, reuseHandler);
            return;
        }

        // Set default handler
        addGestureListener(TapAndHoldProcessor.class, featureHandler);
        setHandler(featureHandler);

        AbstractComponentProcessor doubleTapProcessor = new TapProcessor(RamApp.getApplication(),
                Constants.TAP_MAX_FINGER_UP, true, Constants.TAP_DOUBLE_TAP_TIME);
        doubleTapProcessor.setBubbledEventsEnabled(true);
        registerInputProcessor(doubleTapProcessor);
        addGestureListener(TapProcessor.class, featureHandler);

        // Features that are not root can be dragged
        if (!isRoot) {
            AbstractComponentProcessor rightClick = new DragProcessor(RamApp.getApplication());
            rightClick.setBubbledEventsEnabled(true);
            registerInputProcessor(rightClick);
            addGestureListener(DragProcessor.class, featureHandler);
        }

        registerRelationShipListeners();
    }

    /**
     * Adds the listeners for the relationship elements.
     */
    private void registerRelationShipListeners() {
        FeatureRelationShipHandler relHandler = new FeatureRelationShipHandler();

        // Allow modification of relationship to parent (MANDATORY/OPTIONAL)
        if (circle != null) {
            circle.unregisterAllInputProcessors();
            circle.removeAllGestureEventListeners();

            circle.registerInputProcessor(new TapProcessor(RamApp.getApplication()));
            circle.addGestureListener(TapProcessor.class, relHandler);
            relHandler.setFeatureIcon(this);
        }

        // Allow modification of relationship to children (OX/XOR)
        if (triangle != null) {
            triangle.unregisterAllInputProcessors();
            triangle.removeAllGestureEventListeners();

            triangle.registerInputProcessor(new TapProcessor(RamApp.getApplication()));
            triangle.addGestureListener(TapProcessor.class, relHandler);
            relHandler.setFeatureIcon(this);
        }
    }

    /*
     * ------------------------------- HELPERS ---------------------------------
     */

    /**
     * Check if the FeatureView comes from a reuse.
     * 
     * @return true if a parent of the feature is a reuse.
     */
    public boolean isParentReuse() {
        if (parent == null) {
            return false;
        }
        if (parent.isReuse()) {
            return true;
        }
        return parent.isParentReuse();
    }

    /**
     * Function used to get the position of a feature with respect to its parent.
     * 
     * @param degrees - At what angle the unistrokegesture was drawn.
     * @return - The position at which it should it inserted.
     */
    public int getPosition(float degrees) {

        int position = 0;
        for (FeatureView child : childrenFeatures) {
            // Only consider children that are non-reuse links,
            // because reuse links are not part of the feature model.
            if (!child.isReuse()) {
                if (degrees > child.angleWithRespectToParent) {
                    position++;
                } else {
                    break;
                }
            }
        }
        return position;
    }

    /**
     * Get the highest rank reuse parent of the given feature.
     * 
     * @return - The parentReuse, or null if not found
     */
    public FeatureView getHighestParentReuse() {
        FeatureView view = this;
        // Get closest reuse
        while (!view.isReuse() && view.getParentFeatureView() != null) {
            view = view.getParentFeatureView();
        }
        // Check if there is a higher level reuse
        if (view.getParentFeatureView() != null) {
            FeatureView higherReuse = view.getParentFeatureView().getHighestParentReuse();
            if (higherReuse != null && higherReuse.isReuse()) {
                return higherReuse;
            }
        }
        if (view.isReuse()) {
            return view;
        }
        return null;
    }

    /**
     * Returns a set of selected features.
     * 
     * @return the set of selected features
     */
    public Set<COREFeature> collectSelected() {
        Set<COREFeature> result = new HashSet<COREFeature>();
        return collectSelected(result);
    }

    /**
     * Returns the set of re-exposed features.
     * 
     * @return - The set of all reexposed features.
     */
    public Set<COREFeature> collectReexposed() {
        Set<COREFeature> result = new HashSet<COREFeature>();
        return collectReexposed(result);
    }

    /**
     * Function to collect all the selected features.
     * 
     * @param set - The selected list from the parent.
     * @return the set of selected features
     */
    public Set<COREFeature> collectSelected(Set<COREFeature> set) {
        if (selectionStatus == SelectionStatus.AUTO_SELECTED || selectionStatus == SelectionStatus.SELECTED
                || selectionStatus == SelectionStatus.WARNING_SELECTED) {
            set.add(feature);
        }
        for (FeatureView child : getChildrenFeatureViews()) {
            set.addAll(child.collectSelected(set));
        }
        return set;
    }

    /**
     * Function used to collect all the reexposed features.
     * 
     * @param list - The reexposed list from the parent.
     * @return - Recursive return of list containing all the features reexposed by its children.
     */
    public Set<COREFeature> collectReexposed(Set<COREFeature> list) {

        if (selectionStatus == SelectionStatus.RE_EXPOSED) {
            list.add(feature);
        }

        for (FeatureView child : getChildrenFeatureViews()) {
            list.addAll(child.collectReexposed(list));
        }

        return list;
    }

    /**
     * Function called to collect all the Features in the Feature Model. Called on root Feature of Feature Model.
     * Recursively calls on the rest of the child.
     * 
     * @return Set<COREFeature> - The collected COREFeature from the Feature Model.
     */
    public List<COREFeature> collectFeatures() {
        List<COREFeature> features = new LinkedList<COREFeature>();
        if (!isReuse()) {
            for (FeatureView child : childrenFeatures) {
                features.addAll(child.collectFeatures());
            }
            features.add(getFeature());
        }

        return features;
    }

    /**
     * Recursive function which collects all the DragContainers of itself and its children.
     * 
     * @return the list of components to be dragged
     */
    public List<MTComponent> getAllDragContainers() {
        List<MTComponent> components = new LinkedList<MTComponent>();
        for (FeatureView child : childrenFeatures) {
            if (child.lineToParent != null) {
                components.add(child.lineToParent);
            }
            if (child.reuseArrow != null) {
                components.add(child.reuseArrow);
            }
            components.addAll(child.getAllDragContainers());
            components.add(child);
        }
        if (circle != null) {
            components.add(circle);
        }
        if (triangle != null) {
            components.add(triangle);
        }
        return components;
    }

    /**
     * Function used to check if the feature has any children pertaining to the list passed. Can be used both for
     * selections / reexposing.
     * 
     * @param featurePassed - The current feature against which the check has to be performed.
     * @param selectedList - The selected List of features against whom the comparison is to be made.
     * @return collectedChildren - The set of all children which are present.
     */
    public Set<COREFeature> containsChildren(FeatureView featurePassed, Set<COREFeature> selectedList) {

        Set<COREFeature> collectedChildren = new HashSet<COREFeature>();

        for (FeatureView individualLink : featurePassed.getChildrenFeatureViews()) {
            if (selectedList.contains(individualLink.getFeature())) {
                collectedChildren.add(individualLink.getFeature());
            }
            collectedChildren.addAll(containsChildren(individualLink, selectedList));
        }

        return collectedChildren;
    }

    /**
     * Function used to check if any children are present in the immediate one level down.
     * 
     * @param featurePassed - The feature which is used to check.
     * @return collectedChildren - The set of all collected children.
     */
    public static Set<COREFeature> containsChildrenOneLevel(FeatureView featurePassed) {
        Set<COREFeature> collectedChildren = new HashSet<COREFeature>();

        for (FeatureView individualLink : featurePassed.childrenFeatures) {
            if (individualLink.selectionStatus == SelectionStatus.SELECTED
                    || individualLink.selectionStatus == SelectionStatus.AUTO_SELECTED
                    || individualLink.selectionStatus == SelectionStatus.WARNING_SELECTED) {

                collectedChildren.add(individualLink.getFeature());
            }
        }

        return collectedChildren;
    }

    @Override
    public void unregisterAllInputProcessors() {
        super.unregisterAllInputProcessors();
        if (circle != null) {
            circle.unregisterAllInputProcessors();
        }
        if (triangle != null) {
            triangle.unregisterAllInputProcessors();
        }
    }

    @Override
    public void removeAllGestureEventListeners() {
        super.removeAllGestureEventListeners();
        if (circle != null) {
            circle.removeAllGestureEventListeners();
        }
        if (triangle != null) {
            triangle.removeAllGestureEventListeners();
        }
    }

    @Override
    public void destroy() {
        if (circle != null) {
            circle.destroy();
        }
        if (triangle != null) {
            triangle.destroy();
        }
        if (lineToParent != null) {
            lineToParent.destroy();
        }
        if (reuseArrow != null) {
            reuseArrow.destroy();
        }
        if (image != null) {
            image.destroy();
        }
        textView.destroy();

        removeAllGestureEventListeners();
        super.destroy();
    }

}
