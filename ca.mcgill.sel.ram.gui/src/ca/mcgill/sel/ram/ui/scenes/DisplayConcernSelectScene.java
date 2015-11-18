package ca.mcgill.sel.ram.ui.scenes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.mt4j.components.clipping.Clip;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.inputProcessors.componentProcessors.AbstractComponentProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.panProcessor.PanProcessorTwoFingers;

import ca.mcgill.sel.core.COREConcern;
import ca.mcgill.sel.core.COREReuseConfiguration;
import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.core.COREFeatureModel;
import ca.mcgill.sel.core.COREImpactModel;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.core.CoreFactory;
import ca.mcgill.sel.core.evaluator.im.ForwardPropagationAlgorithm;
import ca.mcgill.sel.core.util.COREImpactModelUtil;
import ca.mcgill.sel.core.util.COREModelUtil;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamPanelComponent.HorizontalStick;
import ca.mcgill.sel.ram.ui.components.RamPanelComponent.VerticalStick;
import ca.mcgill.sel.ram.ui.components.RamRectangleComponent;
import ca.mcgill.sel.ram.ui.events.RightClickDragProcessor;
import ca.mcgill.sel.ram.ui.events.listeners.ActionListener;
import ca.mcgill.sel.ram.ui.scenes.handler.IConcernSelectSceneHandler;
import ca.mcgill.sel.ram.ui.scenes.handler.impl.ConcernSelectSceneHandler;
import ca.mcgill.sel.ram.ui.utils.Colors;
import ca.mcgill.sel.ram.ui.utils.Constants;
import ca.mcgill.sel.ram.ui.utils.Icons;
import ca.mcgill.sel.ram.ui.utils.SelectionsSingleton;
import ca.mcgill.sel.ram.ui.utils.Strings;
import ca.mcgill.sel.ram.ui.views.containers.COREConstraintSelectContainer;
import ca.mcgill.sel.ram.ui.views.containers.COREImpactConcernSelectContainer;
import ca.mcgill.sel.ram.ui.views.feature.FeatureDiagramSelectView;
import ca.mcgill.sel.ram.ui.views.feature.FeatureView;
import ca.mcgill.sel.ram.ui.views.feature.handler.impl.FeatureSelectModeHandler;
import ca.mcgill.sel.ram.ui.views.feature.helpers.Constraint;
import ca.mcgill.sel.ram.ui.views.handler.HandlerFactory;

/**
 * Scene class used to represent the Selection mode of a concern.
 *
 * @author Nishanth
 * @author oalam
 */
