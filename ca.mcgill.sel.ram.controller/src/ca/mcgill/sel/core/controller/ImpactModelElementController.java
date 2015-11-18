package ca.mcgill.sel.core.controller;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.commons.emf.util.EMFModelUtil;
import ca.mcgill.sel.core.COREImpactModel;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.core.CoreFactory;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.core.LayoutElement;
import ca.mcgill.sel.core.impl.LayoutContainerMapImpl;

/**
 * The controller for {@link COREImpactNode}.
 *
 * @author Romain
 *
 */
public class ImpactModelElementController extends AbstractImpactModelElementController {

    /**
     * Creates a new instance of {@link ImpactModelElementController}.
     */
    ImpactModelElementController() {
        // prevent anyone outside this package to instantiate
    }

    /**
     * Create new root {@link COREImpactNode}.
     *
     * @param impactModel the {@link COREImpactModel} that will contains the root
     * @param name the name of the new {@link COREImpactNode}
     * @param x the x of the {@link LayoutElement} for the root goal
     * @param y the y of the {@link LayoutElement} for the root goal
     */
    public void createRootImpactModelElement(COREImpactModel impactModel, String name, float x, float y) {
        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(impactModel);

        Command command = createRootImpactModelElementCommand(editingDomain, impactModel, name, x, y);

        doExecute(editingDomain, command);
    }

    /**
     * Create a command that will create a new root {@link COREImpactNode}.
     *
     * @param editingDomain the domain for this new {@link Command}
     * @param impactModel the {@link COREImpactModel} that will contains the root
     * @param name the name of the new {@link COREImpactNode}
     * @param x the x of the {@link LayoutElement} for the root goal
     * @param y the y of the {@link LayoutElement} for the root goal
     * @return the {@link Command} created
     */
    protected Command createRootImpactModelElementCommand(EditingDomain editingDomain, COREImpactModel impactModel,
            String name, float x, float y) {
        COREImpactNode root = CoreFactory.eINSTANCE.createCOREImpactNode();
        root.setName(name);

        CompoundCommand compoundCommand = new CompoundCommand();

        compoundCommand.append(createImpactModelElementCommand(editingDomain, impactModel, root));
        compoundCommand.append(
                createLayoutContainerCommand(editingDomain, impactModel, root, x, y));

        return compoundCommand;
    }

    /**
     * Create new {@link COREImpactNode} and add a layout element for this new {@link COREImpactNode}.
     *
     * @param impactModel the {@link COREImpactModel} that will contains the root
     * @param root the key of the {@link LayoutContainerMapImpl}
     * @param x the x of the {@link LayoutElement}
     * @param y the y of the {@link LayoutElement}
     * @param name the name of the new {@link COREImpactNode}
     * @return the {@link COREImpactNode} created
     */
    public COREImpactNode createImpactModelElement(COREImpactModel impactModel, COREImpactNode root,
            float x, float y, String name) {
        COREImpactNode impactModelElement = CoreFactory.eINSTANCE.createCOREImpactNode();
        impactModelElement.setName(name);

        LayoutContainerMapImpl layoutContainerMap = EMFModelUtil.getEntryFromMap(impactModel.getLayouts(), root);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(impactModel);

        CompoundCommand compoundCommand = new CompoundCommand();

        Command addImpactModelElementCommand =
                createImpactModelElementCommand(editingDomain, impactModel, impactModelElement);
        compoundCommand.append(addImpactModelElementCommand);

        Command addlayoutElementCommand =
                createLayoutElementCommand(editingDomain, layoutContainerMap, root, impactModelElement, x, y);
        compoundCommand.append(addlayoutElementCommand);

        doExecute(editingDomain, compoundCommand);

        return impactModelElement;
    }

    /**
     * Create a new command that add a {@link COREImpactNode} to the {@link COREImpactModel}.
     *
     * @param editingDomain the domain for this new {@link Command}
     * @param impactModel the Impact model that will contain the new element.
     * @param impactModelElement the {@link COREImpactNode} to add
     * @return the {@link Command} created
     */
    private Command createImpactModelElementCommand(EditingDomain editingDomain, COREImpactModel impactModel,
            COREImpactNode impactModelElement) {
        Command addImpactModelElementCommand =
                AddCommand.create(editingDomain, impactModel,
                        CorePackage.Literals.CORE_IMPACT_MODEL__IMPACT_MODEL_ELEMENTS, impactModelElement);

        return addImpactModelElementCommand;
    }

