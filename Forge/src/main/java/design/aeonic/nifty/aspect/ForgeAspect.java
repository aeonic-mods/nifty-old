package design.aeonic.nifty.aspect;

import design.aeonic.nifty.api.aspect.Aspect;
import design.aeonic.nifty.api.aspect.AspectProvider;
import net.minecraftforge.common.util.LazyOptional;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ForgeAspect<T> implements Aspect<T> {

    private final Supplier<LazyOptional<T>> supplier;
    private LazyOptional<T> cached;

    static <T> ForgeAspect<T> of(AspectProvider provider, Supplier<LazyOptional<T>> supplier) {
        ForgeAspect<T> aspect = new ForgeAspect<>(supplier);
        provider.onRefreshAspects(aspect::refresh);
        return aspect;
    }

    private ForgeAspect(Supplier<LazyOptional<T>> supplier) {
        this.supplier = supplier;
        this.cached = supplier.get();
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

    public <R> Wrapper<T, R> wrap(Function<T, R> wrapperFunction) {
        return new Wrapper<>(this, wrapperFunction);
    }

    public static final class Wrapper<A, B> implements Aspect<B> {

        private final ForgeAspect<A> aspect;
        private final Function<A, B> wrapperFunction;

        private Wrapper(ForgeAspect<A> aspect, Function<A, B> wrapperFunction) {
            this.aspect = aspect;
            this.wrapperFunction = wrapperFunction;
        }

        @Override
        public Optional<B> get() {
            return Optional.ofNullable(aspect.ifPresent(wrapperFunction));
        }

        @Override
        public void refresh() {
            aspect.refresh();
        }
    }

}
