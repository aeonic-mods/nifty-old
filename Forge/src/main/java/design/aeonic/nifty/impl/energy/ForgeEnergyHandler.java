package design.aeonic.nifty.impl.energy;

import design.aeonic.nifty.api.energy.EnergyHandler;
import design.aeonic.nifty.impl.aspect.WrappingEnergyHandler;
import net.minecraftforge.energy.IEnergyStorage;

public class ForgeEnergyHandler extends WrappingEnergyHandler implements IEnergyStorage {

    public ForgeEnergyHandler(EnergyHandler parent) {
        super(parent);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return (int) Math.min(parent.insert(maxReceive, simulate), Integer.MAX_VALUE);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return (int) Math.min(parent.extract(maxExtract, simulate), Integer.MAX_VALUE);
    }

    @Override
    public int getEnergyStored() {
        return (int) Math.min(parent.getAmount(), Integer.MAX_VALUE);
    }

    @Override
    public int getMaxEnergyStored() {
        return (int) Math.min(parent.getCapacity(), Integer.MAX_VALUE);
    }

    @Override
    public boolean canReceive() {
        return parent.allowInsertion();
    }

    @Override
    public boolean canExtract() {
        return parent.allowExtraction();
    }

}
