package ca.mcgill.sel.ram.ui.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ca.mcgill.sel.core.COREFeature;

/**
 * A singleton class used to store all the selections.
 * The selections can be composed of selections and reexposed features.
 * Every time a Feature Model Select Scene is displated, its clear all it entries.
 * @author Nishanth
 */
public final class SelectionsSingleton {
    
    private static SelectionsSingleton instance;
    
    private Set<COREFeature> selectedList = new HashSet<COREFeature>();
    private Set<COREFeature> reexposedList = new HashSet<COREFeature>();
    
    /**
     * Prevent to use constructor.
     */
    private SelectionsSingleton() {
    }
    
    /**
     * Function used to return one instance of the class (Singleton).
     * @return - Returns the instance (Singleton) of the class.
     */
    public static SelectionsSingleton getInstance() {
        if (instance == null) {
            instance = new SelectionsSingleton();
        }
        return instance;
    }
    
    /**
     * Function called when both the List have to be cleared.
     * Called when a new Feature Model Select Scene is loaded.
     */
    public void clearAll() {
        selectedList.clear();
        reexposedList.clear();
    }
    
    /**
     * Getter for the list of selected {@link COREFeature}.
     * @return - the list of selected {@link COREFeature}
     */
    public Set<COREFeature> getSelectedList() {
        return selectedList;
    }
    
    /**
     * Getter for the list of re-exposed {@link COREFeature}.
     * @return - the list of re-exposed {@link COREFeature}
     */
    public Set<COREFeature> getReexposedList() {
        return reexposedList;
    }
    
    /**
     * Used to add a new selection to the list.
     * @param feature - The {@link COREFeature} that was selected
     */
    public void addSelection(COREFeature feature) {
        selectedList.add(feature);
    }
    
    /**
     * Used to add new selections to the list.
     * @param features - The collection of {@link COREFeature} that were selected
     */
    public void addAllSelections(Collection<COREFeature> features) {
        selectedList.addAll(features);
    }
    
    /**
     * Used to remove selection from the list.
     * @param feature - The {@link COREFeature} that we want to be remove
     * @return true if the list contained the specified element
     */
    public boolean removeSelection(COREFeature feature) {
        return selectedList.remove(feature);
    }
    
    /**
     * Used to add a new re-exposition to the list.
     * @param feature - The {@link COREFeature} that was re-exposed
     */
    public void addReexposition(COREFeature feature) {
        reexposedList.add(feature);
    }
    
    /**
     * Used to add new re-expositions to the list.
     * @param features - The collection of {@link COREFeature} that were re-exposed
     */
    public void addAllReexpositions(Collection<COREFeature> features) {
        reexposedList.addAll(features);
    }
    
    /**
     * Used to remove a re-exposed feature from the list.
     * @param feature - The {@link COREFeature} that we want to be remove
     * @return true if the list contained the specified element
     */
    public boolean removeReexposition(COREFeature feature) {
        return reexposedList.remove(feature);
    }
    
    /**
     * Checks if a given feature is selected.
     * @param feature - The {@link COREFeature} to check
     * @return true if the feature is contained in the list of selected features
     */
    public boolean containsSelectedFeature(COREFeature feature) {
        return selectedList.contains(feature);
    }
    
    /**
     * Checks if a given feature is re-exposed.
     * @param feature - The {@link COREFeature} to check
     * @return true if the feature is contained in the list of re-rexposed features
     */
    public boolean containsReexposedFeature(COREFeature feature) {
        return reexposedList.contains(feature);
    }
    
}
