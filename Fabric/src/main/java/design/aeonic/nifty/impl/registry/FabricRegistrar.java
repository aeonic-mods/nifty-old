package design.aeonic.nifty.impl.registry;

import design.aeonic.nifty.api.registry.GameObject;
import design.aeonic.nifty.api.registry.Registrar;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class FabricRegistrar implements Registrar {
    @Override
    public <T> GameObject<T> register(Registry<? super T> registry, ResourceLocation key, Supplier<T> object) {
        return new FabricGameObject<>(key, Registry.register(registry, key, object.get()));
    }
}
