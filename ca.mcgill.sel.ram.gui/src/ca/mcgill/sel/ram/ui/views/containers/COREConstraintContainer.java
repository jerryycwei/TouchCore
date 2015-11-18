package ca.mcgill.sel.ram.ui.views.containers;

import java.util.Collection;
import java.util.Comparator;

import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.ram.ui.components.RamButton;
import ca.mcgill.sel.ram.ui.components.RamImageComponent;
import ca.mcgill.sel.ram.ui.components.RamListComponent.Namer;
import ca.mcgill.sel.ram.ui.components.RamPanelListComponent;
import ca.mcgill.sel.ram.ui.components.RamRectangleComponent;
import ca.mcgill.sel.ram.ui.components.RamTextComponent;
import ca.mcgill.sel.ram.ui.events.listeners.ActionListener;
import ca.mcgill.sel.ram.ui.layouts.HorizontalLayoutVerticallyCentered;
import ca.mcgill.sel.ram.ui.utils.Colors;
import ca.mcgill.sel.ram.ui.utils.Fonts;
import ca.mcgill.sel.ram.ui.utils.Icons;
import ca.mcgill.sel.ram.ui.utils.Strings;
import ca.mcgill.sel.ram.ui.views.TextView;
import ca.mcgill.sel.ram.ui.views.feature.helpers.Constraint;

/**
 * Container class used to contain all the constraints on a feature model. Each entry contains a delete button and the
 * constraint specification.
 * 
 * @author Nishanth
 * @author CCamillieri
 */
public class COREConstraintContainer extends RamPanelListComponent<Constraint> {
    
    /**
     * The container takes in the root feature. It displays all the constraints concerning this feature.
     * 
     * @param horizontalStick - The horizontalStick for the Panel
     * @param verticalStick - The verticalStick for the Panel
     * @param deletable - whether we want to delete constraints or not.
     */
    public COREConstraintContainer(HorizontalStick horizontalStick, VerticalStick verticalStick, boolean deletable) {
        super(5, Strings.LABEL_CONSTRAINT_CONTAINER, horizontalStick, verticalStick);
        setNamer(new ConstraintNamer(deletable));
    }
    
    @Override
    public void setElements(Collection<Constraint> elements) {
        super.setElements(elements);
        list.setElementsOrder(new ConstraintsComparator());
    }
    
    /**
     * Comparator for constraints.
     */
    private class ConstraintsComparator implements Comparator<Constraint> {
        @Override
        public int compare(Constraint o1, Constraint o2) {
            String name1 = list.getNamer().getSortingName(o1);
            String name2 = list.getNamer().getSortingName(o2);
            return String.CASE_INSENSITIVE_ORDER.compare(name1, name2);
        }
    }
    
    /**
     * Namer used to display constraints.
     */
    private class ConstraintNamer implements Namer<Constraint> {
        
        private boolean deletable;
        
        /**
         * Constructor for {@link ConstraintNamer}.
         * 
         * @param deletable - whether the constraint can be deleted or not.
         */
        public ConstraintNamer(boolean deletable) {
            this.deletable = deletable;
        }
        
        @Override
        public RamRectangleComponent getDisplayComponent(final Constraint constraint) {
            RamRectangleComponent container = new RamRectangleComponent();
            container.setBufferSize(Cardinal.WEST, 5);
            container.setNoFill(false);
            container.setFillColor(Colors.DEFAULT_ELEMENT_FILL_COLOR);
            container.setNoStroke(false);
            container.setStrokeWeight(3.0f);
            container.setStrokeColor(Colors.DEFAULT_ELEMENT_STROKE_COLOR);
            
            // first add the delete button
            if (deletable) {
                RamButton deleteButton = buildDeleteButton(constraint);
                container.addChild(deleteButton);
            }
            
            RamRectangleComponent constraintCell = new RamRectangleComponent(new HorizontalLayoutVerticallyCentered());
            TextView from = new TextView(constraint.getOwner(), CorePackage.Literals.CORE_NAMED_ELEMENT__NAME);
            TextView to = new TextView(constraint.getTarget(), CorePackage.Literals.CORE_NAMED_ELEMENT__NAME);
            RamTextComponent symbol = new RamTextComponent(constraint.getSymbol());
            
            from.setFont(Fonts.FONT_CLASS_NAME);
            from.setNoStroke(true);
            from.setAutoMaximizes(true);
            to.setFont(Fonts.FONT_CLASS_NAME);
            to.setNoStroke(true);
            to.setAutoMaximizes(true);
            symbol.setFont(Fonts.FONT_CLASS_NAME);
            symbol.setNoStroke(true);
            symbol.setAutoMaximizes(true);
            
            constraintCell.addChild(from);
            constraintCell.addChild(symbol);
            constraintCell.addChild(to);
            
            container.addChild(constraintCell);
            container.setLayout(new HorizontalLayoutVerticallyCentered(5));
            return container;
        }
        
        /**
         * Create a deletion button for the given element.
         * 
         * @param element - the constraint
         * @return the button
         */
        private RamButton buildDeleteButton(final Constraint element) {
            RamImageComponent deleteImage = new RamImageComponent(Icons.ICON_CLOSE, Colors.ICON_CLOSE_COLOR);
            deleteImage.setMinimumSize(20, 20);
            deleteImage.setMaximumSize(20, 20);
            deleteImage.setBufferSize(Cardinal.WEST, 10);
            deleteImage.setBufferSize(Cardinal.EAST, 10);
            
            RamButton deleteButton = new RamButton(deleteImage);
            
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    // TODO: belongs to handler
                    element.delete();
                }
            });
            return deleteButton;
        }
        
        @Override
        public String getSortingName(Constraint element) {
            return element.getOwner().toString() + element.getSymbol() + element.getTarget().toString();
        }
        
        @Override
        public String getSearchingName(Constraint element) {
            return getSortingName(element);
        }
    };
    
}
