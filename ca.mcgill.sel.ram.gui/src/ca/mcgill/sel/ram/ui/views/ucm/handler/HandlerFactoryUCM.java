package ca.mcgill.sel.ram.ui.views.ucm.handler;

import ca.mcgill.sel.ram.ui.scenes.handler.impl.UCMSceneHandler;
import ca.mcgill.sel.ram.ui.views.ucm.handler.impl.NodeConnectionViewHandler;
import ca.mcgill.sel.ram.ui.views.ucm.handler.impl.PathNodeViewHandler;
import ca.mcgill.sel.ram.ui.views.ucm.handler.impl.UCMDiagramViewHandler;

/**
 * A factory to obtain all ucm handlers.
 *
 * @author jerrywei
 *
 */
public final class HandlerFactoryUCM {

    /**
     * The singleton instance of this factory.
     */
    public static final HandlerFactoryUCM INSTANCE = new HandlerFactoryUCM();

    private NodeConnectionViewHandler nodeConnectionHandler;
    private UCMDiagramViewHandler ucmDiagramViewHandler;
    private PathNodeViewHandler pathNodeViewHandler;
    private UCMSceneHandler ucmSceneHandler;

    /**
     * Creates a new instance.
     */
    private HandlerFactoryUCM() {

    }

    /**
     * Returns the default handler for the node connection view.
     *
     * @return the {@link NodeConnectionViewHandler}
     */
    public NodeConnectionViewHandler getNodeConnectionViewHandler() {
        if (nodeConnectionHandler == null) {
            nodeConnectionHandler = new NodeConnectionViewHandler();
        }

        return nodeConnectionHandler;
    }

    /**
     * Returns the default handler for the UCM diagram view.
     *
     * @return the {@link UCMDiagramViewHandler}
     */
    public UCMDiagramViewHandler getUCMDiagramViewHandler() {
        if (ucmDiagramViewHandler == null) {
            ucmDiagramViewHandler = new UCMDiagramViewHandler();
        }

        return ucmDiagramViewHandler;
    }

    /**
     * Returns the default handler for the path node view.
     *
     * @return the {@link PathNodeViewHandler}
     */
    public PathNodeViewHandler getPathNodeViewHandler() {
        if (pathNodeViewHandler == null) {
            pathNodeViewHandler = new PathNodeViewHandler();
        }

        return pathNodeViewHandler;
    }

    /**
     * Returns the default handler for the UCM Scene.
     *
     * @return the {@link UCMSceneHandler}
     */
    public UCMSceneHandler getUCMSceneHandler() {
        if (ucmSceneHandler == null) {
            ucmSceneHandler = new UCMSceneHandler();
        }

        return ucmSceneHandler;
    }

}
