package ca.mcgill.sel.ram.ui.views.impact.handler.impl;

import java.util.HashSet;
import java.util.Set;

import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.ram.ui.events.DelayedDrag;
import ca.mcgill.sel.ram.ui.events.listeners.IDragListener;
import ca.mcgill.sel.ram.ui.views.handler.BaseHandler;
import ca.mcgill.sel.ram.ui.views.impact.ContributionView;
import ca.mcgill.sel.ram.ui.views.impact.ImpactModelElementView;

/**
 * Handler used to handle all the events on a view in the Impact Model.
 *
 * @author Romain
 *
 */
public abstract class ImpactModelElementViewHandler extends BaseHandler implements IDragListener {

    /**
     * The minimum distance an object has to be dragged before it actually is dragged.
     */
    private static final float MIN_DRAG_DISTANCE = 2f;

    private DelayedDrag dragAction = new DelayedDrag(MIN_DRAG_DISTANCE);

    @Override
    public boolean processDragEvent(DragEvent dragEvent) {
        @SuppressWarnings("unchecked")
        ImpactModelElementView<ImpactModelElementViewHandler> view =
                (ImpactModelElementView<ImpactModelElementViewHandler>) dragEvent.getTarget();

        switch (dragEvent.getId()) {
            case MTGestureEvent.GESTURE_STARTED:
                // Drag the element itself
                dragAction.processGestureEvent(dragEvent);
                break;
            case MTGestureEvent.GESTURE_UPDATED:
                // Drag the element itself
                dragAction.processGestureEvent(dragEvent);

                // Drag the children
                if (dragAction.wasDragPerformed()) {
                    Vector3D localTranslationVector = dragEvent.getTranslationVect();

                    Set<ImpactModelElementView<? extends ImpactModelElementViewHandler>> elementsAlreadyDrag =
                            new HashSet<ImpactModelElementView<? extends ImpactModelElementViewHandler>>();
                    elementsAlreadyDrag.add(view);

                    for (ContributionView link : view.getIncoming()) {
                        if (!elementsAlreadyDrag.contains(link.getSource())) {
                            translateChild(link.getSource(), localTranslationVector, elementsAlreadyDrag);
                        }
                    }
                }

                break;
            case MTGestureEvent.GESTURE_CANCELED:
                break;
            case MTGestureEvent.GESTURE_ENDED:
                if (dragAction.wasDragPerformed()) {
                    this.handleDragEvent(dragEvent, view);
                }
                break;
        }

        return false;
    }

    /**
     * Translate the position of the children and recursively according to this vector. If the children of this
     * {@link ImpactModelElementView} are already in the Set "elementsAlreadyDrag", it will not call this operation on
     * it.
     *
     * @param impactModelElementView the {@link ImpactModelElementView} to translate
     * @param localTranslationVector the translation vector
     * @param elementsAlreadyDrag the set of elements that have been already dragged.
     */
    private void translateChild(ImpactModelElementView<? extends ImpactModelElementViewHandler> impactModelElementView,
            Vector3D localTranslationVector,
            Set<ImpactModelElementView<? extends ImpactModelElementViewHandler>> elementsAlreadyDrag) {

        // Drag the element itself
        impactModelElementView.translateGlobal(new Vector3D(localTranslationVector));
        elementsAlreadyDrag.add(impactModelElementView);

        // Drag the children
        for (ContributionView link : impactModelElementView.getIncoming()) {
            if (!elementsAlreadyDrag.contains(link.getSource())) {
                translateChild(link.getSource(), localTranslationVector, elementsAlreadyDrag);
            }
        }
    }

    /**
     * Handle the {@link DragEvent} that occurs of this view.
     *
     * @param dragEvent the {@link DragEvent}
     * @param view the target view
     */
    protected abstract void handleDragEvent(DragEvent dragEvent,
            ImpactModelElementView<? extends ImpactModelElementViewHandler> view);

}
