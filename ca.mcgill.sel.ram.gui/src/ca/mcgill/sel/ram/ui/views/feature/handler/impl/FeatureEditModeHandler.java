package ca.mcgill.sel.ram.ui.views.feature.handler.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.sceneManagement.transition.SlideTransition;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.core.COREConcern;
import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.core.COREFeatureModel;
import ca.mcgill.sel.core.COREFeatureRelationshipType;
import ca.mcgill.sel.core.COREModel;
import ca.mcgill.sel.core.COREReuse;
import ca.mcgill.sel.core.controller.ControllerFactory;
import ca.mcgill.sel.core.controller.FeatureController;
import ca.mcgill.sel.core.util.COREModelUtil;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamListComponent.Namer;
import ca.mcgill.sel.ram.ui.components.RamRectangleComponent;
import ca.mcgill.sel.ram.ui.components.RamSelectorComponent;
import ca.mcgill.sel.ram.ui.components.RamTextComponent;
import ca.mcgill.sel.ram.ui.components.browser.AspectFileBrowser;
import ca.mcgill.sel.ram.ui.components.browser.UcmFileBrowser;
import ca.mcgill.sel.ram.ui.components.browser.interfaces.AspectFileBrowserListener;
import ca.mcgill.sel.ram.ui.components.browser.interfaces.UcmFileBrowserListener;
import ca.mcgill.sel.ram.ui.components.listeners.AbstractDefaultRamSelectorListener;
import ca.mcgill.sel.ram.ui.components.listeners.RamSelectorListener;
import ca.mcgill.sel.ram.ui.events.DelayedDrag;
import ca.mcgill.sel.ram.ui.scenes.AbstractConcernScene;
import ca.mcgill.sel.ram.ui.scenes.DisplayConcernEditScene;
import ca.mcgill.sel.ram.ui.scenes.RamAbstractScene;
import ca.mcgill.sel.ram.ui.utils.Colors;
import ca.mcgill.sel.ram.ui.utils.Constants;
import ca.mcgill.sel.ram.ui.utils.Strings;
import ca.mcgill.sel.ram.ui.views.OptionSelectorView;
import ca.mcgill.sel.ram.ui.views.feature.FeatureView;
import ca.mcgill.sel.ram.ui.views.feature.handler.IFeatureHandler;
import ca.mcgill.sel.ram.ui.views.handler.BaseHandler;
import ca.mcgill.sel.ucm.UseCaseMap;

/**
 * Feature Handler, used to handle events on the Feature.
 *
 * @author Nishanth
 * @author jerrywei
 */
public class FeatureEditModeHandler extends BaseHandler implements IFeatureHandler {

    private DelayedDrag dragAction = new DelayedDrag(Constants.DELAYED_DRAG_MIN_DRAG_DISTANCE);

    /**
     * Enum containing all the options to be shown when tap and hold is complete.
     */
    private enum CreateFeature {
        NEW_ASPECT, NEW_USE_CASE_MAP, RENAME_FEATURE, DELETE_FEATURE, ADD_OPTIONAL, ADD_MANDATORY, ADD_XOR, ADD_OR,
        ASSOCIATE_ASPECT, ASSOCIATE_UCM, COLLAPSE, EXPAND,
    }

    /**
     * Namer for the selector of models to show.
     */
    private class ModelSelectorNamer implements Namer<COREModel> {
        @Override
        public RamRectangleComponent getDisplayComponent(COREModel element) {
            RamTextComponent textComponent = new RamTextComponent(element.getName());
            textComponent.setNoFill(false);
            textComponent.setFillColor(Colors.DEFAULT_ELEMENT_FILL_COLOR);
            textComponent.setAutoMaximizes(true);
            textComponent.setNoStroke(false);
            textComponent.setStrokeColor(Colors.DEFAULT_ELEMENT_STROKE_COLOR);
            return textComponent;
        }

        @Override
        public String getSortingName(COREModel element) {
            return element.getName();
        }

