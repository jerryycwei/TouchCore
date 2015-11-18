package ca.mcgill.sel.core.weaver;

import java.util.ArrayList;
import java.util.Set;

import ca.mcgill.sel.core.COREConcern;
import ca.mcgill.sel.core.COREModelCompositionSpecification;
import ca.mcgill.sel.core.COREModelReuse;
import ca.mcgill.sel.core.COREVisibilityType;
import ca.mcgill.sel.core.weaver.util.WeaverListener;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.Classifier;
import ca.mcgill.sel.ram.Instantiation;
import ca.mcgill.sel.ram.RamFactory;
import ca.mcgill.sel.ram.util.RAMModelUtil;
import ca.mcgill.sel.ram.weaver.RAMWeaver;

/**
 * The COREWeaver is capable of weaving the aspects realizing features to one aspect.
 * The weaving process for the selected features of a concern is performed in a top-down fashion
 * where the instantiation with the highest depth is always woven first.
 * This ensures that an aspect is never woven twice, i.e., instantiations for the same aspect
 * can be merged together.
 * 
 * @author oalam
 * @author mschoettle
 */
public final class COREWeaver {

    /**
     * The singleton instance of this class.
     */
    private static COREWeaver instance;

    /**
     * Creates a new instance of the weaver.
     */
    private COREWeaver() {

    }

    /**
     * Returns the singleton instance of {@link COREWeaver}.
     * 
     * @return the singleton instance
     */
    public static COREWeaver getInstance() {
        if (instance == null) {
            instance = new COREWeaver();
        }

        return instance;
    }

    /**
     * Weaves the aspects of the selected features of the given concern to one aspect.
     * 
     * @param concern the reused concern
     * @param resolvedAspects the selected features of the concern
     * @return the woven aspect containing the merge result of all selected features
     */
    public Aspect weaveSelectedFeatures(COREConcern concern, Set<Aspect> resolvedAspects) {
        if (resolvedAspects == null) {
            return createBadConfigurationAspect();
        }
        Aspect aspect = createAspect(concern, resolvedAspects);

        aspect = weaveByRank(aspect);
        aspect = weaveModelReuses(aspect, false);
        changeClassifierVisibility(aspect);
        return aspect;
    }

    /**
     * Weaves the all reuses and the extends for a particular aspect.
     * 
     * @param base the aspect to be woven
     * @param listener to notify when events occur during weaving
     */
    public void weaveAll(Aspect base, WeaverListener<Aspect> listener) {
        try {
            listener.weavingStarted();
            RAMWeaver weaver = RAMWeaver.getInstance();
            Aspect aspect = weaver.loadNewInstance(base);
            aspect = weaveByRank(aspect);
            aspect = weaveModelReuses(aspect, true);
            weaver.weaveAll(aspect);
            changeClassifierVisibility(aspect);
            listener.weavingFinished(aspect);
            // CHECKSTYLE:IGNORE IllegalCatch: There could be many different exceptions during initialization.
        } catch (Exception e) {
            // TODO: catch in lower level weaver operations, and throw custom exception.
            listener.weavingFailed(e);
        }
    }

    /**
     * Weaves a single extends.
     * 
     * @param base input Aspect
     * @param ins input Instantiation
     * @param listener to notify when events occur during weaving
     */
    public void weaveExtendSingle(Aspect base, Instantiation ins, WeaverListener<Aspect> listener) {
        try {
            listener.weavingStarted();
            RAMWeaver weaver = RAMWeaver.getInstance();
            Aspect wovenAspect = weaver.weaveSingle(base, ins);
            listener.weavingFinished(wovenAspect);
            // CHECKSTYLE:IGNORE IllegalCatch: There could be many different exceptions during initialization.
        } catch (Exception e) {
            // TODO: catch in lower level weaver operations, and throw custom exception.
            listener.weavingFailed(e);
        }
    }

    /**
     * Weaves extends by ranks.
     * 
     * @param base input Aspect
     * @return aspect woven aspect
     */
    private Aspect weaveByRank(Aspect base) {
        RAMWeaver weaver = RAMWeaver.getInstance();
        ConcernWeavingInfo cwi = new ConcernWeavingInfo(base);
        int highestRank = cwi.getHighestRank(base);
        Aspect result = base;
        // WeaveSingle at each level and then merge the duplicate instantiations
        while (highestRank > 1) {
            Instantiation ins = cwi.getHighestRankedAspect();
            result = weaver.weaveSingle(result, ins);
            mergeInstantiations(result);
            highestRank = cwi.getHighestRank(result);
        }

        mergeInstantiations(base);
        result = weaver.weaveAllSingle(result);
        return result;
    }

    /**
     * Weaves model reuses.
     * 
     * @param base input Aspect
     * @param weaveAll boolean to indicate whether this operation is called by the Reuse or weaveAll method.
     * @return aspect woven aspect
     */
    private Aspect weaveModelReuses(Aspect base, boolean weaveAll) {
        RAMWeaver weaver = RAMWeaver.getInstance();

        ArrayList<COREModelReuse> reexposedModelReuses = new ArrayList<COREModelReuse>();
        for (int i = 0; i < base.getModelReuses().size(); i++) {
            if (base.getModelReuses().get(i).getReuse().getSelectedConfiguration().getReexposed().size() > 0) {
                reexposedModelReuses.add(base.getModelReuses().get(i));
                if (!weaveAll) {
                    for (COREModelCompositionSpecification<?> comp : base.getModelReuses().get(i).getCompositions()) {
                        weaver.doWeaveSingle(base, (Instantiation) comp);
                    }
                }
            } else {
                for (COREModelCompositionSpecification<?> comp : base.getModelReuses().get(i).getCompositions()) {
                    weaver.doWeaveSingle(base, (Instantiation) comp);
                }
            }
        }
        base.getModelReuses().clear();
        base.getModelReuses().addAll(reexposedModelReuses);
        return base;
    }

