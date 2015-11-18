package ca.mcgill.sel.ram.ui.views.impact.namer;

import java.util.List;

import org.mt4j.util.logging.ILogger;
import org.mt4j.util.logging.MTLoggerFactory;

import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.ram.ui.components.RamExpendableComponent;
import ca.mcgill.sel.ram.ui.components.RamListComponent;
import ca.mcgill.sel.ram.ui.components.RamListComponent.Namer;
import ca.mcgill.sel.ram.ui.components.RamRectangleComponent;
import ca.mcgill.sel.ram.ui.components.RamRectangleComponent.Cardinal;
import ca.mcgill.sel.ram.ui.components.RamTextComponent.Alignment;
import ca.mcgill.sel.ram.ui.components.listeners.RamListListener;
import ca.mcgill.sel.ram.ui.layouts.HorizontalLayoutVerticallyCentered;
import ca.mcgill.sel.ram.ui.utils.Colors;
import ca.mcgill.sel.ram.ui.utils.Fonts;
import ca.mcgill.sel.ram.ui.views.TextView;

/**
 * A namer that is capable of displaying a hierarchy of {@link COREFeature}. If the {@link COREFeature} has no child,
 * just creates a {@link TextView} else, create an {@link RamExpendableComponent} with a {@link TextView} as title and a
 * {@link RamListComponent} with this same namer as hideable part of the container.
 *
 * @author Romain
 */
public class ImpactFeatureNamer implements Namer<COREFeature> {
    /** The Constant logger. */
    private static final ILogger LOGGER = MTLoggerFactory.getLogger(ImpactFeatureNamer.class.getName());
    static {
        // LOGGER.setLevel(ILogger.ERROR);
        // LOGGER.setLevel(ILogger.WARN);
        LOGGER.setLevel(ILogger.DEBUG);
        // LOGGER.setLevel(ILogger.INFO);
    }

    private RamListListener<COREFeature> featureListListener;

    /**
     * Create a new {@link ImpactFeatureNamer} with no listener for the element of the sub list.
     *
     */
    public ImpactFeatureNamer() {
        this(null);
    }

    /**
     * Create a new {@link ImpactFeatureNamer} with this listener for the element of each new sub list.
     *
     * @param listener The listener of the sub lists.
     *
     */
    public ImpactFeatureNamer(RamListListener<COREFeature> listener) {
        this.featureListListener = listener;
    }

    @Override
    public RamRectangleComponent getDisplayComponent(COREFeature element) {
        List<COREFeature> children = element.getChildren();
        if (children.isEmpty()) {
            // create a simple selectable component
            return createComponent(element);
        }
        return createExpandableComponent(element, children);
    }

    /**
     * Creates a {@link RamRectangleComponent} associated to the given {@link COREFeature}. By default the
     * background is colored
     *
     * @param element the {@link COREFeature} to create the view for
     * @return the created {@link RamRectangleComponent}
     */
    protected RamRectangleComponent createComponent(COREFeature element) {
        return createComponent(element, false);
    }

    /**
     * Creates a {@link RamRectangleComponent} associated to the given {@link COREFeature}.
     *
     * @param element the {@link COREFeature} to create the view for
     * @param noFill whether the background shouldn't be colored or not
     * @return the created {@link RamRectangleComponent}
     */
    private static RamRectangleComponent createComponent(final COREFeature element, boolean noFill) {
        RamRectangleComponent container = new RamRectangleComponent();
        container.setBufferSize(Cardinal.WEST, 5);
        container.setNoFill(noFill);
        container.setFillColor(Colors.DEFAULT_ELEMENT_FILL_COLOR);
        container.setNoStroke(false);
        container.setStrokeWeight(3.0f);
        container.setStrokeColor(Colors.DEFAULT_ELEMENT_STROKE_COLOR);

        TextView impactModelCell =
                new TextView(element, CorePackage.Literals.CORE_NAMED_ELEMENT__NAME);

        impactModelCell.setFont(Fonts.FONT_CLASS_NAME);
        impactModelCell.setNoStroke(true);

        impactModelCell.setAutoMaximizes(true);
        impactModelCell.setAlignment(Alignment.LEFT_ALIGN);
        container.addChild(impactModelCell);

        container.setLayout(new HorizontalLayoutVerticallyCentered(5));
        return container;
    }

    /**
     * Creates a {@link RamExpendableComponent} associated to the given {@link COREFeature}. The
     * {@link RamExpendableComponent}'s title is a {@link RamRectangleComponent} Its hideable content is a
     * {@link RamListComponent} of the children {@link COREFeature}.
     *
     * @param element the {@link COREFeature} to create the view for
     * @param children the list of children
     *
     * @return the created {@link RamExpendableComponent}
     */
    private RamExpendableComponent createExpandableComponent(COREFeature element,
            List<COREFeature> children) {
        RamRectangleComponent title = createComponent(element, true);
        RamListComponent<COREFeature> content = createListImpactModelElement(children);
        RamExpendableComponent component = new RamExpendableComponent(title, content);
        component.setFillColor(Colors.DEFAULT_ELEMENT_FILL_COLOR);
        return component;
    }

    /**
     * Create a {@link RamListComponent} with the children.
     *
     * @param children the list of children
     * @return the created {@link RamListComponent}
     */
    private RamListComponent<COREFeature> createListImpactModelElement(
            List<COREFeature> children) {
        RamListComponent<COREFeature> list =
                new RamListComponent<COREFeature>(children, this);

        list.setNoFill(true);
        list.setNoStroke(true);

        if (this.featureListListener != null) {
            list.registerListener(featureListListener);
        }

        return list;
    }

    @Override
    public String getSortingName(COREFeature element) {
        return element.getName();
    }

    @Override
    public String getSearchingName(COREFeature element) {
        return element.getName();
    }

    /**
     * Set the listener for the element of each new sub list.
     *
     * @param listener The listener of the sub lists.
     */
    protected void setListener(RamListListener<COREFeature> listener) {
        this.featureListListener = listener;
    }
}
