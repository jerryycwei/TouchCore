package Bellairs_stubonly.Instantiators.BaseStub;

import ram.reactiveworkflow.*;
import ram.workflow.*;

public class ProposeRouteWorkflowInstantiator extends WorkflowInstantiator{
    public Workflow workflow=new Workflow();
    public CustomizableNode _AlertFSCNotCoordinated=createCustomizableNode("Bellairs_stubonly.Steps.BaseStub.FireRouteProposal_PoliceRouteProposal.AlertFSCNotCoordinated");
    public EndNode _routesNotCoordinated=new EndNode();
    public CustomizableNode _AskForPoliceRoute=createCustomizableNode("Bellairs_stubonly.Steps.BaseStub.FSCRouteConfirmation_PoliceRouteParameters_FireRouteParameters.AskForPoliceRoute");
    public InputNode _PoliceRouteProposal=new InputNode();
    public EndNode _routesCoordinated=new EndNode();
    public CustomizableNode _AskForFireRoute=createCustomizableNode("Bellairs_stubonly.Steps.BaseStub.FSCRouteConfirmation_PoliceRouteParameters_FireRouteParameters.AskForFireRoute");
    public InputNode _FireRouteProposal=new InputNode();
    public ConditionalExecutionNode _OrFork117115=new ConditionalExecutionNode();
    public ConditionalExecutionNode _OrFork117600=new ConditionalExecutionNode();
    public ParallelExecutionNode _AndFork115939=new ParallelExecutionNode();
    public SynchronizationNode _AndJoin115953=new SynchronizationNode();
    public StubNode _aspectMarker117641=new StubNode();
    public StubNode _aspectMarker117643=new StubNode();
    public CustomizableNode _ProposedRouteToFSC=createCustomizableNode("Bellairs_stubonly.Steps.BaseStub.FireRouteProposal_PoliceRouteProposal.ProposedRouteToFSC");
    public InputNode _FSCRouteConfirmation=new InputNode();
    public StubNode _aspectMarker117655=new StubNode();
    public CustomizableNode _ProcessPoliceRouteProposal=createCustomizableNode("Bellairs_stubonly.Steps.BaseStub.FireRouteProposal_PoliceRouteProposal.ProcessPoliceRouteProposal");
    public CustomizableNode _ProcessFireRouteProposal=createCustomizableNode("Bellairs_stubonly.Steps.BaseStub.FireRouteProposal_PoliceRouteProposal.ProcessFireRouteProposal");
    public CustomizableNode _ProcessFSCRouteConfirmation=createCustomizableNode("Bellairs_stubonly.Steps.BaseStub.FSCRouteConfirmation_PoliceRouteParameters_FireRouteParameters.ProcessFSCRouteConfirmation");

    public void link(){
        linkNodesToNextNodes();
        linkNodesToWorkflow();
        linkStartNodesToWorkflow();
    }

    public void linkNodesToNextNodes(){
        _AlertFSCNotCoordinated.addNextNode(_routesNotCoordinated);
        _AskForPoliceRoute.addNextNode(_PoliceRouteProposal);
        _PoliceRouteProposal.addNextNode(_aspectMarker117643,"1");
        _AskForFireRoute.addNextNode(_FireRouteProposal);
        _FireRouteProposal.addNextNode(_aspectMarker117641,"1");
        _OrFork117115.addNextNode("else",_ProposedRouteToFSC);
        _OrFork117115.addNextNode("noRouteProposed",_AlertFSCNotCoordinated);
        _OrFork117600.addNextNode("else",_AndFork115939);
        _OrFork117600.addNextNode("RouteConfirmed",_routesCoordinated);
        _AndFork115939.addNextNode(_AskForPoliceRoute);
        _AndFork115939.addNextNode(_AskForFireRoute);
        _AndJoin115953.addNextNode(_OrFork117115);
        _aspectMarker117641.addNextNode("1",_ProcessFireRouteProposal);
        _aspectMarker117643.addNextNode("1",_ProcessPoliceRouteProposal);
        _ProposedRouteToFSC.addNextNode(_FSCRouteConfirmation);
        _FSCRouteConfirmation.addNextNode(_aspectMarker117655,"1");
        _aspectMarker117655.addNextNode("1",_ProcessFSCRouteConfirmation);
        _ProcessPoliceRouteProposal.addNextNode(_AndJoin115953);
        _ProcessFireRouteProposal.addNextNode(_AndJoin115953);
        _ProcessFSCRouteConfirmation.addNextNode(_OrFork117600);
    }

    public void linkNodesToWorkflow(){
        workflow.addNode(_AlertFSCNotCoordinated);
        workflow.addNode(_routesNotCoordinated);
        workflow.addNode(_AskForPoliceRoute);
        workflow.addNode(_PoliceRouteProposal);
        workflow.addNode(_routesCoordinated);
        workflow.addNode(_AskForFireRoute);
        workflow.addNode(_FireRouteProposal);
        workflow.addNode(_OrFork117115);
        workflow.addNode(_OrFork117600);
        workflow.addNode(_AndFork115939);
        workflow.addNode(_AndJoin115953);
        workflow.addNode(_aspectMarker117641);
        workflow.addNode(_aspectMarker117643);
        workflow.addNode(_ProposedRouteToFSC);
        workflow.addNode(_FSCRouteConfirmation);
        workflow.addNode(_aspectMarker117655);
        workflow.addNode(_ProcessPoliceRouteProposal);
        workflow.addNode(_ProcessFireRouteProposal);
        workflow.addNode(_ProcessFSCRouteConfirmation);
    }

    public void linkStartNodesToWorkflow(){
    }

    public void bind(Bellairs_stubonly.Instantiators.Authentication.AuthenticationWorkflowInstantiator p_Authentication){

        Binding l_aspectMarker117641_PluginBinding=new Binding(p_Authentication);
        _aspectMarker117641.addBinding(l_aspectMarker117641_PluginBinding);
        _aspectMarker117641.addInBinding(l_aspectMarker117641_PluginBinding,"1",p_Authentication._checkAuthentication);
        _aspectMarker117641.addOutBinding(l_aspectMarker117641_PluginBinding,p_Authentication._authenticated,"1");

        Binding l_aspectMarker117643_PluginBinding=new Binding(p_Authentication);
        _aspectMarker117643.addBinding(l_aspectMarker117643_PluginBinding);
        _aspectMarker117643.addInBinding(l_aspectMarker117643_PluginBinding,"1",p_Authentication._checkAuthentication);
        _aspectMarker117643.addOutBinding(l_aspectMarker117643_PluginBinding,p_Authentication._authenticated,"1");

        Binding l_aspectMarker117655_PluginBinding=new Binding(p_Authentication);
        _aspectMarker117655.addBinding(l_aspectMarker117655_PluginBinding);
        _aspectMarker117655.addInBinding(l_aspectMarker117655_PluginBinding,"1",p_Authentication._checkAuthentication);
        _aspectMarker117655.addOutBinding(l_aspectMarker117655_PluginBinding,p_Authentication._authenticated,"1");
    }
}
