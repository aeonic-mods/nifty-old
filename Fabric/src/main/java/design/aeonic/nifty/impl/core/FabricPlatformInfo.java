package design.aeonic.nifty.impl.core;

import design.aeonic.nifty.api.core.Platform;
import design.aeonic.nifty.api.core.PlatformInfo;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.Optional;

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
    public Optional<String> getModDisplayName(String modId) {
        if (isModLoaded(modId)) {
            Optional<ModContainer> container = FabricLoader.getInstance().getModContainer(modId);
            if (container.isPresent()) {
                return Optional.of(container.get().getMetadata().getName());
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
