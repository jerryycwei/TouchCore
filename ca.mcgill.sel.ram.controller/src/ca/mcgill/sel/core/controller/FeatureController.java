package ca.mcgill.sel.core.controller;

import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.core.COREConcern;
import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.core.COREFeatureImpactNode;
import ca.mcgill.sel.core.COREFeatureRelationshipType;
import ca.mcgill.sel.core.COREImpactModel;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.core.COREModel;
import ca.mcgill.sel.core.COREModelReuse;
import ca.mcgill.sel.core.COREReuse;
import ca.mcgill.sel.core.CoreFactory;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.Instantiation;
import ca.mcgill.sel.ram.RamFactory;
import ca.mcgill.sel.ram.RamPackage;
import ca.mcgill.sel.ram.util.RAMModelUtil;
import ca.mcgill.sel.ucm.UseCaseMap;

/**
 * The controller for {link @Feature}.
 *
 * @author Nishanth
 * @author jerrywei
 */
public class FeatureController extends CoreBaseController {

    /**
     * Creates a new instance of {@link FeatureController}.
     */
    FeatureController() {
        // prevent anyone outside this package to instantiate
    }

    /**
     * Controller to add new feature (FeatureName passed as string) to feature.
     *
     * @param concern - The concern which is the editing domain
     * @param position - The position at which the new feature is to be added.
     * @param parentFeature - The feature for which a new child is to be added.
     * @param newFeature - The name of the new child feature to be added.
     * @param association - The association between the new feature and the current feature.
     */
    public void addFeature(COREConcern concern, int position, COREFeature parentFeature, String newFeature,
            COREFeatureRelationshipType association) {

        // Create the new COREFeature
        COREFeature newCOREFeature = CoreFactory.eINSTANCE.createCOREFeature();
        newCOREFeature.setName(newFeature);
        newCOREFeature.setParentRelationship(association);

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(concern);

        CompoundCommand compoundCommand = new CompoundCommand();

        if (parentFeature.getChildren().size() > 0) {
            COREFeatureRelationshipType rel = parentFeature.getChildren().get(0).getParentRelationship();
            // If the relationship is XOR or OR and it was not the case for other children, update their relationship
            if ((association == COREFeatureRelationshipType.XOR || association == COREFeatureRelationshipType.OR)
                    && rel != association) {
                compoundCommand.append(getChangeLinkCommand(editingDomain, parentFeature, association));
                // If the relationship is MANDATORY and children are XOR or OR, update children relationship to OPTIONAL
            } else if ((association == COREFeatureRelationshipType.MANDATORY
                    || association == COREFeatureRelationshipType.OPTIONAL)
                    && (rel == COREFeatureRelationshipType.XOR || rel == COREFeatureRelationshipType.OR)) {
                compoundCommand.append(getChangeLinkCommand(editingDomain, parentFeature,
                        COREFeatureRelationshipType.OPTIONAL));
            }
        }

        Command addChildCommand = AddCommand.create(editingDomain, concern.getFeatureModel(),
                CorePackage.Literals.CORE_FEATURE_MODEL__FEATURES, newCOREFeature);

        Command addFeatureAsChildCommand = AddCommand.create(editingDomain, parentFeature,
                CorePackage.Literals.CORE_FEATURE__CHILDREN, newCOREFeature, position);

        compoundCommand.append(addFeatureAsChildCommand);
        compoundCommand.append(addChildCommand);

        doExecute(editingDomain, compoundCommand);
    }

    /**
     * Add an already existing a feature as a child of another one.
     *
     * @param concern - The concern which is the editing domain.
     * @param feature - The feature to add
     * @param parentFeature - The parent feature
     * @param association - The {@link COREFeatureRelationshipType} with the parent feature.
     */
    public void addExistingFeature(COREConcern concern, COREFeature feature, COREFeature parentFeature,
            COREFeatureRelationshipType association) {

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(concern);

        // First set the association to the new parent.
        Command setCommand = SetCommand.create(editingDomain, feature,
                CorePackage.Literals.CORE_FEATURE__PARENT_RELATIONSHIP, association);

        // Second remove the feature from Existing parent
        Command removeChildCommand = RemoveCommand.create(editingDomain, feature.getParent(),
                CorePackage.Literals.CORE_FEATURE__CHILDREN, feature);

        // Add to the new Parent
        Command addChildCommand = AddCommand.create(editingDomain, parentFeature,
                CorePackage.Literals.CORE_FEATURE__CHILDREN, feature);

        CompoundCommand compoundCommand = new CompoundCommand();

        compoundCommand.append(setCommand);
        compoundCommand.append(removeChildCommand);
        compoundCommand.append(addChildCommand);

        doExecute(editingDomain, compoundCommand);
    }

