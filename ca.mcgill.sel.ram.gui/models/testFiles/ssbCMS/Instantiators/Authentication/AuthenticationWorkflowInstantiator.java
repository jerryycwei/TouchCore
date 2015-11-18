package Bellairs_stubonly.Instantiators.Authentication;

import ram.reactiveworkflow.*;
import ram.workflow.*;

public class AuthenticationWorkflowInstantiator extends WorkflowInstantiator{
    public Workflow workflow=new Workflow();
    public ConditionalExecutionNode _CheckAuthenticated=new ConditionalExecutionNode();
    public ConditionalExecutionNode _CheckBlock=new ConditionalExecutionNode();
    public EndNode _authenticated=new EndNode();
    public ConditionalExecutionNode _CheckAuthenticatedAndIdle=new ConditionalExecutionNode();
    public EndNode _fail=new EndNode();
    public ConditionalExecutionNode _CheckTrials=new ConditionalExecutionNode();
    public CustomizableNode _authenticate=createCustomizableNode("Bellairs_stubonly.Steps.Authentication.EnterUserCredentials.authenticate");
    public CustomizableNode _block=createCustomizableNode("Bellairs_stubonly.Steps.Authentication.EnterUserCredentials.block");
    public CustomizableNode _checkAuthentication=createCustomizableNode("Bellairs_stubonly.Steps.Authentication.EnterUserCredentials.checkAuthentication");
    public CustomizableNode _AskEnterCredentials=createCustomizableNode("Bellairs_stubonly.Steps.Authentication.EnterUserCredentials.AskEnterCredentials");
    public InputNode _EnterUserCredentials=new InputNode();

    public void link(){
        linkNodesToNextNodes();
        linkNodesToWorkflow();
        linkStartNodesToWorkflow();
    }

    public void linkNodesToNextNodes(){
        _CheckAuthenticated.addNextNode("fail",_CheckTrials);
        _CheckAuthenticated.addNextNode("success",_authenticated);
        _CheckBlock.addNextNode("blocked",_fail);
        _CheckBlock.addNextNode("else",_CheckAuthenticatedAndIdle);
        _CheckAuthenticatedAndIdle.addNextNode("else",_AskEnterCredentials);
        _CheckAuthenticatedAndIdle.addNextNode("alreadyAuthenticated_AND_idleLessThan30min",_authenticated);
        _CheckTrials.addNextNode("lessThan4Times",_AskEnterCredentials);
        _CheckTrials.addNextNode("moreThan3Times",_block);
        _authenticate.addNextNode(_CheckAuthenticated);
        _block.addNextNode(_fail);
        _checkAuthentication.addNextNode(_CheckBlock);
        _AskEnterCredentials.addNextNode(_EnterUserCredentials);
        _EnterUserCredentials.addNextNode(_authenticate);
    }

    public void linkNodesToWorkflow(){
        workflow.addNode(_CheckAuthenticated);
        workflow.addNode(_CheckBlock);
        workflow.addNode(_authenticated);
        workflow.addNode(_CheckAuthenticatedAndIdle);
        workflow.addNode(_fail);
        workflow.addNode(_CheckTrials);
        workflow.addNode(_authenticate);
        workflow.addNode(_block);
        workflow.addNode(_checkAuthentication);
        workflow.addNode(_AskEnterCredentials);
        workflow.addNode(_EnterUserCredentials);
    }

    public void linkStartNodesToWorkflow(){
    }

    public void bind(){
    }
}
