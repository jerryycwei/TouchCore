package ca.mcgill.sel.ram.weaver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import ca.mcgill.sel.core.COREModelReuse;
import ca.mcgill.sel.core.weaver.util.WeaverListener;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.Instantiation;
import ca.mcgill.sel.ram.PrimitiveType;
import ca.mcgill.sel.ram.RamFactory;
import ca.mcgill.sel.ram.Traceable;
import ca.mcgill.sel.ram.WovenAspect;
import ca.mcgill.sel.ram.weaver.messageview.MessageViewWeaver;
import ca.mcgill.sel.ram.weaver.stateview.StateViewWeaver;
import ca.mcgill.sel.ram.weaver.stateview.StateViewWeaverUtils;
import ca.mcgill.sel.ram.weaver.structuralview.StructuralViewWeaver;
import ca.mcgill.sel.ram.weaver.util.ReferenceUpdater;
import ca.mcgill.sel.ram.weaver.util.WeavingInformation;

/**
 * The RAMWeaver is capable of weaving two or more aspects together within a hierarchy.
 * This class is a singleton.
 * 
 * The weaver has two operations:
 * <ul>
 * <li><b>single weave:</b> weaving two directly dependent aspects together</li>
 * <li><b>complete weave (weave all):</b> weaves together all aspects within a hierarchy to receive an independent
 * aspect</li>
 * </ul>
 * 
 * For each two aspects that are woven together, the {@link ca.mcgill.sel.ram.StructuralView} is woven.
 * The {@link ca.mcgill.sel.ram.MessageView}s and {@link ca.mcgill.sel.ram.StateView}s are copied
 * from the lower-level to the higher-level aspect and are only woven at the very end,
 * when there are no dependencies left.
 *
 * @author mschoettle
 * @author walabe
 * @author abirayed
 * @author oalam
 */
// TODO: mschoettle: Use proper logger instead of System.out.
public final class RAMWeaver {

    /**
     * File to save an aspect temporarily.
     */
    private static final File TEMPORARY_ASPECT = new File("temp.ram");

    /**
     * The singleton instance of this class.
     */
    private static RAMWeaver instance;

    private MessageViewWeaver messageViewWeaver;
    private StateViewWeaver stateViewWeaver;
    // Keep weaving information across multiple single weaves to be able to access it later (e.g., in weaveAll(...)).
    private WeavingInformation weavingInformation;

    /**
     * Creates a new instance of the weaver and initializes all required objects.
     */
    private RAMWeaver() {
        messageViewWeaver = new MessageViewWeaver();
        stateViewWeaver = new StateViewWeaver();
        weavingInformation = new WeavingInformation();
    }

    /**
     * Returns the singleton instance of {@link RAMWeaver}.
     * 
     * @return the singleton instance
     */
    public static RAMWeaver getInstance() {
        if (instance == null) {
            instance = new RAMWeaver();
        }

        return instance;
    }

    /**
     * Weaves an aspect hierarchy using the top down approach.
     * Starts at the top of the hierarchy (with the aspect that should be woven)
     * and subsequently weaves depending aspects into this aspect until no dependencies are left anymore.
     * 
     * Consider the following example:
     * 
     * <pre>
     *        A
     *      / | \
     *     B  C  D
     *    /  / \  \
     *   E  F   G  H
     *  /           \
     * I             J
     * </pre>
     * 
     * The algorithm will first weave B, C and D into A (= A')
     * Then it will weave E, F, G and H into A' (= A'').
     * Finally, it will weave I and J into A''.
     * 
     * @param aspect the aspect of which all dependent aspects should be woven into
     * @return a woven aspect (copy) with no dependencies (all dependent aspects are woven into it)
     */
    public Aspect weaveAllTopDown(Aspect aspect) {
        weavingInformation.clear();

        Aspect result = weaveAllSingle(aspect);
        weaveAll(result);

        return result;
    }

    /**
     * Weaves the complete hierarchy in a top-down manner with the exception that no
     * state and message views are woven.
     * This means, state and message views are only copied.
     * 
     * @param aspect the aspect of which all dependent aspects should be woven into
     * @return a woven aspect (copy) with no dependencies (all dependent aspects are woven into it)
     * @see #weaveAllTopDown(Aspect)
     * @see #weaveSingle(Aspect, Instantiation)
     */
    public Aspect weaveAllSingle(Aspect aspect) {
        weavingInformation.clear();

        Aspect result = loadNewInstance(aspect);

        while (!result.getInstantiations().isEmpty()) {
            // Get the first level of instantiations for this aspect ...
            List<Instantiation> currentLevel = new ArrayList<Instantiation>(result.getInstantiations());

            // Weave only the first level ...
            for (Instantiation instantiation : currentLevel) {
                doWeaveSingle(result, instantiation);
            }

        }

        return result;
    }

