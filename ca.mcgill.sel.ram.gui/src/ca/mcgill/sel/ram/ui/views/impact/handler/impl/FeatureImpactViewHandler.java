package ca.mcgill.sel.ram.ui.views.impact.handler.impl;

import org.mt4j.components.TransformSpace;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.core.controller.ControllerFactory;
import ca.mcgill.sel.core.controller.ImpactModelElementController;
import ca.mcgill.sel.ram.ui.views.impact.ImpactModelElementView;

/**
 * Handler used to handle all the events on the Feature representation in the Impact Model.
 *
 * @author Nishanth
 * @author Romain
 *
 */
public class FeatureImpactViewHandler extends ImpactModelElementViewHandler {

    @Override
    protected void handleDragEvent(DragEvent dragEvent,
            ImpactModelElementView<? extends ImpactModelElementViewHandler> view) {

        Vector3D endPosition = view.getPosition(TransformSpace.RELATIVE_TO_PARENT);

        // Create a MapLayout for the element
        ImpactModelElementController impactModelElementController =
                ControllerFactory.INSTANCE.getImpactModelElementController();
        impactModelElementController.moveImpactModelElement(view.getRootImpactModelElement(),
                view.getImpactModelElement(), endPosition.getX(), endPosition.getY());
    }
}
