package ca.mcgill.sel.ram.weaver.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EObject;

import ca.mcgill.sel.ram.Classifier;
import ca.mcgill.sel.ram.NamedElement;
import ca.mcgill.sel.ram.StructuralView;

/**
 * Stores information on what element from the lower-level aspect (from element)
 * is woven to which element in the higher-level aspect (to element).
 * The weaving information can be considered a map, that maps from lower-level
 * to higher-level and higher-level to lower-level elements.
 * The information can be retrieved either using the from or to element.
 * Since a lower-level element can be mapped to more than one element
 * in the higher-level, getting the corresponding to element using the from element
 * retrieves a list of elements.
 * 
 * @author mschoettle
 */
public class WeavingInformation implements Cloneable {

    private Map<EObject, List<EObject>> fromTosMap;

    /**
     * Creates a new weaving information with no initial information.
     */
    public WeavingInformation() {
        fromTosMap = new HashMap<EObject, List<EObject>>();
    }

/**
     * Adds the connection between the from and the to element. This means that
     * the from element in the lower-level aspect is now the to element in the
     * higher-level aspect. This either means it is a new element, or an already
     * existing element in the higher-level aspect.
     * When calling, make sure that both elements are of the same type.
     * The only exception is, when the super-type of both is
     * Classifier (e.g., a {@link ca.mcgill.sel.ram.Class} is mapped to 
     * an {@link ca.mcgill.sel.ram.ImplementationClass     
     * If a to element is already contained, it will not be added again (the list is unique).
     * 
     * @param fromElement the element from the lower-level aspect
     * @param toElement the element from the higher-level aspect
     * @throws IllegalArgumentException if the types of the from and to element are different
     */
    public void add(EObject fromElement, EObject toElement) {
        if (!toElement.eClass().isInstance(fromElement)) {
            // Mapping a class to an implementation class is fine, though,
            // e.g. Key (Class) is mapped to RString (ImplementationClass).
            if (!(toElement instanceof Classifier && fromElement instanceof Classifier)) {
                throw new IllegalArgumentException("from and to element have to be of the same type");
            }
        }

        List<EObject> toElements = fromTosMap.get(fromElement);

        if (toElements == null) {
            toElements = new ArrayList<EObject>();
            fromTosMap.put(fromElement, toElements);
        }
        // make list unique
        if (!toElements.contains(toElement)) {
            toElements.add(toElement);
        }
    }

    /**
     * Returns all from elements of the lower-level aspect that were woven
     * into the given to element of the higher-level aspect.
     * 
     * @param toElement the element from the higher-level aspect
     * @param <T> the type of the mapped object
     * @return the from elements located in the lower-level aspect
     */
    public <T extends EObject> List<T> getFromElements(T toElement) {
        List<T> fromElements = new ArrayList<T>();

        for (Entry<EObject, List<EObject>> entry : fromTosMap.entrySet()) {
            if (entry.getValue().contains(toElement)) {
                @SuppressWarnings("unchecked")
                T fromElement = (T) entry.getKey();

                if (!fromElements.contains(fromElement)) {
                    fromElements.add(fromElement);
                }
            }
        }

        return fromElements;
    }

    /**
     * Returns a list of to elements of the higher-level aspect, the given from element
     * was woven into.
     * 
     * @param fromElement the from element located in the lower-level aspect
     * @param <T> the type of the mapped object
     * @return the list of to elements of the higher-level aspect
     */
    public <T extends EObject> List<T> getToElements(T fromElement) {
        @SuppressWarnings("unchecked")
        List<T> toElements = (List<T>) fromTosMap.get(fromElement);

        return toElements;
    }

    /**
     * Returns all the elements something was woven to (to elements).
     * 
     * @return the entire list of to elements of the higher-level aspect
     */
    public List<EObject> getToElements() {
        List<EObject> tosList = new ArrayList<EObject>();

        for (Entry<EObject, List<EObject>> entry : fromTosMap.entrySet()) {
            tosList.addAll(entry.getValue());
        }

        return tosList;
    }

    /**
     * Returns the first to element of the list of elements the given from element
     * was woven into.
     * <b>Note:</b> Make sure to only call this if you are sure, that there is
     * only one element.
     * 
     * @see #getToElements(EObject)
     * @param fromElement the from element located in the lower-level aspect
     * @param <T> the type of the mapped object
     * @return the first element of the list of to elements
     */
    public <T extends EObject> T getFirstToElement(T fromElement) {
        List<T> toElements = getToElements(fromElement);

        // Make sure there is at least one element.
        if (toElements == null || toElements.size() < 1) {
            return null;
        }

        if (toElements.size() > 1) {
            System.err.println("Warning: there is more than one element for: " + fromElement);
        }

        return toElements.get(0);
    }

    /**
     * Returns whether the given from element was woven to an element in the
     * higher-level aspect.
     * 
     * @param fromElement the from element located in the lower-level aspect
     * @return true, if the from element was woven to (at least one) an element in the higher-level aspect,
     *         false otherwise
     */
    public boolean wasWoven(EObject fromElement) {
        return fromTosMap.containsKey(fromElement);
    }

    /**
     * Removes all weaving information that is stored.
     */
    public void clear() {
        fromTosMap.clear();
    }

    @Override
    public WeavingInformation clone() {
        WeavingInformation clone = new WeavingInformation();

        clone.fromTosMap = new HashMap<EObject, List<EObject>>(fromTosMap);

        return clone;
    }

    /**
     * Merges the information from the given {@link WeavingInformation} into this.
     * This is equivalent to copying all information stored in the given {@link WeavingInformation} into this one.
     * 
     * @param weavingInformation the {@link WeavingInformation} to be merged in with this one
     */
    public void merge(WeavingInformation weavingInformation) {
        for (EObject fromElement : weavingInformation.fromTosMap.keySet()) {
            for (EObject toElement : weavingInformation.getToElements(fromElement)) {
                this.add(fromElement, toElement);
            }
        }
    }

    /**
     * Prints the weaving information in the form of "from element -> to elements".
     * The information also includes the type of the element and it's container.
     */
    public void print() {
        System.out.println("printing weaving information\n");

        for (Entry<EObject, List<EObject>> entry : fromTosMap.entrySet()) {
            print(entry.getKey());

            System.out.print(" -> ");

            for (EObject value : entry.getValue()) {
                print(value);
                System.out.print(", ");
            }

            System.out.println();
        }
    }

    /**
     * Prints the information of the given object.
     * 
     * @param object the {@link EObject} to print
     */
    private void print(EObject object) {
        if (object instanceof NamedElement) {
            System.out.print(((NamedElement) object).getName());
        } else {
            System.out.print(object);
        }

        System.out.print(" [" + object.eClass().getName() + "]");

        EObject container = object.eContainer();

        if (!(container instanceof StructuralView) && container instanceof NamedElement) {
            System.out.print(" (");
            print(container);
            System.out.print(")");
        }
    }

}
