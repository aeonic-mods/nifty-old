package design.aeonic.nifty.mixin;

import design.aeonic.nifty.aspect.ForgeAspectProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(BlockEntity.class)
public abstract class ForgeBlockEntityMixin extends CapabilityProvider<BlockEntity> implements ForgeAspectProvider<BlockEntity> {

    private final List<Runnable> onRefresh = new ArrayList<>();
    private final Map<Capability<?>, LazyOptional<?>> capabilityCache = new HashMap<>();

    // Just here so we can compile
    protected ForgeBlockEntityMixin(Class<BlockEntity> baseClass) {
        super(baseClass);
    }

    @Override
    public void onRefreshAspects(Runnable callback) {
        onRefresh.add(callback);
    }

    @Override
    public void refreshAspects() {
        onRefresh.forEach(Runnable::run);
    }

    @Override
    public BlockEntity self() {
        return (BlockEntity) ((Object) this);
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        // Forge Capability contract expects us to cache these
        @Nullable LazyOptional<T> maybeCap = (LazyOptional<T>) capabilityCache.computeIfAbsent(cap, c -> ForgeAspectProvider.getCapabilityCallback(this, c, side));
        if (maybeCap != null) return maybeCap;
        return super.getCapability(cap, side);
    }

}
