package design.aeonic.nifty.impl.core;

import design.aeonic.nifty.api.util.Platform;
import design.aeonic.nifty.api.core.PlatformInfo;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ForgePlatformInfo implements PlatformInfo {

    @Override
    public Platform getPlatform() {
        return Platform.FORGE;
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public Optional<String> getModDisplayName(String modId) {
        if (isModLoaded(modId)) {
            Optional<? extends ModContainer> container = ModList.get().getModContainerById(modId);
            if (container.isPresent()) {
                return Optional.of(container.get().getModInfo().getDisplayName());
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public boolean isRunningDatagen() {
        return DatagenModLoader.isRunningDataGen();
    }
}
