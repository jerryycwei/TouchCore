package ca.mcgill.sel.ram.ui.views.ucm.handler.impl;

import org.mt4j.components.TransformSpace;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.ram.ui.events.DelayedDrag;
import ca.mcgill.sel.ram.ui.events.listeners.IDragListener;
import ca.mcgill.sel.ram.ui.views.handler.BaseHandler;
import ca.mcgill.sel.ram.ui.views.ucm.PathNodeView;
import ca.mcgill.sel.ucm.controller.PathNodeController;

/**
 * The Handler for all {@link PathNodeView}.
 *
 * @author jerrywei
 *
 */
public class PathNodeViewHandler extends BaseHandler implements IDragListener {
    /**
     * The minimum distance an object has to be dragged before it actually is dragged.
     */
    private static final float MIN_DRAG_DISTANCE = 2f;

    private DelayedDrag dragAction = new DelayedDrag(MIN_DRAG_DISTANCE);

    @Override
    public boolean processDragEvent(DragEvent dragEvent) {

        PathNodeView pnView =
                (PathNodeView) dragEvent.getTarget();

        if (!pnView.containsPointGlobal(dragEvent.getFrom())) {
            return false;
        }

        switch (dragEvent.getId()) {
            case MTGestureEvent.GESTURE_STARTED:
                // Drag the element itself
                dragAction.processGestureEvent(dragEvent);
                break;
            case MTGestureEvent.GESTURE_UPDATED:
                // Drag the element itself
                dragAction.processGestureEvent(dragEvent);

                // Update the node connections
                if (dragAction.wasDragPerformed()) {
                    pnView.updateNodeConnectionsPositions();
                }
                break;
            case MTGestureEvent.GESTURE_CANCELED:
                break;
            case MTGestureEvent.GESTURE_ENDED:
                if (dragAction.wasDragPerformed()) {
                    this.handleDragEvent(dragEvent, pnView);
                }
                break;
        }
        return false;
    }

    /**
     * Handle the drag event and change the layout element of this view.
     *
     * @param dragEvent The drag event that occurred
     * @param pnView The path node view that changed position
     */
    protected void handleDragEvent(DragEvent dragEvent, PathNodeView pnView) {

        Vector3D endPosition = pnView.getPosition(TransformSpace.RELATIVE_TO_PARENT);

        System.out.println(endPosition);

        PathNodeController pnController =
                ca.mcgill.sel.ucm.controller.ControllerFactory.INSTANCE.getPathNodeController();

        pnController.movePathNode(pnView.getPathNode(), endPosition.getX(), endPosition.getY());
    }
}
