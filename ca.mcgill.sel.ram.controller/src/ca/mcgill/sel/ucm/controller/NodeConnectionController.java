package ca.mcgill.sel.ucm.controller;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.commons.emf.util.EMFModelUtil;
import ca.mcgill.sel.ucm.NodeConnection;
import ca.mcgill.sel.ucm.PathNode;
import ca.mcgill.sel.ucm.UCMFactory;
import ca.mcgill.sel.ucm.UCMPackage;
import ca.mcgill.sel.ucm.UseCaseMap;

/**
 * A controller class that edits anything related to the node connection in the ucm model.
 *
 * @author jerrywei
 *
 */
public class NodeConnectionController extends UCMBaseController {

    /**
     * Creates a new instance of {@link NodeConnectionController}.
     */
    NodeConnectionController() {
        // prevent anyone outside this package to instantiate
    }

    /**
     * Creates a node connection between 2 path nodes.
     *
     * @param ucm The ucm to add this node connection to
     * @param src The source path node
     * @param target The target path node
     * @return The node connection created
     */
    public NodeConnection createNodeConnection(UseCaseMap ucm, PathNode src, PathNode target) {
        NodeConnection nc = UCMFactory.eINSTANCE.createNodeConnection();

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(ucm);

        CompoundCommand createNCCompCom = new CompoundCommand();

        Command setSourceNodeConnectionCommand =
                SetCommand.create(editingDomain, nc, UCMPackage.Literals.NODE_CONNECTION__SOURCE, src);

        Command setTargetNodeConnectionCommand =
                SetCommand.create(editingDomain, nc, UCMPackage.Literals.NODE_CONNECTION__TARGET, target);

        Command addNodeConnectionCommand =
                AddCommand.create(editingDomain, ucm, UCMPackage.Literals.USE_CASE_MAP__CONNECTIONS,
                        nc);

        createNCCompCom.append(setSourceNodeConnectionCommand);
        createNCCompCom.append(setTargetNodeConnectionCommand);
        createNCCompCom.append(addNodeConnectionCommand);

        doExecute(editingDomain, createNCCompCom);

        return nc;
    }

    /**
     * Creates a command that will remove {@link NodeConnection}. Also removes {@link NodeConnection} from source and
     * target {@link PathNode}.
     *
     * @param ucm the {@link UseCaseMap} that contains the {@link NodeConnection}
     * @param nc the {@link NodeConnection} to remove
     * @return the command created
     */
    public Command createRemoveNodeConnectionCommand(UseCaseMap ucm, NodeConnection nc) {
        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(ucm);

        CompoundCommand rmNCCompCom = new CompoundCommand();

        rmNCCompCom.append(RemoveCommand.create(editingDomain, ucm,
                UCMPackage.Literals.USE_CASE_MAP__CONNECTIONS, nc));

        rmNCCompCom.append(SetCommand.create(editingDomain, nc,
                UCMPackage.Literals.NODE_CONNECTION__TARGET, SetCommand.UNSET_VALUE));
        rmNCCompCom.append(SetCommand.create(editingDomain, nc,
                UCMPackage.Literals.NODE_CONNECTION__SOURCE, SetCommand.UNSET_VALUE));

        return rmNCCompCom;
    }

    /**
     * Removes the node connection.
     * 
     * @param nc The node connection to remove.
     */
    public void removeNodeConnection(NodeConnection nc) {
        UseCaseMap ucm = EMFModelUtil.getRootContainerOfType(nc, UCMPackage.Literals.USE_CASE_MAP);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(ucm);

        Command removeNCCom = createRemoveNodeConnectionCommand(ucm, nc);

        doExecute(editingDomain, removeNCCom);
    }

    /**
     * Sets the condition for {@link NodeConnection}.
     *
     * @param nc the {@link NodeConnection} to set
     * @param expression the condition of the {@link NodeConnection}
     */
    public void setCondition(NodeConnection nc, String expression) {
        UseCaseMap ucm =
                EMFModelUtil.getRootContainerOfType(nc, UCMPackage.Literals.USE_CASE_MAP);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(ucm);

        Command setConditionCom =
                SetCommand.create(editingDomain, nc, UCMPackage.Literals.NODE_CONNECTION__CONDITION,
                        expression);

        doExecute(editingDomain, setConditionCom);
    }

}
