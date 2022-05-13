package design.aeonic.nifty.api.aspect;

import net.minecraft.core.Direction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A game object that can expose arbitrary contained objects and data in the form of Aspects. See the {@link Aspect}
 * interface for more info. This interface is applied via mixin to a few classes by default:
 * <ul>
 *     <li>{@link net.minecraft.world.level.block.entity.BlockEntity BlockEntity}</li>
 *     <li>{@link net.minecraft.world.item.Item Item}</li> (for item stacks)
 *     <li>{@link net.minecraft.world.entity.Entity Entity}</li>
 * </ul>
 * It can be added to other arbitrary game objects you might define yourself (or other existing ones), but you'll need
 * to provide an implementation mirroring what's present in the platform-specific mixins for these existing providers.
 */
public interface AspectProvider {

    /**
     * Gets an Aspect, if it exists and should be exposed on the given side. This decision is made by the provider
     * subclass (ie your custom block entity).<br><br>
     * If this provider isn't a blockentity, the direction will probably be null; you should return whatever Aspect
     * you want to expose regardless of the direction value.<br><br>
     * If you're not returning an Aspect, you should return the super implementation. Otherwise, use {@link Aspect#of}
     * to wrap the object you're exposing.<br><br>
     * You can pass any interface to this method, if it's an interface that exists as a Forge capability from another mod
     * (or within the Fabric API system), the provider will attempt to wrap it as an Aspect.
     * @param aspectClass the base class/interface of the Aspect to return - you may want to compare with
     * {@link Class#isAssignableFrom(Class)} for safety
     * @param <A> the Aspect type to return
     * @return a given Aspect if you want to expose it (via {@link Aspect#of}), otherwise an empty Aspect
     */
    default <A> Aspect<A> getAspect(Class<A> aspectClass, @Nullable Direction direction) {
        return Aspect.empty();
    }

    /**
     * Adds a callback to be run when this object's exposed Aspects should be refreshed or invalidated.<br><br>
     * When this is run depends on the provider implementation.<br><br>
     * The implementation for this method is provided by the default aspect provider mixins; you probably shouldn't
     * override it unless you know what you're doing.
     * @param callback the callback to run on refresh
     */
    default void onRefreshAspects(Runnable callback) {}

    /**
     * Called when this provider's exposed Aspects have been updated or the provider has been invalidated.<br>
     * If you are changing where Aspects can be access from at any point, or modifying Aspects in general, call this method!<br><br>
     * The implementation for this method is provided by the default aspect provider mixins; you probably shouldn't
     * override it unless you know what you're doing.
     */
    default void refreshAspects() {}

    /**
     * Casts the given object to an AspectProvider to avoid warnings in your editor (since mixins are not visibly applied
     * at compile time).<br><br>
     * Obviously the passed object should be one of those listed in the {@link AspectProvider} Javadoc, or one that
     * you've added the interface to yourself.
     */
    static AspectProvider cast(@Nonnull Object gameObject) {
        return (AspectProvider) gameObject;
    }

}
