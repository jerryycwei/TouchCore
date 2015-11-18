package ca.mcgill.sel.ram.ui.scenes;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.commons.emf.util.EMFModelUtil;
import ca.mcgill.sel.core.COREConcern;
import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.core.COREFeatureModel;
import ca.mcgill.sel.core.COREImpactModel;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.core.COREModel;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.core.impl.LayoutContainerMapImpl;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.RamPackage;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.ConfirmPopup;
import ca.mcgill.sel.ram.ui.components.RamPanelComponent.HorizontalStick;
import ca.mcgill.sel.ram.ui.components.RamPanelComponent.VerticalStick;
import ca.mcgill.sel.ram.ui.components.browser.AspectFileBrowser;
import ca.mcgill.sel.ram.ui.scenes.handler.IConcernEditSceneHandler;
import ca.mcgill.sel.ram.ui.utils.Colors;
import ca.mcgill.sel.ram.ui.utils.Constants;
import ca.mcgill.sel.ram.ui.utils.Icons;
import ca.mcgill.sel.ram.ui.utils.Strings;
import ca.mcgill.sel.ram.ui.views.containers.COREAspectContainer;
import ca.mcgill.sel.ram.ui.views.containers.COREConstraintContainer;
import ca.mcgill.sel.ram.ui.views.containers.COREImpactConcernEditContainer;
import ca.mcgill.sel.ram.ui.views.feature.FeatureDiagramEditView;
import ca.mcgill.sel.ram.ui.views.feature.FeatureView;
import ca.mcgill.sel.ram.ui.views.feature.helpers.Constraint;
import ca.mcgill.sel.ram.ui.views.feature.helpers.Constraint.ConstraintType;
import ca.mcgill.sel.ram.ui.views.handler.HandlerFactory;
import ca.mcgill.sel.ucm.UCMPackage;
import ca.mcgill.sel.ucm.UseCaseMap;

/**
 * Class for the FeatureModel scene of the concern.
 *
 * @author Nishanth
 * @author jerrywei
 */
