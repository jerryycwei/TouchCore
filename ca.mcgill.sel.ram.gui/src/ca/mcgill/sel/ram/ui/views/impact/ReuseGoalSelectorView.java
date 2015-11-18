package ca.mcgill.sel.ram.ui.views.impact;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.sel.core.COREFeatureImpactNode;
import ca.mcgill.sel.core.COREImpactModel;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.core.COREReuse;
import ca.mcgill.sel.ram.ui.components.RamExpendableComponent;
import ca.mcgill.sel.ram.ui.components.RamListComponent;
import ca.mcgill.sel.ram.ui.components.RamListComponent.Namer;
import ca.mcgill.sel.ram.ui.components.RamRectangleComponent;
import ca.mcgill.sel.ram.ui.components.RamSelectorComponent;
import ca.mcgill.sel.ram.ui.components.RamTextComponent;
import ca.mcgill.sel.ram.ui.components.RamTextComponent.Alignment;
import ca.mcgill.sel.ram.ui.components.listeners.RamListListener;
import ca.mcgill.sel.ram.ui.components.listeners.RamSelectorListener;
import ca.mcgill.sel.ram.ui.layouts.HorizontalLayoutVerticallyCentered;
import ca.mcgill.sel.ram.ui.utils.Colors;
import ca.mcgill.sel.ram.ui.utils.Fonts;
import ca.mcgill.sel.ram.ui.views.impact.namer.ImpactModelElementNamer;

/**
 * A selector view which shows a list of {@link COREReuse}. And for each {@link COREReuse}, the {@link COREImpactNode}
 * of the {@link COREImpactModel}.
 *
 * @author Romain
 */
public class ReuseGoalSelectorView extends RamSelectorComponent<COREReuse> implements RamSelectorListener<COREReuse> {

    private List<ReuseGoalListener> listeners;

    /**
     * The listener if you want to be notified when you valid your selection or close the selector.
     *
     * @author Romain
     *
     */
    public interface ReuseGoalListener {
        /**
         * When the user hit the button "Select".
         *
         * @param reuse The {@link COREReuse} that has been selected
         * @param impactModelElement the {@link COREImpactNode} that has been selected
         */
        void selectorValid(COREReuse reuse, COREImpactNode impactModelElement);

        /**
         * When the user hit the button close of the {@link RamSelectorComponent}.
         */
        void selectorCancel();
    }

    /**
     * A namer that is capable of displaying a hierarchy of {@link COREReuse}. If the {@link COREImpactModel} has no
     * {@link COREImpactNode}, this {@link COREReuse} will not be displayed. Otherwise, it will create a
     * hierarchy of the {@link COREImpactNode}s.
     */
    private class InternalReuseGoalNamer implements Namer<COREReuse> {
        @Override
        public RamRectangleComponent getDisplayComponent(COREReuse element) {
            COREImpactModel reusedImpactModel = element.getReusedConcern().getImpactModel();
            return createExpandableComponent(element, reusedImpactModel.getImpactModelElements());
        }

        /**
         * Creates a {@link RamExpendableComponent} associated to the given {@link COREImpactNode}. The
         * {@link RamExpendableComponent}'s title is a {@link RamRectangleComponent} Its hideable content is a
         * {@link RamListComponent} of the children {@link COREImpactNode}.
         *
         * @param element the {@link COREImpactNode} to create the view for
         * @param incomingElements the list of incoming elements
         *
         * @return the created {@link RamExpendableComponent}
         */
        private RamExpendableComponent createExpandableComponent(COREReuse element,
                List<COREImpactNode> incomingElements) {
            RamRectangleComponent title = createComponent(element, true);
            RamListComponent<COREImpactNode> content = createListImpactModelElement(incomingElements, element);
            RamExpendableComponent component = new RamExpendableComponent(title, content);
            component.setFillColor(Colors.DEFAULT_ELEMENT_FILL_COLOR);
            return component;
        }

