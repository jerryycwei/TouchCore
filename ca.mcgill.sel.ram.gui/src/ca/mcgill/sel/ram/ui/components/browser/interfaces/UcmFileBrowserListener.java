package ca.mcgill.sel.ram.ui.components.browser.interfaces;

import java.io.File;

import ca.mcgill.sel.ucm.UseCaseMap;

/**
 *
 * @author jerrywei
 *
 */
public interface UcmFileBrowserListener {

    /**
     * Called when a ucm is loaded on a ucm file browser.
     *
     * @param aspect The loaded aspect
     */
    void ucmLoaded(UseCaseMap ucm);

    /**
     * Called when a ucm is saved on a ucm file browser.
     *
     * @param file The saved aspect file
     */
    void ucmSaved(File file);
}
