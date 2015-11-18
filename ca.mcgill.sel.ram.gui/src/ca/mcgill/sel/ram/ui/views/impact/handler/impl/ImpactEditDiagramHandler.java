package ca.mcgill.sel.ram.ui.views.impact.handler.impl;

import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeEvent;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils.UnistrokeGesture;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.core.COREFeatureImpactNode;
import ca.mcgill.sel.core.COREImpactModel;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.core.controller.ControllerFactory;
import ca.mcgill.sel.core.controller.ImpactModelElementController;
import ca.mcgill.sel.core.util.COREImpactModelUtil;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamPopup.PopupType;
import ca.mcgill.sel.ram.ui.components.RamSelectorComponent;
import ca.mcgill.sel.ram.ui.components.listeners.AbstractDefaultRamSelectorListener;
import ca.mcgill.sel.ram.ui.events.listeners.ITapAndHoldListener;
import ca.mcgill.sel.ram.ui.scenes.AbstractImpactScene;
import ca.mcgill.sel.ram.ui.utils.Constants;
import ca.mcgill.sel.ram.ui.utils.ImpactModelUtil;
import ca.mcgill.sel.ram.ui.utils.MathUtils;
import ca.mcgill.sel.ram.ui.utils.Strings;
import ca.mcgill.sel.ram.ui.utils.UnistrokeProcessorUtils;
import ca.mcgill.sel.ram.ui.views.AbstractView;
import ca.mcgill.sel.ram.ui.views.OptionSelectorView;
import ca.mcgill.sel.ram.ui.views.handler.impl.AbstractViewHandler;
import ca.mcgill.sel.ram.ui.views.impact.ImpactEditDiagramView;
import ca.mcgill.sel.ram.ui.views.impact.ImpactModelElementView;

/**
 * Class used to handle events on the Background layer of the Impact Model.
 *
 * @author Romain
 *
 */
public class ImpactEditDiagramHandler extends AbstractViewHandler implements ITapAndHoldListener {

    /**
     * Values which are to be displayed.
     */
    private enum Values {
        Create_Goal
    }

    /**
     * Function called to create a Goal Model element on the scene.
     *
     * @param impactEditDiagramView the view where the goal should be added
     * @param locationOnScreen - The location where the goal should be created.
     */
    private static void createGoal(ImpactEditDiagramView impactEditDiagramView, Vector3D locationOnScreen) {
        COREImpactModel impactModel = impactEditDiagramView.getImpactModel();
        COREImpactNode root = impactEditDiagramView.getRootImpactModelElement();

        ImpactModelElementController controller = ControllerFactory.INSTANCE.getImpactModelElementController();
        controller.createImpactModelElement(impactModel, root, locationOnScreen.x, locationOnScreen.y,
                ImpactModelUtil.getUniqueGoalName(impactModel));
    }

    /**
     * Function called to check if an association can be added between a goal and goal at a certain position.
     *
     * @param impactEditDiagramView the view where the link should be added
     * @param source - The from {@link COREImpactNode}
     * @param target - The to {@link COREImpactNode}
     */
    private static void createContribution(ImpactEditDiagramView impactEditDiagramView, COREImpactNode source,
            COREImpactNode target) {
        COREImpactModel impactModel = impactEditDiagramView.getImpactModel();
        if (source == impactEditDiagramView.getRootImpactModelElement()) {
            RamApp.getActiveScene().displayPopup(Strings.POPUP_ERROR_ROOT_GOAL_OUTGOING,
                    PopupType.ERROR);
            return;
        }

        if (source != target && !(target instanceof COREFeatureImpactNode)
                && COREImpactModelUtil.canCreateContribution(source, target)) {
            ControllerFactory.INSTANCE.getContributionController().createContribution(impactModel, source, target);
        }
    }

