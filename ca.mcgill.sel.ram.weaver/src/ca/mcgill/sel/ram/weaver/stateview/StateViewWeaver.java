/**
 * <hr>
 * StateViewWeaver.java Created by @author abirayed on Mar 24, 2013
 * @version 3.0
 * @since 3.0 i.e version of the project when this was created
 * <hr>
 * @copyright
 */
package ca.mcgill.sel.ram.weaver.stateview;

import java.util.ArrayList;

import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.Constraint;
import ca.mcgill.sel.ram.LiteralString;
import ca.mcgill.sel.ram.Operation;
import ca.mcgill.sel.ram.RamFactory;
import ca.mcgill.sel.ram.State;
import ca.mcgill.sel.ram.StateMachine;
import ca.mcgill.sel.ram.StateView;
import ca.mcgill.sel.ram.Transition;

public class StateViewWeaver {

    private class StateEquivalence {
        private final State wovenState;

        private final State stateOfMachine1;

        private final State stateOfMachine2;

        public StateEquivalence(final State wovenState, final State stateOfMachine1, final State stateOfMachine2) {
            this.wovenState = wovenState;
            this.stateOfMachine1 = stateOfMachine1;
            this.stateOfMachine2 = stateOfMachine2;
        }

        public State getWovenState() {
            return wovenState;
        }

        public State getStateOfMachine1() {
            return stateOfMachine1;
        }

        public State getStateOfMachine2() {
            return stateOfMachine2;
        }

    }

    private ArrayList<StateEquivalence> statesEquivalence;

    private ArrayList<State> statesToProcess;

    /**
     * Weave the state machines inside each state view of the given aspect.
     * 
     * @param base the aspect for which the state views will be weaved
     */
    public void weaveStateViews(final Aspect base) {
        for (final StateView ramStateView : base.getStateViews()) {
            // Weave stateMachines inside the state View
            weaveMachinesInsideStateView(ramStateView);
        }
    }

    /**
     * Weave the state machines inside the given state view.
     *
     * @param stateView the state view for which the state machines will be weaved
     */
    private void weaveMachinesInsideStateView(final StateView stateView) {
        StateMachine stateMachine = stateView.getStateMachines().get(0);
        for (final StateMachine sm : stateView.getStateMachines()) {
            if (sm != stateMachine) {
                stateMachine = weaveStateMachines(stateMachine, sm);
            }
        }
        stateView.getStateMachines().clear();
        stateView.getStateMachines().add(stateMachine);
    }

    /**
     * Weave two state machines.
     * 
     * @param stateMachine1
     * @param stateMachine2
     * @return The resulted woven machine
     */
    private StateMachine weaveStateMachines(final StateMachine stateMachine1, final StateMachine stateMachine2) {

        final StateMachine wovenStateMachine = RamFactory.eINSTANCE.createStateMachine();

        statesEquivalence = new ArrayList<StateEquivalence>();
        statesToProcess = new ArrayList<State>();

        // Add start state to the woven Machine
        final State startState = RamFactory.eINSTANCE.createState();
        startState.setName("Start");
        wovenStateMachine.setStart(startState);
        wovenStateMachine.getStates().add(startState);

        // each state in the woven stateMachine is mapped to 2 states, one from stateMachine1 and one from stateMachine2
        statesEquivalence.add(new StateEquivalence(startState, stateMachine1.getStart(), stateMachine2.getStart()));
        statesToProcess.add(startState);

        int statesLeftToProcess = statesToProcess.size();
        while (statesLeftToProcess > 0) {
            final State stateToProcess = statesToProcess.get(0);
            final StateEquivalence equivalence = getEquivalence(stateToProcess);
            processOutgoingTransitions(equivalence, wovenStateMachine, stateMachine1, stateMachine2, 0);
            processOutgoingTransitions(equivalence, wovenStateMachine, stateMachine2, stateMachine1, 1);
            statesToProcess.remove(stateToProcess);
            statesLeftToProcess = statesToProcess.size();
        }

        return wovenStateMachine;
    }

    private void processOutgoingTransitions(final StateEquivalence equivalence, final StateMachine wovenStateMachine,
            final StateMachine machineA, final StateMachine machineB, final int index) {

        State stateA = equivalence.getStateOfMachine1();
        State stateB = equivalence.getStateOfMachine2();
        if (index != 0) {
            stateA = equivalence.getStateOfMachine2();
            stateB = equivalence.getStateOfMachine1();
        }

        for (final Transition transitionA : stateA.getOutgoings()) {
            // if the transition doesn't already exist in the wovenState outgoing transitions
            if (getTransitionFromStateOutgoings(equivalence.getWovenState(), transitionA) == null) {
                final Transition transitionB = getTransitionFromStateOutgoings(stateB, transitionA);
                State stateToBeCombined = stateB;
                boolean canAccept = false;

                if (transitionB != null) {
                    // if the transition is in the outgoings of the other mapped state (stateB) of the other machine
                    // (machineB)
                    stateToBeCombined = transitionB.getEndState();
                    canAccept = true;
                } else if (getTransitionFromMachineTransitions(machineB, stateB, transitionA) == null) {
                    // Should not be in any other state of machineB
                    canAccept = true;
                }

                if (canAccept) {

                    final State existingState = getEquivalentStateInWovenMachine(transitionA.getEndState(),
                            stateToBeCombined, index);
                    addStateAndTransitionToStateMachine(wovenStateMachine, transitionA, stateToBeCombined,
                            existingState, index);
                }
            }
        }
    }

