package design.aeonic.nifty.api.core;

import design.aeonic.nifty.api.aspect.internal.item.ItemHandler;
import design.aeonic.nifty.api.aspect.internal.item.slot.SimpleSlot;

public interface Wrappers {

    /**
     * Wraps an existing item handler, allowing for it to be utilized in existing platform-specific transfer systems.<br><br>
     *
     * @param parent an existing item handler to wrap
     * @return the new item handler
     */
    ItemHandler itemHandler(ItemHandler parent);

}
