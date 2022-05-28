package design.aeonic.nifty.api.fluid;

import java.util.function.Predicate;

/**
 * A simple fluid slot implementation. Much of this class mirrors {@link design.aeonic.nifty.api.item.SimpleSlot}.
 * More documentation can be found in the parent class {@link AbstractFluidSlot}.
 */
public class SimpleFluidSlot extends AbstractFluidSlot {

    protected Runnable onChanged;
    protected Predicate<FluidStack> allowedPredicate;
    protected int capacity;

    public SimpleFluidSlot(Runnable onChanged, int capacity) {
        this(onChanged, $ -> true, capacity);
    }

    public SimpleFluidSlot(Runnable onChanged, Predicate<FluidStack> allowedPredicate, int capacity) {
        this.onChanged = onChanged;
        this.allowedPredicate = allowedPredicate;
        this.capacity = capacity;
    }

    @Override
    public void onChanged() {
        onChanged.run();
    }

    @Override
    public boolean allowedInSlot(FluidStack stack) {
        return allowedPredicate.test(stack);
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

}
