package design.aeonic.nifty.impl.fluid;

import design.aeonic.nifty.api.fluid.FluidStack;
import design.aeonic.nifty.api.item.FluidHandler;
import design.aeonic.nifty.api.util.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.function.BiPredicate;

public class IFluidHandlerWrapper implements FluidHandler {

    protected final IFluidHandler parent;

    public IFluidHandlerWrapper(IFluidHandler parent) {
        this.parent = parent;
    }

    @Override
    public boolean allowedInSlot(int slot, FluidStack stack) {
        return parent.isFluidValid(slot, ForgeFluidHandler.forgeFromNifty(stack));
    }

    @Override
    public FluidStack insert(FluidStack stack, boolean simulate) {
        return stack.split(parent.fill(ForgeFluidHandler.forgeFromNifty(stack),
                simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE));
    }

    @Override
    public FluidStack insert(int slot, FluidStack stack, boolean simulate) {
        return insert(stack, simulate);
    }

    @Override
    public FluidStack extract(BiPredicate<Fluid, CompoundTag> fluidPredicate, int amount, boolean simulate) {
        for (int i = 0; i < parent.getTanks(); i++) {
            net.minecraftforge.fluids.FluidStack stack = parent.getFluidInTank(i).copy();
            if (fluidPredicate.test(stack.getFluid(), stack.getTag())) {
                stack.setAmount(amount);
                return ForgeFluidHandler.niftyFromForge(parent.drain(stack,
                        simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE));
            }
        }
        return FluidStack.EMPTY_STACK;
    }

    @Override
    public FluidStack extract(int slot, int amount, boolean simulate) {
        return ForgeFluidHandler.niftyFromForge(parent.drain(amount,
                simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE));
    }

    @Override
    public int getNumSlots() {
        return parent.getTanks();
    }

    @Override
    public FluidStack get(int slot) {
        return ForgeFluidHandler.niftyFromForge(parent.getFluidInTank(slot));
    }

    @Override
    public void set(int slot, FluidStack stack) {
        Constants.LOGGER.error("#set() was called on a wrapping FluidHandler!");
    }

    @Override
    public int getCapacity(int slot) {
        return parent.getTankCapacity(slot);
    }

    @Override
    public CompoundTag serialize() {
        return null;
    }

    @Override
    public void deserialize(CompoundTag tag) {}

}
