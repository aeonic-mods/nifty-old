package design.aeonic.nifty.impl;

import design.aeonic.nifty.api.core.Constants;

import java.util.ServiceLoader;

public class Services {

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Constants.NIFTY_LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
