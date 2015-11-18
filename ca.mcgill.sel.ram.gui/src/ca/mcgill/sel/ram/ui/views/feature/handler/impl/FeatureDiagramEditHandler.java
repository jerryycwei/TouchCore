package ca.mcgill.sel.ram.ui.views.feature.handler.impl;

import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeEvent;
import org.mt4j.input.inputProcessors.componentProcessors.unistrokeProcessor.UnistrokeUtils.UnistrokeGesture;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.core.COREFeatureRelationshipType;
import ca.mcgill.sel.core.controller.ControllerFactory;
import ca.mcgill.sel.core.controller.FeatureController;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamSelectorComponent;
import ca.mcgill.sel.ram.ui.components.listeners.AbstractDefaultRamSelectorListener;
import ca.mcgill.sel.ram.ui.scenes.DisplayConcernEditScene;
import ca.mcgill.sel.ram.ui.utils.UnistrokeProcessorUtils;
import ca.mcgill.sel.ram.ui.views.AbstractView;
import ca.mcgill.sel.ram.ui.views.OptionSelectorView;
import ca.mcgill.sel.ram.ui.views.feature.FeatureView;
import ca.mcgill.sel.ram.ui.views.handler.impl.AbstractViewHandler;

/**
 * Handler for {@link ca.mcgill.sel.ram.ui.views.feature.FeatureDiagramEditView}.
 * 
 * @author CCamillieri
 */
public class FeatureDiagramEditHandler extends AbstractViewHandler {

    // Complete angle is 360 degrees, half of it is 180 degrees
    private static final int HALF_DEGREES = 180;
    // Complete angle is 360 degrees
    private static final int COMPLETE_DEGREES = 360;

    /**
     * Enum containing all the options to be shown when line is drawn on to another feature.
     */
    private enum ConstraintOptions {
        ADD_REQUIRES, ADD_EXCLUDES
    }

    @Override
    public void handleUnistrokeGesture(AbstractView<?> target, UnistrokeGesture gesture, Vector3D startPosition,
            UnistrokeEvent event) {
        DisplayConcernEditScene scene = RamApp.getActiveConcernEditScene();
        if (scene != null) {

            switch (gesture) {
                case X:
                    // if the gesture is a delete gesture
                    // get the start position of the gesture
                    Vector3D startPositionObtained = event.getCursor().getStartPosition();
                    // get the end position
                    Vector3D endPosition = event.getCursor().getPosition();
                    // delete class if the gesture is drawn over a class

                    deleteFeature(scene, startPositionObtained, endPosition, event.getCursor());
                    return;
            }

            /*
             * Check if it's an XOR Gesture
             */
            FeatureView resultFeature = null;
            resultFeature = scene.isXORCurve(event.getCursor().getStartPosX(), event.getCursor().getStartPosY(), event
                                .getCursor().getPosition().x, event.getCursor().getPosition().y);
            if (resultFeature != null && !resultFeature.isReuse()) {
                ControllerFactory.INSTANCE.getFeatureController().changeFeatureLink(scene.getConcern(),
                        resultFeature.getFeature(), COREFeatureRelationshipType.XOR);
                return;
            }

            /*
             * Check if it's an "add child" or "add constraint" gesture
             */
            // Check if the unistroke starts from a feature
            FeatureView startFeature = scene.liesAround(event.getCursor().getStartPosition());
            // Check if the unistroke ends on a feature
            FeatureView endFeature = scene.liesInside(event.getCursor().getPosition());

            if (startFeature != null && !startFeature.isReuse()) {
                if (endFeature == null) {
                    // Check if it can be added as a child
                    addChildFeature(event, startFeature, scene);
                } else if (!endFeature.isReuse() && startFeature != endFeature) {
                    // Show the Options for the constraints in UI
                    showConstraintOptions(event.getCursor().getPosition(), startFeature, endFeature);
                }
                return;
            }

            /*
             * Check if we are in association mode, if so, exit the mode
             */
            if (startFeature == null && endFeature == null && scene.isAspectSelected()) {
                scene.switchToEditMode();
            }
        }
    }

    /**
     * We need to check at what relative position to other children was the line drawn, and call the interface to add a
     * feature at that particular position. A minimum distance equal to the feature height is needed to be drawn, for
     * invoking the add Child option. If there exists no other child, an Optional child is added, else it added with the
     * same relationship as the other children.
     *
     * @param event the {@link UnistrokeEvent} that was received
     * @param startFeature the parent {@link FeatureView}
     * @param scene the {@link ca.mcgill.sel.ram.ui.scenes.AbstractConcernScene}
     */
    private static void addChildFeature(UnistrokeEvent event, FeatureView startFeature, DisplayConcernEditScene scene) {
        float deltaY = -(event.getCursor().getPosition().y - event.getCursor().getStartPosY());
        float deltaX = event.getCursor().getPosition().x - event.getCursor().getStartPosX();

        float x1 = event.getCursor().getPosition().x;
        float x2 = event.getCursor().getStartPosX();
        float y1 = event.getCursor().getPosition().y;
        float y2 = event.getCursor().getStartPosY();

        float distance = (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));

