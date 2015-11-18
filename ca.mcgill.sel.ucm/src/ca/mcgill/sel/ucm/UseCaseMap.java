/**
 */
package ca.mcgill.sel.ucm;

import ca.mcgill.sel.core.COREModel;

import ca.mcgill.sel.core.LayoutElement;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Use Case Map</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link ca.mcgill.sel.ucm.UseCaseMap#getConnections <em>Connections</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.UseCaseMap#getNodes <em>Nodes</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.UseCaseMap#getResponsibilities <em>Responsibilities</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.UseCaseMap#getComponents <em>Components</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.UseCaseMap#getComponentReferences <em>Component References</em>}</li>
 *   <li>{@link ca.mcgill.sel.ucm.UseCaseMap#getLayoutMaps <em>Layout Maps</em>}</li>
 * </ul>
 * </p>
 *
 * @see ca.mcgill.sel.ucm.UCMPackage#getUseCaseMap()
 * @model
 * @generated
 */
public interface UseCaseMap extends COREModel {
    /**
     * Returns the value of the '<em><b>Connections</b></em>' containment reference list.
     * The list contents are of type {@link ca.mcgill.sel.ucm.NodeConnection}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Connections</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Connections</em>' containment reference list.
     * @see ca.mcgill.sel.ucm.UCMPackage#getUseCaseMap_Connections()
     * @model containment="true"
     * @generated
     */
    EList<NodeConnection> getConnections();

    /**
     * Returns the value of the '<em><b>Nodes</b></em>' containment reference list.
     * The list contents are of type {@link ca.mcgill.sel.ucm.PathNode}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Nodes</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Nodes</em>' containment reference list.
     * @see ca.mcgill.sel.ucm.UCMPackage#getUseCaseMap_Nodes()
     * @model containment="true"
     * @generated
     */
    EList<PathNode> getNodes();

    /**
     * Returns the value of the '<em><b>Responsibilities</b></em>' containment reference list.
     * The list contents are of type {@link ca.mcgill.sel.ucm.Responsibility}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Responsibilities</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Responsibilities</em>' containment reference list.
     * @see ca.mcgill.sel.ucm.UCMPackage#getUseCaseMap_Responsibilities()
     * @model containment="true"
     * @generated
     */
    EList<Responsibility> getResponsibilities();

    /**
     * Returns the value of the '<em><b>Components</b></em>' containment reference list.
     * The list contents are of type {@link ca.mcgill.sel.ucm.Component}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Components</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Components</em>' containment reference list.
     * @see ca.mcgill.sel.ucm.UCMPackage#getUseCaseMap_Components()
     * @model containment="true"
     * @generated
     */
    EList<Component> getComponents();

    /**
     * Returns the value of the '<em><b>Component References</b></em>' containment reference list.
     * The list contents are of type {@link ca.mcgill.sel.ucm.ComponentReference}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Component References</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Component References</em>' containment reference list.
     * @see ca.mcgill.sel.ucm.UCMPackage#getUseCaseMap_ComponentReferences()
     * @model containment="true"
     * @generated
     */
    EList<ComponentReference> getComponentReferences();

    /**
     * Returns the value of the '<em><b>Layout Maps</b></em>' map.
     * The key is of type {@link org.eclipse.emf.ecore.EObject},
     * and the value is of type {@link ca.mcgill.sel.core.LayoutElement},
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Layout Maps</em>' map isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Layout Maps</em>' map.
     * @see ca.mcgill.sel.ucm.UCMPackage#getUseCaseMap_LayoutMaps()
     * @model mapType="ca.mcgill.sel.core.LayoutMap<org.eclipse.emf.ecore.EObject, ca.mcgill.sel.core.LayoutElement>"
     * @generated
     */
    EMap<EObject, LayoutElement> getLayoutMaps();

} // UseCaseMap
