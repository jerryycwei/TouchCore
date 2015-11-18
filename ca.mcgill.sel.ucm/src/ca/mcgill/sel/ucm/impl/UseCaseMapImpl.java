/**
 */
package ca.mcgill.sel.ucm.impl;

import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.core.LayoutElement;
import ca.mcgill.sel.core.impl.COREModelImpl;

import ca.mcgill.sel.core.impl.LayoutMapImpl;
import ca.mcgill.sel.ucm.Component;
import ca.mcgill.sel.ucm.ComponentReference;
import ca.mcgill.sel.ucm.NodeConnection;
import ca.mcgill.sel.ucm.PathNode;
import ca.mcgill.sel.ucm.Responsibility;
import ca.mcgill.sel.ucm.UCMPackage;
import ca.mcgill.sel.ucm.UseCaseMap;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Use Case Map</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link ca.mcgill.sel.ucm.impl.UseCaseMapImpl#getConnections <em>Connections</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.impl.UseCaseMapImpl#getNodes <em>Nodes</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.impl.UseCaseMapImpl#getResponsibilities <em>Responsibilities</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.impl.UseCaseMapImpl#getComponents <em>Components</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.impl.UseCaseMapImpl#getComponentReferences <em>Component References</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.impl.UseCaseMapImpl#getLayoutMaps <em>Layout Maps</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class UseCaseMapImpl extends COREModelImpl implements UseCaseMap {
    /**
     * The cached value of the '{@link #getConnections() <em>Connections</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getConnections()
     * @generated
     * @ordered
     */
    protected EList<NodeConnection> connections;

    /**
     * The cached value of the '{@link #getNodes() <em>Nodes</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNodes()
     * @generated
     * @ordered
     */
    protected EList<PathNode> nodes;

    /**
     * The cached value of the '{@link #getResponsibilities() <em>Responsibilities</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getResponsibilities()
     * @generated
     * @ordered
     */
    protected EList<Responsibility> responsibilities;

    /**
     * The cached value of the '{@link #getComponents() <em>Components</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getComponents()
     * @generated
     * @ordered
     */
    protected EList<Component> components;

    /**
     * The cached value of the '{@link #getComponentReferences() <em>Component References</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getComponentReferences()
     * @generated
     * @ordered
     */
    protected EList<ComponentReference> componentReferences;

    /**
     * The cached value of the '{@link #getLayoutMaps() <em>Layout Maps</em>}' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLayoutMaps()
     * @generated
     * @ordered
     */
    protected EMap<EObject, LayoutElement> layoutMaps;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected UseCaseMapImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return UCMPackage.Literals.USE_CASE_MAP;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<NodeConnection> getConnections() {
        if (connections == null) {
            connections = new EObjectContainmentEList<NodeConnection>(NodeConnection.class, this, UCMPackage.USE_CASE_MAP__CONNECTIONS);
        }
        return connections;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<PathNode> getNodes() {
        if (nodes == null) {
            nodes = new EObjectContainmentEList<PathNode>(PathNode.class, this, UCMPackage.USE_CASE_MAP__NODES);
        }
        return nodes;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<Responsibility> getResponsibilities() {
        if (responsibilities == null) {
            responsibilities = new EObjectContainmentEList<Responsibility>(Responsibility.class, this, UCMPackage.USE_CASE_MAP__RESPONSIBILITIES);
        }
        return responsibilities;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<Component> getComponents() {
        if (components == null) {
            components = new EObjectContainmentEList<Component>(Component.class, this, UCMPackage.USE_CASE_MAP__COMPONENTS);
        }
        return components;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<ComponentReference> getComponentReferences() {
        if (componentReferences == null) {
            componentReferences = new EObjectContainmentEList<ComponentReference>(ComponentReference.class, this, UCMPackage.USE_CASE_MAP__COMPONENT_REFERENCES);
        }
        return componentReferences;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EMap<EObject, LayoutElement> getLayoutMaps() {
        if (layoutMaps == null) {
            layoutMaps = new EcoreEMap<EObject,LayoutElement>(CorePackage.Literals.LAYOUT_MAP, LayoutMapImpl.class, this, UCMPackage.USE_CASE_MAP__LAYOUT_MAPS);
        }
        return layoutMaps;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case UCMPackage.USE_CASE_MAP__CONNECTIONS:
                return ((InternalEList<?>)getConnections()).basicRemove(otherEnd, msgs);
            case UCMPackage.USE_CASE_MAP__NODES:
                return ((InternalEList<?>)getNodes()).basicRemove(otherEnd, msgs);
            case UCMPackage.USE_CASE_MAP__RESPONSIBILITIES:
                return ((InternalEList<?>)getResponsibilities()).basicRemove(otherEnd, msgs);
            case UCMPackage.USE_CASE_MAP__COMPONENTS:
                return ((InternalEList<?>)getComponents()).basicRemove(otherEnd, msgs);
            case UCMPackage.USE_CASE_MAP__COMPONENT_REFERENCES:
                return ((InternalEList<?>)getComponentReferences()).basicRemove(otherEnd, msgs);
            case UCMPackage.USE_CASE_MAP__LAYOUT_MAPS:
                return ((InternalEList<?>)getLayoutMaps()).basicRemove(otherEnd, msgs);
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
            case UCMPackage.USE_CASE_MAP__CONNECTIONS:
                return getConnections();
            case UCMPackage.USE_CASE_MAP__NODES:
                return getNodes();
            case UCMPackage.USE_CASE_MAP__RESPONSIBILITIES:
                return getResponsibilities();
            case UCMPackage.USE_CASE_MAP__COMPONENTS:
                return getComponents();
            case UCMPackage.USE_CASE_MAP__COMPONENT_REFERENCES:
                return getComponentReferences();
            case UCMPackage.USE_CASE_MAP__LAYOUT_MAPS:
                if (coreType) return getLayoutMaps();
                else return getLayoutMaps().map();
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
            case UCMPackage.USE_CASE_MAP__CONNECTIONS:
                getConnections().clear();
                getConnections().addAll((Collection<? extends NodeConnection>)newValue);
                return;
            case UCMPackage.USE_CASE_MAP__NODES:
                getNodes().clear();
                getNodes().addAll((Collection<? extends PathNode>)newValue);
                return;
            case UCMPackage.USE_CASE_MAP__RESPONSIBILITIES:
                getResponsibilities().clear();
                getResponsibilities().addAll((Collection<? extends Responsibility>)newValue);
                return;
            case UCMPackage.USE_CASE_MAP__COMPONENTS:
                getComponents().clear();
                getComponents().addAll((Collection<? extends Component>)newValue);
                return;
            case UCMPackage.USE_CASE_MAP__COMPONENT_REFERENCES:
                getComponentReferences().clear();
                getComponentReferences().addAll((Collection<? extends ComponentReference>)newValue);
                return;
            case UCMPackage.USE_CASE_MAP__LAYOUT_MAPS:
                ((EStructuralFeature.Setting)getLayoutMaps()).set(newValue);
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
            case UCMPackage.USE_CASE_MAP__CONNECTIONS:
                getConnections().clear();
                return;
            case UCMPackage.USE_CASE_MAP__NODES:
                getNodes().clear();
                return;
            case UCMPackage.USE_CASE_MAP__RESPONSIBILITIES:
                getResponsibilities().clear();
                return;
            case UCMPackage.USE_CASE_MAP__COMPONENTS:
                getComponents().clear();
                return;
            case UCMPackage.USE_CASE_MAP__COMPONENT_REFERENCES:
                getComponentReferences().clear();
                return;
            case UCMPackage.USE_CASE_MAP__LAYOUT_MAPS:
                getLayoutMaps().clear();
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
            case UCMPackage.USE_CASE_MAP__CONNECTIONS:
                return connections != null && !connections.isEmpty();
            case UCMPackage.USE_CASE_MAP__NODES:
                return nodes != null && !nodes.isEmpty();
            case UCMPackage.USE_CASE_MAP__RESPONSIBILITIES:
                return responsibilities != null && !responsibilities.isEmpty();
            case UCMPackage.USE_CASE_MAP__COMPONENTS:
                return components != null && !components.isEmpty();
            case UCMPackage.USE_CASE_MAP__COMPONENT_REFERENCES:
                return componentReferences != null && !componentReferences.isEmpty();
            case UCMPackage.USE_CASE_MAP__LAYOUT_MAPS:
                return layoutMaps != null && !layoutMaps.isEmpty();
        }
        return super.eIsSet(featureID);
    }

} //UseCaseMapImpl
