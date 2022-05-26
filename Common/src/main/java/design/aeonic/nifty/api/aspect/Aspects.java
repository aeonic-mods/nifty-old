package design.aeonic.nifty.api.aspect;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import javax.annotation.Nullable;

public interface Aspects {

    /**
     * Checks whether a given class has been registered as an Aspect.
     */
    boolean exists(Class<?> aspectClass);

    /**
     * Registers a given Aspect.<br><br>
     * On Forge, defers to the correct registry event and registers as a Capability.
     * On Fabric, creates an API lookup object mapped to the interface.<br><br>
     * The resource location is unused on Forge (for now), but must be provided and must be unique to this Aspect.
     *
     * @param aspectClass the base class of the class/interface to register
     * @param <T>         the type
     */
    <T> void registerAspect(ResourceLocation id, Class<T> aspectClass);

    /**
     * Gets an Aspect of the given type from the given block entity. If it doesn't have one, you'll receive
     * an empty Aspect.<br><br>
     * The returned Aspect is safe to store (and in fact should be stored to avoid extra queries) as a cache for the
     * object you're trying to obtain. Whenever it becomes unavailable or changes, the Aspect object will refresh its
     * contained value and {@link Aspect#resolve()} will return the new object (or an empty Optional if it is no longer available).<br><br>
     * Thus, you should always keep any usage of returned aspects within a call to {@link Aspect#ifPresent} or similar.
     *
     * @param aspectClass the base class of the Aspect (the one passed to {@link #registerAspect}
     * @param be          the block entity
     * @param direction   the side of the block entity to query
     * @return an Aspect potentially wrapping the given object type
     */
    <T> Aspect<T> query(Class<T> aspectClass, BlockEntity be, Direction direction);

    /**
     * Gets an Aspect of the given type from the given entity. If it doesn't have one, you'll receive an empty Aspect.<br><br>
     * The returned Aspect is safe to store (and in fact should be stored to avoid extra queries) as a cache for the
     * object you're trying to obtain. Whenever it becomes unavailable or changes, the Aspect object will refresh its
     * contained value and {@link Aspect#resolve()} will return the new object (or an empty Optional if it is no longer available).<br><br>
     * Thus, you should always keep any usage of returned aspects within a call to {@link Aspect#ifPresent} or similar.
     *
     * @param aspectClass the base class of the Aspect (the one passed to {@link #registerAspect}
     * @param entity      the entity
     * @return an Aspect potentially wrapping the given object type
     */
    <T> Aspect<T> query(Class<T> aspectClass, Entity entity);

    /**
     * Gets an Aspect of the given type from the given item stack. If it doesn't have one, you'll receive an empty Aspect.<br><br>
     * The returned Aspect is safe to store (and in fact should be stored to avoid extra queries) as a cache for the
     * object you're trying to obtain. Whenever it becomes unavailable or changes, the Aspect object will refresh its
     * contained value and {@link Aspect#resolve()} will return the new object (or an empty Optional if it is no longer available).<br><br>
     * Thus, you should always keep any usage of returned aspects within a call to {@link Aspect#ifPresent} or similar.
     *
     * @param aspectClass the base class of the Aspect (the one passed to {@link #registerAspect}
     * @param stack       the item stack
     * @return an Aspect potentially wrapping the given object type
     */
    <T> Aspect<T> query(Class<T> aspectClass, ItemStack stack);

    /**
     * Registers callbacks used to find Aspects given a blockentity. The callback can return null; in
     * practice, the return from these callbacks will always be wrapped in an {@link Aspect} object for caching
     * (when called from Nifty). External mods accessing these objects will get whatever integrates with their platform
     * - on Fabric, just the object from an ApiLookup call; on Forge, a LazyOptional.<br><br>
     * {@link #registerNarrowLookup(Class, BlockEntityAspectLookup, BlockEntityType[])}  should be used if you're only
     * exposing this aspect from specific items.
     * Using this method will result in more queries overall, and in turn worse performance.<br><br>
     * Regarding callbacks: if the passed direction is null, the aspect should be exposed regardless of what directions
     * you want to limit it to for full compat with Forge capabilities.<br><br>
     * The Aspect must be registered first via {@link #registerAspect}.
     *
     * @param aspectClass the base class of the aspect to register this callback for
     */
    <T> void registerLookup(Class<T> aspectClass, BlockEntityAspectLookup<T> callbacks);

