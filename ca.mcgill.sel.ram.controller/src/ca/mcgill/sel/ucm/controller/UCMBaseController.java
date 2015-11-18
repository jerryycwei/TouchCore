package ca.mcgill.sel.ucm.controller;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import ca.mcgill.sel.core.CoreFactory;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.core.LayoutElement;
import ca.mcgill.sel.core.controller.CoreBaseController;
import ca.mcgill.sel.core.impl.LayoutMapImpl;
import ca.mcgill.sel.ucm.PathNode;
import ca.mcgill.sel.ucm.UCMPackage;
import ca.mcgill.sel.ucm.UseCaseMap;

/**
 * The controller with basic functionality to be used by any UCM class.
 *
 * @author jerrywei
 *
 */
public abstract class UCMBaseController extends CoreBaseController {

    /**
     * Returns a command which creates a layout element with the x, y coordinates and adds this to a layout map with the
     * path node as the key EObject.
     *
     * @param editingDomain The editing domain
     * @param ucm The ucm to add the path node to
     * @param pn The path node to map a layout element to
     * @param x The x coordinate
     * @param y The y coordinate
     * @return a Command which adds the path node and layout element to the layout map
     */
    protected Command createLayoutElementCommand(EditingDomain editingDomain, UseCaseMap ucm,
            PathNode pn, float x, float y) {

        LayoutElement layoutElement = CoreFactory.eINSTANCE.createLayoutElement();
        layoutElement.setX(x);
        layoutElement.setY(y);

        LayoutMapImpl layoutEntry = (LayoutMapImpl) CoreFactory.eINSTANCE.create(CorePackage.Literals.LAYOUT_MAP);
        layoutEntry.setKey(pn);
        layoutEntry.setValue(layoutElement);

        Command layoutEntryCommand;

        layoutEntryCommand =
                AddCommand.create(editingDomain, ucm,
                        UCMPackage.Literals.USE_CASE_MAP__LAYOUT_MAPS, layoutEntry);

        return layoutEntryCommand;
    }

}
