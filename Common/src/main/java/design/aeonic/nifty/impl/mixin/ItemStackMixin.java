package design.aeonic.nifty.impl.mixin;

import design.aeonic.nifty.api.aspect.AspectProvider;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements AspectProvider<ItemStack> {

    List<Runnable> refreshCallbacks = new ArrayList<>();

    @Override
    public void addRefreshCallback(Runnable callback) {
        refreshCallbacks.add(callback);
    }

    @Override
    public void refreshAspects() {
        refreshCallbacks.forEach(Runnable::run);
    }

    @Inject(method = "setCount(I)V", at = @At("TAIL"))
    void injectSetCount(int count, CallbackInfo ci) {
        if (count != this.count) {
            refreshAspects();
        }
    }

    @Inject(method = "onDestroyed(Lnet/minecraft/world/entity/item/ItemEntity;)V", at = @At("TAIL"))
    void injectOnDestroyed(CallbackInfo ci) {
        refreshAspects();
    }

    @Shadow
    private int count;

}
