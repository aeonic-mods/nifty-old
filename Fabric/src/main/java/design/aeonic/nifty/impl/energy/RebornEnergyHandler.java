package design.aeonic.nifty.impl.energy;

import design.aeonic.nifty.api.energy.EnergyHandler;
import design.aeonic.nifty.impl.aspect.WrappingEnergyHandler;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import team.reborn.energy.api.EnergyStorage;

/**
 * An energy handler wrapper with compatibility for Team Reborn's energy API.
 */
public class RebornEnergyHandler extends WrappingEnergyHandler implements EnergyStorage {

    public RebornEnergyHandler(EnergyHandler parent) {
        super(parent);
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        transaction.addCloseCallback((t, r) -> {
            if (r.wasCommitted()) insert(maxAmount, false);
        });
        return insert(maxAmount, true);
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        transaction.addCloseCallback((t, r) -> {
            if (r.wasCommitted()) extract(maxAmount, false);
        });
        return extract(maxAmount, true);
    }

    @Override
    public boolean supportsInsertion() {
        return parent.allowInsertion();
    }

    @Override
    public boolean supportsExtraction() {
        return parent.allowExtraction();
    }
}
