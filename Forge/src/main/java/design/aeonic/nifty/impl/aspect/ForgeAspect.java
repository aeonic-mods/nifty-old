package design.aeonic.nifty.impl.aspect;

import design.aeonic.nifty.api.aspect.Aspect;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class ForgeAspect<T> implements Aspect<T> {

    private final Supplier<LazyOptional<T>> supplier;
    private LazyOptional<T> cached;

    public ForgeAspect(Supplier<LazyOptional<T>> supplier) {
        this.supplier = supplier;
        refresh();
    }

    @Override
    public Optional<T> resolve() {
        return cached.resolve();
    }

    @Override
    public void refresh() {
        cached = supplier.get();
        if (cached.isPresent())
            cached.addListener($ -> refresh());
    }

    public static class Wrapping<C, A> implements Aspect<A> {

        private final Supplier<LazyOptional<? extends C>> supplier;
        private final Function<C, A> wrappingFunc;
        private LazyOptional<? extends C> cached;

        public Wrapping(Supplier<LazyOptional<? extends C>> supplier, Function<C, A> wrappingFunc) {
            this.supplier = supplier;
            this.wrappingFunc = wrappingFunc;
            refresh();
        }

        @Override
        public Optional<A> resolve() {
            Optional<? extends C> res = cached.resolve();
            return res.map(wrappingFunc);
        }

        @Override
        public void refresh() {
            cached = supplier.get();
            if (cached.isPresent())
                cached.addListener($ -> refresh());
        }

    }

}
