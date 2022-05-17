package design.aeonic.nifty.impl.aspect.internal.item;

import com.google.common.collect.Iterators;
import design.aeonic.nifty.api.aspect.internal.item.ItemHandler;
import design.aeonic.nifty.api.util.Constants;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.function.BiPredicate;

/**
 * An item handler implementation that defers to an existing Fabric item storage.
 * Essentially the inverse of {@link FabricItemHandler}.
 */
public record ItemVariantStorageWrapper(Storage<ItemVariant> storage) implements ItemHandler {

    @Override
    public ItemStack insert(int slot, ItemStack stack, boolean simulate) {
        return insert(stack, simulate);
    }

    @Override
    public ItemStack insert(ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return stack;
        Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe());
        int i = 0;
        int inserted = (int) storage.insert(ItemVariant.of(stack), stack.getCount(), transaction);
        if (simulate) transaction.abort(); else transaction.commit();
        return stack.split(inserted);
    }

    @Override
    public ItemStack extract(BiPredicate<ItemLike, CompoundTag> itemPredicate, int amount, boolean simulate) {
        Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe());
        ItemStack ret = ItemStack.EMPTY;
        int i = 0;
        for (var iter = storage.iterator(transaction); iter.hasNext(); i++) {
            var view = iter.next();
            if (view.isResourceBlank()) continue;
            ItemVariant resource = view.getResource();
            if (itemPredicate.test(resource.getItem(), resource.getNbt())) {
                ret = resource.toStack((int) view.extract(resource, amount, transaction));
                break;
            }
        }
        if (simulate) transaction.abort(); else transaction.commit();
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
                ret = item.toStack((int) view.extract(item, amount, transaction));
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
    public void set(int slot, ItemStack stack) {
        Constants.LOGGER.error("#set() was called on a wrapping ItemHandler!");
    }

    @Override
    public CompoundTag serialize(CompoundTag tag) {
        return null;
    }

    @Override
    public void deserialize(CompoundTag tag) {}

}