    /**
     * Registers callbacks used to find Aspects given an entity. The callback can return null; in
     * practice, the return from these callbacks will always be wrapped in an {@link Aspect} object for caching
     * (when called from Nifty). External mods accessing these objects will get whatever integrates with their platform
     * - on Fabric, just the object from an ApiLookup call; on Forge, a LazyOptional.<br><br>
     * {@link #registerNarrowLookup(Class, EntityAspectLookup, EntityType[])}  should be used if you're only
     * exposing this aspect from specific items.
     * Using this method will result in more queries overall, and in turn worse performance.<br><br>
     * The Aspect must be registered first via {@link #registerAspect}.
     *
     * @param aspectClass the base class of the aspect to register this callback for
     */
    <T> void registerLookup(Class<T> aspectClass, EntityAspectLookup<T> callbacks);

    /**
     * Registers callbacks used to find Aspects given an item stack. The callback can return null; in
     * practice, the return from these callbacks will always be wrapped in an {@link Aspect} object for caching
     * (when called from Nifty). External mods accessing these objects will get whatever integrates with their platform
     * - on Fabric, just the object from an ApiLookup call; on Forge, a LazyOptional.<br><br>
     * {@link #registerNarrowLookup(Class, ItemStackAspectLookup, ItemLike...)} should be used if you're only
     * exposing this aspect from specific items.
     * Using this method will result in more queries overall, and in turn worse performance.<br><br>
     * The Aspect must be registered first via {@link #registerAspect}.
     *
     * @param aspectClass the base class of the aspect to register this callback for
     */
    <T> void registerLookup(Class<T> aspectClass, ItemStackAspectLookup<T> callbacks);

    /**
     * Registers callbacks used to find Aspects given a blockentity, only for a specific blockentity type. Results in
     * more performant queries in general than {@link #registerLookup}.<br><br>
     * Regarding callbacks: if the passed direction is null, the aspect should be exposed regardless of what directions
     * you want to limit it to for full compat with Forge capabilities.
     * The Aspect must be registered first via {@link #registerAspect}.
     *
     * @param aspectClass the base class of the aspect to register this callback for
     */
    <T> void registerNarrowLookup(Class<T> aspectClass, BlockEntityAspectLookup<T> callback, BlockEntityType<?>... blockEntityTypes);

    /**
     * Registers callbacks used to find Aspects given an entity, only for a specific entity type. Results in
     * more performant queries in general than {@link #registerLookup}.<br><br>
     * The Aspect must be registered first via {@link #registerAspect}.
     *
     * @param aspectClass the base class of the aspect to register this callback for
     */
    <T> void registerNarrowLookup(Class<T> aspectClass, EntityAspectLookup<T> callback, EntityType<?>... entityTypes);

    /**
     * Registers callbacks used to find Aspects given an item stack, only for a specific item type. Results in
     * more performant queries in general than {@link #registerLookup}.<br><br>
     * The Aspect must be registered first via {@link #registerAspect}.
     *
     * @param aspectClass the base class of the aspect to register this callback for
     */
    <T> void registerNarrowLookup(Class<T> aspectClass, ItemStackAspectLookup<T> callback, ItemLike... items);

    @FunctionalInterface
    interface ItemStackAspectLookup<T> {
        @Nullable
        T find(ItemStack stack);
    }

    @FunctionalInterface
    interface BlockEntityAspectLookup<T> {
        @Nullable
        T find(BlockEntity be, @Nullable Direction direction);
    }

    @FunctionalInterface
    interface EntityAspectLookup<T> {
        @Nullable
        T find(Entity entity);
    }

}
