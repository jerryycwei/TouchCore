package ca.mcgill.sel.ram.ui.views.containers;

import java.util.Collections;
import java.util.Random;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.INotifyChangedListener;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.core.COREConcern;
import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.core.COREFeatureModel;
import ca.mcgill.sel.core.COREImpactModel;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.core.controller.ControllerFactory;
import ca.mcgill.sel.core.controller.FeatureImpactController;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamListComponent;
import ca.mcgill.sel.ram.ui.components.RamPanelListComponent;
import ca.mcgill.sel.ram.ui.components.listeners.RamListListener;
import ca.mcgill.sel.ram.ui.utils.Strings;
import ca.mcgill.sel.ram.ui.views.handler.IAbstractViewHandler;
import ca.mcgill.sel.ram.ui.views.impact.ImpactDiagramView;
import ca.mcgill.sel.ram.ui.views.impact.namer.ImpactFeatureNamer;

/**
 * Container class used to contain all the features in the Feature Model.
 *
 * @author Nishanth
 * @author Romain
 *
 */
public class COREFeatureContainer extends RamPanelListComponent<COREFeature> implements INotifyChangedListener {

    private static final float PERCENTAGE_WIDTH_FEATURE_POSITION = 0.3f;
    private static final float PERCENTAGE_HEIGHT_FEATURE_POSITION = 0.75f;

    private COREImpactModel impactModel;
    private COREFeatureModel featureModel;
    private COREImpactNode root;
    private ImpactDiagramView<? extends IAbstractViewHandler> impactEditDiagramView;

    /**
     * The container takes in the default path of the container. It displays all the aspects in the folder.
     *
     * @param concern - The concern which contains all the Features.
     * @param impactDiagramView - The diagram that contains the root node.
     * @param root - The root node of the scene.
     */
    public COREFeatureContainer(COREConcern concern,
            ImpactDiagramView<? extends IAbstractViewHandler> impactDiagramView,
            COREImpactNode root) {
        super(5, Strings.LABEL_FEATURE_CONTAINER, HorizontalStick.RIGHT, VerticalStick.BOTTOM);

        this.impactModel = concern.getImpactModel();
        this.root = root;

        this.impactEditDiagramView = impactDiagramView;

        featureModel = concern.getFeatureModel();
        EMFEditUtil.addListenerFor(featureModel, this);

        FeatureSelectorListener listener = new FeatureSelectorListener();

        this.setListener(listener);
        this.setNamer(new ImpactFeatureNamer(listener));
        this.setElements(Collections.singletonList(featureModel.getRoot()));
    }

    /**
     * Private class which is a listener of the selectors implemented.
     *
     * @author Romain
     */
    private class FeatureSelectorListener implements RamListListener<COREFeature> {

        @Override
        public void elementSelected(RamListComponent<COREFeature> listComponent, COREFeature element) {
            Random r = new Random();

            float percent =
                    Math.abs(r.nextFloat() - (2 * PERCENTAGE_WIDTH_FEATURE_POSITION))
                    + PERCENTAGE_WIDTH_FEATURE_POSITION;

            FeatureImpactController featureImpactController = ControllerFactory.INSTANCE.getFeatureImpactController();
            featureImpactController.createFeatureImpact(impactModel, root, element, impactEditDiagramView.getWidth()
                    * percent, (float) (impactEditDiagramView.getHeight() * PERCENTAGE_HEIGHT_FEATURE_POSITION));
        }

        @Override
        public void elementDoubleClicked(RamListComponent<COREFeature> listComponent, COREFeature element) {
        }

        @Override
        public void elementHeld(RamListComponent<COREFeature> list, COREFeature element) {
        }
    }

    @Override
    public void notifyChanged(Notification notification) {
        RamApp.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                setElements(Collections.singletonList(featureModel.getRoot()));
            }
        });
    }

    @Override
    public void destroy() {
        EMFEditUtil.removeListenerFor(featureModel, this);
        super.destroy();
    }
}
