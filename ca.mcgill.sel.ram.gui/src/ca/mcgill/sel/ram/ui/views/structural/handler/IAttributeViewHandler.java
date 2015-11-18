package ca.mcgill.sel.ram.ui.views.structural.handler;

import org.mt4j.input.inputProcessors.IGestureEventListener;

import ca.mcgill.sel.ram.Attribute;
import ca.mcgill.sel.ram.controller.ClassController;
import ca.mcgill.sel.ram.ui.views.structural.AttributeView;

/**
 * This interface is implemented by something that can handle events for an {@link AttributeView}.
 * 
 * @author mschoettle
 */
public interface IAttributeViewHandler extends IGestureEventListener {

    /**
     * Handles the removal of an attribute.
     * 
     * @param attributeView
     *            the affected {@link AttributeView}
     */
    void removeAttribute(AttributeView attributeView);

    /**
     * Handles the set of attribute to static.
     * 
     * @param controller - related {@link ClassController}
     * @param attribute - the affected {@link AttributeView}
     */
    void setAttributeStatic(ClassController controller, Attribute attribute);

    /**
     * Handles the generation of getter.
     * 
     * @param controller - related {@link ClassController}
     * @param attribute - the affected {@link AttributeView}
     */
    void generateGetter(ClassController controller, Attribute attribute);

    /**
     * Handles the generation of setter.
     * 
     * @param controller - related {@link ClassController}
     * @param attribute - the affected {@link AttributeView}
     */
    void generateSetter(ClassController controller, Attribute attribute);
}
