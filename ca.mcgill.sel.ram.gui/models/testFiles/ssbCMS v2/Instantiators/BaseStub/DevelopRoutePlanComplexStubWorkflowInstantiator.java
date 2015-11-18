package ssbCMS.Instantiators.BaseStub;

import ram.reactiveworkflow.*;
import ram.workflow.*;

public class DevelopRoutePlanComplexStubWorkflowInstantiator extends WorkflowInstantiator{
    public Workflow workflow=new Workflow();
    public StubNode _ProposeRoute=new StubNode();
    public CustomizableNode _EnterNumFireTruck=createCustomizableNode("ssbCMS.Steps.BaseStub.Develop.EnterNumFireTruck");
    public InputNode _FireRouteParameters=new InputNode();
    public SynchronizationNode _AndJoin115936=new SynchronizationNode();
    public StartupNode _Develop=new StartupNode();
    public CustomizableNode _EnterNumPoliceVehicle=createCustomizableNode("ssbCMS.Steps.BaseStub.Develop.EnterNumPoliceVehicle");
    public InputNode _PoliceRouteParameters=new InputNode();
    public EndNode _RoutesCoordinated=new EndNode();
    public ParallelExecutionNode _AndFork115896=new ParallelExecutionNode();
    public EndNode _RoutesNotCoordinated=new EndNode();
    public StubNode _AspectMarker117632=new StubNode();
    public StubNode _AspectMarker117636=new StubNode();
    public CustomizableNode _ProcessFireRouteParameters=createCustomizableNode("ssbCMS.Steps.BaseStub.FSCRouteConfirmation_PoliceRouteParameters_FireRouteParameters.ProcessFireRouteParameters");
    public CustomizableNode _ProcessPoliceRouteParameters=createCustomizableNode("ssbCMS.Steps.BaseStub.FSCRouteConfirmation_PoliceRouteParameters_FireRouteParameters.ProcessPoliceRouteParameters");

    public void link(){
        linkNodesToNextNodes();
        linkNodesToWorkflow();
        linkStartNodesToWorkflow();
    }

    public void linkNodesToNextNodes(){
        _ProposeRoute.addNextNode("1",_RoutesNotCoordinated);
        _ProposeRoute.addNextNode("2",_RoutesCoordinated);
        _EnterNumFireTruck.addNextNode(_FireRouteParameters);
        _FireRouteParameters.addNextNode(_AspectMarker117632,"1");
        _AndJoin115936.addNextNode(_ProposeRoute,"1");
        _Develop.addNextNode(_AndFork115896);
        _EnterNumPoliceVehicle.addNextNode(_PoliceRouteParameters);
        _PoliceRouteParameters.addNextNode(_AspectMarker117636,"1");
        _AndFork115896.addNextNode(_EnterNumPoliceVehicle);
        _AndFork115896.addNextNode(_EnterNumFireTruck);
        _AspectMarker117632.addNextNode("1",_ProcessFireRouteParameters);
        _AspectMarker117636.addNextNode("1",_ProcessPoliceRouteParameters);
        _ProcessFireRouteParameters.addNextNode(_AndJoin115936);
        _ProcessPoliceRouteParameters.addNextNode(_AndJoin115936);
    }

    public void linkNodesToWorkflow(){
        workflow.addNode(_ProposeRoute);
        workflow.addNode(_EnterNumFireTruck);
        workflow.addNode(_FireRouteParameters);
        workflow.addNode(_AndJoin115936);
        workflow.addNode(_Develop);
        workflow.addNode(_EnterNumPoliceVehicle);
        workflow.addNode(_PoliceRouteParameters);
        workflow.addNode(_RoutesCoordinated);
        workflow.addNode(_AndFork115896);
        workflow.addNode(_RoutesNotCoordinated);
        workflow.addNode(_AspectMarker117632);
        workflow.addNode(_AspectMarker117636);
        workflow.addNode(_ProcessFireRouteParameters);
        workflow.addNode(_ProcessPoliceRouteParameters);
    }

    public void linkStartNodesToWorkflow(){
        workflow.addStartupNode(_Develop,false);
    }

    public void bind(ssbCMS.Instantiators.BaseStub.ProposeRouteWorkflowInstantiator p_ProposeRoute,
                     ssbCMS.Instantiators.Authentication.AuthenticationWorkflowInstantiator p_Authentication){

        Binding l_ProposeRoute_PluginBinding=new Binding(p_ProposeRoute.workflow);
        _ProposeRoute.addBinding(l_ProposeRoute_PluginBinding);
        _ProposeRoute.addInBinding(l_ProposeRoute_PluginBinding,"1",p_ProposeRoute._AndFork115939);
        _ProposeRoute.addOutBinding(l_ProposeRoute_PluginBinding,p_ProposeRoute._RoutesNotCoordinated,"1");
        _ProposeRoute.addOutBinding(l_ProposeRoute_PluginBinding,p_ProposeRoute._RoutesCoordinated,"2");

        Binding l_AspectMarker117632_PluginBinding=new Binding(p_Authentication.workflow);
        _AspectMarker117632.addBinding(l_AspectMarker117632_PluginBinding);
        _AspectMarker117632.addInBinding(l_AspectMarker117632_PluginBinding,"1",p_Authentication._CheckAuthentication);
        _AspectMarker117632.addOutBinding(l_AspectMarker117632_PluginBinding,p_Authentication._Authenticated,"1");

        Binding l_AspectMarker117636_PluginBinding=new Binding(p_Authentication.workflow);
        _AspectMarker117636.addBinding(l_AspectMarker117636_PluginBinding);
        _AspectMarker117636.addInBinding(l_AspectMarker117636_PluginBinding,"1",p_Authentication._CheckAuthentication);
        _AspectMarker117636.addOutBinding(l_AspectMarker117636_PluginBinding,p_Authentication._Authenticated,"1");
    }
}
