package ca.mcgill.sel.ram.ui.views.feature;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.componentProcessors.panProcessor.PanProcessorTwoFingers;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.zoomProcessor.ZoomProcessor;
import org.mt4j.util.MTColor;

import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.core.COREFeatureModel;
import ca.mcgill.sel.core.COREFeatureRelationshipType;
import ca.mcgill.sel.core.COREReuse;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.events.MouseWheelProcessor;
import ca.mcgill.sel.ram.ui.events.RightClickDragProcessor;
import ca.mcgill.sel.ram.ui.scenes.DisplayConcernSelectScene.DisplayMode;
import ca.mcgill.sel.ram.ui.utils.Colors;
import ca.mcgill.sel.ram.ui.utils.SelectionsSingleton;
import ca.mcgill.sel.ram.ui.views.handler.IAbstractViewHandler;

/**
 * View used to represent a feature model in select mode, when we choose feature for reusing.
 * 
 * @author Nishanth
 */
public class FeatureDiagramSelectView extends FeatureDiagramView<IAbstractViewHandler> {

    /**
     * Enum containing all the possible selection status for the features in Select mode.
     * They are associated with the color that should be given to the view.
     * 
     * @author Nishanth
     * @author CCamillieri
     */
    public enum SelectionStatus {
        /** The Feature has been selected manually. */
        SELECTED(Colors.FEATURE_SELECTED_FILL_COLOR),
        /** The Feature has been auto-selected. */
        AUTO_SELECTED(Colors.FEATURE_AUTOSELECTED_FILL_COLOR),
        /** There is an issue with the Feature selection. */
        WARNING_SELECTED(Colors.FEATURE_NO_FILL, Colors.FEATURE_SELECTION_CLASH_COLOR,
                Colors.FEATURE_SELECTION_CLASH_COLOR),
        /** There is an issue with the Feature selection because the feature was unselected. */
        WARNING_NOT_SELECTED(Colors.FEATURE_NO_FILL, Colors.FEATURE_SELECTION_CLASH_COLOR,
                Colors.FEATURE_SELECTION_CLASH_COLOR),
        /** The Feature is not selected, nor re-exposed. */
        NOT_SELECTED(Colors.FEATURE_NOT_SELECTED_FILL_COLOR),
        /** The Feature has been re-exposed. */
        RE_EXPOSED(Colors.FEATURE_REEXPOSED_FILL_COLOR);

        private MTColor fillColor;
        private MTColor strokeColor;
        private MTColor parentLinkColor;

        /**
         * Constructor for {@link SelectionStatus}.
         * 
         * @param fillColor - the fill color of the feature when in the given status.
         */
        private SelectionStatus(MTColor fillColor) {
            this(fillColor, Colors.DEFAULT_ELEMENT_STROKE_COLOR, Colors.DEFAULT_ELEMENT_STROKE_COLOR);
        }

        /**
         * Constructor for {@link SelectionStatus}.
         * 
         * @param fillColor - the fill color of the feature when in the given status.
         * @param strokeColor - the stroke color of the feature when in the given status.
         * @param parentLinkColor - the color of the link to parent feature when in the given status.
         */
        private SelectionStatus(MTColor fillColor, MTColor strokeColor, MTColor parentLinkColor) {
            this.fillColor = fillColor;
            this.strokeColor = strokeColor;
            this.parentLinkColor = parentLinkColor;
        }

        /**
         * Getter for the fill color associated with the status.
         * 
         * @return - The fill color associated with the current {@link SelectionStatus}
         */
        public MTColor getFillColor() {
            return fillColor;
        }

        /**
         * Getter for the stroke color associated with the status.
         * 
         * @return - The stroke color associated with the current {@link SelectionStatus}
         */
        public MTColor getStrokeColor() {
            return strokeColor;
        }

        /**
         * Getter for the color of the link to the parent feature associated with the status.
         * 
         * @return - The color of the link to the parentassociated with the current {@link SelectionStatus}
         */
        public MTColor getParentLinkColor() {
            return parentLinkColor;
        }

