package design.aeonic.nifty.impl.aspect;

import design.aeonic.nifty.api.aspect.Aspect;
import design.aeonic.nifty.api.aspect.AspectProvider;
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
    public Optional<T> get() {
        return cached.resolve();
    }

    @Override
    public void refresh() {
        cached = supplier.get();
        if (cached.isPresent())
            cached.addListener($ -> refresh());
    }

    public static class Wrapping<A, B> implements Aspect<B> {

        private final Supplier<LazyOptional<A>> supplier;
        private final Function<A, B> wrappingFunc;
        private LazyOptional<A> cached;

        public Wrapping(Supplier<LazyOptional<A>> supplier, Function<A, B> wrappingFunc) {
            this.supplier = supplier;
            this.wrappingFunc = wrappingFunc;
            refresh();
        }

        @Override
        public Optional<B> get() {
            Optional<A> res = cached.resolve();
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
