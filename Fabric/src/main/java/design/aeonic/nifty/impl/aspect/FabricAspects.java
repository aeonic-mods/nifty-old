package design.aeonic.nifty.impl.aspect;

import design.aeonic.nifty.api.aspect.Aspect;
import design.aeonic.nifty.api.aspect.Aspects;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.entity.EntityApiLookup;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class FabricAspects implements Aspects {

    private final Map<Class<?>, BlockApiLookup<?, Direction>> blockLookupMap = new HashMap<>();
    private final Map<Class<?>, EntityApiLookup<?, Direction>> entityLookupMap = new HashMap<>();
    private final Map<Class<?>, ItemApiLookup<?, Direction>> itemLookupMap = new HashMap<>();

    @Override
    public boolean exists(Class<?> aspectClass) {
        return blockLookupMap.containsKey(aspectClass);
    }

    @Override
    public <T> void registerAspect(ResourceLocation id, Class<T> aspectClass) {
        blockLookupMap.computeIfAbsent(aspectClass, clazz -> BlockApiLookup.get(id, clazz, Direction.class));
        entityLookupMap.computeIfAbsent(aspectClass, clazz -> EntityApiLookup.get(id, clazz, Direction.class));
        itemLookupMap.computeIfAbsent(aspectClass, clazz -> ItemApiLookup.get(id, clazz, Direction.class));
    }

    // We don't need to cache these lookups, since the actual Aspect object does the caching for us if used properly
    @SuppressWarnings("unchecked")
    @Override
    public <T> Aspect<T> query(Class<T> aspectClass, @Nullable BlockEntity be, @Nullable Direction direction) {
        BlockApiLookup<T, Direction> lookup = (BlockApiLookup<T, Direction>) blockLookupMap.get(aspectClass);
        if (lookup != null && be != null) {
            return Aspect.of(() -> lookup.find(be.getLevel(), be.getBlockPos(), be.getBlockState(), be, direction));
        }
        return Aspect.empty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Aspect<T> query(Class<T> aspectClass, Entity entity) {
        EntityApiLookup<T, Direction> lookup = (EntityApiLookup<T, Direction>) entityLookupMap.get(aspectClass);
        if (lookup != null) {
            return Aspect.of(() -> lookup.find(entity, null));
        }
        return Aspect.empty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Aspect<T> query(Class<T> aspectClass, ItemStack stack) {
        ItemApiLookup<T, Direction> lookup = (ItemApiLookup<T, Direction>) itemLookupMap.get(aspectClass);
        if (lookup != null) {
            return Aspect.of(() -> lookup.find(stack, null));
        }
        return Aspect.empty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void registerLookup(Class<T> aspectClass, BlockEntityAspectLookup<T> callback) {
        ((BlockApiLookup<T, Direction>) blockLookupMap.get(aspectClass)).registerFallback(((world, pos, state, be, dir) -> be == null || be.isRemoved() ? null : callback.find(be, dir)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void registerLookup(Class<T> aspectClass, EntityAspectLookup<T> callback) {
        ((EntityApiLookup<T, Direction>) entityLookupMap.get(aspectClass)).registerFallback((entity, $) -> entity == null || entity.isRemoved() ? null : callback.find(entity));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void registerLookup(Class<T> aspectClass, ItemStackAspectLookup<T> callback) {
        ((ItemApiLookup<T, Direction>) itemLookupMap.get(aspectClass)).registerFallback((stack, $) -> stack == null || stack.isEmpty() ? null : callback.find(stack));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void registerNarrowLookup(Class<T> aspectClass, BlockEntityAspectLookup<T> callback, BlockEntityType<?>... blockEntityTypes) {
        ((BlockApiLookup<T, Direction>) blockLookupMap.get(aspectClass)).registerForBlockEntities((be, dir) -> be == null || be.isRemoved() ? null : callback.find(be, dir), blockEntityTypes);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void registerNarrowLookup(Class<T> aspectClass, EntityAspectLookup<T> callback, EntityType<?>... entityTypes) {
        ((EntityApiLookup<T, Direction>) entityLookupMap.get(aspectClass)).registerForTypes((entity, $) -> entity == null || entity.isRemoved() ? null : callback.find(entity), entityTypes);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void registerNarrowLookup(Class<T> aspectClass, ItemStackAspectLookup<T> callback, ItemLike... items) {
        ((ItemApiLookup<T, Direction>) itemLookupMap.get(aspectClass)).registerForItems((stack, $) -> stack == null || stack.isEmpty() ? null : callback.find(stack), items);
    }

}
