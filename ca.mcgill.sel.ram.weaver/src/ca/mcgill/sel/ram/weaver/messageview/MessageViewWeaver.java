package ca.mcgill.sel.ram.weaver.messageview;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.EqualityHelper;

import ca.mcgill.sel.ram.AbstractMessageView;
import ca.mcgill.sel.ram.Aspect;
import ca.mcgill.sel.ram.AspectMessageView;
import ca.mcgill.sel.ram.CombinedFragment;
import ca.mcgill.sel.ram.FragmentContainer;
import ca.mcgill.sel.ram.Gate;
import ca.mcgill.sel.ram.Interaction;
import ca.mcgill.sel.ram.InteractionFragment;
import ca.mcgill.sel.ram.InteractionOperand;
import ca.mcgill.sel.ram.Lifeline;
import ca.mcgill.sel.ram.Message;
import ca.mcgill.sel.ram.MessageEnd;
import ca.mcgill.sel.ram.MessageOccurrenceSpecification;
import ca.mcgill.sel.ram.MessageSort;
import ca.mcgill.sel.ram.MessageView;
import ca.mcgill.sel.ram.MessageViewReference;
import ca.mcgill.sel.ram.Operation;
import ca.mcgill.sel.ram.OriginalBehaviorExecution;
import ca.mcgill.sel.ram.RamFactory;
import ca.mcgill.sel.ram.RamPackage;
import ca.mcgill.sel.ram.Reference;
import ca.mcgill.sel.ram.TemporaryProperty;
import ca.mcgill.sel.ram.util.RAMModelUtil;
import ca.mcgill.sel.ram.weaver.util.ReferenceUpdater;
import ca.mcgill.sel.ram.weaver.util.WeavingInformation;

/**
 * The Message View Weaver weaves message views from a lower-level aspect (<tt>dependee</tt>) into
 * the higher-level aspect (<tt>base</tt>). The weaver supports two modes:
 * <ol>
 * <li>
 * <b>Copy Message Views:</b> Copies all message views from the <tt>dependee</tt> to the <tt>base</tt> and updates all
 * references to point to elements in the <tt>base</tt>.</li>
 * <li>
 * <b>Weave Message Views></b> Weaves all the {@link AspectMessageView}s a {@link MessageView} is affected by into that
 * message view.</li>
 * </ol>
 * 
 * @author mschoettle
 */
public class MessageViewWeaver {
    
