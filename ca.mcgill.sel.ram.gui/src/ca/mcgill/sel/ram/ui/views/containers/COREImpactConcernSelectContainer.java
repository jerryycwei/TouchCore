package ca.mcgill.sel.ram.ui.views.containers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.mt4j.sceneManagement.transition.SlideTransition;

import ca.mcgill.sel.core.COREConcern;
import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamListComponent;
import ca.mcgill.sel.ram.ui.scenes.AbstractConcernScene;

/**
 * Container class used to contain all the impact model elements in the Concern. It also display the weight of each goal
 * according to the selection of the user.
 *
 * @author Nishanth
 * @author Romain
 *
 */
public class COREImpactConcernSelectContainer extends COREImpactContainer {

    private AbstractConcernScene<?, ?> currentScene;
    private Map<COREImpactNode, Float> goalImpactMap;
    private Set<COREFeature> selectedFeatures;

    /**
     * A namer that is capable of displaying a hierarchy of {@link COREImpactNode}. It will not use a
     * text view to display the titles.
     */
    private class InternalNamer extends InternalImpactModelElementNamer {
        /**
         * Build a namer that is capable of displaying a hierarchy of {@link COREImpactNode}. It will not use a
         * text view to display the titles.
         */
        public InternalNamer() {
            super(false, false);
        }
    }

    /**
     * The container takes in the default path of the container. It displays all the aspects in the folder.
     *
     * @param concern - The concern which contains the Impact Models.
     * @param scene - The scene of the current Concern.
     * @param goalImpactMap The map that associates impact model elements with their weight
     * @param collectSelected - the set of selected features
     */
    public COREImpactConcernSelectContainer(COREConcern concern, AbstractConcernScene<?, ?> scene,
            Map<COREImpactNode, Float> goalImpactMap, Set<COREFeature> collectSelected) {
        super(concern);

        currentScene = scene;

        if (goalImpactMap.size() == 0) {
            this.goalImpactMap = new HashMap<COREImpactNode, Float>();
            for (COREImpactNode impactModelElement : this.getSuperGoals()) {
                this.goalImpactMap.put(impactModelElement, 0.00f);
            }
        } else {
            this.goalImpactMap = goalImpactMap;
        }

        selectedFeatures = collectSelected;

        setNamer(new InternalNamer());
        setElements(getSuperGoals());
    }

    /**
     * Retrieve the name to display in the {@link ca.mcgill.sel.ram.ui.components.RamTextComponent} for this element. By
     * default, it return just the name of the element. But the child of this class can override this operation and
     * return something else.
     *
     * @param element the element we want to display
     * @return the name for this element. By default, we call the operation getName() on the element.
     */
    @Override
    protected String getNameByElement(COREImpactNode element) {
        Float value = goalImpactMap.get(element);
        value = value == null ? 0.00f : value;
        return element.getName() + " : " + value;
    }

    /**
     * Show the impact model in select mode with this element as the main goal of the view.
     */
    @Override
    protected void goalClicked(RamListComponent<COREImpactNode> list, COREImpactNode element) {
        currentScene.setTransition(new SlideTransition(RamApp.getApplication(), 700, true));
        currentScene.getApplication().showImpactModelSelectMode(currentScene.getConcern(), element, goalImpactMap,
                selectedFeatures);
    }

    @Override
    protected void goalDoubleClicked(RamListComponent<COREImpactNode> list, COREImpactNode element) {

    }

}