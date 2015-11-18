package ca.mcgill.sel.ram.ui.views.ucm.handler.impl;

import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeEvent;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils.UnistrokeGesture;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamPopup.PopupType;
import ca.mcgill.sel.ram.ui.components.RamSelectorComponent;
import ca.mcgill.sel.ram.ui.components.listeners.AbstractDefaultRamSelectorListener;
import ca.mcgill.sel.ram.ui.events.listeners.ITapAndHoldListener;
import ca.mcgill.sel.ram.ui.scenes.DisplayUCMScene;
import ca.mcgill.sel.ram.ui.utils.Constants;
import ca.mcgill.sel.ram.ui.utils.MathUtils;
import ca.mcgill.sel.ram.ui.utils.Strings;
import ca.mcgill.sel.ram.ui.views.AbstractView;
import ca.mcgill.sel.ram.ui.views.OptionSelectorView;
import ca.mcgill.sel.ram.ui.views.handler.impl.AbstractViewHandler;
import ca.mcgill.sel.ram.ui.views.ucm.PathNodeView;
import ca.mcgill.sel.ram.ui.views.ucm.UCMDiagramView;
import ca.mcgill.sel.ucm.EndPoint;
import ca.mcgill.sel.ucm.PathNode;
import ca.mcgill.sel.ucm.StartPoint;
import ca.mcgill.sel.ucm.UseCaseMap;
import ca.mcgill.sel.ucm.controller.ControllerFactory;
import ca.mcgill.sel.ucm.controller.EndPointController;
import ca.mcgill.sel.ucm.controller.ResponsibilityController;
import ca.mcgill.sel.ucm.controller.StartPointController;
import ca.mcgill.sel.ucm.util.UCMModelUtil;

/**
 * Class used to handle events of the UCM Model.
 *
 * @author jerrywei
 *
 */
public class UCMDiagramViewHandler extends AbstractViewHandler implements ITapAndHoldListener {

    /**
     * Values which are to be displayed by menu.
     */
    private enum Values {
        Create_StartPoint, Create_Responsibility, Create_EndPoint;
    }

    /**
     * Calls {@link StartPointController} to create a new start point.
     *
     * @param ucmDiagramView The diagram where the new path node view will be added.
     * @param locationOnScreen The location to add the view
     */
    private static void createStartPoint(UCMDiagramView ucmDiagramView, Vector3D locationOnScreen) {
        UseCaseMap ucm = ucmDiagramView.getUseCaseMap();

        StartPointController spController = ControllerFactory.INSTANCE.getStartPointController();
        String name = UCMModelUtil.getUniquePathNodeName(ucm);
        spController.createStartPoint(ucm, locationOnScreen.x, locationOnScreen.y, name);
    }

    /**
     * Calls {@link EndPointController} to create a new end point.
     *
     * @param ucmDiagramView The diagram where the new path node view will be added.
     * @param locationOnScreen The location to add the view
     */
    private static void createEndPoint(UCMDiagramView ucmDiagramView, Vector3D locationOnScreen) {
        UseCaseMap ucm = ucmDiagramView.getUseCaseMap();

        EndPointController epController = ControllerFactory.INSTANCE.getEndPointController();

        String name = UCMModelUtil.getUniquePathNodeName(ucm);

        epController.createEndPoint(ucm, locationOnScreen.x, locationOnScreen.y, name);

    }

    /**
     * Calls {@link ResponsibilityController} to create a new responsibility.
     *
     * @param ucmDiagramView The diagram where the new path node view will be added.
     * @param locationOnScreen The location to add the view
     */
    private static void createResponsibility(UCMDiagramView ucmDiagramView, Vector3D locationOnScreen) {
        UseCaseMap ucm = ucmDiagramView.getUseCaseMap();

        ResponsibilityController respController = ControllerFactory.INSTANCE.getResponsibilityController();
        String name = UCMModelUtil.getUniquePathNodeName(ucm);

        respController.createResponsibilityRef(ucm, locationOnScreen.x, locationOnScreen.y, name);

    }

