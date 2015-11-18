package ca.mcgill.sel.ram.ui.views.feature.handler.impl;

import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;

import ca.mcgill.sel.core.COREFeatureRelationshipType;
import ca.mcgill.sel.core.controller.ControllerFactory;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.scenes.DisplayConcernEditScene;
import ca.mcgill.sel.ram.ui.views.feature.FeatureView;
import ca.mcgill.sel.ram.ui.views.feature.handler.IChangeLinkHandler;
import ca.mcgill.sel.ram.ui.views.handler.BaseHandler;

/**
 * Class used to detect tap/touch gesture/action on links/association.
 * Calls appropriate methods depending on the current relationship and link.
 * @author Nishanth
 */
public class FeatureRelationShipHandler extends BaseHandler implements IChangeLinkHandler {
    
    /**
     * The change link handler is created for every feature Icon.
     * There are as many handlers for change link handlers as the number of Features.
     * Every time there is a change link command called, it is called on the associated Feature.
     */
    private FeatureView featureIcon;
    
    @Override
    public boolean processTapEvent(TapEvent tapEvent) {
        DisplayConcernEditScene scene = RamApp.getActiveConcernEditScene();
        
        if (tapEvent.isTapped() && scene != null) {
            
            // Check whether it is a Mandatory / Optional link
            if (tapEvent.getTarget() instanceof MTEllipse) {
                // If it is optional make it Mandatory
                if (featureIcon.getFeature().getParentRelationship() == COREFeatureRelationshipType.MANDATORY) {
                    
                    ControllerFactory.INSTANCE.getFeatureController().changeFeatureLink(scene.getConcern(),
                            featureIcon.getFeature(), COREFeatureRelationshipType.OPTIONAL);
                    
                } else if (featureIcon.getParentRelationship() == COREFeatureRelationshipType.OPTIONAL) {
                    
                    ControllerFactory.INSTANCE.getFeatureController().changeFeatureLink(scene.getConcern(),
                            featureIcon.getFeature(), COREFeatureRelationshipType.MANDATORY);
                    
                }
                
            } else if (tapEvent.getTarget() instanceof MTPolygon) {
                
                // Check if the relationship is XOR
                if (featureIcon.getChildrenRelationship() == COREFeatureRelationshipType.XOR) {
                    
                    ControllerFactory.INSTANCE.getFeatureController().changeFeatureLink(scene.getConcern(),
                            featureIcon.getFeature(), COREFeatureRelationshipType.OR);
                    
                } else if (featureIcon.getChildrenRelationship() == COREFeatureRelationshipType.OR) {
                    
                    ControllerFactory.INSTANCE.getFeatureController().changeFeatureLink(scene.getConcern(),
                            featureIcon.getFeature(), COREFeatureRelationshipType.XOR);
                    
                }
                
            }
            
        }
        
        return false;
    }
    
    @Override
    public void setFeatureIcon(final FeatureView feature) {
        featureIcon = feature;
    }
    
}
