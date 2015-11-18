package ca.mcgill.sel.ram.weaver.structuralview;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;

import ca.mcgill.sel.commons.emf.util.EMFModelUtil;
import ca.mcgill.sel.core.COREModelReuse;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.Association;
import ca.mcgill.sel.ram.AssociationEnd;
import ca.mcgill.sel.ram.Classifier;
import ca.mcgill.sel.ram.ClassifierMapping;
import ca.mcgill.sel.ram.ImplementationClass;
import ca.mcgill.sel.ram.Instantiation;
import ca.mcgill.sel.ram.Operation;
import ca.mcgill.sel.ram.Parameter;
import ca.mcgill.sel.ram.TypeParameter;
import ca.mcgill.sel.ram.weaver.util.ReferenceUpdater;
import ca.mcgill.sel.ram.weaver.util.WeavingInformation;

/**
 * This class is in charge of handling the post processing phase after
 * weaving has occurred.
 * @author walabe
 *
 */
public final class PostProcessor {
    
    /**
     * Name of association end corresponding to the collection.
     */
    private static final String ASSOCIATION_END_NAME_COLLECTION = "myCollection";

    /**
     * Association classified mapping name for data.
     */
    private static final String DATA = "Data";

    /**
     * Association name between data and collection.
     */
    private static final String ASSOCIATION_NAME_DATA_COLLECTION = "Data_Collection";

    /**
     * Default private constructor.
     */
    private PostProcessor() {
        
    }
    
    /**
     * Main method of this class. Will perform all necessary updates.
     * @param aspect The woven aspect
     * @param lowerlevelAspect The aspect that was woven
     * @param weavingInformation The data structure containing all mapping information
     * @param instantiation The instantiation woven into the aspect
     */
    public static void performUpdates(Aspect aspect, Aspect lowerlevelAspect, WeavingInformation weavingInformation,
            Instantiation instantiation) {
        updateInstantiationDeclaratives(aspect, lowerlevelAspect, weavingInformation);
        
        updateReferences(aspect, weavingInformation);
        
        ArrayList<Classifier> duplicates = getDuplicateClasses(aspect, weavingInformation);
        
        mergeDuplicates(weavingInformation, duplicates);
        
        updateReferences(aspect, weavingInformation);
        
        removeDuplicates(aspect, duplicates);
        
        processAssociations(aspect, instantiation);
    }
    
    /**
     * This method will update the instantiations by iterating through
     * all instantiations in the lower level aspect and creating copies
     * and adding them to the woven aspect.
     * @param aspect The woven aspect
     * @param lowerLevelAspect The aspect that was woven
     * @param weavingInformation The data structure containing all mapping information
     */
    private static void updateInstantiationDeclaratives(Aspect aspect, Aspect lowerLevelAspect,
            WeavingInformation weavingInformation) {
        // If the lower-level aspect has instantiations (which could be the case when weaving top down),
        // it is necessary to copy the instantiations to the higher level aspect and update the references.
        // Updating is done in a separate loop for all instantiations (see below).
        for (Instantiation lowerLevelInst : lowerLevelAspect.getInstantiations()) {
            Instantiation instantiationCopy = EcoreUtil.copy(lowerLevelInst);
            aspect.getInstantiations().add(instantiationCopy);
        }
        
        // Update all instantiations. Do it for all, because an existing instantiation in the higher-level aspect
        // could contain a mapping to an element in another aspect, which could have been woven.
        // Then the mapping needs to be updated.
        // See issue #58.
        // This is now taken care of by the ReferenceUpdater (second step of the post processor).
    }
    
    /**
     * This method will perform the updating of references after weaving.
     * @param aspect The woven aspect
     * @param weavingInformation The data structure containing all mapping information
     */
    private static void updateReferences(Aspect aspect, WeavingInformation weavingInformation) {
        ReferenceUpdater updater = ReferenceUpdater.getInstance();
        
        // Update the structural view.
        updater.update(aspect.getStructuralView(), weavingInformation);
        // Update all instantiations.
        updater.update(aspect.getInstantiations(), weavingInformation);
        // Update the model reuses.
//        for (COREModelReuse modelReuse : aspect.getModelReuses()) {
//            for (COREModelCompositionSpecification<?> comp : modelReuse.getCompositions()) {
//                updater.update((Instantiation) comp, weavingInformation);
//                }
//        }
    }
    
