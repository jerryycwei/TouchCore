package ca.mcgill.sel.ram.ui.views.ucm;

import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamRectangleComponent;
import ca.mcgill.sel.ram.ui.utils.Constants;
import ca.mcgill.sel.ram.ui.views.RamEnd.Position;
import ca.mcgill.sel.ram.ui.views.RelationshipView;
import ca.mcgill.sel.ucm.NodeConnection;
import ca.mcgill.sel.ucm.PathNode;

/**
 * The view class representing a Node Connection.
 *
 * @author jerrywei
 *
 */
public class NodeConnectionView extends RelationshipView<PathNode, PathNodeView> {

    /**
     * The {@link NodeConnection} that this view represents.
     */
    protected NodeConnection nodeConnection;

    /**
     * A float keeping the previous degrees a path node is rotated by.
     */
    protected float prevDegrees;

    /**
     *
     * The constructor to create a NodeConnectionView.
     *
     * @param nc The {@link NodeConnection} that this view represents
     * @param from The {@link PathNode} that this {@link NodeConnectionView} is successor to
     * @param fromView The {@link PathNodeView} representing the from
     * @param to The {@link PathNode} that this {@link NodeConnectionView} is predecessor to
     * @param toView The {@link PathNodeView} representing the to
     */
    public NodeConnectionView(NodeConnection nc, PathNode from, PathNodeView fromView, PathNode to,
            PathNodeView toView) {
        super(from, fromView, to, toView);
        nodeConnection = nc;
        fromView.addOutgoing(this);
        toView.addIncoming(this);

        float toX = toEnd.getLocation().getX();
        float toY = toEnd.getLocation().getY();

        prevDegrees = 0;

        RamRectangleComponent viewTo = toEnd.getComponentView();

        if (viewTo instanceof EndPointView) {
            EndPointView epView = (EndPointView) viewTo;
            rotateShape(epView, toX, toY, toEnd.getPosition());
        }


    }

    @Override
    public void update() {
        float fromX = fromEnd.getLocation().getX();
        float fromY = fromEnd.getLocation().getY();
        float toX = toEnd.getLocation().getX();
        float toY = toEnd.getLocation().getY();

        RamRectangleComponent viewTo = toEnd.getComponentView();

        if (viewTo instanceof EndPointView) {
            rotateShape(viewTo, toX, toY, toEnd.getPosition());
        }

        this.drawLine(fromX, fromY, null, toX, toY);

    }

    @Override
    protected void rotateShape(AbstractShape shape, float x, float y, Position position) {
        Vector3D rotationPoint = new Vector3D(x, y, 1);

        float fromX = fromEnd.getLocation().getX();
        float fromY = fromEnd.getLocation().getY();

        float deltaX = fromX - x;
        float deltaY = fromY - y;
        float degrees = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));

        shape.rotateZ(rotationPoint, degrees - prevDegrees);
        prevDegrees = degrees;
    }

    /**
     * Gets the node connection for this view.
     *
     * @return the node connection.
     */
    public NodeConnection getNodeConnection() {
        return nodeConnection;
    }

    @Override
    protected void registerInputProcessors() {
        registerInputProcessor(new TapProcessor(RamApp.getApplication(), Constants.TAP_MAX_FINGER_UP, true,
                Constants.TAP_DOUBLE_TAP_TIME));
        registerInputProcessor(new TapAndHoldProcessor(RamApp.getApplication(), Constants.TAP_AND_HOLD_DURATION));
        addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(RamApp.getApplication(), this));

    }

}
