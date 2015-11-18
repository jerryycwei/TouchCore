package ca.mcgill.sel.ram.ui.scenes;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;

import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.core.COREImpactModel;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.events.listeners.ActionListener;
import ca.mcgill.sel.ram.ui.views.impact.ImpactSelectDiagramView;
import ca.mcgill.sel.ram.ui.views.impact.handler.HandlerFactoryImpactModel;

/**
 * Class used to represent the Impact Model Scene.
 *
 * @author Nishanth
 * @author Romain
 */
public class DisplayImpactModelSelectScene extends AbstractImpactScene implements ActionListener {
    private ImpactSelectDiagramView impactSelectDiagramView;

    /**
     * Constructor called from the RamApp to display the Impact Model.
     *
     * @param application - The RamApp application.
     * @param name - The name of the scene.
     * @param im - The Impact Model.
     * @param rootNode the root node of this scene
     * @param goalImpactMap The map associating impact model element to the weight of the evaluation
     * @param selectedFeatures The selected feature on the DisplayConcernSelectScene
     */
    public DisplayImpactModelSelectScene(RamApp application, String name, COREImpactModel im,
            COREImpactNode rootNode, Map<COREImpactNode, Float> goalImpactMap,
            Set<COREFeature> selectedFeatures) {
        super(application, name, im, rootNode, false);

        impactSelectDiagramView =
                new ImpactSelectDiagramView(im, rootNode, goalImpactMap, selectedFeatures, getWidth(), getHeight());
        containerLayer.addChild(0, impactSelectDiagramView);
        
        impactSelectDiagramView.setHandler(HandlerFactoryImpactModel.INSTANCE.getImpactSelectDiagramHandler());
    }

    @Override
    protected EObject getElementToSave() {
        return null;
    }
}
