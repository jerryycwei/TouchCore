package ca.mcgill.sel.ram.ui.views.impact.handler.impl;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.core.COREFeatureImpactNode;
import ca.mcgill.sel.core.COREImpactModel;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.core.COREReuse;
import ca.mcgill.sel.core.controller.ControllerFactory;
import ca.mcgill.sel.core.util.COREImpactModelUtil;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamPopup.PopupType;
import ca.mcgill.sel.ram.ui.components.RamSelectorComponent;
import ca.mcgill.sel.ram.ui.components.listeners.AbstractDefaultRamSelectorListener;
import ca.mcgill.sel.ram.ui.events.listeners.ITapAndHoldListener;
import ca.mcgill.sel.ram.ui.scenes.AbstractImpactScene;
import ca.mcgill.sel.ram.ui.utils.Strings;
import ca.mcgill.sel.ram.ui.views.OptionSelectorView;
import ca.mcgill.sel.ram.ui.views.impact.ImpactModelElementView;
import ca.mcgill.sel.ram.ui.views.impact.ReuseGoalSelectorView;
import ca.mcgill.sel.ram.ui.views.impact.ReuseGoalSelectorView.ReuseGoalListener;

/**
 * Handler used to handle all the events on the Feature representation in the Impact Model.
 *
 * @author Nishanth
 * @author Romain
 *
 */
public class FeatureImpactViewEditHandler extends FeatureImpactViewHandler implements ITapAndHoldListener {

    /**
     * The options to display when tap-and-hold is performed.
     */
    private enum FeatureImpactOption {
        DELETE_FROM_MODEL, DELETE_FROM_VIEW, ADD_MAPPING
    }

    @Override
    public boolean processTapAndHoldEvent(final TapAndHoldEvent tapAndHoldEvent) {
        if (tapAndHoldEvent.isHoldComplete()) {
            @SuppressWarnings("unchecked")
            final ImpactModelElementView<FeatureImpactViewEditHandler> target =
                    (ImpactModelElementView<FeatureImpactViewEditHandler>) tapAndHoldEvent.getTarget();

            final COREFeatureImpactNode featureImpact = (COREFeatureImpactNode) target.getImpactModelElement();

            // Get the reuse that have an impact model and some goals.
            final List<COREReuse> reuses = new ArrayList<COREReuse>();
            for (COREReuse reuse : featureImpact.getRepresents().getReuses()) {
                COREImpactModel reusedImpactModel = reuse.getReusedConcern().getImpactModel();
                if (reusedImpactModel != null && !reusedImpactModel.getImpactModelElements().isEmpty()) {
                    reuses.add(reuse);
                }
            }

            // Remove delete from the options if the target is the root.
            EnumSet<FeatureImpactOption> availableOptions = EnumSet.allOf(FeatureImpactOption.class);

            if (reuses.isEmpty()) {
                availableOptions.remove(FeatureImpactOption.ADD_MAPPING);
            }

            if (COREImpactModelUtil.shouldDeleteNodeFromModel(target.getRootImpactModelElement(),
                    target.getImpactModelElement())) {
                availableOptions.remove(FeatureImpactOption.DELETE_FROM_VIEW);
            } else {
                availableOptions.remove(FeatureImpactOption.DELETE_FROM_MODEL);
            }

            // Not nice but casting an Object[] into FeatureImpactOption[] somehow doesn't work.
            FeatureImpactOption[] options = availableOptions.toArray(new FeatureImpactOption[0]);

            // If the array is empty, we don't show this selector.
            if (options.length == 0) {
                return true;
            }

            OptionSelectorView<FeatureImpactOption> selector = new OptionSelectorView<FeatureImpactOption>(options);

            ((AbstractImpactScene) RamApp.getActiveScene())
                    .addComponent(selector, tapAndHoldEvent.getLocationOnScreen());

            selector.registerListener(new AbstractDefaultRamSelectorListener<FeatureImpactOption>() {

                @Override
                public void elementSelected(RamSelectorComponent<FeatureImpactOption> selector,
                        FeatureImpactOption element) {
                    switch (element) {
                        case DELETE_FROM_VIEW:
                            deleteElement(target, false);
                            break;
                        case DELETE_FROM_MODEL:
                            deleteElement(target, true);
                            break;
                        case ADD_MAPPING:
                            showReuseGoals(featureImpact, tapAndHoldEvent.getLocationOnScreen(), reuses);
                            break;
                    }
                }
            });
        }

        return true;
    }

    /**
     * Show a selector in order to allow the user to select a {@link COREReuse} and a {@link COREImpactNode}.
     *
     * @param featureImpact the {@link COREFeatureImpactNode} that will be the target of the mapping.
     * @param creationLocation the location where the selector will be created.
     * @param reuses the {@link COREReuse}s that we will be displayed.
     */
    private static void showReuseGoals(final COREFeatureImpactNode featureImpact, Vector3D creationLocation,
            List<COREReuse> reuses) {
        ReuseGoalSelectorView selector = new ReuseGoalSelectorView(reuses);

        ((AbstractImpactScene) RamApp.getActiveScene()).addComponent(selector, creationLocation);

        selector.registerListener(new ReuseGoalListener() {

            @Override
            public void selectorValid(COREReuse reuse, COREImpactNode impactModelElement) {
                addWeightedMapping(reuse, impactModelElement, featureImpact);
            }

            @Override
            public void selectorCancel() {
            }
        });
    }

    /**
     * Delete a COREImpactModelElement from the model.
     *
     * @param target the {@link ImpactModelElementView} that contains the COREImpactModelElement
     * @param shouldDeleteNodeFromModel if the element should be deleted from the model or not.
     */
    private static void deleteElement(ImpactModelElementView<FeatureImpactViewEditHandler> target,
            boolean shouldDeleteNodeFromModel) {
        ControllerFactory.INSTANCE.getFeatureImpactController().removeFeatureImpact(
                target.getRootImpactModelElement(), (COREFeatureImpactNode) target.getImpactModelElement(), false,
                shouldDeleteNodeFromModel);
    }

    /**
     * Create a new COREWeightedMapping between this {@link COREImpactNode} and a {@link COREFeatureImpactNode}.
     *
     * @param reuse The {@link COREReuse} of the concern that contains the source goal.
     * @param source the {@link COREImpactNode} that will be the "from" of this mapping.
     * @param target the {@link COREFeatureImpactNode} that will be the "to" of this mapping.
     */
    private static void addWeightedMapping(COREReuse reuse, COREImpactNode source, COREFeatureImpactNode target) {
        if (!COREImpactModelUtil.areAlreadyMappedTogether(source, target)) {
            ControllerFactory.INSTANCE.getFeatureImpactController().createWeightedMapping(reuse, source, target);
        } else {
            RamApp.getActiveScene().displayPopup(Strings.POPUP_ERROR_MAPPING_ALREADY_DEFINED, PopupType.ERROR);
        }
    }
}
