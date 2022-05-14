package design.aeonic.nifty.api.aspect;

import design.aeonic.nifty.api.aspect.internal.item.ItemHandler;
import design.aeonic.nifty.api.aspect.internal.item.slot.AbstractSlot;

public interface Aspects {

    boolean exists(Class<?> aspectClass);

    /**
     * Registers a given Aspect. On Forge, defers to the correct registry event and registers as a Capability.
     * Fabric TBD.
     *
     * @param aspectType the base class of the class/interface to register
     * @param <T>        the type
     */
    <T> void registerAspect(Class<T> aspectType);

    /**
     * Creates a new item handler, providing the correct implementation for the current platform.
     *
     * @param slots the slots describing this handler's behavior
     * @return the new item handler
     */
    ItemHandler itemHandler(AbstractSlot... slots);

}
