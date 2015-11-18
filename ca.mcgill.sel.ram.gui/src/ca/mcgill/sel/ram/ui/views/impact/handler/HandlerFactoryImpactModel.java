package ca.mcgill.sel.ram.ui.views.impact.handler;

import ca.mcgill.sel.ram.ui.views.impact.handler.impl.ContributionViewHandler;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.FeatureImpactViewEditHandler;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.FeatureImpactViewHandler;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.GoalImpactViewEditHandler;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.GoalImpactViewHandler;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.ImpactEditDiagramHandler;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.ImpactSelectDiagramHandler;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.WeightedMappingHandler;

/**
 * A factory to obtain all (default) handlers for views of impact model.
 *
 * @author Romain
 */
public final class HandlerFactoryImpactModel {

    /**
     * The singleton instance of this factory.
     */
    public static final HandlerFactoryImpactModel INSTANCE = new HandlerFactoryImpactModel();

    private ContributionViewHandler contributionViewHandler;
    private FeatureImpactViewHandler featureImpactViewHandler;
    private FeatureImpactViewEditHandler featureImpactViewEditHandler;
    private GoalImpactViewHandler goalImpactViewHandler;
    private GoalImpactViewEditHandler goalImpactViewEditHandler;
    private ImpactEditDiagramHandler impactEditDiagramHandler;
    private ImpactSelectDiagramHandler impactSelectDiagramHandler;
    private WeightedMappingHandler weightedMappingHandler;

    /**
     * Creates a new instance of the handler factory.
     */
    private HandlerFactoryImpactModel() {

    }

    /**
     * Returns the default handler for a contribution view.
     *
     * @return the default {@link ContributionViewHandler}
     */
    public ContributionViewHandler getContributionViewHandler() {
        if (contributionViewHandler == null) {
            contributionViewHandler = new ContributionViewHandler();
        }

        return contributionViewHandler;
    }

    /**
     * Returns the default handler for a feature impact view.
     *
     * @return the default {@link FeatureImpactViewHandler}
     */
    public FeatureImpactViewHandler getFeatureImpactViewHandler() {
        if (featureImpactViewHandler == null) {
            featureImpactViewHandler = new FeatureImpactViewHandler();
        }

        return featureImpactViewHandler;
    }

    /**
     * Returns the default handler for a feature impact view edit mode.
     *
     * @return the default {@link FeatureImpactViewEditHandler}
     */
    public FeatureImpactViewEditHandler getFeatureImpactViewEditHandler() {
        if (featureImpactViewEditHandler == null) {
            featureImpactViewEditHandler = new FeatureImpactViewEditHandler();
        }

        return featureImpactViewEditHandler;
    }

    /**
     * Returns the default handler for a goal impact view.
     *
     * @return the default {@link GoalImpactViewHandler}
     */
    public GoalImpactViewHandler getGoalImpactViewHandler() {
        if (goalImpactViewHandler == null) {
            goalImpactViewHandler = new GoalImpactViewHandler();
        }

        return goalImpactViewHandler;
    }

    /**
     * Returns the default handler for a goal impact view edit mode.
     *
     * @return the default {@link GoalImpactViewEditHandler}
     */
    public GoalImpactViewEditHandler getGoalImpactViewEditHandler() {
        if (goalImpactViewEditHandler == null) {
            goalImpactViewEditHandler = new GoalImpactViewEditHandler();
        }

        return goalImpactViewEditHandler;
    }

    /**
     * Returns the default handler for a impact edit diagram view.
     *
     * @return the default {@link ImpactEditDiagramHandler}
     */
    public ImpactEditDiagramHandler getImpactEditDiagramHandler() {
        if (impactEditDiagramHandler == null) {
            impactEditDiagramHandler = new ImpactEditDiagramHandler();
        }

        return impactEditDiagramHandler;
    }

    /**
     * Returns the default handler for a impact select diagram view.
     *
     * @return the default {@link ImpactSelectDiagramHandler}
     */
    public ImpactSelectDiagramHandler getImpactSelectDiagramHandler() {
        if (impactSelectDiagramHandler == null) {
            impactSelectDiagramHandler = new ImpactSelectDiagramHandler();
        }

        return impactSelectDiagramHandler;
    }

    /**
     * Returns the default handler for a weighted mappings list.
     *
     * @return the default {@link WeightedMappingHandler}
     */
    public WeightedMappingHandler getWeightedMappingHandler() {
        if (weightedMappingHandler == null) {
            weightedMappingHandler = new WeightedMappingHandler();
        }

        return weightedMappingHandler;
    }

}