    private void addStateAndTransitionToStateMachine(final StateMachine wovenStateMachine, final Transition transition,
            final State otherState, final State existingWMState, final int stateIndex) {

        final State currentProcessedWMState = statesToProcess.get(0);

        final Transition transitionToAdd = RamFactory.eINSTANCE.createTransition();
        transitionToAdd.setSignature(transition.getSignature());

        // Guard
        Constraint guardToCopy = transition.getGuard();
        Constraint newGuard = null;
        if (guardToCopy != null) {
            newGuard = RamFactory.eINSTANCE.createConstraint();
            if (guardToCopy.getSpecification() instanceof LiteralString) {
                LiteralString specification = RamFactory.eINSTANCE.createLiteralString();
                specification.setValue(((LiteralString) guardToCopy.getSpecification()).getValue());
                newGuard.setSpecification(specification);
            }
        }
        transitionToAdd.setGuard(newGuard);
        transitionToAdd.setStartState(currentProcessedWMState);
        // **
        transitionToAdd.setName(transition.getName());

        wovenStateMachine.getTransitions().add(transitionToAdd);

        if (existingWMState == null) {

            final State combinedState = RamFactory.eINSTANCE.createState();
            if (transition.getEndState().getName().equals(otherState.getName())) {
                combinedState.setName(transition.getEndState().getName());
            } else if (stateIndex == 0) {
                combinedState.setName(combineStrings(transition.getEndState().getName(), otherState.getName()));
            } else {
                combinedState.setName(combineStrings(otherState.getName(), transition.getEndState().getName()));
            }

            transitionToAdd.setEndState(combinedState);
            wovenStateMachine.getStates().add(combinedState);

            // Add combinedState to the mapping
            if (stateIndex == 0) {
                statesEquivalence.add(new StateEquivalence(combinedState, transition.getEndState(), otherState));
            } else {
                statesEquivalence.add(new StateEquivalence(combinedState, otherState, transition.getEndState()));
            }

            statesToProcess.add(combinedState);

        } else {

            transitionToAdd.setEndState(existingWMState);
        }
    }

    private String combineStrings(String str, final String strSub) {
        final String[] seq1 = strSub.split("_");
        for (int i = 0; i < seq1.length; i++) {
            if (!str.contains(seq1[i])) {
                str = str + "_" + seq1[i];
            }
        }
        return str;
    }

    private State getEquivalentStateInWovenMachine(final State state1, final State state2, final int index) {
        State stateA = state1;
        State stateB = state2;
        if (index == 1) {
            stateA = state2;
            stateB = state1;
        }

        for (final StateEquivalence equivalence : statesEquivalence) {
            if (equivalence.getStateOfMachine1().equals(stateA) && equivalence.getStateOfMachine2().equals(stateB)) {
                return equivalence.getWovenState();
            }
        }

        return null;
    }

    private Transition getTransitionFromStateOutgoings(final State wovenState, final Transition transition) {
        for (final Transition tr : wovenState.getOutgoings()) {
            if (sameSignature(transition.getSignature(), tr.getSignature())) {
                if (sameGuard(transition.getGuard(), tr.getGuard())) {
                    return tr;
                } else if (transition.getGuard() != null && tr.getGuard() == null) {
                    return tr;
                }
            }
        }
        return null;
    }

    private Transition getTransitionFromMachineTransitions(final StateMachine stateMachine, State exceptState,
            final Transition transition) {
        for (final Transition tr : stateMachine.getTransitions()) {
            if (!tr.getStartState().equals(exceptState)
                    && sameSignature(transition.getSignature(), tr.getSignature())) {

                if (transition.getSignature() == null) {
                    if (sameGuard(transition.getGuard(), tr.getGuard())) {
                        return tr;
                    }
                } else {
                    // if the operation is not null than we don't have to check the guard
                    // This means that this machine doesn't allow the call of this operation in the current state
                    return tr;
                }
            }
        }
        return null;
    }

    private boolean sameGuard(Constraint guard1, Constraint guard2) {
        if (guard1 != null
                && guard2 != null
                && ((LiteralString) guard1.getSpecification()).getValue().equals(
                        ((LiteralString) guard2.getSpecification()).getValue())) {
            return true;
        } else if (guard1 == guard2) {
            return true;
        }
        return false;
    }

    private boolean sameSignature(Operation operation1, Operation operation2) {
        if (operation1 != null && operation2 != null && operation2.equals(operation1)) {
            return true;
        } else if (operation1 == operation2) {
            return true;
        }
        return false;
    }

    private StateEquivalence getEquivalence(final State state) {
        for (final StateEquivalence equivalence : statesEquivalence) {
            if (equivalence.getWovenState().equals(state)) {
                return equivalence;
            }
        }
        return null;
    }

    // private void checkTransitionSubstitution(StateMachine stateMachine, State state, Transition transition)
    // {
    // List<Substitution> transitionSubstitutions= stateMachine.getSubstitutions();
    // for (Substitution sub : transitionSubstitutions){
    // if(((TransitionSubstitution)sub).getFrom().equal
    // }
    // }
}
