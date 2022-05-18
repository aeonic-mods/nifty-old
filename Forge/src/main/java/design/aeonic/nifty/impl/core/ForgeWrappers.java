package design.aeonic.nifty.impl.core;

import design.aeonic.nifty.api.aspect.internal.ItemHandler;
import design.aeonic.nifty.api.core.Wrappers;
import design.aeonic.nifty.impl.item.ForgeItemHandler;

public class ForgeWrappers implements Wrappers {
    @Override
    public ItemHandler itemHandler(ItemHandler parent) {
        return new ForgeItemHandler(parent);
    }
}
