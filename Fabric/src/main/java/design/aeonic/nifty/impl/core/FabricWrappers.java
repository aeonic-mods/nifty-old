package design.aeonic.nifty.impl.core;

import design.aeonic.nifty.api.energy.EnergyHandler;
import design.aeonic.nifty.api.item.FluidHandler;
import design.aeonic.nifty.api.item.ItemHandler;
import design.aeonic.nifty.api.core.Wrappers;
import design.aeonic.nifty.impl.fluid.FabricFluidHandler;
import design.aeonic.nifty.impl.item.FabricItemHandler;

public class FabricWrappers implements Wrappers {

    @Override
    public ItemHandler itemHandler(ItemHandler parent) {
        return new FabricItemHandler(parent);
    }

    @Override
    public FluidHandler fluidHandler(FluidHandler parent) {
        return new FabricFluidHandler(parent);
    }

    @Override
    public EnergyHandler energyHandler(EnergyHandler parent) {
        return parent;
    }

}
