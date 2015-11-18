/**
 * <hr>
 * PreProcessor.java Created by @author walabe on Mar 16, 2013
 * @version 3.2
 * @since 3.0 i.e version of the project when this was created
 * <hr>
 * @copyright
 */
package ca.mcgill.sel.ram.weaver.structuralview;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import ca.mcgill.sel.commons.emf.util.EMFModelUtil;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.Attribute;
import ca.mcgill.sel.ram.AttributeMapping;
import ca.mcgill.sel.ram.Class;
import ca.mcgill.sel.ram.Classifier;
import ca.mcgill.sel.ram.ClassifierMapping;
import ca.mcgill.sel.ram.ImplementationClass;
import ca.mcgill.sel.ram.Instantiation;
import ca.mcgill.sel.ram.InstantiationType;
import ca.mcgill.sel.ram.Operation;
import ca.mcgill.sel.ram.OperationMapping;
import ca.mcgill.sel.ram.PrimitiveType;
import ca.mcgill.sel.ram.RAny;
import ca.mcgill.sel.ram.RCollection;
import ca.mcgill.sel.ram.REnum;
import ca.mcgill.sel.ram.RVoid;
import ca.mcgill.sel.ram.RamFactory;
import ca.mcgill.sel.ram.RamPackage;
import ca.mcgill.sel.ram.Type;
import ca.mcgill.sel.ram.weaver.util.WeavingInformation;

/**
 * <hr>
 * This is a helper class that is in charge of preparing for weaving by extracting and manipulating the mappings and
 * creating skeleton classes. All its methods are static. It must be called before weaving can occur
 * <hr>
 *
 * @author walabe
 */
public final class PreProcessor {
    
    /**
     * Our Private Constructor since this class only contains static methods
     * and has no state there is no need to instantiate an object of type PreProcessor.
     */
    private PreProcessor() {
        
    }
    
    /**
     * This method is the main method of pre-processing.
     * @param lowerLevelnstantiation The instantiation of higherLevelAspect that we are processing
     * @param higherLevelAspect The aspect that is being lowerLevelnstantiation is being applied to
     * @param weavingInformation The data structure containing all mapping information
     * @return true if this is an extends weaving or false if its a depends
     */
    public static boolean performPreProcess(Instantiation lowerLevelnstantiation, final Aspect higherLevelAspect,
            WeavingInformation weavingInformation) {
        Aspect lowerLevelAspect = lowerLevelnstantiation.getSource();
        boolean isExtends = false;
        
        processInstantiations(lowerLevelnstantiation, weavingInformation);

        // check to see the type of the instantiation
        if (lowerLevelnstantiation.getType().equals(InstantiationType.EXTENDS)) {
            PreProcessor.processExtends(higherLevelAspect, lowerLevelAspect, weavingInformation);
            isExtends = true;
        }

        processPrimitiveTypes(higherLevelAspect, lowerLevelAspect, weavingInformation);
        
        createSkeletonClasses(higherLevelAspect, lowerLevelAspect, weavingInformation);
        
        processCollectionTypes(higherLevelAspect, lowerLevelAspect, weavingInformation);
        
        return isExtends;
    }
    