    /**
     * Adds a copy of all message views of <tt>dependee</tt> into <tt>base</tt>.
     * Handles special cases, such as merging <tt>affected by</tt> if there is already a message view in the
     * <tt>base</tt> or the <tt>base</tt> uses a {@link MessageViewReference}.
     * Updates all references of all messages that point to an element in the <tt>dependee</tt>,
     * which was woven during the structural weaving process, by replacing it with a reference
     * to the woven element in the <tt>base</tt>.
     * 
     * @param base the higher-level aspect, where message views should be added to
     * @param dependee the lower-level aspect, where message views are copied from and added to <tt>base</tt>
     * @param weavingInformation the {@link WeavingInformation}, which contains information
     *            on which element in the structural view from the <tt>dependee</tt> was woven to
     *            which element in the <tt>base</tt> during the structural weaving process
     */
    public void copyMessageViews(Aspect base, Aspect dependee, WeavingInformation weavingInformation) {
        // Copy aspect message views first ...
        for (AspectMessageView aspectMessageView : RAMModelUtil
                .getMessageViewsOfType(dependee, AspectMessageView.class)) {
            AbstractMessageView copy = EcoreUtil.copy(aspectMessageView);
            base.getMessageViews().add(copy);
            
            // Keep track of aspect message views to update affected by of message views.
            weavingInformation.add(aspectMessageView, copy);
        }
        
        // Handle (normal) message views ...
        for (MessageView messageView : RAMModelUtil.getMessageViewsOfType(dependee, MessageView.class)) {
            List<MessageView> existingMessageViews =
                    getMessageViewsFor(weavingInformation.getToElements(messageView.getSpecifies()), base);
            
            // For all message views, which are already defined in base (higher-level aspect)
            // copy only the affected by information from the dependent (lower-level aspect).
            // This can happen if an aspect is used multiple times and the same classes are mapped.
            if (!existingMessageViews.isEmpty()) {
                for (MessageView existingMessageView : existingMessageViews) {
                    // TODO: mschoettle: Figure out if it has to be added to the end or the beginning of the list.
                    mergeAffectedBy(existingMessageView, messageView, true);
                }
            } else if (messageView.getSpecification() == null
                    && weavingInformation.wasWoven(messageView.getSpecifies())
                    && !existingMessageViews.isEmpty()) {
                // Partial methods have no specification and have to be mapped,
                // which means there will be two message views.
                // The one from the higher-level aspect has the specification
                // so just the affected by information has to be added to it.
                // However, only do this if there are existing message views. Otherwise
                // the message view from the lower-level will be lost including it's
                // affected by information.
                // NOTE: This means that the model is invalid, because there should
                // be a message view in the higher-level aspect. (print warning below)
                for (MessageView existingMessageView : existingMessageViews) {
                    mergeAffectedBy(existingMessageView, messageView, false);
                }
            } else {
                // Copy the message view ...
                
                // Handle special case or print warning for invalid model (see comment above).
                // TODO: mschoettle: Make sure the special case never happens with a specification.
                if (messageView.getSpecification() == null
                        && weavingInformation.wasWoven(messageView.getSpecifies())
                        && existingMessageViews.isEmpty()) {
                    // Handle a special case described in issue #63.
                    // If the "woven to" operation actually was "woven" from another operation,
                    // because the "from operation" was mapped to an operation from another aspect,
                    // special handling is required.
                    // In that case, a MessageViewReference should be used to reference
                    // the actual MessageView which defines the operation (if it exists).
                    // All then is required is to merge the affected by,
                    // in case the MessageView we are trying to copy here contains them.
                    Operation specifies = messageView.getSpecifies();
                    // The operation that "specifies" was mapped/woven to
                    Operation wovenTo = weavingInformation.getFirstToElement(specifies);
                    // The operation that "wovenTo" was originally mapped/woven from.
                    // In a normal scenario, this would be the same as "specifies".
                    // In the special case, this is another operation.
                    List<Operation> actualOperations = weavingInformation.getFromElements(wovenTo);
                    
                    for (Operation actualOperation : actualOperations) {
                        if (actualOperation != specifies) {
                            // Get the MessageView that was defined for the actual operation.
                            MessageView otherMessageView = RAMModelUtil.getMessageViewFor(dependee, actualOperation);
                            
                            if (otherMessageView != null) {
                                MessageViewReference messageViewReference =
                                        RamFactory.eINSTANCE.createMessageViewReference();
                                messageViewReference.setReferences(otherMessageView);
                                base.getMessageViews().add(messageViewReference);
                                
                                mergeAffectedBy(messageViewReference, messageView, true);
                                // Prevent from doing the rest of the loop below.
                                continue;
                            }
                        } else {
                            System.err.println("Warning: [MessageViewWeaver] Message view for "
                                    + messageView.getSpecifies().getName()
                                    + " empty, but no message view existent in higher-level aspect.");
                        }
                    }
                }
                
                MessageView copy = EcoreUtil.copy(messageView);
                base.getMessageViews().add(copy);
                
                // Check if there is a message view reference in the base
                // that references the message view in the dependee.
                // If so, copy the affected by information to the copy of the dependee message view.
                for (MessageViewReference messageViewReference : RAMModelUtil.getMessageViewsOfType(base,
                        MessageViewReference.class)) {
                    if (messageView == messageViewReference.getReferences()) {
                        mergeAffectedBy(copy, messageViewReference, true);
                        // Remove the message view reference, because it is not required anymore.
                        base.getMessageViews().remove(messageViewReference);
                    }
                }
            }
        }
        
        // MessageViewReferences from a lower-level aspect
        // that refer to another (lower-level) aspect need to be copied as well.
        for (MessageViewReference messageViewReference : RAMModelUtil.getMessageViewsOfType(dependee,
                MessageViewReference.class)) {
            MessageViewReference copy = EcoreUtil.copy(messageViewReference);
            base.getMessageViews().add(copy);
            
            weavingInformation.add(messageViewReference, copy);
        }
        
        // Update the references of all message views.
        // All message views have to be updated, not only the ones that were
        // newly added to the base, because existing message views in base
        // might reference elements from lower-level aspects, which are now
        // part of the base aspect.
        for (AbstractMessageView messageView : base.getMessageViews()) {
            ReferenceUpdater.getInstance().update(messageView, weavingInformation);
        }
    }
    
