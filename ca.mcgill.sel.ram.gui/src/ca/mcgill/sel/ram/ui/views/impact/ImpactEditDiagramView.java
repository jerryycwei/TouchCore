package ca.mcgill.sel.ram.ui.views.impact;

import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;

import ca.mcgill.sel.core.COREImpactModel;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.utils.Constants;
import ca.mcgill.sel.ram.ui.views.impact.handler.HandlerFactoryImpactModel;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.FeatureImpactViewHandler;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.GoalImpactViewHandler;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.ImpactEditDiagramHandler;

/**
 * Class used to represent an impact model. It is used to create the impact model. This class only describe which
 * Handler we should use for the edit mode of the Impact model.
 *
 * @author Romain
 *
 */
public class ImpactEditDiagramView extends ImpactDiagramView<ImpactEditDiagramHandler> {

    /**
     * Create a {@link ImpactEditDiagramView}. It has a root goal and builds the model from it.
     *
     * @param im the {@link COREImpactModel} of this view
     * @param rootNode the root {@link COREImpactNode}
     * @param width the width of this view
     * @param height the height of this view
     */
    public ImpactEditDiagramView(COREImpactModel im, COREImpactNode rootNode, float width, float height) {
        super(im, rootNode, width, height);
    }

    @Override
    protected void registerGestureListeners(IGestureEventListener listener) {
        super.registerGestureListeners(listener);

        addGestureListener(TapAndHoldProcessor.class, listener);
        addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(RamApp.getApplication(), getParent()));
    }

    @Override
    protected void registerInputProcessors() {
        super.registerInputProcessors();
        registerInputProcessor(new TapAndHoldProcessor(RamApp.getApplication(), Constants.TAP_AND_HOLD_DURATION));
    }

    @Override
    protected void setGoalImpactViewHandler(ImpactModelElementView<GoalImpactViewHandler> view) {
        view.setHandler(HandlerFactoryImpactModel.INSTANCE.getGoalImpactViewEditHandler());
    }

    @Override
    protected void setFeatureImpactViewHandler(ImpactModelElementView<FeatureImpactViewHandler> view) {
        view.setHandler(HandlerFactoryImpactModel.INSTANCE.getFeatureImpactViewEditHandler());
    }

    @Override
    protected void setContributionViewHandler(ContributionView contributionView) {
        contributionView.setHandler(HandlerFactoryImpactModel.INSTANCE.getContributionViewHandler());
    }
}
