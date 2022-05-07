package design.aeonic.nifty.registry;

import design.aeonic.nifty.api.registry.GameObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public record ForgeGameObject<T>(RegistryObject<T> object) implements GameObject<T> {
    @Override
    public ResourceLocation key() {
        return object.getId();
    }

    @Override
    public T get() {
        return object.get();
    }

    @Override
    public T getOrNull() {
        return object.get();
    }
}
