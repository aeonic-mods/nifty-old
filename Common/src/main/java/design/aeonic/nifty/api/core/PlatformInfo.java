package design.aeonic.nifty.api.core;

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
