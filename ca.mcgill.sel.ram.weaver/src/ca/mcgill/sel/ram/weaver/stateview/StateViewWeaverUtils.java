/**
 * <hr>
 * StateViewWeaverUtils.java Created by @author abirayed on Mar 24, 2013
 * @version 3.0
 * @since 3.0 i.e version of the project when this was created
 * <hr>
 * @copyright
 */
package ca.mcgill.sel.ram.weaver.stateview;

import java.util.List;

import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.Classifier;
import ca.mcgill.sel.ram.Constraint;
import ca.mcgill.sel.ram.LiteralString;
import ca.mcgill.sel.ram.Operation;
import ca.mcgill.sel.ram.RamFactory;
import ca.mcgill.sel.ram.State;
import ca.mcgill.sel.ram.StateMachine;
import ca.mcgill.sel.ram.StateView;
import ca.mcgill.sel.ram.Transition;
import ca.mcgill.sel.ram.weaver.util.WeavingInformation;

public final class StateViewWeaverUtils {
    
    /**
     * Creates a new instance.
     */
    private StateViewWeaverUtils() {
        
    }
    
    /**
     * This operation:
     * copies the state views from the dependee aspect to the base aspect
     * updates operation's reference in the transitions of the copied state machines and base state
     * machines.
     */
    public static void copyStateViews(final Aspect base, final Aspect dependee,
            final WeavingInformation weavingInformation) {
        
        // Update transition references in base state views
        for (final StateView ramStateView : base.getStateViews()) {
            for (final StateMachine ramStateMachine : ramStateView.getStateMachines()) {
                for (final Transition transition : ramStateMachine.getTransitions()) {
                    if (weavingInformation.wasWoven(transition.getSignature())) {
                        // TODO check if there can be a case where there is more than one woven object
                        transition.setSignature(weavingInformation.getFirstToElement(transition.getSignature()));
                        transition.setName(getTransitionName(transition));
                    }
                }
            }
        }
        
        // copy all message views from the lower-level aspect to the higher-level aspect
        for (final StateView dependeeStateView : dependee.getStateViews()) {
            // 1- get the corresponding stateView in the higherLevel aspect (if it exists)
            final List<Classifier> baseClasses = weavingInformation.getToElements(dependeeStateView.getSpecifies());
            if (baseClasses != null) {
                for (Classifier baseClass : baseClasses) {
                    // get the stateView that specifies the baseClass
                    final StateView baseStateView = classSpecifiedBy(base, baseClass);
                    if (baseStateView == null) {
                        // there is no corresponding stateView in base
                        // 2- copy the whole dependeeStateView and add it to the base aspect
                        base.getStateViews().add(copyStateView(baseClass, dependeeStateView, weavingInformation));
                        
                    } else {
                        // 2- copy stateMachines from the lower-level aspect to the higher-level aspect
                        for (final StateMachine dependeeMachine : dependeeStateView.getStateMachines()) {
                            baseStateView.getStateMachines().add(
                                    copyStateMachine(baseClass, dependeeMachine, weavingInformation));
                        }
                        
                    }
                }
            } else {
                System.err
                .println("Error: [StateViewWeaver] Specifies of dependee state view not found in weaving information.");
            }
            
        }
        
    }
    
    /**
     * This method creates a copy of the given stateView (dependee) with updated operation references
     * (in the base aspect).
     * 
     * @param classifier the classifier for which we need to add the copied state view
     * @param stateViewToCopy The state view to be copied
     */
    private static StateView copyStateView(final Classifier classifier, final StateView stateViewToCopy,
            final WeavingInformation weavingInformation) {
        final StateView copiedStateView = RamFactory.eINSTANCE.createStateView();
        copiedStateView.setName(stateViewToCopy.getName());
        copiedStateView.setSpecifies(classifier);
        for (final StateMachine stateMachine : stateViewToCopy.getStateMachines()) {
            copiedStateView.getStateMachines().add(copyStateMachine(classifier, stateMachine, weavingInformation));
        }
        return copiedStateView;
    }
    
    /**
     * This method creates a copy of the given stateMachine (dependee) with updated operation references
     * (in the base aspect).
     * 
     * @param classifier the classifier for which we need to add the copied state machine
     * @param stateMachineToCopy The state machine to be copied
     */
    private static StateMachine copyStateMachine(final Classifier classifier, final StateMachine stateMachineToCopy,
            final WeavingInformation weavingInformation) {
        final StateMachine copiedStateMachine = RamFactory.eINSTANCE.createStateMachine();
        for (final State state : stateMachineToCopy.getStates()) {
            copiedStateMachine.getStates().add(copyState(state));
        }
        
        for (final Transition transition : stateMachineToCopy.getTransitions()) {
            copyTransition(classifier, copiedStateMachine, transition, weavingInformation);
        }
        
        // set start state
        for (final State state : copiedStateMachine.getStates()) {
            if (state.getName().equals(stateMachineToCopy.getStart().getName())) {
                copiedStateMachine.setStart(state);
                break;
            }
        }
        return copiedStateMachine;
    }
    
