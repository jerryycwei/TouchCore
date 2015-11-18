/**
 */
package ca.mcgill.sel.ucm.impl;

import ca.mcgill.sel.ucm.Responsibility;
import ca.mcgill.sel.ucm.ResponsibilityRef;
import ca.mcgill.sel.ucm.UCMPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Responsibility</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link ca.mcgill.sel.ucm.impl.ResponsibilityImpl#getResponsibilityReferences <em>Responsibility References</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ResponsibilityImpl extends UCMModelElementImpl implements Responsibility {
    /**
     * The cached value of the '{@link #getResponsibilityReferences() <em>Responsibility References</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResponsibilityReferences()
     * @generated
     * @ordered
     */
    protected EList<ResponsibilityRef> responsibilityReferences;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ResponsibilityImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return UCMPackage.Literals.RESPONSIBILITY;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<ResponsibilityRef> getResponsibilityReferences() {
        if (responsibilityReferences == null) {
            responsibilityReferences = new EObjectWithInverseResolvingEList<ResponsibilityRef>(ResponsibilityRef.class, this, UCMPackage.RESPONSIBILITY__RESPONSIBILITY_REFERENCES, UCMPackage.RESPONSIBILITY_REF__RESPONSIBILITY_DEFINITION);
        }
        return responsibilityReferences;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case UCMPackage.RESPONSIBILITY__RESPONSIBILITY_REFERENCES:
                return ((InternalEList<InternalEObject>)(InternalEList<?>)getResponsibilityReferences()).basicAdd(otherEnd, msgs);
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
            case UCMPackage.RESPONSIBILITY__RESPONSIBILITY_REFERENCES:
                return ((InternalEList<?>)getResponsibilityReferences()).basicRemove(otherEnd, msgs);
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
            case UCMPackage.RESPONSIBILITY__RESPONSIBILITY_REFERENCES:
                return getResponsibilityReferences();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case UCMPackage.RESPONSIBILITY__RESPONSIBILITY_REFERENCES:
                getResponsibilityReferences().clear();
                getResponsibilityReferences().addAll((Collection<? extends ResponsibilityRef>)newValue);
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
            case UCMPackage.RESPONSIBILITY__RESPONSIBILITY_REFERENCES:
                getResponsibilityReferences().clear();
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
            case UCMPackage.RESPONSIBILITY__RESPONSIBILITY_REFERENCES:
                return responsibilityReferences != null && !responsibilityReferences.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ResponsibilityImpl
