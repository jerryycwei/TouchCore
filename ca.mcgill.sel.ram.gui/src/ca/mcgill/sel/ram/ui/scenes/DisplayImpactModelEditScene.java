package ca.mcgill.sel.ram.ui.scenes;

import org.eclipse.emf.ecore.EObject;

import ca.mcgill.sel.core.COREConcern;
import ca.mcgill.sel.core.COREImpactModel;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.views.containers.COREFeatureContainer;
import ca.mcgill.sel.ram.ui.views.containers.COREImpactModelContainer;
import ca.mcgill.sel.ram.ui.views.impact.ImpactEditDiagramView;
import ca.mcgill.sel.ram.ui.views.impact.handler.HandlerFactoryImpactModel;

/**
 * Class used to represent the Impact Model Scene.
 *
 * @author Nishanth
 * @author Romain
 */
public class DisplayImpactModelEditScene extends AbstractImpactScene {

    private ImpactEditDiagramView impactEditDiagramView;

    /**
     * Constructor called from the RamApp to display the Impact Model.
     *
     * @param application - The RamApp application.
     * @param name - The name of the scene.
     * @param concern - The concern name.
     * @param im - The Impact Model.
     * @param rootNode the root node of this scene
     */
    public DisplayImpactModelEditScene(RamApp application, String name, COREConcern concern, COREImpactModel im,
            COREImpactNode rootNode) {
        super(application, name, im, rootNode, true);

        impactEditDiagramView = new ImpactEditDiagramView(im, rootNode, getWidth(), getHeight());
        containerLayer.addChild(0, impactEditDiagramView);
        impactEditDiagramView.setHandler(HandlerFactoryImpactModel.INSTANCE.getImpactEditDiagramHandler());

        COREFeatureContainer featureContainer =
                new COREFeatureContainer(concern, impactEditDiagramView,
                        this.impactEditDiagramView.getRootImpactModelElement());
        containerLayer.addChild(featureContainer);

        COREImpactModelContainer impactModelContainer =
                new COREImpactModelContainer(concern, impactEditDiagramView,
                        this.impactEditDiagramView.getRootImpactModelElement(),
                        this.impactEditDiagramView.getImpactModelElements());
        containerLayer.addChild(impactModelContainer);

        setCommandStackListener(concern);
    }

    @Override
    protected EObject getElementToSave() {
        if (impactModel != null) {
            return impactModel.getCoreConcern();
        }
        return null;
    }
}
