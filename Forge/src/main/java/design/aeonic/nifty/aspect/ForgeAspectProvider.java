package design.aeonic.nifty.aspect;

import design.aeonic.nifty.ForgeNifty;
import design.aeonic.nifty.api.aspect.Aspect;
import design.aeonic.nifty.api.aspect.AspectProvider;
import design.aeonic.nifty.mixin.access.CapabilityManagerAccess;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.openjdk.nashorn.internal.codegen.types.Type;

import javax.annotation.Nullable;

/**
 * Forge implementation for an Aspect provider that defers to Capabilities.
 */
public interface ForgeAspectProvider<T extends CapabilityProvider<T>> extends AspectProvider {

    T getProvider();

    @Override
    default <A> Aspect<A> getAspect(Class<A> aspectClass, @Nullable Direction direction) {
        if (!ForgeNifty.ASPECTS.exists(aspectClass)) {
            // If the aspect class has been registered as an Aspect, do nothing - it would have been exposed by the
            // child's override of this method

            // Not in known Aspects; attempt to expose an existing Capability as an Aspect
            Capability<A> cap = ((CapabilityManagerAccess) ((Object) CapabilityManager.INSTANCE)).callGet(Type.getInternalName(aspectClass), false);
            if (cap.isRegistered()) {
                // The capability exists
                LazyOptional<A> opt = getProvider().getCapability(cap, direction);
                onRefreshAspects(opt::invalidate);
                return new ForgeAspect<>(opt, () -> getProvider().getCapability(cap, direction));
            }
        }
        // Default behavior
        return AspectProvider.super.getAspect(aspectClass, direction);
    }

    /**
     * Callback for Forge {@link CapabilityProvider}s to expose Aspects as capabilities.<br><br>
     * If null, the default super implementation of {@link CapabilityProvider#getCapability} should be used.
     */
    static @Nullable <A> LazyOptional<A> getCapabilityCallback(AspectProvider provider, Capability<A> cap, @Nullable Direction direction) {
        Class<A> clazz = ForgeNifty.ASPECTS.asAspectClass(cap);
        if (clazz != null) {
            // The capability is a previously registered Aspect
            Aspect<A> aspect = provider.getAspect(clazz, direction);
            @Nullable A obj = aspect.ifPresent((a, o) -> o);
            if (obj != null) return LazyOptional.of(() -> obj);
            return LazyOptional.empty();
        }
        return null;
    }

}
