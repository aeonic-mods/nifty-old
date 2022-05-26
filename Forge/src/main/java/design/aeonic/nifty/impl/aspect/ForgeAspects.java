package design.aeonic.nifty.impl.aspect;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import design.aeonic.nifty.api.aspect.Aspect;
import design.aeonic.nifty.api.aspect.AspectProvider;
import design.aeonic.nifty.api.aspect.Aspects;
import design.aeonic.nifty.impl.mixin.access.CapabilityManagerAccess;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import org.objectweb.asm.Type;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;


public class ForgeAspects implements Aspects {
    // Somewhat messy implementation, may rewrite later

    private final List<Class<?>> queue = new ArrayList<>();

    /**
     * The type of the key class and the value capability might not be the same in the case of wrapping Aspects.
     * Caster no casting!
     */
    private final BiMap<Class<?>, Capability<?>> aspectCaps = HashBiMap.create();

    private final Multimap<Class<?>, BlockEntityAspectLookup<?>> blockLookups = LinkedHashMultimap.create();
    private final Multimap<Class<?>, EntityAspectLookup<?>> entityLookups = LinkedHashMultimap.create();
    private final Multimap<Class<?>, ItemStackAspectLookup<?>> itemLookups = LinkedHashMultimap.create();

    // Map with fallbacks for capabilities that already exist but need to be wrapped
    private final Map<Class<?>, BiFunction<CapabilityProvider<?>, Direction, Aspect<?>>> capToAspectLookups = new HashMap<>();
    // Map with functions for wrapping a registered Aspect to a preexisting capability interface
    private final Map<Capability<?>, Function<?, ?>> aspectToCapWrappers = new HashMap<>();

    @Override
    public boolean exists(Class<?> aspectClass) {
        return aspectCaps.containsKey(aspectClass);
    }

    @Override
    public <T> void registerAspect(ResourceLocation id, Class<T> aspectClass) {
        queue.add(aspectClass);
    }

    /**
     * Register an Aspect that, when queried, wraps the return from {@link CapabilityProvider#getCapability} with
     * a preexisting capability via the given wrapping function. Also wraps getCapability calls with the second wrapping
     * function.
     */
    public <A, C> void registerWrappedCapability(Class<A> aspectClass, Capability<C> capability, Function<C, A> capToAspectWrapper, Function<A, C> aspectToCapWrapper) {
        aspectCaps.put(aspectClass, capability);
        aspectToCapWrappers.put(capability, aspectToCapWrapper);
        capToAspectLookups.put(aspectClass, (provider, direction) ->
                new ForgeAspect.Wrapping<>(() -> provider.getCapability(capability, direction), capToAspectWrapper));
    }

    /**
     * Used like {@link #registerWrappedCapability(Class, Capability, Function, Function)} for interfaces that might
     * have multiple caps, such as fluid handlers which have a separate itemstack capability.
     */
    public <A, C> void registerWrappedCapability(Class<A> aspectClass, Capability<C> capability, Function<C, A> capToAspectWrapper,
                                                 Function<A, C> aspectToCapWrapper, Function<CapabilityProvider<?>, Capability<? extends C>> whichCap) {
        aspectCaps.put(aspectClass, capability);
        aspectToCapWrappers.put(capability, aspectToCapWrapper);
        capToAspectLookups.put(aspectClass, (provider, direction) ->
                new ForgeAspect.Wrapping<>(() -> provider.getCapability(whichCap.apply(provider), direction), capToAspectWrapper));
    }

    public void processAspectCaps(RegisterCapabilitiesEvent event) {
        queue.forEach(aspect -> processAspectCap(event, aspect));
        queue.clear();
    }