public class DisplayConcernSelectScene
        extends AbstractConcernScene<FeatureDiagramSelectView, IConcernSelectSceneHandler>
        implements ActionListener {

    /**
     * The enum indicating the different modes of selection.
     *
     * @author Nishanth.
     */
    public enum DisplayMode {
        /**
         * The Full_Mode selection in Feature Selection stage.
         * Show all features, even those that were selected in reuses.
         */
        FULL,
        /**
         * The Next_Mode selection in Feature Selection stage.
         * Shows only the next level of selections to make.
         */
        NEXT
    }

    private static final String ACTION_WEAVE = "weave.all";
    private static final String ACTION_MENU = "display.menu";
    private static final String ACTION_SWITCH_MODE = "switch.mode";
    private static final String ACTION_CLEAR_ALL = "clear.all";

    /**
     * Temporary fix until the weaver is fixed to disable/ enable reusing.
     */
    private boolean canReuse = true;

    private COREConstraintSelectContainer constraintSelectContainer;

    private COREImpactConcernSelectContainer impactSelectContainer;

    private Aspect aspect;

    private DisplayMode currentMode;

    /**
     * Creates a new select scene for the given Concern.
     *
     * @param app - The current application
     * @param concern - The concern to allow selections to be made from
     * @param aspect - Reusing aspect.
     */
    public DisplayConcernSelectScene(RamApp app, COREConcern concern, Aspect aspect) {
        super(app, concern.getName().concat(" " + Strings.SCENE_NAME_CONCERN_SELECT_MODE), false);

        this.aspect = aspect;
        this.concern = concern;

        concernRectangle = new MTRectangle(app, getWidth(), getHeight());
        concernRectangle.setFillColor(Colors.BACKGROUND_COLOR);
        concernRectangle.setNoFill(false);
        concernRectangle.unregisterAllInputProcessors();
        concernRectangle.setChildClip(new Clip(RamApp.getApplication(), 0, 0, getWidth(), getHeight()));

        replot();

    }

    /**
     * Getter for the current display mode.
     *
     * @return - The current mode
     */
    public DisplayMode getCurrentMode() {
        return currentMode;
    }

    /**
     * Setter for the current display mode.
     *
     * @param currentMode - The new mode
     */
    public void setCurrentMode(DisplayMode currentMode) {
        this.currentMode = currentMode;
        this.featureDiagramView.setCurrentMode(currentMode);
    }

    /**
     * Function to add the Background layer action of creating an Aspect.
     */
    public void addBackgroundLayer() {
        RamRectangleComponent rectangle = new RamRectangleComponent(0, 0, RamApp.getActiveScene().getWidth(), RamApp
                .getActiveScene().getHeight());
        rectangle.setVisible(true);
        rectangle.setNoFill(true);
        rectangle.setFillColor(Colors.COLOR_BUTTON_ENABLED);

        AbstractComponentProcessor rightClick = new RightClickDragProcessor(RamApp.getApplication());
        rightClick.setBubbledEventsEnabled(true);

        AbstractComponentProcessor twoPanFinger = new PanProcessorTwoFingers(RamApp.getApplication());
        twoPanFinger.setBubbledEventsEnabled(true);

        FeatureSelectModeHandler tapHandler = HandlerFactory.INSTANCE.getFeatureSelectModeHandler();

        containerLayer.registerInputProcessor(rightClick);
        containerLayer.addGestureListener(RightClickDragProcessor.class, tapHandler);

        containerLayer.registerInputProcessor(twoPanFinger);
        containerLayer.addGestureListener(PanProcessorTwoFingers.class, tapHandler);

        containerLayer.addChild(rectangle);
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        String actionCommand = event.getActionCommand();
        if (ACTION_MENU.equals(actionCommand)) {
            // Delete folder if no other reuse uses the concern.
            COREModelUtil.deleteReusedConcernDirectory(concern, aspect.getCoreConcern());
            handler.switchToAspect(this);
        } else if (ACTION_WEAVE.equals(actionCommand)) {
            handler.reuse(this);
        } else if (ACTION_CLEAR_ALL.equals(actionCommand)) {
            handler.clear(this);
        } else if (ACTION_SWITCH_MODE.equals(actionCommand)) {
            handler.switchMode(this);
        }
    }

    /**
     * Function used to set the handler for the scene.
     *
     * @param handler - The concern select scene handler.
     */
    public void setHandler(ConcernSelectSceneHandler handler) {
        this.handler = handler;
    }

    /**
     * Returns the aspect which is reusing the concern.
     *
     * @return Aspect - The aspect which is reusing the concern.
     */
    public Aspect getAspect() {
        return aspect;
    }

    /**
     * Temporary fix made to return whether the concern can be woven or not.
     *
     * @return - Boolean value set in the scene, whether a reuse can be made or not.
     */
    public boolean getCanReuse() {
        return canReuse;
    }

    /**
     * Temporary fix made to set whether the concern can be woven or not.
     *
     * @param canReuse - can we reuse the concern or not
     */
    private void setCanReuse(boolean canReuse) {
        this.canReuse = canReuse;
    }

    /**
     * Method used to change the buttons when different mode is selected.
     */
    public void updateModeButtons() {
        boolean tog = currentMode == DisplayMode.NEXT;
        getMenu().toggleAction(tog, ACTION_SWITCH_MODE);
    }

    /**
     * Enables or disables the reuse buttons according to the current selection validity.
     */
    public void updateReuseButton() {
        Set<COREFeature> selectedFeatures = featureDiagramView.collectSelected();
        Set<COREFeature> reExposedFeatures = featureDiagramView.collectReexposed();
        boolean constraints = constraintSelectContainer == null
                || constraintSelectContainer.validate(selectedFeatures, reExposedFeatures);
        boolean reuse = constraints && featureDiagramView.checkForClashes();
        setCanReuse(reuse);
        menu.enableAction(reuse, ACTION_WEAVE);
    }

    /**
     * Re-create the whole Feature model view.
     */
    @Override
    protected void replot() {
        // Add the background Rectangle
        addBackgroundLayer();

        COREFeatureModel fm = concern.getFeatureModel();
        root = fm.getRoot();

        // Draw feature diagram
        redrawFeatureDiagram(true);

        List<Constraint> constraintsList = getConstraints(root);
        if (!constraintsList.isEmpty()) {
            constraintSelectContainer = new COREConstraintSelectContainer(HorizontalStick.LEFT, VerticalStick.TOP);
            constraintSelectContainer.setElements(constraintsList);
            containerLayer.addChild(constraintSelectContainer);
        }

    }

    @Override
    public void redrawFeatureDiagram(boolean repopulate) {

        SelectionsSingleton.getInstance().addSelection(root);

        if (featureDiagramView == null) {
            featureDiagramView = new FeatureDiagramSelectView(getWidth(), getHeight(), concern.getFeatureModel());
            containerLayer.addChild(featureDiagramView);
            featureDiagramView.setHandler(HandlerFactory.INSTANCE.getFeatureDiagramSelectHandler());
            setCurrentMode(DisplayMode.FULL);
        } else {
            featureDiagramView.updateFeaturesDisplay(repopulate);
        }

        selected(featureDiagramView.getRootFeature());
    }

    /**
     * Function called when a feature is selected / tapped.
     *
     * @param featureView - The current scene of the concern (Select mode).
     */
    public void selected(FeatureView featureView) {
        resetFeaturesStatus();
        updateReuseButton();
        if (concern.getImpactModel() != null) {
            refreshImpactSelectContainer();
        }
    }

    /**
     * Function used to clear all the selections back to the re exposed state.
     */
    public void resetFeaturesStatus() {
        featureDiagramView.clearFeatures();
        featureDiagramView.autoSelect();
        featureDiagramView.placeIcons();
    }

    /**
     * Check if there are clashes in the selections made.
     *
     * @return true if there are clashes, false otherwise
     */
    public boolean hasClashes() {
        return !featureDiagramView.checkForClashes();
    }

    /**
     * Get all the selected features.
     *
     * @return - Set of selected features
     */
    public Set<COREFeature> collectSelected() {
        return featureDiagramView.collectSelected();
    }

    /**
     * Get all the re-exposed features.
     *
     * @return - Set of re-exposed features
     */
    public Set<COREFeature> collectReexposed() {
        return featureDiagramView.collectReexposed();
    }

    /**
     * Create a new {@link COREImpactConcernSelectContainer} with the new goal Impact Map and the new selected feature.
     */
    public void refreshImpactSelectContainer() {
        // TODO Romain/Cecile/Berk
        COREReuseConfiguration configuration = CoreFactory.eINSTANCE.createCOREReuseConfiguration();
        EList<COREFeature> selectedFeatures = configuration.getSelected();
        selectedFeatures.addAll(collectSelected());

        COREImpactModel impactModel = concern.getImpactModel();

        List<COREImpactNode> rootGoals =
                new ArrayList<COREImpactNode>(COREImpactModelUtil.getRootGoals(impactModel));

        Map<COREImpactNode, Float> goalImpactMap =
                ForwardPropagationAlgorithm.propagate(impactModel, configuration, rootGoals);

        // TODO Maybe do that at the end of propagate operation :
        for (Entry<COREImpactNode, Float> entry : goalImpactMap.entrySet()) {
            BigDecimal bd = new BigDecimal(entry.getValue());
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            entry.setValue(bd.floatValue());
        }

        if (containerLayer.containsChild(impactSelectContainer)) {
            containerLayer.removeChild(impactSelectContainer);
        }

        if (impactModel != null) {
            impactSelectContainer =
                    new COREImpactConcernSelectContainer(concern, this, goalImpactMap, collectSelected());
            containerLayer.addChild(impactSelectContainer);
        }
    }

    @Override
    protected void initMenu() {
        this.getMenu().addAction(Strings.MENU_WEAVE_ALL, Icons.ICON_MENU_VALIDATE, ACTION_WEAVE, this, true);

        this.getMenu().addSubMenu(2, ACTION_MENU);
        this.getMenu().addAction(Strings.MENU_BACK, Icons.ICON_MENU_CLOSE, ACTION_MENU, this, ACTION_MENU, true);

        this.getMenu().addSubMenu(1, Constants.MENU_EXTRA);
        this.getMenu().addAction(Strings.MENU_SWITCH_NEXT, Strings.MENU_SWITCH_FULL, Icons.ICON_MENU_SHOW_NORMAL_MODE,
                Icons.ICON_MENU_SHOW_FULL_MODE, ACTION_SWITCH_MODE, this, Constants.MENU_EXTRA, true, false);
        this.getMenu().addAction(Strings.MENU_CLEAR_SELECTION, Icons.ICON_MENU_CLEAR_SELECTION, ACTION_CLEAR_ALL, this,
                Constants.MENU_EXTRA, true);
    }

    @Override
    protected EObject getElementToSave() {
        return null;
    }
}