    /**
     * Weaves the {@link AspectMessageView}s each {@link MessageView} is affected by
     * into the message view. In case a message view has no specification, the basic
     * specification is created in order to allow weaving. The basic specification
     * consists of the initial call from the outside to the receiving lifeline.
     * 
     * @param base the {@link Aspect} containing the message views
     * @param weavingInformation the {@link WeavingInformation}, which contains information
     *            on which element in the structural view from the <tt>dependee</tt> was woven to
     *            which element in the <tt>base</tt> during <b>all</b> structural weaving processes
     */
    public void weaveMessageViews(Aspect base, WeavingInformation weavingInformation) {
        // Weave the advice of every "affected by" into the message view.
        for (MessageView messageView : RAMModelUtil.getMessageViewsOfType(base, MessageView.class)) {
            for (AspectMessageView aspectMessageView : messageView.getAffectedBy()) {
                // Workaround: In case of an empty message view create the specification
                // with the initial structure of the advice.
                if (messageView.getSpecification() == null) {
                    Interaction specification =
                            initializeInteraction(messageView.getSpecifies(), aspectMessageView.getAdvice());
                    messageView.setSpecification(specification);
                }
                
                weaveMessageView(messageView, aspectMessageView, weavingInformation);
            }
            
            // "affected by" can now be removed ...
            messageView.getAffectedBy().clear();
        }
        
        // Delete all aspect message views ...
        for (AspectMessageView aspectMessageView : RAMModelUtil.getMessageViewsOfType(base, AspectMessageView.class)) {
            // Delete any layout that is stored in this aspect of this message view.
            base.getLayout().getContainers().remove(aspectMessageView);
            base.getMessageViews().remove(aspectMessageView);
        }
    }
    
    /**
     * Weaves the advice of the {@link AspectMessageView} the given {@link MessageView} is affected by into the message
     * view.
     * The advice is placed depending on the position of the {@link OriginalBehaviorExecution} in the advice. Also takes
     * care of copying all required elements and making sure their references
     * are up-to-date.
     * 
     * @param messageView the {@link MessageView} the advice is woven into
     * @param aspectMessageView the {@link AspectMessageView} whose advice is woven into the message view
     * @param structuralWeavingInformation the {@link WeavingInformation}, which contains information
     *            on which element in the structural view from the <tt>dependee</tt> was woven to
     *            which element in the <tt>base</tt> during the structural weaving process
     */
    private void weaveMessageView(MessageView messageView, AspectMessageView aspectMessageView,
            WeavingInformation structuralWeavingInformation) {
        Interaction specification = messageView.getSpecification();
        Interaction advice = aspectMessageView.getAdvice();
        
        // Generally the weaving process doesn't require the weaving information from
        // the structural weaving.
        // However, currently the updateFragmentMessage(...) method requires it,
        // for the case when an AspectMessageView is used in more than one message view.
        
        // Create a copy of weaving information to have a fresh copy of the
        // structural weaving information for any call to this method.
        WeavingInformation weavingInformation = structuralWeavingInformation.clone();
        
        // Copy all fragments ...
        // Go through advice and copy all fragments to the base until original behavior execution is reached.
        // Then do the same after the original behavior execution.
        int nextIndex = 0;
        
        for (InteractionFragment oldFragment : advice.getFragments()) {
            // Skip the first occurrence since it is the receiving event of the call,
            // which already exists.
            if (nextIndex > 0) {
                /**
                 * Don't copy the last reply message from the advice.
                 */
                if (nextIndex == advice.getFragments().size() - 1 && isFragmentOfReplyMessage(oldFragment)) {
                    break;
                }
                
                InteractionFragment fragmentCopy = EcoreUtil.copy(oldFragment);
                
                specification.getFragments().add(nextIndex, fragmentCopy);
                
                // The copy does not have "covered" set and references the message that "fragment" references.
                // Therefore, update the references right away ...
                updateFragment(specification, oldFragment, fragmentCopy, weavingInformation);
            }
            
            nextIndex++;
        }

        updateOriginalBehaviourPosition(specification, specification, nextIndex);
    }
    
