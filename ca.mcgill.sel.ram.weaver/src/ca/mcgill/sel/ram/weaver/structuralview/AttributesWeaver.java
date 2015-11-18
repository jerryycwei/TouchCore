/**
 * <hr>
 * AttibutesWeaver.java Created by @author walabe on Mar 16, 2013
 * @version 3.0
 * @since 3.0 i.e version of the project when this was created
 * <hr>
 * @copyright
 */
package ca.mcgill.sel.ram.weaver.structuralview;

import org.eclipse.emf.ecore.util.EcoreUtil;

import ca.mcgill.sel.ram.Attribute;
import ca.mcgill.sel.ram.Class;
import ca.mcgill.sel.ram.weaver.util.WeavingInformation;

/**
 * This class handles weaving of attributes.
 * @author walabe
 *
 */
public final class AttributesWeaver {
    
    /**
     * Default private constructor.
     */
    private AttributesWeaver() {
        
    }
    
    /**
     * This method will perform weaving of attributes.
     * @param classFromHigherLevel The class we are merging classFromLowerLevel into
     * @param classFromLowerLevel The class that will be merged into classFromHigherLevel
     * @param weavingInformation The data structure containing all mapping information
     */
    public static void weaveAttributes(final Class classFromHigherLevel, final Class classFromLowerLevel,
            final WeavingInformation weavingInformation) {
        
        /*
         * iterate through each attribute in the lower level class
         * and weave it into the higher level class
         */
        for (final Attribute attrFromLowerLevelClass : classFromLowerLevel.getAttributes()) {
            // check to see if the attribute was not mapped
            // if it was mapped it means we are renaming hence we do not need to modify
            // anything in the higher level class
            if (!weavingInformation.wasWoven(attrFromLowerLevelClass)) {
                // check to make sure that the attribute in the higherlevelclass does not already exist
                // TODO how to handle attributes
                /*
                 * for (final Attribute attrInHigherLevel : classFromHigherLevel.getAttributes()) {
                 * if (attrInHigherLevel.getName().equals(attrFromLowerLevelClass.getName())) {
                 * return;
                 * //TODO signal an error;
                 * //TODO check types
                 * }
                 * }
                 */
                // create a copy of the attribute that we will add to classFromHigherLevel
                final Attribute attrInHigherLevelClass = EcoreUtil.copy(attrFromLowerLevelClass);
                
                // create a mapping between attributes indicating they have been woven
                weavingInformation.add(attrFromLowerLevelClass, attrInHigherLevelClass);
                // and add the attribute to the class from the base
                classFromHigherLevel.getAttributes().add(attrInHigherLevelClass);
            }
        }
    }
}
