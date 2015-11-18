/**
 */
package ca.mcgill.sel.ucm.impl;

import ca.mcgill.sel.ucm.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class UCMFactoryImpl extends EFactoryImpl implements UCMFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static UCMFactory init() {
        try {
            UCMFactory theUCMFactory = (UCMFactory)EPackage.Registry.INSTANCE.getEFactory(UCMPackage.eNS_URI);
            if (theUCMFactory != null) {
                return theUCMFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new UCMFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UCMFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case UCMPackage.AND_FORK: return createAndFork();
            case UCMPackage.AND_JOIN: return createAndJoin();
            case UCMPackage.OR_FORK: return createOrFork();
            case UCMPackage.OR_JOIN: return createOrJoin();
            case UCMPackage.EMPTY_POINT: return createEmptyPoint();
            case UCMPackage.PATH_NODE: return createPathNode();
            case UCMPackage.RESPONSIBILITY_REF: return createResponsibilityRef();
            case UCMPackage.RESPONSIBILITY: return createResponsibility();
            case UCMPackage.START_POINT: return createStartPoint();
            case UCMPackage.NODE_CONNECTION: return createNodeConnection();
            case UCMPackage.UCM_MODEL_ELEMENT: return createUCMModelElement();
            case UCMPackage.USE_CASE_MAP: return createUseCaseMap();
            case UCMPackage.COMPONENT_REFERENCE: return createComponentReference();
            case UCMPackage.COMPONENT: return createComponent();
            case UCMPackage.CONDITION: return createCondition();
            case UCMPackage.END_POINT: return createEndPoint();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object createFromString(EDataType eDataType, String initialValue) {
        switch (eDataType.getClassifierID()) {
            case UCMPackage.COMPONENT_KIND:
                return createComponentKindFromString(eDataType, initialValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String convertToString(EDataType eDataType, Object instanceValue) {
        switch (eDataType.getClassifierID()) {
            case UCMPackage.COMPONENT_KIND:
                return convertComponentKindToString(eDataType, instanceValue);
            default:
                throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AndFork createAndFork() {
        AndForkImpl andFork = new AndForkImpl();
        return andFork;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public AndJoin createAndJoin() {
        AndJoinImpl andJoin = new AndJoinImpl();
        return andJoin;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OrFork createOrFork() {
        OrForkImpl orFork = new OrForkImpl();
        return orFork;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public OrJoin createOrJoin() {
        OrJoinImpl orJoin = new OrJoinImpl();
        return orJoin;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EmptyPoint createEmptyPoint() {
        EmptyPointImpl emptyPoint = new EmptyPointImpl();
        return emptyPoint;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public PathNode createPathNode() {
        PathNodeImpl pathNode = new PathNodeImpl();
        return pathNode;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ResponsibilityRef createResponsibilityRef() {
        ResponsibilityRefImpl responsibilityRef = new ResponsibilityRefImpl();
        return responsibilityRef;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Responsibility createResponsibility() {
        ResponsibilityImpl responsibility = new ResponsibilityImpl();
        return responsibility;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public StartPoint createStartPoint() {
        StartPointImpl startPoint = new StartPointImpl();
        return startPoint;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NodeConnection createNodeConnection() {
        NodeConnectionImpl nodeConnection = new NodeConnectionImpl();
        return nodeConnection;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UCMModelElement createUCMModelElement() {
        UCMModelElementImpl ucmModelElement = new UCMModelElementImpl();
        return ucmModelElement;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UseCaseMap createUseCaseMap() {
        UseCaseMapImpl useCaseMap = new UseCaseMapImpl();
        return useCaseMap;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComponentReference createComponentReference() {
        ComponentReferenceImpl componentReference = new ComponentReferenceImpl();
        return componentReference;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Component createComponent() {
        ComponentImpl component = new ComponentImpl();
        return component;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public Condition createCondition() {
        ConditionImpl condition = new ConditionImpl();
        return condition;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EndPoint createEndPoint() {
        EndPointImpl endPoint = new EndPointImpl();
        return endPoint;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ComponentKind createComponentKindFromString(EDataType eDataType, String initialValue) {
        ComponentKind result = ComponentKind.get(initialValue);
        if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String convertComponentKindToString(EDataType eDataType, Object instanceValue) {
        return instanceValue == null ? null : instanceValue.toString();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UCMPackage getUCMPackage() {
        return (UCMPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static UCMPackage getPackage() {
        return UCMPackage.eINSTANCE;
    }

} //UCMFactoryImpl
