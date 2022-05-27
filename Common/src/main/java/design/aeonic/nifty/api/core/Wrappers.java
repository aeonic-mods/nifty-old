package design.aeonic.nifty.api.core;

import design.aeonic.nifty.api.energy.EnergyHandler;
import design.aeonic.nifty.api.item.FluidHandler;
import design.aeonic.nifty.api.item.ItemHandler;

public interface Wrappers {

    /**
     * Wraps an existing item handler, allowing for it to be utilized in existing platform-specific transfer systems.<br><br>
     *
     * @param parent an existing item handler to wrap
     * @return the new item handler
     */
    ItemHandler itemHandler(ItemHandler parent);

    /**
     * Wraps an existing fluid handler, allowing for it to be utilized in existing platform-specific transfer systems.<br><br>
     *
     * @param parent an existing fluid handler to wrap
     * @return the new fluid handler
     */
    FluidHandler fluidHandler(FluidHandler parent);

    /**
     * Wraps an existing energy handler, allowing for it to be utilized in existing platform-specific transfer systems.<br><br>
     *
     * @param parent an existing energy handler to wrap
     * @return the new energy handler
     */
    EnergyHandler energyHandler(EnergyHandler parent);

}
