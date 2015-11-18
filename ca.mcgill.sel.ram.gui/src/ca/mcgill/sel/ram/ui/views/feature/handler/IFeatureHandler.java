package ca.mcgill.sel.ram.ui.views.feature.handler;

import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.core.COREFeatureRelationshipType;
import ca.mcgill.sel.ram.ui.events.listeners.IDragListener;
import ca.mcgill.sel.ram.ui.events.listeners.ITapAndHoldListener;
import ca.mcgill.sel.ram.ui.events.listeners.ITapListener;
import ca.mcgill.sel.ram.ui.scenes.AbstractConcernScene;
import ca.mcgill.sel.ram.ui.scenes.DisplayConcernEditScene;
import ca.mcgill.sel.ram.ui.views.feature.FeatureView;

/**
 * Interface to handle events on the feature.
 * 
 * @author Nishanth
 *
 */
public interface IFeatureHandler extends ITapAndHoldListener, ITapListener, IDragListener {

    /**
     * After the drag event has completed,the new position in relation to its siblings has be be updated.
     * The following function calculates the new position with its parent and calls the reorder function.
     * 
     * @param featureIcon - The Feature icon on which the drag/pan action was completed.
     * @param scene - The current active concern scene.
     */
    void setPositionUpdate(FeatureView featureIcon, AbstractConcernScene<?, ?> scene);

    /**
     * Function used to check where the start and end of unistroke gesture classifies to be a XOR change operation.
     * 
     * @param startX - X position where the unistroke gesture was started.
     * @param startY - Y position where the unistroke gesture was started.
     * @param endX - X position where the unistroke gesture ended.
     * @param endY - Y position where the unistroke gesture ended.
     * @param featureIcon - The feature Icon on which the unistroke gestyre was drawn on.
     * @return boolean - Where the unistroke is in the first 30% section of the icon and ended in the last 30%.
     */
    boolean isXORCurve(float startX, float startY, float endX, float endY, FeatureView featureIcon);
    
    /**
     * Rename the given feature.
     * 
     * @param view - the {@link FeatureView} to rename
     */
    void renameFeature(FeatureView view);

    /**
     * Delete the given feature. Cannot delete the root. If the feature comes from a reuse, it is deleted only it is not
     * used anywhere
     * 
     * @param scene - The current scene
     * @param view - the {@link FeatureView} to delete
     */
    void deleteFeature(DisplayConcernEditScene scene, FeatureView view);

    /**
     * Hide the given feature.
     * 
     * @param scene - The current scene
     * @param view - the {@link FeatureView} to delete
     */
    void hideFeature(DisplayConcernEditScene scene, FeatureView view);

    /**
     * Add a child feature to the given feature.
     * 
     * @param scene - The current scene
     * @param feature - The parent feature
     * @param position - The position of the new child
     * @param optional - The {@link COREFeatureRelationshipType} of this child
     */
    void addChild(DisplayConcernEditScene scene, COREFeature feature, int position,
            COREFeatureRelationshipType optional);
}
