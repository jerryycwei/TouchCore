package ca.mcgill.sel.ram.ui.views.impact;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.commons.emf.util.EMFModelUtil;
import ca.mcgill.sel.core.COREFeatureImpactNode;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.core.COREModelReuse;
import ca.mcgill.sel.core.COREWeightedMapping;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.core.LayoutElement;
import ca.mcgill.sel.ram.ui.components.RamExpendableComponent;
import ca.mcgill.sel.ram.ui.components.RamListComponent;
import ca.mcgill.sel.ram.ui.components.RamListComponent.Namer;
import ca.mcgill.sel.ram.ui.components.RamRectangleComponent;
import ca.mcgill.sel.ram.ui.components.RamTextComponent;
import ca.mcgill.sel.ram.ui.components.TextViewContainerComponent;
import ca.mcgill.sel.ram.ui.utils.Colors;
import ca.mcgill.sel.ram.ui.views.TextView;
import ca.mcgill.sel.ram.ui.views.handler.HandlerFactory;
import ca.mcgill.sel.ram.ui.views.handler.IAbstractViewHandler;
import ca.mcgill.sel.ram.ui.views.impact.handler.HandlerFactoryImpactModel;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.FeatureImpactViewEditHandler;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.FeatureImpactViewHandler;

/**
 * Class used to visually representing the {@link COREFeatureImpactNode} in an Impact Model.
 *
 * @author Nishanth
 * @author Romain
 *
 */
public class FeatureImpactView extends ImpactModelElementView<FeatureImpactViewHandler> {

    private static final int NB_MAX_MAPPING_VISIBLE = 5;

    private TextViewContainerComponent titleRectangle;
    private RamListComponent<COREWeightedMapping> scrollableListComponent;
    private RamExpendableComponent expandableComponent;

    /**
     * A namer that display a {@link COREWeightedMapping} in a {@link RamRectangleComponent}.
     */
    private Namer<COREWeightedMapping> weightedMappingNamer = new Namer<COREWeightedMapping>() {
        @Override
        public RamRectangleComponent getDisplayComponent(final COREWeightedMapping element) {
            TextViewContainerComponent rectangle = new TextViewContainerComponent();
            COREModelReuse modelReuse =
                    EMFModelUtil.getRootContainerOfType(element, CorePackage.Literals.CORE_MODEL_REUSE);
            rectangle.createTextView(modelReuse.getReuse(), CorePackage.Literals.CORE_NAMED_ELEMENT__NAME, null);
            rectangle.addChild(new RamTextComponent("-"));
            rectangle.createTextView(element.getFrom(), CorePackage.Literals.CORE_NAMED_ELEMENT__NAME, null);

            if (handler instanceof FeatureImpactViewEditHandler) {
                rectangle.createTextView(element, CorePackage.Literals.CORE_WEIGHTED_MAPPING__WEIGHT,
                        HandlerFactory.INSTANCE.getTextViewHandler());
            } else {
                rectangle.createTextView(element, CorePackage.Literals.CORE_WEIGHTED_MAPPING__WEIGHT, null);
            }

            return rectangle;
        }

        @Override
        public String getSortingName(COREWeightedMapping element) {
            return element.toString();
        }

        @Override
        public String getSearchingName(COREWeightedMapping element) {
            return getSortingName(element);
        }
    };

    /**
     * Constructor called on the Feature Impact View .
     *
     * @param root The root {@link COREImpactNode} of this view
     * @param element - the associated {@link COREFeatureImpactNode}
     * @param impactDiagramView - the current scene
     */
    public FeatureImpactView(COREImpactNode root, COREFeatureImpactNode element,
            ImpactDiagramView<? extends IAbstractViewHandler> impactDiagramView) {
        super(root, element, impactDiagramView);

        super.setNoStroke(true);
        super.setNoFill(true);

        EMFEditUtil.addListenerFor(this.impactModelElement, this);

        List<COREWeightedMapping> weightedMappings =
                ((COREFeatureImpactNode) this.impactModelElement).getWeightedMappings();

        // Create title of expendable component
        titleRectangle = new TextViewContainerComponent();
        titleRectangle.createTextView(this.impactModelElement, CorePackage.Literals.CORE_NAMED_ELEMENT__NAME, null);
        titleRectangle.createTextView(this.impactModelElement,
                CorePackage.Literals.CORE_FEATURE_IMPACT_NODE__RELATIVE_FEATURE_WEIGHT,
                null);
        titleRectangle.setNoFill(true);

        // Create hideable component of expendable component
        scrollableListComponent =
                new RamListComponent<COREWeightedMapping>(weightedMappings, weightedMappingNamer, true);
        scrollableListComponent.setMaxNumberOfElements(NB_MAX_MAPPING_VISIBLE);

        // Create expendable component
        expandableComponent =
                new RamExpendableComponent(titleRectangle, scrollableListComponent);

        expandableComponent.setNoStroke(false);
        expandableComponent.setNoFill(false);
        expandableComponent.setStrokeColor(Colors.DEFAULT_ELEMENT_STROKE_COLOR);
        expandableComponent.setFillColor(Colors.DEFAULT_ELEMENT_FILL_COLOR);

        this.addChild(expandableComponent);

        // If there is no mappings, hide the weight of the feature and the icons of the expandable component
        if (weightedMappings.isEmpty()) {
            titleRectangle.getLastTextView().setVisible(false);
            expandableComponent.setIconsVisible(false);
        }
    }

