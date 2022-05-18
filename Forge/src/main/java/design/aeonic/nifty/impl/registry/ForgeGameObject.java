package design.aeonic.nifty.impl.registry;

import design.aeonic.nifty.api.registry.GameObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public final class ForgeGameObject<T> implements GameObject<T> {

    private final RegistryObject<T> object;

    public ForgeGameObject(RegistryObject<T> object) {
        this.object = object;
    }

    @Override
    public ResourceLocation key() {
        return object.getId();
    }

    @Override
    public boolean isRegistered() {
        return object.isPresent();
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
