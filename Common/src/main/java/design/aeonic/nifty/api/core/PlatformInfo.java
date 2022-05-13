package design.aeonic.nifty.api.core;

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
