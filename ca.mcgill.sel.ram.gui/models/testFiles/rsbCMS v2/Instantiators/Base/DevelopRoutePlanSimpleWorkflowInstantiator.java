package rsbCMS.Instantiators.Base;

import ram.reactiveworkflow.*;
import ram.workflow.*;

public class DevelopRoutePlanSimpleWorkflowInstantiator extends WorkflowInstantiator{
    public Workflow workflow=new Workflow();
    public StartupNode _EstablishRoutes=new StartupNode();
    public EndNode _RoutesEstablished=new EndNode();
    public InputNode _SelectedVehicle=new InputNode();
    public CustomizableNode _ShowVehicleList=createCustomizableNode("rsbCMS.Steps.Base.EstablishRoutes.ShowVehicleList");
    public CustomizableNode _ShowAssignment=createCustomizableNode("rsbCMS.Steps.Base.SelectedVehicle.ShowAssignment");
    public StubNode _AspectMarker117697=new StubNode();

    public void link(){
        linkNodesToNextNodes();
        linkNodesToWorkflow();
        linkStartNodesToWorkflow();
    }

    public void linkNodesToNextNodes(){
        _EstablishRoutes.addNextNode(_ShowVehicleList);
        _SelectedVehicle.addNextNode(_AspectMarker117697,"1");
        _ShowVehicleList.addNextNode(_SelectedVehicle);
        _ShowAssignment.addNextNode(_RoutesEstablished);
        _AspectMarker117697.addNextNode("1",_ShowAssignment);
    }

    public void linkNodesToWorkflow(){
        workflow.addNode(_EstablishRoutes);
        workflow.addNode(_RoutesEstablished);
        workflow.addNode(_SelectedVehicle);
        workflow.addNode(_ShowVehicleList);
        workflow.addNode(_ShowAssignment);
        workflow.addNode(_AspectMarker117697);
    }

    public void linkStartNodesToWorkflow(){
        workflow.addStartupNode(_EstablishRoutes,false);
    }

    public void bind(rsbCMS.Instantiators.Authentication.AuthenticationWorkflowInstantiator p_Authentication){

        Binding l_AspectMarker117697_PluginBinding=new Binding(p_Authentication.workflow);
        _AspectMarker117697.addBinding(l_AspectMarker117697_PluginBinding);
        _AspectMarker117697.addInBinding(l_AspectMarker117697_PluginBinding,"1",p_Authentication._CheckAuthentication);
        _AspectMarker117697.addOutBinding(l_AspectMarker117697_PluginBinding,p_Authentication._Authenticated,"1");
    }
}
