package ca.mcgill.sel.ram.ui.views.ucm;

import org.eclipse.emf.common.notify.Notification;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.core.LayoutElement;
import ca.mcgill.sel.ram.ui.components.RamRectangleComponent;
import ca.mcgill.sel.ram.ui.utils.Colors;
import ca.mcgill.sel.ram.ui.utils.Constants;
import ca.mcgill.sel.ucm.EndPoint;

/**
 * EndPoint class.
 *
 * @author jerrywei
 *
 */
public class EndPointView extends PathNodeView {

    /**
     * Constructor for an EndPointView representing a {@link EndPoint}.
     *
     * @param endPt The {@link EndPoint} that this View represents.
     * @param layoutElement The layoutElement of this view.
     */
    public EndPointView(EndPoint endPt, LayoutElement layoutElement) {
        super(endPt, layoutElement);

        RamRectangleComponent rect = new RamRectangleComponent();
        rect.setMaximumSize(Constants.END_POINT_VIEW_WIDTH, Constants.END_POINT_VIEW_HEIGHT);
        rect.setNoFill(false);
        rect.setNoStroke(false);
        rect.setFillColor(Colors.PATH_NODE_FILL_COLOR);
        this.addChild(rect);

        EMFEditUtil.addListenerFor(endPt, this);

    }

    @Override
    public void notifyChanged(Notification notification) {

    }

}
