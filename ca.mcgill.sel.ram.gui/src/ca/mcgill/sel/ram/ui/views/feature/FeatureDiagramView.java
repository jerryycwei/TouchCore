package ca.mcgill.sel.ram.ui.views.feature;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.abego.treelayout.NodeExtentProvider;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;
import org.eclipse.emf.common.util.EList;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import ca.mcgill.sel.core.COREReuseConfiguration;
import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.core.COREFeatureModel;
import ca.mcgill.sel.core.COREFeatureRelationshipType;
import ca.mcgill.sel.core.COREModel;
import ca.mcgill.sel.core.COREReuse;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamImageComponent;
import ca.mcgill.sel.ram.ui.components.RamLineComponent;
import ca.mcgill.sel.ram.ui.utils.Colors;
import ca.mcgill.sel.ram.ui.utils.Constants;
import ca.mcgill.sel.ram.ui.utils.Icons;
import ca.mcgill.sel.ram.ui.utils.MathUtils;
import ca.mcgill.sel.ram.ui.utils.autolayout.DefaultNodeExtentProvider;
import ca.mcgill.sel.ram.ui.views.AbstractView;
import ca.mcgill.sel.ram.ui.views.RelationshipView.LineStyle;
import ca.mcgill.sel.ram.ui.views.feature.FeatureDiagramSelectView.SelectionStatus;
import ca.mcgill.sel.ram.ui.views.handler.IAbstractViewHandler;

/**
 * Class used to represent a feature model.
 * Has a root feature and builds the model from it
 * 
 * @author CCamillieri
 * @param <T> The type of handler for this view
 */
public abstract class FeatureDiagramView<T extends IAbstractViewHandler> extends AbstractView<T> {

    /**
     * Display options for a given {@link FeatureView}.
     */
    protected enum DisplayOptions {
        /** The view will be displayed. */
        SHOW,
        /** The view is to be collapsed : it will be displayed but its children hidden. */
        COLLAPSE,
        /** The view will not be displayed. */
        HIDE
    }

    // The initial level / depth to be shown based on screen resolution
    private static final float INITIAL_NUMBER_OF_LEVELS = 4;

    // The maximum distance between two features while being displayed.
    private static final float MAXIMUM_VERTICAL_DISTANCE = 100;

    // The minimum distance between two features while being displayed.
    private static final float INITIAL_HORIZONTAL_DISTANCE = 50;

    /** Tree layout representing the diagram. */
    private static TreeLayout<FeatureView> treeLayout;

    /** Tree containing the features hierarchy. */
    protected DefaultTreeForTreeLayout<FeatureView> tree;

    /** Map containing the {@link FeatureView}s of the feature model. */
    protected Map<COREFeature, FeatureView> features;

    /** Map containing the reuses of the feature model and their associated featureViews. */
    protected Map<COREReuse, List<FeatureView>> reuses;

    /** Feature model displayed by this diagram. */
    protected COREFeatureModel featureModel;

    /** {@link FeatureView} that have been collapsed by the user. */
    protected Set<FeatureView> collapsedFeatures;

    private float gapBetweenLevels;

    /**
     * Create a feature diagram representation view.
     * 
     * @param width - The width of the view
     * @param height - The height of the view
     * @param fm - The {@link COREFeatureModel} the diagram represents
     */
    public FeatureDiagramView(float width, float height, COREFeatureModel fm) {
        super(width, height);
        this.featureModel = fm;
        this.features = new HashMap<COREFeature, FeatureView>();
        this.reuses = new HashMap<COREReuse, List<FeatureView>>();

        this.collapsedFeatures = new HashSet<FeatureView>();

        // Compute the gap between the levels based on the height of the screen, divided by the desired value of user.
        this.gapBetweenLevels = RamApp.getActiveScene().getHeight() / INITIAL_NUMBER_OF_LEVELS;
        if (gapBetweenLevels >= MAXIMUM_VERTICAL_DISTANCE) {
            gapBetweenLevels = MAXIMUM_VERTICAL_DISTANCE;
        }

        // Create the views for all the features
        COREFeature root = featureModel.getRoot();
        initFeatureViews(root, null);

        // Build the tree
        populateTree();

        // Compute positions for the features
        computeFeaturesPosition();

        // Add the views to the container. Also creates lines and circles/triangles for relationships and initializes
        // listeners
        displayDiagram();
    }

    /**
     * Get the list of collapsed {@link FeatureView}s.
     * 
     * @return the list of collapsed {@link FeatureView}s.
     */
    public Set<FeatureView> getCollapsedFeatures() {
        return collapsedFeatures;
    }

    @Override
    public void destroy() {
        // Destroy all FeatureViews
        for (FeatureView feature : collectFeatureViews(true)) {
            destroyFeatureView(feature);
        }
        // Clear lists
        features.clear();
        reuses.clear();
        super.destroy();
    }