    /**
     * Move a feature to a new position.
     *
     * @param concern - The concern containing the feature
     * @param parent - The parent of the feature
     * @param feature - The feature to move
     * @param newPosition - The new position of the feature
     */
    public void changePositionOfFeature(COREConcern concern, COREFeature parent, COREFeature feature, int newPosition) {

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(concern);

        Command changePositionCommand =
                MoveCommand.create(editingDomain, parent, CorePackage.Literals.CORE_FEATURE__CHILDREN, feature,
                        newPosition);

        doExecute(editingDomain, changePositionCommand);
    }

    /**
     * Controller to delete a feature.
     *
     * @param concern - The concern which is the editing domain.
     * @param listOfFeatures - The list of features to be deleted.
     */
    public void deleteFeature(COREConcern concern, List<COREFeature> listOfFeatures) {

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(concern);

        CompoundCommand compoundCommand = new CompoundCommand();

        // Delete constraints from the feature
        for (COREFeature toDelete : listOfFeatures) {
            for (COREFeature feature : toDelete.getExcludes()) {
                Command removeConstraint = RemoveCommand.create(editingDomain, toDelete,
                        CorePackage.Literals.CORE_FEATURE__EXCLUDES, feature);
                compoundCommand.append(removeConstraint);
            }
            for (COREFeature feature : toDelete.getRequires()) {
                Command removeConstraint = RemoveCommand.create(editingDomain, toDelete,
                        CorePackage.Literals.CORE_FEATURE__REQUIRES, feature);
                compoundCommand.append(removeConstraint);
            }
        }

        // Delete constraints to the feature
        for (COREFeature feature : concern.getFeatureModel().getFeatures()) {
            for (COREFeature toDelete : listOfFeatures) {
                if (feature.getExcludes().contains(toDelete)) {
                    Command removeConstraint = RemoveCommand.create(editingDomain, feature,
                            CorePackage.Literals.CORE_FEATURE__EXCLUDES, toDelete);
                    compoundCommand.append(removeConstraint);
                }
                if (feature.getRequires().contains(toDelete)) {
                    Command removeConstraint = RemoveCommand.create(editingDomain, feature,
                            CorePackage.Literals.CORE_FEATURE__REQUIRES, toDelete);
                    compoundCommand.append(removeConstraint);
                }
            }
        }

        // Delete COREFeatureImpacts
        COREImpactModel impactModel = concern.getImpactModel();
        if (impactModel != null) {
            for (COREImpactNode impactModelElement : impactModel.getImpactModelElements()) {
                if (impactModelElement instanceof COREFeatureImpactNode) {
                    COREFeatureImpactNode featureImpact = (COREFeatureImpactNode) impactModelElement;
                    if (listOfFeatures.contains(featureImpact.getRepresents())) {
                        compoundCommand.append(ControllerFactory.INSTANCE.getFeatureImpactController()
                                .createDeleteImpactModelElementCommand(editingDomain, impactModel, null,
                                        featureImpact, false, new HashSet<COREImpactNode>()));
                    }
                }
            }
        }

        // Delete features
        for (COREFeature feature : listOfFeatures) {

            // Delete Reuses
            for (COREReuse reuse : feature.getReuses()) {
                compoundCommand.append(RemoveCommand.create(editingDomain, feature,
                        CorePackage.Literals.CORE_FEATURE__REUSES, reuse));
            }

            // Delete link from realization models
            for (COREModel model : feature.getRealizedBy()) {
                compoundCommand.append(RemoveCommand.create(editingDomain, feature,
                        CorePackage.Literals.CORE_FEATURE__REALIZED_BY, model));
            }

            Command removeCommand = RemoveCommand.create(editingDomain, feature.getParent(),
                    CorePackage.Literals.CORE_FEATURE__CHILDREN, feature);
            Command deleteCommand = DeleteCommand.create(editingDomain, feature);
            compoundCommand.append(deleteCommand);
            compoundCommand.append(removeCommand);
        }

        doExecute(editingDomain, compoundCommand);
    }

