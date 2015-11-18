package ca.mcgill.sel.ram.ui.components.browser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

import ca.mcgill.sel.commons.emf.util.ResourceManager;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamPopup.PopupType;
import ca.mcgill.sel.ram.ui.components.browser.RamFileBrowser.RamFileBrowserType;
import ca.mcgill.sel.ram.ui.components.browser.interfaces.RamFileBrowserListener;
import ca.mcgill.sel.ram.ui.components.browser.interfaces.UcmFileBrowserListener;
import ca.mcgill.sel.ram.ui.utils.Constants;
import ca.mcgill.sel.ram.ui.utils.Strings;
import ca.mcgill.sel.ucm.UseCaseMap;

/**
 *
 * @author jerrywei
 *
 */
public final class UcmFileBrowser {
    private static File lastSharedFolder = new File(Constants.DIRECTORY_MODELS).getAbsoluteFile();
    private static File rootConcernFolder;

    /**
     * Private constructor.
     */
    private UcmFileBrowser() {
    }

    /**
     * Load a ucm with the file browser.
     *
     * @param listener The callback to handle the loaded ucm
     */
    public static void loadUCM(final UcmFileBrowserListener listener) {
        RamFileBrowser browser = new RamFileBrowser(RamFileBrowserType.LOAD, Constants.UCM_FILE_EXTENSION,
                lastSharedFolder, rootConcernFolder);
        // TODO: Threaded loading can cause MT4J to crash during drawAndUpdate
        // browser.setCallbackThreaded(true);
        browser.addFileBrowserListener(new RamFileBrowserListener() {

            @Override
            public void fileSelected(File file, RamFileBrowser fileBrowser) {
                try {
                    if (file.canRead()) {
                        lastSharedFolder = file.getParentFile();
                        UseCaseMap ucm = (UseCaseMap) ResourceManager.loadModel(file);
                        listener.ucmLoaded(ucm);
                    }
                    // CHECKSTYLE:IGNORE IllegalCatch FOR 1 LINES: Need to catch RuntimeException
                } catch (Exception e) {
                    RamApp.getActiveScene().displayPopup(Strings.POPUP_ERROR_FILE_LOAD_SYNTAX, PopupType.ERROR);
                    e.printStackTrace();
                }
            }
        });
        browser.display();
    }

    /**
     * Save a ucm with the file browser.
     *
     * @param ucm The aspect the user wants to save
     * @param listener The callback to handle the saved file
     */
    public static void saveUCM(final UseCaseMap ucm, final UcmFileBrowserListener listener) {
        File suggestedFile;

        // Set the selected file to suggest a name.
        // If the ucm is already contained in a resource, choose that file.
        if (ucm.eResource() != null) {
            suggestedFile = new File(ucm.eResource().getURI().toFileString());
        } else {
            suggestedFile = new File(lastSharedFolder, ucm.getName());
        }

        RamFileBrowser browser = new RamFileBrowser(RamFileBrowserType.SAVE, Constants.UCM_FILE_EXTENSION,
                suggestedFile, rootConcernFolder);

        // TODO: Threaded loading can cause MT4J to crash during drawAndUpdate
        // browser.setCallbackThreaded(true);

        // CHECKSTYLE:IGNORE AnonInnerLength FOR 1 LINES: Okay here
        browser.addFileBrowserListener(new RamFileBrowserListener() {

            @Override
            public void fileSelected(File file, RamFileBrowser fileBrowser) {
                if (!file.exists()) {
                    try {
                        if (!file.getParentFile().exists()) {
                            // create parent directory if someone's trying to be tricky.
                            file.getParentFile().mkdirs();
                        }
                        file.createNewFile();
                    } catch (final IOException e) {
                        // TODO display something to user
                        e.printStackTrace();
                    }
                }
                try {
                    // Check permission
                    if (!file.canWrite()) {
                        throw new IOException("Permission denied");
                    }

                    UseCaseMap ucmToSave = ucm;
                    // if the file/ucm is already saved in a resource it should be copied
                    // but only if the file name differs
                    // otherwise the old resource will be empty
                    // if the ucm was created new it means it has no eResource because
                    // it has never been saved
                    // if we are overwriting a file then dont create a new resource
                    if (ucm.eResource() != null
                            && ucm.eResource().getURI().equals(URI.createFileURI(file.getAbsolutePath()))) {
                        // Save only resources that have actually changed.
                        final Map<Object, Object> saveOptions = new HashMap<Object, Object>();
                        saveOptions.put(
                                Resource.OPTION_SAVE_ONLY_IF_CHANGED,
                                Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
                        ucm.eResource().save(saveOptions);
                        listener.ucmSaved(file);
                    } else {
                        if (ucm.eResource() != null) {
                            ucmToSave = EcoreUtil.copy(ucm);
                        }

                        ResourceManager.saveModel(ucmToSave, file.getAbsolutePath());
                        if (listener != null) {
                            lastSharedFolder = file.getParentFile();
                            listener.ucmSaved(file);
                        }
                    }
                } catch (IOException e) {
                    RamApp.getActiveAspectScene()
                            .displayPopup(Strings.POPUP_ERROR_FILE_SAVE + e.getMessage(), PopupType.ERROR);
                }
            }
        });
        browser.display();
    }

}
