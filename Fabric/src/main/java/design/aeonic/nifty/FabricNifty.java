package design.aeonic.nifty;

import design.aeonic.nifty.api.item.FluidHandler;
import design.aeonic.nifty.api.item.ItemHandler;
import design.aeonic.nifty.impl.fluid.FabricFluidHandler;
import design.aeonic.nifty.impl.fluid.FluidVariantStorageWrapper;
import design.aeonic.nifty.impl.item.FabricItemHandler;
import design.aeonic.nifty.impl.item.ItemVariantStorageWrapper;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;

public class FabricNifty {

    public void initializeCommon() {
        Nifty.init();

        // Expose blocks using the Fabric transfer API to Nifty's item handler lookup
        Nifty.ASPECTS.registerLookup(ItemHandler.class, (be, dir) -> {
            if (be == null || be.getLevel() == null) return null;
            Storage<ItemVariant> storage = ItemStorage.SIDED.find(be.getLevel(), be.getBlockPos(), be.getBlockState(), be, dir);
            if (storage == null) return null;
            return new ItemVariantStorageWrapper(storage);
        });

        // Expose blocks using the Fabric transfer API to Nifty's fluid handler lookup
        Nifty.ASPECTS.registerLookup(FluidHandler.class, (be, dir) -> {
            if (be == null || be.getLevel() == null) return null;
            Storage<FluidVariant> storage = FluidStorage.SIDED.find(be.getLevel(), be.getBlockPos(), be.getBlockState(), be, dir);
            if (storage == null) return null;
            return new FluidVariantStorageWrapper(storage);
        });

        // Expose Nifty's item handlers to the Fabric transfer API
        ItemStorage.SIDED.registerFallback((level, pos, state, be, dir) -> be == null ? null :
                (FabricItemHandler) Nifty.ASPECTS.query(ItemHandler.class, be, dir).resolve().orElse(null));


        // Expose Nifty's fluid handlers to the Fabric transfer API
        FluidStorage.SIDED.registerFallback((level, pos, state, be, dir) -> be == null ? null :
                (FabricFluidHandler) Nifty.ASPECTS.query(FluidHandler.class, be, dir).resolve().orElse(null));
    }

    public void initializeClient() {
        Nifty.clientInit();
    }

}
