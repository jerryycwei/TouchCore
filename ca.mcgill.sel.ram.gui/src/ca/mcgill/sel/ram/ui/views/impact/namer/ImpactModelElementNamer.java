package ca.mcgill.sel.ram.ui.views.impact.namer;

import java.util.ArrayList;
import java.util.List;

import org.mt4j.util.logging.ILogger;
import org.mt4j.util.logging.MTLoggerFactory;

import ca.mcgill.sel.core.COREContribution;
import ca.mcgill.sel.core.COREFeatureImpactNode;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.core.controller.ControllerFactory;
import ca.mcgill.sel.ram.ui.components.RamButton;
import ca.mcgill.sel.ram.ui.components.RamExpendableComponent;
import ca.mcgill.sel.ram.ui.components.RamImageComponent;
import ca.mcgill.sel.ram.ui.components.RamListComponent;
import ca.mcgill.sel.ram.ui.components.RamListComponent.Namer;
import ca.mcgill.sel.ram.ui.components.RamRectangleComponent;
import ca.mcgill.sel.ram.ui.components.RamRectangleComponent.Cardinal;
import ca.mcgill.sel.ram.ui.components.RamTextComponent;
import ca.mcgill.sel.ram.ui.components.RamTextComponent.Alignment;
import ca.mcgill.sel.ram.ui.components.listeners.RamListListener;
import ca.mcgill.sel.ram.ui.events.listeners.ActionListener;
import ca.mcgill.sel.ram.ui.layouts.HorizontalLayoutVerticallyCentered;
import ca.mcgill.sel.ram.ui.utils.Colors;
import ca.mcgill.sel.ram.ui.utils.Fonts;
import ca.mcgill.sel.ram.ui.utils.Icons;
import ca.mcgill.sel.ram.ui.views.TextView;

/**
 * A namer that is capable of displaying a hierarchy of {@link COREImpactNode}. If the
 * {@link COREImpactNode} has no child, just creates a {@link TextView} else, create an
 * {@link RamExpendableComponent} with a {@link TextView} as title and a {@link RamListComponent} with this same
 * namer as hideable part of the container.
 *
 * @author Romain
 */
public class ImpactModelElementNamer implements Namer<COREImpactNode>, Cloneable {
    /** The Constant logger. */
    private static final ILogger LOGGER = MTLoggerFactory.getLogger(ImpactModelElementNamer.class.getName());
    static {
        // LOGGER.setLevel(ILogger.ERROR);
        // LOGGER.setLevel(ILogger.WARN);
        LOGGER.setLevel(ILogger.DEBUG);
        // LOGGER.setLevel(ILogger.INFO);
    }

    private RamListListener<COREImpactNode> impactListListener;
    private boolean showDeleteRootButton;
    private boolean useTextView;

    /**
     * Create a new {@link ImpactModelElementNamer} with no listener for the element of the sub list.
     *
     */
    public ImpactModelElementNamer() {
        this(null);
    }

    /**
     * Create a new {@link ImpactModelElementNamer} with this listener for the element of each new sub list.
     *
     * @param listener The listener of the sub lists.
     *
     */
    public ImpactModelElementNamer(RamListListener<COREImpactNode> listener) {
        this(listener, false, true);
    }

    /**
     * Create a new {@link ImpactModelElementNamer} with no listener for the element of the sub list.
     *
     * @param showDeleteButton if a delete button has to be shown or not
     */
    public ImpactModelElementNamer(boolean showDeleteButton) {
        this(showDeleteButton, true);
    }

    /**
     * Create a new {@link ImpactModelElementNamer} with no listener for the element of the sub list.
     *
     * @param showDeleteButton if a delete button has to be shown or not
     * @param useTextView if a textView should be used or not
     */
    public ImpactModelElementNamer(boolean showDeleteButton,
            boolean useTextView) {
        this(null, showDeleteButton, useTextView);
    }

    /**
     * Create a new {@link ImpactModelElementNamer} with this listener for the element of each new sub list.
     *
     * @param listener The listener of the sub lists.
     * @param showDeleteButton if a delete button has to be shown or not
     * @param useTextView if a textView should be used or not
     */
    public ImpactModelElementNamer(RamListListener<COREImpactNode> listener, boolean showDeleteButton,
            boolean useTextView) {
        this.impactListListener = listener;
        this.showDeleteRootButton = showDeleteButton;
        this.useTextView = useTextView;
    }

    @Override
    public RamRectangleComponent getDisplayComponent(COREImpactNode element) {
        List<COREImpactNode> incomingElement = getIncoming(element);
        if (incomingElement.isEmpty()) {
            // create a simple selectable component
            return createComponent(element);
        }
        return createExpandableComponent(element, incomingElement);
    }

    /**
     * Retrieve the incoming element of this {@link COREImpactNode}.
     *
     * @param element the element for which we want to retrieve incoming element.
     * @return the list of incoming element
     */
    protected List<COREImpactNode> getIncoming(COREImpactNode element) {
        List<COREImpactNode> res = new ArrayList<COREImpactNode>();

        if (element.getIncoming() != null && !element.getIncoming().isEmpty()) {
            for (COREContribution contribution : element.getIncoming()) {
                COREImpactNode incomingElement = contribution.getSource();
                if (!(incomingElement instanceof COREFeatureImpactNode)) {
                    res.add(incomingElement);
                }
            }
        }

        return res;
    }

