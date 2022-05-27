package design.aeonic.nifty.api.energy;

import net.minecraft.nbt.CompoundTag;

/**
 * A simple energy handler implementation for easy usage with an initial capacity that can be later changed.
 */
public class SimpleEnergyHandler implements EnergyHandler {

    protected final Runnable onChanged;
    protected long capacity;
    protected boolean allowInsertion;
    protected boolean allowExtraction;
    protected long stored;

    /**
     * Constructs an energy handler that allows both extraction and insertion.
     *
     * @param onChanged a runnable to execute when the handler's contents change
     * @param capacity  the energy handler's total capacity
     */
    public SimpleEnergyHandler(Runnable onChanged, long capacity) {
        this(onChanged, capacity, true, true);
    }

    /**
     * @param onChanged       a runnable to execute when the handler's contents change
     * @param capacity        the energy handler's total capacity
     * @param allowInsertion  whether to allow insertion to this handler
     * @param allowExtraction whether to allow extraction from this handler
     */
    public SimpleEnergyHandler(Runnable onChanged, long capacity, boolean allowInsertion, boolean allowExtraction) {
        this.onChanged = onChanged;
        this.allowInsertion = allowInsertion;
        this.allowExtraction = allowExtraction;
        this.capacity = capacity;
        this.stored = 0;
    }

    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    public void setAmount(long amount) {
        stored = amount;
    }

    public void setAllowInsertion(boolean allowInsertion) {
        this.allowInsertion = allowInsertion;
    }

    public void setAllowExtraction(boolean allowExtraction) {
        this.allowExtraction = allowExtraction;
    }

    @Override
    public long getCapacity() {
        return capacity;
    }

    @Override
    public long getAmount() {
        return stored;
    }

    @Override
    public boolean allowInsertion() {
        return allowInsertion;
    }

    @Override
    public boolean allowExtraction() {
        return allowExtraction;
    }

    @Override
    public long insert(long amount, boolean simulate) {
        if (!allowInsertion) return 0;
        long toInsert = Math.min(amount, capacity - stored);
        if (!simulate && toInsert != 0) {
            stored = stored + toInsert;
            onChanged.run();
        }
        return toInsert;
    }

    @Override
    public long extract(long amount, boolean simulate) {
        if (!allowExtraction) return 0;
        long toExtract = Math.min(amount, stored);
        if (!simulate && toExtract != 0) {
            stored = stored - toExtract;
            onChanged.run();
        }
        return toExtract;
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("Capacity", capacity);
        tag.putLong("Stored", stored);
        return tag;
    }

    @Override
    public void deserialize(CompoundTag tag) {
        long tempCapacity = tag.getLong("Capacity");
        if (tempCapacity != 0) capacity = tempCapacity;
        stored = tag.getLong("Stored");
    }

}