        // 'Degrees' is calculated relative to start position of the gesture and the end position of gesture.
        // If the unistroke gesture is drawn in the upward direction, then no function is called.
        float degrees = 0;
        degrees = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));

        if (degrees < 0) {
            degrees = COMPLETE_DEGREES + degrees;
        }

        if (degrees >= HALF_DEGREES && degrees <= COMPLETE_DEGREES && distance > startFeature.getHeight()) {

            int position = startFeature.getPosition(degrees);

            if (startFeature.getChildrenRelationship() == null) {
                startFeature.getHandler().addChild(scene, startFeature.getFeature(), position,
                        COREFeatureRelationshipType.OPTIONAL);
            } else {
                startFeature.getHandler().addChild(scene, startFeature.getFeature(), position,
                        startFeature.getChildrenRelationship());
            }

            return;
        }
    }

    /**
     * Delete the feature at the intersection point of the X gesture.
     *
     * @param scene the scene
     * @param startPosition the starting position of the gesture
     * @param endPosition the finish position of the gesture
     * @param inputCursor the details of the gesture
     */
    public void deleteFeature(DisplayConcernEditScene scene, Vector3D startPosition, Vector3D endPosition,
            InputCursor inputCursor) {

        Vector3D intersection = UnistrokeProcessorUtils.getIntersectionPoint(startPosition, endPosition, inputCursor);

        for (FeatureView fv : scene.collectFeatureViews(true)) {
            if (fv.containsPointGlobal(intersection)) {
                if (fv.getHandler() != null) {
                    fv.getHandler().deleteFeature(scene, fv);
                }
                break;
            }
        }

    }

    /*
     * ------------------------------------------ CONSTRAINTS-RELATED METHODS ------------------------------------------
     */
    /**
     * Function called a tapAndHold event is complete and options are to be shown. Called when constraint options
     * (Includes / Excludes) needs to be shown.
     *
     * @param position - The position where the options needs to be shown.
     * @param owner - The featureView for which constraint is the 'owner' of
     * @param target - The featureView for which constraint is the 'target' of
     */
    private void showConstraintOptions(Vector3D position, final FeatureView owner, final FeatureView target) {
        // No constraint between same feature or with reuses
        if (owner.getFeature() == target.getFeature() || owner.isReuse() || target.isReuse()
                || owner.isParentReuse() || target.isParentReuse()) {
            return;
        }

        ConstraintOptions[] options = getConstraintsAddOptions(owner.getFeature(), target.getFeature());
        if (options == null) {
            return;
        }
        OptionSelectorView<ConstraintOptions> selector = new OptionSelectorView<ConstraintOptions>(options);
        final DisplayConcernEditScene scene = RamApp.getActiveConcernEditScene();
        if (scene == null) {
            return;
        }

        scene.addComponent(selector, position);

        selector.registerListener(new AbstractDefaultRamSelectorListener<ConstraintOptions>() {
            @Override
            public void elementSelected(RamSelectorComponent<ConstraintOptions> selector, ConstraintOptions element) {
                FeatureController controller = ControllerFactory.INSTANCE.getFeatureController();
                switch (element) {
                    case ADD_REQUIRES:
                        controller.addRequiresConstraint(scene.getConcern(), owner.getFeature(), target.getFeature());
                        break;
                    case ADD_EXCLUDES:
                        controller.addExcludesConstraint(scene.getConcern(), owner.getFeature(), target.getFeature());
                        break;
                }
            }
        });
    }

    /**
     * Get the list of constraints that can be added between two given features. an 'excludes' constraint can be added
     * if and only if - no constraint have been defined between those features - the two features are not directly
     * parents an 'requires' constraint can be added if and only if - no excludes constraint exists between the features
     * - the given constraint doesn't already exist
     *
     * @param owner - The COREFeature for which constraint is the 'owner' of
     * @param target - The COREFeature for which constraint is the 'target' of
     * @return an array of {@link ConstraintOptions} proposed to the user, or null if none is valid
     */
    private ConstraintOptions[] getConstraintsAddOptions(COREFeature owner, COREFeature target) {
        // if there is already an exclude relationship between the two, we can't do anything
        // same thing if there the owner is a descendant of the target
        // if there is already a require constraint in this direction between the two, don't do anything
        if (owner.getExcludes().contains(target) || target.getExcludes().contains(owner)
                || isFeatureAscendant(target, owner) || isFeatureAscendant(owner, target)
                || owner.getRequires().contains(target)) {
            return null;
        }
        // if there is already a require relationship in the other direction for these elements
        // we can only add a requires constraint
        if (target.getRequires().contains(owner)) {
            return new ConstraintOptions[] { ConstraintOptions.ADD_REQUIRES };
        }
        return ConstraintOptions.values();
    }

    /**
     * Check if a feature is a parent of another one. Used to know if we can add a constraint between these features
     *
     * @param parent - The parent COREFeature
     * @param child - The child COREFeature
     * @return - true if child is a descendant of parent, false otherwise
     */
    private boolean isFeatureAscendant(COREFeature parent, COREFeature child) {
        if (parent.getChildren().contains(child)) {
            return true;
        }
        boolean ascendant = false;
        for (COREFeature directChild : parent.getChildren()) {
            if (isFeatureAscendant(directChild, child)) {
                ascendant = true;
            }
        }
        return ascendant;
    }

}