    @Override
    public boolean processTapAndHoldEvent(final TapAndHoldEvent tapAndHoldEvent) {
        if (tapAndHoldEvent.isHoldComplete()) {
            final ImpactEditDiagramView impactEditDiagramView = (ImpactEditDiagramView) tapAndHoldEvent.getTarget();

            OptionSelectorView<Values> selector = new OptionSelectorView<Values>(Values.values());
            ((AbstractImpactScene) RamApp.getActiveScene())
                    .addComponent(selector, tapAndHoldEvent.getLocationOnScreen());

            selector.registerListener(new AbstractDefaultRamSelectorListener<Values>() {

                @Override
                public void elementSelected(RamSelectorComponent<Values> selector, Values element) {
                    switch (element) {
                        case Create_Goal:
                            createGoal(impactEditDiagramView, tapAndHoldEvent.getLocationOnScreen());
                            break;
                    }
                }
            });
        }
        return true;
    }

    @Override
    public void handleUnistrokeGesture(AbstractView<?> target, UnistrokeGesture gesture, Vector3D startPosition,
            UnistrokeEvent event) {
        ImpactEditDiagramView imDiagramView = (ImpactEditDiagramView) target;

        switch (event.getId()) {
            case MTGestureEvent.GESTURE_ENDED:
                Vector3D endPosition = event.getCursor().getPosition();
                COREImpactNode startElement = null;
                COREImpactNode endElement = null;

                for (ImpactModelElementView<? extends ImpactModelElementViewHandler> imModelElementView : imDiagramView
                        .getImpactModelElementsView()) {
                    if (MathUtils.pointIsInRectangle(startPosition, imModelElementView,
                            Constants.MARGIN_ELEMENT_DETECTION)) {
                        startElement = imModelElementView.getImpactModelElement();
                    }
                    if (imModelElementView.containsPointGlobal(endPosition)) {
                        endElement = imModelElementView.getImpactModelElement();
                    }
                    if (startElement != null && endElement != null) {
                        createContribution(imDiagramView, startElement, endElement);
                        return;
                    }
                }
                break;
        }

        switch (gesture) {
            case CIRCLE:
                createGoal(imDiagramView, startPosition);
                break;
            case X:
                Vector3D endPosition = event.getCursor().getPosition();
                deleteElement(imDiagramView, startPosition, endPosition, event.getCursor());
                break;
            default:
                break;
        }
    }

    /**
     * This will check to see if the delete(x) gesture is done over a {@link ImpactModelElementView}. If yes it will
     * delete the {@link COREImpactNode} associated but not its children. Determining if the gesture was done
     * over a {@link ImpactModelElementView} is by comparing the x and y values of the start ] and end position of the
     * mouse cursor with the position of the {@link ImpactModelElementView}.
     *
     * @param imDiagramView this represent the {@link ImpactEditDiagramView} in which the gesture was performed on
     * @param startPosition The position of the cursor when the gesture was started
     * @param endPosition The position of the cursor when the gesture was ended
     * @param inputCursor the input cursor of the event
     */
    private static void deleteElement(ImpactEditDiagramView imDiagramView, Vector3D startPosition,
            Vector3D endPosition, InputCursor inputCursor) {
        Vector3D intersection = UnistrokeProcessorUtils.getIntersectionPoint(startPosition, endPosition, inputCursor);

        float intersectionX = intersection.x;
        float intersectionY = intersection.y;

        for (ImpactModelElementView<? extends ImpactModelElementViewHandler> impactModelElementView : imDiagramView
                .getImpactModelElementsView()) {
            float left = impactModelElementView.getX();
            float upper = impactModelElementView.getY();
            float right = left + impactModelElementView.getWidth();
            float lower = upper + impactModelElementView.getHeight();

            if (left < intersectionX && right > intersectionX && upper < intersectionY && lower > intersectionY) {
                ControllerFactory.INSTANCE.getImpactModelElementController().removeImpactModelElement(
                        imDiagramView.getRootImpactModelElement(), impactModelElementView.getImpactModelElement(),
                        false);
                break;
            }
        }
    }
}
