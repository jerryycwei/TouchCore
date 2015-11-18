/**
 */
package ca.mcgill.sel.ucm;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link ca.mcgill.sel.ucm.NodeConnection#getCondition <em>Condition</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.NodeConnection#getSource <em>Source</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.NodeConnection#getTarget <em>Target</em>}</li>
 * </ul>
 * </p>
 *
 * @see ca.mcgill.sel.ucm.UCMPackage#getNodeConnection()
 * @model
 * @generated
 */
public interface NodeConnection extends EObject {
    /**
     * Returns the value of the '<em><b>Condition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Condition</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Condition</em>' containment reference.
     * @see #setCondition(Condition)
     * @see ca.mcgill.sel.ucm.UCMPackage#getNodeConnection_Condition()
     * @model containment="true"
     * @generated
     */
    Condition getCondition();

    /**
     * Sets the value of the '{@link ca.mcgill.sel.ucm.NodeConnection#getCondition <em>Condition</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Condition</em>' containment reference.
     * @see #getCondition()
     * @generated
     */
    void setCondition(Condition value);

    /**
     * Returns the value of the '<em><b>Source</b></em>' reference.
     * It is bidirectional and its opposite is '{@link ca.mcgill.sel.ucm.PathNode#getSuccessors <em>Successors</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Source</em>' reference.
     * @see #setSource(PathNode)
     * @see ca.mcgill.sel.ucm.UCMPackage#getNodeConnection_Source()
     * @see ca.mcgill.sel.ucm.PathNode#getSuccessors
     * @model opposite="successors" required="true"
     * @generated
     */
    PathNode getSource();

    /**
     * Sets the value of the '{@link ca.mcgill.sel.ucm.NodeConnection#getSource <em>Source</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Source</em>' reference.
     * @see #getSource()
     * @generated
     */
    void setSource(PathNode value);

    /**
     * Returns the value of the '<em><b>Target</b></em>' reference.
     * It is bidirectional and its opposite is '{@link ca.mcgill.sel.ucm.PathNode#getPredecessors <em>Predecessors</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Target</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Target</em>' reference.
     * @see #setTarget(PathNode)
     * @see ca.mcgill.sel.ucm.UCMPackage#getNodeConnection_Target()
     * @see ca.mcgill.sel.ucm.PathNode#getPredecessors
     * @model opposite="predecessors" required="true"
     * @generated
     */
    PathNode getTarget();

    /**
     * Sets the value of the '{@link ca.mcgill.sel.ucm.NodeConnection#getTarget <em>Target</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Target</em>' reference.
     * @see #getTarget()
     * @generated
     */
    void setTarget(PathNode value);

} // NodeConnection
