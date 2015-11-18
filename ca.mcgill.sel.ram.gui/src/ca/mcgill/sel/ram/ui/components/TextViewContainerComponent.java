package ca.mcgill.sel.ram.ui.components;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import ca.mcgill.sel.ram.ui.layouts.HorizontalLayout;
import ca.mcgill.sel.ram.ui.utils.Colors;
import ca.mcgill.sel.ram.ui.views.TextView;
import ca.mcgill.sel.ram.ui.views.handler.HandlerFactory;
import ca.mcgill.sel.ram.ui.views.handler.ITextViewHandler;

/**
 * A Container that can contain many {@link TextView}s.
 *
 * @author Romain
 *
 */
public class TextViewContainerComponent extends RamRectangleComponent {

    /**
     * The list of {@link TextView}s.
     */
    private List<TextView> textViews;

    /**
     * Create a new {@link TextViewContainerComponent}. By default, his layout is a {@link HorizontalLayout} has fill
     * and stroke.
     */
    public TextViewContainerComponent() {
        textViews = new ArrayList<TextView>();

        setNoFill(false);
        setFillColor(Colors.DEFAULT_ELEMENT_FILL_COLOR);
        setNoStroke(false);
        setStrokeColor(Colors.DEFAULT_ELEMENT_STROKE_COLOR);

        setLayout(new HorizontalLayout());
    }

    /**
     * Create a new {@link TextViewContainerComponent}.
     *
     * @param container the container object of the features.
     * @param withTextViewHandler if true, a default {@link ITextViewHandler} will be set for each {@link TextView}
     *            created.
     * @param features the features to display
     */
    public TextViewContainerComponent(EObject container, boolean withTextViewHandler, EStructuralFeature... features) {
        ITextViewHandler handler = withTextViewHandler ? HandlerFactory.INSTANCE.getTextViewHandler() : null;

        for (EStructuralFeature feature : features) {
            createTextView(container, feature, handler);
        }
    }

    /**
     * Create a new {@link TextViewContainerComponent}.
     *
     * @param container the container object of the features.
     * @param feature the features to display
     * @param handler the {@link ITextViewHandler} to set for the {@link TextView} created.
     */
    public void createTextView(EObject container, EStructuralFeature feature, ITextViewHandler handler) {
        TextView textView = new TextView(container, feature);

        if (handler != null) {
            textView.registerTapProcessor(handler);
        }

        this.addChild(textView);
        this.textViews.add(textView);
    }

    /**
     * Retrieve the first {@link TextView}.
     *
     * @return the first {@link TextView}.
     */
    public TextView getFirstTextView() {
        return this.textViews.get(0);
    }

    /**
     * Retrieve the last {@link TextView}.
     *
     * @return the last {@link TextView}.
     */
    public TextView getLastTextView() {
        return this.textViews.get(textViews.size() - 1);
    }

    /**
     * Retrieve the list of {@link TextView}s.
     *
     * @return the list of {@link TextView}s.
     */
    public List<TextView> getTextViews() {
        return textViews;
    }
}