    /**
     * Controller used to change the link of the feature between itself and its children / parent. If the relationship
     * is a mandatory / optional it is called on the child with respect to its parent. Else if the relationship is XOR /
     * OR it is called on the parent.
     *
     * @param concern - The concern which is the editing domain.
     * @param feature - The feature (child / parent) depending on the association.
     * @param relationship - The new relationship which should be on the feature.
     */
    public void changeFeatureLink(COREConcern concern, COREFeature feature, COREFeatureRelationshipType relationship) {

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(concern);

        if (relationship == COREFeatureRelationshipType.OPTIONAL
                || relationship == COREFeatureRelationshipType.MANDATORY) {

            doSet(feature, CorePackage.Literals.CORE_FEATURE__PARENT_RELATIONSHIP, relationship);

        } else {

            CompoundCommand compoundCommand = getChangeLinkCommand(editingDomain, feature, relationship);

            doExecute(editingDomain, compoundCommand);
        }
    }

    /**
     * Create a coumpound command for changing {@link COREFeatureRelationshipType} of a feature's children to a new one.
     *
     * @param editingDomain - The editing domain.
     * @param feature - The parent feature to change the association.
     * @param newRelationship - The new relationship which should be on the feature.
     * @return CompoundCommand which changes the relationship of all children features.
     */
    private CompoundCommand getChangeLinkCommand(EditingDomain editingDomain, COREFeature feature,
            COREFeatureRelationshipType newRelationship) {
        CompoundCommand compoundCommand = new CompoundCommand();

        for (COREFeature child : feature.getChildren()) {
            Command setCommand =
                    SetCommand.create(editingDomain, child, CorePackage.Literals.CORE_FEATURE__PARENT_RELATIONSHIP,
                            newRelationship);
            compoundCommand.append(setCommand);
        }
        return compoundCommand;
    }

    /**
     * Add an aspect to a concern's models.
     *
     * @param concern - The concerner
     * @param aspect - The aspect to add
     */
    public void addModelToConcern(COREConcern concern, Aspect aspect) {

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(aspect);

        Command setConcernForModelCommand =
                SetCommand.create(editingDomain, aspect, CorePackage.Literals.CORE_MODEL__CORE_CONCERN, concern);

        doExecute(editingDomain, setConcernForModelCommand);

    }

    /**
     * Controller used to associate an aspect with a feature.
     *
     * @param concern - The concern which is the editing domain.
     * @param feature - The feature with which an aspect is to be associated.
     * @param aspect - The aspect which is realized by the feature.
     */
    public void associateAspect(COREConcern concern, COREFeature feature, Aspect aspect) {
        Aspect newAspect = aspect;
        // Resolve the proxy for the aspect if any
        if (aspect.eIsProxy()) {
            newAspect = (Aspect) EcoreUtil.resolve(aspect, concern);
        }

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(concern);
        CompoundCommand compoundCommand = new CompoundCommand();

        // If the aspect is not part of the concern models, add it and clear its realizations
        if (!concern.getModels().contains(newAspect)) {
            // Add the aspect to the concern models
            compoundCommand.append(AddCommand.create(editingDomain, concern,
                    CorePackage.Literals.CORE_CONCERN__MODELS, newAspect));
            // Clear the aspect realizations
            for (COREFeature rf : newAspect.getRealizes()) {
                compoundCommand.append(RemoveCommand.create(editingDomain, newAspect,
                        CorePackage.Literals.CORE_MODEL__REALIZES, rf));
            }
        }

        Command addAspectCommand = AddCommand.create(editingDomain, feature,
                CorePackage.Literals.CORE_FEATURE__REALIZED_BY, newAspect);
        compoundCommand.append(addAspectCommand);

        doExecute(editingDomain, compoundCommand);
    }