    /**
     * Weaves the state machines of the state views.
     * Doesn't take into account the dependencies and instantiations of the aspect.
     * 
     * @param aspect the aspect to weave state machines for
     * @param listener to notify when events occur during weaving
     */
    public void weaveStateMachines(Aspect aspect, WeaverListener<Aspect> listener) {
        try {
            listener.weavingStarted();

            Aspect result = loadNewInstance(aspect);
            stateViewWeaver.weaveStateViews(result);

            listener.weavingFinished(result);
            // CHECKSTYLE:IGNORE IllegalCatch: There could be many different exceptions during initialization.
        } catch (Exception e) {
            // TODO: catch in lower level weaver operations, and throw custom exception.
            listener.weavingFailed(e);
        }

    }

    /**
     * Weaves the state machines of the state views.
     * Doesn't apply the CSP composition to the copied state machines.
     * 
     * @param aspect the aspect to weave state machines for
     * @param listener to notify when events occur during weaving
     */
    public void weaveAllNoCSPWeavingForStateViews(Aspect aspect, WeaverListener<Aspect> listener) {
        try {
            listener.weavingStarted();

            Aspect result = loadNewInstance(aspect);
            weavingInformation.clear();
            // Do phase 1: Weave all structural views and copy the message and state views
            // from the lower- to the higher-level aspect.
            resolveDependenciesBottomUp(result);

            messageViewWeaver.weaveMessageViews(aspect, weavingInformation);

            listener.weavingFinished(result);
            // CHECKSTYLE:IGNORE IllegalCatch: There could be many different exceptions during initialization.
        } catch (Exception e) {
            // TODO: catch in lower level weaver operations, and throw custom exception.
            listener.weavingFailed(e);
        }
    }

    /**
     * Performs the final step of weaving.
     * I.e., message views and state views are woven.
     * The actions are performed on the given aspect directly.
     * 
     * @param aspect the aspect for which views should be woven
     */
    public void weaveAll(Aspect aspect) {
        messageViewWeaver.weaveMessageViews(aspect, weavingInformation);
        stateViewWeaver.weaveStateViews(aspect);
    }

    /**
     * Weaves an aspect hierarchy using the bottom up approach.
     * Starts at the bottom (left) of the hierarchy and traverses the path up,
     * subsequently weaving the lower-level aspect into its parent/instantiating (higher-level) aspect.
     * until all aspects are woven into the top-level aspect (i.e., there are no dependencies left anymore).
     * 
     * Consider the following example:
     * 
     * <pre>
     *        A
     *      / | \
     *     B  C  D
     *    /  / \  \
     *   E  F   G  H
     *  /           \
     * I             J
     * </pre>
     * 
     * The algorithm will:
     * <ol>
     * <li>weave I into E (= E')</li>
     * <li>weave E' into B (= B')</li>
     * <li>weave B' into A (= A')</li>
     * <li>weave F into C (= C')</li>
     * <li>weave G into C' (= C'')</li>
     * <li>weave C'' into A' (= A'')</li>
     * <li>weave J into H (= H')</li>
     * <li>weave H' into D (= D')</li>
     * <li>weave D' into A'' (= A''')</li>
     * </ol>
     * 
     * @param aspect the aspect of which all dependent aspects should be woven into
     * @return a woven aspect (copy) with no dependencies (all dependent aspects are woven into it)
     */
    public Aspect weaveAllBottomUp(Aspect aspect) {
        weavingInformation.clear();

        Aspect result = loadNewInstance(aspect);

        // Do phase 1: Weave all structural views and copy the message and state views
        // from the lower- to the higher-level aspect.
        resolveDependenciesBottomUp(result);

        // Do phase 2: Weave message and state views.
        weaveAll(result);

        return result;
    }

    /**
     * Resolves dependencies of the given aspect from the bottom (of the dependency hierarchy) up.
     * For each two aspects, a single weave is performed (only structure is woven, rest is copied over).
     * 
     * @param aspect the aspect to resolve dependencies for
     * @see #weaveAllBottomUp(Aspect)
     */
    private void resolveDependenciesBottomUp(Aspect aspect) {
        // Create a copy of the instantiations to evade a concurrent modification,
        // when instantiations are removed in doWeaveSingle.
        List<Instantiation> instantiations = new ArrayList<Instantiation>(aspect.getInstantiations());

        for (Instantiation instantiation : instantiations) {
            // Find the lowest aspect recursively ...
            if (!instantiation.getSource().getInstantiations().isEmpty()) {
                resolveDependenciesBottomUp(instantiation.getSource());
            }

            doWeaveSingle(aspect, instantiation);
        }

    }