    /**
     * Destroy a featureView.
     * 
     * @param view - The {@link FeatureView} to destroy
     */
    protected void destroyFeatureView(FeatureView view) {
        if (containerLayer.containsChild(view)) {
            if (view.getLineToParent() != null) {
                containerLayer.removeChild(view.getLineToParent());
            }
            if (view.getRelationshipCircle() != null) {
                containerLayer.removeChild(view.getRelationshipCircle());
            }
            if (view.getRelationshipTriangle() != null) {
                containerLayer.removeChild(view.getRelationshipTriangle());
            }
            if (view.getReuseArrow() != null) {
                containerLayer.removeChild(view.getReuseArrow());
            }
            removeFeatureIcon(view);
            containerLayer.removeChild(view);
        }
        collapsedFeatures.remove(view);
        view.destroy();
    }

    /**
     * Removes the image associated with a {@link FeatureView}, if there is one.
     * 
     * @param feature - The {@link FeatureView} of which we want to remove the icon
     */
    public void removeFeatureIcon(FeatureView feature) {
        RamImageComponent image = feature.getImage();
        if (image != null) {
            if (containerLayer.containsChild(image)) {
                containerLayer.removeChild(image);
            }
            feature.setImage(null);
            image.destroy();
        }
    }

    /*
     * ----------------- INITIALIZATION ---------------
     */

    /**
     * Create the {@link FeatureView} for a given {@link COREFeature}.
     * 
     * @param feature - The {@link COREFeature}
     * @param parent - The parent {@link FeatureView}, if any
     */
    private void initFeatureViews(COREFeature feature, FeatureView parent) {
        FeatureView featureView = features.get(feature);
        // Create the element only if the view doesn't already exist
        if (featureView == null) {
            featureView = addNewFeature(feature, false, null);
        }
        // Create the reuse elements
        for (COREReuse reuse : feature.getReuses()) {
            initReuseRoot(reuse, featureView, reuse);
        }

        // Create the children elements
        for (COREFeature child : feature.getChildren()) {
            initFeatureViews(child, featureView);
        }
    }

    /**
     * Create the {@link FeatureView}s for a given {@link COREReuse}.
     * 
     * @param reuse - The {@link COREReuse}
     * @param parent - The {@link FeatureView} of the feature that made the reuse
     */
    protected final void initReuseRoot(COREReuse reuse, FeatureView parent) {
        initReuseRoot(reuse, parent, reuse);
    }

    /**
     * Create the {@link FeatureView}s for a given {@link COREReuse}.
     * 
     * @param reuse - The {@link COREReuse}
     * @param parent - The {@link FeatureView} of the feature that made the reuse
     * @param parentReuse - The parent reuse of the current reuse. Useful for reuses of concern with reuses.
     */
    protected void initReuseRoot(COREReuse reuse, FeatureView parent, COREReuse parentReuse) {
        if (reuse.getReusedConcern() == null || reuse.getReusedConcern().getFeatureModel() == null) {
            return;
        }
        // Add the root reuse as mandatory
        COREFeature root = reuse.getReusedConcern().getFeatureModel().getRoot();

        boolean isReuse = parentReuse == null || parentReuse.equals(reuse);
        COREFeatureRelationshipType rel;
        rel = isReuse ? COREFeatureRelationshipType.NONE : COREFeatureRelationshipType.MANDATORY;
        FeatureView reuseView = createFeature(root, parent, rel, true, reuse);

        // Put in list
        List<FeatureView> views = reuses.get(parentReuse);
        if (views == null) {
            views = new LinkedList<FeatureView>();
        }
        views.add(reuseView);
        reuses.put(parentReuse, views);

        // Add as child of parent FeatureView
        int index = 0;
        for (int i = 0; i < parent.getChildrenFeatureViews().size(); i++) {
            COREReuse r = parent.getFeature().getReuses().get(i);
            FeatureView sibling = parent.getChildrenFeatureViews().get(i);
            if (sibling.isReuse() && !reuse.equals(r)) {
                index++;
            } else {
                break;
            }
        }
        parent.getChildrenFeatureViews().add(index, reuseView);

        // Create children FeatureViews
        for (COREFeature child : root.getChildren()) {
            initReusesView(reuse, reuseView, child, parentReuse);
        }

        // Create the reuse for the reuse root
        for (COREReuse extReuse : root.getReuses()) {
            initReuseRoot(extReuse, reuseView, reuse);
        }
    }

    /**
     * Create the {@link FeatureView}s for a given {@link COREFeature} part of a {@link COREReuse} and for its children.
     * 
     * @param feature - The {@link COREFeature}
     * @param reuse - The {@link COREReuse}
     * @param parent - The parent {@link FeatureView} for this feature
     * @param parentReuse - The parent reuse of the current reuse. Useful for reuses of concern with reuses.
     */
    protected void initReusesView(COREReuse reuse, FeatureView parent, COREFeature feature, COREReuse parentReuse) {
        initReusesView(reuse, parent, feature, parentReuse, true, true);
    }

