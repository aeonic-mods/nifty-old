package design.aeonic.nifty.impl.registry;

import design.aeonic.nifty.api.registry.GameObject;
import design.aeonic.nifty.api.registry.Registrar;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class FabricRegistrar implements Registrar {
    @Override
    public <R, T extends R> GameObject<T> register(Registry<R> registry, ResourceLocation key, Supplier<T> object) {
        return new FabricGameObject<>(key, Registry.register(registry, key, object.get()));
    }
}
