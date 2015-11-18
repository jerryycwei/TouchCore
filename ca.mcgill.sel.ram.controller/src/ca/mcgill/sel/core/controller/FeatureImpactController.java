package ca.mcgill.sel.core.controller;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.commons.emf.util.EMFModelUtil;
import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.core.COREFeatureImpactNode;
import ca.mcgill.sel.core.COREImpactModel;
import ca.mcgill.sel.core.COREImpactModelBinding;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.core.COREModel;
import ca.mcgill.sel.core.COREModelCompositionSpecification;
import ca.mcgill.sel.core.COREModelReuse;
import ca.mcgill.sel.core.COREReuse;
import ca.mcgill.sel.core.COREWeightedMapping;
import ca.mcgill.sel.core.CoreFactory;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.core.impl.LayoutContainerMapImpl;

/**
 * The controller for {@link COREFeatureImpactNode}.
 *
 * @author Romain
 *
 */
public class FeatureImpactController extends AbstractImpactModelElementController {

    /**
     * Creates a new instance of {@link FeatureImpactController}.
     */
    FeatureImpactController() {
        // prevent anyone outside this package to instantiate
    }

    /**
     * Create new {@link COREFeatureImpactNode}.
     *
     * @param impactModel the Impact model that will contain the new element.
     * @param feature the {@link COREFeature} associated to the new {@link COREFeatureImpactNode}
     * @return the {@link COREFeatureImpactNode} created
     */
    public COREFeatureImpactNode createFeatureImpact(COREImpactModel impactModel, COREFeature feature) {
        COREFeatureImpactNode featureImpact = CoreFactory.eINSTANCE.createCOREFeatureImpactNode();
        featureImpact.setRepresents(feature);
        featureImpact.setName(feature.getName());

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(impactModel);

        Command addFeatureImpactCommand = createFeatureImpactCommand(editingDomain, impactModel, featureImpact);

        doExecute(editingDomain, addFeatureImpactCommand);

        return featureImpact;
    }

    /**
     * Create new {@link COREFeatureImpactNode} and add a layout element for this new {@link COREFeatureImpactNode}.
     *
     * @param impactModel the Impact model that will contain the new element.
     * @param root the key of the LayoutContainerMapImpl
     * @param feature the {@link COREFeature} associated to the new {@link COREFeatureImpactNode}
     * @param x the x of the {@link LayoutElement}
     * @param y the y of the {@link LayoutElement}
     * @return the {@link COREImpactNode} created
     */
    public COREFeatureImpactNode createFeatureImpact(COREImpactModel impactModel, COREImpactNode root,
            COREFeature feature, float x, float y) {
        COREFeatureImpactNode featureImpact = CoreFactory.eINSTANCE.createCOREFeatureImpactNode();
        featureImpact.setRepresents(feature);
        featureImpact.setName(feature.getName());

        LayoutContainerMapImpl layoutContainerMap = EMFModelUtil.getEntryFromMap(impactModel.getLayouts(), root);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(impactModel);

        CompoundCommand compoundCommand = new CompoundCommand();

        Command addFeatureImpactCommand = createFeatureImpactCommand(editingDomain, impactModel, featureImpact);
        compoundCommand.append(addFeatureImpactCommand);

        Command addlayoutElementCommand =
                createLayoutElementCommand(editingDomain, layoutContainerMap, root, featureImpact, x, y);
        compoundCommand.append(addlayoutElementCommand);

        doExecute(editingDomain, compoundCommand);

        return featureImpact;
    }

    /**
     * Create a new command that add a {@link COREFeatureImpactNode} to the {@link COREImpactModel}.
     *
     * @param editingDomain the domain for this new {@link Command}
     * @param impactModel the Impact model that will contain the new element.
     * @param featureImpact the {@link COREFeatureImpactNode} to add
     * @return the {@link Command} created
     */
    private Command createFeatureImpactCommand(EditingDomain editingDomain, COREImpactModel impactModel,
            COREFeatureImpactNode featureImpact) {
        Command addFeatureImpactCommand =
                AddCommand.create(editingDomain, impactModel,
                        CorePackage.Literals.CORE_IMPACT_MODEL__IMPACT_MODEL_ELEMENTS, featureImpact);

        return addFeatureImpactCommand;
    }

