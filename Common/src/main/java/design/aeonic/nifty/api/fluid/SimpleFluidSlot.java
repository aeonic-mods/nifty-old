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
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public SimpleFluidSlot(Runnable onChanged, int capacity) {
        this(onChanged, $ -> true, capacity);
    }

    public SimpleFluidSlot(Runnable onChanged, int x, int y, int width, int height, int capacity) {
        this(onChanged, x, y, width, height, $ -> true, capacity);
    }

    public SimpleFluidSlot(Runnable onChanged, Predicate<FluidStack> allowedPredicate, int capacity) {
        this(onChanged, -1, -1, 16, 64, allowedPredicate, capacity);
    }

    public SimpleFluidSlot(Runnable onChanged, int x, int y, int width, int height,
                           Predicate<FluidStack> allowedPredicate, int capacity) {
        this.onChanged = onChanged;
        this.allowedPredicate = allowedPredicate;
        this.capacity = capacity;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

}
