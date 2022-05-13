package design.aeonic.nifty.aspect.internal;

import design.aeonic.nifty.api.aspect.AspectProvider;
import design.aeonic.nifty.api.aspect.internal.item.SimpleItemHandler;
import design.aeonic.nifty.api.aspect.internal.item.slot.AbstractSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link IItemHandler} implementation that defers to the item handler Aspect to expose inventories to Forge cap-driven item systems.
 */
public class AspectIItemHandler extends SimpleItemHandler implements IItemHandler {

    public AspectIItemHandler(AbstractSlot... slots) {
        super(slots);
    }

    @Override
    public int getSlots() {
        return getNumSlots();
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return getSlot(slot).get();
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slotIndex, @NotNull ItemStack stack, boolean simulate) {
        if (simulate) return getSlot(slotIndex).simulateInsert(stack);
        return getSlot(slotIndex).insert(stack);
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slotIndex, int amount, boolean simulate) {
        if (simulate) return getSlot(slotIndex).simulateExtract(amount);
        return getSlot(slotIndex).extract(amount);
    }

    @Override
    public int getSlotLimit(int slotIndex) {
        AbstractSlot slot = getSlot(slotIndex);
        return slot.maxStackSize(slot.get());
    }

    @Override
    public boolean isItemValid(int slotIndex, @NotNull ItemStack stack) {
        return getSlot(slotIndex).allowedInSlot(stack);
    }
}