        @Override
        public String getSearchingName(COREModel element) {
            return element.getName();
        }
    }

    /**
     * Listener for the selector of aspects to show.
     */
    private class ModelSelectorListener implements RamSelectorListener<COREModel> {
        @Override
        public void elementSelected(RamSelectorComponent<COREModel> selector, COREModel element) {
            showModel(element);
            selector.destroy();
        }

        @Override
        public void elementHeld(RamSelectorComponent<COREModel> selector, COREModel element) {
        }

        @Override
        public void elementDoubleClicked(RamSelectorComponent<COREModel> selector, COREModel element) {
        }

        @Override
        public void closeSelector(RamSelectorComponent<COREModel> selector) {
            selector.destroy();
        }
    }

    @Override
    public boolean processTapAndHoldEvent(TapAndHoldEvent tapAndHoldEvent) {
        // Get the current working concern scene.
        DisplayConcernEditScene concernScene = RamApp.getActiveConcernEditScene();

        if (tapAndHoldEvent.isHoldComplete() && concernScene != null) {
            FeatureView featureIcon = (FeatureView) tapAndHoldEvent.getTarget();
            showFeatureOptions(concernScene, featureIcon, tapAndHoldEvent.getLocationOnScreen());
        }
        return true;
    }

    /**
     * Shows the options for the feature.
     *
     * @param scene the current scene
     * @param view the view of the selected feature
     * @param location the location on the screen where the options should be shown
     */
    private void showFeatureOptions(final DisplayConcernEditScene scene, final FeatureView view, Vector3D location) {

        OptionSelectorView<CreateFeature> selector = new OptionSelectorView<CreateFeature>(getActions(scene, view));

        scene.addComponent(selector, location);

        // CHECKSTYLE:IGNORE AnonInnerLength: They can be more than 30 lines
        selector.registerListener(new AbstractDefaultRamSelectorListener<CreateFeature>() {
            @Override
            public void elementSelected(RamSelectorComponent<CreateFeature> selector, CreateFeature element) {
                switch (element) {
                    case RENAME_FEATURE:
                        view.getHandler().renameFeature(view);
                        break;
                    case ADD_OPTIONAL:
                        addChild(scene, view.getFeature(), -1, COREFeatureRelationshipType.OPTIONAL);
                        break;
                    case ADD_MANDATORY:
                        addChild(scene, view.getFeature(), -1, COREFeatureRelationshipType.MANDATORY);
                        break;
                    case ADD_XOR:
                        addChild(scene, view.getFeature(), -1, COREFeatureRelationshipType.XOR);
                        break;
                    case ADD_OR:
                        addChild(scene, view.getFeature(), -1, COREFeatureRelationshipType.OR);
                        break;
                    case DELETE_FEATURE:
                        view.getHandler().deleteFeature(scene, view);
                        break;
                    case NEW_ASPECT:
                        Aspect aspect = ControllerFactory.INSTANCE.getFeatureController().createAspectAndAssociate(
                                scene.getConcern(), view.getFeature());
                        showModel(aspect);
                        break;

                    // Creates a new UCM for the tapped-and-held feature view by calling UCMController which then uses
                    // UCMModelUtil to createUCM with given feature. Then uses ShowModel to use RamApp to load the UCM
                    // into a DisplayUCMScene (same process as when creating an untitled UCM with RamMenu Button in
                    // DisplayConcernEditScene)
                    case NEW_USE_CASE_MAP:
                        UseCaseMap ucm =
                                ca.mcgill.sel.ucm.controller.ControllerFactory.INSTANCE.getUseCaseMapController()
                                        .createUCMAndAssociate(
                                                scene.getConcern(), view.getFeature());
                        showModel(ucm);
                        break;
                    case ASSOCIATE_UCM:
                        loadBrowserAndAssociateUCM(view);
                        break;
                    case ASSOCIATE_ASPECT:
                        loadBrowserAndAssociate(view);
                        break;
                    case COLLAPSE:
                    case EXPAND:
                        view.getHandler().hideFeature(scene, view);
                        break;

                }
            }
        });
    }

