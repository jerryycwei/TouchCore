package ca.mcgill.sel.core.controller;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.commons.emf.util.EMFModelUtil;
import ca.mcgill.sel.core.COREContribution;
import ca.mcgill.sel.core.COREImpactModel;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.core.CoreFactory;
import ca.mcgill.sel.core.CorePackage;

/**
 * The controller for {link @Feature}.
 *
 * @author Romain
 *
 */
public class ContributionController extends CoreBaseController {

    /**
     * Creates a new instance of {@link ContributionController}.
     */
    ContributionController() {
        // prevent anyone outside this package to instantiate
    }

    /**
     * Create a new {@link COREContribution}.
     *
     * @param impactModel The {@link COREImpactModel} that will contain it
     * @param source the source {@link COREImpactNode} of this {@link COREContribution}
     * @param impacts the impact {@link COREImpactNode} of this {@link COREContribution}
     * @return the {@link COREContribution} created
     */
    public COREContribution createContribution(COREImpactModel impactModel, COREImpactNode source,
            COREImpactNode impacts) {
        COREContribution contribution = CoreFactory.eINSTANCE.createCOREContribution();

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(impactModel);

        CompoundCommand compoundCommand = new CompoundCommand();

        Command setSourceContributionCommand =
                SetCommand.create(editingDomain, contribution, CorePackage.Literals.CORE_CONTRIBUTION__SOURCE, source);

        Command setImpactsContributionCommand =
                SetCommand
                .create(editingDomain, contribution, CorePackage.Literals.CORE_CONTRIBUTION__IMPACTS, impacts);

        Command addContributionCommand =
                AddCommand.create(editingDomain, impactModel, CorePackage.Literals.CORE_IMPACT_MODEL__CONTRIBUTIONS,
                        contribution);

        compoundCommand.append(setSourceContributionCommand);
        compoundCommand.append(setImpactsContributionCommand);
        compoundCommand.append(addContributionCommand);

        doExecute(editingDomain, compoundCommand);

        return contribution;
    }

    /**
     * Remove this {@link COREContribution}. It will also remove this {@link COREContribution} from his source and
     * impacts {@link COREImpactNode}.
     *
     * @param contribution the {@link COREContribution} to remove
     */
    public void removeContribution(COREContribution contribution) {
        COREImpactModel impactModel =
                EMFModelUtil.getRootContainerOfType(contribution, CorePackage.Literals.CORE_IMPACT_MODEL);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(impactModel);

        Command command = createRemoveContributionCommand(impactModel, contribution);

        doExecute(editingDomain, command);
    }

    /**
     * Create a command that will remove this {@link COREContribution}. It will also remove this
     * {@link COREContribution} from his source and impacts {@link COREImpactNode}.
     *
     * @param impactModel the {@link COREImpactModel} that contains the {@link COREContribution}
     * @param contribution the {@link COREContribution} to remove
     * @return the command created
     */
    public Command createRemoveContributionCommand(COREImpactModel impactModel, COREContribution contribution) {
        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(impactModel);

        CompoundCommand compoundCommand = new CompoundCommand();

        compoundCommand.append(RemoveCommand.create(editingDomain, impactModel,
                CorePackage.Literals.CORE_IMPACT_MODEL__CONTRIBUTIONS, contribution));

        compoundCommand.append(SetCommand.create(editingDomain, contribution,
                CorePackage.Literals.CORE_CONTRIBUTION__IMPACTS, SetCommand.UNSET_VALUE));
        compoundCommand.append(SetCommand.create(editingDomain, contribution,
                CorePackage.Literals.CORE_CONTRIBUTION__SOURCE, SetCommand.UNSET_VALUE));

        return compoundCommand;
    }

    /**
     * Set the relative weight of this {@link COREContribution}.
     *
     * @param contribution the {@link COREContribution} to set
     * @param weight the new weight of the {@link COREContribution}
     */
    public void setContributionWeight(COREContribution contribution, int weight) {
        COREImpactModel impactModel =
                EMFModelUtil.getRootContainerOfType(contribution, CorePackage.Literals.CORE_IMPACT_MODEL);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(impactModel);

        Command command =
                SetCommand.create(editingDomain, contribution, CorePackage.Literals.CORE_CONTRIBUTION__RELATIVE_WEIGHT,
                        weight);

        doExecute(editingDomain, command);
    }

}
