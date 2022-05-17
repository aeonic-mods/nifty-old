package design.aeonic.nifty.api.aspect.internal.item;

import design.aeonic.nifty.api.aspect.internal.item.slot.AbstractSlot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * An item handler implementation that simplifies creating and defining the behavior of item handler objects.
 * This is done by deferring the actual behavior to contained {@link AbstractSlot} objects.
 * @see design.aeonic.nifty.api.aspect.internal.item.slot
 */
public class SimpleItemHandler implements ItemHandler {

    protected final AbstractSlot[] slots;

    public SimpleItemHandler(AbstractSlot... slots) {
        this.slots = slots;
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
        if (!simulate) return getSlot(slot).insert(stack);
        ItemStack old = get(slot).copy();
        ItemStack ret = getSlot(slot).insert(stack);
        set(slot, old);
        return ret;
    }

    @Override
    public ItemStack extract(BiPredicate<ItemLike, CompoundTag> itemPredicate, int amount, boolean simulate) {
        for (int i = 0; i < slots.length; i++) {
            ItemStack stack = slots[i].get();
            if (itemPredicate.test(stack.getItem(), stack.getOrCreateTag())) {
                return extract(i, amount, simulate);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack extract(int slot, int amount, boolean simulate) {
        if (!simulate) getSlot(slot).extract(amount);
        ItemStack old = get(slot).copy();
        ItemStack ret = getSlot(slot).extract(amount);
        set(slot, old);
        return ret;
    }

    @Override
    public ItemStack get(int index) {
        return getSlot(index).get();
    }

    @Override
    public void set(int slot, ItemStack stack) {
        getSlot(slot).set(stack);
    }

    public AbstractSlot getSlot(int index) {
        return slots[index];
    }

    @Override
    public int getNumSlots() {
        return slots.length;
    }

    @Override
    public int getCapacity(int slot) {
        return ItemHandler.super.getCapacity(slot);
    }

    public CompoundTag serialize(CompoundTag tag) {
        ListTag list = tag.getList("Slots", Tag.TAG_COMPOUND);

        for (int i = 0; i < slots.length; i++) {
            CompoundTag item = list.getCompound(i);
            slots[i].get().save(item);
            list.add(item);
        }
        tag.put("Slots", list);
        return tag;
    }

    public void deserialize(CompoundTag tag) {
        ListTag list = tag.getList("Slots", Tag.TAG_COMPOUND);

        for (int i = 0; i < slots.length; i++) {
            slots[i].set(ItemStack.of(list.getCompound(i)));
        }
    }

}
