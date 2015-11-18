package ca.mcgill.sel.ucm.util;

import ca.mcgill.sel.core.util.COREModelUtil;
import ca.mcgill.sel.ucm.UCMFactory;
import ca.mcgill.sel.ucm.UseCaseMap;

/**
 * Helper class with convenient static methods for working with UCM EMF model objects.
 *
 * @author jerrywei
 */
public final class UCMModelUtil {

    /**
     * Creates a new instance of {@link UCMModelUtil}.
     */
    private UCMModelUtil() {
        // suppress default constructor
    }

    public static UseCaseMap createUCM(String name) {
        UseCaseMap ucm = UCMFactory.eINSTANCE.createUseCaseMap();
        ucm.setName(name);
        return ucm;
    }

    /**
     * Return a unique name for a new pathnode. The resulting name should be unique throughout all pathnodes in a ucm.
     *
     * @param ucm
     * @return a unique name for the new pathnode.
     */
    public static String getUniquePathNodeName(UseCaseMap ucm) {
        if (ucm == null) {
            return "PathNode";
        }
        return COREModelUtil.createUniqueNameFromElements("PathNode", ucm.getNodes());
    }
}