    /**
     * Get the possible actions on the given feature.
     *
     * @param scene - The current scene
     * @param feature - The given {@link FeatureView}
     * @return the set of available actions
     */
    private static CreateFeature[] getActions(DisplayConcernEditScene scene, FeatureView feature) {
        EnumSet<CreateFeature> options = EnumSet.allOf(CreateFeature.class);
        if (feature.getIsRoot()) {
            options.remove(CreateFeature.RENAME_FEATURE);
            options.remove(CreateFeature.DELETE_FEATURE);
            options.remove(CreateFeature.COLLAPSE);
            options.remove(CreateFeature.EXPAND);
        } else if (scene.isAspectSelected() && !feature.isReuse() && !feature.isParentReuse()) {
            options.remove(CreateFeature.COLLAPSE);
            options.remove(CreateFeature.EXPAND);
        } else if (scene.isShowReuses() && feature.getChildrenFeatureViews().isEmpty()) {
            options.remove(CreateFeature.COLLAPSE);
            options.remove(CreateFeature.EXPAND);
        } else if (!scene.isShowReuses() && feature.getFeature().getChildren().isEmpty()) {
            options.remove(CreateFeature.COLLAPSE);
            options.remove(CreateFeature.EXPAND);
        } else if (!scene.isFeatureCollapsed(feature)) {
            options.remove(CreateFeature.EXPAND);
        } else {
            options.remove(CreateFeature.COLLAPSE);
        }
        return options.toArray(new CreateFeature[0]);
    }

    /**
     * Function called, when an external aspect is to be loaded and associated with a feature.
     *
     * @param view - The {@link FeatureView} we want to associate with the new aspect
     */
    private static void loadBrowserAndAssociate(final FeatureView view) {
        AspectFileBrowser.loadAspect(new AspectFileBrowserListener() {

            @Override
            public void aspectLoaded(Aspect aspect) {
                COREFeatureModel fM = (COREFeatureModel) view.getFeature().eContainer();
                COREConcern concern = (COREConcern) fM.eContainer();
                ControllerFactory.INSTANCE.getFeatureController().associateAspect(concern, view.getFeature(), aspect);
            }

            @Override
            public void aspectSaved(File file) {
            }
        });
    }

    /**
     * Function called, when an external ucm is to be loaded and associated with a feature.
     *
     * @param view - The {@link FeatureView} we want to associate with the new ucm
     */
    private static void loadBrowserAndAssociateUCM(final FeatureView view) {
        UcmFileBrowser.loadUCM(new UcmFileBrowserListener() {

            @Override
            public void ucmLoaded(UseCaseMap ucm) {
                COREFeatureModel fM = (COREFeatureModel) view.getFeature().eContainer();
                COREConcern concern = (COREConcern) fM.eContainer();
                ControllerFactory.INSTANCE.getFeatureController().associateUCM(concern, view.getFeature(), ucm);
            }

            @Override
            public void ucmSaved(File file) {
            }
        });
    }

    @Override
    public boolean processTapEvent(final TapEvent tapEvent) {

        RamAbstractScene<?> scene = RamApp.getActiveScene();

        if (scene instanceof DisplayConcernEditScene) {
            FeatureView featureIcon = (FeatureView) tapEvent.getTarget();
            EList<COREModel> realizingModels = featureIcon.getFeature().getRealizedBy();

            // Since it is a double click, open the realizing aspects.
            if (tapEvent.isDoubleTap()) {
                if (realizingModels.size() == 1) {
                    showModel(realizingModels.get(0));
                } else if (realizingModels.size() > 1) {
                    RamSelectorComponent<COREModel> selector;
                    selector = new RamSelectorComponent<COREModel>(new ModelSelectorNamer());
                    selector.setElements(featureIcon.getFeature().getRealizedBy());
                    selector.registerListener(new ModelSelectorListener());
                    // Display selector
                    ((DisplayConcernEditScene) scene).addComponent(selector, tapEvent.getLocationOnScreen());
                }
            }

        }

        return true;
    }

