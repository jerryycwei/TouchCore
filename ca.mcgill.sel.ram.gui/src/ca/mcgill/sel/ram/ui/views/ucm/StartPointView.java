package ca.mcgill.sel.ram.ui.views.ucm;

import org.eclipse.emf.common.notify.Notification;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.core.LayoutElement;
import ca.mcgill.sel.ram.ui.components.ContainerComponent;
import ca.mcgill.sel.ram.ui.components.RamRoundedRectangleComponent;
import ca.mcgill.sel.ram.ui.layouts.Layout.HorizontalAlignment;
import ca.mcgill.sel.ram.ui.layouts.VerticalLayout;
import ca.mcgill.sel.ram.ui.utils.Colors;
import ca.mcgill.sel.ram.ui.utils.Constants;
import ca.mcgill.sel.ram.ui.views.TextView;
import ca.mcgill.sel.ram.ui.views.handler.HandlerFactory;
import ca.mcgill.sel.ucm.StartPoint;

/**
 * The View class for a Start Point.
 *
 * @author jerrywei
 *
 */
public class StartPointView extends PathNodeView {

    /**
     * The {@link RamRoundedRectangleComponent} that is the circle for the StartPointView.
     */
    protected RamRoundedRectangleComponent startPointRoundedRectangle;

    /**
     * The {@link ContainerComponent} that contains the rounded rectangle.
     */
    protected ContainerComponent<IGestureEventListener> roundedRectContainer;

    /**
     * The {@link TextView} that displays the name of the PathNode.
     */
    protected TextView spTextView;

    /**
     * The {@link ContainerComponent} that contains the text view.
     */
    protected ContainerComponent<IGestureEventListener> nameContainer;

    /**
     * Creates new StartPoint for a UCM.
     *
     * @param stPt the {@link StartPoint} that this view represents.
     * @param layoutElement The layoutElement that contains the position of this view.
     */
    public StartPointView(StartPoint stPt, LayoutElement layoutElement) {
        super(stPt, null);

        setPickable(false);
        EMFEditUtil.addListenerFor(stPt, this);

        spTextView = new TextView(stPt, CorePackage.Literals.CORE_NAMED_ELEMENT__NAME);
        spTextView.registerTapProcessor(HandlerFactory.INSTANCE.getTextViewHandler());
        spTextView.setAutoMaximizes(false);
        spTextView.setAutoMinimizes(false);

        nameContainer = new ContainerComponent<IGestureEventListener>();
        nameContainer.setAutoMaximizes(true);
        nameContainer.setLayout(new VerticalLayout(HorizontalAlignment.CENTER));
        nameContainer.setNoStroke(true);
        nameContainer.setNoFill(true);

        startPointRoundedRectangle = new RamRoundedRectangleComponent(Constants.START_POINT_CIRCLE_RADIUS);
        startPointRoundedRectangle.setNoFill(false);
        startPointRoundedRectangle.setNoStroke(false);
        startPointRoundedRectangle.setFillColor(Colors.PATH_NODE_FILL_COLOR);
        startPointRoundedRectangle.setAutoMaximizes(false);
        startPointRoundedRectangle.setAutoMinimizes(false);

        roundedRectContainer = new ContainerComponent<IGestureEventListener>();
        roundedRectContainer.setMaximumSize(Constants.START_POINT_CIRCLE_CONTAINER_MAX_SIZE,
                Constants.START_POINT_CIRCLE_CONTAINER_MAX_SIZE);
        roundedRectContainer.setAutoMaximizes(false);
        roundedRectContainer.setLayout(new VerticalLayout(HorizontalAlignment.CENTER));
        roundedRectContainer.setNoStroke(true);
        roundedRectContainer.setNoFill(true);

        this.setAutoMaximizes(true);
        this.setAutoMinimizes(true);

        this.addChild(nameContainer);
        this.addChild(roundedRectContainer);

        nameContainer.addChild(spTextView);
        roundedRectContainer.addChild(startPointRoundedRectangle);

        setLayoutElement(layoutElement);
    }

    @Override
    public void notifyChanged(Notification notification) {

    }

    @Override
    public boolean containsPointGlobal(Vector3D testPoint) {
        if (roundedRectContainer != null && nameContainer != null) {
            return startPointRoundedRectangle.containsPointGlobal(testPoint) || nameContainer
                    .containsPointGlobal(testPoint);
        }
        return super.containsPointGlobal(testPoint);
    }

    @Override
    public Vector3D getCenterPointLocal() {
        if (roundedRectContainer != null) {
            return roundedRectContainer.getCenterPointRelativeToParent();
        }
        return super.getCenterPointLocal();

    }
}
