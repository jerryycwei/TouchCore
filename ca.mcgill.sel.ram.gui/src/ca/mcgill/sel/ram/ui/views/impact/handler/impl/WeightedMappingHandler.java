package ca.mcgill.sel.ram.ui.views.impact.handler.impl;

import java.util.Arrays;

import ca.mcgill.sel.core.COREWeightedMapping;
import ca.mcgill.sel.core.controller.ControllerFactory;
import ca.mcgill.sel.ram.ui.RamApp;
import ca.mcgill.sel.ram.ui.components.RamListComponent;
import ca.mcgill.sel.ram.ui.components.RamSelectorComponent;
import ca.mcgill.sel.ram.ui.components.listeners.AbstractDefaultRamSelectorListener;
import ca.mcgill.sel.ram.ui.components.listeners.RamListListener;
import ca.mcgill.sel.ram.ui.scenes.AbstractImpactScene;
import ca.mcgill.sel.ram.ui.views.OptionSelectorView;

/**
 * The handler for {@link COREWeightedMapping}.
 *
 * @author Romain
 *
 */
public class WeightedMappingHandler implements RamListListener<COREWeightedMapping> {

    /**
     * The options to display when tap-and-hold is performed.
     */
    private enum OPTION {
        DELETE_MAPPING
    }

    @Override
    public void elementSelected(RamListComponent<COREWeightedMapping> list, COREWeightedMapping element) {
        // TODO Auto-generated method stub

    }

    @Override
    public void elementDoubleClicked(RamListComponent<COREWeightedMapping> list, COREWeightedMapping element) {
        // TODO Auto-generated method stub

    }

    @Override
    public void elementHeld(RamListComponent<COREWeightedMapping> list, final COREWeightedMapping weightedMapping) {
        OptionSelectorView<OPTION> selector = new OptionSelectorView<OPTION>(Arrays.asList(OPTION.values()));

        ((AbstractImpactScene) RamApp.getActiveScene()).addComponent(selector, list.getCenterPointGlobal());

        selector.registerListener(new AbstractDefaultRamSelectorListener<OPTION>() {
            @Override
            public void elementSelected(RamSelectorComponent<OPTION> selector, OPTION element) {
                switch (element) {
                    case DELETE_MAPPING:
                        deleteMappingElement(weightedMapping);
                        break;
                }
            }
        });
    }

    /**
     * Delete a {@link COREWeightedMapping} from the model.
     *
     * @param weightedMapping the {@link COREWeightedMapping} to delete.
     */
    private static void deleteMappingElement(COREWeightedMapping weightedMapping) {
        ControllerFactory.INSTANCE.getFeatureImpactController().deleteWeightedMapping(weightedMapping);
    }

}