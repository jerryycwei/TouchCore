package ca.mcgill.sel.ram.ui.views.feature.handler.impl;

import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;

import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.events.listeners.ITapAndHoldListener;
import ca.mcgill.sel.ram.ui.events.listeners.ITapListener;
import ca.mcgill.sel.ram.ui.scenes.DisplayConcernSelectScene;
import ca.mcgill.sel.ram.ui.scenes.DisplayConcernSelectScene.DisplayMode;
import ca.mcgill.sel.ram.ui.utils.SelectionsSingleton;
import ca.mcgill.sel.ram.ui.views.feature.FeatureView;
import ca.mcgill.sel.ram.ui.views.handler.BaseHandler;

/**
 * Handler class used to perform action on events read in the Select mode of the Feature Model Display.
 * @author Nishanth
 */
public class FeatureSelectModeHandler extends BaseHandler implements ITapListener, ITapAndHoldListener {
    
    @Override
    public boolean processTapEvent(final TapEvent tapEvent) {
        
        if (tapEvent.isTapped()) {
            
            final DisplayConcernSelectScene scene = RamApp.getActiveConcernSelectScene();
            final FeatureView featureView = (FeatureView) tapEvent.getTarget();
            if (featureView.getIsRoot() || scene == null) {
                return true;
            }
            if (SelectionsSingleton.getInstance().containsSelectedFeature(featureView.getFeature())) {
                SelectionsSingleton.getInstance().removeSelection(featureView.getFeature());
            } else {
                SelectionsSingleton.getInstance().addSelection(featureView.getFeature());
            }
            
            RamApp.getApplication().invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (scene.getCurrentMode() == DisplayMode.FULL) {
                        scene.selected(featureView);
                    } else {
                        scene.redrawFeatureDiagram(true);
                    }
                }
            });
        }
        return true;
    }
    
    @Override
    public boolean processTapAndHoldEvent(TapAndHoldEvent tapAndHoldEvent) {
        final DisplayConcernSelectScene scene = RamApp.getActiveConcernSelectScene();
        
        if (tapAndHoldEvent.isHoldComplete() && scene != null) {
            
            FeatureView featureIcon = (FeatureView) tapAndHoldEvent.getTarget();
            
            if (SelectionsSingleton.getInstance().containsReexposedFeature(featureIcon.getFeature())) {
                // Re expose all parent features
                FeatureView current = featureIcon;
                SelectionsSingleton.getInstance().removeReexposition(featureIcon.getFeature());
                while (current.getParentFeatureView() != null) {
                    current = current.getParentFeatureView();
                    SelectionsSingleton.getInstance().removeReexposition(current.getFeature());
                }
            } else {
                unReexpose(featureIcon);
            }
            
            scene.selected(featureIcon);
            
        }
        
        return true;
    }
    
    /**
     * Un-reexpose recursively all children features.
     * @param featureView - the {@link FeatureView} to un-reexpose
     */
    private void unReexpose(FeatureView featureView) {
        SelectionsSingleton.getInstance().addReexposition(featureView.getFeature());
        for (FeatureView child : featureView.getChildrenFeatureViews()) {
            unReexpose(child);
        }
    }
    
    
}
