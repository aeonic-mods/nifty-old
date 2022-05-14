package design.aeonic.nifty.impl.registry;

import design.aeonic.nifty.api.registry.GameObject;
import design.aeonic.nifty.api.registry.Registrar;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ForgeRegistrar implements Registrar {

    // Each key-value pair represents a different namespace - in practice, usually a different mod per DeferredRegisterMap
    public final Map<String, DeferredRegisterMap> modRegistryMap = new HashMap<>();

    @Override
    public <T> GameObject<T> register(Registry<? super T> registry, ResourceLocation key, Supplier<T> object) {
        var registryMap = modRegistryMap.computeIfAbsent(key.getNamespace(), DeferredRegisterMap::new);
        return new ForgeGameObject<>(registryMap.register(registry, key.getPath(), object));
    }

}
