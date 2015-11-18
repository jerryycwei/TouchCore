package Bellairs_stubonly;

import ram.reactiveworkflow.ReactiveWorkflowSystem;
import Bellairs_stubonly.Instantiators.Authentication.AuthenticationWorkflowInstantiator;
import Bellairs_stubonly.Instantiators.BaseStub.DevelopRoutePlanComplexStubWorkflowInstantiator;
import Bellairs_stubonly.Instantiators.BaseStub.ProposeRouteWorkflowInstantiator;

public class Program{

    public static void main(String[] args){
        ReactiveWorkflowSystem reactiveWorkflowSystem=new ReactiveWorkflowSystem();

        AuthenticationWorkflowInstantiator l_AuthenticationWorkflowInstantiator=new AuthenticationWorkflowInstantiator();
        DevelopRoutePlanComplexStubWorkflowInstantiator l_DevelopRoutePlanComplexStubWorkflowInstantiator=new DevelopRoutePlanComplexStubWorkflowInstantiator();
        ProposeRouteWorkflowInstantiator l_ProposeRouteWorkflowInstantiator=new ProposeRouteWorkflowInstantiator();

        l_AuthenticationWorkflowInstantiator.link();
        l_AuthenticationWorkflowInstantiator.bind();
        reactiveWorkflowSystem.addWorkflow(l_AuthenticationWorkflowInstantiator.workflow);

        l_DevelopRoutePlanComplexStubWorkflowInstantiator.link();
        l_DevelopRoutePlanComplexStubWorkflowInstantiator.bind(l_ProposeRouteWorkflowInstantiator,l_AuthenticationWorkflowInstantiator);
        reactiveWorkflowSystem.addWorkflow(l_DevelopRoutePlanComplexStubWorkflowInstantiator.workflow);

        l_ProposeRouteWorkflowInstantiator.link();
        l_ProposeRouteWorkflowInstantiator.bind(l_AuthenticationWorkflowInstantiator);
        reactiveWorkflowSystem.addWorkflow(l_ProposeRouteWorkflowInstantiator.workflow);

        reactiveWorkflowSystem.start();
    }
}
