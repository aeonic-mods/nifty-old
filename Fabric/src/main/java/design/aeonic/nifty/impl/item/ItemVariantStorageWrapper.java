package design.aeonic.nifty.impl.item;

import com.google.common.collect.Iterators;
import design.aeonic.nifty.api.item.ItemHandler;
import design.aeonic.nifty.api.util.Constants;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.BiPredicate;

/**
 * An item handler implementation that defers to an existing Fabric item storage.
 * Essentially the inverse of {@link FabricItemHandler}.
 */
public record ItemVariantStorageWrapper(Storage<ItemVariant> storage) implements ItemHandler {

    @Override
    public boolean allowedInSlot(int slot, ItemStack stack) {
        // When actual insertion is tried the backing Storage should just treat the input correctly if it's
        // not allowed. Not much we could do here, besides iterate and check the itemvariant in each "slot"
        return true;
    }

    @Override
    public ItemStack insert(int slot, ItemStack stack, boolean simulate) {
        return insert(stack, simulate);
    }

    @Override
    public ItemStack insert(ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return stack;
        Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe());
        int inserted = (int) Math.min(storage.insert(ItemVariant.of(stack), stack.getCount(), transaction), Integer.MAX_VALUE);
        if (simulate) transaction.abort();
        else transaction.commit();
        return stack.split(inserted);
    }

    @Override
    public ItemStack extract(BiPredicate<ItemLike, CompoundTag> itemPredicate, int amount, boolean simulate) {
        Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe());
        ItemStack ret = ItemStack.EMPTY;
        for (var iter = storage.iterator(transaction); iter.hasNext();) {
            var view = iter.next();
            if (view.isResourceBlank()) continue;
            ItemVariant resource = view.getResource();
            if (itemPredicate.test(resource.getItem(), resource.getNbt())) {
                ret = resource.toStack((int) Math.min(view.extract(resource, amount, transaction), Integer.MAX_VALUE));
                break;
            }
        }
        if (simulate) transaction.abort();
        else transaction.commit();
        return ret;
    }

    @Override
    public ItemStack extract(int slot, int amount, boolean simulate) {
        Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe());
        ItemStack ret = ItemStack.EMPTY;
        int i = 0;
        for (var iter = storage.iterator(transaction); iter.hasNext(); i++) {
            var view = iter.next();
            if (i == slot && !view.isResourceBlank()) {
                ItemVariant item = view.getResource();
                ret = item.toStack((int) Math.min(view.extract(item, amount, transaction), Integer.MAX_VALUE));
                break;
            }
        }
        if (simulate) transaction.abort(); else transaction.commit();
        return ret;
    }

    @Override
    public int getNumSlots() {
        Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe());
        int size = Iterators.size(storage.iterator(transaction));
        transaction.abort();
        return size;
    }

    @Override
    public ItemStack get(int index) {
        Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe());
        ItemStack ret = ItemStack.EMPTY;
        int i = 0;
        for (var iter = storage.iterator(transaction); iter.hasNext(); i++) {
            var view = iter.next();
            if (i == index ) {
                ret = view.getResource().toStack();
            }
        }
        transaction.commit();
        return ret;
    }

    @Override
    public int getCapacity(int slot) {
        Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe());
        int ret = ItemHandler.super.getCapacity(slot);
        int i = 0;
        for (var iter = storage.iterator(transaction); iter.hasNext(); i++) {
            var view = iter.next();
            if (i == slot) {
                ret = (int) Math.min(view.getCapacity(), Integer.MAX_VALUE);
            }
        }
        transaction.abort();
        return ret;
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
