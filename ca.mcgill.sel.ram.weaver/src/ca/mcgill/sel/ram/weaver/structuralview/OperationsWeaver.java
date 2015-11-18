/**
 * <hr>
 * OperationsWeaver.java Created by @author walabe on Mar 16, 2013
 * @version 3.0
 * @since 3.0 i.e version of the project when this was created
 * <hr>
 * @copyright
 */
package ca.mcgill.sel.ram.weaver.structuralview;

import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;

import ca.mcgill.sel.core.COREVisibilityType;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.Classifier;
import ca.mcgill.sel.ram.ImplementationClass;
import ca.mcgill.sel.ram.Operation;
import ca.mcgill.sel.ram.Parameter;
import ca.mcgill.sel.ram.RAMVisibilityType;
import ca.mcgill.sel.ram.weaver.util.WeavingInformation;

/**
 * This class represents our Operations Weaver.
 * 
 * @author walabe
 *
 */
public final class OperationsWeaver {

    /**
     * Our Private Constructor since this class only contains static methods
     * and has no state there is no need to instantiate an object of type OperationsWeaver.
     */
    private OperationsWeaver() {

    }

    /**
     * This is the main method that perform weaving of operations.
     * 
     * @param classFromHigherLevel The class that classFromLowerLevel is being woven into
     * @param classFromLowerLevel The class that will contain the operations we want to weave into classFromHigherLevel
     * @param higherLevelAspect The aspect that classFromHigherLevel belongs to
     * @param isExtends Represents the type of weaving extends or depends
     * @param weavingInformation The data structure containing all mapping information
     */
    public static void weaveOperations(final Classifier classFromHigherLevel, final Classifier classFromLowerLevel,
            final Aspect higherLevelAspect, final boolean isExtends, final WeavingInformation weavingInformation) {

        for (final Operation opFromLowerLevel : classFromLowerLevel.getOperations()) {
            boolean opMapped = false;
            // First check to see if this operation was mapped.
            // See issue #112.
            List<Operation> toElements = weavingInformation.getToElements(opFromLowerLevel);
            if (toElements != null) {
                for (final Operation key : toElements) {
                    if (key.eContainer().equals(classFromHigherLevel)) {
                        // Operation was mapped by user, operation already exists in weaving information,
                        // but not it's parameters, therefore, create weaving information for parameters
                        createParameterWeavingInformation(opFromLowerLevel, key, weavingInformation);
                        opMapped = true;
                        break;
                    }
                }
            }

            if (!opMapped) {
                final Operation opFromHigherLevel = getExistingOperation(classFromHigherLevel, opFromLowerLevel,
                        weavingInformation);
                if (opFromHigherLevel != null) {
                    weavingInformation.add(opFromLowerLevel, opFromHigherLevel);
                    createParameterWeavingInformation(opFromLowerLevel, opFromHigherLevel, weavingInformation);
                } else {
                    // TODO check parameters
                    Operation opCopy = EcoreUtil.copy(opFromLowerLevel);

                    if (!isExtends && !(classFromHigherLevel instanceof ImplementationClass)
                            && opFromLowerLevel.getExtendedVisibility().equals(RAMVisibilityType.PUBLIC)) {
                        opCopy.setVisibility(COREVisibilityType.CONCERN);
                        opCopy.setExtendedVisibility(RAMVisibilityType.PACKAGE);
                    } else {
                        opCopy.setVisibility(opFromLowerLevel.getVisibility());
                        opCopy.setExtendedVisibility(opFromLowerLevel.getExtendedVisibility());
                    }

                    classFromHigherLevel.getOperations().add(opCopy);
                    weavingInformation.add(opFromLowerLevel, opCopy);

                    createParameterWeavingInformation(opFromLowerLevel, opCopy, weavingInformation);
                }
            }
        }
    }

    /**
     * Operations are sometimes just created to be checked
     * in createOpCopyInHigherLevel, but never added. Therefore, parameters
     * are "mapped" only when in certain circumstances (e.g., operation is
     * actually added to a class).
     * 
     * @param original The original operation
     * @param copy The copy of original
     * @param weavingInformation The data structure containing all mapping information
     */
    public static void createParameterWeavingInformation(Operation original, Operation copy,
            WeavingInformation weavingInformation) {
        for (int index = 0; index < original.getParameters().size(); index++) {
            Parameter oldParameter = original.getParameters().get(index);
            Parameter newParameter = copy.getParameters().get(index);
            weavingInformation.add(oldParameter, newParameter);
        }
    }

    /**
     * This method will search in parentClass for an operation that matches
     * opToLookFor by signature.
     * 
     * @param classFromHigherLevel The class to search in.
     * @param operationFromLowerLevel The operation from lower level class to look for in classFromHigherLevel.
     * @param weavingInformation The data structure containing all mapping information.
     * @return the operation in the higher level class matching operation from lower level class
     *         or null if there is no match.
     */
    public static Operation getExistingOperation(Classifier classFromHigherLevel, Operation operationFromLowerLevel,
            WeavingInformation weavingInformation) {
        for (final Operation operationFromHigherLevel : classFromHigherLevel.getOperations()) {
            if (operationFromHigherLevel.getName().equals(operationFromLowerLevel.getName())) {
                boolean match = false;
                if (weavingInformation.wasWoven(operationFromLowerLevel.getReturnType())) {
                    match = weavingInformation.getFirstToElement(operationFromLowerLevel.getReturnType()).equals(
                            operationFromHigherLevel.getReturnType());
                }
                match = match
                        || operationFromLowerLevel.getReturnType().equals(operationFromHigherLevel.getReturnType());

                if (match
                        && operationFromHigherLevel.getParameters().size() == operationFromLowerLevel.getParameters()
                                .size()
                        && sameParameters(operationFromHigherLevel, operationFromLowerLevel, weavingInformation)) {
                    return operationFromHigherLevel;
                }
            }
        }
        return null;
    }

    /**
     * This method will compare the parameters of two operations to
     * see if they match. For a match to occur the two operations must
     * have the same number of parameters in the same order and for
     * each parameter must have the same name and same type.
     * 
     * @param operationFromHigherLevel The operation from higher level class
     * @param operationFromLowerLevel The operation from lower level class
     * @param weavingInformation The data structure containing all mapping information
     * @return true if the parameters match or false otherwise
     */
    private static boolean sameParameters(final Operation operationFromHigherLevel,
            final Operation operationFromLowerLevel, WeavingInformation weavingInformation) {
        boolean same = false;
        if (operationFromHigherLevel.getParameters().size() == operationFromLowerLevel.getParameters().size()) {
            same = true;
            for (int index = 0; index < operationFromHigherLevel.getParameters().size(); index++) {
                final Parameter paramFromHigherLevel = operationFromHigherLevel.getParameters().get(index);
                final Parameter paramFromLowerLevel = operationFromLowerLevel.getParameters().get(index);
                if (weavingInformation.wasWoven(paramFromLowerLevel)) {
                    same = weavingInformation.getFirstToElement(paramFromLowerLevel).equals(paramFromHigherLevel);
                }

                if (weavingInformation.wasWoven(paramFromLowerLevel.getType())) {
                    same = same
                            && (weavingInformation.getFirstToElement(paramFromLowerLevel.getType()).equals(
                                    paramFromHigherLevel.getType()) || paramFromLowerLevel.getType().equals(
                                    paramFromHigherLevel.getType()));
                } else {
                    same = same && paramFromLowerLevel.getType().equals(paramFromHigherLevel.getType());
                }

                // TODO check if there is a mapping either names have to be the same or there is a mapping
            }
        }
        return same;
    }
}