    /**
     * Moves the given {@link COREImpactNode} to a new position.
     *
     * @param root the {@link COREImpactNode} that is the root of this impactModelElement
     * @param impactModelElement the {@link COREImpactNode} to move
     * @param x the new x position
     * @param y the new y position
     */
    public void moveImpactModelElement(COREImpactNode root, COREImpactNode impactModelElement, float x,
            float y) {
        EClass eclass = CorePackage.Literals.CORE_IMPACT_MODEL;

        COREImpactModel impactModel = EMFModelUtil.getRootContainerOfType(impactModelElement, eclass);
        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(impactModel);

        Command moveClassCommand =
                createMoveCommand(editingDomain, impactModel.getLayouts(), root, impactModelElement, x, y);

        doExecute(editingDomain, moveClassCommand);
    }

    /**
     * Create a new {@link LayoutElement} for this element using the root as the key of the
     * {@link LayoutContainerMapImpl}.
     *
     * @param root the key of the {@link LayoutContainerMapImpl}
     * @param elementMap the elements to update with their new position
     */
    public void moveImpactModelElements(COREImpactNode root,
            Map<COREImpactNode, SimpleEntry<Float, Float>> elementMap) {
        if (elementMap.isEmpty()) {
            return;
        }

        EClass eclass = CorePackage.Literals.CORE_IMPACT_MODEL;
        COREImpactModel impactModel = EMFModelUtil.getRootContainerOfType(root, eclass);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(impactModel);

        CompoundCommand compoundCommand = new CompoundCommand();

        for (COREImpactNode impactModelElement : elementMap.keySet()) {
            SimpleEntry<Float, Float> positionEntry = elementMap.get(impactModelElement);
            float x = positionEntry.getKey();
            float y = positionEntry.getValue();

            compoundCommand.append(createMoveCommand(editingDomain, impactModel.getLayouts(), root, impactModelElement,
                    x, y));
        }

        doExecute(editingDomain, compoundCommand);
    }

    /**
     * Create a new {@link LayoutElement} for this element using the root as the key of the
     * {@link LayoutContainerMapImpl}.
     *
     * @param root the key of the {@link LayoutContainerMapImpl}
     * @param element the {@link COREImpactNode} that will used this {@link LayoutElement}
     * @param x the x of the {@link LayoutElement}
     * @param y the y of the {@link LayoutElement}
     */
    public void createLayoutElement(COREImpactNode root, COREImpactNode element, float x, float y) {
        COREImpactModel impactModel = EMFModelUtil.getRootContainerOfType(root, CorePackage.Literals.CORE_IMPACT_MODEL);

        LayoutContainerMapImpl layoutContainerMap = EMFModelUtil.getEntryFromMap(impactModel.getLayouts(), root);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(impactModel);

        Command addlayoutElementCommand =
                createLayoutElementCommand(editingDomain, layoutContainerMap, root, element, x, y);

        doExecute(editingDomain, addlayoutElementCommand);
    }

    /**
     * Create a new {@link LayoutElement} for these elements using the root as the key of the
     * {@link LayoutContainerMapImpl}.
     *
     * @param root the key of the {@link LayoutContainerMapImpl}
     * @param elementMap the map of Element to update with their new position
     */
    public void createLayoutElements(COREImpactNode root,
            Map<COREImpactNode, AbstractMap.SimpleEntry<Float, Float>> elementMap) {
        if (elementMap.isEmpty()) {
            return;
        }

        COREImpactModel impactModel = EMFModelUtil.getRootContainerOfType(root, CorePackage.Literals.CORE_IMPACT_MODEL);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(impactModel);

        CompoundCommand compoundCommand = new CompoundCommand();

        LayoutContainerMapImpl layoutContainerMap = EMFModelUtil.getEntryFromMap(impactModel.getLayouts(), root);

        for (COREImpactNode impactModelElement : elementMap.keySet()) {
            SimpleEntry<Float, Float> positionEntry = elementMap.get(impactModelElement);
            float x = positionEntry.getKey();
            float y = positionEntry.getValue();

            compoundCommand.append(createLayoutElementCommand(editingDomain, layoutContainerMap, root,
                    impactModelElement, x, y));
        }

        doExecute(editingDomain, compoundCommand);
    }

