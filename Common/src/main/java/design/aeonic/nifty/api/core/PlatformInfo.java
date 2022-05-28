package design.aeonic.nifty.api.core;

import design.aeonic.nifty.api.util.Platform;

import java.util.Optional;

/**
 * Contains information about the current platform.
 */
public interface PlatformInfo {

    Platform getPlatform();

    default boolean isRunningDatagen() {
        return false;
    }

    boolean isModLoaded(String modId);

    Optional<String> getModDisplayName(String modId);

    boolean isDevelopmentEnvironment();

}
