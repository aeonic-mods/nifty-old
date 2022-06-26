package design.aeonic.nifty.api.aspect;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Defines an Aspect; synonymous with a Capability on Forge. On Forgen't defers to Fabric API transactions.<br><br>
 * The Aspect system allows you to conditionally expose objects from any {@link AspectProvider}. It consists of a few
 * moving parts:
 * <ul>
 *     <li>{@link Aspect}<ul>
 *         <li>Contains a cached instance of the actual object that's being exposed.</li>
 *         <li>Whenever the wrapped instance becomes unavailable (the Aspect is invalidated, or the Provider it belongs
 *         to is invalidated), the Aspect attempts to fetch the contained object from whence it came.</li>
 *         <li>Whenever the Optional this Aspect gives you is empty, the object it was tied to has become inaccessible.</li>
 *         <li>Because of this pattern, any game object that needs to access other providers' Aspects can simply store
 *         the Aspect objects for easy caching.</li>
 *         <li>On Forge, essentially a replacement for LazyOptional with the extra caching contained within</li>
 *         <li>On Fabric, takes the form of a slightly more complex hook into the API transaction system</li>
 *     </ul></li>
 *     <li>The actual Aspect's implementation<ul>
 *         <li>Provided as an Aspect's type parameter and exposed within the Optional it returns.</li>
 *         <li>Defines an Aspect's API contract.</li>
 *         <li>Generally a single interface backed by any number of implementations.</li>
 *         <li>Can be an existing interface from a specific platform (ie one that's already exposed in a Forge capability).</li>
 *     </ul></li>
 *     <li>{@link AspectProvider}<ul>
 *         <li>Contains any number of objects (see above) that it can expose within Aspects.</li>
 *         <li>Platform-specific implementations are applied via mixin to existing classes:<ul>
 *             <li>{@link net.minecraft.world.level.block.entity.BlockEntity BlockEntity}</li>
 *             <li>{@link net.minecraft.world.item.ItemStack ItemStack}</li>
 *             <li>{@link javax.swing.text.html.parser.Entity Entity}</li>
 *             <li>Custom Aspect provider implementations can be created, but need to hook into platform-specific Aspect implementations.</li>
 *         </ul></li>
 *         <li>To use, just have your subclass implement the correct AspectProvider listed above.</li>
 *     </ul></li>
 * </ul>
 * As an Aspect can wrap any object, it's also used to expose existing Capabilities on Forge (or the equivalent on Fabric,
 * once we figure that out; the implementation is dubious). Just pass a {@link Class} object for the capability interface
 * you're trying to access and it'll be provided as an Aspect! Handy, no?
 *
 * @param <T> the Aspect interface
 */
public interface Aspect<T> {

    /**
     * Wraps a given object as an Aspect.
     *
     * @param supplier a supplier to update the contained object - should probably be a call to the provider's #getAspect method
     * @return the object, wrapped as an Aspect that will update via the passed supplier when invalidated
     */
    static <T> Aspect<T> of(Supplier<T> supplier) {
        return new SimpleAspect<>(supplier);
    }

    /**
     * Creates an Aspect that will always be empty.
     */
    static <T> Aspect<T> empty() {
        return new Empty<>();
    }

    /**
     * Gets the actual object this Aspect wraps
     */
    Optional<T> resolve();

    /**
     * Called when the Aspect is invalidated; refreshes the contained Optional from wherever it was acquired.<br><br>
     * Can also be used to force update a cahced Aspect.
     */
    void refresh();

    /**
     * Checks whether the contained object exists.
     */
    default boolean isPresent() {
        return resolve().isPresent();
    }

    /**
     * Identical to {@link #ifPresent(Function)}, but without the return value.
     */
    default void ifPresent(Consumer<T> consumer) {
        resolve().ifPresent(consumer);
    }

    /**
     * Identical to {@link #ifPresent(BiFunction)}, but without the Aspect parameter.
     */
    default @Nullable
    <R> R ifPresent(Function<T, R> function) {
        return ifPresent((aspect, t) -> function.apply(t));
    }

    /**
     * Fancier {@link Optional#ifPresent(Consumer)}; runs the given function and returns its return value if the wrapped
     * object is present; otherwise returns null. The function is passed this aspect object as well in case you need to
     * store it etc.<br><br>
     */
    default @Nullable
    <R> R ifPresent(BiFunction<Aspect<T>, T, R> function) {
        if (resolve().isPresent())
            return function.apply(this, resolve().get());
        return null;
    }

    /**
     * Simple Aspect implementation for providers exposing Aspects.<br><br>
     */
    class SimpleAspect<T> implements Aspect<T> {

        private final Supplier<T> supplier;
        private @Nullable
        T cached;

        private SimpleAspect(Supplier<T> supplier) {
            this.supplier = supplier;
            this.cached = supplier.get();
        }

        @Override
        public Optional<T> resolve() {
            return Optional.ofNullable(cached);
        }

        @Override
        public void refresh() {
            cached = supplier.get();
        }

    }

    class Empty<T> implements Aspect<T> {

        private Empty() {
        }

        @Override
        public Optional<T> resolve() {
            return Optional.empty();
        }

        @Override
        public void refresh() {
        }

    }

}
