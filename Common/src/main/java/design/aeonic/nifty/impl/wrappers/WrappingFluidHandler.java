package design.aeonic.nifty.impl.wrappers;

import design.aeonic.nifty.api.fluid.FluidStack;
import design.aeonic.nifty.api.item.FluidHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.material.Fluid;

import java.util.function.BiPredicate;

public class WrappingFluidHandler implements FluidHandler {

    protected final FluidHandler parent;

    public WrappingFluidHandler(FluidHandler parent) {
        this.parent = parent;
    }

    @Override
    public boolean allowedInSlot(int slot, FluidStack stack) {
        return parent.allowedInSlot(slot, stack);
    }

    @Override
    public FluidStack insert(FluidStack stack, boolean simulate) {
        return parent.insert(stack, simulate);
    }

    @Override
    public FluidStack insert(int slot, FluidStack stack, boolean simulate) {
        return parent.insert(slot, stack, simulate);
    }

    @Override
    public FluidStack extract(BiPredicate<Fluid, CompoundTag> fluidPredicate, int amount, boolean simulate) {
        return parent.extract(fluidPredicate, amount, simulate);
    }

    @Override
    public FluidStack extract(int slot, int amount, boolean simulate) {
        return parent.extract(slot, amount, simulate);
    }

    @Override
    public int getNumSlots() {
        return parent.getNumSlots();
    }

    @Override
    public FluidStack get(int slot) {
        return parent.get(slot);
    }

    @Override
    public void set(int slot, FluidStack stack) {
        parent.set(slot, stack);
    }

    @Override
    public int getCapacity(int slot) {
        return parent.getCapacity(slot);
    }

    @Override
    public CompoundTag serialize() {
        return parent.serialize();
    }

    @Override
    public void deserialize(CompoundTag tag) {
        parent.deserialize(tag);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf) {
        parent.toNetwork(buf);
    }

    @Override
    public void fromNetwork(FriendlyByteBuf buf) {
        parent.fromNetwork(buf);
    }

}
