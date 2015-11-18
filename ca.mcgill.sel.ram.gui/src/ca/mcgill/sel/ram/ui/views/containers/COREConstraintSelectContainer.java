package ca.mcgill.sel.ram.ui.views.containers;

import java.util.Set;

import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.ram.ui.components.RamRectangleComponent;
import ca.mcgill.sel.ram.ui.utils.Colors;
import ca.mcgill.sel.ram.ui.views.feature.helpers.Constraint;

/**
 * {@link COREConstraintContainer} containing the constraints on a feature model.
 * 
 * @author CCcamillieri
 */
public class COREConstraintSelectContainer extends COREConstraintContainer {
    
    /**
     * The container takes in the root feature of the model It displays all the constraint on the rootFeature and its
     * children.
     * 
     * @param hStick - The horizontalStick for the Panel
     * @param vStick - The verticalStick for the Panel
     */
    public COREConstraintSelectContainer(HorizontalStick hStick, VerticalStick vStick) {
        super(hStick, vStick, false);
    }
    
    /**
     * Puts back the constraints to their default background color.
     */
    private void resetColors() {
        for (RamRectangleComponent rectangelDisplay : list.getElementMap().values()) {
            rectangelDisplay.setFillColor(Colors.DEFAULT_ELEMENT_FILL_COLOR);
        }
    }
    
    /**
     * Checks if any constraints are violated in the selectedFeatures and color them red if so.
     * 
     * @param selectedFeatures the features that have been selected
     * @param reExposedFeatures the features that have been re-exposed
     * @return true if no constraint is violated, false otherwise
     */
    public boolean validate(Set<COREFeature> selectedFeatures, Set<COREFeature> reExposedFeatures) {
        
        boolean returnValue = true;
        
        // reset the color in the constraints list
        resetColors();
        
        Set<Constraint> constraintSet = list.getElementMap().keySet();
        
        // Loop through the constraint set
        for (Constraint constraint : constraintSet) {
            if (!constraint.isValid(selectedFeatures, reExposedFeatures)) {
                list.getElementMap().get(constraint).setFillColor(Colors.FEATURE_SELECTION_CLASH_COLOR);
                returnValue = false;
            }
        }
        
        return returnValue;
    }
    
}
