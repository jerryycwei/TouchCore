package ca.mcgill.sel.ram.ui.views.impact.handler.impl;

import org.mt4j.input.inputData.MTMouseInputEvt;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;

import ca.mcgill.sel.core.controller.ControllerFactory;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamSelectorComponent;
import ca.mcgill.sel.ram.ui.components.listeners.AbstractDefaultRamSelectorListener;
import ca.mcgill.sel.ram.ui.events.listeners.IDragListener;
import ca.mcgill.sel.ram.ui.scenes.AbstractImpactScene;
import ca.mcgill.sel.ram.ui.views.OptionSelectorView;
import ca.mcgill.sel.ram.ui.views.RamEnd;
import ca.mcgill.sel.ram.ui.views.TextView;
import ca.mcgill.sel.ram.ui.views.handler.BaseHandler;
import ca.mcgill.sel.ram.ui.views.handler.IRelationshipViewHandler;
import ca.mcgill.sel.ram.ui.views.impact.ContributionView;
import ca.mcgill.sel.ram.ui.views.impact.ContributionView.TextViewContainer;

/**
 * Handler to handle changes to the contribution value.
 *
 * @author Nishanth
 * @author Romain
 *
 */
public class ContributionViewHandler extends BaseHandler implements IDragListener, IRelationshipViewHandler {

    /**
     * The options to display when tap-and-hold is performed.
     */
    private enum OPTION {
        DELETE
    }

    @Override
    public boolean processDoubleTap(TapEvent tapEvent, RamEnd<?, ?> end) {
        return false;
    }

    @Override
    public boolean processTapAndHold(TapAndHoldEvent tapAndHoldEvent, RamEnd<?, ?> end) {
        if (tapAndHoldEvent.isHoldComplete()) {
            final ContributionView contributionView = (ContributionView) end.getRelationshipView();
            OptionSelectorView<OPTION> selector = new OptionSelectorView<OPTION>(OPTION.values());

            ((AbstractImpactScene) RamApp.getActiveScene())
                    .addComponent(selector, tapAndHoldEvent.getLocationOnScreen());

            selector.registerListener(new AbstractDefaultRamSelectorListener<OPTION>() {

                @Override
                public void elementSelected(RamSelectorComponent<OPTION> selector, OPTION element) {
                    switch (element) {
                        case DELETE:
                            ControllerFactory.INSTANCE.getContributionController().removeContribution(
                                    contributionView.getContribution());
                            break;
                    }
                }
            });
        }

        return true;
    }

    @Override
    public boolean processDragEvent(DragEvent dragEvent) {
        TextViewContainer textViewContainer = (TextViewContainer) dragEvent.getTarget();

        ContributionView contributionView = textViewContainer.getContribution();
        TextView textView = contributionView.getTextView();
        int currentWeight = Integer.valueOf(textView.getText());
        switch (dragEvent.getId()) {
            case MTGestureEvent.GESTURE_UPDATED:
                float velocityVectorY = dragEvent.getDragCursor().getVelocityVector().y;
                float translationVectorY = dragEvent.getTranslationVect().y;

                boolean mtFingerInputCheck = !(dragEvent.getDragCursor().getCurrentEvent() instanceof MTMouseInputEvt)
                        && Math.abs(translationVectorY) < 2;
                boolean mtMouseInputCheck = dragEvent.getDragCursor().getCurrentEvent() instanceof MTMouseInputEvt
                        && Math.abs(translationVectorY) < 1;

                if (mtFingerInputCheck || mtMouseInputCheck) {
                    return true;
                }

                int weightToSubstract = (int) Math.signum(velocityVectorY);

                textView.setText(String.valueOf(currentWeight - weightToSubstract));
                break;
            case MTGestureEvent.GESTURE_ENDED:
                ControllerFactory.INSTANCE.getContributionController().setContributionWeight(
                        contributionView.getContribution(), currentWeight);
                break;
        }
        return false;
    }
}
