package design.aeonic.nifty;

import design.aeonic.nifty.api.aspect.internal.item.ItemHandler;
import design.aeonic.nifty.impl.aspect.internal.item.FabricItemHandler;
import design.aeonic.nifty.impl.aspect.internal.item.ItemVariantStorageWrapper;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;

public class FabricNifty {

    public void initializeCommon() {
        Nifty.init();

        // Expose blocks using the Fabric transfer API to Nifty's item handler lookup
        Nifty.ASPECTS.registerCallback(ItemHandler.class, (be, dir) -> {
            if (be == null || be.getLevel() == null) return null;
            Storage<ItemVariant> storage = ItemStorage.SIDED.find(be.getLevel(), be.getBlockPos(), be.getBlockState(), be, dir);
            if (storage == null) return null;
            return new ItemVariantStorageWrapper(storage);
        });

        // Expose Nifty's item handlers to the Fabric transfer API
        ItemStorage.SIDED.registerFallback((level, pos, state, be, dir) -> be == null ? null :
                (FabricItemHandler) Nifty.ASPECTS.query(ItemHandler.class, be, dir).get().orElse(null));
    }

    public void initializeClient() {
        Nifty.clientInit();
    }
}
