package design.aeonic.nifty.api.core;

import design.aeonic.nifty.api.util.Platform;

/**
 * Contains information about the current platform.
 */
public interface PlatformInfo {

    Platform getPlatform();

    default boolean isRunningDatagen() {
        return false;
    }

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();
}