    /**
     * Constructor called on an existing element in the impact model.
     *
     * @param root The root {@link COREImpactNode} of this view
     * @param element the associated {@link COREFeatureImpactNode}
     * @param centerPoint - The point where the view is to be created.
     * @param impactDiagramView The scene associated to this element
     */
    public FeatureImpactView(COREImpactNode root, COREFeatureImpactNode element, Vector3D centerPoint,
            ImpactDiagramView<? extends IAbstractViewHandler> impactDiagramView) {
        this(root, element, impactDiagramView);
        if (centerPoint != null) {
            this.setPositionRelativeToParent(centerPoint);
        }
    }

    /**
     * Constructor called on an existing element in the impact model.
     *
     * @param root The root {@link COREImpactNode} of this view
     * @param element the associated {@link COREFeatureImpactNode}
     * @param layoutElement - The point where the view is to be created.
     * @param imDiagramView The scene associated to this element
     */
    public FeatureImpactView(COREImpactNode root, COREFeatureImpactNode element, LayoutElement layoutElement,
            ImpactDiagramView<? extends IAbstractViewHandler> imDiagramView) {
        this(root, element, imDiagramView);
        if (layoutElement != null) {
            this.setPositionRelativeToParent(new Vector3D(layoutElement.getX(), layoutElement.getY()));
        }
    }

    @Override
    public void notifyChanged(Notification notification) {
        if (notification.getFeature() == CorePackage.Literals.CORE_FEATURE_IMPACT_NODE__WEIGHTED_MAPPINGS) {
            COREWeightedMapping weightedMapping;
            switch (notification.getEventType()) {
                case Notification.ADD:
                    weightedMapping = (COREWeightedMapping) notification.getNewValue();
                    if (weightedMapping.getTo() == this.impactModelElement) {
                        scrollableListComponent.addElement(weightedMapping);
                        titleRectangle.getLastTextView().setVisible(true);
                        expandableComponent.setIconsVisible(true);
                    }
                    break;
                case Notification.REMOVE:
                    weightedMapping = (COREWeightedMapping) notification.getOldValue();
                    if (weightedMapping.getTo() == this.impactModelElement) {
                        scrollableListComponent.removeElement(weightedMapping);
                        if (scrollableListComponent.size() == 0) {
                            expandableComponent.setIconsVisible(false);
                            expandableComponent.hideHideableComponent();
                            titleRectangle.getLastTextView().setVisible(false);
                        }
                    }
                    break;
            }
        }
        super.notifyChanged(notification);
    }

    /**
     * Function used to return the textView associated with the view.
     *
     * @return textView.
     */
    public TextView getTextView() {
        return titleRectangle.getFirstTextView();
    }

    /**
     * Function used to return the weightedMapping associated with the view.
     *
     * @return weightedMappings.
     */
    public Collection<COREWeightedMapping> getWeightedMapping() {
        return this.scrollableListComponent.getElementMap().keySet();
    }

    @Override
    public void changeColor(MTColor color) {
        this.expandableComponent.getFirstLine().setNoFill(false);
        this.expandableComponent.getFirstLine().setFillColor(color);
        this.expandableComponent.getHideableContainer().setNoFill(false);
        this.expandableComponent.getHideableContainer().setFillColor(color);

        for (RamRectangleComponent rectangle : this.scrollableListComponent.getElementMap().values()) {
            rectangle.setNoFill(false);
            rectangle.setFillColor(color);
        }
    }

    @Override
    public void changeColorToDefault() {
        this.expandableComponent.getFirstLine().setNoFill(false);
        this.expandableComponent.getFirstLine().setFillColor(Colors.DEFAULT_ELEMENT_FILL_COLOR);
        this.expandableComponent.getHideableContainer().setNoFill(false);
        this.expandableComponent.getHideableContainer().setFillColor(Colors.DEFAULT_ELEMENT_FILL_COLOR);

        for (RamRectangleComponent rectangle : this.scrollableListComponent.getElementMap().values()) {
            rectangle.setNoFill(false);
            rectangle.setFillColor(Colors.DEFAULT_ELEMENT_FILL_COLOR);
        }
    }

    @Override
    public void destroy() {
        EMFEditUtil.removeListenerFor(this.impactModelElement, this);

        super.destroy();
    }

    @Override
    public void setHandler(FeatureImpactViewHandler handler) {
        // Set the handlers for the TextView (Contribution) and WeightedMapping
        if (handler instanceof FeatureImpactViewEditHandler) {
            this.titleRectangle.getLastTextView().unregisterAllInputProcessors();
            this.titleRectangle.getLastTextView().registerTapProcessor(HandlerFactory.INSTANCE.getTextViewHandler());

            for (RamRectangleComponent rectangleComponent : scrollableListComponent.getElementMap().values()) {
                if (rectangleComponent instanceof TextViewContainerComponent) {
                    TextViewContainerComponent textViewContainerComponent =
                            (TextViewContainerComponent) rectangleComponent;

                    textViewContainerComponent.unregisterAllInputProcessors();
                    textViewContainerComponent.getLastTextView().registerTapProcessor(
                            HandlerFactory.INSTANCE.getTextViewHandler());
                }
            }

            scrollableListComponent.registerTapAndHoldProcessor();
            scrollableListComponent.registerListener(HandlerFactoryImpactModel.INSTANCE.getWeightedMappingHandler());
        }

        super.setHandler(handler);
    }
}
