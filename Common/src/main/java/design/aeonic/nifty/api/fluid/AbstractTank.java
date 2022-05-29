package design.aeonic.nifty.api.fluid;

import design.aeonic.nifty.api.client.ui.template.FillingUiElementTemplate;

public abstract class AbstractTank implements FillingUiElementTemplate.FillLevel {

    protected FluidStack containedStack;

    public AbstractTank() {
        this.containedStack = FluidStack.EMPTY_STACK.copy();
    }

    /**
     * Checks whether the given fluid stack is allowed in this slot. To make an "output-only" slot, just return false here
     * and move fluids to this slot with {@link #forceInsert(FluidStack)}.
     *
     * @param stack the fluid stack to check
     */
    public abstract boolean allowedInSlot(FluidStack stack);

    /**
     * Run when this slot's contents have changed; used to mark that a fluid handler should be reserialized.
     */
    public abstract void onChanged();

    /**
     * Returns the max amount of a given fluid that this slot can hold.<br><br>
     * Return should disregard whatever the current slot contents are. For a tank with a specific capacity for all
     * fluids, just return a constant (ie a 16 bucket tank should just always return 16,000).
     */
    public int getCapacity() {
        return FluidStack.BUCKET;
    }

    /**
     * Simulates insertion.
     */
    public FluidStack simulateInsert(FluidStack stack) {
        FluidStack temp = containedStack.copy();
        FluidStack ret = insert(stack);
        containedStack = temp;
        return ret;
    }

    /**
     * Simulates extraction.
     */
    public FluidStack simulateExtract(int amount) {
        FluidStack temp = containedStack.copy();
        FluidStack ret = extract(amount);
        containedStack = temp;
        return ret;
    }

    /**
     * Inserts a fluid stack to the slot if allowed; returns the remainder of the inserted stack.<br><br>
     *
     * @param stack the item stack to insert
     * @return what's left of the inserted fluid stack - if nothing is inserted, will equal the passed stack
     * @see #forceInsert(FluidStack)
     */
    public FluidStack insert(FluidStack stack) {
        if (allowedInSlot(stack)) return forceInsert(stack);
        return stack;
    }

    /**
     * Inserts a fluid stack to the slot regardless of whether it's allowed in it; returns the remainder of the inserted stack.<br><br>
     * Still respects the max slot capacity and any stack that's already in the slot; in other words, normal insertion
     * behavior, but without the slot's filtering.
     *
     * @param stack the fluid stack to insert
     * @return what's left of the inserted fluid stack - if nothing is inserted, will equal the passed stack
     */
    public FluidStack forceInsert(FluidStack stack) {
        FluidStack old = stack.copy();
        if (!containedStack.isEmpty()) {
            // Item can't be stacked with contained
            if (!stack.canStack(containedStack)) return stack;

            int toInsert = getCapacity() - containedStack.getAmount();
            // Something fishy
            if (toInsert <= 0) return stack;
            // Normal stacking
            FluidStack temp = stack.split(toInsert);
            containedStack.grow(temp.getAmount());
            return stack;
        }
        // Slot is empty
        containedStack = stack.split(getCapacity());
        if (!stack.equals(old)) onChanged();
        return stack;
    }

    /**
     * Attempts to extract the given amount, modifying the slot's stored fluid immediately and returning the extracted stack.<br><br>
     *
     * @param amount the amount to extract
     * @return the extracted stack; an empty stack if extraction failed or the slot is empty
     */
    public FluidStack extract(int amount) {
        if (amount > 0) onChanged();
        return containedStack.split(amount);
    }

    /**
     * Gets the fluid stack instance in this slot.
     */
    public FluidStack get() {
        return containedStack;
    }

    /**
     * Sets the contained fluid stack, with no checks. Mainly for internal use.
     *
     * @param stack the fluid stack
     */
    public void set(FluidStack stack) {
        containedStack = stack;
    }

    @Override
    public float getFillLevel() {
        return get().getAmount() / (float) getCapacity();
    }
}