        /**
         * Creates a {@link RamRectangleComponent} associated to the given {@link COREImpactNode}.
         *
         * @param element the {@link COREImpactNode} to create the view for
         * @param noFill whether the background shouldn't be colored or not
         * @return the created {@link RamRectangleComponent}
         */
        private RamRectangleComponent createComponent(COREReuse element, boolean noFill) {
            RamRectangleComponent container = new RamRectangleComponent();
            container.setBufferSize(Cardinal.WEST, 5);
            container.setNoFill(noFill);
            container.setFillColor(Colors.DEFAULT_ELEMENT_FILL_COLOR);
            container.setNoStroke(false);
            container.setStrokeWeight(3.0f);
            container.setStrokeColor(Colors.DEFAULT_ELEMENT_STROKE_COLOR);

            RamTextComponent reuseCell = new RamTextComponent(element.getName());
            reuseCell.setFont(Fonts.FONT_CLASS_NAME);
            reuseCell.setNoStroke(true);

            reuseCell.setAutoMaximizes(true);
            reuseCell.setAlignment(Alignment.LEFT_ALIGN);
            container.addChild(reuseCell);

            container.setLayout(new HorizontalLayoutVerticallyCentered(5));
            return container;
        }

        /**
         * Create a {@link RamListComponent} with the incoming elements.
         *
         * @param impactModelElements the list of {@link COREImpactNode}
         * @param reuse This reuse has a reference to the concern that contains these {@link COREImpactNode}
         * @return the created {@link RamListComponent}
         */
        private RamListComponent<COREImpactNode> createListImpactModelElement(
                List<COREImpactNode> impactModelElements, COREReuse reuse) {
            RamListComponent<COREImpactNode> list;

            List<COREImpactNode> elements = new ArrayList<COREImpactNode>();

            for (COREImpactNode impactModelElement : impactModelElements) {
                if (!(impactModelElement instanceof COREFeatureImpactNode)
                        && impactModelElement.getOutgoing().isEmpty()) {
                    elements.add(impactModelElement);
                }
            }

            list =
                    new RamListComponent<COREImpactNode>(elements, new ImpactModelElementNamer(
                            new ImpactListListener(reuse)));
            list.setNoFill(true);
            list.setNoStroke(true);
            list.registerListener(new ImpactListListener(reuse));
            return list;
        }

        @Override
        public String getSortingName(COREReuse element) {
            return element.getName();
        }

        @Override
        public String getSearchingName(COREReuse element) {
            return getSortingName(element);
        }

        /**
         * Private class which is a listener of the lists implemented.
         *
         * @author Romain
         *
         */
        private class ImpactListListener implements RamListListener<COREImpactNode> {

            private COREReuse reuse;

            /**
             * Create a new {@link ImpactListListener}.
             *
             * @param reuse This reuse has a reference to the concern that contains these {@link COREImpactNode}
             */
            public ImpactListListener(COREReuse reuse) {
                this.reuse = reuse;
            }

            @Override
            public void elementSelected(RamListComponent<COREImpactNode> list, COREImpactNode element) {
                for (ReuseGoalListener listener : listeners) {
                    listener.selectorValid(reuse, element);
                }
                ReuseGoalSelectorView.this.destroy();
            }

            @Override
            public void elementDoubleClicked(RamListComponent<COREImpactNode> list,
                    COREImpactNode element) {
            }

            @Override
            public void elementHeld(RamListComponent<COREImpactNode> list, COREImpactNode element) {
            }
        }
    }

    /**
     * Creates an selector with the given elements.
     *
     * @param elements the list of the {@link COREReuse}
     */
    public ReuseGoalSelectorView(List<COREReuse> elements) {
        super();

        listeners = new ArrayList<ReuseGoalListener>();

        showTextField(false);
        setNamer(new InternalReuseGoalNamer());

        // Remove the reuse that does not have impact model or goals.
        List<COREReuse> reuses = new ArrayList<COREReuse>();
        for (COREReuse reuse : elements) {
            COREImpactModel reusedImpactModel = reuse.getReusedConcern().getImpactModel();
            if (reusedImpactModel == null || !reusedImpactModel.getImpactModelElements().isEmpty()) {
                reuses.add(reuse);
            }
        }

        setElements(reuses);

        this.registerListener(this);
    }

    /**
     * Add a new {@link ReuseGoalListener} to the list of listeners.
     *
     * @param listener the listener to add.
     */
    public void registerListener(ReuseGoalListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void elementSelected(RamSelectorComponent<COREReuse> selector, COREReuse element) {
    }

    @Override
    public void closeSelector(RamSelectorComponent<COREReuse> selector) {
        for (ReuseGoalListener listener : this.listeners) {
            listener.selectorCancel();
        }
        this.destroy();
    }

    @Override
    public void elementDoubleClicked(RamSelectorComponent<COREReuse> selector, COREReuse element) {
    }

    @Override
    public void elementHeld(RamSelectorComponent<COREReuse> selector, COREReuse element) {
    }
}
