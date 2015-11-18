package ca.mcgill.sel.ram.ui.views.structural.handler.impl;

import ca.mcgill.sel.core.COREConcern;
import ca.mcgill.sel.core.controller.ReuseController;
import ca.mcgill.sel.core.util.COREModelUtil;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.Instantiation;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.browser.CoreFileBrowser;
import ca.mcgill.sel.ram.ui.components.browser.interfaces.CoreFileBrowserListener;
import ca.mcgill.sel.ram.ui.scenes.handler.impl.ConcernSelectSceneHandler;
import ca.mcgill.sel.ram.ui.utils.Constants;
import ca.mcgill.sel.ram.ui.utils.Strings;
import ca.mcgill.sel.ram.ui.views.handler.HandlerFactory;
import ca.mcgill.sel.ram.ui.views.structural.InstantiationSplitEditingView;
import ca.mcgill.sel.ram.ui.views.structural.handler.IInstantiationsContainerViewHandler;

/**
 * Handles events for a {@link ca.mcgill.sel.ram.ui.views.structural.InstantiationsContainerView} which is showing the
 * list of modelreuses in an aspect.
 * 
 * @author oalam
 */
public class ModelReuseContainerViewHandler implements IInstantiationsContainerViewHandler {
    /**
     * This method deletes a given instantiation. If the instantiation is of type
     * DEPENDS, it finds its {@link ca.mcgill.sel.core.COREReuse} and calls {@link deleteReuseInstantiation}.
     * Otherwise, if the instantiation is of type EXTENDs, it simply deletes the instantiation.
     * 
     * @param instantiation the instantiation to be deleted.
     */
    @Override
    public void deleteInstantiation(Instantiation instantiation) {
        // Disallow deleting the instantiation if split view is enabled.
        boolean splitModeEnabled =
                RamApp.getActiveAspectScene().getCurrentView() instanceof InstantiationSplitEditingView;
        if (!splitModeEnabled) {
            ReuseController controller = ca.mcgill.sel.core.controller.ControllerFactory.INSTANCE.getReuseController();
            controller.removeReuseInstantiation(instantiation);
        } else {
            RamApp.getActiveAspectScene().displayPopup("You can not delete the instantiation while editing it.");
        }
    }

    @Override
    public void loadBrowser(final Aspect aspect) {
        CoreFileBrowser.setInitialFolder(Constants.DIRECTORY_LIBRARIES);

        // Ask the user to select an aspect that should be depended on
        CoreFileBrowser.loadCoreFile(new CoreFileBrowserListener() {

            @Override
            public void concernLoaded(COREConcern concern) {
                ConcernSelectSceneHandler handler = HandlerFactory.INSTANCE.getDisplayFMSceneSelectHandler();
                COREConcern localConcern = COREModelUtil.createConcernCopy(concern,
                        aspect.getCoreConcern().eResource().getURI());
                if (localConcern == null) {
                    RamApp.getActiveScene().displayPopup(Strings.POPUP_ERROR_SELF_REUSE);
                } else {
                    RamApp.getApplication().displayFMSelect(localConcern, aspect, handler);
                }
            }
        });
    }

}
