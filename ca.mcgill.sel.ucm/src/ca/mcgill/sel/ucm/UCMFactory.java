/**
 */
package ca.mcgill.sel.ucm;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see ca.mcgill.sel.ucm.UCMPackage
 * @generated
 */
public interface UCMFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    UCMFactory eINSTANCE = ca.mcgill.sel.ucm.impl.UCMFactoryImpl.init();

    /**
     * Returns a new object of class '<em>And Fork</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>And Fork</em>'.
     * @generated
     */
    AndFork createAndFork();

    /**
     * Returns a new object of class '<em>And Join</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>And Join</em>'.
     * @generated
     */
    AndJoin createAndJoin();

    /**
     * Returns a new object of class '<em>Or Fork</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Or Fork</em>'.
     * @generated
     */
    OrFork createOrFork();

    /**
     * Returns a new object of class '<em>Or Join</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Or Join</em>'.
     * @generated
     */
    OrJoin createOrJoin();

    /**
     * Returns a new object of class '<em>Empty Point</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Empty Point</em>'.
     * @generated
     */
    EmptyPoint createEmptyPoint();

    /**
     * Returns a new object of class '<em>Path Node</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Path Node</em>'.
     * @generated
     */
    PathNode createPathNode();

    /**
     * Returns a new object of class '<em>Responsibility Ref</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Responsibility Ref</em>'.
     * @generated
     */
    ResponsibilityRef createResponsibilityRef();

    /**
     * Returns a new object of class '<em>Responsibility</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Responsibility</em>'.
     * @generated
     */
    Responsibility createResponsibility();

    /**
     * Returns a new object of class '<em>Start Point</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Start Point</em>'.
     * @generated
     */
    StartPoint createStartPoint();

    /**
     * Returns a new object of class '<em>Node Connection</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Node Connection</em>'.
     * @generated
     */
    NodeConnection createNodeConnection();

    /**
     * Returns a new object of class '<em>Model Element</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Model Element</em>'.
     * @generated
     */
    UCMModelElement createUCMModelElement();

    /**
     * Returns a new object of class '<em>Use Case Map</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Use Case Map</em>'.
     * @generated
     */
    UseCaseMap createUseCaseMap();

    /**
     * Returns a new object of class '<em>Component Reference</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Component Reference</em>'.
     * @generated
     */
    ComponentReference createComponentReference();

    /**
     * Returns a new object of class '<em>Component</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Component</em>'.
     * @generated
     */
    Component createComponent();

    /**
     * Returns a new object of class '<em>Condition</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Condition</em>'.
     * @generated
     */
    Condition createCondition();

    /**
     * Returns a new object of class '<em>End Point</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>End Point</em>'.
     * @generated
     */
    EndPoint createEndPoint();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    UCMPackage getUCMPackage();

} //UCMFactory
