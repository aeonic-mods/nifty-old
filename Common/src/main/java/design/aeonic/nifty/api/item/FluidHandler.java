package design.aeonic.nifty.api.item;

import design.aeonic.nifty.api.fluid.FluidStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.material.Fluid;

import java.util.function.BiPredicate;

/**
 * An aspect that allows providers to contain, move, and manipulate fluids via {@link FluidStack}s.<br><br>
 * Each "slot" represents an individual tank that can hold an arbitrary amount of a given fluid type.
 */
public interface FluidHandler {

    /**
     * Checks if the given fluid stack is allowed in the given slot.<br><br>
     * Checks for stacking should be done elsewhere; the return of this method should be the same regardless
     * of the given slot's current contents.
     */
    boolean allowedInSlot(int slot, FluidStack stack);

    /**
     * Same as {@link #insert(int, FluidStack, boolean)}, but inserts to the first available slot, filling empty slots and
     * stacking with whatever it can.
     */
    FluidStack insert(FluidStack stack, boolean simulate);

    /**
     * Attempts to insert a fluid stack. Returns the remainder of the inserted stack.
     *
     * @param slot     the slot index to insert to
     * @param stack    the stack to insert
     * @param simulate if true, doesn't make any changes to stored contents
     * @return what's left of the inserted fluid stack - if nothing is inserted, will equal the passed stack
     * @throws IndexOutOfBoundsException if the slot index is invalid.
     */
    FluidStack insert(int slot, FluidStack stack, boolean simulate);

    /**
     * Attempts to extract the given amount from any slot of a resource matching the given predicate.
     *
     * @param fluidPredicate a predicate that must be met for a stack to be extracted
     * @param amount         the amount to extract
     * @param simulate       if true, doesn't make any changes to stored contents
     * @return the extracted fluid stack
     */
    FluidStack extract(BiPredicate<Fluid, CompoundTag> fluidPredicate, int amount, boolean simulate);

    /**
     * Attempts to extract the given amount from the given slot, returning the extracted stack. In most cases,
     * {@link #extract(BiPredicate, int, boolean)} should be used instead for better performance on Forgen't.<br><br>
     *
     * @param slot     the slot index to extract from
     * @param amount   the amount to extract
     * @param simulate if true, doesn't make any changes to stored contents
     * @return the extracted stack; an empty stack if extraction failed (or the slot is empty, etc)
     * @throws IndexOutOfBoundsException if the slot index is invalid.
     */
    FluidStack extract(int slot, int amount, boolean simulate);

    /**
     * Returns the number of fluid slots (tanks) in this handler.
     */
    int getNumSlots();

    /**
     * Gets the fluid stack in the given slot.
     */
    FluidStack get(int slot);

    /**
     * Sets the fluid stack in the given slot.
     */
    void set(int slot, FluidStack stack);

    /**
     * Gets the capacity, in milibuckets, of the given slot.
     */
    default int getCapacity(int slot) {
        return FluidStack.BUCKET;
    }

    /**
     * Writes this fluid handler's slot contents to a given compound tag.<br><br>
     * Assumes a tag without existing data.
     */
    CompoundTag serialize();

    /**
     * Reads and reconstructs this fluid handler's slot contents from a given compound tag.
     */
    void deserialize(CompoundTag tag);

    /**
     * Writes this fluid handler's slot contents to a network packet.
     */
    void toNetwork(FriendlyByteBuf buf);

    /**
     * Reads and reconstructs this fluid handler's slot contents from a network packet.
     */
    void fromNetwork(FriendlyByteBuf buf);

}
