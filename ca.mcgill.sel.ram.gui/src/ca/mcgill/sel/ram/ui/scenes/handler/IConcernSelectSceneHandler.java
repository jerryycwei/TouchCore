package ca.mcgill.sel.ram.ui.scenes.handler;

import ca.mcgill.sel.ram.ui.scenes.DisplayConcernSelectScene;

/**
 * Handler interface for Concern Scene in Select Mode.
 * 
 * @author Nishanth
 */
public interface IConcernSelectSceneHandler extends IRamAbstractSceneHandler {
    
    /**
     * Interface function to not use any selection and switch back to the aspect scene.
     * @param scene the current scene
     */
    void switchToAspect(DisplayConcernSelectScene scene);
    
    /**
     * Interface function to weave all the Features (aspects) selected together.
     * @param scene the current scene
     */
    void reuse(DisplayConcernSelectScene scene);
    
    /**
     * Interface function to clear all the selections made in the current select mode.
     * @param scene the current scene
     */
    void clear(DisplayConcernSelectScene scene);
    
    /**
     * Interface function to change the mode to the other mode.
     * @param scene the current scene
     */
    void switchMode(DisplayConcernSelectScene scene);
    
}