    /**
     * Recursively updates the position of the original behaviour based on the position of 
     * the {@link OriginalBehaviorExecution} fragment.
     * For instance, it could be located inside a combined fragment. 
     * The existing behaviour is moved to the position where that fragment is located.
     * 
     * @param specification the {@link Interaction} of the message view in question
     * @param container the {@link FragmentContainer} to search the original behaviour fragment in
     * @param originalBehaviourIndex the index where the original behaviour starts at
     */
    private void updateOriginalBehaviourPosition(Interaction specification, FragmentContainer container, 
                    int originalBehaviourIndex) {
        FragmentContainer originalBehaviourContainer = null;
        int originalBehaviourFragmentIndex = 0;
        
        for (InteractionFragment fragment : new ArrayList<InteractionFragment>(container.getFragments())) {
            if (fragment instanceof OriginalBehaviorExecution) {
                originalBehaviourContainer = container;
                originalBehaviourFragmentIndex = container.getFragments().indexOf(fragment);
                container.getFragments().remove(originalBehaviourFragmentIndex);
                // Make sure the fragment is removed from the lifeline's coveredBy.
                fragment.getCovered().clear();
                break;
            } else if (fragment instanceof CombinedFragment) {
                CombinedFragment combinedFragment = (CombinedFragment) fragment;
                for (InteractionOperand operand : combinedFragment.getOperands()) {
                    updateOriginalBehaviourPosition(specification, operand, originalBehaviourIndex);
                }
            }
        }
        
        // Move existing fragments into originalBehaviourContainer.
        if (originalBehaviourContainer != null) {            
            int currentIndex = originalBehaviourIndex;
            /**
             * If the original behaviour fragment is located in the specification, 
             * we need to adjust the index, because it was removed, which moves every fragment up by 1.
             */
            if (originalBehaviourContainer == specification) {
                currentIndex--;
            }
            
            /**
             * We don't want the last fragment to be moved if it is the reply message at the end.
             */
            int lastIndex = specification.getFragments().size() - 1;
            if (isFragmentOfReplyMessage(specification.getFragments().get(specification.getFragments().size() - 1))) {
                lastIndex--;
            }
            
            /**
             * First remove all the fragments, then add them at the end,
             * because in the case that they are moved (removed then added) within the same container,
             * the indexes would be incorrect.
             * 
             * Note: It is possible that this is done even though it is unnecessary,
             * i.e., when the original behaviour is already at the correct position.
             */
            List<InteractionFragment> fragments = new ArrayList<InteractionFragment>();
            
            for (int i = currentIndex; i <= lastIndex; i++) {
                InteractionFragment currentFragment = specification.getFragments().remove(currentIndex);
                fragments.add(currentFragment);
            }
            
            originalBehaviourContainer.getFragments().addAll(originalBehaviourFragmentIndex, fragments);
        }
    }
    
