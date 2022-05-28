package design.aeonic.nifty.impl.energy;

import design.aeonic.nifty.api.energy.EnergyHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.energy.IEnergyStorage;

public class IEnergyStorageWrapper implements EnergyHandler {

    protected final IEnergyStorage parent;

    public IEnergyStorageWrapper(IEnergyStorage parent) {
        this.parent = parent;
    }

    @Override
    public long getCapacity() {
        return parent.getMaxEnergyStored();
    }

    @Override
    public long getAmount() {
        return parent.getEnergyStored();
    }

    @Override
    public boolean allowInsertion() {
        return parent.canReceive();
    }

    @Override
    public boolean allowExtraction() {
        return parent.canExtract();
    }

    @Override
    public long insert(long amount, boolean simulate) {
        return parent.receiveEnergy((int) Math.min(amount, Integer.MAX_VALUE), simulate);
    }

    @Override
    public long extract(long amount, boolean simulate) {
        return parent.extractEnergy((int) Math.min(amount, Integer.MAX_VALUE), simulate);
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
