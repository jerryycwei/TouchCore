package ca.mcgill.sel.ucm.controller;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.ucm.EndPoint;
import ca.mcgill.sel.ucm.UCMFactory;
import ca.mcgill.sel.ucm.UseCaseMap;

/**
 * A Controller class for end points in the ucm.
 *
 * @author jerrywei
 *
 */
public class EndPointController extends PathNodeController {

    /**
     * Creates a new end point controller.
     */
    EndPointController() {

    }

    /**
     * Create a new {@link EndPoint}.
     *
     * @param ucm The ucm to create an end point on
     * @param x The x coordinate the EndPoint will be created at
     * @param y The y coordinate the EndPoint will be created at
     * @param name The name of the EndPoint
     * @return the {@link EndPoint} created
     */
    public EndPoint createEndPoint(UseCaseMap ucm, float x, float y, String name) {
        EndPoint endPt = UCMFactory.eINSTANCE.createEndPoint();

        endPt.setName(name);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(ucm);

        CompoundCommand compoundCommand = new CompoundCommand();

        Command createEPCom = createPathNodeCommand(editingDomain, ucm, endPt);
        compoundCommand.append(createEPCom);

        Command addLayoutElementCommand = createLayoutElementCommand(editingDomain, ucm, endPt, x, y);
        compoundCommand.append(addLayoutElementCommand);

        doExecute(editingDomain, compoundCommand);

        return endPt;
    }

}
