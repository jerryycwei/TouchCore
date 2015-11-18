/**
 * <hr>
 * AssociationsWeaver.java Created by @author walabe on Mar 16, 2013
 * @version 3.0
 * @since 3.0 i.e version of the project when this was created
 * <hr>
 * @copyright
 */
package ca.mcgill.sel.ram.weaver.structuralview;

import org.eclipse.emf.ecore.util.EcoreUtil;

import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.Association;
import ca.mcgill.sel.ram.AssociationEnd;
import ca.mcgill.sel.ram.Classifier;
import ca.mcgill.sel.ram.weaver.util.WeavingInformation;

/**
 * The AssociationsWeaver is in charge of handling the correct weaving of associations between two classes
 * mapped to one another.
 * 
 * @author walabe
 */
public final class AssociationsWeaver {
    
    /**
     * Our Private Constructor since this class only contains static methods
     * and has no state there is no need to instantiate an object of type AssociationsWeaver.
     */
    private AssociationsWeaver() {
        
    }
    
    /**
     * This method performs the weaving of associations.
     * @param classFromHigherLevel Higher level class that classFromLowerLevel associations ends will be woven into
     * @param classFromLowerLevel Lower level class whose association ends will be woven into classFromHigherLevel
     * @param higherLevelAspect The aspect that classFromHigherLevel belongs to
     * @param weavingInformation The data structure containing all mapping information
     */
    public static void weaveAssociations(final Classifier classFromHigherLevel, final Classifier classFromLowerLevel,
            final Aspect higherLevelAspect, final WeavingInformation weavingInformation) {
        for (final AssociationEnd assocEnd : classFromLowerLevel.getAssociationEnds()) {
            // iterate over all the association ends of the lower level class
            // only proceed if that association end has not been processed i.e not yet woven
            if (!weavingInformation.wasWoven(assocEnd)) {
                final AssociationEnd assocEndCopy = EcoreUtil.copy(assocEnd);
                
                final Association associationCopy = EcoreUtil.copy(assocEnd.getAssoc());
                AssociationEnd otherEnd = null;
                for (final AssociationEnd end : assocEnd.getAssoc().getEnds()) {
                    if (!end.equals(assocEnd)) {
                        otherEnd = end;
                    }
                }
                final AssociationEnd otherEndCopy = EcoreUtil.copy(otherEnd);
                
                Classifier otherEndClass = weavingInformation.getFirstToElement(otherEnd.getClassifier());
                
                if (!weavingInformation.wasWoven(otherEnd)) {
                    // since there is a bi-directional association
                    // between classifier and association end, setClassifier will automatically
                    // add the association end to the list of ends of the class
                    assocEndCopy.setClassifier(classFromHigherLevel);
                    otherEndCopy.setClassifier(otherEndClass);
                    // add the end copies to the association copy
                    associationCopy.getEnds().clear();
                    associationCopy.getEnds().add(otherEndCopy);
                    associationCopy.getEnds().add(assocEndCopy);
                    // add the association to the higher level aspect
                    higherLevelAspect.getStructuralView().getAssociations().add(associationCopy);
                    // add a mapping for the woven association ends and their copies
                    weavingInformation.add(assocEnd, assocEndCopy);
                    weavingInformation.add(otherEnd, otherEndCopy);
                }
            }
        }
    }
}
