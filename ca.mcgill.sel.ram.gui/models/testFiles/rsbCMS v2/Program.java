package rsbCMS;

import ram.reactiveworkflow.ReactiveWorkflowSystem;
import rsbCMS.Instantiators.Authentication.AuthenticationWorkflowInstantiator;
import rsbCMS.Instantiators.Base.DevelopRoutePlanSimpleWorkflowInstantiator;

public class Program{

    public static void main(String[] args){
        ReactiveWorkflowSystem reactiveWorkflowSystem=new ReactiveWorkflowSystem();

        AuthenticationWorkflowInstantiator l_AuthenticationWorkflowInstantiator=new AuthenticationWorkflowInstantiator();
        DevelopRoutePlanSimpleWorkflowInstantiator l_DevelopRoutePlanSimpleWorkflowInstantiator=new DevelopRoutePlanSimpleWorkflowInstantiator();

        l_AuthenticationWorkflowInstantiator.link();
        l_AuthenticationWorkflowInstantiator.bind();
        reactiveWorkflowSystem.addWorkflow(l_AuthenticationWorkflowInstantiator.workflow);

        l_DevelopRoutePlanSimpleWorkflowInstantiator.link();
        l_DevelopRoutePlanSimpleWorkflowInstantiator.bind(l_AuthenticationWorkflowInstantiator);
        reactiveWorkflowSystem.addWorkflow(l_DevelopRoutePlanSimpleWorkflowInstantiator.workflow);

        reactiveWorkflowSystem.start();
    }
}
