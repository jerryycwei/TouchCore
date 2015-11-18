package ca.mcgill.sel.ram.ui.scenes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.commons.emf.util.ResourceManager;
import ca.mcgill.sel.core.COREConcern;
import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.scenes.handler.IRamAbstractSceneHandler;
import ca.mcgill.sel.ram.ui.views.feature.FeatureDiagramView;
import ca.mcgill.sel.ram.ui.views.feature.FeatureView;
import ca.mcgill.sel.ram.ui.views.feature.helpers.Constraint;
import ca.mcgill.sel.ram.ui.views.feature.helpers.Constraint.ConstraintType;

/**
 * ConcernAbstractScene is used as an abstract class for rest of the concern scenes to extend it from.
 * 
 * @author Nishanth.
 * @param <T> the class for the feature diagram type
 * @param <E> generic type the handler
 */
public abstract class AbstractConcernScene<T extends FeatureDiagramView<?>, E extends IRamAbstractSceneHandler>
        extends RamAbstractScene<E> {

    /**
     * Constant number defined to refer to the menu position.
     */
    protected static final int FIVE = 5;

    /**
     * The root Feature of the Feature Model.
     */
    protected COREFeature root;

    /**
     * The concern representing the Feature Model.
     */
    protected COREConcern concern;

    /**
     * The file path of the concern.
     */
    protected File filePath;

    /**
     * View for the feature model.
     */
    protected T featureDiagramView;

    /**
     * View for the feature model.
     */
    protected MTRectangle concernRectangle;

    /**
     * Default Constructor which passes the RAMApplication and name of the scene.
     * 
     * @param application - The RAM Application
     * @param name - The scene name passed by the user
     * @param defaultActions - Whether we want the default actions or not.
     */
    public AbstractConcernScene(RamApp application, String name, boolean defaultActions) {
        super(application, name, defaultActions);

    }

    /**
     * Method used to initialize and display the whole scene.
     */
    protected abstract void replot();

    /**
     * Method used to redraw only the feature diagram after model changes for example.
     * 
     * @param repopulate - Whether we have to update the tree because a new element has been added or just update
     *            the position of the features and/or their links.
     */
    public abstract void redrawFeatureDiagram(boolean repopulate);

    /**
     * Getter of the concern.
     * 
     * @return concern
     */
    public COREConcern getConcern() {
        return concern;
    }

    /**
     * Function called to unload all the resources. Resource Manager takes up resources which have to be unloaded after
     * use.
     */
    public void unLoadAllResources() {
        Set<Aspect> list = new HashSet<Aspect>();

        list = featureDiagramView.collectRealizedAspects();

        for (Aspect aspect : list) {
            if (aspect.eResource() != null) {
                ResourceManager.unloadResource(aspect.eResource());
            }
        }
        ResourceManager.unloadResource(concern.eResource());

    }

    /**
     * Get children of the given feature that are part of the given list.
     * 
     * @param featurePassed - the feature to check
     * @param list - the list to check against
     * @return intersection between given list and children of passed feature
     */
    protected Set<COREFeature> getContainedChildren(COREFeature featurePassed, List<COREFeature> list) {

        Set<COREFeature> collectedChildren = new HashSet<COREFeature>();

        if (list.contains(featurePassed)) {
            collectedChildren.add(featurePassed);
        }

        for (COREFeature child : featurePassed.getChildren()) {
            if (list.contains(child)) {
                collectedChildren.add(child);
            }
            collectedChildren.addAll(getContainedChildren(child, list));
        }

        return collectedChildren;
    }

    /**
     * Get the FeatureView closest to the position in the diagram, if there is one.
     * 
     * @param position - The position to check
     * @return the closest {@link FeatureView} or null if there is no close enough feature
     */
    public FeatureView liesAround(Vector3D position) {
        return featureDiagramView.liesAround(position);
    }

    /**
     * Get the FeatureView containing the given position in the diagram, if there is one.
     * 
     * @param position - The position to check
     * @return the closest {@link FeatureView} or null if there is no feature at this position
     */
    public FeatureView liesInside(Vector3D position) {
        return featureDiagramView.liesInside(position);
    }

    /**
     * Get all the {@link FeatureView} of the diagram.
     * 
     * @param reuses - Whether we want to get features from reuses as well or not
     * @return the set of {@link FeatureView}
     */
    public Set<FeatureView> collectFeatureViews(boolean reuses) {
        return featureDiagramView.collectFeatureViews(reuses);
    }

    /**
     * Add recursively the constraints for the given feature and its children to the list.
     * 
     * @param feature - The feature to add constraints for
     * @return - The list of constraint for the feature and its children
     */
    protected List<Constraint> getConstraints(COREFeature feature) {
        List<Constraint> list = new ArrayList<Constraint>();
        for (COREFeature requires : feature.getRequires()) {
            Constraint constraint = new Constraint(feature, requires, ConstraintType.REQUIRES);
            list.add(constraint);
        }
        for (COREFeature excludes : feature.getExcludes()) {
            Constraint constraint = new Constraint(feature, excludes, ConstraintType.EXCLUDES);
            list.add(constraint);
        }
        for (COREFeature child : feature.getChildren()) {
            list.addAll(getConstraints(child));
        }
        return list;
    }

    /**
     * Check whether a feature is part of the collapsed features of the scene.
     * 
     * @param feature - The {@link FeatureView} to check.
     * @return true if the feature is collapsed.
     */
    public boolean isFeatureCollapsed(FeatureView feature) {
        return featureDiagramView.getCollapsedFeatures().contains(feature);
    }

}
