package ca.mcgill.sel.ram.ui.views.impact;

import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.core.COREContribution;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamRectangleComponent;
import ca.mcgill.sel.ram.ui.layouts.HorizontalLayout;
import ca.mcgill.sel.ram.ui.views.RamEnd.Position;
import ca.mcgill.sel.ram.ui.views.RelationshipView;
import ca.mcgill.sel.ram.ui.views.TextView;
import ca.mcgill.sel.ram.ui.views.handler.HandlerFactory;
import ca.mcgill.sel.ram.ui.views.handler.IRelationshipViewHandler;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.ImpactModelElementViewHandler;

/**
 * Class used to represent the links between two goals or between goal and a feature.
 *
 * @author Nishanth
 * @author Romain
 *
 */
public class ContributionView extends
        RelationshipView<COREImpactNode, ImpactModelElementView<? extends ImpactModelElementViewHandler>> {

    private TextViewContainer textViewContainer;

    private COREContribution contribution;

    /**
     * Class used to represent the contribution weight.
     */
    public class TextViewContainer extends RamRectangleComponent {

        private static final float BUFFER_SIZE = 25f;

        private TextView textView;

        /**
         * The constructor used to represent the contribution.
         */
        public TextViewContainer() {
            super();

            setNoFill(true);
            setNoStroke(true);

            setBufferSize(Cardinal.EAST, TextViewContainer.BUFFER_SIZE);
            setBufferSize(Cardinal.WEST, TextViewContainer.BUFFER_SIZE);

            setLayout(new HorizontalLayout());

            textView = new TextView(contribution, CorePackage.Literals.CORE_CONTRIBUTION__RELATIVE_WEIGHT) {
                @Override
                public void showKeyboard() {
                    super.showKeyboard(this);
                    getKeyboard().setSymbolsState(true);
                }
            };

            addChild(textView);
        }

        /**
         * Retrieve the {@link ContributionView} that contains this container.
         *
         * @return the {@link ContributionView} that contains this container.
         */
        public ContributionView getContribution() {
            return ContributionView.this;
        }

    }

    /**
     * The constructor when a link has to be added between a feature and a goal.
     *
     * @param impactModelElementSource - The feature for which the link has to be added.
     * @param impactModelElementTarget - The goal for which the link is added to.
     * @param contribution - The contribution value.
     */
    public ContributionView(ImpactModelElementView<? extends ImpactModelElementViewHandler> impactModelElementSource,
            ImpactModelElementView<? extends ImpactModelElementViewHandler> impactModelElementTarget,
            COREContribution contribution) {
        super(impactModelElementSource.impactModelElement, impactModelElementSource,
                impactModelElementTarget.impactModelElement, impactModelElementTarget);

        this.setContribution(contribution);

        textViewContainer = new TextViewContainer();
        addChild(textViewContainer);

        impactModelElementSource.addOutgoing(this);
        impactModelElementTarget.addIncoming(this);
    }

    /**
     * Get the source of this link.
     *
     * @return the {@link ImpactModelElementView} that is the source of this link
     */
    public ImpactModelElementView<? extends ImpactModelElementViewHandler> getSource() {
        return this.getFromEnd().getComponentView();
    }

    /**
     * Get the target of this link.
     *
     * @return the {@link ImpactModelElementView} that is the target of this link
     */
    public ImpactModelElementView<? extends ImpactModelElementViewHandler> getTarget() {
        return this.getToEnd().getComponentView();
    }

    @Override
    public void update() {
        float fromX = fromEnd.getLocation().getX();
        float fromY = fromEnd.getLocation().getY();
        float toX = toEnd.getLocation().getX();
        float toY = toEnd.getLocation().getY();

        this.drawLine(fromX, fromY, null, toX, toY);

        MTPolygon polygon = new ArrowPolygon(toX, toY, drawColor);
        rotateShape(polygon, toX, toY, toEnd.getPosition());
        addChild(polygon);

        float componentToX = toEnd.getComponentView().getCenterPointRelativeToParent().getX();
        float componentToY = toEnd.getComponentView().getCenterPointRelativeToParent().getY();

        PositionAnchor newAnchor;

        if ((fromX <= componentToX)
                && (fromY < componentToY)) {
            newAnchor = PositionAnchor.UPPER_RIGHT;
        } else if ((fromX >= componentToX)
                && (fromY < componentToY)) {
            newAnchor = PositionAnchor.UPPER_LEFT;
        } else if ((fromX < componentToX)
                && (fromY >= componentToY)) {
            newAnchor = PositionAnchor.LOWER_RIGHT;
        } else {
            newAnchor = PositionAnchor.LOWER_LEFT;
        }

        if (newAnchor != textViewContainer.getAnchor()) {
            textViewContainer.setAnchor(newAnchor);
        }

        float displayRectangleX = (fromX + toX) / 2;
        float displayRectangleY = (fromY + toY) / 2;

        float offset = (newAnchor == PositionAnchor.UPPER_RIGHT || newAnchor == PositionAnchor.LOWER_RIGHT)
                ? TextViewContainer.BUFFER_SIZE
                : -TextViewContainer.BUFFER_SIZE;

        textViewContainer.setPositionRelativeToParent(new Vector3D(displayRectangleX + offset,
                displayRectangleY));
    }

    /**
     * Rotates the given shape at the given point depending on the position. We will calculate the degree using the
     * fromEnd element and then rotate.
     *
     * @param shape the shape to rotate
     * @param x the rotation X point
     * @param y the rotation Y point
     * @param position the edge where the shape needs to attach to
     */
    @Override
    protected void rotateShape(AbstractShape shape, float x, float y, Position position) {
        Vector3D rotationPoint = new Vector3D(x, y, 1);

        float fromX = fromEnd.getLocation().getX();
        float fromY = fromEnd.getLocation().getY();

        float deltaX = fromX - x;
        float deltaY = fromY - y;
        float degrees = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));
        shape.rotateZ(rotationPoint, degrees);
    }

    /**
     * Function used to return the contribution.
     *
     * @return the contribution of the link.
     */
    public COREContribution getContribution() {
        return contribution;
    }

    /**
     * Setter to set the contibution.
     *
     * @param contribution the contribution to set
     */
    public void setContribution(COREContribution contribution) {
        this.contribution = contribution;
    }

    @Override
    public void destroy() {
        // Remove it from the UI
        getSource().removeOutgoing(this);
        getTarget().removeIncoming(this);

        super.destroy();
    }

    @Override
    public void setHandler(IRelationshipViewHandler handler) {
        if (handler instanceof IGestureEventListener) {
            // Set handler of the innerRectangle
            DragProcessor dragProcessor = new DragProcessor(RamApp.getApplication());
            dragProcessor.setBubbledEventsEnabled(true);

            textViewContainer.unregisterAllInputProcessors();
            textViewContainer.registerInputProcessor(dragProcessor);
            textViewContainer.addGestureListener(DragProcessor.class, (IGestureEventListener) handler);
        }

        textViewContainer.textView.registerTapProcessor(HandlerFactory.INSTANCE.getTextViewHandler());

        super.setHandler(handler);
    }

    /**
     * Retrieve the {@link TextView} that represents the weight of this contribution.
     *
     * @return the {@link TextView} that represents the weight of this contribution
     */
    public TextView getTextView() {
        return textViewContainer.textView;
    }
}