    /**
     * Weaves two directly dependent aspects together (single weave).
     * Weaves the structural view and copies the message and states view
     * of the lower-level to the higher-level aspect.
     * 
     * @param base the aspect to weave into (higher-level aspect)
     * @param instantiation the instantiation for the lower-level aspect
     * 
     * @return the woven aspect which resulted from weaving the lower-level into the higher-level aspect
     */
    public Aspect weaveSingle(Aspect base, Instantiation instantiation) {
        weavingInformation.clear();

        Aspect result = loadNewInstance(base);

        // Get the instantiation from the new instance, because the parameter
        // refers to the instantiation of base.
        int index = base.getInstantiations().indexOf(instantiation);
        Instantiation actualInstantiation = result.getInstantiations().get(index);

        doWeaveSingle(result, actualInstantiation);

        return result;
    }

    /**
     * Performs the step of a single weave.
     * The lower-level aspect from the instantiation is woven into the given aspect.
     * 
     * @param base the higher-level aspect the lower-level aspect is woven into
     * @param instantiation the lower-level aspect to weave into the base aspect
     */
    public void doWeaveSingle(Aspect base, Instantiation instantiation) {
        System.out.println("Weaving " + instantiation.getSource().getName() + " into " + base.getName());
        StructuralViewWeaver structuralViewWeaver = StructuralViewWeaver.getInstance();
        for (COREModelReuse modelReuse : instantiation.getSource().getModelReuses()) {
            COREModelReuse copy = EcoreUtil.copy(modelReuse);
            base.getModelReuses().add(copy);
        }

        // CHECKSTYLE:IGNORE ParameterAssignment FOR 2 LINES: Needed, because the operation returns the result,
        // however it returns base.
        base = structuralViewWeaver.weave(base, instantiation);

        WeavingInformation currentWeavingInformation = structuralViewWeaver.getWeavingInformation();

        // Update the model reuses.
        ReferenceUpdater.getInstance().update(base.getModelReuses(), currentWeavingInformation);

        createTrace(base, instantiation.getSource(), currentWeavingInformation);

        // Merge weaving information, to be able to look at woven elements later.
        this.weavingInformation.merge(currentWeavingInformation);

        messageViewWeaver.copyMessageViews(base, instantiation.getSource(), currentWeavingInformation);
        StateViewWeaverUtils.copyStateViews(base, instantiation.getSource(), currentWeavingInformation);

        base.getInstantiations().remove(instantiation);
    }

    /**
     * Create a trace of each traceable element in woven aspect.
     * 
     * @param base the aspect we are adding tracing information to
     * @param wovenAspect the aspect woven into the base
     * @param currentWeavingInformation contains all the weaving information of the current weaving
     */
    private void createTrace(Aspect base, Aspect wovenAspect, WeavingInformation currentWeavingInformation) {
        WovenAspect wovenAspectTracing = RamFactory.eINSTANCE.createWovenAspect();
        // TODO: Might have to be the textual represention of the aspect name...
        wovenAspectTracing.setName(wovenAspect.getName());
        wovenAspectTracing.setComesFrom(wovenAspect);
        // Create tracing hierarchy
        createTracingHierarchy(wovenAspect, wovenAspectTracing);
        base.getWovenAspects().add(wovenAspectTracing);
        // associate the wovenAspects to the Traceable elements
        createTrace(currentWeavingInformation, wovenAspectTracing);
    }

    /**
     * Creates the hierarchy of {@link WovenAspect} associated for wovenAspect.
     * This hierarchy will contain the tracing information.
     * Realizes a copy of the WovenAspects so that they don't directly own references to the tracing elements
     * 
     * @param wovenAspect the {@link Aspect} associated to the tracing hierarchy
     * @param wovenAspectTracing the {@link WovenAspect} to add the hierarchy to
     */
    private void createTracingHierarchy(Aspect wovenAspect, WovenAspect wovenAspectTracing) {
        for (WovenAspect child : wovenAspect.getWovenAspects()) {
            WovenAspect childCopy = EcoreUtil.copy(child);
            wovenAspectTracing.getChildren().add(childCopy);
        }
    }

