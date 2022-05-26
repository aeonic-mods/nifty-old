package design.aeonic.nifty.impl.item;

import design.aeonic.nifty.api.item.ItemHandler;
import design.aeonic.nifty.impl.aspect.WrappingItemHandler;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

/**
 * An {@link IItemHandler} implementation that defers to the item handler Aspect to expose inventories to Forge cap item systems.
 */
public class ForgeItemHandler extends WrappingItemHandler implements IItemHandler {

    public ForgeItemHandler(ItemHandler parent) {
        super(parent);
    }

    @Override
    public int getSlots() {
        return parent.getNumSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return parent.get(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return parent.insert(slot, stack.copy(), simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return parent.extract(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return parent.getCapacity(slot);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return parent.allowedInSlot(slot, stack);
    }

}
