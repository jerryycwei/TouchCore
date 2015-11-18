/**
 */
package ca.mcgill.sel.ucm;

import ca.mcgill.sel.core.CorePackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see ca.mcgill.sel.ucm.UCMFactory
 * @model kind="package"
 * @generated
 */
public interface UCMPackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "ucm";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://cs.mcgill.ca/sel/ucm/1.0";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "ucm";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    UCMPackage eINSTANCE = ca.mcgill.sel.ucm.impl.UCMPackageImpl.init();

    /**
     * The meta object id for the '{@link ca.mcgill.sel.ucm.impl.UCMModelElementImpl <em>Model Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see ca.mcgill.sel.ucm.impl.UCMModelElementImpl
     * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getUCMModelElement()
     * @generated
     */
    int UCM_MODEL_ELEMENT = 10;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UCM_MODEL_ELEMENT__NAME = CorePackage.CORE_MODEL_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Partiality</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UCM_MODEL_ELEMENT__PARTIALITY = CorePackage.CORE_MODEL_ELEMENT__PARTIALITY;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UCM_MODEL_ELEMENT__VISIBILITY = CorePackage.CORE_MODEL_ELEMENT__VISIBILITY;

    /**
     * The number of structural features of the '<em>Model Element</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int UCM_MODEL_ELEMENT_FEATURE_COUNT = CorePackage.CORE_MODEL_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link ca.mcgill.sel.ucm.impl.PathNodeImpl <em>Path Node</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see ca.mcgill.sel.ucm.impl.PathNodeImpl
     * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getPathNode()
     * @generated
     */
    int PATH_NODE = 5;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATH_NODE__NAME = UCM_MODEL_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Partiality</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATH_NODE__PARTIALITY = UCM_MODEL_ELEMENT__PARTIALITY;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATH_NODE__VISIBILITY = UCM_MODEL_ELEMENT__VISIBILITY;

    /**
     * The feature id for the '<em><b>Successors</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATH_NODE__SUCCESSORS = UCM_MODEL_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Predecessors</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATH_NODE__PREDECESSORS = UCM_MODEL_ELEMENT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Component Reference</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATH_NODE__COMPONENT_REFERENCE = UCM_MODEL_ELEMENT_FEATURE_COUNT + 2;

    /**
     * The number of structural features of the '<em>Path Node</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int PATH_NODE_FEATURE_COUNT = UCM_MODEL_ELEMENT_FEATURE_COUNT + 3;

    /**
     * The meta object id for the '{@link ca.mcgill.sel.ucm.impl.AndForkImpl <em>And Fork</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see ca.mcgill.sel.ucm.impl.AndForkImpl
     * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getAndFork()
     * @generated
     */
    int AND_FORK = 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AND_FORK__NAME = PATH_NODE__NAME;

    /**
     * The feature id for the '<em><b>Partiality</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AND_FORK__PARTIALITY = PATH_NODE__PARTIALITY;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AND_FORK__VISIBILITY = PATH_NODE__VISIBILITY;

    /**
     * The feature id for the '<em><b>Successors</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AND_FORK__SUCCESSORS = PATH_NODE__SUCCESSORS;

    /**
     * The feature id for the '<em><b>Predecessors</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AND_FORK__PREDECESSORS = PATH_NODE__PREDECESSORS;

    /**
     * The feature id for the '<em><b>Component Reference</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AND_FORK__COMPONENT_REFERENCE = PATH_NODE__COMPONENT_REFERENCE;

    /**
     * The number of structural features of the '<em>And Fork</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AND_FORK_FEATURE_COUNT = PATH_NODE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link ca.mcgill.sel.ucm.impl.AndJoinImpl <em>And Join</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see ca.mcgill.sel.ucm.impl.AndJoinImpl
     * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getAndJoin()
     * @generated
     */
    int AND_JOIN = 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AND_JOIN__NAME = PATH_NODE__NAME;

    /**
     * The feature id for the '<em><b>Partiality</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AND_JOIN__PARTIALITY = PATH_NODE__PARTIALITY;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AND_JOIN__VISIBILITY = PATH_NODE__VISIBILITY;

    /**
     * The feature id for the '<em><b>Successors</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AND_JOIN__SUCCESSORS = PATH_NODE__SUCCESSORS;

    /**
     * The feature id for the '<em><b>Predecessors</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AND_JOIN__PREDECESSORS = PATH_NODE__PREDECESSORS;

    /**
     * The feature id for the '<em><b>Component Reference</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AND_JOIN__COMPONENT_REFERENCE = PATH_NODE__COMPONENT_REFERENCE;

    /**
     * The number of structural features of the '<em>And Join</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int AND_JOIN_FEATURE_COUNT = PATH_NODE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link ca.mcgill.sel.ucm.impl.OrForkImpl <em>Or Fork</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see ca.mcgill.sel.ucm.impl.OrForkImpl
     * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getOrFork()
     * @generated
     */
    int OR_FORK = 2;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OR_FORK__NAME = PATH_NODE__NAME;

    /**
     * The feature id for the '<em><b>Partiality</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OR_FORK__PARTIALITY = PATH_NODE__PARTIALITY;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OR_FORK__VISIBILITY = PATH_NODE__VISIBILITY;

    /**
     * The feature id for the '<em><b>Successors</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OR_FORK__SUCCESSORS = PATH_NODE__SUCCESSORS;

    /**
     * The feature id for the '<em><b>Predecessors</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OR_FORK__PREDECESSORS = PATH_NODE__PREDECESSORS;

    /**
     * The feature id for the '<em><b>Component Reference</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OR_FORK__COMPONENT_REFERENCE = PATH_NODE__COMPONENT_REFERENCE;

    /**
     * The number of structural features of the '<em>Or Fork</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OR_FORK_FEATURE_COUNT = PATH_NODE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link ca.mcgill.sel.ucm.impl.OrJoinImpl <em>Or Join</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see ca.mcgill.sel.ucm.impl.OrJoinImpl
     * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getOrJoin()
     * @generated
     */
    int OR_JOIN = 3;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OR_JOIN__NAME = PATH_NODE__NAME;

    /**
     * The feature id for the '<em><b>Partiality</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OR_JOIN__PARTIALITY = PATH_NODE__PARTIALITY;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OR_JOIN__VISIBILITY = PATH_NODE__VISIBILITY;

    /**
     * The feature id for the '<em><b>Successors</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OR_JOIN__SUCCESSORS = PATH_NODE__SUCCESSORS;

    /**
     * The feature id for the '<em><b>Predecessors</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OR_JOIN__PREDECESSORS = PATH_NODE__PREDECESSORS;

    /**
     * The feature id for the '<em><b>Component Reference</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OR_JOIN__COMPONENT_REFERENCE = PATH_NODE__COMPONENT_REFERENCE;

    /**
     * The number of structural features of the '<em>Or Join</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int OR_JOIN_FEATURE_COUNT = PATH_NODE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link ca.mcgill.sel.ucm.impl.EmptyPointImpl <em>Empty Point</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see ca.mcgill.sel.ucm.impl.EmptyPointImpl
     * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getEmptyPoint()
     * @generated
     */
    int EMPTY_POINT = 4;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EMPTY_POINT__NAME = PATH_NODE__NAME;

    /**
     * The feature id for the '<em><b>Partiality</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EMPTY_POINT__PARTIALITY = PATH_NODE__PARTIALITY;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EMPTY_POINT__VISIBILITY = PATH_NODE__VISIBILITY;

    /**
     * The feature id for the '<em><b>Successors</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EMPTY_POINT__SUCCESSORS = PATH_NODE__SUCCESSORS;

    /**
     * The feature id for the '<em><b>Predecessors</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EMPTY_POINT__PREDECESSORS = PATH_NODE__PREDECESSORS;

    /**
     * The feature id for the '<em><b>Component Reference</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EMPTY_POINT__COMPONENT_REFERENCE = PATH_NODE__COMPONENT_REFERENCE;

    /**
     * The feature id for the '<em><b>Direction</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EMPTY_POINT__DIRECTION = PATH_NODE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Empty Point</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EMPTY_POINT_FEATURE_COUNT = PATH_NODE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link ca.mcgill.sel.ucm.impl.ResponsibilityRefImpl <em>Responsibility Ref</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see ca.mcgill.sel.ucm.impl.ResponsibilityRefImpl
     * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getResponsibilityRef()
     * @generated
     */
    int RESPONSIBILITY_REF = 6;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBILITY_REF__NAME = PATH_NODE__NAME;

    /**
     * The feature id for the '<em><b>Partiality</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBILITY_REF__PARTIALITY = PATH_NODE__PARTIALITY;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBILITY_REF__VISIBILITY = PATH_NODE__VISIBILITY;

    /**
     * The feature id for the '<em><b>Successors</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBILITY_REF__SUCCESSORS = PATH_NODE__SUCCESSORS;

    /**
     * The feature id for the '<em><b>Predecessors</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBILITY_REF__PREDECESSORS = PATH_NODE__PREDECESSORS;

    /**
     * The feature id for the '<em><b>Component Reference</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBILITY_REF__COMPONENT_REFERENCE = PATH_NODE__COMPONENT_REFERENCE;

    /**
     * The feature id for the '<em><b>Responsibility Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBILITY_REF__RESPONSIBILITY_DEFINITION = PATH_NODE_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Responsibility Ref</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBILITY_REF_FEATURE_COUNT = PATH_NODE_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link ca.mcgill.sel.ucm.impl.ResponsibilityImpl <em>Responsibility</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see ca.mcgill.sel.ucm.impl.ResponsibilityImpl
     * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getResponsibility()
     * @generated
     */
    int RESPONSIBILITY = 7;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBILITY__NAME = UCM_MODEL_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Partiality</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBILITY__PARTIALITY = UCM_MODEL_ELEMENT__PARTIALITY;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBILITY__VISIBILITY = UCM_MODEL_ELEMENT__VISIBILITY;

    /**
     * The feature id for the '<em><b>Responsibility References</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBILITY__RESPONSIBILITY_REFERENCES = UCM_MODEL_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>Responsibility</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int RESPONSIBILITY_FEATURE_COUNT = UCM_MODEL_ELEMENT_FEATURE_COUNT + 1;

    /**
     * The meta object id for the '{@link ca.mcgill.sel.ucm.impl.StartPointImpl <em>Start Point</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see ca.mcgill.sel.ucm.impl.StartPointImpl
     * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getStartPoint()
     * @generated
     */
    int START_POINT = 8;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int START_POINT__NAME = PATH_NODE__NAME;

    /**
     * The feature id for the '<em><b>Partiality</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int START_POINT__PARTIALITY = PATH_NODE__PARTIALITY;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int START_POINT__VISIBILITY = PATH_NODE__VISIBILITY;

    /**
     * The feature id for the '<em><b>Successors</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int START_POINT__SUCCESSORS = PATH_NODE__SUCCESSORS;

    /**
     * The feature id for the '<em><b>Predecessors</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int START_POINT__PREDECESSORS = PATH_NODE__PREDECESSORS;

    /**
     * The feature id for the '<em><b>Component Reference</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int START_POINT__COMPONENT_REFERENCE = PATH_NODE__COMPONENT_REFERENCE;

    /**
     * The number of structural features of the '<em>Start Point</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int START_POINT_FEATURE_COUNT = PATH_NODE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link ca.mcgill.sel.ucm.impl.NodeConnectionImpl <em>Node Connection</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see ca.mcgill.sel.ucm.impl.NodeConnectionImpl
     * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getNodeConnection()
     * @generated
     */
    int NODE_CONNECTION = 9;

    /**
     * The feature id for the '<em><b>Condition</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NODE_CONNECTION__CONDITION = 0;

    /**
     * The feature id for the '<em><b>Source</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NODE_CONNECTION__SOURCE = 1;

    /**
     * The feature id for the '<em><b>Target</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NODE_CONNECTION__TARGET = 2;

    /**
     * The number of structural features of the '<em>Node Connection</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NODE_CONNECTION_FEATURE_COUNT = 3;

    /**
     * The meta object id for the '{@link ca.mcgill.sel.ucm.impl.UseCaseMapImpl <em>Use Case Map</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see ca.mcgill.sel.ucm.impl.UseCaseMapImpl
     * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getUseCaseMap()
     * @generated
     */
    int USE_CASE_MAP = 11;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USE_CASE_MAP__NAME = CorePackage.CORE_MODEL__NAME;

    /**
     * The feature id for the '<em><b>Model Reuses</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USE_CASE_MAP__MODEL_REUSES = CorePackage.CORE_MODEL__MODEL_REUSES;

    /**
     * The feature id for the '<em><b>Model Elements</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USE_CASE_MAP__MODEL_ELEMENTS = CorePackage.CORE_MODEL__MODEL_ELEMENTS;

    /**
     * The feature id for the '<em><b>Realizes</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USE_CASE_MAP__REALIZES = CorePackage.CORE_MODEL__REALIZES;

    /**
     * The feature id for the '<em><b>Core Concern</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USE_CASE_MAP__CORE_CONCERN = CorePackage.CORE_MODEL__CORE_CONCERN;

    /**
     * The feature id for the '<em><b>Connections</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USE_CASE_MAP__CONNECTIONS = CorePackage.CORE_MODEL_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Nodes</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USE_CASE_MAP__NODES = CorePackage.CORE_MODEL_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Responsibilities</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USE_CASE_MAP__RESPONSIBILITIES = CorePackage.CORE_MODEL_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Components</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USE_CASE_MAP__COMPONENTS = CorePackage.CORE_MODEL_FEATURE_COUNT + 3;

    /**
     * The feature id for the '<em><b>Component References</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USE_CASE_MAP__COMPONENT_REFERENCES = CorePackage.CORE_MODEL_FEATURE_COUNT + 4;

    /**
     * The feature id for the '<em><b>Layout Maps</b></em>' map.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USE_CASE_MAP__LAYOUT_MAPS = CorePackage.CORE_MODEL_FEATURE_COUNT + 5;

    /**
     * The number of structural features of the '<em>Use Case Map</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int USE_CASE_MAP_FEATURE_COUNT = CorePackage.CORE_MODEL_FEATURE_COUNT + 6;

    /**
     * The meta object id for the '{@link ca.mcgill.sel.ucm.impl.ComponentReferenceImpl <em>Component Reference</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see ca.mcgill.sel.ucm.impl.ComponentReferenceImpl
     * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getComponentReference()
     * @generated
     */
    int COMPONENT_REFERENCE = 12;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_REFERENCE__NAME = UCM_MODEL_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Partiality</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_REFERENCE__PARTIALITY = UCM_MODEL_ELEMENT__PARTIALITY;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_REFERENCE__VISIBILITY = UCM_MODEL_ELEMENT__VISIBILITY;

    /**
     * The feature id for the '<em><b>Component Definition</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_REFERENCE__COMPONENT_DEFINITION = UCM_MODEL_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Children</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_REFERENCE__CHILDREN = UCM_MODEL_ELEMENT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Parent</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_REFERENCE__PARENT = UCM_MODEL_ELEMENT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Nodes</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_REFERENCE__NODES = UCM_MODEL_ELEMENT_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Component Reference</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_REFERENCE_FEATURE_COUNT = UCM_MODEL_ELEMENT_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link ca.mcgill.sel.ucm.impl.ComponentImpl <em>Component</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see ca.mcgill.sel.ucm.impl.ComponentImpl
     * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getComponent()
     * @generated
     */
    int COMPONENT = 13;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT__NAME = UCM_MODEL_ELEMENT__NAME;

    /**
     * The feature id for the '<em><b>Partiality</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT__PARTIALITY = UCM_MODEL_ELEMENT__PARTIALITY;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT__VISIBILITY = UCM_MODEL_ELEMENT__VISIBILITY;

    /**
     * The feature id for the '<em><b>Component References</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT__COMPONENT_REFERENCES = UCM_MODEL_ELEMENT_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Children</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT__CHILDREN = UCM_MODEL_ELEMENT_FEATURE_COUNT + 1;

    /**
     * The feature id for the '<em><b>Parents</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT__PARENTS = UCM_MODEL_ELEMENT_FEATURE_COUNT + 2;

    /**
     * The feature id for the '<em><b>Kind</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT__KIND = UCM_MODEL_ELEMENT_FEATURE_COUNT + 3;

    /**
     * The number of structural features of the '<em>Component</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int COMPONENT_FEATURE_COUNT = UCM_MODEL_ELEMENT_FEATURE_COUNT + 4;

    /**
     * The meta object id for the '{@link ca.mcgill.sel.ucm.impl.ConditionImpl <em>Condition</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see ca.mcgill.sel.ucm.impl.ConditionImpl
     * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getCondition()
     * @generated
     */
    int CONDITION = 14;

    /**
     * The feature id for the '<em><b>Expression</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONDITION__EXPRESSION = 0;

    /**
     * The number of structural features of the '<em>Condition</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int CONDITION_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link ca.mcgill.sel.ucm.impl.EndPointImpl <em>End Point</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see ca.mcgill.sel.ucm.impl.EndPointImpl
     * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getEndPoint()
     * @generated
     */
    int END_POINT = 15;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int END_POINT__NAME = PATH_NODE__NAME;

    /**
     * The feature id for the '<em><b>Partiality</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int END_POINT__PARTIALITY = PATH_NODE__PARTIALITY;

    /**
     * The feature id for the '<em><b>Visibility</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int END_POINT__VISIBILITY = PATH_NODE__VISIBILITY;

    /**
     * The feature id for the '<em><b>Successors</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int END_POINT__SUCCESSORS = PATH_NODE__SUCCESSORS;

    /**
     * The feature id for the '<em><b>Predecessors</b></em>' reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int END_POINT__PREDECESSORS = PATH_NODE__PREDECESSORS;

    /**
     * The feature id for the '<em><b>Component Reference</b></em>' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int END_POINT__COMPONENT_REFERENCE = PATH_NODE__COMPONENT_REFERENCE;

    /**
     * The number of structural features of the '<em>End Point</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int END_POINT_FEATURE_COUNT = PATH_NODE_FEATURE_COUNT + 0;

    /**
     * The meta object id for the '{@link ca.mcgill.sel.ucm.ComponentKind <em>Component Kind</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see ca.mcgill.sel.ucm.ComponentKind
     * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getComponentKind()
     * @generated
     */
    int COMPONENT_KIND = 16;


    /**
     * Returns the meta object for class '{@link ca.mcgill.sel.ucm.AndFork <em>And Fork</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>And Fork</em>'.
     * @see ca.mcgill.sel.ucm.AndFork
     * @generated
     */
    EClass getAndFork();

    /**
     * Returns the meta object for class '{@link ca.mcgill.sel.ucm.AndJoin <em>And Join</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>And Join</em>'.
     * @see ca.mcgill.sel.ucm.AndJoin
     * @generated
     */
    EClass getAndJoin();

    /**
     * Returns the meta object for class '{@link ca.mcgill.sel.ucm.OrFork <em>Or Fork</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Or Fork</em>'.
     * @see ca.mcgill.sel.ucm.OrFork
     * @generated
     */
    EClass getOrFork();

    /**
     * Returns the meta object for class '{@link ca.mcgill.sel.ucm.OrJoin <em>Or Join</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Or Join</em>'.
     * @see ca.mcgill.sel.ucm.OrJoin
     * @generated
     */
    EClass getOrJoin();

    /**
     * Returns the meta object for class '{@link ca.mcgill.sel.ucm.EmptyPoint <em>Empty Point</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Empty Point</em>'.
     * @see ca.mcgill.sel.ucm.EmptyPoint
     * @generated
     */
    EClass getEmptyPoint();

    /**
     * Returns the meta object for the attribute '{@link ca.mcgill.sel.ucm.EmptyPoint#isDirection <em>Direction</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Direction</em>'.
     * @see ca.mcgill.sel.ucm.EmptyPoint#isDirection()
     * @see #getEmptyPoint()
     * @generated
     */
    EAttribute getEmptyPoint_Direction();

    /**
     * Returns the meta object for class '{@link ca.mcgill.sel.ucm.PathNode <em>Path Node</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Path Node</em>'.
     * @see ca.mcgill.sel.ucm.PathNode
     * @generated
     */
    EClass getPathNode();

    /**
     * Returns the meta object for the reference list '{@link ca.mcgill.sel.ucm.PathNode#getSuccessors <em>Successors</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Successors</em>'.
     * @see ca.mcgill.sel.ucm.PathNode#getSuccessors()
     * @see #getPathNode()
     * @generated
     */
    EReference getPathNode_Successors();

    /**
     * Returns the meta object for the reference list '{@link ca.mcgill.sel.ucm.PathNode#getPredecessors <em>Predecessors</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Predecessors</em>'.
     * @see ca.mcgill.sel.ucm.PathNode#getPredecessors()
     * @see #getPathNode()
     * @generated
     */
    EReference getPathNode_Predecessors();

    /**
     * Returns the meta object for the reference '{@link ca.mcgill.sel.ucm.PathNode#getComponentReference <em>Component Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Component Reference</em>'.
     * @see ca.mcgill.sel.ucm.PathNode#getComponentReference()
     * @see #getPathNode()
     * @generated
     */
    EReference getPathNode_ComponentReference();

    /**
     * Returns the meta object for class '{@link ca.mcgill.sel.ucm.ResponsibilityRef <em>Responsibility Ref</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Responsibility Ref</em>'.
     * @see ca.mcgill.sel.ucm.ResponsibilityRef
     * @generated
     */
    EClass getResponsibilityRef();

    /**
     * Returns the meta object for the reference '{@link ca.mcgill.sel.ucm.ResponsibilityRef#getResponsibilityDefinition <em>Responsibility Definition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Responsibility Definition</em>'.
     * @see ca.mcgill.sel.ucm.ResponsibilityRef#getResponsibilityDefinition()
     * @see #getResponsibilityRef()
     * @generated
     */
    EReference getResponsibilityRef_ResponsibilityDefinition();

    /**
     * Returns the meta object for class '{@link ca.mcgill.sel.ucm.Responsibility <em>Responsibility</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Responsibility</em>'.
     * @see ca.mcgill.sel.ucm.Responsibility
     * @generated
     */
    EClass getResponsibility();

    /**
     * Returns the meta object for the reference list '{@link ca.mcgill.sel.ucm.Responsibility#getResponsibilityReferences <em>Responsibility References</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Responsibility References</em>'.
     * @see ca.mcgill.sel.ucm.Responsibility#getResponsibilityReferences()
     * @see #getResponsibility()
     * @generated
     */
    EReference getResponsibility_ResponsibilityReferences();

    /**
     * Returns the meta object for class '{@link ca.mcgill.sel.ucm.StartPoint <em>Start Point</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Start Point</em>'.
     * @see ca.mcgill.sel.ucm.StartPoint
     * @generated
     */
    EClass getStartPoint();

    /**
     * Returns the meta object for class '{@link ca.mcgill.sel.ucm.NodeConnection <em>Node Connection</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Node Connection</em>'.
     * @see ca.mcgill.sel.ucm.NodeConnection
     * @generated
     */
    EClass getNodeConnection();

    /**
     * Returns the meta object for the containment reference '{@link ca.mcgill.sel.ucm.NodeConnection#getCondition <em>Condition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference '<em>Condition</em>'.
     * @see ca.mcgill.sel.ucm.NodeConnection#getCondition()
     * @see #getNodeConnection()
     * @generated
     */
    EReference getNodeConnection_Condition();

    /**
     * Returns the meta object for the reference '{@link ca.mcgill.sel.ucm.NodeConnection#getSource <em>Source</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Source</em>'.
     * @see ca.mcgill.sel.ucm.NodeConnection#getSource()
     * @see #getNodeConnection()
     * @generated
     */
    EReference getNodeConnection_Source();

    /**
     * Returns the meta object for the reference '{@link ca.mcgill.sel.ucm.NodeConnection#getTarget <em>Target</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Target</em>'.
     * @see ca.mcgill.sel.ucm.NodeConnection#getTarget()
     * @see #getNodeConnection()
     * @generated
     */
    EReference getNodeConnection_Target();

    /**
     * Returns the meta object for class '{@link ca.mcgill.sel.ucm.UCMModelElement <em>Model Element</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Model Element</em>'.
     * @see ca.mcgill.sel.ucm.UCMModelElement
     * @generated
     */
    EClass getUCMModelElement();

    /**
     * Returns the meta object for class '{@link ca.mcgill.sel.ucm.UseCaseMap <em>Use Case Map</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Use Case Map</em>'.
     * @see ca.mcgill.sel.ucm.UseCaseMap
     * @generated
     */
    EClass getUseCaseMap();

    /**
     * Returns the meta object for the containment reference list '{@link ca.mcgill.sel.ucm.UseCaseMap#getConnections <em>Connections</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Connections</em>'.
     * @see ca.mcgill.sel.ucm.UseCaseMap#getConnections()
     * @see #getUseCaseMap()
     * @generated
     */
    EReference getUseCaseMap_Connections();

    /**
     * Returns the meta object for the containment reference list '{@link ca.mcgill.sel.ucm.UseCaseMap#getNodes <em>Nodes</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Nodes</em>'.
     * @see ca.mcgill.sel.ucm.UseCaseMap#getNodes()
     * @see #getUseCaseMap()
     * @generated
     */
    EReference getUseCaseMap_Nodes();

    /**
     * Returns the meta object for the containment reference list '{@link ca.mcgill.sel.ucm.UseCaseMap#getResponsibilities <em>Responsibilities</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Responsibilities</em>'.
     * @see ca.mcgill.sel.ucm.UseCaseMap#getResponsibilities()
     * @see #getUseCaseMap()
     * @generated
     */
    EReference getUseCaseMap_Responsibilities();

    /**
     * Returns the meta object for the containment reference list '{@link ca.mcgill.sel.ucm.UseCaseMap#getComponents <em>Components</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Components</em>'.
     * @see ca.mcgill.sel.ucm.UseCaseMap#getComponents()
     * @see #getUseCaseMap()
     * @generated
     */
    EReference getUseCaseMap_Components();

    /**
     * Returns the meta object for the containment reference list '{@link ca.mcgill.sel.ucm.UseCaseMap#getComponentReferences <em>Component References</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Component References</em>'.
     * @see ca.mcgill.sel.ucm.UseCaseMap#getComponentReferences()
     * @see #getUseCaseMap()
     * @generated
     */
    EReference getUseCaseMap_ComponentReferences();

    /**
     * Returns the meta object for the map '{@link ca.mcgill.sel.ucm.UseCaseMap#getLayoutMaps <em>Layout Maps</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the map '<em>Layout Maps</em>'.
     * @see ca.mcgill.sel.ucm.UseCaseMap#getLayoutMaps()
     * @see #getUseCaseMap()
     * @generated
     */
    EReference getUseCaseMap_LayoutMaps();

    /**
     * Returns the meta object for class '{@link ca.mcgill.sel.ucm.ComponentReference <em>Component Reference</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Component Reference</em>'.
     * @see ca.mcgill.sel.ucm.ComponentReference
     * @generated
     */
    EClass getComponentReference();

    /**
     * Returns the meta object for the reference '{@link ca.mcgill.sel.ucm.ComponentReference#getComponentDefinition <em>Component Definition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Component Definition</em>'.
     * @see ca.mcgill.sel.ucm.ComponentReference#getComponentDefinition()
     * @see #getComponentReference()
     * @generated
     */
    EReference getComponentReference_ComponentDefinition();

    /**
     * Returns the meta object for the reference list '{@link ca.mcgill.sel.ucm.ComponentReference#getChildren <em>Children</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Children</em>'.
     * @see ca.mcgill.sel.ucm.ComponentReference#getChildren()
     * @see #getComponentReference()
     * @generated
     */
    EReference getComponentReference_Children();

    /**
     * Returns the meta object for the reference '{@link ca.mcgill.sel.ucm.ComponentReference#getParent <em>Parent</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference '<em>Parent</em>'.
     * @see ca.mcgill.sel.ucm.ComponentReference#getParent()
     * @see #getComponentReference()
     * @generated
     */
    EReference getComponentReference_Parent();

    /**
     * Returns the meta object for the reference list '{@link ca.mcgill.sel.ucm.ComponentReference#getNodes <em>Nodes</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Nodes</em>'.
     * @see ca.mcgill.sel.ucm.ComponentReference#getNodes()
     * @see #getComponentReference()
     * @generated
     */
    EReference getComponentReference_Nodes();

    /**
     * Returns the meta object for class '{@link ca.mcgill.sel.ucm.Component <em>Component</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Component</em>'.
     * @see ca.mcgill.sel.ucm.Component
     * @generated
     */
    EClass getComponent();

    /**
     * Returns the meta object for the reference list '{@link ca.mcgill.sel.ucm.Component#getComponentReferences <em>Component References</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Component References</em>'.
     * @see ca.mcgill.sel.ucm.Component#getComponentReferences()
     * @see #getComponent()
     * @generated
     */
    EReference getComponent_ComponentReferences();

    /**
     * Returns the meta object for the reference list '{@link ca.mcgill.sel.ucm.Component#getChildren <em>Children</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Children</em>'.
     * @see ca.mcgill.sel.ucm.Component#getChildren()
     * @see #getComponent()
     * @generated
     */
    EReference getComponent_Children();

    /**
     * Returns the meta object for the reference list '{@link ca.mcgill.sel.ucm.Component#getParents <em>Parents</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the reference list '<em>Parents</em>'.
     * @see ca.mcgill.sel.ucm.Component#getParents()
     * @see #getComponent()
     * @generated
     */
    EReference getComponent_Parents();

    /**
     * Returns the meta object for the attribute '{@link ca.mcgill.sel.ucm.Component#getKind <em>Kind</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Kind</em>'.
     * @see ca.mcgill.sel.ucm.Component#getKind()
     * @see #getComponent()
     * @generated
     */
    EAttribute getComponent_Kind();

    /**
     * Returns the meta object for class '{@link ca.mcgill.sel.ucm.Condition <em>Condition</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Condition</em>'.
     * @see ca.mcgill.sel.ucm.Condition
     * @generated
     */
    EClass getCondition();

    /**
     * Returns the meta object for the attribute '{@link ca.mcgill.sel.ucm.Condition#getExpression <em>Expression</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Expression</em>'.
     * @see ca.mcgill.sel.ucm.Condition#getExpression()
     * @see #getCondition()
     * @generated
     */
    EAttribute getCondition_Expression();

    /**
     * Returns the meta object for class '{@link ca.mcgill.sel.ucm.EndPoint <em>End Point</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>End Point</em>'.
     * @see ca.mcgill.sel.ucm.EndPoint
     * @generated
     */
    EClass getEndPoint();

    /**
     * Returns the meta object for enum '{@link ca.mcgill.sel.ucm.ComponentKind <em>Component Kind</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for enum '<em>Component Kind</em>'.
     * @see ca.mcgill.sel.ucm.ComponentKind
     * @generated
     */
    EEnum getComponentKind();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    UCMFactory getUCMFactory();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals {
        /**
         * The meta object literal for the '{@link ca.mcgill.sel.ucm.impl.AndForkImpl <em>And Fork</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see ca.mcgill.sel.ucm.impl.AndForkImpl
         * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getAndFork()
         * @generated
         */
        EClass AND_FORK = eINSTANCE.getAndFork();

        /**
         * The meta object literal for the '{@link ca.mcgill.sel.ucm.impl.AndJoinImpl <em>And Join</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see ca.mcgill.sel.ucm.impl.AndJoinImpl
         * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getAndJoin()
         * @generated
         */
        EClass AND_JOIN = eINSTANCE.getAndJoin();

        /**
         * The meta object literal for the '{@link ca.mcgill.sel.ucm.impl.OrForkImpl <em>Or Fork</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see ca.mcgill.sel.ucm.impl.OrForkImpl
         * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getOrFork()
         * @generated
         */
        EClass OR_FORK = eINSTANCE.getOrFork();

        /**
         * The meta object literal for the '{@link ca.mcgill.sel.ucm.impl.OrJoinImpl <em>Or Join</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see ca.mcgill.sel.ucm.impl.OrJoinImpl
         * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getOrJoin()
         * @generated
         */
        EClass OR_JOIN = eINSTANCE.getOrJoin();

        /**
         * The meta object literal for the '{@link ca.mcgill.sel.ucm.impl.EmptyPointImpl <em>Empty Point</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see ca.mcgill.sel.ucm.impl.EmptyPointImpl
         * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getEmptyPoint()
         * @generated
         */
        EClass EMPTY_POINT = eINSTANCE.getEmptyPoint();

        /**
         * The meta object literal for the '<em><b>Direction</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EMPTY_POINT__DIRECTION = eINSTANCE.getEmptyPoint_Direction();

        /**
         * The meta object literal for the '{@link ca.mcgill.sel.ucm.impl.PathNodeImpl <em>Path Node</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see ca.mcgill.sel.ucm.impl.PathNodeImpl
         * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getPathNode()
         * @generated
         */
        EClass PATH_NODE = eINSTANCE.getPathNode();

        /**
         * The meta object literal for the '<em><b>Successors</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PATH_NODE__SUCCESSORS = eINSTANCE.getPathNode_Successors();

        /**
         * The meta object literal for the '<em><b>Predecessors</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PATH_NODE__PREDECESSORS = eINSTANCE.getPathNode_Predecessors();

        /**
         * The meta object literal for the '<em><b>Component Reference</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference PATH_NODE__COMPONENT_REFERENCE = eINSTANCE.getPathNode_ComponentReference();

        /**
         * The meta object literal for the '{@link ca.mcgill.sel.ucm.impl.ResponsibilityRefImpl <em>Responsibility Ref</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see ca.mcgill.sel.ucm.impl.ResponsibilityRefImpl
         * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getResponsibilityRef()
         * @generated
         */
        EClass RESPONSIBILITY_REF = eINSTANCE.getResponsibilityRef();

        /**
         * The meta object literal for the '<em><b>Responsibility Definition</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RESPONSIBILITY_REF__RESPONSIBILITY_DEFINITION = eINSTANCE.getResponsibilityRef_ResponsibilityDefinition();

        /**
         * The meta object literal for the '{@link ca.mcgill.sel.ucm.impl.ResponsibilityImpl <em>Responsibility</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see ca.mcgill.sel.ucm.impl.ResponsibilityImpl
         * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getResponsibility()
         * @generated
         */
        EClass RESPONSIBILITY = eINSTANCE.getResponsibility();

        /**
         * The meta object literal for the '<em><b>Responsibility References</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference RESPONSIBILITY__RESPONSIBILITY_REFERENCES = eINSTANCE.getResponsibility_ResponsibilityReferences();

        /**
         * The meta object literal for the '{@link ca.mcgill.sel.ucm.impl.StartPointImpl <em>Start Point</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see ca.mcgill.sel.ucm.impl.StartPointImpl
         * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getStartPoint()
         * @generated
         */
        EClass START_POINT = eINSTANCE.getStartPoint();

        /**
         * The meta object literal for the '{@link ca.mcgill.sel.ucm.impl.NodeConnectionImpl <em>Node Connection</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see ca.mcgill.sel.ucm.impl.NodeConnectionImpl
         * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getNodeConnection()
         * @generated
         */
        EClass NODE_CONNECTION = eINSTANCE.getNodeConnection();

        /**
         * The meta object literal for the '<em><b>Condition</b></em>' containment reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference NODE_CONNECTION__CONDITION = eINSTANCE.getNodeConnection_Condition();

        /**
         * The meta object literal for the '<em><b>Source</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference NODE_CONNECTION__SOURCE = eINSTANCE.getNodeConnection_Source();

        /**
         * The meta object literal for the '<em><b>Target</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference NODE_CONNECTION__TARGET = eINSTANCE.getNodeConnection_Target();

        /**
         * The meta object literal for the '{@link ca.mcgill.sel.ucm.impl.UCMModelElementImpl <em>Model Element</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see ca.mcgill.sel.ucm.impl.UCMModelElementImpl
         * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getUCMModelElement()
         * @generated
         */
        EClass UCM_MODEL_ELEMENT = eINSTANCE.getUCMModelElement();

        /**
         * The meta object literal for the '{@link ca.mcgill.sel.ucm.impl.UseCaseMapImpl <em>Use Case Map</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see ca.mcgill.sel.ucm.impl.UseCaseMapImpl
         * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getUseCaseMap()
         * @generated
         */
        EClass USE_CASE_MAP = eINSTANCE.getUseCaseMap();

        /**
         * The meta object literal for the '<em><b>Connections</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference USE_CASE_MAP__CONNECTIONS = eINSTANCE.getUseCaseMap_Connections();

        /**
         * The meta object literal for the '<em><b>Nodes</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference USE_CASE_MAP__NODES = eINSTANCE.getUseCaseMap_Nodes();

        /**
         * The meta object literal for the '<em><b>Responsibilities</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference USE_CASE_MAP__RESPONSIBILITIES = eINSTANCE.getUseCaseMap_Responsibilities();

        /**
         * The meta object literal for the '<em><b>Components</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference USE_CASE_MAP__COMPONENTS = eINSTANCE.getUseCaseMap_Components();

        /**
         * The meta object literal for the '<em><b>Component References</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference USE_CASE_MAP__COMPONENT_REFERENCES = eINSTANCE.getUseCaseMap_ComponentReferences();

        /**
         * The meta object literal for the '<em><b>Layout Maps</b></em>' map feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference USE_CASE_MAP__LAYOUT_MAPS = eINSTANCE.getUseCaseMap_LayoutMaps();

        /**
         * The meta object literal for the '{@link ca.mcgill.sel.ucm.impl.ComponentReferenceImpl <em>Component Reference</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see ca.mcgill.sel.ucm.impl.ComponentReferenceImpl
         * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getComponentReference()
         * @generated
         */
        EClass COMPONENT_REFERENCE = eINSTANCE.getComponentReference();

        /**
         * The meta object literal for the '<em><b>Component Definition</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPONENT_REFERENCE__COMPONENT_DEFINITION = eINSTANCE.getComponentReference_ComponentDefinition();

        /**
         * The meta object literal for the '<em><b>Children</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPONENT_REFERENCE__CHILDREN = eINSTANCE.getComponentReference_Children();

        /**
         * The meta object literal for the '<em><b>Parent</b></em>' reference feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPONENT_REFERENCE__PARENT = eINSTANCE.getComponentReference_Parent();

        /**
         * The meta object literal for the '<em><b>Nodes</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPONENT_REFERENCE__NODES = eINSTANCE.getComponentReference_Nodes();

        /**
         * The meta object literal for the '{@link ca.mcgill.sel.ucm.impl.ComponentImpl <em>Component</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see ca.mcgill.sel.ucm.impl.ComponentImpl
         * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getComponent()
         * @generated
         */
        EClass COMPONENT = eINSTANCE.getComponent();

        /**
         * The meta object literal for the '<em><b>Component References</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPONENT__COMPONENT_REFERENCES = eINSTANCE.getComponent_ComponentReferences();

        /**
         * The meta object literal for the '<em><b>Children</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPONENT__CHILDREN = eINSTANCE.getComponent_Children();

        /**
         * The meta object literal for the '<em><b>Parents</b></em>' reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference COMPONENT__PARENTS = eINSTANCE.getComponent_Parents();

        /**
         * The meta object literal for the '<em><b>Kind</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute COMPONENT__KIND = eINSTANCE.getComponent_Kind();

        /**
         * The meta object literal for the '{@link ca.mcgill.sel.ucm.impl.ConditionImpl <em>Condition</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see ca.mcgill.sel.ucm.impl.ConditionImpl
         * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getCondition()
         * @generated
         */
        EClass CONDITION = eINSTANCE.getCondition();

        /**
         * The meta object literal for the '<em><b>Expression</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute CONDITION__EXPRESSION = eINSTANCE.getCondition_Expression();

        /**
         * The meta object literal for the '{@link ca.mcgill.sel.ucm.impl.EndPointImpl <em>End Point</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see ca.mcgill.sel.ucm.impl.EndPointImpl
         * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getEndPoint()
         * @generated
         */
        EClass END_POINT = eINSTANCE.getEndPoint();

        /**
         * The meta object literal for the '{@link ca.mcgill.sel.ucm.ComponentKind <em>Component Kind</em>}' enum.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see ca.mcgill.sel.ucm.ComponentKind
         * @see ca.mcgill.sel.ucm.impl.UCMPackageImpl#getComponentKind()
         * @generated
         */
        EEnum COMPONENT_KIND = eINSTANCE.getComponentKind();

    }

} //UCMPackage