    /**
     * This method will take all the instantiations that map to the lower level aspect and extract the
     * mappings and store them in our weavingInformation data structure.
     * @param lowerLevelnstantiation Our instantiation of the lower level aspect we are weaving
     * @param weavingInformation The data structure that holds all our mappings needed for weaving
     */
    private static void processInstantiations(Instantiation lowerLevelnstantiation,
            WeavingInformation weavingInformation) {
        // process the mappings
        for (ClassifierMapping classifierMapping : lowerLevelnstantiation.getMappings()) {
            // Handle special case where an element from another aspect was mapped.
            // First, figure out the aspect of the to element.
            
            Aspect higherLevelAspect = EMFModelUtil.getRootContainerOfType(lowerLevelnstantiation,
                    RamPackage.Literals.ASPECT);
            Classifier toElement = classifierMapping.getTo();
            Aspect otherAspect = (Aspect) toElement.eContainer().eContainer();
            
            // Remember this information for the other mappings.
            boolean otherAspectElementMapped = false;
            
            // In the case that a class from another aspect was mapped, we need
            // to create a skeleton class in the current aspect and adjust the mapping
            // to that class. Otherwise, the lower-level class would be woven into the
            // class in another aspect. See issue #63.
            if (otherAspect != higherLevelAspect && toElement instanceof Class) {
                otherAspectElementMapped = true;
                Class copy = shallowCopy((Class) toElement);
                higherLevelAspect.getStructuralView().getClasses().add(copy);
                classifierMapping.setTo(copy);
                
                // Remember it in case another weaver needs this information.
                weavingInformation.add(toElement, copy);
            }
            
            // TODO COM FEDFXDG
            weavingInformation.add(classifierMapping.getFrom(), classifierMapping.getTo());
            for (OperationMapping opMapping : classifierMapping.getOperationMappings()) {
                // Do the same for all mapped operations (because they are contained in the class of another aspect).
                // Actually create the complete operation and update the mapping.
                if (otherAspectElementMapped) {
                    Operation toOperation = opMapping.getTo();
                    
                    Operation copy = EcoreUtil.copy(toOperation);
                    opMapping.setTo(copy);
                    classifierMapping.getTo().getOperations().add(copy);
                    
                    // Remember it in case another weaver needs this information.
                    weavingInformation.add(toOperation, copy);
                }
                
                weavingInformation.add(opMapping.getFrom(), opMapping.getTo());
            }
            
            for (AttributeMapping attrMapping : classifierMapping.getAttributeMappings()) {
                weavingInformation.add(attrMapping.getFrom(), attrMapping.getTo());
            }
        }
    }
    
    /**
     * If the higher level aspect extends the lower level aspect then this method will ensure that
     * for every class in the higher level, if there is a matching class in the lower level then it
     * will create a mapping between them so that they can be woven together later on.
     * 
     * @param higherLevelAspect The Aspect being woven into
     * @param lowerLevelAspect The Aspect we are weaving into higherLevelAspect
     * @param weavingInformation The data structure that holds all our mappings needed for weaving
     */
    private static void processExtends(final Aspect higherLevelAspect, final Aspect lowerLevelAspect,
            WeavingInformation weavingInformation) {
        // for every class in the base find a matching class in the lower level
        // aspect
        for (final Classifier classFromHigherLevel : higherLevelAspect.getStructuralView().getClasses()) {
            Classifier classFromLowerLevel;
            // even though primitive types and collection types are
            // implementation classes
            // because they are added to the types set of structural view and
            // not the classes set
            // we should never find them here
            // TODO right now searching by name only should actually search by
            // name and operation signatures
            classFromLowerLevel = getClassFromAspect(lowerLevelAspect, classFromHigherLevel.getName());
            // if there is a matching class in the lower level aspect
            // then create a mapping between the class from the base and
            // the class from the lower level so that we can merge them
            // when weaving later on
            if (classFromLowerLevel != null) {
                // 1. Map the classes
                weavingInformation.add(classFromLowerLevel, classFromHigherLevel);
                
                // 2. Map all the attributes
                // TODO how to handle attributes
                
                if (classFromHigherLevel instanceof Class && classFromLowerLevel instanceof Class) {
                    for (final Attribute attrFromHigherLevel : ((Class) classFromHigherLevel).getAttributes()) {
                        for (final Attribute attrFromLowerLevel : ((Class) classFromLowerLevel).getAttributes()) {
                            if (attrFromHigherLevel.getName().equals(attrFromLowerLevel.getName())) {
                                // TODO check if two attributes match by name that their type
                                // also match
                                // TODO this is not fully correct if we allow attributes to have implementation classes
                                // as types
                                if (attrFromHigherLevel.getType().getName().equals(
                                        attrFromLowerLevel.getType().getName())) {
                                    weavingInformation.add(attrFromLowerLevel, attrFromHigherLevel);
                                    break;
                                }
                            }
                        }
                    }
                }
                
            }
        }
    }
    
