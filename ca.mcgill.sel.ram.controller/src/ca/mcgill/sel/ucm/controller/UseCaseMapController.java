package ca.mcgill.sel.ucm.controller;

import java.util.HashMap;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import ca.mcgill.sel.commons.emf.util.EMFEditUtil;
import ca.mcgill.sel.core.COREConcern;
import ca.mcgill.sel.core.COREFeature;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.ucm.EndPoint;
import ca.mcgill.sel.ucm.ResponsibilityRef;
import ca.mcgill.sel.ucm.StartPoint;
import ca.mcgill.sel.ucm.UseCaseMap;
import ca.mcgill.sel.ucm.util.UCMModelUtil;

/**
 * A controller class for the UseCaseMap object.
 *
 * @author jerrywei
 *
 */
public class UseCaseMapController extends UCMBaseController {

    /**
     * A map to contain the number of UCMs associated with specific features.
     */
    protected HashMap<COREFeature, Integer> ucmCardinalityMap;

    /**
     * Creates a new instance of {@link UseCaseMapController}.
     */
    UseCaseMapController() {
        // prevent anyone outside this package to instantiate
        ucmCardinalityMap = new HashMap<COREFeature, Integer>();
    }

    /**
     * Creates a standard path (start point - responsibility - end point) in the ucm.
     *
     * @param ucm The ucm to add this standard path.
     * @return The ucm
     */
    public UseCaseMap createStandardPath(UseCaseMap ucm) {
        StartPointController spController = ControllerFactory.INSTANCE.getStartPointController();
        String spName = UCMModelUtil.getUniquePathNodeName(ucm);
        StartPoint sp = spController.createStartPoint(ucm, 100, 200, spName);

        ResponsibilityController respController = ControllerFactory.INSTANCE.getResponsibilityController();
        String respName = UCMModelUtil.getUniquePathNodeName(ucm);
        ResponsibilityRef respRef = respController.createResponsibilityRef(ucm, 300, 200, respName);

        EndPointController epController = ControllerFactory.INSTANCE.getEndPointController();
        String epName = UCMModelUtil.getUniquePathNodeName(ucm);
        EndPoint ep = epController.createEndPoint(ucm, 500, 200, epName);

        NodeConnectionController ncController = ControllerFactory.INSTANCE.getNodeConnectionController();

        ncController.createNodeConnection(ucm, sp, respRef);
        ncController.createNodeConnection(ucm, respRef, ep);

        return ucm;
    }

    /**
     * Create a UCM and associate it with the selected feature from the concern.
     *
     * @param concern The concern containing the feature
     * @param feature The feature the ucm is associated with
     * @return The ucm
     */
    public UseCaseMap createUCMAndAssociate(COREConcern concern, COREFeature feature) {

        UseCaseMap ucm;

        // Add "UCM" in front of model to identify easier when shown in selectorView with aspects with the same feature
        // name. As there can be more than 1 ucm associated with a feature, name the UCM models differently to
        // distinguish easier.
        if (ucmCardinalityMap.get(feature) == null || ucmCardinalityMap.get(feature) == 0) {
            ucm = UCMModelUtil.createUCM(feature.getName());
            ucmCardinalityMap.put(feature, 1);
        } else {
            int i = ucmCardinalityMap.get(feature);
            ucm = UCMModelUtil.createUCM(feature.getName() + i);
            ucmCardinalityMap.put(feature, i + 1);
        }

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(ucm);

        CompoundCommand compoundCommand = new CompoundCommand();

        compoundCommand.append(SetCommand.create(editingDomain, ucm, CorePackage.Literals.CORE_MODEL__CORE_CONCERN,
                concern));

        compoundCommand.append(AddCommand.create(editingDomain, ucm,
                CorePackage.Literals.CORE_MODEL__REALIZES, feature));

        doExecute(editingDomain, compoundCommand);

        createStandardPath(ucm);

        return ucm;
    }

    /**
     * Add a ucm to a concern's models.
     *
     * @param concern - The concerner
     * @param ucm - The ucm to add
     */
    public void addModelToConcern(COREConcern concern, UseCaseMap ucm) {

        EditingDomain editingDomain = EMFEditUtil.getEditingDomain(ucm);

        Command setUCMForModelCommand =
                SetCommand.create(editingDomain, ucm, CorePackage.Literals.CORE_MODEL__CORE_CONCERN, concern);

        doExecute(editingDomain, setUCMForModelCommand);

    }

}
