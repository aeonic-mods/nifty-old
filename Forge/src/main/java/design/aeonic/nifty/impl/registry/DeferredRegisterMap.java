package design.aeonic.nifty.impl.registry;

import net.minecraft.core.Registry;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A generic map of Vanilla {@link Registry} -> Forge {@link DeferredRegister} for use in {@link ForgeRegistrar}.<br><br>
 * Probably not very well optimized but should be negligible as it's only used once during registry.
 */
@SuppressWarnings("unchecked")
public class DeferredRegisterMap {

    private final String namespace;
    private final List<Entry<?>> entries = new ArrayList<>();

    public DeferredRegisterMap(String namespace) {
        this.namespace = namespace;
    }

    public <T> RegistryObject<T> register(Registry<? super T> registry, String name, Supplier<T> object) {
        return get(registry).register(name, object);
    }

    public <T> DeferredRegister<T> get(Registry<T> key) {
        var ret = getOrNull(key);
        if (ret != null) return ret;
        var register = DeferredRegister.create(key.key(), namespace);
        register.register(FMLJavaModLoadingContext.get().getModEventBus());
        return put(key, register);
    }

    public <T> DeferredRegister<T> getOrNull(Registry<T> key) {
        for (var entry : entries) {
            if (entry.key.equals(key)) return (DeferredRegister<T>) entry.value;
        }
        return null;
    }

    <T> DeferredRegister<T> put(Registry<T> key, DeferredRegister<T> value) {
        entries.add(new Entry<>(key, value));
        return value;
    }

    record Entry<T>(Registry<T> key, DeferredRegister<T> value) implements Map.Entry<Registry<T>, DeferredRegister<T>> {
        @Override
        public Registry<T> getKey() {
            return key;
        }

        @Override
        public DeferredRegister<T> getValue() {
            return value;
        }

        @Override
        public DeferredRegister<T> setValue(DeferredRegister<T> value) {
            throw new UnsupportedOperationException();
        }
    }
}
