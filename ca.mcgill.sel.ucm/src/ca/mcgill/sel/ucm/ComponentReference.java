/**
 */
package ca.mcgill.sel.ucm;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Component Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link ca.mcgill.sel.ucm.ComponentReference#getComponentDefinition <em>Component Definition</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.ComponentReference#getChildren <em>Children</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.ComponentReference#getParent <em>Parent</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.ComponentReference#getNodes <em>Nodes</em>}</li>
 * </ul>
 * </p>
 *
 * @see ca.mcgill.sel.ucm.UCMPackage#getComponentReference()
 * @model
 * @generated
 */
public interface ComponentReference extends UCMModelElement {
    /**
     * Returns the value of the '<em><b>Component Definition</b></em>' reference.
     * It is bidirectional and its opposite is '{@link ca.mcgill.sel.ucm.Component#getComponentReferences <em>Component References</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Component Definition</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Component Definition</em>' reference.
     * @see #setComponentDefinition(Component)
     * @see ca.mcgill.sel.ucm.UCMPackage#getComponentReference_ComponentDefinition()
     * @see ca.mcgill.sel.ucm.Component#getComponentReferences
     * @model opposite="componentReferences" required="true"
     * @generated
     */
    Component getComponentDefinition();

    /**
     * Sets the value of the '{@link ca.mcgill.sel.ucm.ComponentReference#getComponentDefinition <em>Component Definition</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Component Definition</em>' reference.
     * @see #getComponentDefinition()
     * @generated
     */
    void setComponentDefinition(Component value);

    /**
     * Returns the value of the '<em><b>Children</b></em>' reference list.
     * The list contents are of type {@link ca.mcgill.sel.ucm.ComponentReference}.
     * It is bidirectional and its opposite is '{@link ca.mcgill.sel.ucm.ComponentReference#getParent <em>Parent</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Children</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Children</em>' reference list.
     * @see ca.mcgill.sel.ucm.UCMPackage#getComponentReference_Children()
     * @see ca.mcgill.sel.ucm.ComponentReference#getParent
     * @model opposite="parent"
     * @generated
     */
    EList<ComponentReference> getChildren();

    /**
     * Returns the value of the '<em><b>Parent</b></em>' reference.
     * It is bidirectional and its opposite is '{@link ca.mcgill.sel.ucm.ComponentReference#getChildren <em>Children</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Parent</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parent</em>' reference.
     * @see #setParent(ComponentReference)
     * @see ca.mcgill.sel.ucm.UCMPackage#getComponentReference_Parent()
     * @see ca.mcgill.sel.ucm.ComponentReference#getChildren
     * @model opposite="children"
     * @generated
     */
    ComponentReference getParent();

    /**
     * Sets the value of the '{@link ca.mcgill.sel.ucm.ComponentReference#getParent <em>Parent</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Parent</em>' reference.
     * @see #getParent()
     * @generated
     */
    void setParent(ComponentReference value);

    /**
     * Returns the value of the '<em><b>Nodes</b></em>' reference list.
     * The list contents are of type {@link ca.mcgill.sel.ucm.PathNode}.
     * It is bidirectional and its opposite is '{@link ca.mcgill.sel.ucm.PathNode#getComponentReference <em>Component Reference</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Nodes</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Nodes</em>' reference list.
     * @see ca.mcgill.sel.ucm.UCMPackage#getComponentReference_Nodes()
     * @see ca.mcgill.sel.ucm.PathNode#getComponentReference
     * @model opposite="componentReference"
     * @generated
     */
    EList<PathNode> getNodes();

} // ComponentReference
