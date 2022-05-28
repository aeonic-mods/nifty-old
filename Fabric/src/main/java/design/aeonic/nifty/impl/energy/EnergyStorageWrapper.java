package design.aeonic.nifty.impl.energy;

import design.aeonic.nifty.api.energy.EnergyHandler;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import team.reborn.energy.api.EnergyStorage;

/**
 * An energy handler implementation that defers to an existing Reborn Energy Storage.
 * Essentially the inverse of {@link FabricEnergyHandler}
 */
public record EnergyStorageWrapper(EnergyStorage storage) implements EnergyHandler {

    @Override
    public long getCapacity() {
        return storage.getCapacity();
    }

    @Override
    public long getAmount() {
        return storage.getAmount();
    }

    @Override
    public boolean allowInsertion() {
        return storage.supportsInsertion();
    }

    @Override
    public boolean allowExtraction() {
        return storage.supportsExtraction();
    }

    @Override
    public long insert(long amount, boolean simulate) {
        Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe());
        long inserted = storage.insert(amount, transaction);
        if (simulate) transaction.abort();
        else transaction.commit();
        return inserted;
    }

    @Override
    public long extract(long amount, boolean simulate) {
        Transaction transaction = Transaction.openNested(Transaction.getCurrentUnsafe());
        long extracted = storage.extract(amount, transaction);
        if (simulate) transaction.abort();
        else transaction.commit();
        return extracted;
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
