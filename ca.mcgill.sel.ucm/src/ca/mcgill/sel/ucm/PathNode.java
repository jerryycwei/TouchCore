/**
 */
package ca.mcgill.sel.ucm;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Path Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link ca.mcgill.sel.ucm.PathNode#getSuccessors <em>Successors</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.PathNode#getPredecessors <em>Predecessors</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.PathNode#getComponentReference <em>Component Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @see ca.mcgill.sel.ucm.UCMPackage#getPathNode()
 * @model
 * @generated
 */
public interface PathNode extends UCMModelElement {
    /**
     * Returns the value of the '<em><b>Successors</b></em>' reference list.
     * The list contents are of type {@link ca.mcgill.sel.ucm.NodeConnection}.
     * It is bidirectional and its opposite is '{@link ca.mcgill.sel.ucm.NodeConnection#getSource <em>Source</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Successors</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Successors</em>' reference list.
     * @see ca.mcgill.sel.ucm.UCMPackage#getPathNode_Successors()
     * @see ca.mcgill.sel.ucm.NodeConnection#getSource
     * @model opposite="source"
     * @generated
     */
    EList<NodeConnection> getSuccessors();

    /**
     * Returns the value of the '<em><b>Predecessors</b></em>' reference list.
     * The list contents are of type {@link ca.mcgill.sel.ucm.NodeConnection}.
     * It is bidirectional and its opposite is '{@link ca.mcgill.sel.ucm.NodeConnection#getTarget <em>Target</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Predecessors</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Predecessors</em>' reference list.
     * @see ca.mcgill.sel.ucm.UCMPackage#getPathNode_Predecessors()
     * @see ca.mcgill.sel.ucm.NodeConnection#getTarget
     * @model opposite="target"
     * @generated
     */
    EList<NodeConnection> getPredecessors();

    /**
     * Returns the value of the '<em><b>Component Reference</b></em>' reference.
     * It is bidirectional and its opposite is '{@link ca.mcgill.sel.ucm.ComponentReference#getNodes <em>Nodes</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Component Reference</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Component Reference</em>' reference.
     * @see #setComponentReference(ComponentReference)
     * @see ca.mcgill.sel.ucm.UCMPackage#getPathNode_ComponentReference()
     * @see ca.mcgill.sel.ucm.ComponentReference#getNodes
     * @model opposite="nodes"
     * @generated
     */
    ComponentReference getComponentReference();

    /**
     * Sets the value of the '{@link ca.mcgill.sel.ucm.PathNode#getComponentReference <em>Component Reference</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Component Reference</em>' reference.
     * @see #getComponentReference()
     * @generated
     */
    void setComponentReference(ComponentReference value);

} // PathNode
