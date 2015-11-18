package ca.mcgill.sel.ram.ui.views.impact;

import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.core.LayoutElement;
import ca.mcgill.sel.ram.ui.components.RamRoundedRectangleComponent;
import ca.mcgill.sel.ram.ui.layouts.VerticalLayout;
import ca.mcgill.sel.ram.ui.utils.Colors;
import ca.mcgill.sel.ram.ui.views.TextView;
import ca.mcgill.sel.ram.ui.views.handler.HandlerFactory;
import ca.mcgill.sel.ram.ui.views.handler.IAbstractViewHandler;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.GoalImpactViewEditHandler;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.GoalImpactViewHandler;

/**
 * Class used to visually representing {@link COREImpactNode} in an Impact Model.
 *
 * @author Nishanth
 * @author Romain
 *
 */
public class GoalImpactView extends ImpactModelElementView<GoalImpactViewHandler> {

    /**
     * The {@link TextView} that contains the name of this {@link GoalImpactView}.
     */
    private TextView textView;

    /**
     * The {@link RamRoundedRectangleComponent} that contains the {@link TextView} of this {@link GoalImpactView}.
     */
    private RamRoundedRectangleComponent container;

    /**
     * Function used to create a visual representation of the goal.
     *
     * @param root The root {@link COREImpactNode} of this view
     * @param element The associated {@link COREImpactNode}
     * @param impactDiagramView the scene in which this view will be displayed
     */
    public GoalImpactView(COREImpactNode root, COREImpactNode element,
            ImpactDiagramView<? extends IAbstractViewHandler> impactDiagramView) {
        super(root, element, impactDiagramView);

        this.setNoFill(true);
        this.setNoStroke(true);

        container = new RamRoundedRectangleComponent(20);
        container.setBuffers(5);
        container.setLayout(new VerticalLayout());

        container.setNoStroke(false);
        container.setNoFill(false);
        container.setStrokeWeight(1f);
        container.setStrokeColor(Colors.DEFAULT_ELEMENT_STROKE_COLOR);
        container.setFillColor(Colors.IMPACT_GOAL_FILL_COLOR);

        textView = new TextView(impactModelElement, CorePackage.Literals.CORE_NAMED_ELEMENT__NAME);
        textView.setUniqueName(true);

        container.addChild(textView);

        this.addChild(container);
    }

    /**
     * Constructor to create the visual representation of goal based on the point clicked.
     *
     * @param root The root {@link COREImpactNode} of this view
     * @param centerPoint - The point where the goal is to be created.
     * @param element The associated {@link COREImpactNode}
     * @param impactDiagramView the scene in which this view will be displayed
     */
    public GoalImpactView(COREImpactNode root, Vector3D centerPoint, COREImpactNode element,
            ImpactDiagramView<? extends IAbstractViewHandler> impactDiagramView) {
        this(root, element, impactDiagramView);
        if (centerPoint != null) {
            this.setPositionRelativeToParent(centerPoint);
        }
    }

    /**
     * Constructor to create the visual representation of goal based on the point clicked.
     *
     * @param root The root {@link COREImpactNode} of this view
     * @param layoutElement - The point where the goal is to be created.
     * @param element The associated {@link COREImpactNode}
     * @param impactDiagramView the scene in which this view will be displayed
     */
    public GoalImpactView(COREImpactNode root, LayoutElement layoutElement, COREImpactNode element,
            ImpactDiagramView<? extends IAbstractViewHandler> impactDiagramView) {
        this(root, element, impactDiagramView);
        if (layoutElement != null) {
            this.setPositionRelativeToParent(new Vector3D(layoutElement.getX(), layoutElement.getY()));
        }
    }

    /**
     * Function used to return the textView associated with the view.
     *
     * @return textView.
     */
    public TextView getTextView() {
        return textView;
    }

    @Override
    public void changeColor(MTColor color) {
        this.container.setNoFill(false);
        this.container.setFillColor(color);
    }

    @Override
    public void changeColorToDefault() {
        this.container.setFillColor(Colors.IMPACT_GOAL_FILL_COLOR);
    }

    @Override
    public void setHandler(GoalImpactViewHandler handler) {
        if (handler instanceof GoalImpactViewEditHandler) {
            textView.registerTapProcessor(HandlerFactory.INSTANCE.getTextViewHandler());
        }

        super.setHandler(handler);
    }
}
