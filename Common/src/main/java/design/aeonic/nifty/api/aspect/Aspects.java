package design.aeonic.nifty.api.aspect;

import design.aeonic.nifty.api.core.Platform;

public interface Aspects {

    boolean exists(Class<?> aspectClass);

    /**
     * Registers a given Aspect. On Forge, defers to the correct registry event and registers as a Capability.
     * Fabric TBD.
     * @param aspectType the base class of the class/interface to register
     * @param <T> the type
     */
    <T> void registerAspect(Class<T> aspectType);

}
