package design.aeonic.nifty.api.aspect.internal.item;

import design.aeonic.nifty.api.aspect.internal.item.slot.AbstractSlot;

public class SimpleItemHandler implements ItemHandler {

    protected AbstractSlot[] slots;

    public SimpleItemHandler(AbstractSlot[] slots) {
        this.slots = slots;
    }

    @Override
    public AbstractSlot[] getItemSlots() {
        return slots;
    }
}
