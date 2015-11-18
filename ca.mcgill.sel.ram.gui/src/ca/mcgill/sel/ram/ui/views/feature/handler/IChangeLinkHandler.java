package ca.mcgill.sel.ram.ui.views.feature.handler;

import ca.mcgill.sel.ram.ui.events.listeners.ITapListener;
import ca.mcgill.sel.ram.ui.views.feature.FeatureView;

/**
 * Interface used to detect tap/touch gesture/action on links/association.
 * @author Nishanth
 *
 */
public interface IChangeLinkHandler extends ITapListener {
    
    /**
     * Function used to set the Feature Icon to the link.
     * Added on the ellipse circle for Mandatory/Optional or the triangle for XOR /OR.
     * @param feature - the feature to add the feature Icon for
     */
    void setFeatureIcon(final FeatureView feature);
}
