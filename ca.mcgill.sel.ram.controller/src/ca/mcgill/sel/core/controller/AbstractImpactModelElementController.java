package ca.mcgill.sel.core.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import ca.mcgill.sel.commons.emf.util.EMFModelUtil;
import ca.mcgill.sel.core.COREContribution;
import ca.mcgill.sel.core.COREFeatureImpactNode;
import ca.mcgill.sel.core.COREImpactModel;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.core.impl.LayoutContainerMapImpl;
import ca.mcgill.sel.core.util.COREImpactModelUtil;

/**
 * The controller for {@link COREImpactNode} and COREFeatureImpact.
 *
 * @author Romain
 *
 */
public abstract class AbstractImpactModelElementController extends CoreBaseController {

    /**
     * This operation remove a {@link COREImpactNode} from the layout of this root. If all the outgoing
     * contribution of this element are in this layout container, then, we delete this {@link COREImpactNode} from the
     * model.
     *
     * @param editingDomain The domain on which the command will be executed.
     * @param impactModel the container of the {@link COREImpactNode}
     * @param root the root key of the {@link LayoutContainerMapImpl}
     * @param impactModelElement the element to delete
     * @param deleteChildren if the children has to be deleted or not
     * @param elementsAlreadyDeleted the set that contains the elements already deleted in order to not delete an
     *            element twice.
     * @param shouldDeleteNodeFromModel if the element should be deleted from the model or not.
     *
     * @return the {@link Command} created.
     */
    protected Command createDeleteImpactModelElementCommand(EditingDomain editingDomain, COREImpactModel impactModel,
            COREImpactNode root, COREImpactNode impactModelElement, boolean deleteChildren,
            Set<COREImpactNode> elementsAlreadyDeleted, boolean shouldDeleteNodeFromModel) {
        CompoundCommand compoundCommand = new CompoundCommand();

        if (shouldDeleteNodeFromModel) {
            // Add to this compoundCommand the command to delete the element and his contribution from the impact model
            addCommandDeleteElement(editingDomain, compoundCommand, impactModel, impactModelElement, root,
                    deleteChildren, elementsAlreadyDeleted);
        } else {
            List<COREContribution> outgoings = new ArrayList<COREContribution>();

            if (root != null) {

                LayoutContainerMapImpl containerMap = EMFModelUtil.getEntryFromMap(impactModel.getLayouts(), root);

                for (COREContribution outgoing : impactModelElement.getOutgoing()) {
                    if (containerMap.getValue().containsKey(outgoing.getImpacts())) {
                        outgoings.add(outgoing);
                    }
                }
            } else {
                outgoings.addAll(impactModelElement.getOutgoing());
            }

            // Add to this compoundCommand the command to delete the layout for this element and his children.
            // It will also remove the outgoing contribution of this element that link it to a node that his in the same
            // layout container (with the key root)
            addCommandRemoveLayout(editingDomain, compoundCommand, impactModel, impactModelElement, root,
                    outgoings, deleteChildren, elementsAlreadyDeleted);
        }

        elementsAlreadyDeleted.add(impactModelElement);

        return compoundCommand;
    }

    /**
     * This operation remove a {@link COREImpactNode} from the layout of this root. If all the outgoing
     * contribution of this element are in this layout container, then, we delete this {@link COREImpactNode} from the
     * model.
     *
     * @param editingDomain The domain on which the command will be executed.
     * @param impactModel the container of the {@link COREImpactNode}
     * @param root the root key of the {@link LayoutContainerMapImpl}
     * @param impactModelElement the element to delete
     * @param deleteChildren if the children has to be deleted or not
     * @param elementsAlreadyDeleted the set that contains the elements already deleted in order to not delete an
     *            element twice.
     *
     * @return the {@link Command} created.
     */
    protected Command createDeleteImpactModelElementCommand(EditingDomain editingDomain, COREImpactModel impactModel,
            COREImpactNode root, COREImpactNode impactModelElement, boolean deleteChildren,
            Set<COREImpactNode> elementsAlreadyDeleted) {

        return this.createDeleteImpactModelElementCommand(editingDomain, impactModel, root, impactModelElement,
                deleteChildren, elementsAlreadyDeleted,
                COREImpactModelUtil.shouldDeleteNodeFromModel(root, impactModelElement));
    }

