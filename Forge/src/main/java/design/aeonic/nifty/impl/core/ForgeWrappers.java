package design.aeonic.nifty.impl.core;

import design.aeonic.nifty.api.energy.EnergyHandler;
import design.aeonic.nifty.api.item.FluidHandler;
import design.aeonic.nifty.api.item.ItemHandler;
import design.aeonic.nifty.api.util.Wrappers;
import design.aeonic.nifty.impl.energy.ForgeEnergyHandler;
import design.aeonic.nifty.impl.fluid.ForgeFluidHandler;
import design.aeonic.nifty.impl.item.ForgeItemHandler;

public class ForgeWrappers implements Wrappers {

    @Override
    public ItemHandler itemHandler(ItemHandler parent) {
        return new ForgeItemHandler(parent);
    }

    @Override
    public FluidHandler fluidHandler(FluidHandler parent) {
        return new ForgeFluidHandler(parent);
    }

    @Override
    public EnergyHandler energyHandler(EnergyHandler parent) {
        return new ForgeEnergyHandler(parent);
    }

}