    /**
     * Controller used to delete an association of the aspect from the feature.
     *
     * @param concern - The concern which is the editing domain.
     * @param feature - The feature whose realization is to be deleted.
     */
    public void removeAspect(COREConcern concern, COREFeature feature) {

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(concern);

        Command removeAspectCommand =
                RemoveCommand.create(editingDomain, feature, CorePackage.Literals.CORE_FEATURE__REALIZED_BY,
                        feature.getRealizedBy());

        doExecute(editingDomain, removeAspectCommand);

    }

    /**
     * Controller used to delete an association of the aspect from the feature.
     *
     * @param concern - The concern which is the editing domain.
     * @param feature - The feature whose realization is to be deleted.
     * @param aspect - The aspect which is realized by the feature.
     */
    public void removeAspect(COREConcern concern, COREFeature feature, Aspect aspect) {

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(concern);

        Aspect newAspect = aspect;
        // Resolve the proxy for the aspect if any
        if (aspect.eIsProxy()) {
            newAspect = (Aspect) EcoreUtil.resolve(aspect, concern);
        }

        Command removeAspectCommand =
                RemoveCommand.create(editingDomain, feature, CorePackage.Literals.CORE_FEATURE__REALIZED_BY, newAspect);

        doExecute(editingDomain, removeAspectCommand);

    }

    /**
     * Controller used to add a reuse of another concern.
     *
     * @param concern - The concern which is the editing domain.
     * @param feature - The feature for which the reuse is being added.
     * @param reuse - The reuse which is added to the feature.
     * @return addReuseCommand the reuseCommand to be returned.
     */
    public Command addReuse(COREConcern concern, COREFeature feature, COREReuse reuse) {
        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(concern);

        Command addReuseCommand =
                AddCommand.create(editingDomain, feature, CorePackage.Literals.CORE_FEATURE__REUSES, reuse);

        return addReuseCommand;
    }

    /**
     * Create a 'A requires B' constraint between two features.
     *
     * @param concern - The concern which is the editing domain.
     * @param owner - The feature which owns the constraint
     * @param constraint - The feature which is the target of the constraint
     */
    public void addRequiresConstraint(COREConcern concern, COREFeature owner, COREFeature constraint) {

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(concern);

        Command addConstraint =
                AddCommand.create(editingDomain, owner, CorePackage.Literals.CORE_FEATURE__REQUIRES, constraint);

        doExecute(editingDomain, addConstraint);
    }

    /**
     * Create an exclude constraint between two features.
     *
     * @param concern - The concern which is the editing domain.
     * @param owner - The feature which owns the constraint
     * @param constraint - The feature which is the target of the constraint
     */
    public void addExcludesConstraint(COREConcern concern, COREFeature owner, COREFeature constraint) {

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(concern);

        Command addConstraint =
                AddCommand.create(editingDomain, owner, CorePackage.Literals.CORE_FEATURE__EXCLUDES, constraint);

        doExecute(editingDomain, addConstraint);
    }

    /**
     * Remove the require constraint between two features.
     *
     * @param concern - The concern which is the editing domain.
     * @param owner - The feature which owns the constraint
     * @param constraint - The feature which is the target of the constraint
     */
    public void removeRequiresConstraint(COREConcern concern, COREFeature owner, COREFeature constraint) {

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(concern);

        Command removeConstraint =
                RemoveCommand.create(editingDomain, owner, CorePackage.Literals.CORE_FEATURE__REQUIRES, constraint);

        doExecute(editingDomain, removeConstraint);
    }

    /**
     * Remove the exclude constraint between two features.
     *
     * @param concern - The concern which is the editing domain.
     * @param owner - The feature which owns the constraint
     * @param constraint - The feature which is the target of the constraint
     */
    public void removeExcludesConstraint(COREConcern concern, COREFeature owner, COREFeature constraint) {

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(concern);

        Command removeConstraint =
                RemoveCommand.create(editingDomain, owner, CorePackage.Literals.CORE_FEATURE__EXCLUDES, constraint);

        doExecute(editingDomain, removeConstraint);
    }

    /**
     * Deletes a {@link COREModelReuse}.
     *
     * @param eachModel - The {@link COREModel} which is the editing domain.
     * @param modelReuse - The model reuse to delete
     */
    public void removeModelReuse(COREModel eachModel, COREModelReuse modelReuse) {

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(eachModel);

        Command deleteCommand = DeleteCommand.create(editingDomain, modelReuse);

        doExecute(editingDomain, deleteCommand);

    }

