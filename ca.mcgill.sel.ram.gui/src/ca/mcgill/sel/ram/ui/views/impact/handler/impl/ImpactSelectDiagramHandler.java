package ca.mcgill.sel.ram.ui.views.impact.handler.impl;

import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeEvent;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils.UnistrokeGesture;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.ram.ui.views.AbstractView;
import ca.mcgill.sel.ram.ui.views.handler.impl.AbstractViewHandler;

/**
 * Class used to handle events on the Background layer of the Impact Model.
 *
 * @author Romain
 *
 */
public class ImpactSelectDiagramHandler extends AbstractViewHandler {

    @Override
    public void handleUnistrokeGesture(AbstractView<?> target, UnistrokeGesture gesture, Vector3D startPosition,
            UnistrokeEvent event) {
        // Do nothing
    }

}
