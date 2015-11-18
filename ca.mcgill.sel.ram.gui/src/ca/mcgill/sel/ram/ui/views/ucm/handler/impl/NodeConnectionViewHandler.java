package ca.mcgill.sel.ram.ui.views.ucm.handler.impl;

import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.commons.emf.util.EMFModelUtil;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamSelectorComponent;
import ca.mcgill.sel.ram.ui.components.listeners.AbstractDefaultRamSelectorListener;
import ca.mcgill.sel.ram.ui.events.listeners.ITapAndHoldListener;
import ca.mcgill.sel.ram.ui.scenes.DisplayUCMScene;
import ca.mcgill.sel.ram.ui.views.OptionSelectorView;
import ca.mcgill.sel.ram.ui.views.RamEnd;
import ca.mcgill.sel.ram.ui.views.handler.BaseHandler;
import ca.mcgill.sel.ram.ui.views.handler.IRelationshipViewHandler;
import ca.mcgill.sel.ram.ui.views.ucm.NodeConnectionView;
import ca.mcgill.sel.ram.ui.views.ucm.PathNodeView;
import ca.mcgill.sel.ucm.NodeConnection;
import ca.mcgill.sel.ucm.PathNode;
import ca.mcgill.sel.ucm.ResponsibilityRef;
import ca.mcgill.sel.ucm.UCMPackage;
import ca.mcgill.sel.ucm.UseCaseMap;
import ca.mcgill.sel.ucm.controller.ControllerFactory;
import ca.mcgill.sel.ucm.controller.NodeConnectionController;
import ca.mcgill.sel.ucm.controller.ResponsibilityController;
import ca.mcgill.sel.ucm.util.UCMModelUtil;

/**
 * The handler for all node connection views.
 *
 * @author jerrywei
 *
 */
public class NodeConnectionViewHandler extends BaseHandler implements ITapAndHoldListener, IRelationshipViewHandler {

    /**
     * Values which are to be displayed by menu.
     */
    private enum Values {
        Create_Responsibility,
    }

    /**
     * Calls {@link ResponsibilityController} to create a new responsibility.
     *
     * @param ncView The node connection where the new responsibility view will be added.
     * @param locationOnScreen The location to add the view
     */
    protected void createResponsibilityOnNCV(NodeConnectionView ncView, Vector3D locationOnScreen) {
        RamEnd<PathNode, PathNodeView> fromEnd = ncView.getFromEnd();
        RamEnd<PathNode, PathNodeView> toEnd = ncView.getToEnd();

        NodeConnection nc = ncView.getNodeConnection();

        UseCaseMap ucm =
                EMFModelUtil.getRootContainerOfType(nc, UCMPackage.Literals.USE_CASE_MAP);

        NodeConnectionController ncController = ControllerFactory.INSTANCE.getNodeConnectionController();

        // remove the existing node connection
        ncController.removeNodeConnection(nc);

        ResponsibilityController respController = ControllerFactory.INSTANCE.getResponsibilityController();
        String name = UCMModelUtil.getUniquePathNodeName(ucm);

        ResponsibilityRef respRef =
                respController.createResponsibilityRef(ucm, locationOnScreen.x, locationOnScreen.y, name);

        ncController.createNodeConnection(ucm, fromEnd.getModel(), respRef);

        ncController.createNodeConnection(ucm, respRef, toEnd.getModel());

    }

    @Override
    public boolean processTapAndHoldEvent(final TapAndHoldEvent tapAndHoldEvent) {
        if (tapAndHoldEvent.isHoldComplete()) {
            final NodeConnectionView ncView =
                    (NodeConnectionView) tapAndHoldEvent.getTarget();

            System.out.println(tapAndHoldEvent.getTarget());

            OptionSelectorView<Values> selector = new OptionSelectorView<Values>(Values.values());

            ((DisplayUCMScene) RamApp.getActiveScene())
                    .addComponent(selector, tapAndHoldEvent.getLocationOnScreen());

            selector.registerListener(new AbstractDefaultRamSelectorListener<Values>() {

                @Override
                public void elementSelected(RamSelectorComponent<Values> selector, Values element) {
                    switch (element) {
                        case Create_Responsibility:
                            createResponsibilityOnNCV(ncView, tapAndHoldEvent.getLocationOnScreen());
                            break;
                    }
                }
            });
        }
        return false;
    }

    @Override
    public boolean processDoubleTap(TapEvent tapEvent, RamEnd<?, ?> end) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean processTapAndHold(TapAndHoldEvent tapAndHoldEvent, RamEnd<?, ?> end) {
        // TODO Auto-generated method stub
        return false;
    }

}