    /**
     * Weaves a single model reuse.
     * 
     * @param base input Aspect
     * @param comp the input composition
     * @param listener to notify when events occur during weaving
     */
    public void weaveModelReuseSingle(Aspect base, Instantiation comp, WeaverListener<Aspect> listener) {
        try {
            listener.weavingStarted();
            RAMWeaver weaver = RAMWeaver.getInstance();
            Aspect result = weaver.loadNewInstance(base);
            int mIndex = base.getModelReuses().indexOf(comp.eContainer());
            int cIndex = base.getModelReuses().get(mIndex).getCompositions().indexOf(comp);
            Instantiation actualInstantiation = (Instantiation) result.getModelReuses().get(mIndex).getCompositions()
                    .get(cIndex);
            weaver.doWeaveSingle(result, actualInstantiation);
            result.getModelReuses().remove(actualInstantiation.eContainer());
            listener.weavingFinished(result);
            // CHECKSTYLE:IGNORE IllegalCatch: There could be many different exceptions during initialization.
        } catch (Exception e) {
            // TODO: catch in lower level weaver operations, and throw custom exception.
            listener.weavingFailed(e);
        }
    }

    /**
     * Changes the classifier visibility from Public to Package.
     * 
     * @param wovenAspect woven aspect resulting from selecting the
     *            features.
     */
    private void changeClassifierVisibility(Aspect wovenAspect) {

        for (Classifier c : wovenAspect.getStructuralView().getClasses()) {
            // Update visibility for classifiers
            if (c.getVisibility() == COREVisibilityType.PUBLIC) {
                c.setVisibility(COREVisibilityType.CONCERN);
            }
        }
    }

    /**
     * Creates a new aspect with no contents besides instantiations for each selected feature (aspect).
     * 
     * @param concern the reused concern
     * @param selectedFeatures the selected features of the concern
     * @return the new and empty aspect
     */
    private Aspect createAspect(COREConcern concern, Set<Aspect> selectedFeatures) {
        // Create an empty aspect where all selected features are woven into.
        Aspect aspect = RAMModelUtil.createAspect(createAspectName(concern, selectedFeatures));

        for (Aspect realizingAspect : selectedFeatures) {
            Instantiation instantiation = RamFactory.eINSTANCE.createInstantiation();
            instantiation.setSource(realizingAspect);
            aspect.getInstantiations().add(instantiation);
        }

        return aspect;
    }

    /**
     * Creates a new aspect with no contents, for bad feature selection.
     * 
     * @return the new and empty aspect
     */
    private Aspect createBadConfigurationAspect() {
        Aspect aspect = RAMModelUtil.createAspect("BadConfiguration");
        return aspect;
    }

    /**
     * Combines an aspect name based on the concern and the selected features.
     * The format is the following: "concernname"<"concatenated list of selected features">,
     * e.g., Authentication&lt;PasswordExpiry,Password&gt;
     * 
     * @param concern the reused concern
     * @param selectedFeatures the selected features of the concern
     * @return the combined aspect name
     */
    private String createAspectName(COREConcern concern, Set<Aspect> selectedFeatures) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(concern.getName());
        stringBuilder.append("<");

        for (Aspect realizingAspect : selectedFeatures) {
            stringBuilder.append(realizingAspect.getName());
            stringBuilder.append(",");
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(">");

        return stringBuilder.toString();
    }

    /**
     * This method finds two duplicate instantiations and then merges them.
     * There should be at most one duplicate instantiation after each
     * WeaveSingle.
     * 
     * @param base is the Aspect which instantiations are being merged
     */
    private void mergeInstantiations(Aspect base) {
        Instantiation inst1 = null;
        Instantiation inst2 = null;
        int loop = 0;
        outerloop: while (loop < base.getInstantiations().size()) {
            for (int j = loop + 1; j < base.getInstantiations().size(); j++) {
                if (base.getInstantiations().get(loop).getSource() == base.getInstantiations().get(j).getSource()) {
                    inst1 = base.getInstantiations().get(loop);
                    inst2 = base.getInstantiations().get(j);
                    break outerloop;
                }
            }
            loop++;
        }
        if (inst1 != null && inst2 != null) {
            mergeInstantiation(base, inst1, inst2);
        }
    }

    /**
     * Merges the mappings of two instantiations together.
     * 
     * @param base the base aspect
     * @param firstTarget is the aspect to which the result of merging will be saved
     * @param secondTarget is the aspect to be merged
     */
    private void mergeInstantiation(Aspect base, Instantiation firstTarget, Instantiation secondTarget) {
        // Add the mappings from the secondTarget to the instantiation to firstTarget.
        firstTarget.getMappings().addAll(secondTarget.getMappings());
        base.getInstantiations().remove(secondTarget);
    }

}
