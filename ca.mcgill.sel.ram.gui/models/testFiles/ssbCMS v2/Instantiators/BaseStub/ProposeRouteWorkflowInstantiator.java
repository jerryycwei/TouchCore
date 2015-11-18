package ssbCMS.Instantiators.BaseStub;

import ram.reactiveworkflow.*;
import ram.workflow.*;

public class ProposeRouteWorkflowInstantiator extends WorkflowInstantiator{
    public Workflow workflow=new Workflow();
    public CustomizableNode _AlertFSCNotCoordinated=createCustomizableNode("ssbCMS.Steps.BaseStub.FireRouteProposal_PoliceRouteProposal.AlertFSCNotCoordinated");
    public EndNode _RoutesNotCoordinated=new EndNode();
    public CustomizableNode _AskForPoliceRoute=createCustomizableNode("ssbCMS.Steps.BaseStub.FSCRouteConfirmation_PoliceRouteParameters_FireRouteParameters.AskForPoliceRoute");
    public InputNode _PoliceRouteProposal=new InputNode();
    public EndNode _RoutesCoordinated=new EndNode();
    public CustomizableNode _AskForFireRoute=createCustomizableNode("ssbCMS.Steps.BaseStub.FSCRouteConfirmation_PoliceRouteParameters_FireRouteParameters.AskForFireRoute");
    public InputNode _FireRouteProposal=new InputNode();
    public ConditionalExecutionNode _OrFork117115=new ConditionalExecutionNode();
    public ConditionalExecutionNode _OrFork117600=new ConditionalExecutionNode();
    public ParallelExecutionNode _AndFork115939=new ParallelExecutionNode();
    public SynchronizationNode _AndJoin115953=new SynchronizationNode();
    public StubNode _AspectMarker117641=new StubNode();
    public StubNode _AspectMarker117643=new StubNode();
    public CustomizableNode _ProposedRouteToFSC=createCustomizableNode("ssbCMS.Steps.BaseStub.FireRouteProposal_PoliceRouteProposal.ProposedRouteToFSC");
    public InputNode _FSCRouteConfirmation=new InputNode();
    public StubNode _AspectMarker117655=new StubNode();
    public CustomizableNode _ProcessPoliceRouteProposal=createCustomizableNode("ssbCMS.Steps.BaseStub.FireRouteProposal_PoliceRouteProposal.ProcessPoliceRouteProposal");
    public CustomizableNode _ProcessFireRouteProposal=createCustomizableNode("ssbCMS.Steps.BaseStub.FireRouteProposal_PoliceRouteProposal.ProcessFireRouteProposal");
    public CustomizableNode _ProcessFSCRouteConfirmation=createCustomizableNode("ssbCMS.Steps.BaseStub.FSCRouteConfirmation_PoliceRouteParameters_FireRouteParameters.ProcessFSCRouteConfirmation");

    public void link(){
        linkNodesToNextNodes();
        linkNodesToWorkflow();
        linkStartNodesToWorkflow();
    }

    public void linkNodesToNextNodes(){
        _AlertFSCNotCoordinated.addNextNode(_RoutesNotCoordinated);
        _AskForPoliceRoute.addNextNode(_PoliceRouteProposal);
        _PoliceRouteProposal.addNextNode(_AspectMarker117643,"1");
        _AskForFireRoute.addNextNode(_FireRouteProposal);
        _FireRouteProposal.addNextNode(_AspectMarker117641,"1");
        _OrFork117115.addNextNode("else",_ProposedRouteToFSC);
        _OrFork117115.addNextNode("noRouteProposed",_AlertFSCNotCoordinated);
        _OrFork117600.addNextNode("else",_AndFork115939);
        _OrFork117600.addNextNode("RouteConfirmed",_RoutesCoordinated);
        _AndFork115939.addNextNode(_AskForPoliceRoute);
        _AndFork115939.addNextNode(_AskForFireRoute);
        _AndJoin115953.addNextNode(_OrFork117115);
        _AspectMarker117641.addNextNode("1",_ProcessFireRouteProposal);
        _AspectMarker117643.addNextNode("1",_ProcessPoliceRouteProposal);
        _ProposedRouteToFSC.addNextNode(_FSCRouteConfirmation);
        _FSCRouteConfirmation.addNextNode(_AspectMarker117655,"1");
        _AspectMarker117655.addNextNode("1",_ProcessFSCRouteConfirmation);
        _ProcessPoliceRouteProposal.addNextNode(_AndJoin115953);
        _ProcessFireRouteProposal.addNextNode(_AndJoin115953);
        _ProcessFSCRouteConfirmation.addNextNode(_OrFork117600);
    }

    public void linkNodesToWorkflow(){
        workflow.addNode(_AlertFSCNotCoordinated);
        workflow.addNode(_RoutesNotCoordinated);
        workflow.addNode(_AskForPoliceRoute);
        workflow.addNode(_PoliceRouteProposal);
        workflow.addNode(_RoutesCoordinated);
        workflow.addNode(_AskForFireRoute);
        workflow.addNode(_FireRouteProposal);
        workflow.addNode(_OrFork117115);
        workflow.addNode(_OrFork117600);
        workflow.addNode(_AndFork115939);
        workflow.addNode(_AndJoin115953);
        workflow.addNode(_AspectMarker117641);
        workflow.addNode(_AspectMarker117643);
        workflow.addNode(_ProposedRouteToFSC);
        workflow.addNode(_FSCRouteConfirmation);
        workflow.addNode(_AspectMarker117655);
        workflow.addNode(_ProcessPoliceRouteProposal);
        workflow.addNode(_ProcessFireRouteProposal);
        workflow.addNode(_ProcessFSCRouteConfirmation);
    }

    public void linkStartNodesToWorkflow(){
    }

    public void bind(ssbCMS.Instantiators.Authentication.AuthenticationWorkflowInstantiator p_Authentication){

        Binding l_AspectMarker117641_PluginBinding=new Binding(p_Authentication.workflow);
        _AspectMarker117641.addBinding(l_AspectMarker117641_PluginBinding);
        _AspectMarker117641.addInBinding(l_AspectMarker117641_PluginBinding,"1",p_Authentication._CheckAuthentication);
        _AspectMarker117641.addOutBinding(l_AspectMarker117641_PluginBinding,p_Authentication._Authenticated,"1");

        Binding l_AspectMarker117643_PluginBinding=new Binding(p_Authentication.workflow);
        _AspectMarker117643.addBinding(l_AspectMarker117643_PluginBinding);
        _AspectMarker117643.addInBinding(l_AspectMarker117643_PluginBinding,"1",p_Authentication._CheckAuthentication);
        _AspectMarker117643.addOutBinding(l_AspectMarker117643_PluginBinding,p_Authentication._Authenticated,"1");

        Binding l_AspectMarker117655_PluginBinding=new Binding(p_Authentication.workflow);
        _AspectMarker117655.addBinding(l_AspectMarker117655_PluginBinding);
        _AspectMarker117655.addInBinding(l_AspectMarker117655_PluginBinding,"1",p_Authentication._CheckAuthentication);
        _AspectMarker117655.addOutBinding(l_AspectMarker117655_PluginBinding,p_Authentication._Authenticated,"1");
    }
}
