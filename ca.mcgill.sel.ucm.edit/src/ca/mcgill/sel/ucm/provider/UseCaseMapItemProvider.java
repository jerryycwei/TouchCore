/**
 */
package ca.mcgill.sel.ucm.provider;


import ca.mcgill.sel.core.CoreFactory;
import ca.mcgill.sel.core.CorePackage;
import ca.mcgill.sel.core.provider.COREModelItemProvider;

import ca.mcgill.sel.ucm.UCMFactory;
import ca.mcgill.sel.ucm.UCMPackage;
import ca.mcgill.sel.ucm.UseCaseMap;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link ca.mcgill.sel.ucm.UseCaseMap} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class UseCaseMapItemProvider extends COREModelItemProvider {
    /**
     * This constructs an instance from a factory and a notifier.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public UseCaseMapItemProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    /**
     * This returns the property descriptors for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
        if (itemPropertyDescriptors == null) {
            super.getPropertyDescriptors(object);

        }
        return itemPropertyDescriptors;
    }

    /**
     * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
     * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
     * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
        if (childrenFeatures == null) {
            super.getChildrenFeatures(object);
            childrenFeatures.add(UCMPackage.Literals.USE_CASE_MAP__CONNECTIONS);
            childrenFeatures.add(UCMPackage.Literals.USE_CASE_MAP__NODES);
            childrenFeatures.add(UCMPackage.Literals.USE_CASE_MAP__RESPONSIBILITIES);
            childrenFeatures.add(UCMPackage.Literals.USE_CASE_MAP__COMPONENTS);
            childrenFeatures.add(UCMPackage.Literals.USE_CASE_MAP__COMPONENT_REFERENCES);
            childrenFeatures.add(UCMPackage.Literals.USE_CASE_MAP__LAYOUT_MAPS);
        }
        return childrenFeatures;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EStructuralFeature getChildFeature(Object object, Object child) {
        // Check the type of the specified child object and return the proper feature to use for
        // adding (see {@link AddCommand}) it as a child.

        return super.getChildFeature(object, child);
    }

    /**
     * This returns UseCaseMap.gif.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object getImage(Object object) {
        return overlayImage(object, getResourceLocator().getImage("full/obj16/UseCaseMap"));
    }

    /**
     * This returns the label text for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((UseCaseMap)object).getName();
        return label == null || label.length() == 0 ?
            getString("_UI_UseCaseMap_type") :
            getString("_UI_UseCaseMap_type") + " " + label;
    }
    

    /**
     * This handles model notifications by calling {@link #updateChildren} to update any cached
     * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void notifyChanged(Notification notification) {
        updateChildren(notification);

        switch (notification.getFeatureID(UseCaseMap.class)) {
            case UCMPackage.USE_CASE_MAP__CONNECTIONS:
            case UCMPackage.USE_CASE_MAP__NODES:
            case UCMPackage.USE_CASE_MAP__RESPONSIBILITIES:
            case UCMPackage.USE_CASE_MAP__COMPONENTS:
            case UCMPackage.USE_CASE_MAP__COMPONENT_REFERENCES:
            case UCMPackage.USE_CASE_MAP__LAYOUT_MAPS:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
                return;
        }
        super.notifyChanged(notification);
    }

    /**
     * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
     * that can be created under this object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
        super.collectNewChildDescriptors(newChildDescriptors, object);

        newChildDescriptors.add
            (createChildParameter
                (UCMPackage.Literals.USE_CASE_MAP__CONNECTIONS,
                 UCMFactory.eINSTANCE.createNodeConnection()));

        newChildDescriptors.add
            (createChildParameter
                (UCMPackage.Literals.USE_CASE_MAP__NODES,
                 UCMFactory.eINSTANCE.createPathNode()));

        newChildDescriptors.add
            (createChildParameter
                (UCMPackage.Literals.USE_CASE_MAP__NODES,
                 UCMFactory.eINSTANCE.createAndFork()));

        newChildDescriptors.add
            (createChildParameter
                (UCMPackage.Literals.USE_CASE_MAP__NODES,
                 UCMFactory.eINSTANCE.createAndJoin()));

        newChildDescriptors.add
            (createChildParameter
                (UCMPackage.Literals.USE_CASE_MAP__NODES,
                 UCMFactory.eINSTANCE.createOrFork()));

        newChildDescriptors.add
            (createChildParameter
                (UCMPackage.Literals.USE_CASE_MAP__NODES,
                 UCMFactory.eINSTANCE.createOrJoin()));

        newChildDescriptors.add
            (createChildParameter
                (UCMPackage.Literals.USE_CASE_MAP__NODES,
                 UCMFactory.eINSTANCE.createEmptyPoint()));

        newChildDescriptors.add
            (createChildParameter
                (UCMPackage.Literals.USE_CASE_MAP__NODES,
                 UCMFactory.eINSTANCE.createResponsibilityRef()));

        newChildDescriptors.add
            (createChildParameter
                (UCMPackage.Literals.USE_CASE_MAP__NODES,
                 UCMFactory.eINSTANCE.createStartPoint()));

        newChildDescriptors.add
            (createChildParameter
                (UCMPackage.Literals.USE_CASE_MAP__NODES,
                 UCMFactory.eINSTANCE.createEndPoint()));

        newChildDescriptors.add
            (createChildParameter
                (UCMPackage.Literals.USE_CASE_MAP__RESPONSIBILITIES,
                 UCMFactory.eINSTANCE.createResponsibility()));

        newChildDescriptors.add
            (createChildParameter
                (UCMPackage.Literals.USE_CASE_MAP__COMPONENTS,
                 UCMFactory.eINSTANCE.createComponent()));

        newChildDescriptors.add
            (createChildParameter
                (UCMPackage.Literals.USE_CASE_MAP__COMPONENT_REFERENCES,
                 UCMFactory.eINSTANCE.createComponentReference()));

        newChildDescriptors.add
            (createChildParameter
                (UCMPackage.Literals.USE_CASE_MAP__LAYOUT_MAPS,
                 CoreFactory.eINSTANCE.create(CorePackage.Literals.LAYOUT_MAP)));
    }

    /**
     * Return the resource locator for this item provider's resources.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public ResourceLocator getResourceLocator() {
        return UCMEditPlugin.INSTANCE;
    }

}
