package design.aeonic.nifty.impl.item;

import design.aeonic.nifty.api.item.ItemHandler;
import design.aeonic.nifty.api.core.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.items.IItemHandler;

import java.util.function.BiPredicate;

/**
 * An item handler implementation that defers to an existing {@link net.minecraftforge.items.IItemHandler}.
 * Essentially the inverse of {@link ForgeItemHandler}.
 */
public class IItemHandlerWrapper implements ItemHandler {

    protected final IItemHandler parent;

    public IItemHandlerWrapper(IItemHandler parent) {
        this.parent = parent;
    }

    @Override
    public int getCapacity(int slot) {
        return parent.getSlotLimit(slot);
    }

    @Override
    public boolean allowedInSlot(int slot, ItemStack stack) {
        return parent.isItemValid(slot, stack);
    }

    @Override
    public ItemStack insert(ItemStack stack, boolean simulate) {
        ItemStack ret = stack.copy();
        for (int i = 0; i < getNumSlots(); i++) {
            if (ret.isEmpty()) return ItemStack.EMPTY;
            ret = insert(i, ret, simulate);
        }
        return ret;
    }

    @Override
    public ItemStack insert(int slot, ItemStack stack, boolean simulate) {
        return parent.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extract(BiPredicate<ItemLike, CompoundTag> itemPredicate, int amount, boolean simulate) {
        for (int i = 0; i < parent.getSlots(); i++) {
            ItemStack stack = parent.getStackInSlot(i);
            if (itemPredicate.test(stack.getItem(), stack.getOrCreateTag())) {
                return extract(i, amount, simulate);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack extract(int slot, int amount, boolean simulate) {
        return parent.extractItem(slot, Math.min(amount, parent.getStackInSlot(slot).getMaxStackSize()), simulate);
    }

    @Override
    public int getNumSlots() {
        return parent.getSlots();
    }

    @Override
    public ItemStack get(int index) {
        return parent.getStackInSlot(index);
    }

    @Override
    public void set(int slot, ItemStack stack) {
        Constants.LOGGER.error("#set() was called on a wrapping ItemHandler!");
    }

    @Override
    public CompoundTag serialize() {
        return null;
    }

    @Override
    public void deserialize(CompoundTag tag) {}

    @Override
    public void toNetwork(FriendlyByteBuf buf) {}

    @Override
    public void fromNetwork(FriendlyByteBuf buf) {}

}
