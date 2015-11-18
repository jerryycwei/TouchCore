package ca.mcgill.sel.ucm.controller;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.commons.emf.util.EMFModelUtil;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.core.LayoutElement;
import ca.mcgill.sel.ucm.PathNode;
import ca.mcgill.sel.ucm.UCMFactory;
import ca.mcgill.sel.ucm.UCMPackage;
import ca.mcgill.sel.ucm.UseCaseMap;

/**
 * A Controller class for path nodes in the ucm.
 *
 * @author jerrywei
 *
 */
public class PathNodeController extends UCMBaseController {

    /**
     * Creates a new path node controller.
     */
    PathNodeController() {

    }

    /**
     * Create a new {@link PathNode}.
     *
     * @param ucm The ucm to create the PathNode on
     * @param x The x coordinate the PathNode will be created at
     * @param y The y coordinate the PathNode will be created at
     * @param name The name of the PathNode
     * @return the {@link PathNode} created
     */
    public PathNode createPathNode(UseCaseMap ucm, float x, float y, String name) {
        PathNode pathNode = UCMFactory.eINSTANCE.createPathNode();

        pathNode.setName(name);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(ucm);

        Command addPNCommand = createPathNodeCommand(editingDomain, ucm, pathNode);

        doExecute(editingDomain, addPNCommand);

        return pathNode;
    }

    /**
     * Creates a command which creates a path node.
     *
     * @param editingDomain The editing domain
     * @param ucm The ucm to add this path node
     * @param pn The path node
     * @return A command which creates the path node
     */
    protected Command createPathNodeCommand(EditingDomain editingDomain, UseCaseMap ucm, PathNode pn) {
        Command addPathNodeCommand = AddCommand.create(editingDomain, ucm, UCMPackage.Literals.USE_CASE_MAP__NODES, pn);
        return addPathNodeCommand;
    }

    /**
     * Removes the path node from the ucm.
     *
     * @param pn The path node to be removed.
     */
    public void removePathNode(PathNode pn) {
        UseCaseMap useCaseMap =
                EMFModelUtil.getRootContainerOfType(pn, UCMPackage.Literals.USE_CASE_MAP);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(useCaseMap);

        CompoundCommand rmPNCompCom = new CompoundCommand();

        rmPNCompCom
                .append(RemoveCommand.create(editingDomain, useCaseMap, UCMPackage.Literals.USE_CASE_MAP__NODES, pn));

        // TODO: Design Decision - When delete pathnode want delete attached node connections

        rmPNCompCom.append(SetCommand.create(editingDomain, pn, UCMPackage.Literals.NODE_CONNECTION__SOURCE,
                SetCommand.UNSET_VALUE));

        rmPNCompCom.append(SetCommand.create(editingDomain, pn, UCMPackage.Literals.NODE_CONNECTION__TARGET,
                SetCommand.UNSET_VALUE));

        doExecute(editingDomain, rmPNCompCom);
    }

    /**
     * Moves the Path Node to a new location.
     *
     * @param pn The path node to move
     * @param x The new location x
     * @param y The new location y
     */
    public void movePathNode(PathNode pn, float x, float y) {
        EClass eclass = UCMPackage.Literals.USE_CASE_MAP;

        UseCaseMap useCaseMap = EMFModelUtil.getRootContainerOfType(pn, eclass);
        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(useCaseMap);

        Command movePNCom = createMoveCommand(editingDomain, useCaseMap.getLayoutMaps().get(pn), pn, x, y);
        doExecute(editingDomain, movePNCom);
    }

    /**
     * Creates a new command which moves the given PathNode to the given position.
     *
     * @param editingDomain the {@link EditingDomain}
     * @param layoutElement the layoutElement of this pathnode
     * @param o the PathNode that should be moved
     * @param x the new x position
     * @param y the new y position
     * @return the command which moves the PathNode to the new position
     */
    protected Command createMoveCommand(EditingDomain editingDomain,
            LayoutElement layoutElement, PathNode o, float x, float y) {

        CompoundCommand moveCC = new CompoundCommand();

        moveCC.append(SetCommand.create(editingDomain, layoutElement, CorePackage.Literals.LAYOUT_ELEMENT__X, x));
        moveCC.append(SetCommand.create(editingDomain, layoutElement, CorePackage.Literals.LAYOUT_ELEMENT__Y, y));

        return moveCC;

    }
}
