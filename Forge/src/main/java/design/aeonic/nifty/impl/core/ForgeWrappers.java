package design.aeonic.nifty.impl.core;

import design.aeonic.nifty.api.aspect.internal.item.ItemHandler;
import design.aeonic.nifty.api.core.Wrappers;
import design.aeonic.nifty.impl.aspect.internal.item.ForgeItemHandler;

public class ForgeWrappers implements Wrappers {
    @Override
    public ItemHandler itemHandler(ItemHandler parent) {
        return new ForgeItemHandler(parent);
    }
}
