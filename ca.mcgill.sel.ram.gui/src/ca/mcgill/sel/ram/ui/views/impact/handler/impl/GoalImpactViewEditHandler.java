package ca.mcgill.sel.ram.ui.views.impact.handler.impl;

import java.util.EnumSet;

import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;

import ca.mcgill.sel.core.controller.ControllerFactory;
import ca.mcgill.sel.core.util.COREImpactModelUtil;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamSelectorComponent;
import ca.mcgill.sel.ram.ui.components.listeners.AbstractDefaultRamSelectorListener;
import ca.mcgill.sel.ram.ui.events.listeners.ITapAndHoldListener;
import ca.mcgill.sel.ram.ui.scenes.AbstractImpactScene;
import ca.mcgill.sel.ram.ui.views.OptionSelectorView;
import ca.mcgill.sel.ram.ui.views.impact.ImpactModelElementView;

/**
 * Handler used to handle events on goal displays.
 *
 * @author Nishanth
 * @author Romain
 *
 */
public class GoalImpactViewEditHandler extends GoalImpactViewHandler implements ITapAndHoldListener {

    /**
     * The options to display when tap-and-hold is performed.
     */
    private enum GoalOption {
        DELETE_FROM_MODEL, DELETE_FROM_VIEW, DELETE_WITH_CHILDREN
    }

    @Override
    public boolean processTapAndHoldEvent(final TapAndHoldEvent tapAndHoldEvent) {
        if (tapAndHoldEvent.isHoldComplete()) {
            @SuppressWarnings("unchecked")
            final ImpactModelElementView<GoalImpactViewEditHandler> target =
                    (ImpactModelElementView<GoalImpactViewEditHandler>) tapAndHoldEvent.getTarget();

            // Remove delete from the options if the target is the root.
            EnumSet<GoalOption> availableOptions = EnumSet.allOf(GoalOption.class);

            if (target.isRootImpactModelElement()) {
                availableOptions.remove(GoalOption.DELETE_FROM_MODEL);
                availableOptions.remove(GoalOption.DELETE_FROM_VIEW);
                availableOptions.remove(GoalOption.DELETE_WITH_CHILDREN);
            } else {
                if (COREImpactModelUtil.shouldDeleteNodeFromModel(target.getRootImpactModelElement(),
                        target.getImpactModelElement())) {
                    availableOptions.remove(GoalOption.DELETE_FROM_VIEW);
                } else {
                    availableOptions.remove(GoalOption.DELETE_FROM_MODEL);
                }
            }

            // Not nice but casting an Object[] into GoalOption[] somehow doesn't work.
            GoalOption[] options = availableOptions.toArray(new GoalOption[0]);

            // If the array is empty, we don't show this selector.
            if (options.length == 0) {
                return true;
            }

            OptionSelectorView<GoalOption> selector = new OptionSelectorView<GoalOption>(options);

            ((AbstractImpactScene) RamApp.getActiveScene())
                    .addComponent(selector, tapAndHoldEvent.getLocationOnScreen());

            selector.registerListener(new AbstractDefaultRamSelectorListener<GoalOption>() {
                @Override
                public void elementSelected(RamSelectorComponent<GoalOption> selector, GoalOption element) {
                    switch (element) {
                        case DELETE_FROM_MODEL:
                            deleteElement(target, false, true);
                            break;
                        case DELETE_FROM_VIEW:
                            deleteElement(target, false, false);
                            break;
                        case DELETE_WITH_CHILDREN:
                            deleteElement(target, true);
                            break;
                    }
                }
            });
        }
        return true;
    }

    /**
     * Delete a {@link ca.mcgill.sel.core.COREImpactNode} from the model.
     *
     * @param target the {@link ImpactModelElementView} that contains the {@link ca.mcgill.sel.core.COREImpactNode}
     * @param deleteChildren if the children has to be deleted or not
     * @param shouldDeleteNodeFromModel if the element should be deleted from the model or not.
     */
    private static void deleteElement(ImpactModelElementView<GoalImpactViewEditHandler> target,
            boolean deleteChildren, boolean shouldDeleteNodeFromModel) {
        ControllerFactory.INSTANCE.getImpactModelElementController().removeImpactModelElement(
                target.getRootImpactModelElement(), target.getImpactModelElement(), deleteChildren,
                shouldDeleteNodeFromModel);
    }

    /**
     * Delete a {@link ca.mcgill.sel.core.COREImpactNode} from the model.
     *
     * @param target the {@link ImpactModelElementView} that contains the {@link ca.mcgill.sel.core.COREImpactNode}
     * @param deleteChildren if the children has to be deleted or not
     */
    private static void deleteElement(ImpactModelElementView<GoalImpactViewEditHandler> target,
            boolean deleteChildren) {
        ControllerFactory.INSTANCE.getImpactModelElementController().removeImpactModelElement(
                target.getRootImpactModelElement(), target.getImpactModelElement(), deleteChildren);
    }
}
