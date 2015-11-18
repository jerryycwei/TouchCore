/**
 */
package ca.mcgill.sel.ucm;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Component</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link ca.mcgill.sel.ucm.Component#getComponentReferences <em>Component References</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.Component#getChildren <em>Children</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.Component#getParents <em>Parents</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.Component#getKind <em>Kind</em>}</li>
 * </ul>
 * </p>
 *
 * @see ca.mcgill.sel.ucm.UCMPackage#getComponent()
 * @model
 * @generated
 */
public interface Component extends UCMModelElement {
    /**
     * Returns the value of the '<em><b>Component References</b></em>' reference list.
     * The list contents are of type {@link ca.mcgill.sel.ucm.ComponentReference}.
     * It is bidirectional and its opposite is '{@link ca.mcgill.sel.ucm.ComponentReference#getComponentDefinition <em>Component Definition</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Component References</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Component References</em>' reference list.
     * @see ca.mcgill.sel.ucm.UCMPackage#getComponent_ComponentReferences()
     * @see ca.mcgill.sel.ucm.ComponentReference#getComponentDefinition
     * @model opposite="componentDefinition"
     * @generated
     */
    EList<ComponentReference> getComponentReferences();

    /**
     * Returns the value of the '<em><b>Children</b></em>' reference list.
     * The list contents are of type {@link ca.mcgill.sel.ucm.Component}.
     * It is bidirectional and its opposite is '{@link ca.mcgill.sel.ucm.Component#getParents <em>Parents</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Children</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Children</em>' reference list.
     * @see ca.mcgill.sel.ucm.UCMPackage#getComponent_Children()
     * @see ca.mcgill.sel.ucm.Component#getParents
     * @model opposite="parents"
     * @generated
     */
    EList<Component> getChildren();

    /**
     * Returns the value of the '<em><b>Parents</b></em>' reference list.
     * The list contents are of type {@link ca.mcgill.sel.ucm.Component}.
     * It is bidirectional and its opposite is '{@link ca.mcgill.sel.ucm.Component#getChildren <em>Children</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Parents</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parents</em>' reference list.
     * @see ca.mcgill.sel.ucm.UCMPackage#getComponent_Parents()
     * @see ca.mcgill.sel.ucm.Component#getChildren
     * @model opposite="children"
     * @generated
     */
    EList<Component> getParents();

    /**
     * Returns the value of the '<em><b>Kind</b></em>' attribute.
     * The default value is <code>"Team"</code>.
     * The literals are from the enumeration {@link ca.mcgill.sel.ucm.ComponentKind}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Kind</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Kind</em>' attribute.
     * @see ca.mcgill.sel.ucm.ComponentKind
     * @see #setKind(ComponentKind)
     * @see ca.mcgill.sel.ucm.UCMPackage#getComponent_Kind()
     * @model default="Team"
     * @generated
     */
    ComponentKind getKind();

    /**
     * Sets the value of the '{@link ca.mcgill.sel.ucm.Component#getKind <em>Kind</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Kind</em>' attribute.
     * @see ca.mcgill.sel.ucm.ComponentKind
     * @see #getKind()
     * @generated
     */
    void setKind(ComponentKind value);

} // Component