    /**
     * This handles creating mappings correctly for primitive Types.
     * 
     * @param higherLevelAspect The Aspect being woven into
     * @param lowerLevelAspect The Aspect we are weaving into higherLevelAspect
     * @param weavingInformation The data structure that holds all our mappings needed for weaving
     */
    private static void processPrimitiveTypes(final Aspect higherLevelAspect, final Aspect lowerLevelAspect,
            WeavingInformation weavingInformation) {
        // hack because structural view contains types and classes and
        // classifiers are types there is a problem the primitive types which
        // are implementation classes
        // are being added to the types set and not the classes set
        for (final Type typeFromHigherLevel : higherLevelAspect.getStructuralView().getTypes()) {
            // we only execute if the base Type is a primitive type
            if (typeFromHigherLevel instanceof PrimitiveType) {
                final PrimitiveType primitiveTypeFromLowerLevel;
                primitiveTypeFromLowerLevel = getPrimitiveTypeFromAspect(lowerLevelAspect,
                        typeFromHigherLevel.getName());
                if (primitiveTypeFromLowerLevel != null) {
                    // TODO if class was mapped to primitive type by user (e.g., Map: Key -> String),
                    // the mapping in mappings will be overriden by the regular primitive type mapping
                    weavingInformation.add(primitiveTypeFromLowerLevel, typeFromHigherLevel);
                }
            } else if (typeFromHigherLevel instanceof RVoid) {
                final Type voidTypeFromLowerLevel;
                voidTypeFromLowerLevel = getVoidTypeFromAspect(lowerLevelAspect);
                if (voidTypeFromLowerLevel != null) {
                    // TODO if class was mapped to primitive type by user (e.g., Map: Key -> String),
                    // the mapping in mappings will be overriden by the regular primitive type mapping
                    weavingInformation.add(voidTypeFromLowerLevel, typeFromHigherLevel);
                }
            } else if (typeFromHigherLevel instanceof RAny) {
                final Type anyTypeFromLowerLevel;
                anyTypeFromLowerLevel = getAnyTypeFromAspect(lowerLevelAspect);
                if (anyTypeFromLowerLevel != null) {
                    // TODO if class was mapped to primitive type by user (e.g., Map: Key -> String),
                    // the mapping in mappings will be overriden by the regular primitive type mapping
                    weavingInformation.add(anyTypeFromLowerLevel, typeFromHigherLevel);
                }
            }
        }
        // We need to support enums coming from lower level aspect
        // The basic support would be to just copy the enum as is
        for (final Type enumFromLowerLevel : lowerLevelAspect.getStructuralView().getTypes()) {
            // we only execute if the base Type is an enumeration
            if (enumFromLowerLevel instanceof REnum) {
                // check to make sure the enumeration does not already exist in the higher level aspect
                REnum enumFromHigherLevel = getEnumerationFromAspect(higherLevelAspect, enumFromLowerLevel.getName());
                if (enumFromHigherLevel != null) {
                    // TODO does not handle merging assumes that the two enums are identical by name only
                    // does not check literals
                    weavingInformation.add(enumFromLowerLevel, enumFromHigherLevel);
                } else {
                    final REnum enumCopy = EcoreUtil.copy((REnum) enumFromLowerLevel);
                    weavingInformation.add(enumFromLowerLevel, enumCopy);
                    higherLevelAspect.getStructuralView().getTypes().add(enumCopy);
                }
            }
        }
    }
    