    /**
     * Creates a {@link RamRectangleComponent} associated to the given {@link COREImpactNode}. By default the
     * background is colored
     *
     * @param element the {@link COREImpactNode} to create the view for
     * @return the created {@link RamRectangleComponent}
     */
    protected RamRectangleComponent createComponent(COREImpactNode element) {
        return createComponent(element, false);
    }

    /**
     * Creates a {@link RamRectangleComponent} associated to the given {@link COREImpactNode}.
     *
     * @param element the {@link COREImpactNode} to create the view for
     * @param noFill whether the background shouldn't be colored or not
     * @return the created {@link RamRectangleComponent}
     */
    private RamRectangleComponent createComponent(final COREImpactNode element, boolean noFill) {
        RamRectangleComponent container = new RamRectangleComponent();
        container.setBufferSize(Cardinal.WEST, 5);
        container.setNoFill(noFill);
        container.setFillColor(Colors.DEFAULT_ELEMENT_FILL_COLOR);
        container.setNoStroke(false);
        container.setStrokeWeight(3.0f);
        container.setStrokeColor(Colors.DEFAULT_ELEMENT_STROKE_COLOR);

        if (showDeleteRootButton) {
            RamImageComponent deleteImage = new RamImageComponent(Icons.ICON_CLOSE, Colors.ICON_CLOSE_COLOR);
            deleteImage.setMinimumSize(20, 20);
            deleteImage.setMaximumSize(20, 20);
            deleteImage.setBufferSize(Cardinal.WEST, 10);
            deleteImage.setBufferSize(Cardinal.EAST, 10);

            RamButton deleteButton = new RamButton(deleteImage);

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    ControllerFactory.INSTANCE.getImpactModelElementController().removeRootGoal(
                            element);
                }
            });
            container.addChild(deleteButton);
        }

        RamTextComponent impactModelCell;
        if (useTextView) {
            impactModelCell = new TextView(element, CorePackage.Literals.CORE_NAMED_ELEMENT__NAME);
        } else {
            impactModelCell = new RamTextComponent(this.getElementName(element));
        }

        impactModelCell.setFont(Fonts.FONT_CLASS_NAME);
        impactModelCell.setNoStroke(true);

        impactModelCell.setAutoMaximizes(true);
        impactModelCell.setAlignment(Alignment.LEFT_ALIGN);
        container.addChild(impactModelCell);

        container.setLayout(new HorizontalLayoutVerticallyCentered(5));
        return container;
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
    private RamExpendableComponent createExpandableComponent(COREImpactNode element,
            List<COREImpactNode> incomingElements) {
        RamRectangleComponent title = createComponent(element, true);
        RamListComponent<COREImpactNode> content = createListImpactModelElement(incomingElements);
        RamExpendableComponent component = new RamExpendableComponent(title, content);
        component.setFillColor(Colors.DEFAULT_ELEMENT_FILL_COLOR);
        return component;
    }

    /**
     * Create a {@link RamListComponent} with the incoming elements.
     *
     * @param incomingElement the list of incoming elements
     * @return the created {@link RamListComponent}
     */
    private RamListComponent<COREImpactNode> createListImpactModelElement(
            List<COREImpactNode> incomingElement) {
        RamListComponent<COREImpactNode> list;

        try {
            ImpactModelElementNamer namer = (ImpactModelElementNamer) this.clone();
            namer.showDeleteRootButton = false;
            list = new RamListComponent<COREImpactNode>(incomingElement, namer);
        } catch (CloneNotSupportedException e) {
            LOGGER.debug("CloneNotSupportedException : Couldn't create a clone of the namer. "
                    + "The sub list won't have a namer.");
            list = new RamListComponent<COREImpactNode>(incomingElement);
        }

        list.setNoFill(true);
        list.setNoStroke(true);

        if (this.impactListListener != null) {
            list.registerListener(impactListListener);
        }

        return list;
    }

    @Override
    public String getSortingName(COREImpactNode element) {
        return element.getName();
    }

    @Override
    public String getSearchingName(COREImpactNode element) {
        return element.getName();
    }

    /**
     * Return the name of this element. By default, it call the operation getName() on this element. But a child can
     * override this operation.
     *
     * @param element the element which need a name.
     * @return the name of this element.
     */
    protected String getElementName(COREImpactNode element) {
        return element.getName();
    }

    /**
     * Set the listener for the element of each new sub list.
     *
     * @param listener The listener of the sub lists.
     */
    protected void setListener(RamListListener<COREImpactNode> listener) {
        this.impactListListener = listener;
    }

    /**
     * Set the value of the boolean showDeleteButton.
     *
     * @param showDeleteButton true for displaying the delete button in front of each element, false otherwise.
     */
    protected void setShowDeleteButton(boolean showDeleteButton) {
        this.showDeleteRootButton = showDeleteButton;
    }

    /**
     * Set the value of the boolean useTextView.
     *
     * @param useTextView true for displaying the title element using a {@link TextView}, false otherwise.
     */
    protected void setUseTextView(boolean useTextView) {
        this.useTextView = useTextView;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        ImpactModelElementNamer namer = (ImpactModelElementNamer) super.clone();
        namer.impactListListener = impactListListener;
        return namer;
    }
}
