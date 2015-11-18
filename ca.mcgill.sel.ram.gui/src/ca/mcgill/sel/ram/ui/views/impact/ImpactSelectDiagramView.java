package ca.mcgill.sel.ram.ui.views.impact;

import java.util.Map;
import java.util.Set;

import org.mt4j.util.MTColor;

import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.core.COREFeatureImpactNode;
import ca.mcgill.sel.core.COREImpactModel;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.ram.ui.views.impact.handler.HandlerFactoryImpactModel;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.FeatureImpactViewHandler;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.GoalImpactViewHandler;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.ImpactModelElementViewHandler;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.ImpactSelectDiagramHandler;

/**
 * Class used to represent an impact model. It is used to create the impact model. This class only describe which
 * Handler we should use for the edit mode of the Impact model.
 *
 * @author Romain
 *
 */
public class ImpactSelectDiagramView extends ImpactDiagramView<ImpactSelectDiagramHandler> {

    private Map<COREImpactNode, Float> goalImpactMap;
    private Set<COREFeature> selectedFeatures;

    /**
     * Create a {@link ImpactSelectDiagramView}. It has a root goal and builds the model from it.
     *
     * @param im the {@link COREImpactModel} of this view
     * @param rootNode the root {@link COREImpactNode}
     * @param goalImpactMap The map associating impact model element to the weight of the evaluation
     * @param selectedFeatures The selected feature on the DisplayConcernSelectScene
     * @param width the width of this view
     * @param height the height of this view
     */
    public ImpactSelectDiagramView(COREImpactModel im, COREImpactNode rootNode,
            Map<COREImpactNode, Float> goalImpactMap, Set<COREFeature> selectedFeatures, float width,
            float height) {
        super(im, rootNode, width, height);

        this.goalImpactMap = goalImpactMap;
        this.selectedFeatures = selectedFeatures;

        this.changeElementColors();
    }

    /**
     * Change the color of the feature selected and the goals used.
     */
    private void changeElementColors() {
        for (ImpactModelElementView<? extends ImpactModelElementViewHandler> view : impactMap.values()) {
            COREImpactNode impactModelElement = view.getImpactModelElement();

            if (impactModelElement instanceof COREFeatureImpactNode) {
                COREFeature feature = ((COREFeatureImpactNode) impactModelElement).getRepresents();
                FeatureImpactView featureImpactView = (FeatureImpactView) view;

                if (selectedFeatures.contains(feature)) {
                    featureImpactView.changeColor(MTColor.GREEN);
                } else {
                    featureImpactView.changeColor(MTColor.RED);
                }
            } else {
                Float value = goalImpactMap.get(impactModelElement);
                if (value != null) {

                    GoalImpactView goalImpactView = (GoalImpactView) view;

                    goalImpactView.getTextView().setText(
                            impactModelElement.getName() + " : " + Math.round(value * 100) / 100.0);

                    goalImpactView.changeColor(new MTColor(2 * (255 - (value.floatValue() * 2.55f)), 2 * value
                            .floatValue() * 2.55f, 0f));
                }
            }
        }
    }

    @Override
    protected void setContributionViewHandler(ContributionView contributionView) {
    }

    @Override
    protected void setGoalImpactViewHandler(ImpactModelElementView<GoalImpactViewHandler> view) {
        view.setHandler(HandlerFactoryImpactModel.INSTANCE.getGoalImpactViewHandler());
    }

    @Override
    protected void setFeatureImpactViewHandler(ImpactModelElementView<FeatureImpactViewHandler> view) {
        view.setHandler(HandlerFactoryImpactModel.INSTANCE.getFeatureImpactViewHandler());
    }
}
