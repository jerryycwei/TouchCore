/**
 */
package ca.mcgill.sel.ucm.impl;

import ca.mcgill.sel.core.CorePackage;

import ca.mcgill.sel.ucm.AndFork;
import ca.mcgill.sel.ucm.AndJoin;
import ca.mcgill.sel.ucm.Component;
import ca.mcgill.sel.ucm.ComponentKind;
import ca.mcgill.sel.ucm.ComponentReference;
import ca.mcgill.sel.ucm.Condition;
import ca.mcgill.sel.ucm.EmptyPoint;
import ca.mcgill.sel.ucm.EndPoint;
import ca.mcgill.sel.ucm.NodeConnection;
import ca.mcgill.sel.ucm.OrFork;
import ca.mcgill.sel.ucm.OrJoin;
import ca.mcgill.sel.ucm.PathNode;
import ca.mcgill.sel.ucm.Responsibility;
import ca.mcgill.sel.ucm.ResponsibilityRef;
import ca.mcgill.sel.ucm.StartPoint;
import ca.mcgill.sel.ucm.UCMFactory;
import ca.mcgill.sel.ucm.UCMModelElement;
import ca.mcgill.sel.ucm.UCMPackage;
import ca.mcgill.sel.ucm.UseCaseMap;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class UCMPackageImpl extends EPackageImpl implements UCMPackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass andForkEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass andJoinEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass orForkEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass orJoinEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass emptyPointEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass pathNodeEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass responsibilityRefEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass responsibilityEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass startPointEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass nodeConnectionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass ucmModelElementEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass useCaseMapEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass componentReferenceEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass componentEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass conditionEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass endPointEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EEnum componentKindEEnum = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
     * package URI value.
     * <p>Note: the correct way to create the package is via the static
     * factory method {@link #init init()}, which also performs
     * initialization of the package, or returns the registered package,
     * if one already exists.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see ca.mcgill.sel.ucm.UCMPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private UCMPackageImpl() {
        super(eNS_URI, UCMFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     * 
     * <p>This method is used to initialize {@link UCMPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static UCMPackage init() {
        if (isInited) return (UCMPackage)EPackage.Registry.INSTANCE.getEPackage(UCMPackage.eNS_URI);

        // Obtain or create and register package
        UCMPackageImpl theUCMPackage = (UCMPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof UCMPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new UCMPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        CorePackage.eINSTANCE.eClass();

        // Create package meta-data objects
        theUCMPackage.createPackageContents();

        // Initialize created meta-data
        theUCMPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theUCMPackage.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(UCMPackage.eNS_URI, theUCMPackage);
        return theUCMPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAndFork() {
        return andForkEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getAndJoin() {
        return andJoinEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOrFork() {
        return orForkEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getOrJoin() {
        return orJoinEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getEmptyPoint() {
        return emptyPointEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getEmptyPoint_Direction() {
        return (EAttribute)emptyPointEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getPathNode() {
        return pathNodeEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPathNode_Successors() {
        return (EReference)pathNodeEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPathNode_Predecessors() {
        return (EReference)pathNodeEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getPathNode_ComponentReference() {
        return (EReference)pathNodeEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getResponsibilityRef() {
        return responsibilityRefEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getResponsibilityRef_ResponsibilityDefinition() {
        return (EReference)responsibilityRefEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getResponsibility() {
        return responsibilityEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getResponsibility_ResponsibilityReferences() {
        return (EReference)responsibilityEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getStartPoint() {
        return startPointEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getNodeConnection() {
        return nodeConnectionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getNodeConnection_Condition() {
        return (EReference)nodeConnectionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getNodeConnection_Source() {
        return (EReference)nodeConnectionEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getNodeConnection_Target() {
        return (EReference)nodeConnectionEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getUCMModelElement() {
        return ucmModelElementEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getUseCaseMap() {
        return useCaseMapEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUseCaseMap_Connections() {
        return (EReference)useCaseMapEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUseCaseMap_Nodes() {
        return (EReference)useCaseMapEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUseCaseMap_Responsibilities() {
        return (EReference)useCaseMapEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUseCaseMap_Components() {
        return (EReference)useCaseMapEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUseCaseMap_ComponentReferences() {
        return (EReference)useCaseMapEClass.getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getUseCaseMap_LayoutMaps() {
        return (EReference)useCaseMapEClass.getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getComponentReference() {
        return componentReferenceEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getComponentReference_ComponentDefinition() {
        return (EReference)componentReferenceEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getComponentReference_Children() {
        return (EReference)componentReferenceEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getComponentReference_Parent() {
        return (EReference)componentReferenceEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getComponentReference_Nodes() {
        return (EReference)componentReferenceEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getComponent() {
        return componentEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getComponent_ComponentReferences() {
        return (EReference)componentEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getComponent_Children() {
        return (EReference)componentEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getComponent_Parents() {
        return (EReference)componentEClass.getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getComponent_Kind() {
        return (EAttribute)componentEClass.getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getCondition() {
        return conditionEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getCondition_Expression() {
        return (EAttribute)conditionEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getEndPoint() {
        return endPointEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EEnum getComponentKind() {
        return componentKindEEnum;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UCMFactory getUCMFactory() {
        return (UCMFactory)getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package.  This method is
     * guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void createPackageContents() {
        if (isCreated) return;
        isCreated = true;

        // Create classes and their features
        andForkEClass = createEClass(AND_FORK);

        andJoinEClass = createEClass(AND_JOIN);

        orForkEClass = createEClass(OR_FORK);

        orJoinEClass = createEClass(OR_JOIN);

        emptyPointEClass = createEClass(EMPTY_POINT);
        createEAttribute(emptyPointEClass, EMPTY_POINT__DIRECTION);

        pathNodeEClass = createEClass(PATH_NODE);
        createEReference(pathNodeEClass, PATH_NODE__SUCCESSORS);
        createEReference(pathNodeEClass, PATH_NODE__PREDECESSORS);
        createEReference(pathNodeEClass, PATH_NODE__COMPONENT_REFERENCE);

        responsibilityRefEClass = createEClass(RESPONSIBILITY_REF);
        createEReference(responsibilityRefEClass, RESPONSIBILITY_REF__RESPONSIBILITY_DEFINITION);

        responsibilityEClass = createEClass(RESPONSIBILITY);
        createEReference(responsibilityEClass, RESPONSIBILITY__RESPONSIBILITY_REFERENCES);

        startPointEClass = createEClass(START_POINT);

        nodeConnectionEClass = createEClass(NODE_CONNECTION);
        createEReference(nodeConnectionEClass, NODE_CONNECTION__CONDITION);
        createEReference(nodeConnectionEClass, NODE_CONNECTION__SOURCE);
        createEReference(nodeConnectionEClass, NODE_CONNECTION__TARGET);

        ucmModelElementEClass = createEClass(UCM_MODEL_ELEMENT);

        useCaseMapEClass = createEClass(USE_CASE_MAP);
        createEReference(useCaseMapEClass, USE_CASE_MAP__CONNECTIONS);
        createEReference(useCaseMapEClass, USE_CASE_MAP__NODES);
        createEReference(useCaseMapEClass, USE_CASE_MAP__RESPONSIBILITIES);
        createEReference(useCaseMapEClass, USE_CASE_MAP__COMPONENTS);
        createEReference(useCaseMapEClass, USE_CASE_MAP__COMPONENT_REFERENCES);
        createEReference(useCaseMapEClass, USE_CASE_MAP__LAYOUT_MAPS);

        componentReferenceEClass = createEClass(COMPONENT_REFERENCE);
        createEReference(componentReferenceEClass, COMPONENT_REFERENCE__COMPONENT_DEFINITION);
        createEReference(componentReferenceEClass, COMPONENT_REFERENCE__CHILDREN);
        createEReference(componentReferenceEClass, COMPONENT_REFERENCE__PARENT);
        createEReference(componentReferenceEClass, COMPONENT_REFERENCE__NODES);

        componentEClass = createEClass(COMPONENT);
        createEReference(componentEClass, COMPONENT__COMPONENT_REFERENCES);
        createEReference(componentEClass, COMPONENT__CHILDREN);
        createEReference(componentEClass, COMPONENT__PARENTS);
        createEAttribute(componentEClass, COMPONENT__KIND);

        conditionEClass = createEClass(CONDITION);
        createEAttribute(conditionEClass, CONDITION__EXPRESSION);

        endPointEClass = createEClass(END_POINT);

        // Create enums
        componentKindEEnum = createEEnum(COMPONENT_KIND);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model.  This
     * method is guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void initializePackageContents() {
        if (isInitialized) return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Obtain other dependent packages
        CorePackage theCorePackage = (CorePackage)EPackage.Registry.INSTANCE.getEPackage(CorePackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        andForkEClass.getESuperTypes().add(this.getPathNode());
        andJoinEClass.getESuperTypes().add(this.getPathNode());
        orForkEClass.getESuperTypes().add(this.getPathNode());
        orJoinEClass.getESuperTypes().add(this.getPathNode());
        emptyPointEClass.getESuperTypes().add(this.getPathNode());
        pathNodeEClass.getESuperTypes().add(this.getUCMModelElement());
        responsibilityRefEClass.getESuperTypes().add(this.getPathNode());
        responsibilityEClass.getESuperTypes().add(this.getUCMModelElement());
        startPointEClass.getESuperTypes().add(this.getPathNode());
        ucmModelElementEClass.getESuperTypes().add(theCorePackage.getCOREModelElement());
        useCaseMapEClass.getESuperTypes().add(theCorePackage.getCOREModel());
        componentReferenceEClass.getESuperTypes().add(this.getUCMModelElement());
        componentEClass.getESuperTypes().add(this.getUCMModelElement());
        endPointEClass.getESuperTypes().add(this.getPathNode());

        // Initialize classes and features; add operations and parameters
        initEClass(andForkEClass, AndFork.class, "AndFork", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(andJoinEClass, AndJoin.class, "AndJoin", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(orForkEClass, OrFork.class, "OrFork", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(orJoinEClass, OrJoin.class, "OrJoin", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(emptyPointEClass, EmptyPoint.class, "EmptyPoint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getEmptyPoint_Direction(), ecorePackage.getEBoolean(), "direction", "false", 0, 1, EmptyPoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(pathNodeEClass, PathNode.class, "PathNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getPathNode_Successors(), this.getNodeConnection(), this.getNodeConnection_Source(), "successors", null, 0, -1, PathNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getPathNode_Predecessors(), this.getNodeConnection(), this.getNodeConnection_Target(), "predecessors", null, 0, -1, PathNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getPathNode_ComponentReference(), this.getComponentReference(), this.getComponentReference_Nodes(), "componentReference", null, 0, 1, PathNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(responsibilityRefEClass, ResponsibilityRef.class, "ResponsibilityRef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getResponsibilityRef_ResponsibilityDefinition(), this.getResponsibility(), this.getResponsibility_ResponsibilityReferences(), "responsibilityDefinition", null, 1, 1, ResponsibilityRef.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(responsibilityEClass, Responsibility.class, "Responsibility", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getResponsibility_ResponsibilityReferences(), this.getResponsibilityRef(), this.getResponsibilityRef_ResponsibilityDefinition(), "responsibilityReferences", null, 0, -1, Responsibility.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(startPointEClass, StartPoint.class, "StartPoint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(nodeConnectionEClass, NodeConnection.class, "NodeConnection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getNodeConnection_Condition(), this.getCondition(), null, "condition", null, 0, 1, NodeConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getNodeConnection_Source(), this.getPathNode(), this.getPathNode_Successors(), "source", null, 1, 1, NodeConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getNodeConnection_Target(), this.getPathNode(), this.getPathNode_Predecessors(), "target", null, 1, 1, NodeConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(ucmModelElementEClass, UCMModelElement.class, "UCMModelElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        initEClass(useCaseMapEClass, UseCaseMap.class, "UseCaseMap", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getUseCaseMap_Connections(), this.getNodeConnection(), null, "connections", null, 0, -1, UseCaseMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUseCaseMap_Nodes(), this.getPathNode(), null, "nodes", null, 0, -1, UseCaseMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUseCaseMap_Responsibilities(), this.getResponsibility(), null, "responsibilities", null, 0, -1, UseCaseMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUseCaseMap_Components(), this.getComponent(), null, "components", null, 0, -1, UseCaseMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUseCaseMap_ComponentReferences(), this.getComponentReference(), null, "componentReferences", null, 0, -1, UseCaseMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getUseCaseMap_LayoutMaps(), theCorePackage.getLayoutMap(), null, "layoutMaps", null, 0, -1, UseCaseMap.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(componentReferenceEClass, ComponentReference.class, "ComponentReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getComponentReference_ComponentDefinition(), this.getComponent(), this.getComponent_ComponentReferences(), "componentDefinition", null, 1, 1, ComponentReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getComponentReference_Children(), this.getComponentReference(), this.getComponentReference_Parent(), "children", null, 0, -1, ComponentReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getComponentReference_Parent(), this.getComponentReference(), this.getComponentReference_Children(), "parent", null, 0, 1, ComponentReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getComponentReference_Nodes(), this.getPathNode(), this.getPathNode_ComponentReference(), "nodes", null, 0, -1, ComponentReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(componentEClass, Component.class, "Component", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getComponent_ComponentReferences(), this.getComponentReference(), this.getComponentReference_ComponentDefinition(), "componentReferences", null, 0, -1, Component.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getComponent_Children(), this.getComponent(), this.getComponent_Parents(), "children", null, 0, -1, Component.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEReference(getComponent_Parents(), this.getComponent(), this.getComponent_Children(), "parents", null, 0, -1, Component.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getComponent_Kind(), this.getComponentKind(), "kind", "Team", 0, 1, Component.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(conditionEClass, Condition.class, "Condition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getCondition_Expression(), ecorePackage.getEString(), "expression", null, 0, 1, Condition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(endPointEClass, EndPoint.class, "EndPoint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        // Initialize enums and add enum literals
        initEEnum(componentKindEEnum, ComponentKind.class, "ComponentKind");
        addEEnumLiteral(componentKindEEnum, ComponentKind.TEAM);
        addEEnumLiteral(componentKindEEnum, ComponentKind.ACTOR);

        // Create resource
        createResource(eNS_URI);
    }

} //UCMPackageImpl
