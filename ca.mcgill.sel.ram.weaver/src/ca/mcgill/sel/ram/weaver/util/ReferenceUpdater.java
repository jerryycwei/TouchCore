package ca.mcgill.sel.ram.weaver.util;

import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import ca.mcgill.sel.ram.AbstractMessageView;
import ca.mcgill.sel.ram.AspectMessageView;
import ca.mcgill.sel.ram.AssignmentStatement;
import ca.mcgill.sel.ram.AssociationEnd;
import ca.mcgill.sel.ram.Attribute;
import ca.mcgill.sel.ram.Classifier;
import ca.mcgill.sel.ram.ClassifierMapping;
import ca.mcgill.sel.ram.Lifeline;
import ca.mcgill.sel.ram.Message;
import ca.mcgill.sel.ram.MessageView;
import ca.mcgill.sel.ram.ObjectType;
import ca.mcgill.sel.ram.Operation;
import ca.mcgill.sel.ram.OperationMapping;
import ca.mcgill.sel.ram.Parameter;
import ca.mcgill.sel.ram.ParameterValue;
import ca.mcgill.sel.ram.ParameterValueMapping;
import ca.mcgill.sel.ram.PrimitiveType;
import ca.mcgill.sel.ram.RCollection;
import ca.mcgill.sel.ram.Reference;
import ca.mcgill.sel.ram.StructuralFeature;
import ca.mcgill.sel.ram.StructuralFeatureValue;
import ca.mcgill.sel.ram.Type;
import ca.mcgill.sel.ram.TypeParameter;
import ca.mcgill.sel.ram.TypedElement;
import ca.mcgill.sel.ram.util.RamSwitch;

/**
 * Takes care of updating the references of elements of any {@link EObject} of RAM based
 * on the {@link WeavingInformation} provided by the structural view weaver.
 * This class is a singleton and it's instance can be retrieved via {@link #getInstance()}.
 * 
 * @author mschoettle
 */
public final class ReferenceUpdater {
    
    /**
     * Switch for each class that needs to be updated according to the weaving information.
     * 
     * @author mschoettle
     */
    private class Switcher extends RamSwitch<Object> {
        
        @Override
        public Object caseAbstractMessageView(AbstractMessageView abstractMessageView) {
            // Update "affected by" information ...
            // Use regular for loop, because when using enhanced for loop, a concurrent
            // modification exception will happen (due to add and remove operations).
            for (int index = 0; index < abstractMessageView.getAffectedBy().size(); index++) {
                AspectMessageView aspectMessageView = abstractMessageView.getAffectedBy().get(index);
                
                // Replace it with the new aspect message view ...
                if (currentWeavingInformation.wasWoven(aspectMessageView)) {
                    AspectMessageView newAspectMessageView =
                            currentWeavingInformation.getFirstToElement(aspectMessageView);
                    abstractMessageView.getAffectedBy().remove(index);
                    abstractMessageView.getAffectedBy().add(index, newAspectMessageView);
                }
            }
            
            return Boolean.TRUE;
        }
        
        @Override
        public Object caseMessageView(MessageView messageView) {
            Operation specifies = messageView.getSpecifies();
            
            if (currentWeavingInformation.wasWoven(specifies)) {
                // TODO: mschoettle: Can we assume there is always only 1?
                messageView.setSpecifies(currentWeavingInformation.getFirstToElement(specifies));
            }
            
            // Don't terminate to also perform general updates (AbstractMessageView).
            return null;
        }
        
        @Override
        public Object caseLifeline(Lifeline lifeline) {
            TypedElement represents = lifeline.getRepresents();
            
            if (currentWeavingInformation.wasWoven(represents)) {
                lifeline.setRepresents(currentWeavingInformation.getFirstToElement(represents));
            } else {
                // It is possible that the type needs to be updated.
                return doSwitch(represents);
            }
            
            return Boolean.TRUE;
        }
        
        @Override
        public Object caseReference(Reference reference) {
            ObjectType type = reference.getType();
            
            if (currentWeavingInformation.wasWoven(type)) {
                reference.setType(currentWeavingInformation.getFirstToElement(type));
            }
            
            return Boolean.TRUE;
        }
        
