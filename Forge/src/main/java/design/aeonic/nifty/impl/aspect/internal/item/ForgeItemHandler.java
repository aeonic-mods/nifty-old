package design.aeonic.nifty.impl.aspect.internal.item;

import design.aeonic.nifty.api.aspect.internal.item.ItemHandler;
import design.aeonic.nifty.api.aspect.internal.item.SimpleItemHandler;
import design.aeonic.nifty.api.aspect.internal.item.slot.AbstractSlot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.function.BiPredicate;

/**
 * An {@link IItemHandler} implementation that defers to the item handler Aspect to expose inventories to Forge cap item systems.
 */
public class ForgeItemHandler implements ItemHandler, IItemHandler {


    private final ItemHandler parent;

    public ForgeItemHandler(ItemHandler parent) {
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
    public CompoundTag serialize(CompoundTag tag) {
        return parent.serialize(tag);
    }

    @Override
    public void deserialize(CompoundTag tag) {
        parent.deserialize(tag);
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
