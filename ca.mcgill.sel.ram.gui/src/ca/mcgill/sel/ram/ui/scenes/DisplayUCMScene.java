package ca.mcgill.sel.ram.ui.scenes;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.INotifyChangedListener;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.ConfirmPopup;
import ca.mcgill.sel.ram.ui.components.ConfirmPopup.OptionType;
import ca.mcgill.sel.ram.ui.components.RamPanelComponent;
import ca.mcgill.sel.ram.ui.components.listeners.RamPanelListener;
import ca.mcgill.sel.ram.ui.scenes.handler.impl.UCMSceneHandler;
import ca.mcgill.sel.ram.ui.utils.Constants;
import ca.mcgill.sel.ram.ui.utils.Fonts;
import ca.mcgill.sel.ram.ui.utils.Icons;
import ca.mcgill.sel.ram.ui.utils.LoggerUtils;
import ca.mcgill.sel.ram.ui.utils.Strings;
import ca.mcgill.sel.ram.ui.views.TextView;
import ca.mcgill.sel.ram.ui.views.handler.HandlerFactory;
import ca.mcgill.sel.ram.ui.views.ucm.UCMDiagramView;
import ca.mcgill.sel.ram.ui.views.ucm.handler.HandlerFactoryUCM;
import ca.mcgill.sel.ucm.UseCaseMap;

/**
 * This scene displays everything relevant to a ucm.
 *
 * @author jerrywei
 *
 */
public class DisplayUCMScene extends RamAbstractScene<UCMSceneHandler>
        implements INotifyChangedListener, RamPanelListener {

    private static final String ACTION_BACK = "display.back";

    private UseCaseMap useCaseMap;

    private UCMDiagramView ucmDiagramView;

    private TextView ucmName;

    /**
     * Creates a scene that will display this ucm.
     *
     * @param app The application to which the scene belongs.
     * @param name The name of this scene
     * @param ucm The ucm this Scene will display.
     * @param defaultActions Whether to show default menu
     */
    public DisplayUCMScene(final RamApp app, String name, UseCaseMap ucm, boolean defaultActions) {
        super(app, name, defaultActions);

        useCaseMap = ucm;

        EMFEditUtil.addListenerFor(ucm, this);
        setCommandStackListener(ucm);

        displayUcmDiagramView();
        buildUcmName();

    }

    /**
     * Build the text view containing the name of the UCM.
     */
    private void buildUcmName() {

        ucmName = new TextView(useCaseMap, CorePackage.Literals.CORE_NAMED_ELEMENT__NAME);
        ucmName.setFont(Fonts.SCENE_FONT);
        ucmName.setPlaceholderFont(Fonts.SCENE_PLACEHOLDER_FONT);

        ucmName.setPlaceholderText(Strings.PH_ENTER_USE_CASE_MAP_NAME);

        containerLayer.addChild(ucmName);
        ucmName.translate(Constants.MENU_BAR_HEIGHT, Constants.MENU_BAR_HEIGHT);

        ucmName.registerTapProcessor(HandlerFactory.INSTANCE.getTextViewHandler());
    }

    /**
     * Create a new UCM Diagram View and add to scene.
     */
    private void displayUcmDiagramView() {
        ucmDiagramView = new UCMDiagramView(useCaseMap, getWidth(), getHeight());
        containerLayer.addChild(ucmDiagramView);
        ucmDiagramView.setHandler(HandlerFactoryUCM.INSTANCE.getUCMDiagramViewHandler());
    }

    @Override
    protected EObject getElementToSave() {
        return useCaseMap;
    }

    @Override
    protected void initMenu() {
        menu.addAction(Strings.MENU_BACK, Icons.ICON_MENU_BACK, ACTION_BACK, this, true);
    }

    @Override
    public void notifyChanged(Notification notification) {

    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (handler != null) {
            String actionCommand = event.getActionCommand();
            if (ACTION_BACK.equals(actionCommand)) {
                handler.switchToConcern(this);
            } else {
                super.actionPerformed(event);
            }
        } else {
            LoggerUtils.warn("No handler set for " + this.getClass().getName());
        }
    }

    @Override
    public void panelClosed(RamPanelComponent panel) {

    }

    /**
     * Shows a confirm popup for the given ucm to ask the user whether the ucm should be saved.
     *
     * @param parent
     *            the scene where the popup should be displayed, usually the current scene
     * @param listener
     *            the listener to inform which option the user selected
     */
    public void showCloseConfirmPopup(RamAbstractScene<?> parent, ConfirmPopup.SelectionListener listener) {
        showCloseConfirmPopup(parent, listener, OptionType.YES_NO_CANCEL);
    }

    /**
     * Shows a confirm popup for the given ucm to ask the user whether the ucm should be saved.
     *
     * @param parent the scene where the popup should be displayed, usually the current scene
     * @param listener the listener to inform which option the user selected
     * @param options the buttons to display in the popup
     */
    public void showCloseConfirmPopup(RamAbstractScene<?> parent, ConfirmPopup.SelectionListener listener,
            OptionType options) {
        String message = Strings.MODEL_UCM + " " + useCaseMap.getName() + Strings.POPUP_MODIFIED_SAVE;
        ConfirmPopup saveConfirmPopup = new ConfirmPopup(message, options);
        saveConfirmPopup.setListener(listener);

        parent.displayPopup(saveConfirmPopup);
    }

    /**
     * Returns the ucm.
     *
     * @return The ucm that this scene is displaying.
     */
    public UseCaseMap getUCM() {
        if (useCaseMap != null) {
            return useCaseMap;

        }
        return null;
    }

}