        @Override
        public Object caseAttribute(Attribute attribute) {
            // Right now this will only be called for attributes that are temporary properties of a lifeline.
            // Therefore it is enough to replace the type of the attribute.
            PrimitiveType type = attribute.getType();
            
            if (currentWeavingInformation.wasWoven(type)) {
                attribute.setType(currentWeavingInformation.getFirstToElement(type));
            }
            
            return Boolean.TRUE;
        }
        
        @Override
        public Object caseOperation(Operation operation) {
            Type type = operation.getReturnType();
            
            if (currentWeavingInformation.wasWoven(type)) {
                operation.setReturnType(currentWeavingInformation.getFirstToElement(type));
            }
            
            return Boolean.TRUE;
        }
        
        @Override
        public Object caseParameter(Parameter parameter) {
            Type type = parameter.getType();
            
            if (currentWeavingInformation.wasWoven(type)) {
                parameter.setType(currentWeavingInformation.getFirstToElement(type));
            }
            
            return Boolean.TRUE;
        }
        
        @Override
        public Object caseRCollection(RCollection collection) {
            ObjectType type = collection.getType();
            
            if (currentWeavingInformation.wasWoven(type)) {
                collection.setType(currentWeavingInformation.getFirstToElement(type));
            }
            
            return Boolean.TRUE;
        }
        
        @Override
        public Object caseTypeParameter(TypeParameter typeParam) {
            if (currentWeavingInformation.wasWoven(typeParam.getGenericType())) {
                typeParam.setGenericType(currentWeavingInformation.getFirstToElement(typeParam.getGenericType()));
            }
            return Boolean.TRUE;
        }
        
        @Override
        public Object caseClassifier(Classifier object) {
            List<Classifier> superTypes = object.getSuperTypes();
            
            for (int index = 0; index < superTypes.size(); index++) {
                Classifier classifier = superTypes.get(index);
                
                if (currentWeavingInformation.wasWoven(classifier)) {
                    Classifier newClassifier = currentWeavingInformation.getFirstToElement(classifier);
                    superTypes.remove(index);
                    if (!superTypes.contains(newClassifier)) {
                        superTypes.add(index, newClassifier);
                    }
                }
            }
            return Boolean.TRUE;
        }
        
        @Override
        public Object caseAssociationEnd(AssociationEnd assocEnd) {
            Classifier clss = assocEnd.getClassifier();
            
            if (currentWeavingInformation.wasWoven(clss)) {
                assocEnd.setClassifier(currentWeavingInformation.getFirstToElement(clss));
            }
            return Boolean.TRUE;
        }
        
        @Override
        public Object caseMessage(Message message) {
            if (currentWeavingInformation.wasWoven(message.getSignature())) {
                // It is possible that the lower-level operation was woven to several higher-level operations.
                // In such a case, we have to make sure that the messages signature are not updated.
                // This case has to be handled at another point.
                // One case is a reply message in an advice, which references the operation that was called.
                List<Operation> wovenOperations = currentWeavingInformation.getToElements(message.getSignature());
                
                // TODO: mschoettle: It might be better to create a AspectMessageView for each
                // method that is affected, i.e., not reuse an AMV for multiple operations, but rather clone them.
                if (wovenOperations.size() == 1) {
                    message.setSignature(currentWeavingInformation.getFirstToElement(message.getSignature()));
                } else {
                    System.err.println("Warning: [ReferenceUpdater] Message signature ("
                            + message.getSignature().getName()
                            + ") wasn't updated, because it was woven to more than one operation.");
                }
            }
            
            // TODO: mschoettle: Does this always work? What if assignTo is a local property (temporary property)?
            if (currentWeavingInformation.wasWoven(message.getAssignTo())) {
                message.setAssignTo(currentWeavingInformation.getFirstToElement(message.getAssignTo()));
            }
            
            // Even though events are not woven in the structural view, this method
            // can also be called during weaving of message views, where these tasks
            // might be necessary.
            if (currentWeavingInformation.wasWoven(message.getSendEvent())) {
                message.setSendEvent(currentWeavingInformation.getFirstToElement(message.getSendEvent()));
            }
            
            if (currentWeavingInformation.wasWoven(message.getReceiveEvent())) {
                message.setReceiveEvent(currentWeavingInformation.getFirstToElement(message.getReceiveEvent()));
            }
            
            return Boolean.TRUE;
        }
        
