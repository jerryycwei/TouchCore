package ca.mcgill.sel.ram.ui.views.impact.handler.impl;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;

import org.mt4j.components.TransformSpace;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.core.controller.ControllerFactory;
import ca.mcgill.sel.core.controller.ImpactModelElementController;
import ca.mcgill.sel.ram.ui.views.impact.ContributionView;
import ca.mcgill.sel.ram.ui.views.impact.ImpactModelElementView;

/**
 * Handler used to handle events on goal displays.
 *
 * @author Nishanth
 * @author Romain
 *
 */
public class GoalImpactViewHandler extends ImpactModelElementViewHandler {

    @Override
    protected void handleDragEvent(DragEvent dragEvent,
            ImpactModelElementView<? extends ImpactModelElementViewHandler> view) {
        Map<COREImpactNode, SimpleEntry<Float, Float>> elementMap =
                new HashMap<COREImpactNode, SimpleEntry<Float, Float>>();
        getDescendantsNewPositions(elementMap, view);

        // Create a MapLayout for the root element
        ImpactModelElementController impactModelElementController =
                ControllerFactory.INSTANCE.getImpactModelElementController();
        impactModelElementController.moveImpactModelElements(view.getRootImpactModelElement(),
                elementMap);
    }

    /**
     * Called at the end of a drag in order to update the {@link LayoutElement}.
     *
     * @param impactModelElementView the view that has been updated.
     * @param elementMap a set that contains the descendants of this {@link ImpactModelElementView}
     */
    private void getDescendantsNewPositions(
            Map<COREImpactNode, AbstractMap.SimpleEntry<Float, Float>> elementMap,
            ImpactModelElementView<? extends ImpactModelElementViewHandler> impactModelElementView) {
        if (!elementMap.containsKey(impactModelElementView.getImpactModelElement())) {
            Vector3D position = impactModelElementView.getPosition(TransformSpace.RELATIVE_TO_PARENT);
            SimpleEntry<Float, Float> positionEntry = new SimpleEntry<Float, Float>(position.x, position.y);

            elementMap.put(impactModelElementView.getImpactModelElement(), positionEntry);

            for (ContributionView link : impactModelElementView.getIncoming()) {
                getDescendantsNewPositions(elementMap, link.getSource());
            }
        }
    }
}
