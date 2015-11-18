package ca.mcgill.sel.ram.ui.views.ucm;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.mt4j.components.TransformSpace;
import org.mt4j.util.math.Vector3D;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.core.LayoutElement;
import ca.mcgill.sel.ram.ui.components.ContainerComponent;
import ca.mcgill.sel.ram.ui.layouts.Layout.HorizontalAlignment;
import ca.mcgill.sel.ram.ui.layouts.VerticalLayout;
import ca.mcgill.sel.ram.ui.views.IRelationshipEndView;
import ca.mcgill.sel.ram.ui.views.RamEnd;
import ca.mcgill.sel.ram.ui.views.RamEnd.Position;
import ca.mcgill.sel.ram.ui.views.ucm.handler.impl.PathNodeViewHandler;
import ca.mcgill.sel.ucm.PathNode;

/**
 * Abstract superclass for all nodes on paths of UCM.
 *
 * @author jerrywei
 */
public abstract class PathNodeView extends ContainerComponent<PathNodeViewHandler> implements
        INotifyChangedListener, IRelationshipEndView {

    /**
     * The PathNode that this pathNodeView links to.
     */
    protected PathNode pathNode;

    /**
     * The outgoing {@link NodeConnectionView}.
     */
    protected List<NodeConnectionView> outgoing = new ArrayList<NodeConnectionView>();

    /**
     * The incoming {@link NodeConnectionView}.
     */
    protected List<NodeConnectionView> incoming = new ArrayList<NodeConnectionView>();

    /**
     * Return true if this PathNodeView has a predecessor.
     */
    protected boolean hasSuccessor;

    /**
     * Whether this PathNodeView has a predecessor.
     */
    protected boolean hasPredecessor;

    /**
     * The layoutElement of this view.
     */
    protected LayoutElement layoutElement;

    /**
     * Create a default PathNodeView. It sets the Anchor to Center and the layout to VerticalLayout.
     *
     * @param pathNode The pathnode that this pathNodeView will link to
     * @param layoutElement The layoutElement for this View's position.
     */
    public PathNodeView(PathNode pathNode, LayoutElement layoutElement) {
        this.pathNode = pathNode;
        setAnchor(PositionAnchor.CENTER);
        setLayout(new VerticalLayout(HorizontalAlignment.CENTER));

        if (layoutElement != null) {
            setLayoutElement(layoutElement);
        }
        setNoFill(true);
        setNoStroke(true);

        EMFEditUtil.addListenerFor(pathNode, this);

        hasSuccessor = false;
        hasPredecessor = false;

    }

    @Override
    public void moveRelationshipEnd(RamEnd<?, ?> end, Position oldPosition, Position newPosition) {
        updateRelationshipEnd(end);
    }

    @Override
    public void updateRelationshipEnd(RamEnd<?, ?> end) {
        Vector3D location = this.getPosition(TransformSpace.GLOBAL);

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

    /**
     * Will return the pathnode that the pathNodeView links to.
     *
     * @return This pathnode that the pathNodeView links to.
     */
    public PathNode getPathNode() {
        return this.pathNode;
    }

    @Override
    public void translate(Vector3D dirVect) {
        super.translate(dirVect);

    }

    /**
     * Update the position of the incoming and outgoing NodeConnectionViews of this {@link PathNodeView}.
     */
    public void updateNodeConnectionsPositions() {
        // Drag the outgoing NodeConnectionView
        for (NodeConnectionView ncView : getOutgoing()) {
            ncView.updateLines();
            ncView.shouldUpdate();
        }

        // Drag the incoming NodeConnectionView
        for (NodeConnectionView ncView : getIncoming()) {
            ncView.updateLines();
            ncView.shouldUpdate();
        }
    }

    /**
     * Retrieve the outgoing nodeconnections of this node.
     *
     * @return the outgoing nodeconnections
     */
    public List<NodeConnectionView> getOutgoing() {
        return outgoing;
    }

    /**
     * Retrieve the incoming nodeconnections of this node.
     *
     * @return the incoming nodeconnections
     */
    public List<NodeConnectionView> getIncoming() {
        return incoming;
    }

    /**
     * Add a NodeConnectionView to the outgoing list.
     *
     * @param ncView the NodeConnectionView to add
     */
    public void addOutgoing(NodeConnectionView ncView) {
        this.outgoing.add(ncView);
        checkIfHasSuccessor();
    }

    /**
     * Remove a NodeConnectionView from the outgoing list.
     *
     * @param ncView the NodeConnectionView to remove
     */
    public void removeOutgoing(NodeConnectionView ncView) {
        this.outgoing.remove(ncView);
        checkIfHasSuccessor();
    }

    /**
     * Add a NodeConnectionView to the incoming list.
     *
     * @param ncView the NodeConnectionView to add
     */
    public void addIncoming(NodeConnectionView ncView) {
        this.incoming.add(ncView);
        checkIfHasPredecessor();
    }

    /**
     * Remove a NodeConnectionView to the incoming list.
     *
     * @param ncView the NodeConnectionView to remove
     */
    public void removeIncoming(NodeConnectionView ncView) {
        this.incoming.remove(ncView);
        checkIfHasPredecessor();
    }

    /**
     * Sets the HasSuccessor boolean to true if there is a successor to this node.
     */
    private void checkIfHasSuccessor() {
        if (this.outgoing.isEmpty()) {
            hasSuccessor = false;
        } else {
            hasSuccessor = true;
        }
    }

    /**
     * Sets the hasPredecessor boolean to true if there is a predecessor to this node.
     */
    private void checkIfHasPredecessor() {
        if (this.incoming.isEmpty()) {
            hasPredecessor = false;
        } else {
            hasPredecessor = true;
        }
    }

    /**
     * Return true if this PathNodeView has a successor.
     *
     * @return hasSuccessor if this PathNodeView has a successor
     */
    public boolean getHasSuccessor() {
        return hasSuccessor;
    }

    /**
     * Return true if this PathNodeView has a predecessor.
     *
     * @return hasPredecessor true if this PathNodeView has a predecessor
     */
    public boolean getHasPredecessor() {
        return hasPredecessor;
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

}
