package design.aeonic.nifty.impl.fluid;

import com.google.common.collect.Iterators;
import design.aeonic.nifty.api.fluid.FluidStack;
import design.aeonic.nifty.api.item.FluidHandler;
import design.aeonic.nifty.api.util.Constants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.material.Fluid;

import java.util.function.BiPredicate;

/**
 * A fluid handler implementation that defers to an existing Fabric fluid storage.
 * Essentially the inverse of {@link FabricFluidHandler}.
 */
public record FluidVariantStorageWrapper(Storage<FluidVariant> storage) implements FluidHandler {

    @Override
    public boolean allowedInSlot(int slot, FluidStack stack) {
        // See ItemVariantStorageWrapper#allowedInSlot
        return true;
    }

    @Override
    public FluidStack insert(int slot, FluidStack stack, boolean simulate) {
        return insert(stack, simulate);
    }

    @Override
    public FluidStack insert(FluidStack stack, boolean simulate) {
        if (stack.isEmpty()) return stack;
        Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe());
        int inserted = (int) Math.min(storage.insert(asFluidVariant(stack), stack.getAmount(), transaction), Integer.MAX_VALUE);
        if (simulate) transaction.abort();
        else transaction.commit();
        return stack.split(inserted);
    }

    @Override
    public FluidStack extract(BiPredicate<Fluid, CompoundTag> fluidPredicate, int amount, boolean simulate) {
        Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe());
        FluidStack ret = FluidStack.EMPTY_STACK;
        for (var iter = storage.iterator(transaction); iter.hasNext();) {
            var view = iter.next();
            if (view.isResourceBlank()) continue;
            FluidVariant resource = view.getResource();
            if (fluidPredicate.test(resource.getFluid(), resource.getNbt())) {
                ret = FabricFluidHandler.asFluidStack(resource, view.extract(resource, amount, transaction));
                break;
            }
        }
        if (simulate) transaction.abort();
        else transaction.commit();
        return ret;
    }

    @Override
    public FluidStack extract(int slot, int amount, boolean simulate) {
        Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe());
        FluidStack ret = FluidStack.EMPTY_STACK;
        int i = 0;
        for (var iter = storage.iterator(transaction); iter.hasNext(); i++) {
            var view = iter.next();
            if (i == slot && !view.isResourceBlank()) {
                FluidVariant resource = view.getResource();
                ret = FabricFluidHandler.asFluidStack(resource, (int) Math.min(view.extract(resource, amount, transaction), Integer.MAX_VALUE));
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
    public FluidStack get(int slot) {
        Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe());
        FluidStack ret = FluidStack.EMPTY_STACK;
        int i = 0;
        for (var iter = storage.iterator(transaction); iter.hasNext(); i++) {
            var view = iter.next();
            if (i == slot) {
                ret = FabricFluidHandler.asFluidStack(view.getResource(), view.getAmount());
            }
        }
        transaction.commit();
        return ret;
    }

    @Override
    public void set(int slot, FluidStack stack) {
        Constants.LOGGER.error("#set() was called on a wrapping FluidHandler!");
    }

    @Override
    public int getCapacity(int slot) {
        Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe());
        int ret = FluidHandler.super.getCapacity(slot);
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
    public CompoundTag serialize() {
        return null;
    }

    @Override
    public void deserialize(CompoundTag tag) {}

    @Override
    public void toNetwork(FriendlyByteBuf buf) {}

    @Override
    public void fromNetwork(FriendlyByteBuf buf) {}

    public static FluidVariant asFluidVariant(FluidStack stack) {
        return FluidVariant.of(stack.getFluid(), stack.getTag());
    }

}
