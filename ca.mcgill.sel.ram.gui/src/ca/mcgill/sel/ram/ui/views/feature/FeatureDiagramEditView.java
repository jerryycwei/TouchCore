package ca.mcgill.sel.ram.ui.views.feature;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.componentProcessors.panProcessor.PanProcessorTwoFingers;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils.Direction;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils.UnistrokeGesture;
import org.mt4j.input.inputProcessors.componentProcessors.zoomProcessor.ZoomProcessor;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.core.COREFeatureRelationshipType;
import ca.mcgill.sel.core.COREModel;
import ca.mcgill.sel.core.COREReuse;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.events.MouseWheelProcessor;
import ca.mcgill.sel.ram.ui.events.RightClickDragProcessor;
import ca.mcgill.sel.ram.ui.events.UnistrokeProcessorLeftClick;
import ca.mcgill.sel.ram.ui.scenes.DisplayConcernEditScene;
import ca.mcgill.sel.ram.ui.utils.Constants;
import ca.mcgill.sel.ram.ui.views.RelationshipView.LineStyle;
import ca.mcgill.sel.ram.ui.views.feature.FeatureDiagramSelectView.SelectionStatus;
import ca.mcgill.sel.ram.ui.views.feature.handler.IFeatureHandler;
import ca.mcgill.sel.ram.ui.views.feature.handler.impl.FeatureDiagramEditHandler;
import ca.mcgill.sel.ram.ui.views.handler.HandlerFactory;

/**
 * View used to represent a feature model. It can be edited
 * 
 * @author Nishanth
 */
