package design.aeonic.nifty;

import design.aeonic.nifty.api.energy.EnergyHandler;
import design.aeonic.nifty.api.item.FluidHandler;
import design.aeonic.nifty.api.item.ItemHandler;
import design.aeonic.nifty.impl.aspect.FabricAspects;
import design.aeonic.nifty.impl.energy.EnergyStorageWrapper;
import design.aeonic.nifty.impl.energy.RebornEnergyHandler;
import design.aeonic.nifty.impl.fluid.FabricFluidHandler;
import design.aeonic.nifty.impl.fluid.FluidVariantStorageWrapper;
import design.aeonic.nifty.impl.item.FabricItemHandler;
import design.aeonic.nifty.impl.item.ItemVariantStorageWrapper;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.EnergyStorageUtil;
import team.reborn.energy.impl.EnergyImpl;

import java.util.function.Supplier;

public class FabricNifty {

    public static final FabricAspects ASPECTS = (FabricAspects) Nifty.ASPECTS;

    public void initializeCommon() {
        Nifty.init();

        // FIXME: Separate lookups for external -> internal only

        // Expose blocks using the Fabric transfer API to Nifty's item handler lookup
        ASPECTS.registerExternalLookup(ItemHandler.class, (be, dir) -> {
            if (be == null || be.getLevel() == null) return null;
            Storage<ItemVariant> storage = ItemStorage.SIDED.find(be.getLevel(), be.getBlockPos(), be.getBlockState(), be,
                    dir == null ? Direction.UP : dir);
            if (storage == null) return null;
            return new ItemVariantStorageWrapper(storage);
        });

        // Expose blocks using the Fabric transfer API to Nifty's fluid handler lookup
        ASPECTS.registerExternalLookup(FluidHandler.class, (be, dir) -> {
            if (be == null || be.getLevel() == null) return null;
            Storage<FluidVariant> storage = FluidStorage.SIDED.find(be.getLevel(), be.getBlockPos(), be.getBlockState(), be,
                    dir == null ? Direction.UP : dir);
            if (storage == null) return null;
            return new FluidVariantStorageWrapper(storage);
        });
        ASPECTS.registerExternalLookup(FluidHandler.class, (ItemStack stack) -> {
            Storage<FluidVariant> storage = FluidStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
            if (storage == null) return null;
            return new FluidVariantStorageWrapper(storage);
        });

        // Expose blocks using Team Reborn's Energy API to Nifty's energy handler lookup
        ASPECTS.registerExternalLookup(EnergyHandler.class, (be, dir) -> {
            if (be == null || be.getLevel() == null) return null;
            EnergyStorage storage = EnergyStorage.SIDED.find(be.getLevel(), be.getBlockPos(),
                    dir == null ? Direction.UP : dir);
            return new EnergyStorageWrapper(storage);
        });
        ASPECTS.registerExternalLookup(EnergyHandler.class, (ItemStack stack) -> {
            EnergyStorage storage = EnergyStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
            if (storage == null) return null;
            return new EnergyStorageWrapper(storage);
        });

        // Expose Nifty's item handlers to the Fabric transfer API
        ItemStorage.SIDED.registerFallback((level, pos, state, be, dir) -> {
            if (be == null) return null;
            Supplier<ItemHandler> sup = ASPECTS.queryInternal(ItemHandler.class, be, dir);
            if (sup != null) return (FabricItemHandler) sup.get();
            return null;
        });

        // Expose Nifty's fluid handlers to the Fabric transfer API
        FluidStorage.SIDED.registerFallback((level, pos, state, be, dir) -> {
            if (be == null) return null;
            Supplier<FluidHandler> sup = ASPECTS.queryInternal(FluidHandler.class, be, dir);
            if (sup != null) return (FabricFluidHandler) sup.get();
            return null;
        });
        FluidStorage.ITEM.registerFallback((stack, ctx) -> {
            Supplier<FluidHandler> sup = ASPECTS.queryInternal(FluidHandler.class, stack);
            if (sup != null) return (FabricFluidHandler) sup.get();
            return null;
        });

        // Expose Nifty's energy handlers to Team Reborn's Energy API
        EnergyStorage.SIDED.registerFallback((level, pos, state, be, dir) -> {
            if (be == null) return null;
            Supplier<EnergyHandler> sup = ASPECTS.queryInternal(EnergyHandler.class, be, dir);
            if (sup != null) return (RebornEnergyHandler) sup.get();
            return null;
        });
        EnergyStorage.ITEM.registerFallback((stack, ctx) -> {
            Supplier<EnergyHandler> sup = ASPECTS.queryInternal(EnergyHandler.class, stack);
            if (sup != null) return (RebornEnergyHandler) sup.get();
            return null;
        });

    }

    public void initializeClient() {
        Nifty.clientInit();
    }

}