    /**
     * Create the {@link FeatureView}s for a given {@link COREFeature} part of a {@link COREReuse} and for its children.
     * Only create the view if the {@link COREFeature} has been selected by the reuse
     * 
     * @param feature - The {@link COREFeature}
     * @param reuse - The {@link COREReuse}
     * @param parentReuse - The parent reuse of the current reuse. Useful for reuses of concern with reuses.
     * @param parent - The parent {@link FeatureView} for this feature
     * @param displaySelected - Whether we should create a {@link FeatureView} for selected features as well.
     *            For example in Edit mode we display selected feature only for 'first level' reuses
     * @param displayReexposed - Whether we should create a {@link FeatureView} for re-exposed features as well.
     */
    protected void initReusesView(COREReuse reuse, FeatureView parent, COREFeature feature, COREReuse parentReuse,
            boolean displaySelected, boolean displayReexposed) {
        COREReuseConfiguration selection = reuse.getSelectedConfiguration();
        FeatureView featureView = parent;
        boolean toDisplay = false;
        if (displaySelected && selection.getSelected().contains(feature)) {
            toDisplay = true;
        } else if (displayReexposed && selection.getReexposed().contains(feature)) {
            toDisplay = true;
        } else if (displayReexposed && hasReexposedDescendant(reuse, feature)) {
            toDisplay = true;
        } else if (!displaySelected || !isSelected(feature, selection)) {
            return;
        }
        
        if (toDisplay) {
            featureView = addNewFeature(feature, false, reuse, parentReuse, parent.getFeature());
        }

        // Create reuses recursively
        for (COREReuse extReuse : feature.getReuses()) {
            initReuseRoot(extReuse, featureView, reuse);
        }
        for (COREFeature child : feature.getChildren()) {
            initReusesView(reuse, featureView, child, parentReuse, displaySelected, displayReexposed);
        }
    }