        /**
         * Call to know if the color has to/can be changed or not.
         * 
         * @return true if there is a fill color defined
         */
        public boolean hasFillColor() {
            return fillColor != null && fillColor != Colors.FEATURE_NO_FILL;
        }
    }

    private DisplayMode currentMode;

    /**
     * Create a feature diagram representation view.
     * 
     * @param width - The width of the view
     * @param height - The height of the view
     * @param fm - The represented FeatureModel
     */
    public FeatureDiagramSelectView(float width, float height, COREFeatureModel fm) {
        super(width, height, fm);
    }

    @Override
    protected void registerListeners(FeatureView feature) {
        feature.setListenersSelectMode();
        if (!feature.getIsRoot()) {
            feature.addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(RamApp.getApplication(),
                    containerLayer));
        }
    }

    @Override
    protected void registerInputProcessors() {
        registerInputProcessor(new PanProcessorTwoFingers(RamApp.getApplication()));
        registerInputProcessor(new RightClickDragProcessor(RamApp.getApplication()));
        registerInputProcessor(new ZoomProcessor(RamApp.getApplication()));
        registerInputProcessor(new MouseWheelProcessor(RamApp.getApplication()));
    }

    /**
     * Set the current display mode for the diagram.
     * 
     * @param currentMode - The new mode
     */
    public void setCurrentMode(DisplayMode currentMode) {
        this.currentMode = currentMode;
    }

    /*
     * -------------------------- DISPLAY ---------------------------
     */
    @Override
    protected DisplayOptions shouldDisplay(FeatureView child) {
        COREFeature feature = child.getFeature();
        COREFeature parentFeature = child.getParentFeatureView().getFeature();
        // In next mode, don't display if parent is not selected. If the parent is not selected, but mandatory,
        // it means it has been auto-selected or this method wouldn't have been called.
        if (currentMode == DisplayMode.NEXT) {
            if (!isFeatureChildSelected(feature) && !isFeatureChildSelected(parentFeature)
                    && child.getParentFeatureView().getParentRelationship() != COREFeatureRelationshipType.MANDATORY) {
                return DisplayOptions.HIDE;
            }
        }
        return super.shouldDisplay(child);
    }

    /**
     * Check if there are any clashes with the selection of features.
     * 
     * @return true if there are no clashes, false otherwise
     */
    public boolean checkForClashes() {
        for (FeatureView feature : collectFeatureViews(true)) {
            if (feature.getLineToParent() != null
                    && feature.getLineToParent().getStrokeColor().equals(Colors.FEATURE_SELECTION_CLASH_COLOR)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Update the features selection and layout automatically.
     */
    public void autoSelect() {
        autoSelectFeature(getRootFeature());
    }

    /**
     * Update a feature status and its children statuses recursively.
     * 
     * @param feature - the {@link FeatureView} to consider
     */
    private void autoSelectFeature(FeatureView feature) {
        // Get list of the children of the feature that have been selected
        Set<COREFeature> selectedChildren = feature.containsChildren(feature, SelectionsSingleton.getInstance()
                .getSelectedList());

        // A child has been selected => Automatically select the feature
        if (selectedChildren.size() > 0) {
            feature.setSelectionStatus(SelectionStatus.AUTO_SELECTED);
            for (FeatureView child : feature.getChildrenFeatureViews()) {
                if (child.getFeature().getParentRelationship() == COREFeatureRelationshipType.MANDATORY) {
                    child.setSelectionStatus(SelectionStatus.AUTO_SELECTED);
                }
                autoSelectFeature(child);
            }
        }

        // The feature is selected => Select the feature
        if (SelectionsSingleton.getInstance().containsSelectedFeature(feature.getFeature())) {
            feature.setSelectionStatus(SelectionStatus.SELECTED);
            for (FeatureView child : feature.getChildrenFeatureViews()) {
                if (child.getParentRelationship() == COREFeatureRelationshipType.MANDATORY) {
                    child.setSelectionStatus(SelectionStatus.AUTO_SELECTED);
                }
                autoSelectFeature(child);
            }
        }

        // The feature is Auto-Selected => Select children if they are mandatory
        if (feature.getSelectionStatus() == SelectionStatus.AUTO_SELECTED) {
            for (FeatureView child : feature.getChildrenFeatureViews()) {
                if (child.getParentRelationship() == COREFeatureRelationshipType.MANDATORY) {
                    child.setSelectionStatus(SelectionStatus.AUTO_SELECTED);
                }
                autoSelectFeature(child);
            }
        }

        // Call recursively for children to be able to check for clashes
        for (FeatureView child : feature.getChildrenFeatureViews()) {
            autoSelectFeature(child);
        }

        // For 'XOR' or 'OR' relationship, check potential clashes
        if (feature.getChildrenRelationship() == COREFeatureRelationshipType.XOR
                || feature.getChildrenRelationship() == COREFeatureRelationshipType.OR) {
            // get list of direct children that are selected or auto-selected (or in warning status)
            Set<COREFeature> directSelectedChildren = FeatureView.containsChildrenOneLevel(feature);
            boolean selected = feature.getSelectionStatus() == SelectionStatus.SELECTED
                    || feature.getSelectionStatus() == SelectionStatus.AUTO_SELECTED
                    || feature.getSelectionStatus() == SelectionStatus.WARNING_SELECTED;
            boolean reExposed = feature.getSelectionStatus() == SelectionStatus.RE_EXPOSED;

            // There are more than one selected child and it's a 'XOR' => put selected ones in 'Warning' status
            if (selected && directSelectedChildren.size() > 1
                    && feature.getChildrenRelationship() == COREFeatureRelationshipType.XOR) {
                for (FeatureView child : feature.getChildrenFeatureViews()) {
                    if (child.getSelectionStatus() == SelectionStatus.AUTO_SELECTED
                            || child.getSelectionStatus() == SelectionStatus.SELECTED) {
                        child.setSelectionStatus(SelectionStatus.WARNING_SELECTED);
                    } else if (child.getSelectionStatus() != SelectionStatus.WARNING_SELECTED) {
                        clearReexpose(child);
                    }
                }
                // There is only one selected child and it's an XOR => clear re-expose from children
            } else if (directSelectedChildren.size() == 1
                    && feature.getChildrenRelationship() == COREFeatureRelationshipType.XOR) {
                for (FeatureView child : feature.getChildrenFeatureViews()) {
                    if (child.getSelectionStatus() != SelectionStatus.SELECTED
                            && child.getSelectionStatus() != SelectionStatus.AUTO_SELECTED
                            && child.getSelectionStatus() != SelectionStatus.WARNING_SELECTED) {
                        clearReexpose(child);
                    }
                }
                // There are no selected child
            } else if (selected || reExposed) {
                List<FeatureView> reexposed = new LinkedList<FeatureView>();
                // Get all re-exposed children
                for (FeatureView child : feature.getChildrenFeatureViews()) {
                    if (child.getSelectionStatus() != SelectionStatus.NOT_SELECTED
                            && child.getSelectionStatus() != SelectionStatus.WARNING_NOT_SELECTED) {
                        reexposed.add(child);
                    }
                }
                // Only one is re-exposed : it can be automatically selected
                // TODO disabled temporarily because doesn't work with current implementation of next mode. 
                /*if (selected && reexposed.size() == 1) {
                    reexposed.get(0).setSelectionStatus(SelectionStatus.AUTO_SELECTED);
                    autoSelectFeature(reexposed.get(0));
                }*/
                if (!reexposed.isEmpty()) {
                    return;
                }
                // Nothing is re-exposed : warning.
                for (FeatureView child : feature.getChildrenFeatureViews()) {
                    child.setSelectionStatus(SelectionStatus.WARNING_NOT_SELECTED);
                }
            }
        }
    }

    /**
     * Function used to clear the feature back to their original state (ie re-exposed).
     */
    public void clearFeatures() {
        clearFeature(getRootFeature());
    }

    /**
     * Function used to clear a feature and its children back to their original state (ie re-exposed).
     * 
     * @param feature - the feature to clear
     */
    private void clearFeature(FeatureView feature) {
        feature.setSelectionStatus(SelectionStatus.RE_EXPOSED);
        removeFeatureIcon(feature);

        for (FeatureView child : feature.getChildrenFeatureViews()) {
            if (SelectionsSingleton.getInstance().containsReexposedFeature(child.getFeature())) {
                clearReexpose(child);
            } else {
                clearFeature(child);
            }
        }
    }

    /**
     * Function used to clear a feature and its children back to unselected state (not re-exposed).
     * 
     * @param featureView - the feature to clear
     */
    private void clearReexpose(FeatureView featureView) {
        featureView.setSelectionStatus(SelectionStatus.NOT_SELECTED);
        removeFeatureIcon(featureView);

        for (FeatureView child : featureView.getChildrenFeatureViews()) {
            clearReexpose(child);
        }
    }

    @Override
    protected void initReuseRoot(COREReuse reuse, FeatureView parent, COREReuse parentReuse) {
        // Do not display reuse if there is no realizing aspect.
        if (parent.getFeature().getRealizedBy().isEmpty()) {
            return;
        }
        // Only create view if there are re-exposed features
        if (!reuse.getSelectedConfiguration().getReexposed().isEmpty()) {
            super.initReuseRoot(reuse, parent, parentReuse);
        }
    }

    @Override
    protected void initReusesView(COREReuse reuse, FeatureView parent, COREFeature feature, COREReuse parentReuse) {
        super.initReusesView(reuse, parent, feature, parentReuse, false, true);
    }

    /*
     * ------------------------------- HELPERS ---------------------------
     */

    /**
     * Check if a COREFeature or one of its child is selected. Useful in 'Next Mode' because we want to display nodes
     * that have been auto-selected because we selected one of its children.
     * 
     * @param feature - The feature to test
     * @return - true if the feature or one of its children is selected
     */
    private boolean isFeatureChildSelected(COREFeature feature) {
        if (feature == null) {
            return false;
        }
        // If it is selected, it's ok
        if (SelectionsSingleton.getInstance().containsSelectedFeature(feature)) {
            return true;
        }
        // If any of its child is selected, continue
        for (COREFeature child : feature.getChildren()) {
            if (isFeatureChildSelected(child)) {
                return true;
            }
        }
        // If any of a reuse children feature is selected, continue
        for (COREReuse reuse : feature.getReuses()) {
            Set<COREFeature> reuseFeatures = new HashSet<COREFeature>(reuse.getSelectedConfiguration().getSelected());
            reuseFeatures.addAll(reuse.getSelectedConfiguration().getReexposed());
            for (COREFeature child : reuseFeatures) {
                if (isFeatureChildSelected(child)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns a set of selected features.
     * 
     * @return the set of re-exposed features
     */
    public Set<COREFeature> collectReexposed() {
        Set<COREFeature> result = new HashSet<COREFeature>();
        for (FeatureView feature : collectFeatureViews(false)) {
            if (feature.getSelectionStatus() == SelectionStatus.RE_EXPOSED) {
                result.add(feature.getFeature());
            }
        }
        return result;
    }

    /**
     * Returns the set of re-exposed features.
     * 
     * @return - The set of all selected features.
     */
    public Set<COREFeature> collectSelected() {
        Set<COREFeature> result = new HashSet<COREFeature>();
        for (FeatureView feature : collectFeatureViews(false)) {
            SelectionStatus selectionStatus = feature.getSelectionStatus();
            if (selectionStatus == SelectionStatus.AUTO_SELECTED || selectionStatus == SelectionStatus.SELECTED
                    || selectionStatus == SelectionStatus.WARNING_SELECTED) {
                result.add(feature.getFeature());
            }
        }
        return result;
    }

}
