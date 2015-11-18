/**
 */
package ca.mcgill.sel.ucm.util;

import ca.mcgill.sel.core.COREModel;
import ca.mcgill.sel.core.COREModelElement;
import ca.mcgill.sel.core.CORENamedElement;

import ca.mcgill.sel.ucm.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see ca.mcgill.sel.ucm.UCMPackage
 * @generated
 */
public class UCMSwitch<T> extends Switch<T> {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static UCMPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UCMSwitch() {
        if (modelPackage == null) {
            modelPackage = UCMPackage.eINSTANCE;
        }
    }

    /**
     * Checks whether this is a switch for the given package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @parameter ePackage the package in question.
     * @return whether this is a switch for the given package.
     * @generated
     */
    @Override
    protected boolean isSwitchFor(EPackage ePackage) {
        return ePackage == modelPackage;
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    @Override
    protected T doSwitch(int classifierID, EObject theEObject) {
        switch (classifierID) {
            case UCMPackage.AND_FORK: {
                AndFork andFork = (AndFork)theEObject;
                T result = caseAndFork(andFork);
                if (result == null) result = casePathNode(andFork);
                if (result == null) result = caseUCMModelElement(andFork);
                if (result == null) result = caseCOREModelElement(andFork);
                if (result == null) result = caseCORENamedElement(andFork);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case UCMPackage.AND_JOIN: {
                AndJoin andJoin = (AndJoin)theEObject;
                T result = caseAndJoin(andJoin);
                if (result == null) result = casePathNode(andJoin);
                if (result == null) result = caseUCMModelElement(andJoin);
                if (result == null) result = caseCOREModelElement(andJoin);
                if (result == null) result = caseCORENamedElement(andJoin);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case UCMPackage.OR_FORK: {
                OrFork orFork = (OrFork)theEObject;
                T result = caseOrFork(orFork);
                if (result == null) result = casePathNode(orFork);
                if (result == null) result = caseUCMModelElement(orFork);
                if (result == null) result = caseCOREModelElement(orFork);
                if (result == null) result = caseCORENamedElement(orFork);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case UCMPackage.OR_JOIN: {
                OrJoin orJoin = (OrJoin)theEObject;
                T result = caseOrJoin(orJoin);
                if (result == null) result = casePathNode(orJoin);
                if (result == null) result = caseUCMModelElement(orJoin);
                if (result == null) result = caseCOREModelElement(orJoin);
                if (result == null) result = caseCORENamedElement(orJoin);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case UCMPackage.EMPTY_POINT: {
                EmptyPoint emptyPoint = (EmptyPoint)theEObject;
                T result = caseEmptyPoint(emptyPoint);
                if (result == null) result = casePathNode(emptyPoint);
                if (result == null) result = caseUCMModelElement(emptyPoint);
                if (result == null) result = caseCOREModelElement(emptyPoint);
                if (result == null) result = caseCORENamedElement(emptyPoint);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case UCMPackage.PATH_NODE: {
                PathNode pathNode = (PathNode)theEObject;
                T result = casePathNode(pathNode);
                if (result == null) result = caseUCMModelElement(pathNode);
                if (result == null) result = caseCOREModelElement(pathNode);
                if (result == null) result = caseCORENamedElement(pathNode);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case UCMPackage.RESPONSIBILITY_REF: {
                ResponsibilityRef responsibilityRef = (ResponsibilityRef)theEObject;
                T result = caseResponsibilityRef(responsibilityRef);
                if (result == null) result = casePathNode(responsibilityRef);
                if (result == null) result = caseUCMModelElement(responsibilityRef);
                if (result == null) result = caseCOREModelElement(responsibilityRef);
                if (result == null) result = caseCORENamedElement(responsibilityRef);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case UCMPackage.RESPONSIBILITY: {
                Responsibility responsibility = (Responsibility)theEObject;
                T result = caseResponsibility(responsibility);
                if (result == null) result = caseUCMModelElement(responsibility);
                if (result == null) result = caseCOREModelElement(responsibility);
                if (result == null) result = caseCORENamedElement(responsibility);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case UCMPackage.START_POINT: {
                StartPoint startPoint = (StartPoint)theEObject;
                T result = caseStartPoint(startPoint);
                if (result == null) result = casePathNode(startPoint);
                if (result == null) result = caseUCMModelElement(startPoint);
                if (result == null) result = caseCOREModelElement(startPoint);
                if (result == null) result = caseCORENamedElement(startPoint);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case UCMPackage.NODE_CONNECTION: {
                NodeConnection nodeConnection = (NodeConnection)theEObject;
                T result = caseNodeConnection(nodeConnection);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case UCMPackage.UCM_MODEL_ELEMENT: {
                UCMModelElement ucmModelElement = (UCMModelElement)theEObject;
                T result = caseUCMModelElement(ucmModelElement);
                if (result == null) result = caseCOREModelElement(ucmModelElement);
                if (result == null) result = caseCORENamedElement(ucmModelElement);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case UCMPackage.USE_CASE_MAP: {
                UseCaseMap useCaseMap = (UseCaseMap)theEObject;
                T result = caseUseCaseMap(useCaseMap);
                if (result == null) result = caseCOREModel(useCaseMap);
                if (result == null) result = caseCORENamedElement(useCaseMap);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case UCMPackage.COMPONENT_REFERENCE: {
                ComponentReference componentReference = (ComponentReference)theEObject;
                T result = caseComponentReference(componentReference);
                if (result == null) result = caseUCMModelElement(componentReference);
                if (result == null) result = caseCOREModelElement(componentReference);
                if (result == null) result = caseCORENamedElement(componentReference);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case UCMPackage.COMPONENT: {
                Component component = (Component)theEObject;
                T result = caseComponent(component);
                if (result == null) result = caseUCMModelElement(component);
                if (result == null) result = caseCOREModelElement(component);
                if (result == null) result = caseCORENamedElement(component);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case UCMPackage.CONDITION: {
                Condition condition = (Condition)theEObject;
                T result = caseCondition(condition);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case UCMPackage.END_POINT: {
                EndPoint endPoint = (EndPoint)theEObject;
                T result = caseEndPoint(endPoint);
                if (result == null) result = casePathNode(endPoint);
                if (result == null) result = caseUCMModelElement(endPoint);
                if (result == null) result = caseCOREModelElement(endPoint);
                if (result == null) result = caseCORENamedElement(endPoint);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>And Fork</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>And Fork</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAndFork(AndFork object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>And Join</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>And Join</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAndJoin(AndJoin object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Or Fork</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Or Fork</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOrFork(OrFork object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Or Join</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Or Join</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseOrJoin(OrJoin object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Empty Point</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Empty Point</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEmptyPoint(EmptyPoint object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Path Node</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Path Node</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePathNode(PathNode object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Responsibility Ref</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Responsibility Ref</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseResponsibilityRef(ResponsibilityRef object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Responsibility</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Responsibility</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseResponsibility(Responsibility object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Start Point</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Start Point</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseStartPoint(StartPoint object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Node Connection</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Node Connection</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseNodeConnection(NodeConnection object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Model Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Model Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUCMModelElement(UCMModelElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Use Case Map</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Use Case Map</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUseCaseMap(UseCaseMap object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Component Reference</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Component Reference</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseComponentReference(ComponentReference object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Component</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Component</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseComponent(Component object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Condition</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Condition</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCondition(Condition object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>End Point</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>End Point</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseEndPoint(EndPoint object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>CORE Named Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>CORE Named Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCORENamedElement(CORENamedElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>CORE Model Element</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>CORE Model Element</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCOREModelElement(COREModelElement object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>CORE Model</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>CORE Model</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseCOREModel(COREModel object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    @Override
    public T defaultCase(EObject object) {
        return null;
    }

} //UCMSwitch
