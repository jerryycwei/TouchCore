/**
 */
package ca.mcgill.sel.ucm.impl;

import ca.mcgill.sel.ucm.EmptyPoint;
import ca.mcgill.sel.ucm.UCMPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Empty Point</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link ca.mcgill.sel.ucm.impl.EmptyPointImpl#isDirection <em>Direction</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EmptyPointImpl extends PathNodeImpl implements EmptyPoint {
    /**
     * The default value of the '{@link #isDirection() <em>Direction</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isDirection()
     * @generated
     * @ordered
     */
    protected static final boolean DIRECTION_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isDirection() <em>Direction</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isDirection()
     * @generated
     * @ordered
     */
    protected boolean direction = DIRECTION_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EmptyPointImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return UCMPackage.Literals.EMPTY_POINT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public boolean isDirection() {
        return direction;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setDirection(boolean newDirection) {
        boolean oldDirection = direction;
        direction = newDirection;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, UCMPackage.EMPTY_POINT__DIRECTION, oldDirection, direction));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case UCMPackage.EMPTY_POINT__DIRECTION:
                return isDirection();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case UCMPackage.EMPTY_POINT__DIRECTION:
                setDirection((Boolean)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case UCMPackage.EMPTY_POINT__DIRECTION:
                setDirection(DIRECTION_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case UCMPackage.EMPTY_POINT__DIRECTION:
                return direction != DIRECTION_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (direction: ");
        result.append(direction);
        result.append(')');
        return result.toString();
    }

} //EmptyPointImpl
