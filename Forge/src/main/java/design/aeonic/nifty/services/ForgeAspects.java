package design.aeonic.nifty.services;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import design.aeonic.nifty.api.aspect.Aspects;
import design.aeonic.nifty.mixin.access.CapabilityManagerAccess;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import org.objectweb.asm.Type;

import javax.annotation.Nullable;
import java.util.function.Consumer;


public class ForgeAspects implements Aspects {

//    /**
//     * Used for capabilities that have an existing implementation
//     */
//    @SuppressWarnings("rawtypes") // Doesn't like a wildcard Aspect for some reason
//    public final BiMap<Class<? extends Aspect>, Capability<?>> EXISTING_CAPS = HashBiMap.create();
//    public final BiMap<Class<? extends Aspect<?>>, Capability<? extends Aspect<?>>> ASPECT_CAPS = HashBiMap.create();
//
//    public Consumer<RegisterCapabilitiesEvent> registerAspects = (RegisterCapabilitiesEvent event) -> {};
//
//    @Override
//    public <T extends Aspect<?>> void registerAspect(ResourceLocation aspectId, Class<T> aspectType) {
//        // Capabilities can be accessed before they're actually registered
//        ASPECT_CAPS.put(aspectType, ((CapabilityManagerAccess) ((Object) CapabilityManager.INSTANCE)).callGet(Type.getInternalName(aspectType), true));
//        registerAspects = registerAspects.andThen(event -> event.register(aspectType));
//    }

    private final BiMap<Class<?>, Capability<?>> ASPECT_CAPS = HashBiMap.create();
    public Consumer<RegisterCapabilitiesEvent> registerAspects = (RegisterCapabilitiesEvent event) -> {};

    @Override
    public <T> void registerAspect(Class<T> aspectType) {
        ASPECT_CAPS.put(aspectType, ((CapabilityManagerAccess) ((Object) CapabilityManager.INSTANCE)).callGet(Type.getInternalName(aspectType), false));
        registerAspects = registerAspects.andThen(event -> event.register(aspectType));
    }

    public void registerExisting(Class<?> aspectType, Capability<?> capability) {
        ASPECT_CAPS.put(aspectType, capability);
    }

    @Override
    public boolean exists(Class<?> aspectClass) {
        return ASPECT_CAPS.containsKey(aspectClass);
    }

    /**
     * Gets the given Aspect class as a Capability token, or null if it either hasn't been registered or couldn't be
     * cast from the registry map.
     * @param aspectClass the base class/interface passed to {@link Aspects#registerAspect(Class)}
     * @param <T> the Aspect base type
     * @return a Capability representing the Aspect, or null if one couldn't be obtained
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <T> Capability<T> asCapability(Class<?> aspectClass) {
        try {
            return (Capability<T>) ASPECT_CAPS.get(aspectClass);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> Class<T> asAspectClass(Capability<?> cap) {
        try {
            return (Class<T>) ASPECT_CAPS.inverse().get(cap);
        } catch (Exception e) {
            return null;
        }
    }

}