    /**
     * Create a new {@link LayoutContainerMap} with this root element as a key.
     *
     * @param root the key of the {@link LayoutContainerMap}
     * @param x the x of the {@link LayoutElement} for the root goal
     * @param y the y of the {@link LayoutElement} for the root goal
     */
    public void createLayoutContainer(COREImpactNode root, float x, float y) {
        COREImpactModel impactModel = EMFModelUtil.getRootContainerOfType(root, CorePackage.Literals.CORE_IMPACT_MODEL);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(impactModel);

        Command command = this.createLayoutContainerCommand(editingDomain, impactModel, root, x, y);

        doExecute(editingDomain, command);
    }

    /**
     * Create a new command that creates a {@link LayoutContainerMapImpl} for this root. It also creates a layout
     * element for this root with the position 0,0.
     *
     * @param editingDomain the {@link EditingDomain}
     * @param impactModel the Impact model that contains the root.
     * @param root the key of the {@link LayoutContainerMapImpl}
     * @param x the x of the {@link LayoutElement} for the root goal
     * @param y the y of the {@link LayoutElement} for the root goal
     * @return the {@link Command} created
     */
    private Command createLayoutContainerCommand(EditingDomain editingDomain, COREImpactModel impactModel,
            COREImpactNode root, float x, float y) {
        LayoutContainerMapImpl layoutContainerMap =
                (LayoutContainerMapImpl) CoreFactory.eINSTANCE.create(CorePackage.Literals.LAYOUT_CONTAINER_MAP);
        layoutContainerMap.setKey(root);
        layoutContainerMap.setValue(new BasicEMap<EObject, LayoutElement>());

        CompoundCommand compoundCommand = new CompoundCommand();

        compoundCommand.append(AddCommand.create(editingDomain, impactModel,
                CorePackage.Literals.CORE_IMPACT_MODEL__LAYOUTS, layoutContainerMap));

        compoundCommand.append(createLayoutElementCommand(editingDomain, layoutContainerMap, root,
                root, x, y));

        return compoundCommand;
    }

    /**
     * This operation remove a {@link COREImpactNode} from the layout of this root. If all the outgoing
     * contribution of this element are in this layout container, then, we delete this {@link COREImpactNode} from the
     * model.
     *
     *
     * @param root the root key of the {@link LayoutContainerMapImpl}
     * @param impactModelElement the element to delete
     * @param deleteChildren if the children has to be deleted or not
     */
    public void removeImpactModelElement(COREImpactNode root, COREImpactNode impactModelElement,
            boolean deleteChildren) {
        COREImpactModel impactModel =
                EMFModelUtil.getRootContainerOfType(impactModelElement, CorePackage.Literals.CORE_IMPACT_MODEL);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(impactModel);

        Command command =
                this.createDeleteImpactModelElementCommand(editingDomain, impactModel, root, impactModelElement,
                        deleteChildren, new HashSet<COREImpactNode>());

        doExecute(editingDomain, command);
    }

    /**
     * This operation remove a {@link COREImpactNode} from the layout of this root. If all the outgoing
     * contribution of this element are in this layout container, then, we delete this {@link COREImpactNode} from the
     * model.
     *
     *
     * @param root the root key of the {@link LayoutContainerMapImpl}
     * @param impactModelElement the element to delete
     * @param deleteChildren if the children has to be deleted or not
     * @param shouldDeleteNodeFromModel if the element should be deleted from the model or not.
     */
    public void removeImpactModelElement(COREImpactNode root, COREImpactNode impactModelElement,
            boolean deleteChildren, boolean shouldDeleteNodeFromModel) {
        COREImpactModel impactModel =
                EMFModelUtil.getRootContainerOfType(impactModelElement, CorePackage.Literals.CORE_IMPACT_MODEL);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(impactModel);

        Command command =
                this.createDeleteImpactModelElementCommand(editingDomain, impactModel, root, impactModelElement,
                        deleteChildren, new HashSet<COREImpactNode>(), shouldDeleteNodeFromModel);

        doExecute(editingDomain, command);
    }

    /**
     * This operation remove a {@link COREImpactNode} that is a root goal from the model and from any layout. It
     * will also remove its children.
     *
     * @param impactModelElement the element to delete
     */
    public void removeRootGoal(COREImpactNode impactModelElement) {
        COREImpactModel impactModel =
                EMFModelUtil.getRootContainerOfType(impactModelElement, CorePackage.Literals.CORE_IMPACT_MODEL);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(impactModel);

        CompoundCommand compoundCommand = new CompoundCommand();

        addCommandDeleteElement(editingDomain, compoundCommand, impactModel, impactModelElement, impactModelElement,
                true, new HashSet<COREImpactNode>());

        doExecute(editingDomain, compoundCommand);
    }
}
