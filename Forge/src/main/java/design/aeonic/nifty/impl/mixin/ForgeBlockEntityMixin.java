package design.aeonic.nifty.impl.mixin;

import design.aeonic.nifty.ForgeNifty;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mixin(BlockEntity.class)
public class ForgeBlockEntityMixin extends CapabilityProvider<BlockEntity> {

    protected ForgeBlockEntityMixin(Class baseClass) {
        super(baseClass);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        LazyOptional<T> opt = ForgeNifty.ASPECTS.queryAsCap((CapabilityProvider<?>) ((Object) this), cap, side);
        if (opt != null)
            return opt;
        return super.getCapability(cap, side);
    }

}
