package design.aeonic.nifty.impl.core;

import design.aeonic.nifty.api.core.Platform;
import design.aeonic.nifty.api.core.PlatformInfo;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatformInfo implements PlatformInfo {

    @Override
    public Platform getPlatform() {
        return Platform.FABRIC;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
