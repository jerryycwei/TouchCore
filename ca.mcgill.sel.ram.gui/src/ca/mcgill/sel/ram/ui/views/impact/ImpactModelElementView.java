package ca.mcgill.sel.ram.ui.views.impact;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.core.COREImpactNode;
import ca.mcgill.sel.core.LayoutElement;
import ca.mcgill.sel.ram.ui.components.ContainerComponent;
import ca.mcgill.sel.ram.ui.layouts.VerticalLayout;
import ca.mcgill.sel.ram.ui.views.IRelationshipEndView;
import ca.mcgill.sel.ram.ui.views.RamEnd;
import ca.mcgill.sel.ram.ui.views.RamEnd.Position;
import ca.mcgill.sel.ram.ui.views.handler.IAbstractViewHandler;
import ca.mcgill.sel.ram.ui.views.impact.handler.impl.ImpactModelElementViewHandler;

/**
 * Class used to visually representing any element in an Impact Model.
 *
 * @param <T> the handler interface that this component uses
 * @author Romain
 *
 */
public abstract class ImpactModelElementView<T extends ImpactModelElementViewHandler> extends ContainerComponent<T>
        implements INotifyChangedListener, IRelationshipEndView {

    /**
     * The root of this View.
     */
    protected COREImpactNode rootImpactModelElement;

    /**
     * The associated element.
     */
    protected COREImpactNode impactModelElement;

    /**
     * The layoutElement of this view.
     */
    protected LayoutElement layoutElement;

    /**
     * The outgoing {@link ContributionView}.
     */
    protected List<ContributionView> outgoing = new ArrayList<ContributionView>();

    /**
     * The incoming {@link ContributionView}.
     */
    protected List<ContributionView> incoming = new ArrayList<ContributionView>();

    /**
     * Function used to create a visual representation of the goal.
     *
     * @param root The root {@link COREImpactNode} of this view
     * @param element The associated {@link COREImpactNode}
     * @param impactDiagramView The scene associated to this element
     */
    public ImpactModelElementView(COREImpactNode root, COREImpactNode element,
            ImpactDiagramView<? extends IAbstractViewHandler> impactDiagramView) {
        super();

        setLayout(new VerticalLayout());
        setAnchor(PositionAnchor.CENTER);

        rootImpactModelElement = root;
        impactModelElement = element;

    }

    @Override
    public void notifyChanged(final Notification notification) {
        if (notification.getNotifier() == layoutElement) {
            setPositionRelativeToParent(new Vector3D(layoutElement.getX(), layoutElement.getY()));
            this.updateLinksPosition();
        }
    }

    /**
     * Sets the layout element for the corresponding class.
     *
     * @param layoutElement the {@link LayoutElement} to set
     */
    public void setLayoutElement(final LayoutElement layoutElement) {
        this.layoutElement = layoutElement;
        setPositionGlobal(new Vector3D(layoutElement.getX(), layoutElement.getY()));

        EMFEditUtil.addListenerFor(layoutElement, this);
    }

    @Override
    public void translate(Vector3D dirVect) {
        super.translate(dirVect);
        this.sendToFront();
        this.updateLinksPosition();
    }

    @Override
    protected void handleChildResized(MTComponent component) {
        super.handleChildResized(component);
        this.updateLinksPosition();
    }

    /**
     * Update the position of the incoming and outgoing link of this {@link ImpactModelElementView}.
     */
    private void updateLinksPosition() {
        // Drag the outgoing AssociationLinkview
        for (ContributionView link : getOutgoing()) {
            link.updateLines();
            // link.shouldUpdate();
            link.sendToFront();
        }

        // Drag the incoming AssociationLinkview
        for (ContributionView link : getIncoming()) {
            link.updateLines();
            // link.shouldUpdate();
            link.sendToFront();
        }
    }

    /**
     * Returns the {@link COREImpactNode} that is the root of this view.
     *
     * @return The root of this view
     */
    public COREImpactNode getRootImpactModelElement() {
        return rootImpactModelElement;
    }

    /**
     * Returns the {@link COREImpactNode} associated with the representation.
     *
     * @return The impact model element.
     */
    public COREImpactNode getImpactModelElement() {
        return impactModelElement;
    }

    /**
     * Retrieve the outgoing link of this node.
     *
     * @return the outgoings
     */
    public List<ContributionView> getOutgoing() {
        return outgoing;
    }

    /**
     * Retrieve the incoming link of this node.
     *
     * @return the incomings
     */
    public List<ContributionView> getIncoming() {
        return incoming;
    }

    /**
     * Return if this view is the root of the {@link ImpactDiagramView}.
     *
     * @return true it is the root of the {@link ImpactDiagramView}, false otherwise.
     */
    public boolean isRootImpactModelElement() {
        return rootImpactModelElement == impactModelElement;
    }

    /**
     * Add a link to the outgoing list.
     *
     * @param associationLinkView the link to add
     */
    public void addOutgoing(ContributionView associationLinkView) {
        this.outgoing.add(associationLinkView);
    }

    /**
     * Remove a link to the outgoing list.
     *
     * @param associationLinkView the link to remove
     */
    public void removeOutgoing(ContributionView associationLinkView) {
        this.outgoing.remove(associationLinkView);
    }

    /**
     * Add a link to the incoming list.
     *
     * @param associationLinkView the link to add
     */
    public void addIncoming(ContributionView associationLinkView) {
        this.incoming.add(associationLinkView);
    }

    /**
     * Remove a link to the incoming list.
     *
     * @param associationLinkView the link to remove
     */
    public void removeIncoming(ContributionView associationLinkView) {
        this.incoming.remove(associationLinkView);
    }

    @Override
    public void moveRelationshipEnd(RamEnd<?, ?> end, Position oldPosition, Position newPosition) {
        updateRelationshipEnd(end);
    }

    @Override
    public void updateRelationshipEnd(RamEnd<?, ?> end) {
        float width = this.getWidthXYGlobal();
        float height = this.getHeightXYGlobal();

        float offsetX = 0;
        float offsetY = 0;

        if (end.getPosition() == Position.BOTTOM || end.getOpposite().getPosition() == Position.TOP) {
            offsetY = height / 2;
        } else if (end.getPosition() == Position.TOP || end.getOpposite().getPosition() == Position.BOTTOM) {
            offsetY = -height / 2;
        } else if (end.getPosition() == Position.LEFT && end.getOpposite().getPosition() == Position.RIGHT) {
            offsetX = -width / 2;
        } else if (end.getPosition() == Position.RIGHT && end.getOpposite().getPosition() == Position.LEFT) {
            offsetX = width / 2;
        }

        Vector3D location = this.getPosition(TransformSpace.GLOBAL);

        float rightX = location.getX() + offsetX;
        location.setX(rightX);

        float rightY = location.getY() + offsetY;
        location.setY(rightY);

        /*
         * The position needs to be converted to relative position, because drawing everything excepts a relative
         * position.
         */
        location = getGlobalVecToParentRelativeSpace(this, location);
        end.setLocation(location);
    }

    @Override
    public void removeRelationshipEnd(RamEnd<?, ?> end) {

    }

    @Override
    public void destroy() {
        if (layoutElement != null) {
            EMFEditUtil.removeListenerFor(layoutElement, this);
        }
        super.destroy();
    }

    /**
     * Change the color of this component.
     *
     * @param color the color to use.
     */
    public abstract void changeColor(MTColor color);

    /**
     * Change the color back to the default color.
     */
    public abstract void changeColorToDefault();

}
