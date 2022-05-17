package design.aeonic.nifty.impl.aspect.internal.item;

import design.aeonic.nifty.api.aspect.internal.item.ItemHandler;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Iterator;
import java.util.function.BiPredicate;

/**
 * A {@link Storage<ItemVariant>} implementation that defers to a Nifty item handler to expose inventories to Fabric transfer systems.
 */
public final class FabricItemHandler implements ItemHandler, Storage<ItemVariant> {

    private final ItemHandler parent;

    public FabricItemHandler(ItemHandler parent) {
        this.parent = parent;
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
    public long insert(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        int amt = Math.min((int) maxAmount, resource.getItem().getMaxStackSize());
        transaction.addCloseCallback((t, r) -> { if (r.wasCommitted())
            insert(resource.toStack(amt), false); });
        return amt - insert(resource.toStack(amt), true).getCount();
    }

    @Override
    public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
        transaction.addCloseCallback((t, r) -> { if (r.wasCommitted())
            extract((item, tag) -> resource.getItem() == item && resource.getNbt().equals(tag), (int) maxAmount, false); });
        return extract((item, tag) -> resource.getItem() == item && resource.getNbt().equals(tag), (int) maxAmount, true).getCount();
    }

    @Override
    public Iterator<? extends StorageView<ItemVariant>> iterator(TransactionContext transaction) {
        return new SlotIterator();
    }

    class SlotView implements StorageView<ItemVariant> {

        private final int index;

        public SlotView(int index) {
            this.index = index;
        }

        @Override
        public long extract(ItemVariant resource, long maxAmount, TransactionContext transaction) {
            ItemStack stored = get(index);
            if (maxAmount > 0 && resource.matches(stored)) {
                transaction.addCloseCallback((t, r) -> { if (r.wasCommitted()) FabricItemHandler.this.extract(index, (int) maxAmount, false); });
                return FabricItemHandler.this.extract(index, (int) maxAmount, true).getCount();
            }
            return 0;
        }

        @Override
        public boolean isResourceBlank() {
            return get(index).isEmpty();
        }

        @Override
        public ItemVariant getResource() {
            return ItemVariant.of(get(index));
        }

        @Override
        public long getAmount() {
            return get(index).getCount();
        }

        @Override
        public long getCapacity() {
            return get(index).getMaxStackSize();
        }
    }

    class SlotIterator implements Iterator<SlotView> {

        int index = -1;

        @Override
        public boolean hasNext() {
            return index < getNumSlots() - 1;
        }

        @Override
        public SlotView next() {
            return new SlotView(++ index);
        }

    }
}
