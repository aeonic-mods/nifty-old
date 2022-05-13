package design.aeonic.nifty.services;

import design.aeonic.nifty.api.core.Platform;
import design.aeonic.nifty.api.core.PlatformInfo;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import javax.annotation.ParametersAreNonnullByDefault;

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
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    @Override
    public boolean isRunningDatagen() {
        return DatagenModLoader.isRunningDataGen();
    }
}
