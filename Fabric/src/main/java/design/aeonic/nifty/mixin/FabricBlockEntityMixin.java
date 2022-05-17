package design.aeonic.nifty.mixin;

import design.aeonic.nifty.api.aspect.AspectProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(BlockEntity.class)
public class FabricBlockEntityMixin implements AspectProvider<BlockEntity> {

    List<Runnable> refreshCallbacks = new ArrayList<>();

    @Override
    public void addRefreshCallback(Runnable callback) {
        refreshCallbacks.add(callback);
    }

    @Override
    public void refreshAspects() {
        refreshCallbacks.forEach(Runnable::run);
    }

    @Inject(method="setRemoved()V", at=@At("HEAD"))
    void injectSetRemoved(CallbackInfo ci) {
        refreshAspects();
    }

}
