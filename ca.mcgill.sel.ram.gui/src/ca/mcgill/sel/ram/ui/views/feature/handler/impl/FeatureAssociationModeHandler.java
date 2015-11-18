package ca.mcgill.sel.ram.ui.views.feature.handler.impl;

import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;

import ca.mcgill.sel.core.COREModel;
import ca.mcgill.sel.core.controller.ControllerFactory;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.scenes.DisplayConcernEditScene;
import ca.mcgill.sel.ram.ui.views.feature.FeatureView;
import ca.mcgill.sel.ucm.UseCaseMap;

/**
 * Feature Handler, used to handle events on the Feature in edit mode, when associating features and aspects.
 *
 * @author Nishanth
 * @author jerrywei
 */
public class FeatureAssociationModeHandler extends FeatureEditModeHandler {

    @Override
    public boolean processTapEvent(TapEvent tapEvent) {
        if (tapEvent.isTapped() || tapEvent.isDoubleTap()) {
            // Get the current working concern scene.
            DisplayConcernEditScene concernScene = RamApp.getActiveConcernEditScene();
            if (concernScene == null) {
                return true;
            }
            FeatureView featureIcon = (FeatureView) tapEvent.getTarget();

            COREModel selectedCoreModel = concernScene.getAspectSelector().getSelected();
            if (selectedCoreModel instanceof Aspect) {
                Aspect selectedAspect = (Aspect) concernScene.getAspectSelector().getSelected();
                if (selectedAspect != null) {
                    if (!featureIcon.getFeature().getRealizedBy().contains(selectedAspect)) {
                        ControllerFactory.INSTANCE.getFeatureController().associateAspect(concernScene.getConcern(),
                                featureIcon.getFeature(), selectedAspect);
                    } else {
                        ControllerFactory.INSTANCE.getFeatureController().removeAspect(concernScene.getConcern(),
                                featureIcon.getFeature(), selectedAspect);
                    }
                }
            } else if (selectedCoreModel instanceof UseCaseMap) {
                UseCaseMap selectedUCM = (UseCaseMap) concernScene.getAspectSelector().getSelected();
                if (selectedUCM != null) {
                    if (!featureIcon.getFeature().getRealizedBy().contains(selectedUCM)) {
                        ControllerFactory.INSTANCE.getFeatureController().associateUCM(concernScene.getConcern(),
                                featureIcon.getFeature(), selectedUCM);
                    } else {
                        ControllerFactory.INSTANCE.getFeatureController().removeUCM(concernScene.getConcern(),
                                featureIcon.getFeature(), selectedUCM);
                    }
                }
            }

        }
        return true;
    }

}
