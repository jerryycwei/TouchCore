/**
 *
 */
package ca.mcgill.sel.ram.ui.views.ucm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.componentProcessors.panProcessor.PanProcessorTwoFingers;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils.Direction;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils.UnistrokeGesture;
import org.mt4j.input.inputProcessors.componentProcessors.zoomProcessor.ZoomProcessor;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.core.LayoutElement;
import ca.mcgill.sel.core.impl.LayoutMapImpl;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.events.MouseWheelProcessor;
import ca.mcgill.sel.ram.ui.events.RightClickDragProcessor;
import ca.mcgill.sel.ram.ui.events.UnistrokeProcessorLeftClick;
import ca.mcgill.sel.ram.ui.utils.Constants;
import ca.mcgill.sel.ram.ui.views.AbstractView;
import ca.mcgill.sel.ram.ui.views.ucm.handler.HandlerFactoryUCM;
import ca.mcgill.sel.ram.ui.views.ucm.handler.impl.UCMDiagramViewHandler;
import ca.mcgill.sel.ucm.EndPoint;
import ca.mcgill.sel.ucm.NodeConnection;
import ca.mcgill.sel.ucm.PathNode;
import ca.mcgill.sel.ucm.ResponsibilityRef;
import ca.mcgill.sel.ucm.StartPoint;
import ca.mcgill.sel.ucm.UCMPackage;
import ca.mcgill.sel.ucm.UseCaseMap;

/**
 * Class used to represent a ucm mode diagram view. Loads the UCM elements and displays on the scene.
 *
 * @author jerrywei
 */