public class FeatureDiagramEditView extends FeatureDiagramView<FeatureDiagramEditHandler>
        implements INotifyChangedListener {

    private DisplayConcernEditScene scene;

    private boolean showReuses;
    private boolean showReEexposed;

    /**
     * Create a feature diagram representation view.
     * 
     * @param width - The width of the view
     * @param height - The height of the view
     * @param scene - The {@link DisplayConcernEditScene} containing the view
     */
    public FeatureDiagramEditView(float width, float height, DisplayConcernEditScene scene) {
        super(width, height, scene.getConcern().getFeatureModel());
        this.scene = scene;
        EMFEditUtil.addListenerFor(featureModel, this);
    }

    @Override
    protected void registerInputProcessors() {
        registerInputProcessor(new TapProcessor(RamApp.getApplication(), Constants.TAP_MAX_FINGER_UP, false,
                Constants.TAP_DOUBLE_TAP_TIME));
        registerInputProcessor(new TapAndHoldProcessor(RamApp.getApplication(), Constants.TAP_AND_HOLD_DURATION));
        registerInputProcessor(new PanProcessorTwoFingers(RamApp.getApplication()));
        registerInputProcessor(new RightClickDragProcessor(RamApp.getApplication()));
        registerInputProcessor(new ZoomProcessor(RamApp.getApplication()));
        registerInputProcessor(new MouseWheelProcessor(RamApp.getApplication()));

        UnistrokeProcessorLeftClick up = new UnistrokeProcessorLeftClick(RamApp.getApplication());
        up.addTemplate(UnistrokeGesture.X, Direction.CLOCKWISE);
        up.addTemplate(UnistrokeGesture.X, Direction.COUNTERCLOCKWISE);

        registerInputProcessor(up);
    }

    /**
     * Changes the handlers for all {@link FeatureView}.
     * 
     * @param handler - The given handler
     */
    public void changeHandlers(IFeatureHandler handler) {
        changeHandlers(getRootFeature(), handler);
    }

    /**
     * Changes the handlers for a {@link FeatureView} and recursively for its children.
     * 
     * @param feature - The {@link FeatureView} to change the handlers of
     * @param handler - The given handler
     */
    private void changeHandlers(FeatureView feature, IFeatureHandler handler) {
        feature.setListenersEditMode(handler);
        feature.addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(RamApp.getApplication(),
                containerLayer));
        for (FeatureView child : feature.getChildrenFeatureViews()) {
            changeHandlers(child, handler);
        }
    }

    @Override
    protected void initReuseRoot(COREReuse reuse, FeatureView parent, COREReuse parentReuse) {
        // Only add the reuse if it's a first level reuse or if it has re-exposed features
        if (reuse.equals(parentReuse) || !reuse.getSelectedConfiguration().getReexposed().isEmpty()) {
            super.initReuseRoot(reuse, parent, parentReuse);
        }
    }

    @Override
    protected void initReusesView(COREReuse reuse, FeatureView parent, COREFeature feature, COREReuse parentReuse,
            boolean displaySelected, boolean displayReexposed) {
        boolean selected = reuse.equals(parentReuse);
        super.initReusesView(reuse, parent, feature, parentReuse, selected, displayReexposed);
    }

    /*
     * ----------------------------- BEHAVIOR ---------------------------
     */
    @Override
    public void notifyChanged(Notification notification) {
        /* Features in the model */
        if (notification.getFeature() == CorePackage.Literals.CORE_FEATURE_MODEL__FEATURES) {
            // redraw diagram if a feature has been added, removed or moved
            switch (notification.getEventType()) {
                case Notification.ADD:
                    COREFeature feature = (COREFeature) notification.getNewValue();
                    addNewFeature(feature, false, null);
                    updateFeaturesDisplay(true);
                    break;
                case Notification.REMOVE:
                    removeFeature((COREFeature) notification.getOldValue());
                    updateFeaturesDisplay(true);
                    break;
            }
        } else if (notification.getFeature() == CorePackage.Literals.CORE_FEATURE__REALIZED_BY) {
            /* Realization for the feature */
            FeatureView feature = features.get((COREFeature) notification.getNotifier());
            if (feature == null) {
                return;
            }
            switch (notification.getEventType()) {
                case Notification.ADD:
                case Notification.REMOVE:
                    if (expandFeature(feature.getParentFeatureView())) {
                        updateFeaturesDisplay(true);
                    } else {
                        feature.reAssignColors(scene.getAspectSelector().getSelected());
                    }
                    break;
            }
        } else if (notification.getFeature() == CorePackage.Literals.CORE_FEATURE__PARENT_RELATIONSHIP
                && notification.getEventType() == Notification.SET && !notification.isTouch()) {
            updateRelationShipView(notification);
            /* Constraints */
        } else if (notification.getFeature() == CorePackage.Literals.CORE_FEATURE__EXCLUDES
                || notification.getFeature() == CorePackage.Literals.CORE_FEATURE__REQUIRES) {
            // Update the container in the scene
            scene.notifyChanged(notification);

        } else if (notification.getFeature() == CorePackage.Literals.CORE_FEATURE__CHILDREN) {
            switch (notification.getEventType()) {
                case Notification.ADD:
                    // Only useful in the case we already have the feature in the diagram, but change its parent
                    COREFeature feature = (COREFeature) notification.getNewValue();
                    boolean toRedraw = false;
                    if (features.containsKey(feature)) {
                        addNewFeature(feature, false, null);
                        toRedraw = true;
                    }
                    // Expand collapsed features if necessary
                    if (expandFeature(features.get(feature.getParent()))) {
                        toRedraw = true;
                    }
                    // Update the view
                    if (toRedraw) {
                        updateFeaturesDisplay(true);
                    }
                    break;
                case Notification.REMOVE:
                    // If
                    COREFeature feat = (COREFeature) notification.getNotifier();
                    FeatureView f = features.get(feat);
                    if (feat.getChildren().isEmpty()) {
                        f.setChildrenRelationship(null);
                    }
                    // Expand collapsed features if necessary
                    if (expandFeature(f)) {
                        updateFeaturesDisplay(true);
                    }
                    break;
                case Notification.MOVE:
                    COREFeature ff = (COREFeature) notification.getNewValue();
                    moveFeature((COREFeature) notification.getNewValue(), (Integer) notification.getOldValue());
                    // Expand if necessary
                    expandFeature(features.get(ff.getParent()));

                    updateFeaturesDisplay(true);
                    break;
            }
        } else if (notification.getFeature() == CorePackage.Literals.CORE_FEATURE__REUSES) {
            COREFeature feature = (COREFeature) notification.getNotifier();
            COREReuse reuse;
            FeatureView featureView = features.get(feature);
            switch (notification.getEventType()) {
                case Notification.ADD:
                    reuse = (COREReuse) notification.getNewValue();
                    initReuseRoot(reuse, featureView);

                    // Show reuses if we are currently in the concern scene
                    if (RamApp.getActiveScene() == scene && !showReuses) {
                        scene.switchShowHideReuse();
                    }
                    expandFeature(featureView);
                    updateFeaturesDisplay(true);
                    break;
                case Notification.REMOVE:
                    reuse = (COREReuse) notification.getOldValue();

                    List<FeatureView> views = reuses.get(reuse);
                    for (FeatureView view : views) {
                        destroyFeatureView(view);
                    }
                    // Remove from maps and parent feature
                    reuses.remove(reuse);
                    featureView.getChildrenFeatureViews().removeAll(views);

                    // Show reuses if we are currently in the concern scene
                    if (RamApp.getActiveScene() == scene && !showReuses) {
                        scene.switchShowHideReuse();
                    }
                    expandFeature(featureView);                    
                    updateFeaturesDisplay(true);
                    break;
            }
        } else if (notification.getFeature() == CorePackage.Literals.CORE_NAMED_ELEMENT__NAME) {
            FeatureView notifier = null;
            // Expand feature if necessary
            if (notification.getNotifier() instanceof COREReuse) {
                COREReuse reuse = (COREReuse) notification.getNotifier();
                notifier = reuses.get(reuse).get(0);
                // Show reuses if we are currently in the concern scene
                if (RamApp.getActiveScene() == scene && !showReuses) {
                    scene.switchShowHideReuse();
                }
            } else if (notification.getNotifier() instanceof COREFeature) {
                notifier = features.get((COREFeature) notification.getNotifier());
            }
            if (notifier != null && notifier.getParentFeatureView() != null) {
                expandFeature(notifier.getParentFeatureView());
            }
            // place feature correctly again (would be better to update position/size of the view during edit?)
            RamApp.getApplication().invokeLater(new Runnable() {
                @Override
                public void run() {
                    updateFeaturesDisplay(true);
                }
            });
        }
    }

    /**
     * Update the display for the relationships if they change.
     * 
     * @param notification - The received notification
     */
    private void updateRelationShipView(Notification notification) {
        /* Realization for the feature */
        COREFeatureRelationshipType oldV = (COREFeatureRelationshipType) notification.getOldValue();
        COREFeatureRelationshipType newV = (COREFeatureRelationshipType) notification.getNewValue();

        FeatureView view = features.get(notification.getNotifier());
        if (view == null) {
            return;
        }
        FeatureView parentV = view.getParentFeatureView();

        // Expand if necessary
        boolean expanded = expandFeature(parentV);
        boolean toRedraw = false;

        if (newV == COREFeatureRelationshipType.MANDATORY || newV == COREFeatureRelationshipType.OPTIONAL) {
            view.setParentRelationship(newV);
            // Just switch colors
            if (oldV == COREFeatureRelationshipType.MANDATORY || oldV == COREFeatureRelationshipType.OPTIONAL) {
                view.updateRelationshipColor();
            } else {
                // Update the display
                for (FeatureView child : parentV.getChildrenFeatureViews()) {
                    if (!child.equals(view) && !child.isReuse()
                            && child.getParentRelationship() != parentV.getChildrenRelationship()) {
                        parentV.setChildrenRelationship(null);
                    }
                }
                toRedraw = true;
            }
        } else if (newV == COREFeatureRelationshipType.OR || newV == COREFeatureRelationshipType.XOR) {
            view.setParentRelationship(newV);
            if (oldV == COREFeatureRelationshipType.XOR || oldV == COREFeatureRelationshipType.OR) {
                // Update display color
                for (FeatureView child : parentV.getChildrenFeatureViews()) {
                    if (!child.equals(view) && !child.isReuse() && parentV.getChildrenRelationship() != newV
                            && child.getParentRelationship() != parentV.getChildrenRelationship()) {
                        parentV.setChildrenRelationship(newV);
                        parentV.updateRelationshipColor();
                    }
                }
            } else {
                for (FeatureView child : parentV.getChildrenFeatureViews()) {
                    if (!child.equals(view) && !child.isReuse() && child.getParentRelationship() == newV) {
                        // Parent is no longer MANDATORY/OPTIONAL, update the display
                        parentV.setChildrenRelationship(newV);
                    }
                }
                toRedraw = true;
            }
        }
        // Update the view
        if (toRedraw) {
            updateFeaturesDisplay(expanded);
        } else if (expanded) {
            updateFeaturesDisplay(true);
        }
    }

    /**
     * Expands the feature and its parent if necessary. Does not refresh the display.
     * 
     * @param featureView - The {@link FeatureView} we want to make visible
     * @return true if we had to expand at least one {@link FeatureView}
     */
    protected final boolean expandFeature(FeatureView featureView) {
        Set<FeatureView> collapsed = getCollapsedFeatures(featureView);
        if (!scene.isAspectSelected()) {
            // If an aspect is selected, everything is expanded by default
            collapsedFeatures.removeAll(collapsed);
        }
        return !collapsed.isEmpty();
    }

    /**
     * Change position of a feature under its parent.
     * 
     * @param feature - The moved {@link COREFeature}.
     * @param oldPosition - The previous position of the feature.
     */
    private void moveFeature(COREFeature feature, Integer oldPosition) {
        FeatureView view = features.get(feature);
        FeatureView parent = view.getParentFeatureView();
        int newPosition = parent.getFeature().getChildren().indexOf(feature);
        int move = newPosition - oldPosition;
        // We have to do this because if we have reuses they are located at the start children feature views list
        int position = parent.getChildrenFeatureViews().indexOf(view) + move;
        parent.getChildrenFeatureViews().remove(view);
        parent.getChildrenFeatureViews().add(position, view);
    }

    /**
     * Remove completely a {@link COREFeature} from the diagram.
     * Remove link from the parent {@link FeatureView} and destroys the view.
     * 
     * @param feature - The feature to remove.
     */
    private void removeFeature(COREFeature feature) {
        FeatureView view = features.remove(feature);

        if (view == null || view.getChildrenFeatureViews().size() > 0) {
            return;
        }

        // Remove from the parent
        FeatureView parent = view.getParentFeatureView();
        if (parent != null) {
            parent.getChildrenFeatureViews().remove(view);
        }
        destroyFeatureView(view);
    }

    @Override
    protected FeatureView createFeature(COREFeature feature, FeatureView parent, COREFeatureRelationshipType type,
            boolean isReuse, COREReuse reuse) {
        boolean displayReuseName = reuse != null && !parent.isReuse() && !parent.isParentReuse();
        // Register listener for the reuse
        if (displayReuseName) {
            EMFEditUtil.removeListenerFor(reuse, this);
            EMFEditUtil.addListenerFor(reuse, this);
        }
        FeatureView featureView = super.createFeature(feature, parent, type, isReuse, reuse, displayReuseName);
        if (reuse != null && reuse.getSelectedConfiguration().getReexposed().contains(feature)) {
            featureView.setSelectionStatus(SelectionStatus.RE_EXPOSED);
        } else if (isReuse) {
            featureView.setParentRelationship(COREFeatureRelationshipType.NONE);
        }
        return featureView;
    }

    @Override
    public void updateFeaturesDisplay(boolean repopulate) {
        super.updateFeaturesDisplay(repopulate);
        COREModel selected = scene.isAspectSelected() ? scene.getAspectSelector().getSelected() : null;
        reAssignColors(selected);
        scene.updateExpandButton();
        placeIcons();
    }

    @Override
    protected void destroyFeatureView(FeatureView view) {
        EMFEditUtil.removeListenerFor(view.getFeature(), this);
        if (view.getCoreReuse() != null) {
            EMFEditUtil.removeListenerFor(view.getCoreReuse(), this);
        }
        super.destroyFeatureView(view);
    }

    /*
     * -------------------------- DISPLAY ----------------------------
     */

    /**
     * Function called when an aspect is deleted from an aspect container to reassign the colors of aspects.
     * 
     * @param selected - The selected {@link COREModel} in the scene if any. Highlight the view if the model
     *            realizes the feature.
     */
    public void reAssignColors(COREModel selected) {
        reAssignColors(getRootFeature(), selected);
    }

    /**
     * Put back default colors to a feature and its children.
     * 
     * @param feature - The feature to change the color of
     * @param selected - The selected {@link COREModel} in the scene if any. Highlight the view if the model
     *            realizes the feature.
     */
    private void reAssignColors(FeatureView feature, COREModel selected) {
        feature.reAssignColors(selected);
        for (FeatureView child : feature.getChildrenFeatureViews()) {
            reAssignColors(child, selected);
        }
    }

    /*
     * ----------------------------- HELPERS ---------------------------
     */

    /**
     * Function used to check against all {@link FeatureView} if the Unistroke was a XOR curve.
     * 
     * @param startX - The starting position of X.
     * @param startY - The starting position of Y.
     * @param endX - The end position of X.
     * @param endY - The end position of Y.
     * @return FeatureView
     */
    public FeatureView isXORCurve(float startX, float startY, float endX, float endY) {
        return isXORCurve(getRootFeature(), startX, startY, endX, endY);
    }

    /**
     * Function used to check against a {@link FeatureView} and its children if the Unistroke was a XOR curve.
     * 
     * @param feature - The feature to consider
     * @param startX - The starting position of X.
     * @param startY - The starting position of Y.
     * @param endX - The end position of X.
     * @param endY - The end position of Y.
     * @return FeatureView
     */
    private FeatureView isXORCurve(FeatureView feature, float startX, float startY, float endX, float endY) {
        float x = feature.getX();
        float y = feature.getY();
        float w = feature.getWidth();
        float h = feature.getHeight();

        boolean start = false;
        boolean end = false;

        float extraWidth = (float) (0.3 * w);

        if (startX > (getX() - extraWidth) && startX < (x + extraWidth) && startY > y
                && startY < (getY() + 1.3 * getHeight())) {
            start = true;

        }
        if (endX > (getX() + (0.7 * w)) && endX < (x + 1.3 * w) && endY > y && endY < (getY() + 1.3 * h)) {
            end = true;
        }

        if (start && end) {
            return feature;
        } else {
            for (FeatureView child : feature.getChildrenFeatureViews()) {
                FeatureView xor = isXORCurve(child, startX, startY, endX, endY);
                if (xor != null) {
                    return xor;
                }
            }
            return null;
        }
    }

    @Override
    public void destroy() {
        // Remove model listener
        EMFEditUtil.removeListenerFor(featureModel, this);
        super.destroy();
    }

    @Override
    protected void registerListeners(FeatureView pass) {
        if (!pass.isReuse() && !pass.isParentReuse()) {
            EMFEditUtil.removeListenerFor(pass.getFeature(), this);
            EMFEditUtil.addListenerFor(pass.getFeature(), this);
        }

        IFeatureHandler handler;
        if (scene != null && scene.isAspectSelected()) {
            handler = HandlerFactory.INSTANCE.getFeatureAssociationModeHandler();
        } else {
            handler = HandlerFactory.INSTANCE.getFeatureEditModeHandler();
        }
        pass.setListenersEditMode(handler);
        pass.addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(RamApp.getApplication(),
                containerLayer));
    }

    @Override
    protected DisplayOptions shouldDisplay(FeatureView child) {
        if ((child.isReuse() && (!showReuses || (!showReEexposed && !child.equals(child.getHighestParentReuse()))))
                || (!showReEexposed && (child.getSelectionStatus() == SelectionStatus.RE_EXPOSED))) {
            return DisplayOptions.HIDE;
        }
        DisplayOptions option = super.shouldDisplay(child);
        // In association mode we don't collapse
        if (option == DisplayOptions.COLLAPSE
                && !child.isReuse() && !child.isParentReuse() && scene.isAspectSelected()) {
            return DisplayOptions.SHOW;
        }
        return option;
    }

    /**
     * Switch the fact that we display reuses or not.
     * If they were previously hidden, we expand the collapsed features that prevent us from seeing the reuses.
     */
    public void switchShowReuses() {
        boolean update = false;
        this.showReuses = !showReuses;
        // Expand collapsed features that prevent to see reuses
        for (List<FeatureView> fvs : reuses.values()) {
            for (FeatureView view : fvs) {
                // We want to show first level reuses only
                if (view.isReuse() && !view.isParentReuse()) {
                    update = true;
                    if (showReuses) {
                        Set<FeatureView> collapsed = getCollapsedFeatures(view);
                        collapsed.remove(view);
                        collapsedFeatures.removeAll(collapsed);
                    }
                    continue;
                }
            }
        }
        if (update) {
            updateFeaturesDisplay(true);
        }
    }

    /**
     * Switch the fact that we display re-exposed features from reuses or not.
     */
    public void switchShowReexposed() {
        boolean update = false;
        this.showReEexposed = !showReEexposed;
        // See if there are any re-exposed features
        for (FeatureView view : collectFeatureViews(true)) {
            if (view.getSelectionStatus() == SelectionStatus.RE_EXPOSED) {
                update = true;
                break;
            }
        }
        if (update) {
            updateFeaturesDisplay(true);
        }
    }

    /*
     * ------------------- COLLAPSE -------------------
     */

    /**
     * Expand all currently visible features.
     */
    public void expandAllFeatures() {
        Set<FeatureView> collapsed = getAllCollapsed(true);
        for (FeatureView colFeature : collapsed) {
            collapsedFeatures.remove(colFeature);
            colFeature.setLineStipple(LineStyle.SOLID.getStylePattern());
        }
        if (collapsed.size() > 0) {
            updateFeaturesDisplay(true);
        }
    }

    /**
     * Switch the collapsed status for a {@link FeatureView}.
     * 
     * @param feature - The {@link FeatureView} to collapse
     */
    public void switchCollapse(FeatureView feature) {
        if (collapsedFeatures.contains(feature) || !canCollapse(feature)) {
            collapsedFeatures.remove(feature);
        } else {
            collapsedFeatures.add(feature);
        }
        updateFeaturesDisplay(true);
    }

    @Override
    protected boolean canCollapse(FeatureView featureView) {
        if ((featureView.isReuse() || featureView.isParentReuse()) && !showReuses) {
            return true;
        }
        if (showReuses) {
            // If we show reuses we always display everything so we can just check if we have any children FVs.
            return !featureView.getChildrenFeatureViews().isEmpty();
        }
        // If we don't show reuses only display children COREFeatures.
        COREFeature feature = featureView.getFeature();
        return features.containsKey(feature) && feature.getParent() != null && !feature.getChildren().isEmpty();
    }

    @Override
    protected Set<FeatureView> getAllCollapsed(boolean onlyVisible) {
        Set<FeatureView> collapsed = new HashSet<FeatureView>();
        // Add normal features
        if (!onlyVisible || !scene.isAspectSelected()) {
            for (FeatureView feature : features.values()) {
                collapsed.addAll(getCollapsedFeatures(feature, onlyVisible));
            }
        }
        // Add features from reuses
        if (showReuses) {
            for (List<FeatureView> reusesViews : reuses.values()) {
                for (FeatureView feature : reusesViews) {
                    collapsed.addAll(getCollapsedFeatures(feature, onlyVisible));
                }
            }
        }

        return collapsed;
    }

}