    /**
     * Append to the compoundCommand the commands when we have to delete the {@link COREImpactNode} from the
     * model. First we remove the incoming contributions, then the outgoing and finally, the element.
     *
     * @param editingDomain The domain on which the command will be executed.
     * @param compoundCommand the compound command that will contains the commands.
     * @param impactModel the container of the {@link COREImpactNode}
     * @param impactModelElement the element to delete
     * @param root the root key of the Container layout
     * @param deleteChildren if the children has to be deleted or not
     * @param elementsAlreadyDeleted the set that contains the elements already deleted in order to not delete an
     *            element twice.
     */
    protected void addCommandDeleteElement(EditingDomain editingDomain, CompoundCommand compoundCommand,
            COREImpactModel impactModel, COREImpactNode impactModelElement, COREImpactNode root,
            boolean deleteChildren, Set<COREImpactNode> elementsAlreadyDeleted) {
        // Remove incoming contribution
        for (COREContribution incoming : impactModelElement.getIncoming()) {
            COREImpactNode child = incoming.getSource();
            if (!elementsAlreadyDeleted.contains(child)) {
                if (deleteChildren) {
                    if (child instanceof COREFeatureImpactNode) {
                        compoundCommand.append(ControllerFactory.INSTANCE.getFeatureImpactController()
                                .createDeleteImpactModelElementCommand(editingDomain, impactModel, root,
                                        child, deleteChildren, elementsAlreadyDeleted));
                    } else {
                        compoundCommand.append(ControllerFactory.INSTANCE.getImpactModelElementController()
                                .createDeleteImpactModelElementCommand(editingDomain, impactModel, root,
                                        child, deleteChildren, elementsAlreadyDeleted));
                    }
                } else {
                    compoundCommand.append(ControllerFactory.INSTANCE.getContributionController()
                            .createRemoveContributionCommand(impactModel, incoming));
                }
            }
        }

        // Remove outgoing contribution
        for (COREContribution outgoing : impactModelElement.getOutgoing()) {
            compoundCommand.append(ControllerFactory.INSTANCE.getContributionController()
                    .createRemoveContributionCommand(impactModel, outgoing));
        }

        Collection<COREImpactNode> rootsByElement =
                COREImpactModelUtil.getRootByElement(impactModel, impactModelElement);

        for (COREImpactNode rootElement : rootsByElement) {
            // Create command for removing ElementMap (includes the layout element).
            compoundCommand.append(createRemoveLayoutCommand(editingDomain, impactModel.getLayouts(), rootElement,
                    impactModelElement));
        }

        LayoutContainerMapImpl layoutContainerMapEntry =
                EMFModelUtil.getEntryFromMap(impactModel.getLayouts(), impactModelElement);

        if (layoutContainerMapEntry != null) {
            // Create command to remove Layout Container Map Entry
            compoundCommand.append(RemoveCommand.create(editingDomain, impactModel,
                    CorePackage.Literals.CORE_IMPACT_MODEL__LAYOUTS, layoutContainerMapEntry));
        }

        compoundCommand.append(RemoveCommand.create(editingDomain, impactModel,
                CorePackage.Literals.CORE_IMPACT_MODEL__IMPACT_MODEL_ELEMENTS, impactModelElement));
    }

    /**
     * Append to the compoundCommand the commands when we have to remove the {@link COREImpactNode} from the
     * layout container. First we remove the outgoing we have to delete (The ones that linked elements that are inside
     * the same container layout). Then we remove all the descendant of this element from the layout container that
     * are
     * not linked to an element of this container layout.
     *
     * @param editingDomain The domain on which the command will be executed.
     * @param compoundCommand the compound command that will contains the commands.
     * @param impactModel the container of the {@link COREImpactNode}
     * @param impactModelElement the element to delete
     * @param root the root key of the Container layout
     * @param outgoingsToDelete the outgoing contribution to delete
     * @param deleteChildren if the children has to be deleted or not
     * @param elementsAlreadyDeleted the set that contains the elements already deleted in order to not delete an
     *            element twice.
     */
    // CHECKSTYLE:IGNORE ParameterNumberCheck: Split this operation in two methods will affect the readability
    protected void addCommandRemoveLayout(EditingDomain editingDomain, CompoundCommand compoundCommand,
            COREImpactModel impactModel, COREImpactNode impactModelElement, COREImpactNode root,
            List<COREContribution> outgoingsToDelete, boolean deleteChildren,
            Set<COREImpactNode> elementsAlreadyDeleted) {

        // Remove incoming elements if they are in the layout container map using this root.
        LayoutContainerMapImpl containerMap = EMFModelUtil.getEntryFromMap(impactModel.getLayouts(), root);

        for (COREContribution incoming : impactModelElement.getIncoming()) {
            COREImpactNode child = incoming.getSource();

            if (containerMap.getValue().containsKey(child)) {
                if (!elementsAlreadyDeleted.contains(child)) {
                    if (deleteChildren) {
                        addCommandRemoveLayout(editingDomain, compoundCommand, impactModel,
                                child, root, new ArrayList<COREContribution>(), deleteChildren,
                                elementsAlreadyDeleted);
                    } else {
                        compoundCommand.append(ControllerFactory.INSTANCE.getContributionController()
                                .createRemoveContributionCommand(impactModel, incoming));
                    }
                }
            }
        }

        // Remove outgoing contribution
        for (COREContribution outgoing : outgoingsToDelete) {
            compoundCommand.append(ControllerFactory.INSTANCE.getContributionController()
                    .createRemoveContributionCommand(impactModel, outgoing));
        }

        // Create command for removing ElementMap (includes the layout element).
        compoundCommand.append(createRemoveLayoutCommand(editingDomain, impactModel.getLayouts(), root,
                impactModelElement));
    }
}