    /**
     * Returns whether the given fragment is part of a message and in particular a reply message (the send event).
     * 
     * @param fragment the fragment to check
     * @return true, whether the fragment is the send event of a reply message, false otherwise
     */
    private boolean isFragmentOfReplyMessage(InteractionFragment fragment) {
        if (fragment instanceof MessageOccurrenceSpecification) {
            MessageEnd messageEnd = (MessageEnd) fragment;
            
            return messageEnd.getMessage().getMessageSort() == MessageSort.REPLY 
                    && messageEnd.getMessage().getSendEvent() == messageEnd;
        }
        
        return false;
    }
    
    /**
     * Updates a specific cloned fragment depending on the original fragment and adds it to the
     * interaction. Also takes care of assigning the proper lifeline to the fragment,
     * which might include copying of a lifeline if it doesn't exist yet.
     * For {@link CombinedFragment}s, all containing fragments are updated
     * by recursively calling this method.
     * Furthermore, it makes sure that associated messages are created as well,
     * if the fragment is a {@link MessageEnd}.
     * 
     * @param specification the {@link Interaction} of the {@link MessageView}
     * @param oldFragment the old {@link InteractionFragment} that is part of the advice
     * @param fragmentCopy the copy of <tt>oldFragment</tt> that is added to the message view
     * @param weavingInformation the {@link WeavingInformation}, which contains information
     *            on which element in the structural view from the <tt>dependee</tt> was woven to
     *            which element in the <tt>base</tt> during the structural weaving process
     */
    private void updateFragment(Interaction specification, InteractionFragment oldFragment,
            InteractionFragment fragmentCopy, WeavingInformation weavingInformation) {
        weavingInformation.add(oldFragment, fragmentCopy);
        
        // Process lifelines: detect existing ones and clone them if not existent.
        for (Lifeline oldLifeline : oldFragment.getCovered()) {
            Lifeline lifelineCopy = weavingInformation.getFirstToElement(oldLifeline);
            
            // The lifeline wasn't woven yet, so create a copy and update the references.
            // If after the update, a matching lifeline is found in the base,
            // use the existing one instead.
            if (lifelineCopy == null) {
                // Copy and update the references according to the weaving information.
                // This will allow to check whether the lifeline exists in the base already.
                lifelineCopy = EcoreUtil.copy(oldLifeline);
                // Required to update the represents reference, because it might point to an
                // element that is part of the aspect message view.
                ReferenceUpdater.getInstance().update(lifelineCopy, weavingInformation);
                
                // Check if there is already a lifeline with the exact same information.
                Lifeline existingLifeline = searchExistingLifeline(specification, lifelineCopy);
                
                // Add it and store that information for other fragments, if it doesn't exist yet.
                if (existingLifeline == null) {
                    specification.getLifelines().add(lifelineCopy);
                    weavingInformation.add(oldLifeline, lifelineCopy);
                    
                    // References for metaclass lifelines are contained in the interaction.
                    // These need to be copied over in addition.
                    if (oldLifeline.getRepresents() instanceof Reference) {
                        Reference oldReference = (Reference) oldLifeline.getRepresents();
                        
                        if (oldReference.isStatic()
                                && oldReference.eContainingFeature() == RamPackage.Literals.INTERACTION__PROPERTIES) {
                            Reference referenceCopy = EcoreUtil.copy(oldReference);
                            specification.getProperties().add(referenceCopy);
                            lifelineCopy.setRepresents(referenceCopy);
                            weavingInformation.add(oldReference, referenceCopy);
                        }
                    }
                } else {
                    // Save for other fragments.
                    weavingInformation.add(oldLifeline, existingLifeline);
                    // Represents needs to be added, because it could refer to something that wasn't woven.
                    // For example, the initial lifeline has a Reference ("target") that is contained
                    // by the Interaction.
                    weavingInformation.add(oldLifeline.getRepresents(), existingLifeline.getRepresents());
                }
            }
            
            // Finally, we can update "covered" by just getting it from the weaving information.
            fragmentCopy.getCovered().add(weavingInformation.getFirstToElement(oldLifeline));
            
            // Merge local properties.
            // This has to occur after "covered" of the fragment copy was updated,
            // because "covered" is used inside findInitialMessage.
            // The reason merging local properties is done here is,
            // because CombinedFragments will first update all lifelines before doing anything else.
            // In case they are represented by local properties, this reference would never be updated.
            // (Unless we update the complete message view at the end, but this is not necessary)
            Message initialMessageAdvice = RAMModelUtil.findInitialMessage(oldFragment);
            Message initialMessage = RAMModelUtil.findInitialMessage(fragmentCopy);
            
            if (initialMessage != null && initialMessageAdvice != null) {
                for (TemporaryProperty oldProperty : initialMessageAdvice.getLocalProperties()) {
                    if (!weavingInformation.wasWoven(oldProperty)) {
                        TemporaryProperty propertyCopy = EcoreUtil.copy(oldProperty);
                        
                        initialMessage.getLocalProperties().add(propertyCopy);
                        weavingInformation.add(oldProperty, propertyCopy);
                    }
                }
            }
        }
        
        // Process combined fragments: Combined fragments are completely cloned,
        // but the fragments are not updated. Therefore, the fragments of all
        // operands have to be updated manually by recursively calling this method.
        if (oldFragment instanceof CombinedFragment) {
            CombinedFragment oldCombinedFragment = (CombinedFragment) oldFragment;
            CombinedFragment combinedFragmentCopy = (CombinedFragment) fragmentCopy;
            
            // To go from the old operand to the copied operand, the index is used,
            // because they are equal (in the sense that they have the same amount
            // of operands and are in the same order).
            for (int i = 0; i < oldCombinedFragment.getOperands().size(); i++) {
                InteractionOperand oldOperand = oldCombinedFragment.getOperands().get(i);
                InteractionOperand operandCopy = combinedFragmentCopy.getOperands().get(i);
                
                for (int j = 0; j < oldOperand.getFragments().size(); j++) {
                    InteractionFragment oldOperandFragment = oldOperand.getFragments().get(j);
                    InteractionFragment operandFragmentCopy = operandCopy.getFragments().get(j);
                    
                    // Recursively call this operation, because nested CombinedFragments are possible.
                    updateFragment(specification, oldOperandFragment, operandFragmentCopy, weavingInformation);
                }
            }
        }
        
        // Process message ends: The associated message has to be copied.
        if (oldFragment instanceof MessageEnd) {
            updateFragmentMessage(specification, (MessageEnd) oldFragment, (MessageEnd) fragmentCopy,
                    weavingInformation);
        }
        
        /**
         * Update the copy to make sure it contains the proper references.
         * For example, an AssignmentStatement needs to get the assignTo reference
         * updates, which depends on the merged local properties (see above).
         */
        ReferenceUpdater.getInstance().update(fragmentCopy, weavingInformation);
    }
    
