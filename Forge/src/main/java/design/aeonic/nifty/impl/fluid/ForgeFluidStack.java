package design.aeonic.nifty.impl.fluid;

import design.aeonic.nifty.api.fluid.FluidStack;

public class ForgeFluidStack extends FluidStack {

    public ForgeFluidStack(net.minecraftforge.fluids.FluidStack stack) {
        super(stack.getFluid(), stack.getAmount(), stack.getOrCreateTag());
    }

}
