package ca.mcgill.sel.ram.ui.scenes.handler;

import ca.mcgill.sel.core.COREModel;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.ui.scenes.DisplayConcernEditScene;

/**
 * Handler Interface for Concern Scene in edit mode.
 *
 * @author Nishanth
 * @author jerrywei
 */
public interface IConcernEditSceneHandler extends IRamAbstractSceneHandler {

    /**
     * Interface call when the Home button button is pressed.
     *
     * @param scene - the scene
     */
    void switchToHome(DisplayConcernEditScene scene);

    /**
     * Interface call to create an impact model of the concern.
     *
     * @param scene - the scene
     */
    void createImpactModel(DisplayConcernEditScene scene);

    /**
     * Interface call to create a new aspect from the current scene of the concern.
     *
     * @param scene - the scene
     */
    void createAspect(DisplayConcernEditScene scene);

    /**
     * Interface call to delete an aspect from the current scene of the concern.
     *
     * @param scene - the scene
     * @param aspect - the aspect to delete
     */
    void deleteAspect(DisplayConcernEditScene scene, Aspect aspect);

    /**
     * Interface call to create a new ucm from the current scene of the concern.
     * 
     * @param scene - the scene
     */
    void createUCM(DisplayConcernEditScene scene);

    /**
     * Interface call to delete a ucm from the current scene of the concern.
     *
     * @param scene - the scene
     * @param model - the model to delete
     */
    void deleteRealisationModel(DisplayConcernEditScene scene, COREModel model);

}
