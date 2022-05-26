package design.aeonic.nifty;

import design.aeonic.nifty.api.item.FluidHandler;
import design.aeonic.nifty.api.item.ItemHandler;
import design.aeonic.nifty.api.util.Constants;
import design.aeonic.nifty.impl.aspect.ForgeAspects;
import design.aeonic.nifty.impl.fluid.ForgeFluidHandler;
import design.aeonic.nifty.impl.fluid.IFluidHandlerWrapper;
import design.aeonic.nifty.impl.item.ForgeItemHandler;
import design.aeonic.nifty.impl.item.IItemHandlerWrapper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
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
        ASPECTS.registerWrappedCapability(FluidHandler.class, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, IFluidHandlerWrapper::new, ForgeFluidHandler::new,
                prv -> prv instanceof ItemStack ? CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY : CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);

        // Event listeners
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        // Common
        modBus.addListener(EventPriority.LOWEST, ASPECTS::processAspectCaps);
        modBus.addListener((FMLClientSetupEvent event) -> Nifty.clientInit());
    }

}
