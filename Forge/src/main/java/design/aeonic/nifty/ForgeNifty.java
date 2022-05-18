package design.aeonic.nifty;

import design.aeonic.nifty.api.aspect.internal.ItemHandler;
import design.aeonic.nifty.api.util.Constants;
import design.aeonic.nifty.impl.aspect.ForgeAspects;
import design.aeonic.nifty.impl.item.ForgeItemHandler;
import design.aeonic.nifty.impl.item.IItemHandlerWrapper;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.CapabilityItemHandler;

@Mod(Constants.NIFTY_ID)
@Mod.EventBusSubscriber(modid = Constants.NIFTY_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeNifty {

    public static final ForgeAspects ASPECTS = (ForgeAspects) Nifty.ASPECTS;

    public ForgeNifty() {
        Nifty.init();

        // Capability -> Aspect wrapping and vice-versa
        ASPECTS.registerWrappedCapability(ItemHandler.class, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, IItemHandlerWrapper::new, ForgeItemHandler::new);

        // Event listeners
        FMLJavaModLoadingContext.get().getModEventBus().addListener(EventPriority.LOWEST, ASPECTS::processAspectCaps);
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLClientSetupEvent event) -> Nifty.clientInit());
    }

}
