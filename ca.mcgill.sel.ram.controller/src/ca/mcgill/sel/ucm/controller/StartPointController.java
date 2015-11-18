package ca.mcgill.sel.ucm.controller;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.ucm.StartPoint;
import ca.mcgill.sel.ucm.UCMFactory;
import ca.mcgill.sel.ucm.UseCaseMap;

/**
 * The controller for {link @StartPoint}.
 *
 * @author jerrywei
 *
 */
public class StartPointController extends PathNodeController {

    /**
     * Creates a new instance of {@link StartPointController}.
     */
    StartPointController() {

    }

    /**
     * Create a new {@link StartPoint}.
     *
     * @param ucm The {@link UseCaseMap} that contains the startPoint
     * @param x The x coordinate the StartPoint will be created at
     * @param y The y coordinate the StartPoint will be created at
     * @param name The name of the StartPoint
     * @return the {@link StartPoint} created
     */
    public StartPoint createStartPoint(UseCaseMap ucm, float x, float y, String name) {
        StartPoint startPt = UCMFactory.eINSTANCE.createStartPoint();

        startPt.setName(name);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(ucm);

        CompoundCommand compoundCommand = new CompoundCommand();

        Command createSPCom = createPathNodeCommand(editingDomain, ucm, startPt);
        compoundCommand.append(createSPCom);

        Command addLayoutElementCommand = createLayoutElementCommand(editingDomain, ucm, startPt, x, y);
        compoundCommand.append(addLayoutElementCommand);

        doExecute(editingDomain, compoundCommand);

        return startPt;
    }

}
