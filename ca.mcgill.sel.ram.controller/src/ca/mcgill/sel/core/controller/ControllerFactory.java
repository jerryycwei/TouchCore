package ca.mcgill.sel.core.controller;

/**
 * A factory to obtain all controllers related to CORE.
 *
 * @author mschoettle
 */
public final class ControllerFactory {

    /**
     * The singleton instance of this factory.
     */
    public static final ControllerFactory INSTANCE = new ControllerFactory();

    private FeatureController featureController;
    private ReuseController reuseController;
    private ImpactModelElementController impactModelElementController;
    private FeatureImpactController featureImpactController;
    private ContributionController contributionController;
    private ConcernController concernController;

    /**
     * Creates a new instance of {@link ControllerFactory}.
     */
    private ControllerFactory() {

    }

    /**
     * Returns the controller for {@link ca.mcgill.sel.core.COREFeature}s.
     *
     * @return the controller for {@link ca.mcgill.sel.core.COREFeature}s
     */
    public FeatureController getFeatureController() {
        if (featureController == null) {
            featureController = new FeatureController();
        }
        return featureController;
    }

    /**
     * Returns the controller for {@link ca.mcgill.sel.core.COREReuse}s.
     *
     * @return the controller for {@link ca.mcgill.sel.core.COREReuse}s
     */
    public ReuseController getReuseController() {
        if (reuseController == null) {
            reuseController = new ReuseController();
        }
        return reuseController;
    }

    /**
     * Returns the controller for {@link ca.mcgill.sel.core.COREImpactNode}s.
     *
     * @return the controller for {@link ca.mcgill.sel.core.COREImpactNode}s
     */
    public ImpactModelElementController getImpactModelElementController() {
        if (impactModelElementController == null) {
            impactModelElementController = new ImpactModelElementController();
        }
        return impactModelElementController;
    }

    /**
     * Returns the controller for {@link ca.mcgill.sel.core.COREFeatureImpactNode}s.
     *
     * @return the controller for {@link ca.mcgill.sel.core.COREFeatureImpactNode}s
     */
    public FeatureImpactController getFeatureImpactController() {
        if (featureImpactController == null) {
            featureImpactController = new FeatureImpactController();
        }
        return featureImpactController;
    }

    /**
     * Returns the controller for {@link ca.mcgill.sel.core.COREContribution}s.
     *
     * @return the controller for {@link ca.mcgill.sel.core.COREContribution}s
     */
    public ContributionController getContributionController() {
        if (contributionController == null) {
            contributionController = new ContributionController();
        }
        return contributionController;
    }

    /**
     * Returns the controller for {@link ca.mcgill.sel.core.COREConcern}s.
     *
     * @return the controller for {@link ca.mcgill.sel.core.COREConcern}s
     */
    public ConcernController getConcernController() {
        if (concernController == null) {
            concernController = new ConcernController();
        }
        return concernController;
    }

}
