package design.aeonic.nifty.mixin;

import design.aeonic.nifty.api.aspect.AspectProvider;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Entity.class)
public class EntityMixin implements AspectProvider<Entity> {

    List<Runnable> refreshCallbacks = new ArrayList<>();

    @Override
    public void addRefreshCallback(Runnable callback) {
        refreshCallbacks.add(callback);
    }

    @Override
    public void refreshAspects() {
        refreshCallbacks.forEach(Runnable::run);
    }

    @Inject(method="setRemoved", at=@At("TAIL"))
    void injectSetRemoved(CallbackInfo ci) {
        refreshAspects();
    }

}
