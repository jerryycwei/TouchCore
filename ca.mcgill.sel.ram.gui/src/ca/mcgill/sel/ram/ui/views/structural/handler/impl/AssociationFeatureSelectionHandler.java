package ca.mcgill.sel.ram.ui.views.structural.handler.impl;

import java.util.List;
import java.util.Set;

import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;

import ca.mcgill.sel.commons.emf.util.ResourceManager;
import ca.mcgill.sel.core.COREConcern;
import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.core.util.COREModelUtil;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.AssociationEnd;
import ca.mcgill.sel.ram.controller.AssociationController;
import ca.mcgill.sel.ram.controller.ControllerFactory;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamPopup.PopupType;
import ca.mcgill.sel.ram.ui.scenes.DisplayConcernSelectScene;
import ca.mcgill.sel.ram.ui.scenes.handler.impl.ConcernSelectSceneHandler;
import ca.mcgill.sel.ram.ui.utils.Constants;
import ca.mcgill.sel.ram.ui.utils.RamModelUtils;
import ca.mcgill.sel.ram.ui.utils.SelectionsSingleton;
import ca.mcgill.sel.ram.ui.utils.Strings;
import ca.mcgill.sel.ram.ui.views.TextView;
import ca.mcgill.sel.ram.ui.views.handler.impl.TextViewHandler;

/**
 * Handler for the feature selection of an association. Handles the display of the association feature model and setting
 * of the feature selection to the model.
 * 
 * @author cbensoussan
 */
public class AssociationFeatureSelectionHandler extends TextViewHandler {
    
    @Override
    public boolean processTapEvent(TapEvent tapEvent) {
        if (tapEvent.isDoubleTap()) {
            
            TextView target = (TextView) tapEvent.getTarget();
            final AssociationEnd associationEnd = (AssociationEnd) target.getData();
            final Aspect aspect = RamApp.getActiveAspectScene().getAspect();
            
            if (aspect.getRealizes().size() > 0) {
                String filePath = Constants.ASSOCIATION_CONCERN_LOCATION;
                final COREConcern concern = (COREConcern) ResourceManager.loadModel(filePath);
                final COREConcern localConcern = COREModelUtil.createConcernCopy(concern,
                        aspect.getCoreConcern().eResource().getURI());

                if (localConcern == null) {
                    RamApp.getActiveScene().displayPopup(Strings.POPUP_ERROR_SELF_REUSE);
                    return true;
                }

                List<COREFeature> plains =
                      localConcern.getFeatureModel().getRoot().getChildren().get(0).getChildren();
                plains.remove(0);
                plains.remove(0);
                plains.remove(0);
                
                SelectionsSingleton.getInstance().clearAll();
                if (associationEnd.getFeatureSelection() != null) {
                    SelectionsSingleton.getInstance().addAllSelections(
                            associationEnd.getFeatureSelection().getReuse().getSelectedConfiguration().getSelected());
                }
                
                RamApp.getApplication().displayFMSelect(localConcern, aspect, new ConcernSelectSceneHandler() {
                    @Override
                    public void reuse(DisplayConcernSelectScene scene) {
                        scene.displayPopup(Strings.POPUP_REUSING);
                        AssociationController associationController = ControllerFactory.INSTANCE
                                .getAssociationController();
                        Aspect aspect = scene.getAspect();
                        COREConcern concern = scene.getConcern();
                        Aspect wovenAspect;
                        
                        Set<COREFeature> selectedFeatures = SelectionsSingleton.getInstance().getSelectedList();
                        // Remove association feature from selection for better display of the selection in the aspect
                        if (selectedFeatures.size() > 1
                                && selectedFeatures.contains(concern.getFeatureModel().getRoot())) {
                            selectedFeatures.remove(concern.getFeatureModel().getRoot());
                        }

                        wovenAspect = RamModelUtils.createWovenAspect(scene, concern, aspect, false);
                        
                        associationController.setFeatureSelection(associationEnd, SelectionsSingleton.getInstance()
                                .getSelectedList(), localConcern, aspect, wovenAspect);
                        switchToAspect(scene);
                        SelectionsSingleton.getInstance().clearAll();
                    }
                });
                
                return true;
            } else {
                RamApp.getActiveScene().displayPopup(Strings.popupAspectNeedsRealize(aspect.getName()),
                        PopupType.ERROR);
            }
        }
        
        return true;
    }
}
