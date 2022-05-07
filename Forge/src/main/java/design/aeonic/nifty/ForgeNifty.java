package design.aeonic.nifty;

import design.aeonic.nifty.api.core.Constants;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.NIFTY_ID)
@Mod.EventBusSubscriber(modid = Constants.NIFTY_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeNifty {
    public static final IEventBus MOD_BUS = FMLJavaModLoadingContext.get().getModEventBus();

    public ForgeNifty() {
        Nifty.init();
    }
}