    /**
     * This operation remove a {@link COREImpactNode} from the layout of this root. If all the outgoing
     * contribution of this element are in this layout container, then, we delete this {@link COREImpactNode} from the
     * model.
     *
     * @param root the root key of the LayoutContainerMap
     * @param featureImpact the element to delete
     * @param deleteChildren if the children has to be deleted or not
     */
    public void removeFeatureImpact(COREImpactNode root, COREFeatureImpactNode featureImpact,
            boolean deleteChildren) {
        COREImpactModel impactModel =
                EMFModelUtil.getRootContainerOfType(featureImpact, CorePackage.Literals.CORE_IMPACT_MODEL);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(impactModel);

        Command command =
                this.createDeleteImpactModelElementCommand(editingDomain, impactModel, root, featureImpact,
                        deleteChildren, new HashSet<COREImpactNode>());

        doExecute(editingDomain, command);
    }

    /**
     * This operation remove a {@link COREImpactNode} from the layout of this root. If all the outgoing
     * contribution of this element are in this layout container, then, we delete this {@link COREImpactNode} from the
     * model.
     *
     * @param root the root key of the LayoutContainerMap
     * @param featureImpact the element to delete
     * @param deleteChildren if the children has to be deleted or not
     * @param shouldDeleteNodeFromModel if the element should be deleted from the model or not.
     */
    public void removeFeatureImpact(COREImpactNode root, COREFeatureImpactNode featureImpact,
            boolean deleteChildren, boolean shouldDeleteNodeFromModel) {
        COREImpactModel impactModel =
                EMFModelUtil.getRootContainerOfType(featureImpact, CorePackage.Literals.CORE_IMPACT_MODEL);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(impactModel);

        Command command =
                this.createDeleteImpactModelElementCommand(editingDomain, impactModel, root, featureImpact,
                        deleteChildren, new HashSet<COREImpactNode>(), shouldDeleteNodeFromModel);

        doExecute(editingDomain, command);
    }

    @Override
    protected void addCommandDeleteElement(EditingDomain editingDomain, CompoundCommand compoundCommand,
            COREImpactModel impactModel, COREImpactNode impactModelElement, COREImpactNode root,
            boolean deleteChildren,
            Set<COREImpactNode> elementsAlreadyDeleted) {

        // Remove CoreWeightedMappings
        for (COREModelReuse modelReuse : impactModel.getModelReuses()) {
            int modelCompositionSpecificationToDelete = 0;
            for (COREModelCompositionSpecification<? extends COREModel> compositionSpecification : modelReuse
                    .getCompositions()) {
                if (compositionSpecification instanceof COREImpactModelBinding) {
                    COREImpactModelBinding impactModelBinding = (COREImpactModelBinding) compositionSpecification;

                    int mappingToDelete = 0;
                    for (COREWeightedMapping weightedMapping : impactModelBinding.getMappings()) {
                        if (weightedMapping.getTo() == impactModelElement) {

                            compoundCommand.append(RemoveCommand.create(editingDomain, impactModelElement,
                                    CorePackage.Literals.CORE_FEATURE_IMPACT_NODE__WEIGHTED_MAPPINGS, weightedMapping));

                            compoundCommand.append(RemoveCommand.create(editingDomain, impactModelBinding,
                                    CorePackage.Literals.CORE_BINDING__MAPPINGS, weightedMapping));

                            mappingToDelete++;

                            if (mappingToDelete == impactModelBinding.getMappings().size()) {

                                compoundCommand.append(RemoveCommand.create(editingDomain, modelReuse,
                                        CorePackage.Literals.CORE_MODEL_REUSE__COMPOSITIONS, impactModelBinding));

                                modelCompositionSpecificationToDelete++;

                                if (modelCompositionSpecificationToDelete == modelReuse.getCompositions().size()) {
                                    compoundCommand.append(RemoveCommand.create(editingDomain, impactModel,
                                            CorePackage.Literals.CORE_MODEL__MODEL_REUSES, modelReuse));
                                }
                            }
                        }
                    }
                }
            }
        }

        super.addCommandDeleteElement(editingDomain, compoundCommand, impactModel, impactModelElement, root,
                deleteChildren, elementsAlreadyDeleted);
    }

