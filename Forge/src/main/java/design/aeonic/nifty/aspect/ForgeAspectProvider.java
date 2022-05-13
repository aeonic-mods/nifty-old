package design.aeonic.nifty.aspect;

import design.aeonic.nifty.ForgeNifty;
import design.aeonic.nifty.api.aspect.Aspect;
import design.aeonic.nifty.api.aspect.AspectProvider;
import design.aeonic.nifty.api.aspect.internal.item.ItemHandler;
import design.aeonic.nifty.aspect.internal.IItemHandlerWrapper;
import design.aeonic.nifty.mixin.access.CapabilityManagerAccess;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import org.openjdk.nashorn.internal.codegen.types.Type;

import javax.annotation.Nullable;

/**
 * Forge implementation for an Aspect provider that defers to Capabilities.
 */
public interface ForgeAspectProvider<T extends CapabilityProvider<T>> extends AspectProvider {

    T self();

    @SuppressWarnings("unchecked")
    @Override
    default <A> Aspect<A> getAspect(Class<A> aspectClass, @Nullable Direction direction) {
        // TODO: existing capabilities are currently hardcoded
        if (aspectClass.equals(ItemHandler.class)) {
            return (Aspect<A>) ForgeAspect.of(this,
                    () -> self().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction))
                    .wrap(IItemHandlerWrapper::new);
        }
        if (!ForgeNifty.ASPECTS.exists(aspectClass)) {
            // If the aspect class has not been registered explicitly, attempt to expose an existing Capability as an Aspect
            Capability<A> cap = ((CapabilityManagerAccess) ((Object) CapabilityManager.INSTANCE)).callGet(Type.getInternalName(aspectClass), false);
            if (cap.isRegistered()) {
                // The capability exists
                return ForgeAspect.of(this, () -> self().getCapability(cap, direction));
            }
        }
        // Default behavior
        return AspectProvider.super.getAspect(aspectClass, direction);
    }

    /**
     * Callback for Forge {@link CapabilityProvider}s to expose Aspects as capabilities.<br><br>
     * If null, the default super implementation of {@link CapabilityProvider#getCapability} should be used.
     */
    @SuppressWarnings("unchecked")
    static @Nullable <A> LazyOptional<A> getCapabilityCallback(AspectProvider provider, Capability<A> cap, @Nullable Direction direction) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            Aspect<A> aspect = (Aspect<A>) provider.getAspect(ItemHandler.class, direction);
            @Nullable A obj = aspect.ifPresent(a -> a);
            if (obj != null) return LazyOptional.of(() -> obj);
            return LazyOptional.empty();
        }

        Class<A> clazz = ForgeNifty.ASPECTS.asAspectClass(cap);
        if (clazz != null) {
            // The capability is a previously registered Aspect
            Aspect<A> aspect = provider.getAspect(clazz, direction);
            @Nullable A obj = aspect.ifPresent(a -> a);
            if (obj != null) return LazyOptional.of(() -> obj);
            return LazyOptional.empty();
        }
        return null;
    }

}