    /**
     * This is a helper method that searches all the primitive types in the
     * lower level aspect and tries to find one that matches name.
     * @param aspect The aspect to search in
     * @param name The name of the primitive type to search for
     * @return The primitive type in the aspect if found otherwise null if not found.
     */
    private static PrimitiveType getPrimitiveTypeFromAspect(final Aspect aspect, final String name) {
        for (final Type primitiveTypeFromAspect : aspect.getStructuralView().getTypes()) {
            if (primitiveTypeFromAspect instanceof PrimitiveType
                    &&
                    primitiveTypeFromAspect.getName().equals(name)) {
                return (PrimitiveType) primitiveTypeFromAspect;
            }
        }
        return null;
    }
    
    /**
     * This is a helper method that searches all the primitive types in the
     * lower level aspect and tries to find RAny type.
     * @param aspect The aspect to search in
     * @return The RAny type in the aspect if found otherwise null if not found.
     */
    private static RAny getAnyTypeFromAspect(final Aspect aspect) {
        for (final Type anyTypeFromAspect : aspect.getStructuralView().getTypes()) {
            if (anyTypeFromAspect instanceof RAny) {
                return (RAny) anyTypeFromAspect;
            }
        }
        return null;
    }
    
    /**
     * This is a helper method that searches all the primitive types in the
     * lower level aspect and tries to find one that matches name.
     * @param aspect The aspect to search in
     * @return The RVoid type in the aspect if found otherwise null if not found.
     */
    private static RVoid getVoidTypeFromAspect(final Aspect aspect) {
        for (final Type voidTypeFromAspect : aspect.getStructuralView().getTypes()) {
            if (voidTypeFromAspect instanceof RVoid) {
                return (RVoid) voidTypeFromAspect;
            }
        }
        return null;
    }
    
    /**
     * This helper method will search for a class in provided aspect that matches the name provided.
     * @param aspect The aspect to search in.
     * @param name The name of the class to search for.
     * @return The classifier that matches name or null if not found.
     */
    private static Classifier getClassFromAspect(final Aspect aspect, final String name) {
        for (final Classifier currentClass : aspect.getStructuralView().getClasses()) {
            if (currentClass.getName().equals(name)) {
                return currentClass;
            }
        }
        return null;
    }
    
    /**
     * This helper method will search for a class in the aspect
     * that matches name.
     * @param aspect the aspect to search in
     * @param enumName the name of the enumeration to search for
     * @return The enumeration that matches enumName or null if not found
     */
    private static REnum getEnumerationFromAspect(final Aspect aspect, final String enumName) {
        for (final Type enumFromAspect : aspect.getStructuralView().getTypes()) {
            if (enumFromAspect instanceof REnum) {
                if (enumFromAspect.getName().equals(enumName)) {
                    return (REnum) enumFromAspect;
                }
            }
        }
        return null;
    }
    
