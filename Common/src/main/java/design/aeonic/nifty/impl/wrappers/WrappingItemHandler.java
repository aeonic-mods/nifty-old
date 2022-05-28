package design.aeonic.nifty.impl.wrappers;

import design.aeonic.nifty.api.item.ItemHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.BiPredicate;

public class WrappingItemHandler implements ItemHandler {

    protected final ItemHandler parent;

    public WrappingItemHandler(ItemHandler parent) {
        this.parent = parent;
    }

    @Override
    public boolean allowedInSlot(int slot, ItemStack stack) {
        return parent.allowedInSlot(slot, stack);
    }

    @Override
    public ItemStack insert(ItemStack stack, boolean simulate) {
        return parent.insert(stack, simulate);
    }

    @Override
    public ItemStack insert(int slot, ItemStack stack, boolean simulate) {
        return parent.insert(slot, stack, simulate);
    }

    @Override
    public ItemStack extract(BiPredicate<ItemLike, CompoundTag> itemPredicate, int amount, boolean simulate) {
        return parent.extract(itemPredicate, amount, simulate);
    }

    @Override
    public ItemStack extract(int slot, int amount, boolean simulate) {
        return parent.extract(slot, amount, simulate);
    }

    @Override
    public int getCapacity(int slot) {
        return parent.getCapacity(slot);
    }

    @Override
    public int getNumSlots() {
        return parent.getNumSlots();
    }

    @Override
    public ItemStack get(int index) {
        return parent.get(index);
    }

    @Override
    public void set(int slot, ItemStack stack) {
        parent.set(slot, stack);
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