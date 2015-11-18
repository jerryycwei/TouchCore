package ca.mcgill.sel.core.weaver.util;

import org.eclipse.emf.ecore.EObject;

/**
 * Interface used to listen to events from the weaver.
 * TODO: Possible improvements: Do not throw Exception,
 * but custom WeaverExceptions depending of the error that occurred.
 * other: May be useful to be able to send events during weaving at some points, or if errors that do not impact weaving
 * occur (for example error during creation of the tracing).
 *
 * @author CCamillieri
 * @param <T> Type of the weaving result.
 */
public interface WeaverListener<T extends EObject> {

    /**
     * Called when the weaver starts its operations.
     */
    void weavingStarted();

    /**
     * Called if the weaving successfully finished.
     *
     * @param result - The woven result.
     */
    void weavingFinished(T result);

    /**
     * Called if an exception was get during weaving.
     * 
     * @param exception - The caught {@link Exception}
     */
    void weavingFailed(Exception exception);

}
