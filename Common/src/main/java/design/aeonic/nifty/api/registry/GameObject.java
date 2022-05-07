package design.aeonic.nifty.api.registry;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

/**
 * Represents a game object inside one of Minecraft's (or Forge/Fabric's) registries.
 */
public interface GameObject<T> extends Supplier<T> {
    /**
     * @return the object's resource location
     */
    ResourceLocation key();

    /**
     * A memoized getter for the actual game object.
     *
     * @return the object. If it hasn't yet been registered, an error will be thrown
     */
    T get();

    /**
     * @return the object, or null if it hasn't yet been registered
     */
    T getOrNull();
}