    /**
     * Remove a {@link COREReuse} from a feature.
     *
     * @param concern - The concern which is the editing domain.
     * @param owner - The feature which makes the reuse
     * @param reuse - The reuse to delete
     */
    public void removeReuse(COREConcern concern, COREFeature owner, COREReuse reuse) {

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(concern);

        Command removeCommand =
                RemoveCommand.create(editingDomain, owner, CorePackage.Literals.CORE_FEATURE__REUSES, reuse);

        doExecute(editingDomain, removeCommand);
    }

    /**
     * Create a new aspect and associate it to the given feature.
     *
     * @param concern - The concern which is the editing domain.
     * @param feature - The feature realized by the new aspect.
     * @return the created {@link Aspect}
     */
    public Aspect createAspectAndAssociate(COREConcern concern, COREFeature feature) {
        Aspect aspect = RAMModelUtil.createAspect(feature.getName(), concern);
        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(aspect);

        CompoundCommand compoundCommand = new CompoundCommand();
        // Add the aspect to the concern models
        compoundCommand.append(SetCommand.create(editingDomain, aspect,
                CorePackage.Literals.CORE_MODEL__CORE_CONCERN, concern));
        // Associate the aspect to the feature
        compoundCommand.append(AddCommand.create(editingDomain, aspect,
                CorePackage.Literals.CORE_MODEL__REALIZES, feature));
        // Extend aspect realizing parent feature if any
        Aspect externalAspect = RAMModelUtil.getParentAspect(feature);
        if (externalAspect != null) {
            Instantiation instantiation = RamFactory.eINSTANCE.createInstantiation();
            instantiation.setSource(externalAspect);
            compoundCommand.append(AddCommand.create(editingDomain, aspect,
                    RamPackage.Literals.ASPECT__INSTANTIATIONS, instantiation));
        }
        doExecute(editingDomain, compoundCommand);

        return aspect;
    }

    /**
     * Controller used to associate a ucm with a feature.
     *
     * @param concern - The concern which is the editing domain.
     * @param feature - The feature with which an ucm is to be associated.
     * @param selectedUCM - The ucm which is realized by the feature.
     */
    public void associateUCM(COREConcern concern, COREFeature feature, UseCaseMap selectedUCM) {
        UseCaseMap newUCM = selectedUCM;

        if (selectedUCM.eIsProxy()) {
            newUCM = (UseCaseMap) EcoreUtil.resolve(selectedUCM, concern);
        }

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(concern);
        CompoundCommand compoundCommand = new CompoundCommand();

        // If the ucm is not part of the concern models, add it and clear its realisations
        if (!concern.getModels().contains(newUCM)) {
            // Add the ucm to the concern models
            compoundCommand.append(AddCommand.create(editingDomain, concern,
                    CorePackage.Literals.CORE_CONCERN__MODELS, newUCM));
            // Clear the ucm realizations
            for (COREFeature rf : newUCM.getRealizes()) {
                compoundCommand.append(RemoveCommand.create(editingDomain, newUCM,
                        CorePackage.Literals.CORE_MODEL__REALIZES, rf));
            }
        }

        Command addAspectCommand = AddCommand.create(editingDomain, feature,
                CorePackage.Literals.CORE_FEATURE__REALIZED_BY, newUCM);
        compoundCommand.append(addAspectCommand);

        doExecute(editingDomain, compoundCommand);
    }

    /**
     * Controller used to delete an association of the ucm from the feature.
     *
     * @param concern - The concern which is the editing domain.
     * @param feature - The feature whose realization is to be deleted.
     * @param selectedUCM - The ucm which is realized by the feature.
     */
    public void removeUCM(COREConcern concern, COREFeature feature, UseCaseMap selectedUCM) {
        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(concern);

        UseCaseMap newUCM = selectedUCM;
        // Resolve the proxy for the aspect if any
        if (selectedUCM.eIsProxy()) {
            newUCM = (UseCaseMap) EcoreUtil.resolve(selectedUCM, concern);
        }

        Command removeAspectCommand =
                RemoveCommand.create(editingDomain, feature, CorePackage.Literals.CORE_FEATURE__REALIZED_BY, newUCM);

        doExecute(editingDomain, removeAspectCommand);

    }

}
