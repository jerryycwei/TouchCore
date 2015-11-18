package ca.mcgill.sel.ram.ui.views.ucm;

import org.eclipse.emf.common.notify.Notification;
import org.mt4j.util.math.Vertex;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.core.LayoutElement;
import ca.mcgill.sel.ram.ui.components.RamLineComponent;
import ca.mcgill.sel.ram.ui.components.RamRectangleComponent;
import ca.mcgill.sel.ram.ui.utils.Colors;
import ca.mcgill.sel.ram.ui.utils.Constants;
import ca.mcgill.sel.ucm.ResponsibilityRef;

/**
 * The view for a {@link ResponsibilityRef}.
 *
 * @author jerrywei
 *
 */
public class ResponsibilityView extends PathNodeView {

    /**
     * Constructor for the Responsibility View.
     *
     * @param respRef The {@link ResponsibilityRef} that this view realises.
     * @param layoutElement The {@link LayoutElement} with this view's position.
     */
    public ResponsibilityView(ResponsibilityRef respRef, LayoutElement layoutElement) {
        super(respRef, layoutElement);

        setMaximumSize(Constants.RESPONSIBILITY_VIEW_MAX_SIZE, Constants.RESPONSIBILITY_VIEW_MAX_SIZE);
        EMFEditUtil.addListenerFor(respRef, this);

        Vertex[] vertices = this.getVerticesLocal();

        RamLineComponent leftLine = new RamLineComponent(vertices[0], vertices[2]);
        leftLine.setNoFill(false);
        leftLine.setNoStroke(false);
        leftLine.setFillColor(Colors.PATH_NODE_FILL_COLOR);
        leftLine.setStrokeWeight(Constants.RESPONSIBILITY_VIEW_STROKE_WEIGHT);

        RamLineComponent rightLine = new RamLineComponent(vertices[1], vertices[3]);
        rightLine.setNoFill(false);
        rightLine.setNoStroke(false);
        rightLine.setFillColor(Colors.PATH_NODE_FILL_COLOR);
        rightLine.setStrokeWeight(Constants.RESPONSIBILITY_VIEW_STROKE_WEIGHT);

        RamRectangleComponent rect = new RamRectangleComponent();

        rect.setNoFill(true);
        rect.setNoStroke(true);
        rect.setMaximumSize(Constants.RESPONSIBILITY_VIEW_MAX_SIZE, Constants.RESPONSIBILITY_VIEW_MAX_SIZE);

        rect.addChild(leftLine);
        rect.addChild(rightLine);
        this.addChild(rect);

    }

    @Override
    public void notifyChanged(Notification notification) {

    }

}
