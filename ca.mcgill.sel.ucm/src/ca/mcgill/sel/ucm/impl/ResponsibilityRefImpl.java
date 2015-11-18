/**
 */
package ca.mcgill.sel.ucm.impl;

import ca.mcgill.sel.ucm.Responsibility;
import ca.mcgill.sel.ucm.ResponsibilityRef;
import ca.mcgill.sel.ucm.UCMPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Responsibility Ref</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link ca.mcgill.sel.ucm.impl.ResponsibilityRefImpl#getResponsibilityDefinition <em>Responsibility Definition</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ResponsibilityRefImpl extends PathNodeImpl implements ResponsibilityRef {
    /**
     * The cached value of the '{@link #getResponsibilityDefinition() <em>Responsibility Definition</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResponsibilityDefinition()
     * @generated
     * @ordered
     */
    protected Responsibility responsibilityDefinition;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ResponsibilityRefImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return UCMPackage.Literals.RESPONSIBILITY_REF;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Responsibility getResponsibilityDefinition() {
        if (responsibilityDefinition != null && responsibilityDefinition.eIsProxy()) {
            InternalEObject oldResponsibilityDefinition = (InternalEObject)responsibilityDefinition;
            responsibilityDefinition = (Responsibility)eResolveProxy(oldResponsibilityDefinition);
            if (responsibilityDefinition != oldResponsibilityDefinition) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, UCMPackage.RESPONSIBILITY_REF__RESPONSIBILITY_DEFINITION, oldResponsibilityDefinition, responsibilityDefinition));
            }
        }
        return responsibilityDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Responsibility basicGetResponsibilityDefinition() {
        return responsibilityDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetResponsibilityDefinition(Responsibility newResponsibilityDefinition, NotificationChain msgs) {
        Responsibility oldResponsibilityDefinition = responsibilityDefinition;
        responsibilityDefinition = newResponsibilityDefinition;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, UCMPackage.RESPONSIBILITY_REF__RESPONSIBILITY_DEFINITION, oldResponsibilityDefinition, newResponsibilityDefinition);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setResponsibilityDefinition(Responsibility newResponsibilityDefinition) {
        if (newResponsibilityDefinition != responsibilityDefinition) {
            NotificationChain msgs = null;
            if (responsibilityDefinition != null)
                msgs = ((InternalEObject)responsibilityDefinition).eInverseRemove(this, UCMPackage.RESPONSIBILITY__RESPONSIBILITY_REFERENCES, Responsibility.class, msgs);
            if (newResponsibilityDefinition != null)
                msgs = ((InternalEObject)newResponsibilityDefinition).eInverseAdd(this, UCMPackage.RESPONSIBILITY__RESPONSIBILITY_REFERENCES, Responsibility.class, msgs);
            msgs = basicSetResponsibilityDefinition(newResponsibilityDefinition, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, UCMPackage.RESPONSIBILITY_REF__RESPONSIBILITY_DEFINITION, newResponsibilityDefinition, newResponsibilityDefinition));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case UCMPackage.RESPONSIBILITY_REF__RESPONSIBILITY_DEFINITION:
                if (responsibilityDefinition != null)
                    msgs = ((InternalEObject)responsibilityDefinition).eInverseRemove(this, UCMPackage.RESPONSIBILITY__RESPONSIBILITY_REFERENCES, Responsibility.class, msgs);
                return basicSetResponsibilityDefinition((Responsibility)otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case UCMPackage.RESPONSIBILITY_REF__RESPONSIBILITY_DEFINITION:
                return basicSetResponsibilityDefinition(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case UCMPackage.RESPONSIBILITY_REF__RESPONSIBILITY_DEFINITION:
                if (resolve) return getResponsibilityDefinition();
                return basicGetResponsibilityDefinition();
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
            case UCMPackage.RESPONSIBILITY_REF__RESPONSIBILITY_DEFINITION:
                setResponsibilityDefinition((Responsibility)newValue);
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
            case UCMPackage.RESPONSIBILITY_REF__RESPONSIBILITY_DEFINITION:
                setResponsibilityDefinition((Responsibility)null);
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
            case UCMPackage.RESPONSIBILITY_REF__RESPONSIBILITY_DEFINITION:
                return responsibilityDefinition != null;
        }
        return super.eIsSet(featureID);
    }

} //ResponsibilityRefImpl
