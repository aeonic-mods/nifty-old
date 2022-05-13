package design.aeonic.nifty.aspect;

import design.aeonic.nifty.api.aspect.Aspect;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Optional;
import java.util.function.Supplier;

public final class ForgeAspect<T> implements Aspect<T> {

    private final Supplier<LazyOptional<T>> supplier;
    private LazyOptional<T> cached;

    public ForgeAspect(LazyOptional<T> initial, Supplier<LazyOptional<T>> supplier) {
        this.supplier = supplier;
        this.cached = initial;
        this.cached.addListener($ -> refresh());
    }

    @Override
    public Optional<T> get() {
        return cached.resolve();
    }

    @Override
    public void refresh() {
        cached = supplier.get();
    }

}