    /**
     * Updates the associated {@link Message} for {@link MessageEnd}s. Creates
     * the message if it wasn't created yet and makes sure that the fragment is
     * associated with that message and vice versa.
     * 
     * @param specification the {@link Interaction} of the {@link MessageView}
     * @param oldFragment the old {@link MessageEnd} that is part of the advice
     * @param fragmentCopy the copy of <tt>oldFragment</tt> that is added to the message view
     * @param weavingInformation the {@link WeavingInformation}, which contains information
     *            on which element in the structural view from the <tt>dependee</tt> was woven to
     *            which element in the <tt>base</tt> during the structural weaving process
     */
    private void updateFragmentMessage(Interaction specification, MessageEnd oldFragment, MessageEnd fragmentCopy,
            WeavingInformation weavingInformation) {
        Message oldMessage = oldFragment.getMessage();
        
        // Check to see if the message was already copied for another fragment.
        Message message = weavingInformation.getFirstToElement(oldMessage);
        
        if (message == null) {
            // Clone the message ...
            message = EcoreUtil.copy(oldMessage);
            
            // See ReferenceUpdater line 181ff.
            // If it was woven (i.e., it wasn't updated), it is necessary to find
            // the actual operation.
            // A preliminary approach is to use the same as the one that specifies this MessageView,
            // because we assume that it is the reply message of the advice.
            if (weavingInformation.wasWoven(message.getSignature())) {
                Operation specifies = ((MessageView) specification.eContainer()).getSpecifies();
                List<Operation> wovenOperations = weavingInformation.getToElements(message.getSignature());
                
                // Make sure that it actually is part of the woven operations.
                if (wovenOperations.contains(specifies)) {
                    message.setSignature(specifies);
                } else {
                    System.err.println("Error: [MessageViewWeaver] Message signature ("
                            + message.getSignature().getName()
                            + ") cannot be updated in MessageViewWeaver.updateFragmentMessage(...)");
                }
            }
            
            // In case this is an outgoing message, the receive event is a Gate.
            // Because the gate is not copied anywhere else, do it here right away.
            if (message.getReceiveEvent() instanceof Gate) {
                Gate gateCopy = (Gate) EcoreUtil.copy(message.getReceiveEvent());
                gateCopy.setMessage(message);
                gateCopy.setName("out_" + message.getSignature().getName());
                message.setReceiveEvent(gateCopy);
                
                specification.getFormalGates().add(gateCopy);
            }
            
            specification.getMessages().add(message);
            weavingInformation.add(oldMessage, message);
        }
        
        // Updating is necessary, because the properties and events of the (copied) message
        // still point to the ones from the old message.
        ReferenceUpdater.getInstance().update(message, weavingInformation);
        fragmentCopy.setMessage(message);
    }
    