    /**
     * This method creates a copy of the given state.
     * 
     * @param stateToCopy The state to be copied
     */
    private static State copyState(final State stateToCopy) {
        final State copiedState = RamFactory.eINSTANCE.createState();
        copiedState.setName(stateToCopy.getName());
        
        return copiedState;
    }
    
    /**
     * This method creates a copy of the given transition (dependee) with updated operation references
     * (in the base aspect).
     * 
     * @param classifier the classifier for which we need to add the copied transition
     * @param copiedStateMachine The state machine for which we need to add the copied transition
     * @param transitionToCopy The transition to be copied
     */
    private static void copyTransition(final Classifier classifier, final StateMachine copiedStateMachine,
            final Transition transitionToCopy,
            final WeavingInformation weavingInformation) {
        // update Reference in the same time
        // if there is more than one woven operation, create for each operation a transition
        final List<Operation> wovenOpList = weavingInformation.getToElements(transitionToCopy.getSignature());
        
        /*
         * Might be null, if not woven (e.g., it is coming from an extended aspect),
         * in that case the reference is not updated.
         * See issue #260.
         */
        if (wovenOpList != null) {
            for (Operation operation : wovenOpList) {
                // check first if the operations belongs to the same class (baseClass)
                // This won't be the case when a dpendeeClass is mapped to two different baseClasses
                if (classifier.getOperations().contains(operation)) {
                    Transition copiedTransition = createTransitionCopy(transitionToCopy, copiedStateMachine, operation);
                    copiedStateMachine.getTransitions().add(copiedTransition);
                }
            }
        } else {
            Transition copiedTransition = createTransitionCopy(transitionToCopy, copiedStateMachine,
                    transitionToCopy.getSignature());
            copiedStateMachine.getTransitions().add(copiedTransition);
        }
    }
    
    private static Transition createTransitionCopy(Transition transitionToCopy, StateMachine stateMachine,
            Operation operation) {
        Transition copiedTransition = RamFactory.eINSTANCE.createTransition();
        copiedTransition.setName(transitionToCopy.getName());
        // Guard
        Constraint guardToCopy = transitionToCopy.getGuard();
        Constraint newGuard = null;
        LiteralString specification = null;
        if (guardToCopy != null) {
            newGuard = RamFactory.eINSTANCE.createConstraint();
            LiteralString speciToCopy = getLiteralStringSpecificationFromGuard(guardToCopy);
            if (speciToCopy != null) {
                specification = RamFactory.eINSTANCE.createLiteralString();
                specification.setValue(speciToCopy.getValue());
                newGuard.setSpecification(specification);
            }
        }
        copiedTransition.setGuard(newGuard);
        copiedTransition.setSignature(operation);
        copiedTransition.setName(getTransitionName(copiedTransition));
        // set end state
        for (final State state : stateMachine.getStates()) {
            if (state.getName().equals(transitionToCopy.getEndState().getName())) {
                copiedTransition.setEndState(state);
                break;
            }
        }
        // set start state
        for (final State state : stateMachine.getStates()) {
            if (state.getName().equals(transitionToCopy.getStartState().getName())) {
                copiedTransition.setStartState(state);
                break;
            }
        }
        return copiedTransition;
    }
    
    /**
     * Giving the aspect and the class thus method returns the state view that specifies the class.
     */
    private static StateView classSpecifiedBy(final Aspect aspect, final Classifier ramClass) {
        
        StateView stateView = null;
        for (final StateView ramStateView : aspect.getStateViews()) {
            if (ramStateView.getSpecifies().getName().equals(ramClass.getName())) {
                stateView = ramStateView;
            }
            
        }
        return stateView;
    }
    
    private static LiteralString getLiteralStringSpecificationFromGuard(Constraint guard) {
        
        if (guard != null && guard.getSpecification() instanceof LiteralString) {
            return (LiteralString) guard.getSpecification();
        }
        return null;
    }
    
    private static String getTransitionName(Transition transition) {
        
        String transitionName = "";
        if (transition.getSignature() != null) {
            transitionName += transition.getSignature().getName();
        }
        LiteralString specification = getLiteralStringSpecificationFromGuard(transition.getGuard());
        if (specification != null) {
            transitionName += "[" + specification.getValue() + "]";
        }
        
        return transitionName;
    }
}