    /**
     * This method will retrieve any duplicate classifiers as a result of weaving.
     * Must be called after updateReferences
     * @param aspect The woven aspect
     * @param weavingInformation The data structure containing all mapping information
     * @return the list of duplicates
     */
    private static ArrayList<Classifier> getDuplicateClasses(Aspect aspect, WeavingInformation weavingInformation) {
        ArrayList<Classifier> duplicates = new ArrayList<Classifier>();
        for (Classifier currentClass : aspect.getStructuralView().getClasses()) {
            if (!duplicates.contains(currentClass)) {
                getDuplicates(aspect, currentClass, weavingInformation, duplicates);
            }
        }
        
        return duplicates;
        
    }
    
    /**
     * This class will retrieve all duplicate classifiers that match classToCheck.
     * @param aspect The aspect we are searching
     * @param classToCheck The class to check for duplicates for
     * @param weavingInformation The data structure containing all mapping information
     * @param duplicates the data-structure to store duplicates in for removal later
     */
    private static void getDuplicates(Aspect aspect, Classifier classToCheck, WeavingInformation weavingInformation,
            ArrayList<Classifier> duplicates) {
        for (Classifier currentClass : aspect.getStructuralView().getClasses()) {
            // make sure not comparing a class with it self
            if (currentClass != classToCheck) {
                boolean match = true;
                // if we are inspecting implementation classes then we will be comparing their instance class name
                if (classToCheck instanceof ImplementationClass && currentClass instanceof ImplementationClass) {
                    match = ((ImplementationClass) currentClass).getInstanceClassName().equals(
                            ((ImplementationClass) classToCheck).getInstanceClassName());
                } else {
                    // otherwise just compare their names
                    match = currentClass.getName().equals(classToCheck.getName());
                }
                
                if (match) {
                    // if the names match then look at the type parameters and make sure
                    // they also match
                    for (TypeParameter currentTypeParam : currentClass.getTypeParameters()) {
                        // the moment there is a mismatch in terms of the type parameters
                        // we know that these two classes are not the same.
                        if (!typeParamExists(classToCheck, currentTypeParam)) {
                            match = false;
                            break;
                        }
                    }
                }
                
                if (match) {
                    // if the names and the type parameters match then flag this class as a duplicate
                    // by adding it to the duplicates data structure and create a mapping
                    // between this duplicate class and the class we are comparing it to i.e the one
                    // we will be keeping in the end.
                    duplicates.add(currentClass);
                    weavingInformation.add(currentClass, classToCheck);
                    // now check to see if there are any mappings in which currentClass is the toElement
                    // for example if A -> currentClass and we just added a new mapping
                    // currentClass -> classToCheck. we need to create another new mapping
                    // A -> classToCheck and delete old mapping A -> currentClass in order for updating
                    // of references to be correct
                    List<Classifier> temp = weavingInformation.getFromElements(currentClass);
                    for (Classifier classifier : temp) {
                        weavingInformation.add(classifier, classToCheck);
                        weavingInformation.getToElements(classifier).remove(currentClass);
                    }
                    
                }
            }
        }
        
    }
    
    /**
     * This method will merge all the duplicates into the element they are mapped to.
     * @param weavingInformation The data structure containing all mapping information.
     * @param duplicates The datastructure containing the duplicate classifiers.
     */
    private static void mergeDuplicates(WeavingInformation weavingInformation, ArrayList<Classifier> duplicates) {
        for (Classifier duplicateClass : duplicates) {
            Classifier classToCheck = weavingInformation.getFirstToElement(duplicateClass);
            for (Operation currentOp : duplicateClass.getOperations()) {
                Operation opFromHigherLevel =
                        OperationsWeaver.getExistingOperation(classToCheck, currentOp, weavingInformation);
                if (opFromHigherLevel != null) {
                    weavingInformation.add(currentOp, opFromHigherLevel);
                } else {
                    opFromHigherLevel = EcoreUtil.copy(currentOp);
                    classToCheck.getOperations().add(opFromHigherLevel);
                    weavingInformation.add(currentOp, opFromHigherLevel);
                }
                // now check to see if there are any mappings in which currentOp is the toElement
                // for example if A -> currentOp and we just added a new mapping
                // currentOp -> opFromHigherLevel. we need to create another new mapping
                // A -> opFromHigherLevel and delete old mapping A -> currentOp in order for updating
                // of references to be correct
                List<Operation> temp = weavingInformation.getFromElements(currentOp);
                for (Operation op : temp) {
                    weavingInformation.add(op, opFromHigherLevel);
                    weavingInformation.getToElements(op).remove(currentOp);
                }
                
                createParameterWeavingInformation(currentOp, opFromHigherLevel, weavingInformation);
            }
        }
    }
    
