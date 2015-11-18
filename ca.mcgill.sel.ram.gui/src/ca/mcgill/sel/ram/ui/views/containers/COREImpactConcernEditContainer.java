package ca.mcgill.sel.ram.ui.views.containers;

import org.mt4j.sceneManagement.transition.SlideTransition;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.core.COREConcern;
import ca.mcgill.sel.core.COREImpactModel;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamListComponent;
import ca.mcgill.sel.ram.ui.scenes.AbstractConcernScene;

/**
 * Container class used to contain all the impact model elements in the Concern.
 *
 * @author Nishanth
 * @author Romain
 */
public class COREImpactConcernEditContainer extends COREImpactContainer {

    /**
     * A namer that is capable of displaying a hierarchy of {@link COREImpactNode}. It will also display a
     * delete button.
     */
    private class InternalNamer extends InternalImpactModelElementNamer {
        /**
         * Build a namer that is capable of displaying a hierarchy of {@link COREImpactNode}. It will also
         * display a delete button.
         */
        public InternalNamer() {
            super(true);
        }
    }

    private AbstractConcernScene<?, ?> currentScene;

    /**
     * The container takes in the default path of the container. It displays all the aspects in the folder.
     *
     * @param concern - The concern which contains the Impact Models.
     * @param scene - The scene of the current Concern.
     */
    public COREImpactConcernEditContainer(COREConcern concern,
            AbstractConcernScene<?, ?> scene) {
        super(concern);
        currentScene = scene;
        setNamer(new InternalNamer());
        setElements(getSuperGoals());
    }

    /**
     * Show the impact model in edit mode with this element as the main goal of the view.
     */
    @Override
    protected void goalClicked(RamListComponent<COREImpactNode> list, COREImpactNode element) {
        currentScene.setTransition(new SlideTransition(RamApp.getApplication(), 700, true));
        currentScene.getApplication().showImpactModel(currentScene.getConcern(), element);
    }

    @Override
    protected void goalDoubleClicked(RamListComponent<COREImpactNode> list, COREImpactNode element) {

    }

    /**
     * Set the impact model for the container and update the view.
     * 
     * @param im - {@link COREImpactModel}
     */
    public void setImpactModel(COREImpactModel im) {
        if (this.impactModel != null) {
            EMFEditUtil.removeListenerFor(this.impactModel, this);
        }
        if (im != null) {
            this.impactModel = im;
            EMFEditUtil.addListenerFor(im, this);
        }
        setElements(getSuperGoals());
    }
}
