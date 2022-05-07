package design.aeonic.nifty.api.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

/**
 * Allows for registering game objects from your mod's initializer.
 */
public interface Registrar {

    /**
     * On Fabric, registers objects immediately.<br><br>
     * On Forge, defers registry to the appropriate event.
     * Note that on Forge the registry base class must implement IForgeRegistryEntry.
     *
     * @param registry the registry to use - on Forge, this will be rerouted to the wrapped Forge registry
     * @param key      the object's registry key
     * @param object   a supplier for the object to register
     * @return a {@link GameObject} with the given registry key and a memoized supplier for the object
     */
    <T> GameObject<T> register(Registry<? super T> registry, ResourceLocation key, Supplier<T> object);
}