    /**
     * Checks, whether the given {@link Lifeline} is already existent in the
     * given {@link Interaction}. In order to check for matching lifelines, the {@link EqualityHelper} of EMF is used,
     * which compares the properties of
     * two objects. In case the lifeline is represented by a {@link Reference},
     * the equality check is done without the name. This is because, for example,
     * the names between the lifeline in the <tt>specification</tt> and the advice
     * can differ, but they refer to the same.
     * 
     * @param specification the {@link Interaction} of the {@link MessageView}
     * @param lifelineCopy the {@link Lifeline} to search for in <tt>specification</tt>
     * @return true, if there is already a {@link Lifeline} in the given {@link Interaction} that matches the given
     *         lifeline, false otherwise
     * @see EqualityHelper#equals(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
     */
    private Lifeline searchExistingLifeline(Interaction specification, Lifeline lifelineCopy) {
        EqualityHelper equalityHelper = new EqualityHelper();
        
        for (Lifeline lifeline : specification.getLifelines()) {
            // In case the lifeline represents is a Reference,
            // the properties might be the same, but the name might differ.
            // The equality check would consider them not equal, but we consider those to be equal.
            if (lifeline.getRepresents() instanceof Reference && lifelineCopy.getRepresents() instanceof Reference) {
                Reference reference1 = (Reference) lifeline.getRepresents();
                Reference reference2 = (Reference) lifelineCopy.getRepresents();
                if (reference1.getType() == reference2.getType()
                        && reference1.getLowerBound() == reference2.getLowerBound()
                        && reference1.getUpperBound() == reference2.getUpperBound()
                        && reference1.getReferenceType() == reference2.getReferenceType()
                        && reference1.isStatic() == reference2.isStatic()
                        // If the two references have the same container, there are two on purpose and not the same.
                        && reference1.eContainer() != reference2.eContainer()) {
                    return lifeline;
                }
            } else {
                if (equalityHelper.equals(lifeline, lifelineCopy)) {
                    return lifeline;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Returns all message views from the given aspect that specify an operation
     * contained in the given list of operations.
     * 
     * @param wovenOperations the list of operations
     * @param base the aspect
     * @return a list of all message views that specify an operation of the given list
     */
    private List<MessageView> getMessageViewsFor(List<Operation> wovenOperations, Aspect base) {
        List<MessageView> messageViews = new ArrayList<MessageView>();
        
        for (MessageView messageView : RAMModelUtil.getMessageViewsOfType(base, MessageView.class)) {
            if (wovenOperations.contains(messageView.getSpecifies())) {
                messageViews.add(messageView);
            }
        }
        
        return messageViews;
    }
    
    /**
     * Merges the affected by of the given {@link MessageView} with the {@link AbstractMessageView}. The affected by of
     * the <em>dependeeMessageView</em> are added to the <em>baseMessageView</em>.
     * The merge either adds the new elements to the front or the end (append).
     * 
     * @param baseMessageView the {@link AbstractMessageView} (could be a {@link MessageView} or
     *            {@link MessageViewReference}) from the base where affected by information should be added to
     * @param dependeeMessageView the {@link AbstractMessageView} from the dependee
     *            whose affected by information should be added to the base message view,
     * @param append true, if message views should be appended (added to the end),
     *            false, if they should be added to the beginning
     */
    private void mergeAffectedBy(AbstractMessageView baseMessageView, AbstractMessageView dependeeMessageView,
            boolean append) {
        for (AspectMessageView affectedBy : dependeeMessageView.getAffectedBy()) {
            int index = 0;
            
            if (append) {
                index = baseMessageView.getAffectedBy().size();
            }
            
            baseMessageView.getAffectedBy().add(index, affectedBy);
        }
    }
    
    /**
     * Creates the initial interaction for a message view based on the given
     * interaction, which may be the advice of an aspect message view.
     * The initial interaction consists of the initial call (the operation that
     * is specified) to the target lifeline.
     * 
     * @param specifies the {@link Operation} that is specified by the {@link Interaction} to be created
     * @param advice the {@link Interaction} the initialized interaction should be based on
     * @return the initial interaction based on the given interaction
     */
    private Interaction initializeInteraction(Operation specifies, Interaction advice) {
        // TODO: mschoettle: What if this operation returns something?
        
        // Create new interaction ...
        Interaction specification = RamFactory.eINSTANCE.createInteraction();
        
        // Get the first occurrence of the interaction, which should be the
        // receive event of the incoming call (MessageOccurrence).
        MessageOccurrenceSpecification oldReceiveEvent = (MessageOccurrenceSpecification) advice.getFragments().get(0);
        
        // Create the new lifeline based on the receive event ...
        Lifeline oldLifeline = oldReceiveEvent.getCovered().get(0);
        Lifeline lifeline = EcoreUtil.copy(oldLifeline);
        specification.getLifelines().add(lifeline);
        
        // Create the new represents (target) ...
        Reference oldRepresents = (Reference) oldLifeline.getRepresents();
        Reference represents = EcoreUtil.copy(oldRepresents);
        specification.getProperties().add(represents);
        
        lifeline.setRepresents(represents);
        
        // Create the receiving message occurrence ...
        MessageOccurrenceSpecification receiveEvent = EcoreUtil.copy(oldReceiveEvent);
        receiveEvent.getCovered().add(lifeline);
        specification.getFragments().add(receiveEvent);
        
        // Create the send event (Gate) ...
        Gate oldGate = (Gate) oldReceiveEvent.getMessage().getSendEvent();
        Gate gate = EcoreUtil.copy(oldGate);
        gate.setName("in_" + specifies.getName());
        
        specification.getFormalGates().add(gate);
        
        // Create the message ...
        Message oldMessage = oldReceiveEvent.getMessage();
        Message message = EcoreUtil.copy(oldMessage);
        // Remove local properties.
        message.getLocalProperties().clear();
        message.setSignature(specifies);
        message.setSendEvent(gate);
        message.setReceiveEvent(receiveEvent);
        
        specification.getMessages().add(message);
        
        gate.setMessage(message);
        receiveEvent.setMessage(message);
        
        return specification;
    }
}
