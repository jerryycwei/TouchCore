package ca.mcgill.sel.ucm.controller;

/**
 * A factory to obtain all controllers related to UCM.
 *
 * @author jerrywei
 */
public final class ControllerFactory {

    /**
     * The singleton instance of this factory.
     */
    public static final ControllerFactory INSTANCE = new ControllerFactory();

    private NodeConnectionController nodeConnectionController;
    private StartPointController startPointController;
    private EndPointController endPointController;
    private ResponsibilityController responsibilityController;
    private UseCaseMapController useCaseMapController;
    private PathNodeController pathNodeController;

    /**
     * Creates a new instance of {@link ControllerFactory}.
     */
    private ControllerFactory() {

    }

    /**
     * Returns the controller for path nodes.
     *
     * @return the controller for path nodes
     */
    public PathNodeController getPathNodeController() {
        if (pathNodeController == null) {
            pathNodeController = new PathNodeController();
        }
        return pathNodeController;
    }

    /**
     * Returns the controller for node connections.
     *
     * @return the controller for NodeConnection
     */
    public NodeConnectionController getNodeConnectionController() {
        if (nodeConnectionController == null) {
            nodeConnectionController = new NodeConnectionController();
        }
        return nodeConnectionController;
    }

    /**
     * Returns the controller for StartPoint.
     *
     * @return the controller for StartPoint
     */
    public StartPointController getStartPointController() {
        if (startPointController == null) {
            startPointController = new StartPointController();
        }
        return startPointController;
    }

    /**
     * Returns the controller for EndPoint.
     *
     * @return the controller for EndPoint
     */
    public EndPointController getEndPointController() {
        if (endPointController == null) {
            endPointController = new EndPointController();
        }
        return endPointController;
    }

    /**
     * Returns the controller for ResponsibilityRef.
     *
     * @return the controller for ResponsibilityRef
     */
    public ResponsibilityController getResponsibilityController() {
        if (responsibilityController == null) {
            responsibilityController = new ResponsibilityController();
        }
        return responsibilityController;
    }

    /**
     * Returns the controller for UseCaseMapController.
     *
     * @return the controller for UseCaseMapController
     */
    public UseCaseMapController getUseCaseMapController() {
        if (useCaseMapController == null) {
            useCaseMapController = new UseCaseMapController();
        }
        return useCaseMapController;
    }
}
