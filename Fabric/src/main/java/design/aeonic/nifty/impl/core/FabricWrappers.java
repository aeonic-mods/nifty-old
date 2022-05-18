package design.aeonic.nifty.impl.core;

import design.aeonic.nifty.api.aspect.internal.ItemHandler;
import design.aeonic.nifty.api.core.Wrappers;
import design.aeonic.nifty.impl.item.FabricItemHandler;

public class FabricWrappers implements Wrappers {

    @Override
    public ItemHandler itemHandler(ItemHandler parent) {
        return new FabricItemHandler(parent);
    }

}