    /**
     * Adds parameter mapping info to weaving information for given operations.
     * @param fromOp The operation mapped to toOp
     * @param toOp The operation that fromOp is mapped to
     * @param weavingInformation The data structure containing all mapping information
     */
    private static void createParameterWeavingInformation(Operation fromOp, Operation toOp,
            WeavingInformation weavingInformation) {
        for (int index = 0; index < fromOp.getParameters().size(); index++) {
            Parameter oldParameter = fromOp.getParameters().get(index);
            Parameter newParameter = toOp.getParameters().get(index);
            weavingInformation.add(oldParameter, newParameter);
            // now check to see if there are any mappings in which oldParameter is the toElement
            // for example if A -> oldParameter and we just added a new mapping
            // oldParameter -> newParameter. we need to create another new mapping
            // A -> newParameter and delete old mapping A -> oldParameter in order for updating
            // of references to be correct
            List<Parameter> temp = weavingInformation.getFromElements(oldParameter);
            for (Parameter param : temp) {
                weavingInformation.add(param, newParameter);
                weavingInformation.getToElements(param).remove(oldParameter);
            }
        }
    }
    
    /**
     * This method will remove the duplicates from the aspect.
     * @param aspect The aspect to remove the duplicates from
     * @param duplicates The list of duplicates to be removed
     */
    private static void removeDuplicates(Aspect aspect, ArrayList<Classifier> duplicates) {
        
        for (Classifier classifier : duplicates) {
            aspect.getStructuralView().getClasses().remove(classifier);
        }
        
    }
    
    /**
     * This method will search in classToSearchIn for a type parameter that matches typeParamToLookFor.
     * @param classToSearchIn The class to search in
     * @param typeParamToLookFor The type parameter to look for in classToSearchIn
     * @return true if classToSearchIn is parameterized by typeParamToLookFor false other
     */
    private static boolean typeParamExists(Classifier classToSearchIn, TypeParameter typeParamToLookFor) {
        for (TypeParameter typeParam : classToSearchIn.getTypeParameters()) {
            if (typeParam.getGenericType() == null) {
                // ? type
                if (typeParamToLookFor.getGenericType() == null) {
                    return true;
                }
            } else if (typeParam.getGenericType().equals(typeParamToLookFor.getGenericType())) {
                return true;
            }
        }
        
        return false;
    }
    
    
    /**
     * Processes associations.
     * When an associationEnd has a featureSelection, the navigability needs to be set to false because the weaver will
     * create another association through an implementation class.
     * If the other end is not visible, the association can be removed.
     * @param aspect The aspect that contains the associations to be processed.
     * @param instantiation The instantiation woven into the aspect
     */
    private static void processAssociations(Aspect aspect, Instantiation instantiation) {
        
        //Only process if instantiation belongs to a model reuse of the association concern
        COREModelReuse modelReuse = EMFModelUtil.getRootContainerOfType(instantiation, 
                CorePackage.Literals.CORE_MODEL_REUSE);
        if (modelReuse != null && "Association".equals(modelReuse.getReuse().getReusedConcern().getName())) {
            
            String name = "";
            Association associationToRemove = null;
            
            // Find the class mapped to 'Data' and get the association end with the matching feature selection
            for (ClassifierMapping classifierMapping : instantiation.getMappings()) {
                if (DATA.equals(classifierMapping.getFrom().getName())) {
                    for (AssociationEnd associationEnd : classifierMapping.getTo().getAssociationEnds()) {
                        
                        if (associationEnd.getFeatureSelection() == modelReuse) {
                            name = associationEnd.getName();
                            if (associationEnd.getOppositeEnd().isNavigable()) {
                                associationEnd.setFeatureSelection(null);
                                associationEnd.setNavigable(false);
                            } else {
                                associationToRemove = associationEnd.getAssoc();
                            }
                            
                        }
                    }
                }
            }
            
            if (associationToRemove != null) {
                for (AssociationEnd associationEnd : associationToRemove.getEnds()) {
                    associationEnd.getClassifier().getAssociationEnds().remove(associationEnd);
                }
                aspect.getStructuralView().getAssociations().remove(associationToRemove);
            }

            for (Association association : aspect.getStructuralView().getAssociations()) {
                for (AssociationEnd associationEnd : association.getEnds()) {
                    if (ASSOCIATION_END_NAME_COLLECTION.equals(associationEnd.getName())
                            && ASSOCIATION_NAME_DATA_COLLECTION.equals(associationEnd.getAssoc().getName())) {
                        associationEnd.setName(name);
                    }
                }
            }
        }

    }

}
