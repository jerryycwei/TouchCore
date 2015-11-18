package ca.mcgill.sel.ram.ui.views.feature.handler.impl;

import java.util.EnumSet;

import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.core.COREFeatureRelationshipType;
import ca.mcgill.sel.core.controller.ControllerFactory;
import ca.mcgill.sel.core.util.COREModelUtil;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamSelectorComponent;
import ca.mcgill.sel.ram.ui.components.listeners.AbstractDefaultRamSelectorListener;
import ca.mcgill.sel.ram.ui.scenes.AbstractConcernScene;
import ca.mcgill.sel.ram.ui.scenes.DisplayConcernEditScene;
import ca.mcgill.sel.ram.ui.utils.Strings;
import ca.mcgill.sel.ram.ui.views.OptionSelectorView;
import ca.mcgill.sel.ram.ui.views.feature.FeatureView;
import ca.mcgill.sel.ram.ui.views.feature.handler.IFeatureHandler;
import ca.mcgill.sel.ram.ui.views.handler.BaseHandler;

/**
 * Feature Handler, used to handle events on the Feature.
 * 
 * @author CCamillieri
 */
public class ReuseEditModeHandler extends BaseHandler implements IFeatureHandler {

    /**
     * Enum containing all the options to be shown when tap and hold is complete.
     */
    private enum ReuseActions {
        RENAME_REUSE, DELETE_REUSE, COLLAPSE, EXPAND
    }

    @Override
    public boolean processTapAndHoldEvent(TapAndHoldEvent tapAndHoldEvent) {
        // Get the current working concern scene.
        final DisplayConcernEditScene scene = RamApp.getActiveConcernEditScene();

        if (scene != null && tapAndHoldEvent.isHoldComplete()) {
            FeatureView featureIcon = (FeatureView) tapAndHoldEvent.getTarget();
            showFeatureOptions(scene, featureIcon, tapAndHoldEvent.getLocationOnScreen());
        }
        return true;
    }

    /**
     * Shows the options for the feature.
     * 
     * @param scene the current scene
     * @param view the view of the selected feature
     * @param location the location on the screen where the options should be shown
     */
    private static void showFeatureOptions(final DisplayConcernEditScene scene, final FeatureView view,
            Vector3D location) {
        OptionSelectorView<ReuseActions> selector = new OptionSelectorView<ReuseActions>(getActions(scene, view));

        scene.addComponent(selector, location);

        selector.registerListener(new AbstractDefaultRamSelectorListener<ReuseActions>() {
            @Override
            public void elementSelected(RamSelectorComponent<ReuseActions> selector, ReuseActions element) {
                switch (element) {
                    case RENAME_REUSE:
                        view.getHandler().renameFeature(view);
                        break;
                    case DELETE_REUSE:
                        view.getHandler().deleteFeature(scene, view);
                        break;
                    case COLLAPSE:
                    case EXPAND:
                        view.getHandler().hideFeature(scene, view);
                        break;
                }
            }
        });
    }

    /**
     * Get the possible actions on the given feature.
     * 
     * @param scene - The current scene
     * @param feature - The given {@link FeatureView}
     * @return the set of available actions
     */
    private static ReuseActions[] getActions(DisplayConcernEditScene scene, FeatureView feature) {
        EnumSet<ReuseActions> options = EnumSet.allOf(ReuseActions.class);
        if (feature.getChildrenFeatureViews().isEmpty()) {
            options.remove(ReuseActions.COLLAPSE);
            options.remove(ReuseActions.EXPAND);
        } else if (!scene.isFeatureCollapsed(feature)) {
            options.remove(ReuseActions.EXPAND);
        } else {
            options.remove(ReuseActions.COLLAPSE);
        }
        return options.toArray(new ReuseActions[0]);
    }
    
    @Override
    public void renameFeature(FeatureView view) {
        FeatureView reuse = view.getHighestParentReuse();
        if (reuse != null) {
            reuse.showKeyboard();
        }
    }

    @Override
    public void deleteFeature(DisplayConcernEditScene scene, FeatureView view) {
        FeatureView reuse = view.getHighestParentReuse();
        if (reuse != null && COREModelUtil.getModelReuses(scene.getConcern(), reuse.getCoreReuse()).isEmpty()) {
            ControllerFactory.INSTANCE.getFeatureController().removeReuse(scene.getConcern(),
                    view.getParentFeatureView().getFeature(), view.getCoreReuse());
        } else {
            scene.displayPopup(Strings.POPUP_CANT_DELETE_REUSE);
        }
    }

    @Override
    public void hideFeature(DisplayConcernEditScene scene, FeatureView view) {
        scene.switchCollapse(view);
    }

    @Override
    public boolean processTapEvent(TapEvent tapEvent) {
        // unused
        return false;
    }

    @Override
    public boolean processDragEvent(DragEvent dragEvent) {
        // unused
        return false;
    }

    @Override
    public void setPositionUpdate(FeatureView featureIcon, AbstractConcernScene<?, ?> scene) {
        // unused
    }

    @Override
    public boolean isXORCurve(float startX, float startY, float endX, float endY, FeatureView featureIcon) {
        // unused
        return false;
    }

    @Override
    public void addChild(DisplayConcernEditScene scene, COREFeature feature, int position,
            COREFeatureRelationshipType optional) {
        // unused
    }

}
