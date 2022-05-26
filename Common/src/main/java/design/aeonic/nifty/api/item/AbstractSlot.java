package design.aeonic.nifty.api.item;

import net.minecraft.world.item.ItemStack;

/**
 * Describes an {@link ItemHandler} slot. <i>Not</i> synonymous with a Vanilla {@link net.minecraft.world.inventory.Slot Container Slot}.<br><br>
 * Serialization is handled in the Aspect itself; it delegates insertion/extraction and any checks to this class.<br><br>
 * This class thus serves as a behavior specification for a given slot in a given item handler. Dynamic behavior is possible with anonymous instances, or with a subclass that delegates those checks. See {@link AbstractSlot}.
 */
public abstract class AbstractSlot {

    protected ItemStack containedStack;

    public AbstractSlot() {
        this.containedStack = ItemStack.EMPTY;
    }

    /**
     * Gets the x position of this slot in a screen.
     */
    public int getX() {
        return -1;
    }

    /**
     * Gets the y position of this slot in a screen.
     */
    public int getY() {
        return -1;
    }

    /**
     * Checks whether the given item is allowed in this slot. To make an "output-only" slot, just return false here
     * and move items to this slot with {@link #forceInsert(ItemStack)}.
     *
     * @param stack the item stack to check
     */
    public abstract boolean allowedInSlot(ItemStack stack);

    /**
     * Run when this slot's contents have changed; used to mark that an item handler should be reserialized.
     */
    public abstract void onChanged();

    /**
     * Allows overriding the max stack size for a given item stack.<br><br>
     * If you're changing behavior here, you most likely want to use a multiply of the stack's max size and check for unstackables.
     */
    public int maxStackSize(ItemStack stack) {
        return stack.getMaxStackSize();
    }

    /**
     * Simulates insertion.
     */
    public ItemStack simulateInsert(ItemStack stack) {
        ItemStack temp = containedStack;
        containedStack = temp.copy();
        ItemStack ret = insert(stack);
        containedStack = temp;
        return ret;
    }

    /**
     * Simulates extraction.
     */
    public ItemStack simulateExtract(int amount) {
        ItemStack temp = containedStack;
        containedStack = temp.copy();
        ItemStack ret = extract(amount);
        containedStack = temp;
        return ret;
    }

    /**
     * Inserts an item stack to the slot if allowed; returns the remainder of the inserted stack..<br><br>
     *
     * @param stack the item stack to insert
     * @return what's left of the inserted item stack - if nothing is inserted, will equal the passed stack
     * @see #forceInsert(ItemStack)
     */
    public ItemStack insert(ItemStack stack) {
        if (allowedInSlot(stack)) return forceInsert(stack);
        return stack;
    }

    /**
     * Inserts an item stack to the slot regardless of whether it's allowed in it; returns the remainder of the inserted stack.<br><br>
     * Still respects the max slot stack size and any stack that's already in the slot; in other words, normal insertion behavior, but without the slot's filtering.
     *
     * @param stack the item stack to insert
     * @return what's left of the inserted item stack - if nothing is inserted, will equal the passed stack
     */
    public ItemStack forceInsert(ItemStack stack) {
        ItemStack old = stack.copy();
        if (!containedStack.isEmpty()) {
            // Item can't be stacked with contained
            if (!(stack.sameItem(containedStack) &&
                    ((!stack.hasTag() && !containedStack.hasTag()) ||
                            stack.getOrCreateTag().equals(containedStack.getTag())))) return stack;

            int toInsert = maxStackSize(stack) - containedStack.getCount();
            // Something fishy
            if (toInsert <= 0) return stack;
            // Normal stacking
            ItemStack temp = stack.split(toInsert);
            containedStack.grow(temp.getCount());
            return stack;
        }
        // Slot is empty
        containedStack = stack.split(maxStackSize(stack));
        if (!stack.equals(old)) onChanged();
        return stack;
    }

    /**
     * Attempts to extract the given amount, modifying the slot's stored item immediately and returning the extracted stack.<br><br>
     *
     * @param amount the amount to extract
     * @return the extracted stack; an empty stack if extraction failed or the slot is empty
     */
    public ItemStack extract(int amount) {
        if (amount > 0) onChanged();
        return containedStack.split(amount);
    }

    /**
     * Gets the item stack instance in this slot.
     */
    public ItemStack get() {
        return containedStack;
    }

    /**
     * Sets the contained item stack, with no checks. Mainly for internal use.
     *
     * @param stack the item stack
     */
    public void set(ItemStack stack) {
        containedStack = stack;
    }
}
