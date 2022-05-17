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

import java.util.HashMap;
import java.util.Map;

public class FabricAspects implements Aspects {

    public static final Map<Class<?>, BlockApiLookup<?, Direction>> BLOCK_LOOKUP_MAP = new HashMap<>();
    public static final Map<Class<?>, EntityApiLookup<?, Direction>> ENTITY_LOOKUP_MAP = new HashMap<>();
    public static final Map<Class<?>, ItemApiLookup<?, Direction>> ITEM_LOOKUP_MAP = new HashMap<>();

    @Override
    public boolean exists(Class<?> aspectClass) {
        return BLOCK_LOOKUP_MAP.containsKey(aspectClass);
    }

    @Override
    public <T> void registerAspect(ResourceLocation id, Class<T> aspectClass) {
        BLOCK_LOOKUP_MAP.computeIfAbsent(aspectClass, clazz -> BlockApiLookup.get(id, clazz, Direction.class));
        ENTITY_LOOKUP_MAP.computeIfAbsent(aspectClass, clazz -> EntityApiLookup.get(id, clazz, Direction.class));
        ITEM_LOOKUP_MAP.computeIfAbsent(aspectClass, clazz -> ItemApiLookup.get(id, clazz, Direction.class));
    }

    // We don't need to cache these lookups, since the actual Aspect object does the caching for us if used properly
    @SuppressWarnings("unchecked")
    @Override
    public <T> Aspect<T> query(Class<T> aspectClass, BlockEntity be, Direction direction) {
        BlockApiLookup<T, Direction> lookup = (BlockApiLookup<T, Direction>) BLOCK_LOOKUP_MAP.get(aspectClass);
        if (lookup != null && be != null) {
            return Aspect.of(() -> lookup.find(be.getLevel(), be.getBlockPos(), be.getBlockState(), be, direction));
        }
        return Aspect.empty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Aspect<T> query(Class<T> aspectClass, Entity entity) {
        EntityApiLookup<T, Direction> lookup = (EntityApiLookup<T, Direction>) ENTITY_LOOKUP_MAP.get(aspectClass);
        if (lookup != null) {
            return Aspect.of(() -> lookup.find(entity, null));
        }
        return Aspect.empty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Aspect<T> query(Class<T> aspectClass, ItemStack stack) {
        ItemApiLookup<T, Direction> lookup = (ItemApiLookup<T, Direction>) ITEM_LOOKUP_MAP.get(aspectClass);
        if (lookup != null) {
            return Aspect.of(() -> lookup.find(stack, null));
        }
        return Aspect.empty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void registerCallback(Class<T> aspectClass, BlockEntityAspectCallback<T> callback) {
        ((BlockApiLookup<T, Direction>) BLOCK_LOOKUP_MAP.get(aspectClass)).registerFallback(((world, pos, state, be, dir) -> be == null || be.isRemoved() ? null : callback.find(be, dir)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void registerCallback(Class<T> aspectClass, EntityAspectCallback<T> callback) {
        ((EntityApiLookup<T, Direction>) ENTITY_LOOKUP_MAP.get(aspectClass)).registerFallback((entity, $) -> entity == null || entity.isRemoved() ? null : callback.find(entity));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void registerCallback(Class<T> aspectClass, ItemStackAspectCallback<T> callback) {
        ((ItemApiLookup<T, Direction>) ITEM_LOOKUP_MAP.get(aspectClass)).registerFallback((stack, $) -> stack == null || stack.isEmpty() ? null : callback.find(stack));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void registerNarrowCallback(Class<T> aspectClass, BlockEntityAspectCallback<T> callback, BlockEntityType<?>... blockEntityTypes) {
        ((BlockApiLookup<T, Direction>) BLOCK_LOOKUP_MAP.get(aspectClass)).registerForBlockEntities((be, dir) -> be == null || be.isRemoved() ? null : callback.find(be, dir), blockEntityTypes);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void registerNarrowCallback(Class<T> aspectClass, EntityAspectCallback<T> callback, EntityType<?>... entityTypes) {
        ((EntityApiLookup<T, Direction>) ENTITY_LOOKUP_MAP.get(aspectClass)).registerForTypes((entity, $) -> entity == null || entity.isRemoved() ? null : callback.find(entity), entityTypes);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void registerNarrowCallback(Class<T> aspectClass, ItemStackAspectCallback<T> callback, ItemLike... items) {
        ((ItemApiLookup<T, Direction>) ITEM_LOOKUP_MAP.get(aspectClass)).registerForItems((stack, $) -> stack == null || stack.isEmpty() ? null : callback.find(stack), items);
    }

}