package ca.mcgill.sel.core.controller;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.core.COREConcern;
import ca.mcgill.sel.core.COREImpactModel;
import ca.mcgill.sel.core.CoreFactory;
import ca.mcgill.sel.core.CorePackage;

/**
 * The controller for {link @Feature}.
 *
 * @author Romain
 *
 */
public class ConcernController extends CoreBaseController {

    /**
     * Creates a new instance of {@link ConcernController}.
     */
    ConcernController() {
        // prevent anyone outside this package to instantiate
    }

    /**
     * Create a new impact model and the first root goal.
     *
     * @param concern the concern that will contained the new impact model.
     * @param rootGoalName the name of the first root goal.
     * @param x the x of the LayoutElement for the root goal
     * @param y the y of the LayoutElement for the root goal
     *
     * @return the impact model created
     */
    public COREImpactModel createImpactModel(COREConcern concern, String rootGoalName, float x, float y) {
        COREImpactModel impactModel = CoreFactory.eINSTANCE.createCOREImpactModel();
        impactModel.setName("Impact Model");

        CompoundCommand compoundCommand = new CompoundCommand();

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(concern);

        Command setImpactModelCommand =
                SetCommand.create(editingDomain, concern, CorePackage.Literals.CORE_CONCERN__IMPACT_MODEL, impactModel);
        Command addImpactModelCommand =
                AddCommand.create(editingDomain, concern, CorePackage.Literals.CORE_CONCERN__MODELS, impactModel);

        compoundCommand.append(setImpactModelCommand);
        compoundCommand.append(addImpactModelCommand);
        compoundCommand.append(ControllerFactory.INSTANCE.getImpactModelElementController()
                .createRootImpactModelElementCommand(editingDomain,
                        impactModel, rootGoalName, x, y));
        doExecute(editingDomain, compoundCommand);

        return impactModel;
    }
}
