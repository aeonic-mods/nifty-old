package design.aeonic.nifty.impl.aspect.internal;

import design.aeonic.nifty.api.aspect.internal.item.SimpleItemHandler;
import design.aeonic.nifty.api.aspect.internal.item.slot.AbstractSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link IItemHandler} implementation that defers to the item handler Aspect to expose inventories to Forge cap-driven item systems.
 */
public class ForgeItemHandler extends SimpleItemHandler implements IItemHandler {

    public ForgeItemHandler(AbstractSlot... slots) {
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
        ItemStack newStack = stack.copy();
        if (simulate) return getSlot(slotIndex).simulateInsert(newStack);
        return getSlot(slotIndex).insert(newStack);
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
