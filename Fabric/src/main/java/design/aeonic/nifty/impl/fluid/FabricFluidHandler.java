package design.aeonic.nifty.impl.fluid;

import design.aeonic.nifty.api.fluid.FluidStack;
import design.aeonic.nifty.api.item.FluidHandler;
import design.aeonic.nifty.impl.aspect.WrappingFluidHandler;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.nbt.CompoundTag;

import java.util.Iterator;

/**
 * A {@link Storage<FluidVariant>} implementation that defers to a Nifty item handler to expose inventories to Fabric transfer systems.
 */
public class FabricFluidHandler extends WrappingFluidHandler implements Storage<FluidVariant> {

    public FabricFluidHandler(FluidHandler parent) {
        super(parent);
    }

    @Override
    public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        transaction.addCloseCallback((t, r) -> {
            if (r.wasCommitted()) insert(asFluidStack(resource, maxAmount), false);
        });
        return maxAmount - insert(asFluidStack(resource, maxAmount), true).getAmount();
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        transaction.addCloseCallback((t, r) -> {
            if (r.wasCommitted()) extract((fluid, tag) -> resource.getFluid().isSame(fluid) && (!resource.hasNbt() || resource.getNbt().equals(tag)),
                    (int) Math.min(maxAmount, Integer.MAX_VALUE), false);
        });
        return extract((fluid, tag) -> resource.getFluid().isSame(fluid) && (!resource.hasNbt() || resource.getNbt().equals(tag)),
                (int) Math.min(maxAmount, Integer.MAX_VALUE), true).getAmount();
    }

    @Override
    public Iterator<? extends StorageView<FluidVariant>> iterator(TransactionContext transaction) {
        return new SlotIterator();
    }

    public static FluidStack asFluidStack(FluidVariant resource, long amt) {
        return FluidStack.of(resource.getFluid(), (int) Math.min(amt, Integer.MAX_VALUE), resource.hasNbt() ? resource.getNbt() : new CompoundTag());
    }

    class SlotView implements StorageView<FluidVariant> {

        private final int index;

        public SlotView(int index) {
            this.index = index;
        }

        @Override
        public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
            FluidStack stored = get(index);
            if (maxAmount > 0 && resource.isOf(stored.getFluid()) && resource.nbtMatches(stored.getTag())) {
                transaction.addCloseCallback((t, r) -> {
                    if (r.wasCommitted()) FabricFluidHandler.this.extract(index, (int) Math.min(maxAmount, Integer.MAX_VALUE), false);
                });
                return FabricFluidHandler.this.extract(index, (int) Math.min(maxAmount, Integer.MAX_VALUE), true).getAmount();
            }
            return 0;
        }

        @Override
        public boolean isResourceBlank() {
            return get(index).isEmpty();
        }

        @Override
        public FluidVariant getResource() {
            return FluidVariant.of(get(index).getFluid(), get(index).getTag());
        }

        @Override
        public long getAmount() {
            return get(index).getAmount();
        }

        @Override
        public long getCapacity() {
            return FabricFluidHandler.this.getCapacity(index);
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
