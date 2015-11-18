package ca.mcgill.sel.ucm.controller;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.ucm.ResponsibilityRef;
import ca.mcgill.sel.ucm.UCMFactory;
import ca.mcgill.sel.ucm.UseCaseMap;

/**
 * The controller for {link {@link ResponsibilityRef} .
 *
 * @author jerrywei
 *
 */
public class ResponsibilityController extends PathNodeController {

    /**
     * Creates a new instance of {@link ResponsibilityController}.
     */
    ResponsibilityController() {
    }

    /**
     * Create a new {@link ResponsibilityRef}.
     *
     * @param ucm The {@link UseCaseMap} that contains the ResponsibilityRef
     * @param x The x coordinate the ResponsibilityRef will be created at
     * @param y The y coordinate the ResponsibilityRef will be created at
     * @param name The name of the ResponsibilityRef
     * @return the {@link ResponsibilityRef} created
     */
    public ResponsibilityRef createResponsibilityRef(UseCaseMap ucm, float x, float y, String name) {
        ResponsibilityRef respRef = UCMFactory.eINSTANCE.createResponsibilityRef();

        respRef.setName(name);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(ucm);

        CompoundCommand compoundCommand = new CompoundCommand();

        Command creatRRCom = createPathNodeCommand(editingDomain, ucm, respRef);
        compoundCommand.append(creatRRCom);

        Command addLayoutElementCommand = createLayoutElementCommand(editingDomain, ucm, respRef, x, y);
        compoundCommand.append(addLayoutElementCommand);

        doExecute(editingDomain, compoundCommand);

        return respRef;
    }

}