public class DisplayConcernEditScene extends AbstractConcernScene<FeatureDiagramEditView, IConcernEditSceneHandler>
        implements INotifyChangedListener {

    private static final String ACTION_MENU = "display.menu";
    private static final String ACTION_NEW_ASPECT = "new.aspect";
    private static final String ACTION_NEW_IMPACT_MODEL = "new.impact.model";
    private static final String ACTION_SHOW_HIDE_REUSE = "show.hide.reuse";
    private static final String ACTION_SHOW_HIDE_REEXPOSED = "show.hide.reexposed";
    private static final String ACTION_EXPAND_FEATURES = "expand.feature";

    // name for sub menues
    private static final String SUBMENU_REUSES = "sub.reuses";
    private static final String ACTION_NEW_UCM = "new.ucm";

    /**
     * Boolean value to hold whether the reuses should be displayed or not.
     */
    private boolean showReuses;
    /**
     * Boolean value to hold whether the reexposed features from reuses should be displayed or not.
     */
    private boolean showReexposed;

    private COREConstraintContainer constraintContainer;
    private COREAspectContainer aspectContainer;
    private COREImpactConcernEditContainer impactEditContainer;

    /**
     * Constructor called when the concern is loaded. Initializes everything and adds to the TopLayer.
     *
     * @param app - The current {@link RamApp}
     * @param concern - The {@link COREConcern} to display
     */
    public DisplayConcernEditScene(RamApp app, COREConcern concern) {

        // Calling the constructor of the Abstract scene with the name of the concern
        super(app, concern.getName(), true);

        // Assigning the concern and the filePath
        this.concern = concern;
        filePath = new File(concern.eResource().getURI().trimSegments(1).toFileString());

        concernRectangle = new MTRectangle(app, getWidth(), getHeight());
        concernRectangle.setFillColor(Colors.BACKGROUND_COLOR);
        concernRectangle.setNoFill(false);
        concernRectangle.setPickable(false);
        concernRectangle.unregisterAllInputProcessors();

        EMFEditUtil.addListenerFor(concern.getFeatureModel(), this);
        EMFEditUtil.addListenerFor(concern, this);
        COREImpactModel im = concern.getImpactModel();
        if (im != null) {
            EMFEditUtil.addListenerFor(im, this);
        }

        replot();

        setCommandStackListener(concern);
    }

    @Override
    public boolean destroy() {
        EMFEditUtil.removeListenerFor(concern.getFeatureModel(), this);
        EMFEditUtil.removeListenerFor(concern, this);
        COREImpactModel im = concern.getImpactModel();
        if (im != null) {
            EMFEditUtil.removeListenerFor(im, this);
        }

        return super.destroy();
    }

    /**
     * Getter for the showReuse boolean.
     *
     * @return showReuses
     */
    public boolean isShowReuses() {
        return showReuses;
    }

    /**
     * Getter for the {@link Aspect} selector container.
     *
     * @return - The {@link COREAspectContainer}
     */
    public COREAspectContainer getAspectSelector() {
        return aspectContainer;
    }

    /**
     * Get all aspects in the model of the concern.
     *
     * @return the list of the concern's aspects
     */
    private Collection<COREModel> getAllRealisationModels() {
        Collection<COREModel> allRealisationModels = new ArrayList<COREModel>();
        Collection<? extends COREModel> aspects = EMFModelUtil.collectElementsOfType(concern,
                CorePackage.Literals.CORE_CONCERN__MODELS, RamPackage.eINSTANCE.getAspect());
        Collection<? extends COREModel> ucms = EMFModelUtil.collectElementsOfType(concern,
                CorePackage.Literals.CORE_CONCERN__MODELS, UCMPackage.eINSTANCE.getUseCaseMap());
        allRealisationModels.addAll(aspects);
        allRealisationModels.addAll(ucms);
        return allRealisationModels;
    }

    /**
     * Checks whether an aspect is selected in the {@link COREAspectContainer} or not.
     *
     * @return true if an Aspect was selected, false otherwise
     */
    public boolean isAspectSelected() {
        return getAspectSelector() != null && getAspectSelector().getSelected() != null;
    }

    /**
     * Shows a confirm popup for the given aspect to ask the user whether the aspect should be saved.
     *
     * @param parent the scene where the popup should be displayed, usually the current scene
     * @param listener the listener to inform which option the user selected
     */
    public void showCloseConfirmPopup(RamAbstractScene<?> parent, ConfirmPopup.SelectionListener listener) {
        String message = Strings.MODEL_CONCERN + " " + concern.getName() + Strings.POPUP_MODIFIED_SAVE;
        ConfirmPopup saveConfirmPopup = new ConfirmPopup(message, ConfirmPopup.OptionType.YES_NO_CANCEL);
        saveConfirmPopup.setListener(listener);

        parent.displayPopup(saveConfirmPopup);
    }

    @Override
    public void onEnter() {
        AspectFileBrowser.setInitialFolder(filePath.getAbsolutePath());
        AspectFileBrowser.setRootFolder(filePath.getAbsolutePath());

        // Called because when going back from an aspect scene, resources are unloaded for the aspect
        // so the aspect in the container is a proxy. This updates all list elements to not be proxies.
        if (aspectContainer != null) {
            aspectContainer.refreshContent();
        }
    }

    /*
     * --------------------- DISPLAY -------------------
     */
    /**
     * Function called to re-traverse from the root and plot again.
     */
    @Override
    protected void replot() {
        // Get the root of the feature model
        COREFeatureModel fm = concern.getFeatureModel();
        root = fm.getRoot();

        // Container for the aspects defined in the concern
        aspectContainer = new COREAspectContainer(concern, this);
        aspectContainer.setElements(getAllRealisationModels());

        // Container for the constraints defined in the concern
        constraintContainer = new COREConstraintContainer(HorizontalStick.RIGHT, VerticalStick.BOTTOM, true);
        constraintContainer.setElements(getConstraints(root));

        // Container for the goals defined in the concern's impact model
        impactEditContainer = new COREImpactConcernEditContainer(concern, this);

        // Draw feature diagram
        redrawFeatureDiagram(true);

        containerLayer.addChild(aspectContainer);
        containerLayer.addChild(constraintContainer);
        containerLayer.addChild(impactEditContainer);
    }

    /**
     * Redraw only the feature diagram.
     */
    @Override
    public void redrawFeatureDiagram(boolean repopulate) {
        // Keep the same position and scaling as before
        if (featureDiagramView == null) {
            featureDiagramView = new FeatureDiagramEditView(getWidth(), getHeight(), this);
            containerLayer.addChild(featureDiagramView);
            featureDiagramView.setHandler(HandlerFactory.INSTANCE.getFeatureDiagramEditHandler());
        } else {
            featureDiagramView.updateFeaturesDisplay(repopulate);
        }

        // change listeners depending if we are in association or edit mode
        if (isAspectSelected()) {
            updateColorsFromSelection();
            featureDiagramView.changeHandlers(HandlerFactory.INSTANCE.getFeatureAssociationModeHandler());
        } else {
            featureDiagramView.changeHandlers(HandlerFactory.INSTANCE.getFeatureEditModeHandler());
        }
    }

    /**
     * Function called when a color needs to be assigned based on selection in aspect container. All the features
     * realized the aspects will turn Red and their aspects cleared.
     */
    public void updateColorsFromSelection() {
        featureDiagramView.reAssignColors(aspectContainer.getSelected());
    }

    /*
     * --------------------- BEHAVIOR ---------------------
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        String actionCommand = event.getActionCommand();
        if (ACTION_MENU.equals(actionCommand)) {
            handler.switchToHome(this);
        } else if (ACTION_NEW_ASPECT.equals(actionCommand)) {
            handler.createAspect(this);
        } else if (ACTION_NEW_UCM.equals(actionCommand)) {
            handler.createUCM(this);
        } else if (ACTION_NEW_IMPACT_MODEL.equals(actionCommand)) {
            handler.createImpactModel(this);
        } else if (ACTION_SHOW_HIDE_REUSE.equals(actionCommand)) {
            switchShowHideReuse();
        } else if (ACTION_SHOW_HIDE_REEXPOSED.equals(actionCommand)) {
            switchShowHideReexposed();
        } else if (ACTION_EXPAND_FEATURES.equals(actionCommand)) {
            featureDiagramView.expandAllFeatures();
        } else {
            super.actionPerformed(event);
        }
    }

    @Override
    public void notifyChanged(Notification notification) {
        if (notification.getFeature() == CorePackage.Literals.CORE_CONCERN__MODELS) {
            // if an aspect has been added or removed, update the list
            if (notification.getEventType() == Notification.ADD) {
                if (notification.getNewValue() instanceof Aspect) {
                    aspectContainer.addElement((Aspect) notification.getNewValue());
                } else if (notification.getNewValue() instanceof UseCaseMap) {
                    aspectContainer.addElement((UseCaseMap) notification.getNewValue());
                }
            } else if (notification.getEventType() == Notification.REMOVE
                    && notification.getOldValue() instanceof COREModel) {
                COREModel coreModel = null;
                if (notification.getOldValue() instanceof Aspect) {
                    coreModel = (Aspect) notification.getOldValue();

                } else if (notification.getOldValue() instanceof UseCaseMap) {
                    coreModel = (UseCaseMap) notification.getOldValue();
                }
                if (coreModel.eIsProxy()) {
                    coreModel = (COREModel) EcoreUtil.resolve(coreModel, concern);
                }
                aspectContainer.removeElement(coreModel);

            }
        } else if (notification.getFeature() == CorePackage.Literals.CORE_CONCERN__IMPACT_MODEL) {
            COREImpactModel im;
            switch (notification.getEventType()) {
                case Notification.SET:
                    im = (COREImpactModel) notification.getNewValue();
                    if (im != null) {
                        EMFEditUtil.addListenerFor(im, this);
                        if (impactEditContainer != null) {
                            impactEditContainer.setImpactModel(im);
                        }
                        break;
                    }
                case Notification.UNSET:
                    im = (COREImpactModel) notification.getOldValue();
                    EMFEditUtil.removeListenerFor(im, this);
                    break;
            }
        } else if (notification.getFeature() == CorePackage.Literals.CORE_IMPACT_MODEL__LAYOUTS) {
            if (this == RamApp.getActiveScene()) {
                final LayoutContainerMapImpl containerMapImpl;
                switch (notification.getEventType()) {
                    case Notification.ADD:
                        containerMapImpl = (LayoutContainerMapImpl) notification.getNewValue();

                        containerMapImpl.eAdapters().add(new EContentAdapter() {
                            @Override
                            public void notifyChanged(final Notification notif) {
                                if (notif.getFeature() == CorePackage.Literals.LAYOUT_CONTAINER_MAP__VALUE) {
                                    final Entry<?, ?> entry;
                                    switch (notif.getEventType()) {
                                        case Notification.ADD:
                                            containerMapImpl.eAdapters().remove(this);
                                            entry = (Entry<?, ?>) notif.getNewValue();
                                            final COREImpactModel im = concern.getImpactModel();

                                            RamApp.getApplication().invokeLater(new Runnable() {
                                                @Override
                                                public void run() {
                                                    getApplication().displayEditImpactModel(concern, im, im.getName(),
                                                            (COREImpactNode) entry.getKey());
                                                }
                                            });
                                            break;
                                    }
                                }
                            }
                        });
                        break;
                }
            }
        } else if (notification.getFeature() == CorePackage.Literals.CORE_FEATURE__EXCLUDES
                || notification.getFeature() == CorePackage.Literals.CORE_FEATURE__REQUIRES) {
            ConstraintType type = (notification.getFeature() == CorePackage.Literals.CORE_FEATURE__EXCLUDES)
                    ? ConstraintType.EXCLUDES : ConstraintType.REQUIRES;
            COREFeature owner = (COREFeature) notification.getNotifier();
            COREFeature target;
            switch (notification.getEventType()) {
                case Notification.ADD:
                    target = (COREFeature) notification.getNewValue();
                    constraintContainer.addElement(new Constraint(owner, target, type));
                    break;
                case Notification.REMOVE:
                    target = (COREFeature) notification.getOldValue();
                    constraintContainer.removeElement(new Constraint(owner, target, type));
                    break;
            }
        }

    }

    /**
     * Function called when the show// hide reuse button need to be interchanged and switched accordingly.
     */
    public void switchShowHideReuse() {
        if (!showReuses) {
            showReuses = true;
            menu.toggleAction(showReuses, ACTION_SHOW_HIDE_REUSE);
            // Add button for re-exposed features
            menu.addAction(Strings.MENU_SHOW_REEXPOSED, Strings.MENU_HIDE_REEXPOSED,
                    Icons.ICON_MENU_SHOW_REEXPOSED, Icons.ICON_MENU_HIDE_REEEXPOSED, ACTION_SHOW_HIDE_REEXPOSED, this,
                    SUBMENU_REUSES, true, false);
        } else {
            showReuses = false;
            menu.toggleAction(showReuses, ACTION_SHOW_HIDE_REUSE);
            menu.removeAction(ACTION_SHOW_HIDE_REEXPOSED);
        }
        featureDiagramView.switchShowReuses();
    }

    /**
     * Function called when the show// hide reuse button need to be interchanged and switched accordingly.
     */
    private void switchShowHideReexposed() {
        if (!showReuses) {
            return;
        }
        showReexposed = !showReexposed;
        menu.toggleAction(showReexposed, ACTION_SHOW_HIDE_REEXPOSED);

        featureDiagramView.switchShowReexposed();
    }

    /**
     * Switch from normal edit mode to association mode.
     * Update the handlers for the features and expand collapsed features
     */
    public void switchToAssociationMode() {
        if (featureDiagramView.hasCollapsedElements(false)) {
            featureDiagramView.updateFeaturesDisplay(true);
        } else {
            updateColorsFromSelection();
        }
        featureDiagramView.changeHandlers(HandlerFactory.INSTANCE.getFeatureAssociationModeHandler());
    }

    /**
     * Switch from association mode to normal edit mode.
     * Un-select everything from the aspect container and update the handlers for the features.
     */
    public void switchToEditMode() {
        if (isAspectSelected()) {
            aspectContainer.unselect(this);
            return;
        }
        if (featureDiagramView.hasCollapsedElements(false)) {
            featureDiagramView.updateFeaturesDisplay(true);
        } else {
            updateColorsFromSelection();
        }
        featureDiagramView.changeHandlers(HandlerFactory.INSTANCE.getFeatureEditModeHandler());
    }

    /*
     * ---------------------- COLLAPSE -----------------------------
     */
    /**
     * Update the status of the 'expand all' button.
     * Check if features have to be removed from the collapsed feature list and if so remove them.
     */
    public void updateExpandButton() {
        // Remove features that can no longer be collapsed
        featureDiagramView.checkCollapseValidity();
        menu.enableAction(featureDiagramView.hasCollapsedElements(true), ACTION_EXPAND_FEATURES);
    }

    /**
     * Hide features in the view.
     * Children feature of the given feature are collapsed and will not be displayed
     *
     * @param view - The {@link FeatureView} to hide
     */
    public void switchCollapse(FeatureView view) {
        featureDiagramView.switchCollapse(view);
    }

    /**
     * Check if the given gestures corresponds to an XOR curve.
     *
     * @param startPosX - Start position of the gesture in X axis
     * @param startPosY - Start position of the gesture in Y axis
     * @param endPosX - End position of the gesture in X axis
     * @param endPosY - End position of the gesture in Y axis
     * @return the {@link FeatureView} under the gesture, if any.
     */
    public FeatureView isXORCurve(float startPosX, float startPosY, float endPosX, float endPosY) {
        return featureDiagramView.isXORCurve(startPosX, startPosY, endPosX, endPosY);
    }

    @Override
    protected void initMenu() {
        this.getMenu().addAction(Strings.MENU_HOME, Icons.ICON_MENU_HOME, ACTION_MENU, this, true);
        this.getMenu().addSubMenu(1, Constants.MENU_EXTRA);
        this.getMenu().addAction(Strings.MENU_NEW_ASPECT, Icons.ICON_MENU_ADD_ASPECT, ACTION_NEW_ASPECT, this,
                Constants.MENU_EXTRA,
                true);
        this.getMenu().addAction(Strings.MENU_NEW_IMPACT_ROOT, Icons.ICON_MENU_NEW_ROOT_GOAL, ACTION_NEW_IMPACT_MODEL,
                this, Constants.MENU_EXTRA, true);

        // Creates new UCM in ConcernEditSceneHandler which then loads the UCM into a new DisplayUCMScene in RamApp
        this.getMenu().addAction("New Use Case Map", Icons.ICON_MENU_ADD_UCM, ACTION_NEW_UCM, this,
                Constants.MENU_EXTRA,
                true);

        // Reuses
        this.getMenu().addSubMenu(1, SUBMENU_REUSES);
        // Expand
        this.getMenu().addAction(Strings.MENU_EXPAND_ALL, Icons.ICON_MENU_EXPAND_ALL_FEATURES, ACTION_EXPAND_FEATURES,
                this, SUBMENU_REUSES, true);
        this.getMenu().enableAction(false, ACTION_EXPAND_FEATURES);

        this.getMenu().addAction(Strings.MENU_SHOW_REUSES, Strings.MENU_HIDE_REUSES, Icons.ICON_MENU_SHOW_REUSE,
                Icons.ICON_MENU_HIDE_REUSE, ACTION_SHOW_HIDE_REUSE, this, SUBMENU_REUSES, true, false);

    }

    @Override
    protected EObject getElementToSave() {
        return concern;
    }

}
