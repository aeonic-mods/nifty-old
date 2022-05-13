package design.aeonic.nifty.mixin.access;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CapabilityManager.class)
public interface CapabilityManagerAccess {
    @Invoker
    <T> Capability<T> callGet(String realName, boolean registering);
}