    /**
     * Creates tracing information. For each WovenAspect in the hierarchy,
     * associate it with the correct {@link Traceable} for tracing
     * The WovenAspect will update tracing information recursively for its children in the hierarchy
     * 
     * @param currentWeavingInformation the current {@link WeavingInformation}
     * @param wovenAspectTracing the {@link WovenAspect} to add tracing information to
     */
    public void createTrace(WeavingInformation currentWeavingInformation, WovenAspect wovenAspectTracing) {
        for (EObject toElement : currentWeavingInformation.getToElements()) {
            if (toElement instanceof Traceable && !(toElement instanceof PrimitiveType)) {
                // add the element to the parent WovenAspect
                wovenAspectTracing.getWovenElements().add((Traceable) toElement);
                // update the children
                for (WovenAspect wa : wovenAspectTracing.getChildren()) {
                    updateChildren(wa, (Traceable) toElement, currentWeavingInformation);
                }
            }
        }
    }

    /**
     * Creates tracing information for the given WovenAspect.
     * Looks inside the {@link WovenAspect#getWovenElements()} for the given {@link Traceable} T in the
     * {@link WeavingInformation} as a key for mapping and update the aspect accordingly.
     * For example if A is in the aspect wovens elements and we find a mapping A->toElement,
     * we have to add toElement to the wovenAspect wovenElements too.
     * Tracing information will be update like so recursively for all children in the hierarchy.
     * 
     * @param wovenAspect the {@link WovenAspect} to update
     * @param toElement the {@link Traceable} to look for
     * @param currentweavingInfo the {@link WeavingInformation} to search the mappings in.
     */
    private void updateChildren(WovenAspect wovenAspect, Traceable toElement, WeavingInformation currentweavingInfo) {
        List<Traceable> fromElements = new ArrayList<Traceable>();
        // Get all "from elements for the given toElement
        for (Traceable to : wovenAspect.getWovenElements()) {
            for (Traceable from : currentweavingInfo.getFromElements(toElement)) {
                if (from.equals(to)) {
                    fromElements.add(from);
                }
            }
        }
        if (!fromElements.isEmpty() && !wovenAspect.getWovenElements().contains(toElement)) {
            // Add the element
            wovenAspect.getWovenElements().add(toElement);

            // clear list of woven elements of non useful items.
            for (Traceable fromElt : fromElements) {
                boolean toRemove = true;
                for (Traceable toElt : currentweavingInfo.getToElements(fromElt)) {
                    if (!wovenAspect.getWovenElements().contains(toElt)) {
                        toRemove = false;
                        break;
                    }
                }
                if (toRemove) {
                    wovenAspect.getWovenElements().remove(fromElt);
                }
            }
        }
        // update the tracing information recursively for all children
        for (WovenAspect wa : wovenAspect.getChildren()) {
            updateChildren(wa, toElement, currentweavingInfo);
        }
    }

    /**
     * Loads a new instance of an aspect. This is required, because lower-level aspects
     * that contain dependencies will be modified, i.e., it's dependencies are
     * woven into it. If this (lower-level) aspect is loaded somewhere else, e.g., a GUI,
     * the modifications are reflected there, which is unwanted behavior.
     * If the aspect were cloned, it would require the mappings of the instantiation
     * for this lower-level aspect to be updated to the new elements, which would
     * be more time-consuming to do.
     * Therefore, to circumvent this, the aspect is saved in a temporary file
     * and loaded again using a separate resource set, which forces to load
     * other aspects to be loaded as new instances. Otherwise the existing
     * resource set would get the resources for the lower-level aspects
     * from its cache.
     * 
     * @param aspect the aspect to load a new instance for
     * @return a new instance of the given aspect
     */
    public Aspect loadNewInstance(Aspect aspect) {
        // Given aspect has to be cloned. Otherwise, when adding the aspect to the new resource
        // it gets removed from its current resource. This will mean that the given aspect
        // is directly modified.
        Aspect result = EcoreUtil.copy(aspect);
        String pathName = TEMPORARY_ASPECT.getAbsolutePath();

        // Use our own resource set for saving and loading to workaround the issue.
        ResourceSet resourceSet = new ResourceSetImpl();

        // Create a resource to temporarily save the aspect.
        Resource resource = resourceSet.createResource(URI.createFileURI(pathName));
        resource.getContents().add(result);

        try {
            resource.save(Collections.EMPTY_MAP);

            // Load the temporary aspect ...
            resource = resourceSet.getResource(URI.createFileURI(pathName), true);
            result = (Aspect) resource.getContents().get(0);

            // Copy the aspect to loose the reference to the temporary file...
            result = EcoreUtil.copy(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Delete the temporary file ...
        TEMPORARY_ASPECT.delete();

        return result;
    }
}