    /**
     * Calls node connection controller to create a node connection between two path odes.
     *
     * @param ucmDiagramView The diagram where the new path node view will be added.
     * @param source The path node where the node connection will start
     * @param target The path node where the node connection will end
     */
    private static void createNodeConnection(UCMDiagramView ucmDiagramView, PathNode source,
            PathNode target) {
        UseCaseMap ucm = ucmDiagramView.getUseCaseMap();

        if (source != target) {
            ControllerFactory.INSTANCE.getNodeConnectionController().createNodeConnection(ucm, source, target);
        }
    }

    @Override
    public boolean processTapAndHoldEvent(final TapAndHoldEvent tapAndHoldEvent) {
        if (tapAndHoldEvent.isHoldComplete()) {
            final UCMDiagramView ucmDiagramView =
                    (UCMDiagramView) tapAndHoldEvent.getTarget();

            OptionSelectorView<Values> selector = new OptionSelectorView<Values>(Values.values());
            ((DisplayUCMScene) RamApp.getActiveScene())
                    .addComponent(selector, tapAndHoldEvent.getLocationOnScreen());

            selector.registerListener(new AbstractDefaultRamSelectorListener<Values>() {

                @Override
                public void elementSelected(RamSelectorComponent<Values> selector, Values element) {
                    switch (element) {
                        case Create_StartPoint:
                            createStartPoint(ucmDiagramView, tapAndHoldEvent.getLocationOnScreen());
                            break;
                        case Create_Responsibility:
                            createResponsibility(ucmDiagramView, tapAndHoldEvent.getLocationOnScreen());
                            break;
                        case Create_EndPoint:
                            createEndPoint(ucmDiagramView, tapAndHoldEvent.getLocationOnScreen());
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
        UCMDiagramView ucmDiagramView = (UCMDiagramView) target;

        switch (event.getId()) {
            case MTGestureEvent.GESTURE_ENDED:
                Vector3D endPosition = event.getCursor().getPosition();
                PathNode startNode = null;
                PathNode endNode = null;

                for (PathNodeView pnView : ucmDiagramView.getPathNodeViews()) {
                    if (MathUtils.pointIsInRectangle(startPosition, pnView, Constants.MARGIN_ELEMENT_DETECTION)) {
                        startNode = pnView.getPathNode();
                    }
                    if (pnView.containsPointGlobal(endPosition)) {
                        endNode = pnView.getPathNode();
                    }
                    if (startNode != null && endNode != null) {
                        orderPathNodes(ucmDiagramView, startNode, endNode);
                        return;
                    }
                }

                break;

        }

    }

    /**
     * Checks to see that no illegal operations are made while creating node connections. Ensures directionality of node
     * connection is correct, and that no node connections are created on path nodes which do not allow more than 1
     * incoming or 1 outgoing.
     *
     * @param ucmDiagramView The diagram where the new path node view will be added.
     * @param startNode The path node where the node connection will start
     * @param endNode The path node where the node connection will end
     */
    private static void orderPathNodes(UCMDiagramView ucmDiagramView, PathNode startNode,
            PathNode endNode) {

        PathNode sourcePN;
        PathNode targetPN;

        if ((endNode instanceof EndPoint || endNode instanceof StartPoint)
                && (startNode instanceof EndPoint || startNode instanceof StartPoint)) {
            RamApp.getActiveScene().displayPopup(
                    Strings.popupNodeConnectionProhibited(startNode, endNode),
                    PopupType.ERROR);
            return;
        }

        // If there is a startpoint, it will always be the source of any nodeconnection, and vice versa
        // for the EndPoint. If user unistrokes the "wrong" direction, the nodeConnection will be created the correct
        // way.
        if (endNode instanceof StartPoint || startNode instanceof EndPoint) {
            sourcePN = endNode;
            targetPN = startNode;
        } else {
            sourcePN = startNode;
            targetPN = endNode;
        }

        if (!sourcePN.getSuccessors().isEmpty()) {
            RamApp.getActiveScene().displayPopup(
                    Strings.popupNodeConnectionHasSuccessors(sourcePN),
                    PopupType.ERROR);
        } else if (!targetPN.getPredecessors().isEmpty()) {
            RamApp.getActiveScene().displayPopup(
                    Strings.popupNodeConnectionHasPredecessors(targetPN),
                    PopupType.ERROR);
        } else {
            createNodeConnection(ucmDiagramView, sourcePN, targetPN);

        }
    }
}