        @Override
        public Object caseParameterValueMapping(ParameterValueMapping mapping) {
            if (currentWeavingInformation.wasWoven(mapping.getParameter())) {
                mapping.setParameter(currentWeavingInformation.getFirstToElement(mapping.getParameter()));
            }
            
            return Boolean.TRUE;
        }
        
        @Override
        public Object caseStructuralFeatureValue(StructuralFeatureValue structuralFeatureValue) {
            StructuralFeature value = structuralFeatureValue.getValue();
            
            if (currentWeavingInformation.wasWoven(value)) {
                structuralFeatureValue.setValue(currentWeavingInformation.getFirstToElement(value));
            }
            
            return Boolean.TRUE;
        }
        
        @Override
        public Object caseParameterValue(ParameterValue parameterValue) {
            Parameter parameter = parameterValue.getParameter();
            
            if (currentWeavingInformation.wasWoven(parameter)) {
                parameterValue.setParameter(currentWeavingInformation.getFirstToElement(parameter));
            }
            
            return Boolean.TRUE;
        }
        
        @Override
        public Object caseAssignmentStatement(AssignmentStatement assignmentStatement) {
            StructuralFeature structuralFeature = assignmentStatement.getAssignTo();
            
            if (currentWeavingInformation.wasWoven(structuralFeature)) {
                assignmentStatement.setAssignTo(currentWeavingInformation.getFirstToElement(structuralFeature));
            }
            
            return Boolean.TRUE;
        }
        
        @Override
        public Object caseClassifierMapping(ClassifierMapping mapping) {
            Classifier toClassifier = mapping.getTo();
            
            if (currentWeavingInformation.wasWoven(toClassifier)) {
                Classifier classifier = currentWeavingInformation.getFirstToElement(toClassifier);
                mapping.setTo(classifier);
            }
            
            return Boolean.TRUE;
        }
        
        @Override
        public Object caseOperationMapping(OperationMapping mapping) {
            Operation toOperation = mapping.getTo();
            
            if (currentWeavingInformation.wasWoven(toOperation)) {
                Operation operation = currentWeavingInformation.getFirstToElement(toOperation);
                mapping.setTo(operation);
            }
            
            return Boolean.TRUE;
        }
        
    }
    
    /**
     * The singleton instance.
     */
    private static ReferenceUpdater instance;
    
    private WeavingInformation currentWeavingInformation;
    private Switcher switcher = new Switcher();
    
    /**
     * Creates a new reference updater.
     */
    private ReferenceUpdater() {
        
    }
    
    /**
     * Returns the singleton instance of the {@link ReferenceUpdater}.
     * 
     * @return the singleton instance
     */
    public static ReferenceUpdater getInstance() {
        if (instance == null) {
            instance = new ReferenceUpdater();
        }
        
        return instance;
    }
    
    /**
     * Updates an {@link EObject} based on the given {@link WeavingInformation}.
     * Also updates all objects in the hierarchy of the given object.
     * 
     * @param eObject the object to update
     * @param weavingInformation the weaving information
     */
    public void update(EObject eObject, WeavingInformation weavingInformation) {
        this.currentWeavingInformation = weavingInformation;
        
        // Update the object itself.
        switcher.doSwitch(eObject);
        
        // Then update the whole containment hierarchy of the object.
        TreeIterator<EObject> iterator = EcoreUtil.getAllContents(eObject, true);
        
        while (iterator.hasNext()) {
            update(iterator.next(), weavingInformation);
        }
    }
    
    /**
     * Updates a list of {@link EObject} based on the given {@link WeavingInformation}.
     * Also updates all objects in the hierarchy of the given object.
     * 
     * @param eObjects the list of objects to update
     * @param weavingInformation the weaving information
     * @see #update(EObject, WeavingInformation)
     */
    public void update(List<? extends EObject> eObjects, WeavingInformation weavingInformation) {
        for (EObject eObject : eObjects) {
            update(eObject, weavingInformation);
        }
    }
    
}
