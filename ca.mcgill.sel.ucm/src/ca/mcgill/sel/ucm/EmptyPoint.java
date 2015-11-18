/**
 */
package ca.mcgill.sel.ucm;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Empty Point</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link ca.mcgill.sel.ucm.EmptyPoint#isDirection <em>Direction</em>}</li>
 * </ul>
 * </p>
 *
 * @see ca.mcgill.sel.ucm.UCMPackage#getEmptyPoint()
 * @model
 * @generated
 */
public interface EmptyPoint extends PathNode {
    /**
     * Returns the value of the '<em><b>Direction</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Direction</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Direction</em>' attribute.
     * @see #setDirection(boolean)
     * @see ca.mcgill.sel.ucm.UCMPackage#getEmptyPoint_Direction()
     * @model default="false"
     * @generated
     */
    boolean isDirection();

    /**
     * Sets the value of the '{@link ca.mcgill.sel.ucm.EmptyPoint#isDirection <em>Direction</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Direction</em>' attribute.
     * @see #isDirection()
     * @generated
     */
    void setDirection(boolean value);

} // EmptyPoint
