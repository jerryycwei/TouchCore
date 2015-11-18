/**
 */
package ca.mcgill.sel.ucm.impl;

import ca.mcgill.sel.ucm.Component;
import ca.mcgill.sel.ucm.ComponentKind;
import ca.mcgill.sel.ucm.ComponentReference;
import ca.mcgill.sel.ucm.UCMPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link ca.mcgill.sel.ucm.impl.ComponentImpl#getComponentReferences <em>Component References</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.impl.ComponentImpl#getChildren <em>Children</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.impl.ComponentImpl#getParents <em>Parents</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.impl.ComponentImpl#getKind <em>Kind</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ComponentImpl extends UCMModelElementImpl implements Component {
    /**
     * The cached value of the '{@link #getComponentReferences() <em>Component References</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getComponentReferences()
     * @generated
     * @ordered
     */
    protected EList<ComponentReference> componentReferences;

    /**
     * The cached value of the '{@link #getChildren() <em>Children</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getChildren()
     * @generated
     * @ordered
     */
    protected EList<Component> children;

    /**
     * The cached value of the '{@link #getParents() <em>Parents</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParents()
     * @generated
     * @ordered
     */
    protected EList<Component> parents;

    /**
     * The default value of the '{@link #getKind() <em>Kind</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKind()
     * @generated
     * @ordered
     */
    protected static final ComponentKind KIND_EDEFAULT = ComponentKind.TEAM;

    /**
     * The cached value of the '{@link #getKind() <em>Kind</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getKind()
     * @generated
     * @ordered
     */
    protected ComponentKind kind = KIND_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ComponentImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return UCMPackage.Literals.COMPONENT;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<ComponentReference> getComponentReferences() {
        if (componentReferences == null) {
            componentReferences = new EObjectWithInverseResolvingEList<ComponentReference>(ComponentReference.class, this, UCMPackage.COMPONENT__COMPONENT_REFERENCES, UCMPackage.COMPONENT_REFERENCE__COMPONENT_DEFINITION);
        }
        return componentReferences;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<Component> getChildren() {
        if (children == null) {
            children = new EObjectWithInverseResolvingEList.ManyInverse<Component>(Component.class, this, UCMPackage.COMPONENT__CHILDREN, UCMPackage.COMPONENT__PARENTS);
        }
        return children;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<Component> getParents() {
        if (parents == null) {
            parents = new EObjectWithInverseResolvingEList.ManyInverse<Component>(Component.class, this, UCMPackage.COMPONENT__PARENTS, UCMPackage.COMPONENT__CHILDREN);
        }
        return parents;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComponentKind getKind() {
        return kind;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setKind(ComponentKind newKind) {
        ComponentKind oldKind = kind;
        kind = newKind == null ? KIND_EDEFAULT : newKind;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, UCMPackage.COMPONENT__KIND, oldKind, kind));
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
            case UCMPackage.COMPONENT__COMPONENT_REFERENCES:
                return ((InternalEList<InternalEObject>)(InternalEList<?>)getComponentReferences()).basicAdd(otherEnd, msgs);
            case UCMPackage.COMPONENT__CHILDREN:
                return ((InternalEList<InternalEObject>)(InternalEList<?>)getChildren()).basicAdd(otherEnd, msgs);
            case UCMPackage.COMPONENT__PARENTS:
                return ((InternalEList<InternalEObject>)(InternalEList<?>)getParents()).basicAdd(otherEnd, msgs);
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
            case UCMPackage.COMPONENT__COMPONENT_REFERENCES:
                return ((InternalEList<?>)getComponentReferences()).basicRemove(otherEnd, msgs);
            case UCMPackage.COMPONENT__CHILDREN:
                return ((InternalEList<?>)getChildren()).basicRemove(otherEnd, msgs);
            case UCMPackage.COMPONENT__PARENTS:
                return ((InternalEList<?>)getParents()).basicRemove(otherEnd, msgs);
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
            case UCMPackage.COMPONENT__COMPONENT_REFERENCES:
                return getComponentReferences();
            case UCMPackage.COMPONENT__CHILDREN:
                return getChildren();
            case UCMPackage.COMPONENT__PARENTS:
                return getParents();
            case UCMPackage.COMPONENT__KIND:
                return getKind();
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
            case UCMPackage.COMPONENT__COMPONENT_REFERENCES:
                getComponentReferences().clear();
                getComponentReferences().addAll((Collection<? extends ComponentReference>)newValue);
                return;
            case UCMPackage.COMPONENT__CHILDREN:
                getChildren().clear();
                getChildren().addAll((Collection<? extends Component>)newValue);
                return;
            case UCMPackage.COMPONENT__PARENTS:
                getParents().clear();
                getParents().addAll((Collection<? extends Component>)newValue);
                return;
            case UCMPackage.COMPONENT__KIND:
                setKind((ComponentKind)newValue);
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
            case UCMPackage.COMPONENT__COMPONENT_REFERENCES:
                getComponentReferences().clear();
                return;
            case UCMPackage.COMPONENT__CHILDREN:
                getChildren().clear();
                return;
            case UCMPackage.COMPONENT__PARENTS:
                getParents().clear();
                return;
            case UCMPackage.COMPONENT__KIND:
                setKind(KIND_EDEFAULT);
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
            case UCMPackage.COMPONENT__COMPONENT_REFERENCES:
                return componentReferences != null && !componentReferences.isEmpty();
            case UCMPackage.COMPONENT__CHILDREN:
                return children != null && !children.isEmpty();
            case UCMPackage.COMPONENT__PARENTS:
                return parents != null && !parents.isEmpty();
            case UCMPackage.COMPONENT__KIND:
                return kind != KIND_EDEFAULT;
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
        result.append(" (kind: ");
        result.append(kind);
        result.append(')');
        return result.toString();
    }

} //ComponentImpl