    @Override
    public boolean processDragEvent(DragEvent dragEvent) {

        DisplayConcernEditScene scene = RamApp.getActiveConcernEditScene();
        if (scene == null) {
            return true;
        }

        FeatureView featureIcon = (FeatureView) dragEvent.getTarget();

        switch (dragEvent.getId()) {
            case MTGestureEvent.GESTURE_STARTED:
            case MTGestureEvent.GESTURE_UPDATED:
                dragAction.processGestureEvent(dragEvent);
                for (MTComponent comp : featureIcon.getAllDragContainers()) {
                    dragEvent.setTarget(comp);
                    dragAction.processGestureEvent(dragEvent);
                }
                updateFeatureColors(scene.collectFeatureViews(false), featureIcon);
                break;
            case MTGestureEvent.GESTURE_ENDED:
                if (dragAction.wasDragPerformed()) {
                    setPositionUpdate(featureIcon, scene);
                }
                // Reset highlight
                for (FeatureView fv : scene.collectFeatureViews(false)) {
                    fv.highlight(false);
                }
                break;
        }
        return true;
    }

    /**
     * Highlight the underlying {@link FeatureView} when dragging a feature.
     *
     * @param set - The set of features to examine
     * @param feature - The dragged feature
     */
    private static void updateFeatureColors(Set<FeatureView> set, FeatureView feature) {
        // reset all highlights
        for (FeatureView fv : set) {
            fv.highlight(false);
        }
        // highlight underlying feature, if any
        FeatureView featureView = getUnderlyingFeature(set, feature);
        if (featureView != null) {
            featureView.highlight(true);
            feature.highlight(true);
            return;
        }
        // Check if we are to be moved
        FeatureView parentV = feature.getParentFeatureView();
        int newPosition = getFeaturePosition(feature);
        if (newPosition == -1) {
            return;
        }
        newPosition += parentV.getFeature().getReuses().size();
        int offset = feature.getCurrentPosition() - newPosition;
        int prevPosition;
        int nextPosition;
        // To the left
        if (offset > 0) {
            prevPosition = newPosition - 1;
            nextPosition = newPosition;
        } else {
            prevPosition = newPosition;
            nextPosition = newPosition + 1;
        }
        if (prevPosition >= 0 && prevPosition < parentV.getChildrenFeatureViews().size()) {
            FeatureView prevChild = parentV.getChildrenFeatureViews().get(prevPosition);
            if (!prevChild.isReuse()) {
                prevChild.highlight(true);
                parentV.highlight(true);
            }
        }
        if (nextPosition >= 0 && nextPosition < parentV.getChildrenFeatureViews().size()) {
            FeatureView nextChild = parentV.getChildrenFeatureViews().get(nextPosition);
            nextChild.highlight(true);
            parentV.highlight(true);
        }
    }

    /**
     * Function used to translate the Feature when dragged.
     * In addition to translating the Feature,
     * all its children and the line associated with its parent has to be translated.
     *
     * @param position - The position after translation.
     * @param featureIcon - The Feature on which the drag gesture was called.
     */
    public void setNewTranslation(Vector3D position, FeatureView featureIcon) {
        // drag also the children
        for (MTComponent each : featureIcon.getAllDragContainers()) {
            each.translate(position);
        }
    }

    @Override
    public void setPositionUpdate(FeatureView featureIcon, AbstractConcernScene<?, ?> scene) {
        /*
         * First checks if a feature had to be added as a child of another feature
         * This is horizontal movement, where intersection adds it as a child.
         */
        FeatureView target = getUnderlyingFeature(scene.collectFeatureViews(false), featureIcon);
        if (target != null && target != featureIcon && !target.equals(featureIcon.getParentFeatureView())) {

            if (target.getChildrenRelationship() == COREFeatureRelationshipType.XOR
                    || target.getChildrenRelationship() == COREFeatureRelationshipType.OR) {
                ControllerFactory.INSTANCE.getFeatureController().addExistingFeature(scene.getConcern(),
                        featureIcon.getFeature(), target.getFeature(), target.getChildrenRelationship());
            } else {
                COREFeatureRelationshipType rel = featureIcon.getFeature().getParentRelationship();
                if (rel == COREFeatureRelationshipType.MANDATORY || rel == COREFeatureRelationshipType.OPTIONAL) {
                    ControllerFactory.INSTANCE.getFeatureController().addExistingFeature(scene.getConcern(),
                            featureIcon.getFeature(), target.getFeature(), rel);
                } else {
                    ControllerFactory.INSTANCE.getFeatureController().addExistingFeature(scene.getConcern(),
                            featureIcon.getFeature(), target.getFeature(), COREFeatureRelationshipType.OPTIONAL);
                }
            }
            return;
        }

        /* Check if we want to change the position of the feature */
        int newPosition = getFeaturePosition(featureIcon);
        if (newPosition != -1) {
            ControllerFactory.INSTANCE.getFeatureController().changePositionOfFeature(scene.getConcern(),
                    featureIcon.getParentFeatureView().getFeature(), featureIcon.getFeature(), newPosition);
        } else {
            // Move the feature back to its original position
            resetFeatureLocation(featureIcon, scene);
        }
    }

    /**
     * Return the given {@link FeatureView} to its default location.
     *
     * @param feature - The feature to move
     * @param scene - The scene
     */
    private void resetFeatureLocation(FeatureView feature, AbstractConcernScene<?, ?> scene) {
        Vector3D initPosition = new Vector3D(feature.getXposition(), feature.getYposition());
        Vector3D currentPosition = feature.getPosition(TransformSpace.RELATIVE_TO_PARENT);
        Vector3D translation = initPosition.subtractLocal(currentPosition);
        feature.translate(translation);
        setNewTranslation(translation, feature);
    }

    /**
     * Return the current position of a feature related to its sibling on the screen.
     * This is used when a feature is dragged to find out were it should be placed in the end.
     *
     * @param feature - The {@link FeatureView}
     * @return the new position, or -1 if the position hasn't changed
     */
    private static int getFeaturePosition(FeatureView feature) {
        int initialPosition = feature.getCurrentPosition();
        // Get the siblings
        List<FeatureView> siblings = new ArrayList<FeatureView>();
        for (FeatureView fv : feature.getParentFeatureView().getChildrenFeatureViews()) {
            if (!fv.equals(feature) && !fv.isReuse()) {
                siblings.add(fv);
            } else if (fv.isReuse() && fv.getCurrentPosition() < feature.getCurrentPosition()) {
                // if there is a reuse, index should not take it in account
                initialPosition--;
            }
        }
        int newPosition = initialPosition;
        for (FeatureView sib : siblings) {
            if (feature.getCurrentPosition() < sib.getCurrentPosition()) {
                // The feature is originally located left of its sibling
                if (feature.getCenterPointGlobal().getX() > sib.getX() + sib.getWidth()) {
                    // the feature as now moved farther to the right of the sibling
                    newPosition++;
                }
            } else {
                // The feature is originally located right of its sibling
                if (feature.getCenterPointGlobal().getX() < sib.getX()) {
                    // the feature as now moved farther to the left of the sibling
                    newPosition--;
                }
            }
        }
        if (newPosition != initialPosition) {
            return newPosition;
        }
        return -1;
    }

    /**
     * Get the {@link FeatureView} lying under the given one. Used during drag events to check where the gesture ended.
     *
     * @param list - The list of {@link FeatureView}s to examine
     * @param feature - The dragged {@link FeatureView}
     * @return the feature the gesture ended on, or null if none
     */
    private static FeatureView getUnderlyingFeature(Set<FeatureView> list, FeatureView feature) {
        list.remove(feature);
        for (FeatureView fv : list) {
            if (fv.containsPointGlobal(feature.getCenterPointGlobal())) {
                return fv;
            }
        }
        return null;
    }

