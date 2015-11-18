package ca.mcgill.sel.ram.ui.views.feature.helpers;

import java.util.Set;

import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.core.controller.ControllerFactory;
import ca.mcgill.sel.core.controller.FeatureController;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.scenes.DisplayConcernEditScene;
import ca.mcgill.sel.ram.ui.utils.Strings;

/**
 * Class used to represent a Constraint between two features. The constraint owner is where the constraint originates
 * from to the target.
 * 
 * @author Nishanth
 */
public class Constraint {
    
    /**
     * Represents the different kind of constraints that exist.
     */
    public enum ConstraintType {
        /**
         * Constraint used to express that two features cannot be selected at the same time.
         */
        EXCLUDES(Strings.SYMBOL_EXCLUDE_CONSTRAINT),
        /**
         * Constraint used to express that a feature requires another one to be selected.
         */
        REQUIRES(Strings.SYMBOL_REQUIRE_CONSTRAINT);
        
        private String symbol;
        
        /**
         * Constructor for {@link ConstraintType}.
         * 
         * @param symbol - the symbol which represents the constraint.
         */
        private ConstraintType(String symbol) {
            this.symbol = symbol;
        }
        
        /**
         * Get the symbol which represents the constraint.
         * 
         * @return the symbol associated with the constraint
         */
        public String getSymbol() {
            return this.symbol;
        }
    };
    
    private COREFeature owner;
    private COREFeature target;
    private ConstraintType type;
    
    /**
     * Constructor for the constraint class.
     * 
     * @param owner - The owner of the constraint.
     * @param target - The COREFeature to which the constraint is applied to.
     * @param type - the type of the constraint
     */
    public Constraint(COREFeature owner, COREFeature target, ConstraintType type) {
        this.owner = owner;
        this.target = target;
        this.type = type;
    }
    
    /**
     * Function called when a constraint has be deleted.
     */
    public void delete() {
        DisplayConcernEditScene concernScene = RamApp.getActiveConcernEditScene();
        
        if (concernScene != null) {
            FeatureController controller = ControllerFactory.INSTANCE.getFeatureController();
            if (type == ConstraintType.EXCLUDES) {
                controller.removeExcludesConstraint(concernScene.getConcern(), owner, target);
            } else if (type == ConstraintType.REQUIRES) {
                controller.removeRequiresConstraint(concernScene.getConcern(), owner, target);
            }
        }
    }
    
    @Override
    public String toString() {
        return owner.getName() + " " + type.getSymbol() + " " + target.getName();
    }
    
    /**
     * Function used to return the owner of the constraint.
     * 
     * @return - The owner of the constraint.
     */
    public COREFeature getOwner() {
        return owner;
    }
    
    /**
     * Function used to return the target of the constraint.
     * 
     * @return - The target of the constraint.
     */
    public COREFeature getTarget() {
        return target;
    }
    
    /**
     * Function used to return the type of the constraint.
     * 
     * @return the type of the constraint
     */
    public ConstraintType getType() {
        return type;
    }
    
    /**
     * Check if a constraint is valid in regard of the selected and reexposed features.
     * 
     * @param selectedFeatures - the set of selected features
     * @param reExposedFeatures - the set of reexposed features
     * @return true if the constraint is valid, false otherwise
     */
    public boolean isValid(Set<COREFeature> selectedFeatures, Set<COREFeature> reExposedFeatures) {
        boolean result = true;
        
        switch (type) {
            case EXCLUDES:
                if (selectedFeatures.contains(owner)
                        && (selectedFeatures.contains(target) || reExposedFeatures.contains(target))) {
                    return false;
                }
                break;
            case REQUIRES:
                if (selectedFeatures.contains(owner) && !selectedFeatures.contains(target)
                        || (reExposedFeatures.contains(owner) 
                        && !(selectedFeatures.contains(target) || reExposedFeatures.contains(target)))) {
                    return false;
                }
                break;
        }
        
        return result;
    }
    
    /**
     * Get the symbol used to display the given Constraint.
     * 
     * @return the symbol representing the constraint.
     */
    public String getSymbol() {
        return type.getSymbol();
    }
}