public class UCMDiagramView extends AbstractView<UCMDiagramViewHandler> implements
        INotifyChangedListener {

    /**
     * The {@link UseCaseMap} of this concern.
     */
    protected UseCaseMap useCaseMap;

    /**
     * A map with {@link PathNode} as keys and corresponding {@link PathNodeView} as values.
     */
    protected Map<PathNode, PathNodeView> pathNodeMap;

    /**
     * A map with {@link NodeConnection} as keys and corresponding {@link NodeConnectionView} as values.
     */
    protected Map<NodeConnection, NodeConnectionView> nodeConnectionMap;

    /**
     * A map with {@link EObject} as keys and corresponding {@link LayoutElement} as values.
     */
    protected EMap<EObject, LayoutElement> layoutMaps;

    /**
     * Create a UCMDiagramView.
     *
     * @param ucm the {@link UseCaseMap} of this view
     * @param width the width of this view
     * @param height the height of this view
     */
    public UCMDiagramView(UseCaseMap ucm, float width, float height) {
        super(width, height);

        this.useCaseMap = ucm;

        this.layoutMaps = ucm.getLayoutMaps();

        // TODO: Replace PathNodeView with UCMModelElementView once completed UCM
        this.pathNodeMap = new HashMap<PathNode, PathNodeView>();
        this.nodeConnectionMap = new HashMap<NodeConnection, NodeConnectionView>();

        EMFEditUtil.addListenerFor(useCaseMap, this);

        loadUcmElements();
    }

    /**
     * Display the UCM using {@link UCMModelElementView}.
     */
    public void loadUcmElements() {

        for (PathNode node : useCaseMap.getNodes()) {
            createPathNodeView(node, layoutMaps.get(node));
        }

        for (NodeConnection nodeConnection : useCaseMap.getConnections()) {
            this.addNodeConnectionView(nodeConnection);

        }
    }

    /**
     * Create a {@link NodeConnectionView} for the {@link NodeConnection}. Also sets the handler for the view as well as
     * adds it to the nodeConnectionMap.
     *
     * @param nodeConnection the {@link NodeConnection}
     * @return the {@link NodeConnectionView} created
     */
    private NodeConnectionView addNodeConnectionView(NodeConnection nodeConnection) {

        PathNode source = nodeConnection.getSource();
        PathNode target = nodeConnection.getTarget();

        PathNodeView sourceView = this.pathNodeMap.get(source);
        PathNodeView targetView = this.pathNodeMap.get(target);

        if (nodeConnectionMap.containsKey(nodeConnection) || source == null || target == null) {
            return null;
        }

        NodeConnectionView nodeConnectionView =
                new NodeConnectionView(nodeConnection, source, sourceView, target, targetView);
        nodeConnectionView.updateLines();
        nodeConnectionView.shouldUpdate();

        nodeConnectionMap.put(nodeConnection, nodeConnectionView);
        this.addChild(nodeConnectionView);

        nodeConnectionView.setHandler(HandlerFactoryUCM.INSTANCE.getNodeConnectionViewHandler());

        return nodeConnectionView;
    }

    /**
     * Create a {@link PathNodeView} dependent upon which dynamic {@link PathNode} subtype it is invoked with.
     *
     * @param node The {@link PathNode}
     * @param layoutElement The {@link LayoutElement} with the node's position
     * @return the {@link PathNodeView} created
     */
    private PathNodeView createPathNodeView(PathNode node, LayoutElement layoutElement) {

        if (this.pathNodeMap.containsKey(node)) {
            return this.pathNodeMap.get(node);
        }

        PathNodeView pathNodeView = null;

        if (node instanceof StartPoint) {
            pathNodeView = this.addStartPointView((StartPoint) node, layoutElement);
        } else if (node instanceof EndPoint) {
            pathNodeView = this.addEndPointView((EndPoint) node, layoutElement);
        } else if (node instanceof ResponsibilityRef) {
            pathNodeView = this.addResponsibilityView((ResponsibilityRef) node, layoutElement);
        }

        pathNodeView.setHandler(HandlerFactoryUCM.INSTANCE.getPathNodeViewHandler());

        return pathNodeView;
    }

    /**
     * Creates a {@link ResponsibilityView} and sets a handler for it. Also adds the ResponsibilityView to the
     * pathNodeMap as well as to the {@link UCMDiagramView}.
     *
     * @param respRef The {@link ResponsibilityRef}
     * @param layoutElement The {@link LayoutElement}
     * @return the {@link ResponsibilityView} created
     */
    private PathNodeView addResponsibilityView(ResponsibilityRef respRef,
            LayoutElement layoutElement) {
        PathNodeView view =
                new ResponsibilityView(respRef, layoutElement);

        pathNodeMap.put(respRef, view);
        this.addChild(view);
        // view.setHandler(HandlerFactoryUCM.INSTANCE.getResponsibilityViewHandler());
        return view;
    }

    /**
     * Creates a {@link EndPointView} and sets a handler for it. Also adds the {@link EndPointView} to the
     * pathNodeMap as well as to the {@link UCMDiagramView}.
     *
     * @param endPt The {@link EndPoint}
     * @param layoutElement The {@link LayoutElement} with the node's position
     * @return the {@link EndPointView} created
     */
    private PathNodeView addEndPointView(EndPoint endPt, LayoutElement layoutElement) {
        PathNodeView view = new EndPointView(endPt, layoutElement);

        pathNodeMap.put(endPt, view);
        this.addChild(view);

        return view;
    }

    /**
     * Creates a {@link StartPointView} and sets a handler for it. Also adds the {@link StartPointView} to the
     * pathNodeMap as well as to the {@link UCMDiagramView}.
     *
     * @param startPt The {@link StartPoint}
     * @param layoutElement The {@link LayoutElement}
     * @return The {@link StartPointView} created
     */
    private PathNodeView addStartPointView(StartPoint startPt, LayoutElement layoutElement) {
        PathNodeView view = new StartPointView(startPt, layoutElement);

        pathNodeMap.put(startPt, view);
        this.addChild(view);

        return view;
    }

    @Override
    public void notifyChanged(Notification notification) {

        if (notification.getFeature() == UCMPackage.Literals.USE_CASE_MAP__CONNECTIONS) {
            NodeConnection nc;
            switch (notification.getEventType()) {
                case Notification.REMOVE:
                    nc = (NodeConnection) notification.getOldValue();
                    removeNodeConnectionView(nc);
                    break;
                case Notification.ADD:
                    nc = (NodeConnection) notification.getNewValue();
                    addNodeConnectionView(nc);
                    break;
            }
        } else if (notification.getFeature() == UCMPackage.Literals.USE_CASE_MAP__LAYOUT_MAPS) {
            LayoutMapImpl layoutMap;
            PathNode pn;
            LayoutElement layoutElement;

            switch (notification.getEventType()) {
                case Notification.REMOVE:
                    layoutMap = (LayoutMapImpl) notification.getOldValue();
                    pn = (PathNode) layoutMap.getKey();
                    layoutElement = layoutMap.getValue();
                    deletePathNodeView(pn);
                    break;
                case Notification.ADD:
                    layoutMap = (LayoutMapImpl) notification.getNewValue();
                    pn = (PathNode) layoutMap.getKey();
                    layoutElement = layoutMap.getValue();
                    createPathNodeView(pn, layoutElement);
                    break;
            }
        }
    }

    /**
     * Remove the {@link PathNodeView} associated with a path node from the diagram.
     *
     * @param pn the {@link PathNode} removed
     */
    private void deletePathNodeView(PathNode pn) {
        PathNodeView pnToDelete = this.pathNodeMap.remove(pn);

        for (NodeConnection nc : pn.getPredecessors()) {
            this.removeNodeConnectionView(nc);
        }

        for (NodeConnection nc : pn.getSuccessors()) {
            this.removeNodeConnectionView(nc);
        }

        if (pnToDelete != null) {
            pnToDelete.destroy();
        }
    }

    /**
     * Remove the {@link NodeConnectionView} associated with this {@link NodeConnection} from the diagram.
     *
     * @param nc the {@link NodeConnection} removed
     */
    private void removeNodeConnectionView(NodeConnection nc) {
        NodeConnectionView ncView = this.nodeConnectionMap.remove(nc);
        if (ncView != null) {
            ncView.destroy();
        }
    }

    /**
     * Retrieve the UCM model.
     *
     * @return the UCM model
     */
    public UseCaseMap getUseCaseMap() {
        return this.useCaseMap;
    }

    /**
     * Gets the PathNodeMap.
     *
     * @return the PathNodeMap
     */
    public Map<PathNode, PathNodeView> getPathNodeMap() {
        return this.pathNodeMap;
    }

    /**
     * Retrieve the collection of {@link PathNode}.
     *
     * @return the collection of {@link PathNode}
     */
    public Collection<PathNode> getPathNodes() {
        return pathNodeMap.keySet();
    }

    /**
     * Retrieve the collection of {@link PathNodeView}.
     *
     * @return the collection of {@link PathNodeView}
     */
    public Collection<PathNodeView> getPathNodeViews() {
        return pathNodeMap.values();
    }

    /**
     * Retrieve the collection of {@link NodeConnection}.
     *
     * @return the collection of {@link NodeConnection}
     */
    public Collection<NodeConnection> getNodeConnections() {
        return nodeConnectionMap.keySet();
    }

    /**
     * Retrieve the collection of {@link NodeConnectionView}.
     *
     * @return the collection of {@link NodeConnectionView}
     */
    public Collection<NodeConnectionView> getNodeConnectionViews() {
        return nodeConnectionMap.values();

    }

    @Override
    public void destroy() {
        // remove listeners
        EMFEditUtil.removeListenerFor(useCaseMap, this);

        super.destroy();
    }

    @Override
    protected void registerGestureListeners(IGestureEventListener listener) {
        super.registerGestureListeners(listener);

        addGestureListener(TapAndHoldProcessor.class, listener);
        addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(RamApp.getApplication(), getParent()));
    }

    @Override
    protected void registerInputProcessors() {
        registerInputProcessor(new PanProcessorTwoFingers(RamApp.getApplication()));
        registerInputProcessor(new RightClickDragProcessor(RamApp.getApplication()));
        registerInputProcessor(new ZoomProcessor(RamApp.getApplication()));
        registerInputProcessor(new MouseWheelProcessor(RamApp.getApplication()));

        UnistrokeProcessorLeftClick unistrokeProcessor = new UnistrokeProcessorLeftClick(RamApp.getApplication());

        unistrokeProcessor.addTemplate(UnistrokeGesture.CIRCLE, Direction.CLOCKWISE);
        unistrokeProcessor.addTemplate(UnistrokeGesture.CIRCLE, Direction.COUNTERCLOCKWISE);
        unistrokeProcessor.addTemplate(UnistrokeGesture.X, Direction.CLOCKWISE);
        unistrokeProcessor.addTemplate(UnistrokeGesture.X, Direction.COUNTERCLOCKWISE);

        registerInputProcessor(unistrokeProcessor);

        registerInputProcessor(new TapAndHoldProcessor(RamApp.getApplication(), Constants.TAP_AND_HOLD_DURATION));
    }

}
