package design.aeonic.nifty;

import design.aeonic.nifty.api.aspect.Aspect;
import design.aeonic.nifty.api.aspect.AspectProvider;
import design.aeonic.nifty.api.aspect.internal.item.ItemHandler;
import design.aeonic.nifty.api.core.Constants;
import design.aeonic.nifty.impl.aspect.ForgeAspects;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.ServerLifecycleHooks;

@Mod(Constants.NIFTY_ID)
@Mod.EventBusSubscriber(modid = Constants.NIFTY_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeNifty {

    public static final ForgeAspects ASPECTS = (ForgeAspects) Nifty.ASPECTS;

    public ForgeNifty() {
        Nifty.init();

        MinecraftForge.EVENT_BUS.addListener((BlockEvent.NeighborNotifyEvent event) -> {
            ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers().get(0);
            BlockHitResult result = ((BlockHitResult) player.pick(5, 0, true));
            BlockEntity be = player.level.getBlockEntity(result.getBlockPos().below());
            if (be == null) return;

            Aspect<ItemHandler> aspect1 = AspectProvider.cast(be).getAspect(ItemHandler.class, null, false);
            String msg = aspect1.ifPresent(i -> i.getSlot(0).get().toString());
            player.displayClientMessage(new TextComponent(msg == null ? "null" : msg), false);
        });

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ASPECTS.registerAspects);
    }
}
