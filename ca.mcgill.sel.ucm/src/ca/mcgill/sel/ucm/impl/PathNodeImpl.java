/**
 */
package ca.mcgill.sel.ucm.impl;

import ca.mcgill.sel.ucm.ComponentReference;
import ca.mcgill.sel.ucm.NodeConnection;
import ca.mcgill.sel.ucm.PathNode;
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
 * An implementation of the model object '<em><b>Path Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link ca.mcgill.sel.ucm.impl.PathNodeImpl#getSuccessors <em>Successors</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.impl.PathNodeImpl#getPredecessors <em>Predecessors</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.impl.PathNodeImpl#getComponentReference <em>Component Reference</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PathNodeImpl extends UCMModelElementImpl implements PathNode {
    /**
     * The cached value of the '{@link #getSuccessors() <em>Successors</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getSuccessors()
     * @generated
     * @ordered
     */
    protected EList<NodeConnection> successors;

    /**
     * The cached value of the '{@link #getPredecessors() <em>Predecessors</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getPredecessors()
     * @generated
     * @ordered
     */
    protected EList<NodeConnection> predecessors;

    /**
     * The cached value of the '{@link #getComponentReference() <em>Component Reference</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getComponentReference()
     * @generated
     * @ordered
     */
    protected ComponentReference componentReference;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected PathNodeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return UCMPackage.Literals.PATH_NODE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<NodeConnection> getSuccessors() {
        if (successors == null) {
            successors = new EObjectWithInverseResolvingEList<NodeConnection>(NodeConnection.class, this, UCMPackage.PATH_NODE__SUCCESSORS, UCMPackage.NODE_CONNECTION__SOURCE);
        }
        return successors;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<NodeConnection> getPredecessors() {
        if (predecessors == null) {
            predecessors = new EObjectWithInverseResolvingEList<NodeConnection>(NodeConnection.class, this, UCMPackage.PATH_NODE__PREDECESSORS, UCMPackage.NODE_CONNECTION__TARGET);
        }
        return predecessors;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComponentReference getComponentReference() {
        if (componentReference != null && componentReference.eIsProxy()) {
            InternalEObject oldComponentReference = (InternalEObject)componentReference;
            componentReference = (ComponentReference)eResolveProxy(oldComponentReference);
            if (componentReference != oldComponentReference) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, UCMPackage.PATH_NODE__COMPONENT_REFERENCE, oldComponentReference, componentReference));
            }
        }
        return componentReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComponentReference basicGetComponentReference() {
        return componentReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetComponentReference(ComponentReference newComponentReference, NotificationChain msgs) {
        ComponentReference oldComponentReference = componentReference;
        componentReference = newComponentReference;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, UCMPackage.PATH_NODE__COMPONENT_REFERENCE, oldComponentReference, newComponentReference);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setComponentReference(ComponentReference newComponentReference) {
        if (newComponentReference != componentReference) {
            NotificationChain msgs = null;
            if (componentReference != null)
                msgs = ((InternalEObject)componentReference).eInverseRemove(this, UCMPackage.COMPONENT_REFERENCE__NODES, ComponentReference.class, msgs);
            if (newComponentReference != null)
                msgs = ((InternalEObject)newComponentReference).eInverseAdd(this, UCMPackage.COMPONENT_REFERENCE__NODES, ComponentReference.class, msgs);
            msgs = basicSetComponentReference(newComponentReference, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, UCMPackage.PATH_NODE__COMPONENT_REFERENCE, newComponentReference, newComponentReference));
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
            case UCMPackage.PATH_NODE__SUCCESSORS:
                return ((InternalEList<InternalEObject>)(InternalEList<?>)getSuccessors()).basicAdd(otherEnd, msgs);
            case UCMPackage.PATH_NODE__PREDECESSORS:
                return ((InternalEList<InternalEObject>)(InternalEList<?>)getPredecessors()).basicAdd(otherEnd, msgs);
            case UCMPackage.PATH_NODE__COMPONENT_REFERENCE:
                if (componentReference != null)
                    msgs = ((InternalEObject)componentReference).eInverseRemove(this, UCMPackage.COMPONENT_REFERENCE__NODES, ComponentReference.class, msgs);
                return basicSetComponentReference((ComponentReference)otherEnd, msgs);
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
            case UCMPackage.PATH_NODE__SUCCESSORS:
                return ((InternalEList<?>)getSuccessors()).basicRemove(otherEnd, msgs);
            case UCMPackage.PATH_NODE__PREDECESSORS:
                return ((InternalEList<?>)getPredecessors()).basicRemove(otherEnd, msgs);
            case UCMPackage.PATH_NODE__COMPONENT_REFERENCE:
                return basicSetComponentReference(null, msgs);
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
            case UCMPackage.PATH_NODE__SUCCESSORS:
                return getSuccessors();
            case UCMPackage.PATH_NODE__PREDECESSORS:
                return getPredecessors();
            case UCMPackage.PATH_NODE__COMPONENT_REFERENCE:
                if (resolve) return getComponentReference();
                return basicGetComponentReference();
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
            case UCMPackage.PATH_NODE__SUCCESSORS:
                getSuccessors().clear();
                getSuccessors().addAll((Collection<? extends NodeConnection>)newValue);
                return;
            case UCMPackage.PATH_NODE__PREDECESSORS:
                getPredecessors().clear();
                getPredecessors().addAll((Collection<? extends NodeConnection>)newValue);
                return;
            case UCMPackage.PATH_NODE__COMPONENT_REFERENCE:
                setComponentReference((ComponentReference)newValue);
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
            case UCMPackage.PATH_NODE__SUCCESSORS:
                getSuccessors().clear();
                return;
            case UCMPackage.PATH_NODE__PREDECESSORS:
                getPredecessors().clear();
                return;
            case UCMPackage.PATH_NODE__COMPONENT_REFERENCE:
                setComponentReference((ComponentReference)null);
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
            case UCMPackage.PATH_NODE__SUCCESSORS:
                return successors != null && !successors.isEmpty();
            case UCMPackage.PATH_NODE__PREDECESSORS:
                return predecessors != null && !predecessors.isEmpty();
            case UCMPackage.PATH_NODE__COMPONENT_REFERENCE:
                return componentReference != null;
        }
        return super.eIsSet(featureID);
    }

} //PathNodeImpl
