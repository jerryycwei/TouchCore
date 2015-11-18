/**
 */
package ca.mcgill.sel.ucm;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Responsibility Ref</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link ca.mcgill.sel.ucm.ResponsibilityRef#getResponsibilityDefinition <em>Responsibility Definition</em>}</li>
 * </ul>
 * </p>
 *
 * @see ca.mcgill.sel.ucm.UCMPackage#getResponsibilityRef()
 * @model
 * @generated
 */
public interface ResponsibilityRef extends PathNode {
    /**
     * Returns the value of the '<em><b>Responsibility Definition</b></em>' reference.
     * It is bidirectional and its opposite is '{@link ca.mcgill.sel.ucm.Responsibility#getResponsibilityReferences <em>Responsibility References</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Responsibility Definition</em>' reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Responsibility Definition</em>' reference.
     * @see #setResponsibilityDefinition(Responsibility)
     * @see ca.mcgill.sel.ucm.UCMPackage#getResponsibilityRef_ResponsibilityDefinition()
     * @see ca.mcgill.sel.ucm.Responsibility#getResponsibilityReferences
     * @model opposite="responsibilityReferences" required="true"
     * @generated
     */
    Responsibility getResponsibilityDefinition();

    /**
     * Sets the value of the '{@link ca.mcgill.sel.ucm.ResponsibilityRef#getResponsibilityDefinition <em>Responsibility Definition</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Responsibility Definition</em>' reference.
     * @see #getResponsibilityDefinition()
     * @generated
     */
    void setResponsibilityDefinition(Responsibility value);

} // ResponsibilityRef
