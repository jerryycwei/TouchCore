/**
 */
package ca.mcgill.sel.ucm.impl;

import ca.mcgill.sel.ucm.Component;
import ca.mcgill.sel.ucm.ComponentReference;
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
 * An implementation of the model object '<em><b>Component Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link ca.mcgill.sel.ucm.impl.ComponentReferenceImpl#getComponentDefinition <em>Component Definition</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.impl.ComponentReferenceImpl#getChildren <em>Children</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.impl.ComponentReferenceImpl#getParent <em>Parent</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.impl.ComponentReferenceImpl#getNodes <em>Nodes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ComponentReferenceImpl extends UCMModelElementImpl implements ComponentReference {
    /**
     * The cached value of the '{@link #getComponentDefinition() <em>Component Definition</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getComponentDefinition()
     * @generated
     * @ordered
     */
    protected Component componentDefinition;

    /**
     * The cached value of the '{@link #getChildren() <em>Children</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getChildren()
     * @generated
     * @ordered
     */
    protected EList<ComponentReference> children;

    /**
     * The cached value of the '{@link #getParent() <em>Parent</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getParent()
     * @generated
     * @ordered
     */
    protected ComponentReference parent;

    /**
     * The cached value of the '{@link #getNodes() <em>Nodes</em>}' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNodes()
     * @generated
     * @ordered
     */
    protected EList<PathNode> nodes;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ComponentReferenceImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return UCMPackage.Literals.COMPONENT_REFERENCE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Component getComponentDefinition() {
        if (componentDefinition != null && componentDefinition.eIsProxy()) {
            InternalEObject oldComponentDefinition = (InternalEObject)componentDefinition;
            componentDefinition = (Component)eResolveProxy(oldComponentDefinition);
            if (componentDefinition != oldComponentDefinition) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, UCMPackage.COMPONENT_REFERENCE__COMPONENT_DEFINITION, oldComponentDefinition, componentDefinition));
            }
        }
        return componentDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Component basicGetComponentDefinition() {
        return componentDefinition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetComponentDefinition(Component newComponentDefinition, NotificationChain msgs) {
        Component oldComponentDefinition = componentDefinition;
        componentDefinition = newComponentDefinition;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, UCMPackage.COMPONENT_REFERENCE__COMPONENT_DEFINITION, oldComponentDefinition, newComponentDefinition);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setComponentDefinition(Component newComponentDefinition) {
        if (newComponentDefinition != componentDefinition) {
            NotificationChain msgs = null;
            if (componentDefinition != null)
                msgs = ((InternalEObject)componentDefinition).eInverseRemove(this, UCMPackage.COMPONENT__COMPONENT_REFERENCES, Component.class, msgs);
            if (newComponentDefinition != null)
                msgs = ((InternalEObject)newComponentDefinition).eInverseAdd(this, UCMPackage.COMPONENT__COMPONENT_REFERENCES, Component.class, msgs);
            msgs = basicSetComponentDefinition(newComponentDefinition, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, UCMPackage.COMPONENT_REFERENCE__COMPONENT_DEFINITION, newComponentDefinition, newComponentDefinition));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<ComponentReference> getChildren() {
        if (children == null) {
            children = new EObjectWithInverseResolvingEList<ComponentReference>(ComponentReference.class, this, UCMPackage.COMPONENT_REFERENCE__CHILDREN, UCMPackage.COMPONENT_REFERENCE__PARENT);
        }
        return children;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComponentReference getParent() {
        if (parent != null && parent.eIsProxy()) {
            InternalEObject oldParent = (InternalEObject)parent;
            parent = (ComponentReference)eResolveProxy(oldParent);
            if (parent != oldParent) {
                if (eNotificationRequired())
                    eNotify(new ENotificationImpl(this, Notification.RESOLVE, UCMPackage.COMPONENT_REFERENCE__PARENT, oldParent, parent));
            }
        }
        return parent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComponentReference basicGetParent() {
        return parent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetParent(ComponentReference newParent, NotificationChain msgs) {
        ComponentReference oldParent = parent;
        parent = newParent;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, UCMPackage.COMPONENT_REFERENCE__PARENT, oldParent, newParent);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setParent(ComponentReference newParent) {
        if (newParent != parent) {
            NotificationChain msgs = null;
            if (parent != null)
                msgs = ((InternalEObject)parent).eInverseRemove(this, UCMPackage.COMPONENT_REFERENCE__CHILDREN, ComponentReference.class, msgs);
            if (newParent != null)
                msgs = ((InternalEObject)newParent).eInverseAdd(this, UCMPackage.COMPONENT_REFERENCE__CHILDREN, ComponentReference.class, msgs);
            msgs = basicSetParent(newParent, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, UCMPackage.COMPONENT_REFERENCE__PARENT, newParent, newParent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<PathNode> getNodes() {
        if (nodes == null) {
            nodes = new EObjectWithInverseResolvingEList<PathNode>(PathNode.class, this, UCMPackage.COMPONENT_REFERENCE__NODES, UCMPackage.PATH_NODE__COMPONENT_REFERENCE);
        }
        return nodes;
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
            case UCMPackage.COMPONENT_REFERENCE__COMPONENT_DEFINITION:
                if (componentDefinition != null)
                    msgs = ((InternalEObject)componentDefinition).eInverseRemove(this, UCMPackage.COMPONENT__COMPONENT_REFERENCES, Component.class, msgs);
                return basicSetComponentDefinition((Component)otherEnd, msgs);
            case UCMPackage.COMPONENT_REFERENCE__CHILDREN:
                return ((InternalEList<InternalEObject>)(InternalEList<?>)getChildren()).basicAdd(otherEnd, msgs);
            case UCMPackage.COMPONENT_REFERENCE__PARENT:
                if (parent != null)
                    msgs = ((InternalEObject)parent).eInverseRemove(this, UCMPackage.COMPONENT_REFERENCE__CHILDREN, ComponentReference.class, msgs);
                return basicSetParent((ComponentReference)otherEnd, msgs);
            case UCMPackage.COMPONENT_REFERENCE__NODES:
                return ((InternalEList<InternalEObject>)(InternalEList<?>)getNodes()).basicAdd(otherEnd, msgs);
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
            case UCMPackage.COMPONENT_REFERENCE__COMPONENT_DEFINITION:
                return basicSetComponentDefinition(null, msgs);
            case UCMPackage.COMPONENT_REFERENCE__CHILDREN:
                return ((InternalEList<?>)getChildren()).basicRemove(otherEnd, msgs);
            case UCMPackage.COMPONENT_REFERENCE__PARENT:
                return basicSetParent(null, msgs);
            case UCMPackage.COMPONENT_REFERENCE__NODES:
                return ((InternalEList<?>)getNodes()).basicRemove(otherEnd, msgs);
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
            case UCMPackage.COMPONENT_REFERENCE__COMPONENT_DEFINITION:
                if (resolve) return getComponentDefinition();
                return basicGetComponentDefinition();
            case UCMPackage.COMPONENT_REFERENCE__CHILDREN:
                return getChildren();
            case UCMPackage.COMPONENT_REFERENCE__PARENT:
                if (resolve) return getParent();
                return basicGetParent();
            case UCMPackage.COMPONENT_REFERENCE__NODES:
                return getNodes();
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
            case UCMPackage.COMPONENT_REFERENCE__COMPONENT_DEFINITION:
                setComponentDefinition((Component)newValue);
                return;
            case UCMPackage.COMPONENT_REFERENCE__CHILDREN:
                getChildren().clear();
                getChildren().addAll((Collection<? extends ComponentReference>)newValue);
                return;
            case UCMPackage.COMPONENT_REFERENCE__PARENT:
                setParent((ComponentReference)newValue);
                return;
            case UCMPackage.COMPONENT_REFERENCE__NODES:
                getNodes().clear();
                getNodes().addAll((Collection<? extends PathNode>)newValue);
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
            case UCMPackage.COMPONENT_REFERENCE__COMPONENT_DEFINITION:
                setComponentDefinition((Component)null);
                return;
            case UCMPackage.COMPONENT_REFERENCE__CHILDREN:
                getChildren().clear();
                return;
            case UCMPackage.COMPONENT_REFERENCE__PARENT:
                setParent((ComponentReference)null);
                return;
            case UCMPackage.COMPONENT_REFERENCE__NODES:
                getNodes().clear();
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
            case UCMPackage.COMPONENT_REFERENCE__COMPONENT_DEFINITION:
                return componentDefinition != null;
            case UCMPackage.COMPONENT_REFERENCE__CHILDREN:
                return children != null && !children.isEmpty();
            case UCMPackage.COMPONENT_REFERENCE__PARENT:
                return parent != null;
            case UCMPackage.COMPONENT_REFERENCE__NODES:
                return nodes != null && !nodes.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //ComponentReferenceImpl