    @Override
    public boolean isXORCurve(float startX, float startY, float endX, float endY, FeatureView featureIcon) {
        boolean start = false;
        boolean end = false;

        float extraWidth = (float) (0.3 * featureIcon.getWidth());

        if (startX > (featureIcon.getX() - extraWidth) && startX < (featureIcon.getX() + extraWidth)
                && startY > featureIcon.getY() && startY < (featureIcon.getY() + 1.3 * featureIcon.getHeight())) {
            start = true;

        }
        if (endX > (featureIcon.getX() + (0.7 * featureIcon.getWidth()))
                && endX < (featureIcon.getX() + 1.3 * featureIcon.getWidth()) && endY > featureIcon.getY()
                && endY < (featureIcon.getY() + 1.3 * featureIcon.getHeight())) {
            end = true;
        }

        return start && end;
    }

    /**
     * Function called to associate an aspect as RealizedBy for the Feature.
     *
     * @param concern - The concern containing the feature.
     * @param feature - The feature to be realized by the aspect
     * @param aspect - The {@link Aspect} to associate to the feature
     */
    public static void setAspect(COREConcern concern, COREFeature feature, Aspect aspect) {
        ControllerFactory.INSTANCE.getFeatureController().associateAspect(concern, feature, aspect);
    }

    /**
     * Add a child feature to the given feature.
     *
     * @param scene - the current scene
     * @param coreFeature - the feature to add a child
     * @param position - the position to add the child at
     * @param relationshipType - relationship for the child
     */
    @Override
    public void addChild(DisplayConcernEditScene scene, COREFeature coreFeature, int position,
            COREFeatureRelationshipType relationshipType) {

        final String childName = COREModelUtil.createUniqueNameFromElements(Strings.DEFAULT_FEATURE_NAME,
                new HashSet<COREFeature>(scene.getConcern().getFeatureModel().getFeatures()));

        FeatureController controller = ControllerFactory.INSTANCE.getFeatureController();

        controller.addFeature(scene.getConcern(), position, coreFeature, childName, relationshipType);

    }

    @Override
    public void renameFeature(FeatureView view) {
        if (view.getIsRoot()) {
            return;
        }
        view.showKeyboard();
    }

    @Override
    public void deleteFeature(DisplayConcernEditScene scene, FeatureView view) {
        if (view.getIsRoot() || view.isReuse()) {
            return;
        }

        // Get the feature and its children
        List<COREFeature> featuresDeleted = view.collectFeatures();

        // Check if there are any model reuse left
        for (COREFeature feature : featuresDeleted) {
            for (COREReuse reuse : feature.getReuses()) {
                if (!COREModelUtil.getModelReuses(scene.getConcern(), reuse).isEmpty()) {
                    scene.displayPopup(Strings.POPUP_CANT_DELETE_FEATURE);
                    return;
                }
            }
        }

        FeatureController controller = ControllerFactory.INSTANCE.getFeatureController();
        controller.deleteFeature(scene.getConcern(), featuresDeleted);
    }

    @Override
    public void hideFeature(DisplayConcernEditScene scene, FeatureView view) {
        scene.switchCollapse(view);
    }

    /**
     * Display the scene for a model.
     *
     * @param model - The {@link COREModel} to display
     */

    public static void showModel(COREModel model) {
        if (model instanceof Aspect) {
            RamApp.getActiveScene().setTransition(new SlideTransition(RamApp.getApplication(), 700, true));
            RamApp.getApplication().loadAspect((Aspect) model);
        }
        if (model instanceof UseCaseMap) {
            RamApp.getActiveScene().setTransition(new SlideTransition(RamApp.getApplication(), 700, true));
            RamApp.getApplication().loadUCM((UseCaseMap) model);

        }
    }

}
