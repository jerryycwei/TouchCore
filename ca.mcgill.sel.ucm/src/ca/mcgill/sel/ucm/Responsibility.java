/**
 */
package ca.mcgill.sel.ucm;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Responsibility</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link ca.mcgill.sel.ucm.Responsibility#getResponsibilityReferences <em>Responsibility References</em>}</li>
 * </ul>
 * </p>
 *
 * @see ca.mcgill.sel.ucm.UCMPackage#getResponsibility()
 * @model
 * @generated
 */
public interface Responsibility extends UCMModelElement {
    /**
     * Returns the value of the '<em><b>Responsibility References</b></em>' reference list.
     * The list contents are of type {@link ca.mcgill.sel.ucm.ResponsibilityRef}.
     * It is bidirectional and its opposite is '{@link ca.mcgill.sel.ucm.ResponsibilityRef#getResponsibilityDefinition <em>Responsibility Definition</em>}'.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Responsibility References</em>' reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Responsibility References</em>' reference list.
     * @see ca.mcgill.sel.ucm.UCMPackage#getResponsibility_ResponsibilityReferences()
     * @see ca.mcgill.sel.ucm.ResponsibilityRef#getResponsibilityDefinition
     * @model opposite="responsibilityDefinition"
     * @generated
     */
    EList<ResponsibilityRef> getResponsibilityReferences();

} // Responsibility
