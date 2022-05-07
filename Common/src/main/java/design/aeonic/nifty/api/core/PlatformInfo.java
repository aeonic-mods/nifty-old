package design.aeonic.nifty.api.core;

import java.util.Objects;

/**
 * Contains information about the current platform.
 */
public interface PlatformInfo {

    default boolean isFabric() {
        return !isForge();
    }

    default boolean isForge() {
        return Objects.equals(getPlatformName(), "Forge");
    }

    String getPlatformName();

    default boolean isRunningDatagen() {
        return false;
    }

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();
}
