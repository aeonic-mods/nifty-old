package design.aeonic.nifty.api.fluid;

import design.aeonic.nifty.api.item.FluidHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.material.Fluid;

import java.util.function.BiPredicate;

/**
 * A simple fluid handler implementation that uses {@link AbstractFluidSlot} instances to describe functionality.
 */
public class SimpleFluidHandler implements FluidHandler {

    private final AbstractFluidSlot[] slots;

    public SimpleFluidHandler(AbstractFluidSlot... slots) {
        this.slots = slots;
    }

    public AbstractFluidSlot getSlot(int index) {
        return slots[index];
    }

    @Override
    public boolean allowedInSlot(int slot, FluidStack stack) {
        return getSlot(slot).allowedInSlot(stack);
    }

    @Override
    public FluidStack insert(FluidStack stack, boolean simulate) {
        FluidStack ret = stack.copy();
        for (int i = 0; i < getNumSlots(); i++) {
            if (ret.isEmpty()) return FluidStack.EMPTY_STACK;
            ret = insert(i, ret, simulate);
        }
        return ret;
    }

    @Override
    public FluidStack insert(int slot, FluidStack stack, boolean simulate) {
        if (simulate)
            return getSlot(slot).simulateInsert(stack);
        return getSlot(slot).insert(stack);
    }

    @Override
    public FluidStack extract(BiPredicate<Fluid, CompoundTag> fluidPredicate, int amount, boolean simulate) {
        for (int i = 0; i < slots.length; i++) {
            FluidStack stack = slots[i].get();
            if (fluidPredicate.test(stack.getFluid(), stack.getTag())) {
                return extract(i, amount, simulate);
            }
        }
        return FluidStack.EMPTY_STACK;
    }

    @Override
    public FluidStack extract(int slot, int amount, boolean simulate) {
        if (simulate)
            return getSlot(slot).simulateExtract(amount);
        return getSlot(slot).extract(amount);
    }

    @Override
    public int getNumSlots() {
        return slots.length;
    }

    @Override
    public FluidStack get(int slot) {
        return getSlot(slot).get();
    }

    @Override
    public void set(int slot, FluidStack stack) {
        getSlot(slot).set(stack);
    }

    @Override
    public int getCapacity(int slot) {
        return getSlot(slot).getCapacity();
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        ListTag list = tag.getList("Slots", Tag.TAG_COMPOUND);

        for (AbstractFluidSlot slot : slots) {
            list.add(slot.get().toNbt());
        }
        tag.put("Slots", list);
        return tag;
    }

    @Override
    public void deserialize(CompoundTag tag) {
        ListTag list = tag.getList("Slots", Tag.TAG_COMPOUND);

        for (int i = 0; i < slots.length; i++) {
            slots[i].set(FluidStack.fromNbt(list.getCompound(i)));
        }
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf) {
        for (AbstractFluidSlot slot: slots) {
            slot.get().toNetwork(buf);
        }
    }

    @Override
    public void fromNetwork(FriendlyByteBuf buf) {
        for (AbstractFluidSlot slot: slots) {
            slot.set(FluidStack.fromNetwork(buf));
        }
    }

}
