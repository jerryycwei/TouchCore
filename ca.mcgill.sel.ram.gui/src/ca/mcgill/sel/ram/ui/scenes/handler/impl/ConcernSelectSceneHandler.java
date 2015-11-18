package ca.mcgill.sel.ram.ui.scenes.handler.impl;

import org.eclipse.emf.ecore.EObject;
import org.mt4j.sceneManagement.transition.SlideTransition;

import ca.mcgill.sel.core.COREConcern;
import ca.mcgill.sel.core.util.COREModelUtil;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamPopup.PopupType;
import ca.mcgill.sel.ram.ui.scenes.DisplayConcernSelectScene;
import ca.mcgill.sel.ram.ui.scenes.DisplayConcernSelectScene.DisplayMode;
import ca.mcgill.sel.ram.ui.scenes.handler.IConcernSelectSceneHandler;
import ca.mcgill.sel.ram.ui.utils.RamModelUtils;
import ca.mcgill.sel.ram.ui.utils.SelectionsSingleton;
import ca.mcgill.sel.ram.ui.utils.Strings;

/**
 * Handler that handles events occurring in the feature select scene when reusing a concern.
 * 
 * @author Nishanth
 * @author mschoettle
 * @author cbensoussan
 * @author oalam
 */
public class ConcernSelectSceneHandler implements IConcernSelectSceneHandler {
    
    /**
     * Duration for the slide transition.
     */
    private static final int TRANSITION_DURATION = 700;
    
    @Override
    public void switchToAspect(DisplayConcernSelectScene scene) {
        
        SelectionsSingleton.getInstance().clearAll();
        
        scene.unLoadAllResources();
        scene.setTransition(new SlideTransition(RamApp.getApplication(), TRANSITION_DURATION, false));
        scene.getApplication().changeScene(scene.getPreviousScene());
        scene.getApplication().destroySceneAfterTransition(scene);
    }
    
    @Override
    public void reuse(DisplayConcernSelectScene scene) {
        try {
            
            if (scene.hasClashes() || !scene.getCanReuse()) {
                return;
            }
            
            scene.displayPopup(Strings.POPUP_REUSING);
            Aspect aspect = scene.getAspect();
            
            COREConcern concern = scene.getConcern();
            
            COREConcern concernToAssign = COREModelUtil.createConcernCopy(concern,
                    aspect.getCoreConcern().eResource().getURI());
            if (concernToAssign == null) {
                scene.displayPopup(Strings.POPUP_ERROR_SELF_REUSE, PopupType.ERROR);
                return;
            }

            RamModelUtils.createWovenAspect(scene, concern, aspect, true);
            SelectionsSingleton.getInstance().clearAll();            
            switchToAspect(scene);
            
            // CHECKSTYLE:IGNORE IllegalCatch: Many exceptions can occur and we don't want to crash the application.
        } catch (Exception e) {
            scene.displayPopup(Strings.POPUP_ERROR_REUSE, PopupType.ERROR);
            e.printStackTrace();
        }
    }

    @Override
    public void clear(DisplayConcernSelectScene scene) {
        // clear selections
        SelectionsSingleton.getInstance().clearAll();
        scene.redrawFeatureDiagram(true);
    }
    
    @Override
    public void switchMode(DisplayConcernSelectScene scene) {
        if (scene.getCurrentMode().equals(DisplayMode.NEXT)) {
            scene.setCurrentMode(DisplayMode.FULL);
        } else if (scene.getCurrentMode().equals(DisplayMode.FULL)) {
            scene.setCurrentMode(DisplayMode.NEXT);
        }
        scene.updateModeButtons();
        scene.redrawFeatureDiagram(true);
    }

    @Override
    public void save(EObject element) {
        // unused
    }

    @Override
    public void undo(EObject element) {
        // unused
    }

    @Override
    public void redo(EObject element) {
        // unused
    }
}
