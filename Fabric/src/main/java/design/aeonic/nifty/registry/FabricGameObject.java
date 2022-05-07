package design.aeonic.nifty.registry;

import design.aeonic.nifty.api.registry.GameObject;
import net.minecraft.resources.ResourceLocation;

public record FabricGameObject<T>(ResourceLocation key, T object) implements GameObject<T> {
    @Override
    public T get() {
        return object;
    }

    @Override
    public T getOrNull() {
        return object;
    }
}
