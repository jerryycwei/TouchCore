package ca.mcgill.sel.ram.ui.views.containers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEMap.Entry;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.commons.emf.util.EMFModelUtil;
import ca.mcgill.sel.core.COREConcern;
import ca.mcgill.sel.core.COREContribution;
import ca.mcgill.sel.core.COREFeatureImpactNode;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.core.impl.LayoutContainerMapImpl;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamListComponent;
import ca.mcgill.sel.ram.ui.utils.Strings;
import ca.mcgill.sel.ram.ui.views.impact.ImpactEditDiagramView;

/**
 * Container class used to contain all the impact model elements in the Concern. It also display the weight of each goal
 * according to the selection of the user.
 *
 * @author Romain
 *
 */
public class COREImpactModelContainer extends COREImpactContainer {

    /**
     * A namer that is capable of displaying a hierarchy of {@link COREImpactNode}. It will also display the
     * COREFeatureImpacts
     */
    private class InternalNamer extends InternalImpactModelElementNamer {
        /**
         * Construct a namer that is capable of displaying a hierarchy of {@link COREImpactNode}. It will also
         * display the COREFeatureImpacts
         */
        public InternalNamer() {
            super(false);
        }

        /**
         * Retrieve the incoming element of this {@link COREImpactNode}.
         *
         * @param element the element for which we want to retrieve incoming element.
         * @return the list of incoming element
         */
        @Override
        protected List<COREImpactNode> getIncoming(COREImpactNode element) {
            List<COREImpactNode> res = super.getIncoming(element);

            for (COREContribution contribution : element.getIncoming()) {
                COREImpactNode incomingElement = contribution.getSource();
                if (incomingElement instanceof COREFeatureImpactNode
                        && !COREImpactModelContainer.this.ignoreElements.contains(incomingElement)) {
                    res.add(incomingElement);
                }
            }

            return res;
        }
    }

    private ImpactEditDiagramView impactDiagramView;

    /**
     * The {@link LayoutContainerMapImpl} for this root.
     */
    private LayoutContainerMapImpl mapLayoutEntry;

    /**
     * The container takes in the default path of the container. It displays all the aspects in the folder.
     *
     * @param concern - The concern which contains the Impact Models.
     * @param impactDiagramView - The diagram that contains the root goal.
     * @param rootGoal the root goal of this scene
     * @param ignoredElements the list of element that will not be displayed
     */
    public COREImpactModelContainer(COREConcern concern,
            ImpactEditDiagramView impactDiagramView,
            COREImpactNode rootGoal, Collection<COREImpactNode> ignoredElements) {
        super(concern);
        setLabel(Strings.LABEL_IMPACT_CONTAINER_ADD);

        this.impactDiagramView = impactDiagramView;
        ignoreElements.addAll(ignoredElements);

        // Check if there is a MapLayout for the root element, Otherwise create one
        mapLayoutEntry =
                EMFModelUtil.getEntryFromMap(impactModel.getLayouts(), impactDiagramView.getRootImpactModelElement());

        // register to the ContainerMap to receive adds/removes of ElementMaps
        EMFEditUtil.addListenerFor(mapLayoutEntry, this);

        setNamer(new InternalNamer());
        setElements(getSuperGoals());
    }

    @Override
    protected List<COREImpactNode> getSuperGoals() {
        List<COREImpactNode> res = new ArrayList<COREImpactNode>();

        for (COREImpactNode element : super.getSuperGoals()) {
            if (!this.ignoreElements.contains(element)) {
                res.add(element);
            }
        }

        return res;
    }

    /**
     * {@inheritDoc}.
     * Show the impact model in select mode with this element as the main goal of the view.
     */
    @Override
    protected void goalClicked(final RamListComponent<COREImpactNode> list, final COREImpactNode elem) {
        this.ignoreElements.add(elem);

        RamApp.getApplication().invokeLater(new Runnable() {

            @Override
            public void run() {
                list.removeElement(elem);
            }
        });

        impactDiagramView.addGoalReference(elem);
    }

    @Override
    protected void goalDoubleClicked(RamListComponent<COREImpactNode> list, COREImpactNode element) {
    }

    @Override
    public void notifyChanged(Notification notification) {
        if (notification.getFeature() == CorePackage.Literals.LAYOUT_CONTAINER_MAP__VALUE) {
            Entry<?, ?> entry;
            switch (notification.getEventType()) {
                case Notification.ADD:
                    entry = (Entry<?, ?>) notification.getNewValue();
                    this.ignoreElements.add((COREImpactNode) entry.getKey());
                    break;
                case Notification.REMOVE:
                    entry = (Entry<?, ?>) notification.getOldValue();
                    this.ignoreElements.remove((COREImpactNode) entry.getKey());
                    break;
            }
        }

        super.notifyChanged(notification);
    }

    @Override
    public void destroy() {
        EMFEditUtil.removeListenerFor(mapLayoutEntry, this);
        super.destroy();
    }
}