    /**
     * Check if a feature is selected by the given configuration.
     * TODO this is temporary and will only be useful for association because the Configuration only contains features
     * that were manually selected.
     * 
     * @param feature - The feature to check
     * @param selection - The {@link COREReuseConfiguration} to look in.
     * @return true if it's selected, false otherwise
     */
    private boolean isSelected(COREFeature feature, COREReuseConfiguration selection) {
        if (selection.getSelected().contains(feature)) {
            return true;
        }
        // Check if a child is selected
        for (COREFeature child : feature.getChildren()) {
            if (isSelected(child, selection)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a given feature has re-exposed descendant in the current reuse.
     * 
     * @param reuse - The {@link COREReuse}
     * @param feature - The feature to check.
     * @return true if any of the features descendant is re-exposed. 
     */
    protected final boolean hasReexposedDescendant(COREReuse reuse, COREFeature feature) {
        List<COREFeature> reExposed = reuse.getSelectedConfiguration().getReexposed();
        for (COREFeature child : feature.getChildren()) {
            if (reExposed.contains(child) || hasReexposedDescendant(reuse, child)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Create a new {@link FeatureView} and add link it to the others.
     * 
     * @param feature - The {@link COREFeature} represented by the view
     * @param isReuse - Whether the feature is root of a reuse
     * @param reuse - The reuse, if the feature is part of a reuse
     * @return The new {@link FeatureView}
     */
    protected FeatureView addNewFeature(COREFeature feature, boolean isReuse, COREReuse reuse) {
        return addNewFeature(feature, isReuse, reuse, reuse, feature.getParent());
    }

    /**
     * Create a new {@link FeatureView} and add link it to the others.
     * 
     * @param feature - The {@link COREFeature} represented by the view
     * @param isReuse - Whether the feature is root of a reuse
     * @param reuse - The reuse, if the feature is part of a reuse
     * @param parentReuse - The parent reuse of the current reuse. Useful for reuses of concern with reuses.
     * @param parentFeature - The parent feature we want for the feature. Useful in select mode because we don't display
     *            intermediate selected features
     * @return The new {@link FeatureView}
     */
    protected final FeatureView addNewFeature(COREFeature feature, boolean isReuse, COREReuse reuse,
            COREReuse parentReuse, COREFeature parentFeature) {
        FeatureView parent;
        FeatureView child;
        if (reuse == null || parentReuse == null) {
            parent = features.get(parentFeature);
            child = features.get(feature);
        } else {
            parent = findReuseFeature(parentReuse, reuse, parentFeature);
            child = findReuseFeature(parentReuse, reuse, feature);
        }

        COREFeatureRelationshipType rel = feature.getParentRelationship();

        // If the view already exist, move it to its new parent
        if (child != null) {
            // Move to new parent
            FeatureView oldParent = child.getParentFeatureView();
            if (oldParent != null && !oldParent.equals(parent)) {
                oldParent.getChildrenFeatureViews().remove(child);
            }
            child.setParentFeatureView(parent);
        } else {
            child = createFeature(feature, parent, rel, isReuse, reuse);
        }
        // Set the correct relationship for the parent FeatureView if needed
        if (parent != null && !parent.getChildrenFeatureViews().contains(child)) {
            int parentIndex = parentFeature.getChildren().indexOf(feature);
            int nbReuses = 0;
            for (FeatureView fv : parent.getChildrenFeatureViews()) {
                if (fv.isReuse() && fv.getCoreReuse() != null && fv.getCoreReuse().getReusedConcern() != null) {
                    nbReuses++;
                } else {
                    break;
                }
            }
            int index = nbReuses;
            for (COREFeature feat : parent.getFeature().getChildren()) {
                for (int i = 0; i < parentIndex && i + nbReuses < parent.getChildrenFeatureViews().size(); i++) {
                    FeatureView fv = parent.getChildrenFeatureViews().get(i + nbReuses);
                    if (fv.getFeature().equals(feat)) {
                        index++;
                        break;
                    }
                }
            }
            parent.getChildrenFeatureViews().add(index, child);
            if (child.getParentRelationship() == COREFeatureRelationshipType.OR
                    || child.getParentRelationship() == COREFeatureRelationshipType.XOR) {
                parent.setChildrenRelationship(rel);
            }
        }
        // Add to the list of features
        if (reuse == null) {
            features.put(feature, child);
        } else {
            reuses.get(parentReuse).add(child);
        }
        return child;
    }

    /**
     * Look for the {@link FeatureView} corresponding to a given reuse.
     * 
     * @param parentReuse - The highest level containing the feature
     * @param reuse - The {@link COREReuse} for the feature
     * @param feature - The {@link COREFeature} to look for
     * @return The {@link FeatureView} if it exists, null otherwise.
     */
    private FeatureView findReuseFeature(COREReuse parentReuse, COREReuse reuse, COREFeature feature) {
        List<FeatureView> list = reuses.get(parentReuse);
        if (list == null) {
            return null;
        }
        for (FeatureView fv : list) {
            if (fv.getFeature().equals(feature)) {
                FeatureView reuseView = fv;
                // Search closest reuse view
                while (!reuseView.isReuse() && reuseView.getParentFeatureView() != null) {
                    reuseView = reuseView.getParentFeatureView();
                }
                if (reuseView.getCoreReuse() != null && reuseView.getCoreReuse().equals(reuse)) {
                    return fv;
                }
            }
        }
        return null;
    }
    
    /**
     * Create a new {@link FeatureView} with given parameters.
     * 
     * @param feature - The {@link COREFeature} represented by the view
     * @param parent - The parent {@link FeatureView} if any
     * @param type - The {@link COREFeatureRelationshipType} for the feature
     * @param isReuse - Whether the feature is root of a reuse
     * @param reuse - The {@link COREReuse} if the feature is part of a reuse
     * @return The new {@link FeatureView}
     */
    protected FeatureView createFeature(COREFeature feature, FeatureView parent, COREFeatureRelationshipType type,
            boolean isReuse, COREReuse reuse) {
        return createFeature(feature, parent, type, isReuse, reuse, false);
    }

    /**
     * Create a new {@link FeatureView} with given parameters.
     * 
     * @param feature - The {@link COREFeature} represented by the view
     * @param parent - The parent {@link FeatureView} if any
     * @param type - The {@link COREFeatureRelationshipType} for the feature
     * @param isReuse - Whether the feature is root of a reuse
     * @param reuse - The {@link COREReuse} if the feature is part of a reuse
     * @param displayReuse - Whether we want to display the reuse name (true) or the feature name
     * @return The new {@link FeatureView}
     */
    protected FeatureView createFeature(COREFeature feature, FeatureView parent, COREFeatureRelationshipType type,
            boolean isReuse, COREReuse reuse, boolean displayReuse) {
        if (reuse == null) {
            return new FeatureView(feature, parent, type, isReuse, reuse, displayReuse);
        }
        COREFeatureRelationshipType rel = type;
        EList<COREFeature> selected = reuse.getSelectedConfiguration().getSelected();
        EList<COREFeature> reexposed = reuse.getSelectedConfiguration().getReexposed();
        boolean orRel = type == COREFeatureRelationshipType.OR || type == COREFeatureRelationshipType.XOR;
        int nbSelected = 0;
        int nbDisplayed = 0;
        if (feature.getParent() != null) {
            for (COREFeature sib : feature.getParent().getChildren()) {
                if (hasReexposedDescendant(reuse, sib) || reexposed.contains(sib)) {
                    nbDisplayed++;
                }
                if (selected.contains(sib)) {
                    nbSelected++;
                }
            }
        }
        if (orRel && (nbDisplayed < 2 || nbSelected > 0)) {
            if (selected.contains(feature) || (nbDisplayed < 2 && nbSelected == 0)) {
                rel = COREFeatureRelationshipType.MANDATORY;
            } else if (nbSelected > 0) {
                rel = COREFeatureRelationshipType.OPTIONAL;
            }
        } else if (!orRel && (isReuse || !reuse.getSelectedConfiguration().getReexposed().contains(feature))) {
            rel = COREFeatureRelationshipType.MANDATORY;
        }
        return new FeatureView(feature, parent, rel, isReuse, reuse, displayReuse);
    }

    /**
     * Register the listeners for all {@link FeatureView}s.
     */
    private void registerListeners() {
        for (FeatureView v : features.values()) {
            registerListeners(v);
        }
        for (List<FeatureView> fvs : reuses.values()) {
            for (FeatureView fv : fvs) {
                registerListeners(fv);
            }
        }
    }

    /**
     * Register the listeners for a given {@link FeatureView}.
     * 
     * @param feature - The {@link FeatureView} to register listeners for.
     */
    protected abstract void registerListeners(FeatureView feature);

    /*
     * ----------------------- DISPLAY -------------------
     */

    /**
     * Display the whole {@link FeatureDiagramView} according to user defined preferences.
     * Only chosen features will be shown (see shouldDisplay())
     */
    private void displayDiagram() {
        displayFeature(tree.getRoot());
        registerListeners();
    }

    /**
     * Redraw the diagram of features.
     * 
     * @param repopulate - Whether the tree has to be rebuilt or not. For example is a {@link FeatureView} has been
     *            added or removed
     */
    public void updateFeaturesDisplay(boolean repopulate) {
        containerLayer.removeAllChildren();
        if (repopulate) {
            populateTree();
        }
        computeFeaturesPosition();
        displayDiagram();
    }

    /**
     * Display the given {@link FeatureView} and its children.
     * Draws line to parent and if necessary symbols for the relationships.
     * 
     * @param pass - The {@link FeatureView} to display.
     */
    protected void displayFeature(FeatureView pass) {
        // Display feature
        pass.setPositionRelativeToParent(new Vector3D(pass.getXposition(), pass.getYposition(), 0));
        containerLayer.addChild(pass);

        for (FeatureView child : tree.getChildren(pass)) {
            displayFeature(child);
        }

        // Draw line to parent
        drawLine(pass, pass.getParentFeatureView());
    }

    /**
     * Function called to draw a line between parent and the child.
     * 
     * @param child - child feature
     * @param parent - parent feature
     */
    protected void drawLine(FeatureView child, FeatureView parent) {

        MTPolygon triangle = null;
        // Draw XOR/OR relationShip only if not collapsed
        if (tree.getChildrenList(child).size() > 0) {
            triangle = drawRelationshipTriangle(child);
        }
        // Draw the circle for MANDATORY/OPTIONALlationship
        MTEllipse circle = drawRelationshipCircle(child);

        child.updateRelationshipColor();

        int radius = (circle != null) ? (int) child.getRelationshipCircle().getRadiusX() : 0;

        float xposition = child.getXposition();
        float yposition = child.getYposition() - child.getHeight() / 2;

        if (parent != null) {

            float xposition2 = parent.getXposition();
            float yposition2 = parent.getYposition() + parent.getHeight() / 2;
            RamLineComponent line = child.getLineToParent();

            if (line != null) {
                // Destroy previous line
                containerLayer.removeChild(line);
                line.destroy();
            }

            line = new RamLineComponent(xposition, yposition - radius, xposition2, yposition2);

            float deltaY = yposition - yposition2;
            float deltaX = xposition - xposition2;
            float degrees = 0;

            degrees = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));

            child.setAngleWithRespectToParent(360 - degrees);
            child.setLineToParent(line);

            // Display reuses with a dotted line and and arrow
            if (child.isReuse() && child.getParentRelationship() == COREFeatureRelationshipType.NONE) {
                line.setLineStipple(LineStyle.DOTTED.getStylePattern());

                MTPolygon polygon = createArrowPolygon(xposition, yposition);

                // Rotating it with degrees will put it onto the opposite side.
                // Therefore we need to rotate it by 180 degrees less.
                float degree = degrees - 180;
                polygon.rotateZ(new Vector3D(xposition, yposition), degree);
                containerLayer.addChild(polygon);
                child.setReuseArrow(polygon);
            }

            containerLayer.addChild(line);
        }

        if (circle != null) {
            circle.setPositionGlobal(new Vector3D(xposition, yposition - radius, 0));
            circle.setPickable(true);
            containerLayer.addChild(circle);
        }
        if (triangle != null) {
            containerLayer.addChild(triangle);
        }

    }

    /**
     * Place the icons corresponding to selections on all features.
     */
    public void placeIcons() {
        placeIcons(getRootFeature());
    }

    /**
     * Place the icons corresponding to selections on a feature and its children.
     * (only if they are currently being displayed)
     * 
     * @param feature - The {@link FeatureView} to consider
     */
    private void placeIcons(FeatureView feature) {

        for (FeatureView child : feature.getChildrenFeatureViews()) {
            // Recursively add icons to the children
            placeIcons(child);
        }

        if (!containerLayer.containsChild(feature)) {
            return;
        }

        SelectionStatus selectionStatus = feature.getSelectionStatus();
        RamImageComponent image = feature.getImage();

        // If it is selected, or auto-selected , then add the check icon.
        // TODO: Should be double check if it is auto-selected.
        if (selectionStatus == SelectionStatus.AUTO_SELECTED || selectionStatus == SelectionStatus.SELECTED) {
            image = new RamImageComponent(Icons.ICON_SELECTED, Colors.FEATURE_SELECTMODE_ICON_COLOR);
        } else if (selectionStatus == SelectionStatus.WARNING_SELECTED
                || selectionStatus == SelectionStatus.WARNING_NOT_SELECTED) {
            // If it is a warning, show a cross symbol.
            image = new RamImageComponent(Icons.ICON_SELECTION_CLASH, Colors.FEATURE_SELECTMODE_ICON_COLOR);
        } else if (selectionStatus == SelectionStatus.RE_EXPOSED) {
            // If it is re-exposed and display, show an arrow symbol.
            image = new RamImageComponent(Icons.ICON_REEXPOSED, Colors.FEATURE_SELECTMODE_ICON_COLOR);
        }

        if (image != null) {
            Vector3D position = new Vector3D(feature.getXposition() + feature.getWidth() / 2, feature.getYposition());
            image.setPositionGlobal(position);
            getContainerLayer().addChild(image);
            feature.setImage(image);
        }
    }

    /**
     * Draws the view associated for OR or XOR relationships if any.
     * 
     * @param pass - The {@link FeatureView} to consider.
     * @return the created view, or null if none.
     */
    private MTPolygon drawRelationshipTriangle(FeatureView pass) {
        MTPolygon triangle = pass.getRelationshipTriangle();

        boolean needTriangle = pass.getChildrenRelationship() == COREFeatureRelationshipType.XOR
                || pass.getChildrenRelationship() == COREFeatureRelationshipType.OR;

        // Delete the existing triangle since event if we need one, we need to redraw it so that it is well positionned
        if (triangle != null) {
            if (containerLayer.containsChild(triangle)) {
                containerLayer.removeChild(triangle);
            }
            triangle.destroy();
            pass.setRelationshipTriangle(null);
        }

        if (!needTriangle) {
            return null;
        }
        // Find the first child that is not a reuse
        FeatureView firstChild = null;
        for (FeatureView fv : pass.getChildrenFeatureViews()) {
            if (!fv.isReuse()) {
                firstChild = fv;
                break;
            }
        }
        if (firstChild == null) {
            return null;
        }

        FeatureView lastChild = pass.getChildrenFeatureViews().get(pass.getChildrenFeatureViews().size() - 1);
        Vertex[] vertices = new Vertex[3];
        vertices[0] = new Vertex(pass.getXposition(), pass.getYposition() + pass.getHeight() / 2);
        vertices[1] = new Vertex(firstChild.getXposition(), firstChild.getYposition() - pass.getHeight() / 2);
        vertices[2] = new Vertex(lastChild.getXposition(), lastChild.getYposition() - pass.getHeight() / 2);
        double ratio = 0.2;
        float xposition1 = (float) ((1 - ratio) * vertices[0].getX() + ratio * vertices[1].getX());
        float yposition1 = (float) ((1 - ratio) * vertices[0].getY() + ratio * vertices[1].getY());

        float xposition2 = (float) ((1 - ratio) * vertices[0].getX() + ratio * vertices[2].getX());
        float yposition2 = (float) ((1 - ratio) * vertices[0].getY() + ratio * vertices[2].getY());

        vertices[1] = new Vertex(xposition1, yposition1);
        vertices[2] = new Vertex(xposition2, yposition2);

        triangle = new MTPolygon(RamApp.getApplication(), vertices);
        triangle.setNoStroke(false);
        triangle.setStrokeColor(Colors.DEFAULT_ELEMENT_STROKE_COLOR);
        triangle.setStrokeWeight(2.0f);

        if (pass.getChildrenRelationship() == COREFeatureRelationshipType.OR) {
            triangle.setNoFill(false);
            triangle.setFillColor(Colors.FEATURE_OR_RELATIONSHIP_COLOR);
        } else if (pass.getChildrenRelationship() == COREFeatureRelationshipType.XOR) {
            triangle.setNoFill(true);
        }

        pass.setRelationshipTriangle(triangle);

        return triangle;
    }

    /**
     * Draws the view associated for MANDATORY or OPTIONAL relationships if any.
     * 
     * @param pass - The {@link FeatureView} to consider.
     * @return the created view, or null if none.
     */
    private MTEllipse drawRelationshipCircle(FeatureView pass) {
        boolean needCircle = pass.getParent() != null
                && (pass.getParentRelationship() == COREFeatureRelationshipType.MANDATORY || pass
                        .getParentRelationship() == COREFeatureRelationshipType.OPTIONAL);

        if (!needCircle) {
            if (pass.getRelationshipCircle() != null) {
                if (containerLayer.containsChild(pass.getRelationshipCircle())) {
                    containerLayer.removeChild(pass.getRelationshipCircle());
                }
                pass.getRelationshipCircle().destroy();
                pass.setCircle(null);
            }
            return null;
        } else if (pass.getRelationshipCircle() == null) {
            MTEllipse relCircle = new MTEllipse(RamApp.getApplication(), new Vector3D(getWidth() / 2, -6, 0), 8, 8);
            relCircle.removeAllGestureEventListeners();
            relCircle.setNoStroke(true);
            pass.setCircle(relCircle);
        }
        return pass.getRelationshipCircle();
    }

    /**
     * Creates an arrow polygon at the given location.
     * 
     * @param x the x position
     * @param y the y position
     * @return the polygon representing an arrow
     */
    private static MTPolygon createArrowPolygon(float x, float y) {
        Vertex start = new Vertex(x + 12, y - 5);
        Vertex center = new Vertex(x, y);
        Vertex end = new Vertex(x + 12, y + 5);

        MTPolygon polygon = new MTPolygon(RamApp.getApplication(), new Vertex[] { start, center, end });

        // trick to use polygon as a polyline
        polygon.setNoFill(true);
        polygon.setStrokeWeight(2.0f);
        polygon.setStrokeColor(Colors.DEFAULT_ELEMENT_STROKE_COLOR);
        polygon.setEnabled(false);
        polygon.setPickable(false);

        return polygon;
    }

    /**
     * Function called to collect all Aspects realized by the Features in the diagram.
     * 
     * @return SetofAspects
     */
    public Set<Aspect> collectRealizedAspects() {
        Set<Aspect> aspects = new HashSet<Aspect>();
        for (FeatureView feature : collectFeatureViews(true)) {
            for (COREModel model : feature.getFeature().getRealizedBy()) {
                if (model instanceof Aspect) {
                    aspects.add((Aspect) model);
                }
            }
        }
        return aspects;
    }

    /**
     * Get the FeatureView closest to the position in the diagram, if there is one.
     * 
     * @param position - The position to check
     * @return the closest {@link FeatureView} or null if there is no close enough feature
     */
    public FeatureView liesAround(Vector3D position) {
        for (FeatureView feature : collectFeatureViews(true)) {
            if (MathUtils.pointIsInRectangle(position, feature, Constants.MARGIN_ELEMENT_DETECTION)) {
                return feature;
            }
        }
        return null;
    }

    /**
     * Get the FeatureView containing the given position in the diagram, if there is one.
     * 
     * @param position - The position to check
     * @return the closest {@link FeatureView} or null if there is no feature at this position
     */
    public FeatureView liesInside(Vector3D position) {
        for (FeatureView feature : collectFeatureViews(true)) {
            if (feature.containsPointGlobal(position)) {
                return feature;
            }
        }
        return null;
    }

    /**
     * Get all the {@link FeatureView} of the diagram.
     * 
     * @param getReuses - Whether we want the views coming from reuses as well or not.
     * @return the set of {@link FeatureView}
     */
    public Set<FeatureView> collectFeatureViews(boolean getReuses) {
        Set<FeatureView> views = new HashSet<FeatureView>(features.values());
        if (getReuses) {
            for (COREReuse reuse : reuses.keySet()) {
                views.addAll(reuses.get(reuse));
            }
        }
        return views;
    }

    /**
     * Get the root {@link FeatureView}.
     * 
     * @return The root {@link FeatureView}
     */
    public FeatureView getRootFeature() {
        return features.get(featureModel.getRoot());
    }

    /*
     * --------------- LAYOUT --------------------
     */
    /**
     * Function to return back the data structure of Rectangle.
     * 
     * @param node - Pass the node from which the rectangle needs to be extracted.
     * @return Rectangle - Return the rectangle corresponding to the node.
     */
    private static Rectangle2D.Double getBoundsOfNode(FeatureView node) {
        return treeLayout.getNodeBounds().get(node);
    }

    /**
     * Function called to get all the children of the parent.
     * 
     * @param parent - the feature
     * @return Children
     */
    private static Iterable<FeatureView> getChildren(FeatureView parent) {
        return treeLayout.getTree().getChildren(parent);
    }

    /**
     * Function called to set the position of all the features. In turn calls recursive functions which further make the
     * alignment perfect.
     */
    private void computeFeaturesPosition() {
        FeatureView rootFeature = getRootFeature();
        recursiveCall(rootFeature, 0, (float) gapBetweenLevels, 0);

        // This is to to determine at what percentage the feature is to be moved in relative spacing to width of screen.
        float offsetValue = rootFeature.getXposition() - (getWidth() / 2);
        offSet(rootFeature, offsetValue);
    }

    /**
     * Populate the tree layout with the {@link FeatureView} we want to display.
     */
    private void populateTree() {
        FeatureView rootFeature = getRootFeature();
        if (rootFeature == null) {
            return;
        }

        // Create a tree with root feature as per the existing data structure in jar.
        tree = new DefaultTreeForTreeLayout<FeatureView>(rootFeature);

        // With retrospect to rootFeature, add all to the featureViews.
        recurseAndAdd(rootFeature);

        // Copy the horizontal minimum distance.
        double gapBetweenNodes = INITIAL_HORIZONTAL_DISTANCE;

        // A configuration is created according to the data structure provided in the jar, passing the above two values.
        DefaultConfiguration<FeatureView> configuration = new DefaultConfiguration<FeatureView>(gapBetweenLevels,
                gapBetweenNodes);

        // Create the NodeExtentProvider for TextInBox nodes
        NodeExtentProvider<FeatureView> nodeExtentProvider = new DefaultNodeExtentProvider<FeatureView>();

        // Create the layout - Created by passing the tree, the configuration and the nodeExtentProvider.
        treeLayout = new TreeLayout<FeatureView>(tree, nodeExtentProvider, configuration);
    }

    /**
     * Offsets position of a feature and its children on the X axis.
     * 
     * @param feature - the feature to move
     * @param offSetValue - the offset to apply
     */
    private void offSet(FeatureView feature, float offSetValue) {
        feature.setXposition(feature.getXposition() - offSetValue);

        for (FeatureView child : feature.getChildrenFeatureViews()) {
            offSet(child, offSetValue);
        }
    }

    /**
     * Function called to adjust all the final positions of the feature. Recursive function. Calls all its children by
     * itself.
     *
     * @param feature - The feature to which the position is to be computed.
     * @param offsetX - The offset for the X axis.
     * @param offsetY - The gap between each level of the Feature Model.
     * @param position - THe horizontal position of the node.
     */
    private void recursiveCall(FeatureView feature, float offsetX, float offsetY, int position) {
        Rectangle2D.Double b1 = getBoundsOfNode(feature);
        double x1 = b1.getCenterX();
        double y1 = ((position * offsetY) + ((position + 1) * offsetY)) / 2;

        feature.setXposition((float) x1);
        feature.setYposition((float) y1);

        for (FeatureView child : getChildren(feature)) {
            recursiveCall(child, offsetX, offsetY, position + 1);
        }
    }

    /**
     * Function to recursively add all features from Feature Model to layouting data structure.
     * 
     * @param feature - feature to add to the tree
     */
    protected void recurseAndAdd(FeatureView feature) {
        for (FeatureView child : feature.getChildrenFeatureViews()) {
            DisplayOptions display = shouldDisplay(child);
            if (display == DisplayOptions.HIDE) {
                continue;
            }
            tree.addChild(feature, child);
            if (display != DisplayOptions.COLLAPSE) {
                recurseAndAdd(child);
                child.setLineStipple(LineStyle.SOLID.getStylePattern());
            } else {
                child.setLineStipple(LineStyle.DOTTED.getStylePattern());
            }
        }
    }

    /**
     * Check whether a {@link FeatureView} should be displayed or not.
     * This can be used to prevent some children and/or their children to be displayed.
     * 
     * @param child - The {@link FeatureView} we want to display
     * @return Whether the view should be displayed, hidden or collapsed
     */
    protected DisplayOptions shouldDisplay(FeatureView child) {
        if (collapsedFeatures.contains(child)) {
            return DisplayOptions.COLLAPSE;
        }
        return DisplayOptions.SHOW;
    }

    /**
     * Stop collapsing the given feature if it has no right to be collapsed anymore.
     * 
     * @param feature - the feature to expand
     */
    private void checkCollapseValildity(FeatureView feature) {
        if (collapsedFeatures.contains(feature) && !canCollapse(feature)) {
            collapsedFeatures.remove(feature);
            feature.setLineStipple(LineStyle.SOLID.getStylePattern());
        }
    }

    /**
     * Check if we have the right to collapse a given feature.
     * We can't if it's the root, or if it has no children.
     * 
     * @param featureView - The given {@link FeatureView}
     * @return whether the feature can be collapsed or not.
     */
    protected boolean canCollapse(FeatureView featureView) {
        if (featureView == null || featureView.getParentFeatureView() == null) {
            return false;
        }
        if (featureView.isReuse() || featureView.isParentReuse()) {
            return true;
        }
        // Check if the feature has children only display children COREFeatures.
        return !featureView.getChildrenFeatureViews().isEmpty();
    }

    /**
     * Check all the features to see if they have to be un-collapsed.
     */
    public void checkCollapseValidity() {
        // Remove features that can no longer be collapsed
        for (FeatureView feature : features.values()) {
            checkCollapseValildity(feature);
        }
    }

    /**
     * Check if the diagram has at least a collapsed element.
     * 
     * @param onlyVisible - whether we want to check all elements, or only those that are visible in the current mode
     * @return true if at least a feature is collapsed
     */
    public final boolean hasCollapsedElements(boolean onlyVisible) {
        return getAllCollapsed(onlyVisible).size() > 0;
    }

    /**
     * Get all the currently collapsed {@link FeatureView}s.
     * 
     * @param onlyVisible - Whether we should return all collapsed features or only those visible in the current mode.
     * @return The set of collapsed {@link FeatureView}s
     */
    protected Set<FeatureView> getAllCollapsed(boolean onlyVisible) {
        Set<FeatureView> collapsed = new HashSet<FeatureView>();
        // Add normal features
        for (FeatureView feature : features.values()) {
            collapsed.addAll(getCollapsedFeatures(feature, onlyVisible));
        }
        // Add features from reuses
        for (List<FeatureView> fvs : reuses.values()) {
            for (FeatureView feature : fvs) {
                collapsed.addAll(getCollapsedFeatures(feature, onlyVisible));
            }
        }

        return collapsed;
    }

    /**
     * Check is a given feature is currently collapsed or displayed.
     * If it is, return the list of collapsed features above this one in the hierarchy.
     * 
     * @param feature - The {@link COREFeature} to check
     * @return The collapsed features, or an empty list if none.
     */
    protected Set<FeatureView> getCollapsedFeatures(FeatureView feature) {
        return getCollapsedFeatures(feature, false);
    }

    /**
     * Check is a given feature is currently collapsed or displayed.
     * If it is, return the list of collapsed features above this one in the hierarchy.
     * 
     * @param feature - The {@link COREFeature} to check
     * @param onlyVisible - Whether we should return all collapsed features, or only the ones we see in current mode
     * @return The collapsed features, or an empty list if none.
     */
    protected Set<FeatureView> getCollapsedFeatures(FeatureView feature, boolean onlyVisible) {
        if (feature == null) {
            return new HashSet<FeatureView>();
        }
        Set<FeatureView> collapsed = new HashSet<FeatureView>();
        if (collapsedFeatures.contains(feature)) {
            collapsed.add(feature);
        }
        if (feature.getParentFeatureView() != null) {
            collapsed.addAll(getCollapsedFeatures(feature.getParentFeatureView(), onlyVisible));
        }
        return collapsed;
    }

}