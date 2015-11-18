package ca.mcgill.sel.core.controller;

import java.util.ArrayList;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.core.COREConcern;
import ca.mcgill.sel.core.COREReuseConfiguration;
import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.core.COREModelReuse;
import ca.mcgill.sel.core.COREReuse;
import ca.mcgill.sel.core.CoreFactory;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.Instantiation;
import ca.mcgill.sel.ram.RamFactory;

/**
 * The controller for {link @COREReuse and @COREModelReuse}.
 *
 * @author oalam
 *
 */
public class ReuseController extends CoreBaseController {
    /**
     * Creates a new instance of {@link ReuseController}.
     */
    protected ReuseController() {
        // prevent anyone outside this package to instantiate
    }

    /**
     * Creates a new COREModelReuse and In instantiation for the given aspect.
     *
     * @param owner the aspect the instantiation should be added to
     * @param externalAspect the referenced aspect (external woven aspect for models of selected features)
     * @param reusingConcern the concern that the owner aspect belong to.
     * @param concern the reused concern.
     * @param selectedFeatures the features that are selected by the owner
     *            of the {@link Instantiation}
     * @param reexposedFeatures the features that are re-exposed by the owner
     *            of the {@link Instantiation}
     * @return the created {@link COREReuse}
     */
    public COREReuse createReuseInstantiation(Aspect owner, COREConcern reusingConcern, COREConcern concern,
            Aspect externalAspect, Set<COREFeature> selectedFeatures, Set<COREFeature> reexposedFeatures) {

        String configurationName = concern.getName() + "_";
        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(owner);
        COREModelReuse modelReuse = CoreFactory.eINSTANCE.createCOREModelReuse();

        COREReuse reuse = ControllerFactory.INSTANCE.getReuseController().createCOREReuse(concern);
        reuse.setName(concern.getName());

        COREReuseConfiguration configuration = CoreFactory.eINSTANCE.createCOREReuseConfiguration();
        configuration.getSelected().addAll(selectedFeatures);
        configuration.getReexposed().addAll(reexposedFeatures);
        configuration.setSource(concern.getFeatureModel());
        reuse.getConfigurations().add(configuration);
        reuse.setSelectedConfiguration(configuration);

        EList<COREFeature> features = owner.getRealizes();

        ArrayList<Command> addReuseCommands = new ArrayList<Command>();
        // create an instantiation
        Instantiation instantiation = RamFactory.eINSTANCE.createInstantiation();
        instantiation.setSource(externalAspect);
        modelReuse.getCompositions().add(instantiation);
        modelReuse.setReuse(reuse);
        for (COREFeature feature : selectedFeatures) {
            configurationName += feature.getName();
        }
        configuration.setName(configurationName);

        for (COREFeature feature : features) {
            Command addReuseCommand;
            FeatureController fc = ControllerFactory.INSTANCE.getFeatureController();
            addReuseCommand = fc.addReuse(reusingConcern, feature, reuse);
            addReuseCommands.add(addReuseCommand);
        }
        CompoundCommand compoundCommandConcern = new CompoundCommand();
        Command addCOREModelReuseCommand = AddCommand.create(editingDomain, owner,
                CorePackage.Literals.CORE_MODEL__MODEL_REUSES, modelReuse);
        compoundCommandConcern.append(addCOREModelReuseCommand);

        for (Command command : addReuseCommands) {
            compoundCommandConcern.append(command);
        }
        doExecute(editingDomain, compoundCommandConcern);

        return reuse;
    }

    /**
     * Removes an instantiation and its associated COREModelReuse.
     *
     * @param instantiation the {@link Instantiation} to be removed.
     */
    public void removeReuseInstantiation(Instantiation instantiation) {
        COREModelReuse modelReuse = (COREModelReuse) instantiation.eContainer();
        doRemove(modelReuse);
    }

    /**
     * Creates a new CoreReuse for the given aspect.
     *
     * @param externalConcern the referenced concern (external concern) of the {@link COREReuse}
     * @return reuse the COREReuse to be returned.
     */
    public COREReuse createCOREReuse(COREConcern externalConcern) {
        // create an core instantiation
        COREReuse reuse = CoreFactory.eINSTANCE.createCOREReuse();
        reuse.setReusedConcern(externalConcern);
        // doAdd(owner, CorePackage.Literals.CORE_MODEL__REUSES, reuse);
        return reuse;
    }

    /**
     * Delete the given COREReuse.
     *
     * @param reuse the COREReuse to be deleted
     */
    public void deleteCOREReuse(COREReuse reuse) {
        doRemove(reuse);
    }

}
