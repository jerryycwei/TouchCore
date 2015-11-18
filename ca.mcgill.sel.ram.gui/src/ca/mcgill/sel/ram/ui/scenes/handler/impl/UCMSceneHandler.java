package ca.mcgill.sel.ram.ui.scenes.handler.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mt4j.sceneManagement.transition.SlideTransition;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.commons.emf.util.ResourceManager;
import ca.mcgill.sel.core.COREConcern;
import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.ConfirmPopup;
import ca.mcgill.sel.ram.ui.components.browser.interfaces.UcmFileBrowserListener;
import ca.mcgill.sel.ram.ui.scenes.DisplayUCMScene;
import ca.mcgill.sel.ram.ui.utils.BasicActionsUtils;
import ca.mcgill.sel.ucm.UseCaseMap;

/**
 * The default handler for a {@link DisplayUCMScene}.
 *
 * @author jerrywei
 *
 */
public class UCMSceneHandler extends DefaultRamSceneHandler {

    /**
     * Handles switching to the menu.
     *
     * @param scene
     *            the affected {@link DisplayUCMScene}
     */
    public void switchToMenu(DisplayUCMScene scene) {
        // to the left!
        scene.setTransition(new SlideTransition(RamApp.getApplication(), 700, false));
        // go to SelectAspectScene
        RamApp.getApplication().switchToBackground(scene);
    }

    /**
     * Handles the switching to concern when the user wants to return back
     * to the concern scene he was editing.
     *
     * @param scene the affected {@link DisplayUCMScene}
     */
    public void switchToConcern(final DisplayUCMScene scene) {
        boolean isSaveNeeded = EMFEditUtil.getCommandStack(scene.getUCM()).isSaveNeeded();

        if (isSaveNeeded) {
            scene.showCloseConfirmPopup(scene, new ConfirmPopup.SelectionListener() {

                @Override
                public void optionSelected(int selectedOption) {
                    if (selectedOption == ConfirmPopup.YES_OPTION) {
                        BasicActionsUtils.saveUCM(scene.getUCM(), new UcmFileBrowserListener() {

                            @Override
                            public void ucmSaved(File file) {
                                doSwitchToConcern(scene);
                            }

                            @Override
                            public void ucmLoaded(UseCaseMap ucm) {
                            }
                        });
                    } else if (selectedOption == ConfirmPopup.NO_OPTION) {
                        while (EMFEditUtil.getCommandStack(scene.getUCM()).isSaveNeeded()) {
                            EMFEditUtil.getCommandStack(scene.getUCM()).undo();
                        }
                        checkForModelRemoval(scene);
                        doSwitchToConcern(scene);
                    }
                }
            });
        } else {
            checkForModelRemoval(scene);
            doSwitchToConcern(scene);
        }
    }

    /**
     * Performs the switching to the concern scene.
     * Unloads the resource and triggers the scene change to the previous scene.
     *
     * @param scene the current ucm scene
     */
    @SuppressWarnings("static-method")
    private void doSwitchToConcern(DisplayUCMScene scene) {

        ResourceManager.unloadResource(scene.getUCM().eResource());
        scene.setTransition(new SlideTransition(RamApp.getApplication(), 700, false));

        scene.getApplication().changeScene(scene.getPreviousScene());
        scene.getApplication().destroySceneAfterTransition(scene);
    }

    /**
     * Checks whether the ucm model needs to be removed from the concern.
     * If it was not saved and therefore not contained anywhere, the ucm model is removed.
     *
     * @param scene the current ucm scene
     */
    private static void checkForModelRemoval(DisplayUCMScene scene) {
        UseCaseMap ucm = scene.getUCM();

        // If ucm was not saved at all, then can continue
        if (ucm.eResource() == null) {
            // Collect features realising the ucm
            List<COREFeature> listOfFeatures = ucm.getRealizes();
            List<COREFeature> copyOfListOfFeature = new ArrayList<COREFeature>(listOfFeatures);

            // Loop through all the features and remove the realisations
            for (COREFeature feature : copyOfListOfFeature) {
                // Check to see if feature is realized by ucm again
                if (feature.getRealizedBy().contains(ucm)) {
                    feature.getRealizedBy().remove(ucm);
                }
            }

            COREConcern concern = ucm.getCoreConcern();

            // Remove from the list of models
            if (concern != null) {
                concern.getModels().remove(ucm);
            }
        }
    }
}
