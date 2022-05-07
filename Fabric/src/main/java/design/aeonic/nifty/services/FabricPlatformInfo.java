package design.aeonic.nifty.services;

import design.aeonic.nifty.api.core.PlatformInfo;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatformInfo implements PlatformInfo {

    @Override
    public String getPlatformName() {
        return "Fabric";
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
