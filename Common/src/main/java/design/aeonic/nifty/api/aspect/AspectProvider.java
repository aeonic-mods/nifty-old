package design.aeonic.nifty.api.aspect;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nonnull;

/**
 * For internal use, refreshes cached Aspects from whence they are fetched. Applied via mixin to {@link BlockEntity}, {@link ItemStack} and {@link Entity}.<br><br>
 * The only reason to implement this interface explicitly is to define yourself when exposed Aspects should be refreshed,
 * by calling {@link #refreshAspects()}.
 */
public interface AspectProvider<T> {

    /**
     * Casts the given object to an AspectProvider to avoid warnings in your editor.<br><br>
     * Obviously the passed object should be one of those listed in the {@link AspectProvider} Javadoc, or one that
     * you've added the interface to yourself.
     */
    @SuppressWarnings("unchecked")
    static <T> AspectProvider<T> cast(@Nonnull Object gameObject) {
        return (AspectProvider<T>) gameObject;
    }

    /**
     * Gets the object implementing this interface.
     */
    @SuppressWarnings("unchecked")
    default T self() {
        return (T) this;
    }

    /**
     * Adds a callback to be run when this object's exposed Aspects should be refreshed or invalidated.<br><br>
     * When this is run depends on the provider implementation.<br><br>
     * The implementation for this method is provided by the default aspect provider mixins; you probably shouldn't
     * override it unless you know what you're doing.
     *
     * @param callback the callback to run on refresh
     */
    default void addRefreshCallback(Runnable callback) {}

    /**
     * Called when this provider's exposed Aspects have been updated or the provider has been invalidated.<br>
     * If you are changing where Aspects can be access from at any point, or modifying Aspects in general, call this method!<br><br>
     * The implementation for this method is provided by the default aspect provider mixins; you probably shouldn't
     * override it unless you know what you're doing.
     */
    default void refreshAspects() {}

}