    /**
     * This method will create skeleton classes in the higher level aspect for all
     * classifiers in the lower level aspect that do not exist in the higher level. Furthermore, for each
     * class added via this function a mapping will be created between the lower level classifier and the matching
     * skeleton class created in the higher level. A skeleton class does not copy over the attributes, operations, and
     * association ends.
     * @param higherLevelAspect The Aspect lowerLevelAspect is being woven into
     * @param lowerLevelAspect The aspect being woven into higherLevelAspect
     * @param weavingInformation The data structure that holds all our mappings needed for weaving
     */
    private static void createSkeletonClasses(final Aspect higherLevelAspect, final Aspect lowerLevelAspect,
            WeavingInformation weavingInformation) {
        
        // First step is to add skeleton copies of all the unmapped classes from
        // the lower level aspect to the higher level aspect
        for (Iterator<Classifier> iterator = lowerLevelAspect.getStructuralView().getClasses().iterator(); iterator
                .hasNext();) {
            Classifier classFromLowerLevel = iterator.next();
            // check to see if the class from the dependee was not mapped
            if (!weavingInformation.wasWoven(classFromLowerLevel)
                    // added to prevent class getting created when its mapped to a primitive type
                    // e.g., Map: Key -> String
                    // (because in mappings, this mapping is overwritten in processPrimitiveTypes)
                    && weavingInformation.getToElements(classFromLowerLevel) == null) {
                // figure out if you are adding an implementation class or a
                // regular class
                if (classFromLowerLevel instanceof ImplementationClass) {
                    // create a copy of the implementation class to add to the
                    // base
                    final ImplementationClass implementationClassCopy =
                            EcoreUtil.copy((ImplementationClass) classFromLowerLevel);
                    // TODO for some odd reason the copy function copies association ends and operations but in
                    // shallow manner
                    implementationClassCopy.getAssociationEnds().clear();
                    implementationClassCopy.getOperations().clear();
                    // add the implementation class to the base
                    weavingInformation.add(classFromLowerLevel, implementationClassCopy);
                } else {
                    // create a copy of the class to add to the base
                    Class classCopy = shallowCopy((Class) classFromLowerLevel);
                    // add the class to the base
                    weavingInformation.add(classFromLowerLevel, classCopy);
                }
            }
        }
        
        // now add the classes to the base
        for (final EObject key : weavingInformation.getToElements()) {
            if (key instanceof Classifier) {
                if (!higherLevelAspect.getStructuralView().getClasses().contains(key)) {
                    if (!higherLevelAspect.getStructuralView().getTypes().contains(key)) {
                        higherLevelAspect.getStructuralView().getClasses().add((Classifier) key);
                    }
                }
            }
        }
    }
    
    /**
     * This method handles collection types.
     * @param higherLevelAspect The higher level aspect that lowerLevelAspect is being woven into
     * @param lowerLevelAspect The aspect being woven into higherLevelAspect
     * @param weavingInformation The data structure that holds all our mappings needed for weaving
     */
    private static void processCollectionTypes(final Aspect higherLevelAspect, final Aspect lowerLevelAspect,
            WeavingInformation weavingInformation) {
        final Set<RCollection> lowerLevelCollections = new HashSet<RCollection>();
        for (final Type typ : lowerLevelAspect.getStructuralView().getTypes()) {
            if (typ instanceof RCollection) {
                lowerLevelCollections.add((RCollection) typ);
            }
        }
        final Set<RCollection> higherLevelCollections = new HashSet<RCollection>();
        for (final Type typ : higherLevelAspect.getStructuralView().getTypes()) {
            if (typ instanceof RCollection) {
                higherLevelCollections.add((RCollection) typ);
            }
        }
        
        for (final RCollection collection : lowerLevelCollections) {
            if (!weavingInformation.wasWoven(collection)) {
                boolean exists = false;
                for (final RCollection collect : higherLevelCollections) {
                    if (collect.getType().equals(weavingInformation.getFirstToElement(collection.getType()))) {
                        weavingInformation.add(collection, collect);
                        exists = true;
                        break;
                    }
                }
                // if matching collection does not exists in higher level
                // create a new one.
                if (!exists) {
                    RCollection collectionCopy = EcoreUtil.copy(collection);
                    weavingInformation.add(collection, collectionCopy);
                    higherLevelAspect.getStructuralView().getTypes().add(collectionCopy);
                }
            }
        }
    }
    
    /**
     * This is a helper method that will create a shallow
     * copy of the class provided.
     * @param clazz The class we want to create a copy of
     * @return A shallow copy of the class clazz
     */
    private static Class shallowCopy(Class clazz) {
        Class copy = RamFactory.eINSTANCE.createClass();
        copy.setName(clazz.getName());
        copy.setPartiality(clazz.getPartiality());
        copy.setAbstract(clazz.isAbstract());
        // Copy the references and let them be updated at the end by the ReferenceUpdater.
        copy.getSuperTypes().addAll(clazz.getSuperTypes());
        
        return copy;
    }

}
