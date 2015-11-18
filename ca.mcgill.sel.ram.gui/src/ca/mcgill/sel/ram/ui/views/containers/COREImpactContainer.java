package ca.mcgill.sel.ram.ui.views.containers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.INotifyChangedListener;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.core.COREConcern;
import ca.mcgill.sel.core.COREImpactModel;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.core.util.COREImpactModelUtil;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamListComponent;
import ca.mcgill.sel.ram.ui.components.RamPanelListComponent;
import ca.mcgill.sel.ram.ui.components.listeners.RamListListener;
import ca.mcgill.sel.ram.ui.utils.Strings;
import ca.mcgill.sel.ram.ui.views.impact.namer.ImpactModelElementNamer;

/**
 * Container class used to contain all the impact model elements in the Concern.
 *
 * @author Romain
 *
 */
public abstract class COREImpactContainer extends RamPanelListComponent<COREImpactNode> implements
        INotifyChangedListener {

    private static final int DEFAULT_MAX_ELEMENT = 8;
    private static final int DEFAULT_ARC_RADIUS = 5;

    /**
     * The impact model of this concern.
     */
    protected COREImpactModel impactModel;

    /**
     * The set of {@link COREImpactNode} that will be ignore in this Container. If you don't want to display one
     * or more goal, you just have to add it in this set.
     */
    protected Set<COREImpactNode> ignoreElements;

    /**
     * The container takes in the default path of the container. It displays all the aspects in the folder.
     *
     * @param concern - The concern which contains the Impact Models.
     */
    public COREImpactContainer(COREConcern concern) {
        super(DEFAULT_ARC_RADIUS, Strings.LABEL_IMPACT_CONTAINER_VIEW, HorizontalStick.LEFT, VerticalStick.BOTTOM);
        this.impactModel = concern.getImpactModel();
        ignoreElements = new HashSet<COREImpactNode>();
        setNamer(new InternalImpactModelElementNamer(false));
        setListener(new ImpactSelectorListener());
        this.setMaxNumberOfElements(DEFAULT_MAX_ELEMENT);

        if (impactModel != null) {
            EMFEditUtil.addListenerFor(impactModel, this);
        }
    }

    /**
     * Private class which is a listener of the selectors implemented.
     *
     * @author Romain
     */
    private class ImpactSelectorListener implements RamListListener<COREImpactNode> {

        /**
         * Function called when a selector is clicked / tapped.
         *
         * @param list - The list from which the selection was made.
         * @param element - The element of type FileName, which was clicked.
         */
        @Override
        public void elementSelected(RamListComponent<COREImpactNode> list,
                COREImpactNode element) {
            goalClicked(list, element);
        }

        /**
         * {@inheritDoc}
         *
         * @param list - The list from which the selection was made.
         * @param element - The element which was double clicked.
         */
        @Override
        public void elementDoubleClicked(RamListComponent<COREImpactNode> list,
                COREImpactNode element) {
            goalDoubleClicked(list, element);
        }

        @Override
        public void elementHeld(RamListComponent<COREImpactNode> list, COREImpactNode element) {
        }

    }

    /**
     * A namer that is capable of displaying a hierarchy of {@link COREImpactNode}.
     */
    protected class InternalImpactModelElementNamer extends ImpactModelElementNamer {

        /**
         * Create a new {@link InternalImpactModelElementNamer}. With a {@link ImpactListListener} for the subLists.
         */
        public InternalImpactModelElementNamer() {
            this(true);
        }

        /**
         * Create a new {@link InternalImpactModelElementNamer}. With a {@link ImpactListListener} for the subLists.
         *
         * @param showDeleteButton if a delete button has to be shown or not
         */
        public InternalImpactModelElementNamer(boolean showDeleteButton) {
            this(showDeleteButton, true);
        }

        /**
         * Create a new {@link InternalImpactModelElementNamer}. With a {@link ImpactListListener} for the subLists.
         *
         * @param showDeleteButton if a delete button has to be shown or not
         * @param useTextView if the titles need to be display using a TextView
         */
        public InternalImpactModelElementNamer(boolean showDeleteButton, boolean useTextView) {
            super(showDeleteButton, useTextView);
            this.setListener(new ImpactListListener());
        }

        @Override
        protected List<COREImpactNode> getIncoming(COREImpactNode element) {
            List<COREImpactNode> incomingElements = super.getIncoming(element);

            for (COREImpactNode ignoreElement : ignoreElements) {
                incomingElements.remove(ignoreElement);
            }
            return incomingElements;
        }

        @Override
        protected String getElementName(COREImpactNode element) {
            return COREImpactContainer.this.getNameByElement(element);
        }

        /**
         * Private class which is a listener of the lists implemented.
         *
         * @author Romain
         *
         */
        private class ImpactListListener implements RamListListener<COREImpactNode> {

            @Override
            public void elementSelected(RamListComponent<COREImpactNode> list, COREImpactNode element) {
                goalClicked(list, element);
            }

            @Override
            public void elementDoubleClicked(RamListComponent<COREImpactNode> list,
                    COREImpactNode element) {
                goalDoubleClicked(list, element);
            }

            @Override
            public void elementHeld(RamListComponent<COREImpactNode> list, COREImpactNode element) {
            }
        }
    }

    /**
     * Retrieve the name to display in the {@link ca.mcgill.sel.ram.ui.components.RamTextComponent} for this element. By
     * default, it return just the name of the element. But the child of this class can override this operation and
     * return something else.
     *
     * @param element the element we want to display
     * @return the name for this element. By default, we call the operation getName() on the element.
     */
    protected String getNameByElement(COREImpactNode element) {
        return element.getName();
    }

    /**
     * Do an action when a goal is clicked.
     *
     * @param list The list from which the selection was made.
     * @param element the selected goal
     */
    protected abstract void goalClicked(RamListComponent<COREImpactNode> list, COREImpactNode element);

    /**
     * Do an action when a goal is double clicked.
     *
     * @param list The list from which the selection was made.
     * @param element the selected goal
     */
    protected abstract void goalDoubleClicked(RamListComponent<COREImpactNode> list,
            COREImpactNode element);

    /**
     * Retrieve all the super Goals.
     *
     * @return a list of super goals
     */
    protected List<COREImpactNode> getSuperGoals() {
        return new ArrayList<COREImpactNode>(COREImpactModelUtil.getRootGoals(impactModel));
    }

    @Override
    public void notifyChanged(Notification notification) {
        RamApp.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                setElements(getSuperGoals());
            }
        });
    }

    @Override
    public void destroy() {
        // remove listener
        if (impactModel != null) {
            EMFEditUtil.removeListenerFor(impactModel, this);
        }

        super.destroy();
    }
}
