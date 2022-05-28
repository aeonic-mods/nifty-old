package design.aeonic.nifty.impl.wrappers;

import design.aeonic.nifty.api.energy.EnergyHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class WrappingEnergyHandler implements EnergyHandler {

    protected final EnergyHandler parent;

    public WrappingEnergyHandler(EnergyHandler parent) {
        this.parent = parent;
    }

    @Override
    public long getCapacity() {
        return parent.getCapacity();
    }

    @Override
    public long getAmount() {
        return parent.getAmount();
    }

    @Override
    public boolean allowInsertion() {
        return parent.allowInsertion();
    }

    @Override
    public boolean allowExtraction() {
        return parent.allowExtraction();
    }

    @Override
    public long insert(long amount, boolean simulate) {
        return parent.insert(amount, simulate);
    }

    @Override
    public long extract(long amount, boolean simulate) {
        return parent.extract(amount, simulate);
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
