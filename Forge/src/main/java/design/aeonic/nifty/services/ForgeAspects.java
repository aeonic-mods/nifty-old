package design.aeonic.nifty.services;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import design.aeonic.nifty.api.aspect.Aspects;
import design.aeonic.nifty.api.aspect.internal.item.ItemHandler;
import design.aeonic.nifty.api.aspect.internal.item.slot.AbstractSlot;
import design.aeonic.nifty.aspect.internal.ForgeItemHandler;
import design.aeonic.nifty.mixin.access.CapabilityManagerAccess;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import org.checkerframework.checker.units.qual.C;
import org.objectweb.asm.Type;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;


public class ForgeAspects implements Aspects {

    private final BiMap<Class<?>, Capability<?>> aspectCaps = HashBiMap.create();
    public Consumer<RegisterCapabilitiesEvent> registerAspects = (RegisterCapabilitiesEvent event) -> {};

    @Override
    public <T> void registerAspect(Class<T> aspectType) {
        aspectCaps.put(aspectType, ((CapabilityManagerAccess) ((Object) CapabilityManager.INSTANCE)).callGet(Type.getInternalName(aspectType), false));
        registerAspects = registerAspects.andThen(event -> event.register(aspectType));
    }

    @Override
    public boolean exists(Class<?> aspectClass) {
        return aspectCaps.containsKey(aspectClass);
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
            return (Capability<T>) aspectCaps.get(aspectClass);
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> Class<T> asAspectClass(Capability<?> cap) {
        try {
            return (Class<T>) aspectCaps.inverse().get(cap);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ItemHandler itemHandler(AbstractSlot... slots) {
        return new ForgeItemHandler(slots);
    }

}
