package Bellairs_stubonly.Instantiators.BaseStub;

import ram.reactiveworkflow.*;
import ram.workflow.*;

public class DevelopRoutePlanComplexStubWorkflowInstantiator extends WorkflowInstantiator{
    public Workflow workflow=new Workflow();
    public StubNode _ProposeRoute=new StubNode();
    public CustomizableNode _EnterNumFireTruck=createCustomizableNode("Bellairs_stubonly.Steps.BaseStub.develop.EnterNumFireTruck");
    public InputNode _FireRouteParameters=new InputNode();
    public SynchronizationNode _AndJoin115936=new SynchronizationNode();
    public StartupNode _develop=new StartupNode();
    public CustomizableNode _EnterNumPoliceVehicle=createCustomizableNode("Bellairs_stubonly.Steps.BaseStub.develop.EnterNumPoliceVehicle");
    public InputNode _PoliceRouteParameters=new InputNode();
    public EndNode _routesCoordinated=new EndNode();
    public ParallelExecutionNode _AndFork115896=new ParallelExecutionNode();
    public EndNode _routesNotCoordinated=new EndNode();
    public StubNode _aspectMarker117632=new StubNode();
    public StubNode _aspectMarker117636=new StubNode();
    public CustomizableNode _ProcessFireRouteParameters=createCustomizableNode("Bellairs_stubonly.Steps.BaseStub.FSCRouteConfirmation_PoliceRouteParameters_FireRouteParameters.ProcessFireRouteParameters");
    public CustomizableNode _ProcessPoliceRouteParameters=createCustomizableNode("Bellairs_stubonly.Steps.BaseStub.FSCRouteConfirmation_PoliceRouteParameters_FireRouteParameters.ProcessPoliceRouteParameters");

    public void link(){
        linkNodesToNextNodes();
        linkNodesToWorkflow();
        linkStartNodesToWorkflow();
    }

    public void linkNodesToNextNodes(){
        _ProposeRoute.addNextNode("1",_routesNotCoordinated);
        _ProposeRoute.addNextNode("2",_routesCoordinated);
        _EnterNumFireTruck.addNextNode(_FireRouteParameters);
        _FireRouteParameters.addNextNode(_aspectMarker117632,"1");
        _AndJoin115936.addNextNode(_ProposeRoute,"1");
        _develop.addNextNode(_AndFork115896);
        _EnterNumPoliceVehicle.addNextNode(_PoliceRouteParameters);
        _PoliceRouteParameters.addNextNode(_aspectMarker117636,"1");
        _AndFork115896.addNextNode(_EnterNumPoliceVehicle);
        _AndFork115896.addNextNode(_EnterNumFireTruck);
        _aspectMarker117632.addNextNode("1",_ProcessFireRouteParameters);
        _aspectMarker117636.addNextNode("1",_ProcessPoliceRouteParameters);
        _ProcessFireRouteParameters.addNextNode(_AndJoin115936);
        _ProcessPoliceRouteParameters.addNextNode(_AndJoin115936);
    }

    public void linkNodesToWorkflow(){
        workflow.addNode(_ProposeRoute);
        workflow.addNode(_EnterNumFireTruck);
        workflow.addNode(_FireRouteParameters);
        workflow.addNode(_AndJoin115936);
        workflow.addNode(_develop);
        workflow.addNode(_EnterNumPoliceVehicle);
        workflow.addNode(_PoliceRouteParameters);
        workflow.addNode(_routesCoordinated);
        workflow.addNode(_AndFork115896);
        workflow.addNode(_routesNotCoordinated);
        workflow.addNode(_aspectMarker117632);
        workflow.addNode(_aspectMarker117636);
        workflow.addNode(_ProcessFireRouteParameters);
        workflow.addNode(_ProcessPoliceRouteParameters);
    }

    public void linkStartNodesToWorkflow(){
        workflow.addStartupNode(_develop,false);
    }

    public void bind(Bellairs_stubonly.Instantiators.BaseStub.ProposeRouteWorkflowInstantiator p_ProposeRoute,
                     Bellairs_stubonly.Instantiators.Authentication.AuthenticationWorkflowInstantiator p_Authentication){

        Binding l_ProposeRoute_PluginBinding=new Binding(p_ProposeRoute);
        _ProposeRoute.addBinding(l_ProposeRoute_PluginBinding);
        _ProposeRoute.addInBinding(l_ProposeRoute_PluginBinding,"1",p_ProposeRoute._AndFork115939);
        _ProposeRoute.addOutBinding(l_ProposeRoute_PluginBinding,p_ProposeRoute._routesNotCoordinated,"1");
        _ProposeRoute.addOutBinding(l_ProposeRoute_PluginBinding,p_ProposeRoute._routesCoordinated,"2");

        Binding l_aspectMarker117632_PluginBinding=new Binding(p_Authentication);
        _aspectMarker117632.addBinding(l_aspectMarker117632_PluginBinding);
        _aspectMarker117632.addInBinding(l_aspectMarker117632_PluginBinding,"1",p_Authentication._checkAuthentication);
        _aspectMarker117632.addOutBinding(l_aspectMarker117632_PluginBinding,p_Authentication._authenticated,"1");

        Binding l_aspectMarker117636_PluginBinding=new Binding(p_Authentication);
        _aspectMarker117636.addBinding(l_aspectMarker117636_PluginBinding);
        _aspectMarker117636.addInBinding(l_aspectMarker117636_PluginBinding,"1",p_Authentication._checkAuthentication);
        _aspectMarker117636.addOutBinding(l_aspectMarker117636_PluginBinding,p_Authentication._authenticated,"1");
    }
}