    /**
     * Create a new {@link COREWeightedMapping} between this {@link COREImpactNode} and a {@link COREFeatureImpactNode}.
     *
     * @param reuse The {@link COREReuse} of the concern that contains the source goal.
     * @param source the {@link COREImpactNode} that will be the "from" of this mapping.
     * @param target the {@link COREFeatureImpactNode} that will be the "to" of this mapping.
     */
    public void createWeightedMapping(COREReuse reuse, COREImpactNode source, COREFeatureImpactNode target) {
        COREImpactModel impactModel =
                EMFModelUtil.getRootContainerOfType(target, CorePackage.Literals.CORE_IMPACT_MODEL);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(impactModel);

        CompoundCommand compoundCommand = new CompoundCommand();

        // CoreModelReuse
        COREModelReuse impactModelReuse = null;

        for (COREModelReuse modelReuse : impactModel.getModelReuses()) {
            if (modelReuse.getReuse() == reuse) {
                impactModelReuse = modelReuse;
                break;
            }
        }

        if (impactModelReuse == null) {
            impactModelReuse = CoreFactory.eINSTANCE.createCOREModelReuse();
            impactModelReuse.setReuse(reuse);

            compoundCommand.append(AddCommand.create(editingDomain, impactModel,
                    CorePackage.Literals.CORE_MODEL__MODEL_REUSES, impactModelReuse));
        }

        // CoreImpactModelBinding
        COREImpactModelBinding impactModelBinding = null;

        for (COREModelCompositionSpecification<? extends COREModel> compositionSpecification : impactModelReuse
                .getCompositions()) {
            if (compositionSpecification instanceof COREImpactModelBinding) {
                impactModelBinding = (COREImpactModelBinding) compositionSpecification;
                break;
            }
        }

        if (impactModelBinding == null) {
            impactModelBinding = CoreFactory.eINSTANCE.createCOREImpactModelBinding();

            compoundCommand.append(AddCommand.create(editingDomain, impactModelReuse,
                    CorePackage.Literals.CORE_MODEL_REUSE__COMPOSITIONS, impactModelBinding));
        }

        // CoreWeightedMapping
        COREWeightedMapping weightedMapping = CoreFactory.eINSTANCE.createCOREWeightedMapping();
        weightedMapping.setFrom(source);
        weightedMapping.setTo(target);

        compoundCommand.append(AddCommand.create(editingDomain, impactModelBinding,
                CorePackage.Literals.CORE_BINDING__MAPPINGS, weightedMapping));

        compoundCommand.append(AddCommand.create(editingDomain, target,
                CorePackage.Literals.CORE_FEATURE_IMPACT_NODE__WEIGHTED_MAPPINGS, weightedMapping));

        doExecute(editingDomain, compoundCommand);
    }

    /**
     * Delete a {@link COREWeightedMapping}.
     *
     * @param weightedMapping The {@link COREWeightedMapping} that we want to delete.
     */
    public void deleteWeightedMapping(COREWeightedMapping weightedMapping) {
        COREImpactNode to = weightedMapping.getTo();

        COREImpactModel impactModel = EMFModelUtil.getRootContainerOfType(to, CorePackage.Literals.CORE_IMPACT_MODEL);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(impactModel);

        CompoundCommand compoundCommand = new CompoundCommand();

        for (COREModelReuse modelReuse : impactModel.getModelReuses()) {
            for (COREModelCompositionSpecification<? extends COREModel> compositionSpecification : modelReuse
                    .getCompositions()) {
                if (compositionSpecification instanceof COREImpactModelBinding) {
                    COREImpactModelBinding impactModelBinding = (COREImpactModelBinding) compositionSpecification;

                    if (impactModelBinding.getMappings().contains(weightedMapping)) {

                        compoundCommand.append(RemoveCommand.create(editingDomain, to,
                                CorePackage.Literals.CORE_FEATURE_IMPACT_NODE__WEIGHTED_MAPPINGS, weightedMapping));

                        compoundCommand.append(RemoveCommand.create(editingDomain, impactModelBinding,
                                CorePackage.Literals.CORE_BINDING__MAPPINGS, weightedMapping));

                        if (impactModelBinding.getMappings().size() == 1) {

                            compoundCommand.append(RemoveCommand.create(editingDomain, modelReuse,
                                    CorePackage.Literals.CORE_MODEL_REUSE__COMPOSITIONS, impactModelBinding));

                            if (modelReuse.getCompositions().size() == 1) {

                                compoundCommand.append(RemoveCommand.create(editingDomain, impactModel,
                                        CorePackage.Literals.CORE_MODEL__MODEL_REUSES, modelReuse));
                            }
                        }

                        break;
                    }
                }
            }
        }

        doExecute(editingDomain, compoundCommand);
    }

}
