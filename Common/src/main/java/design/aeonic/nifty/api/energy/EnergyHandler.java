package design.aeonic.nifty.api.energy;

import design.aeonic.nifty.api.item.ItemHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

/**
 * An aspect that allows providers to contain and transfer energy.<br><br>
 * Much like {@link ItemHandler} and {@link design.aeonic.nifty.api.item.FluidHandler}, but vastly simplified
 * as is the nature of power.<br><br>
 * On Forge, energy is in terms of FE - on Fabric, who knows, man?
 */
public interface EnergyHandler {

    /**
     * Gets the capacity of this handler.
     */
    long getCapacity();

    /**
     * Gets the amount of currently contained energy in this handler.
     */
    long getAmount();

    /**
     * A preliminary check for whether this handler should allow inserting energy.<br><br>
     * If this method returns false, any calls to {@link #insert} should return 0.
     */
    boolean allowInsertion();

    /**
     * A preliminary check for whether this handler should allow extracting energy.<br><br>
     * If this method returns false, any calls to {@link #extract} should return 0.
     */
    boolean allowExtraction();

    /**
     * Inserts up to a specified amount of energy.
     *
     * @param amount   the max amount to insert
     * @param simulate if true, only simulates insertion and doesn't modify the handler
     * @return the amount inserted
     */
    long insert(long amount, boolean simulate);

    /**
     * Extracts up to a specified amount of energy.
     *
     * @param amount   the max amount to extract
     * @param simulate if true, only simulates insertion and doesn't modify the handler
     * @return the amount extracted
     */
    long extract(long amount, boolean simulate);

    /**
     * Writes this energy handler's storage to NBT.
     */
    CompoundTag serialize();

    /**
     * Reads and reconstructs this energy handler's storage from NBT.
     */
    void deserialize(CompoundTag tag);

    /**
     * Writes this energy handler to a network packet.
     */
    void toNetwork(FriendlyByteBuf buf);

    /**
     * Reads and reconstructs this energy handler from a network packet.
     */
    void fromNetwork(FriendlyByteBuf buf);

}