    private <T> void processAspectCap(RegisterCapabilitiesEvent event, Class<T> aspectClass) {
        // If the Aspect was already registered with a wrapper etc don't overwrite anything
        if (aspectCaps.containsKey(aspectClass)) return;

        Capability<T> cap = ((CapabilityManagerAccess) ((Object) CapabilityManager.INSTANCE))
                .callGet(Type.getInternalName(aspectClass), false);

        if (cap.isRegistered())
            // Too much brain fog to figure out if I actually need this, but worst case it's just unnecessary
            // and should overwrite the old entry with the same value as only one Capability exists per class
            aspectCaps.put(aspectClass, cap);
        else {
            // Capability not registered; check if it has already been put in the wrapping lookups
            if (capToAspectLookups.containsKey(aspectClass)) {
                // Not in the wrapped callbacks; register a new capability
                aspectCaps.put(aspectClass, cap);
                event.register(aspectClass);
            }
        }
    }

    /**
     * Query Aspects for a capability from {@link CapabilityProvider#getCapability} calls. Returns null if no object
     * was found, in which case the callback should use default behavior (super call, etc).
     */
    @Nullable
    public <C> LazyOptional<C> queryAsCap(CapabilityProvider<?> provider, Capability<C> capability, Direction direction) {
        Class<?> aspectClass = aspectCaps.inverse().get(capability);
        if (aspectClass != null) {
            return queryAsCap(provider, aspectClass, capability, direction);
        }
        return null;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    private <A, C> LazyOptional<C> queryAsCap(CapabilityProvider<?> provider, Class<A> aspectClass, Capability<C> capability, Direction direction) {
        // Don't need to worry about adding invalidate callbacks; anything returned from the capability provider will be
        // invalidated when necessary by Forge
        if (provider instanceof BlockEntity be) {
            Supplier<A> supplier = queryInternal(aspectClass, be, direction);
            if (supplier != null && supplier.get() != null) {
                if (aspectToCapWrappers.containsKey(capability)) {
                    return LazyOptional.of(() -> ((Function<A, C>) aspectToCapWrappers.get(capability)).apply(supplier.get()));
                } else {
                    return LazyOptional.of(() -> (C) supplier.get());
                }
            }
        }
        else if (provider instanceof Entity entity) {
            Supplier<A> supplier = queryInternal(aspectClass, entity);
            if (supplier != null && supplier.get() != null) {
                if (aspectToCapWrappers.containsKey(capability)) {
                    return LazyOptional.of(() -> ((Function<A, C>) aspectToCapWrappers.get(capability)).apply(supplier.get()));
                } else {
                    return LazyOptional.of(() -> (C) supplier.get());
                }
            }
        }
        else if (provider instanceof ItemStack stack) {
            Supplier<A> supplier = queryInternal(aspectClass, stack);
            if (supplier != null && supplier.get() != null) {
                if (aspectToCapWrappers.containsKey(capability)) {
                    return LazyOptional.of(() -> ((Function<A, C>) aspectToCapWrappers.get(capability)).apply(supplier.get()));
                } else {
                    return LazyOptional.of(() -> (C) supplier.get());
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Aspect<T> query(Class<T> aspectClass, BlockEntity be, Direction direction) {
        if (be == null) return Aspect.empty();
        Supplier<T> supplier = queryInternal(aspectClass, be, direction);
        if (supplier != null) {
            Aspect<T> aspect = Aspect.of(supplier);
            AspectProvider.cast(be).addRefreshCallback(aspect::refresh);
            return aspect;
        }
        if (capToAspectLookups.containsKey(aspectClass)) {
            return (Aspect<T>) capToAspectLookups.get(aspectClass).apply(be, direction);
        }
        // Ok to cast value in aspectCaps since we've already covered the edge cases above
        // Also, ForgeAspect takes care of its own refreshing from the lazy optional so don't need to worry about that
        return new ForgeAspect<>(() -> be.getCapability((Capability<T>) aspectCaps.get(aspectClass)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Aspect<T> query(Class<T> aspectClass, Entity entity) {
        if (entity == null) return Aspect.empty();
        Supplier<T> supplier = queryInternal(aspectClass, entity);
        if (supplier != null) {
            Aspect<T> aspect = Aspect.of(supplier);
            AspectProvider.cast(entity).addRefreshCallback(aspect::refresh);
            return aspect;
        }
        if (capToAspectLookups.containsKey(aspectClass)) {
            return (Aspect<T>) capToAspectLookups.get(aspectClass).apply(entity, null);
        }
        return new ForgeAspect<>(() -> entity.getCapability((Capability<T>) aspectCaps.get(aspectClass)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Aspect<T> query(Class<T> aspectClass, ItemStack stack) {
        if (stack == null) return Aspect.empty();
        Supplier<T> supplier = queryInternal(aspectClass, stack);
        if (supplier != null) {
            Aspect<T> aspect = Aspect.of(supplier);
            AspectProvider.cast(stack).addRefreshCallback(aspect::refresh);
            return aspect;
        }
        if (capToAspectLookups.containsKey(aspectClass)) {
            return (Aspect<T>) capToAspectLookups.get(aspectClass).apply(stack, null);
        }
        return new ForgeAspect<>(() -> stack.getCapability((Capability<T>) aspectCaps.get(aspectClass)));
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> Supplier<T> queryInternal(Class<T> aspectClass, BlockEntity be, Direction direction) {
        if (blockLookups.containsKey(aspectClass)) {
            for (BlockEntityAspectLookup<?> lookup : blockLookups.get(aspectClass)) {
                if (((BlockEntityAspectLookup<T>) lookup).find(be, direction) != null) {
                    return () -> ((BlockEntityAspectLookup<T>) lookup).find(be, direction);
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> Supplier<T> queryInternal(Class<T> aspectClass, Entity entity) {
        if (entityLookups.containsKey(aspectClass)) {
            for (EntityAspectLookup<?> lookup : entityLookups.get(aspectClass)) {
                if (((EntityAspectLookup<T>) lookup).find(entity) != null) {
                    return () -> ((EntityAspectLookup<T>) lookup).find(entity);
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> Supplier<T> queryInternal(Class<T> aspectClass, ItemStack stack) {
        if (itemLookups.containsKey(aspectClass)) {
            for (ItemStackAspectLookup<?> lookup : itemLookups.get(aspectClass)) {
                if (((ItemStackAspectLookup<T>) lookup).find(stack) != null) {
                    return () -> ((ItemStackAspectLookup<T>) lookup).find(stack);
                }
            }
        }
        return null;
    }

    @Override
    public <T> void registerLookup(Class<T> aspectClass, BlockEntityAspectLookup<T> callback) {
        blockLookups.put(aspectClass, callback);
    }

    @Override
    public <T> void registerLookup(Class<T> aspectClass, EntityAspectLookup<T> callback) {
        entityLookups.put(aspectClass, callback);
    }

    @Override
    public <T> void registerLookup(Class<T> aspectClass, ItemStackAspectLookup<T> callback) {
        itemLookups.put(aspectClass, callback);
    }

    // The narrow callbacks here try to insert at the beginning of the map's values so they're called
    // before the fallbacks, though I'm not sure if it even works...
    // TODO: Profiling, see if this actually does what it should + has a worthwhile performance increase

    @Override
    public <T> void registerNarrowLookup(Class<T> aspectClass, BlockEntityAspectLookup<T> callback, BlockEntityType<?>... blockEntityTypes) {
        List<BlockEntityType<?>> types = Arrays.asList(blockEntityTypes);
        addOrInsert(blockLookups.get(aspectClass), (be, dir) -> be == null || !types.contains(be.getType()) ? null : callback.find(be, dir));
    }

    @Override
    public <T> void registerNarrowLookup(Class<T> aspectClass, EntityAspectLookup<T> callback, EntityType<?>... entityTypes) {
        List<EntityType<?>> types = Arrays.asList(entityTypes);
        addOrInsert(entityLookups.get(aspectClass), entity -> entity == null || !types.contains(entity.getType()) ? null : callback.find(entity));
    }

    @Override
    public <T> void registerNarrowLookup(Class<T> aspectClass, ItemStackAspectLookup<T> callback, ItemLike... items) {
        List<ItemLike> itemList = Arrays.asList(items);
        addOrInsert(itemLookups.get(aspectClass), stack -> stack == null || !itemList.contains(stack.getItem()) ? null : callback.find(stack));
    }

    private <T> void addOrInsert(Collection<T> collection, T object) {
        if (collection instanceof List<T> list) {
            list.add(0, object);
        } else {
            collection.add(object);
        }
    }

}
