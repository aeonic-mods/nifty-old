package design.aeonic.nifty.api.aspect.internal.item;

import design.aeonic.nifty.api.aspect.AspectProvider;
import design.aeonic.nifty.api.aspect.internal.item.slot.AbstractSlot;
import net.minecraft.nbt.CompoundTag;

/**
 * An Item Handler delegate that mirrors some slots from its parent; can be used to expose slots conditionally or only to specific sides.<br><br>
 */
public class DelegateItemHandler implements ItemHandler {

    protected final ItemHandler parent;
    protected final AbstractSlot[] slots;

    /**
     * @param parent the parent item handler
     * @param slots the indices of slots that this handler should expose from its parent - must be valid for the parent
     */
    public DelegateItemHandler(ItemHandler parent, int[] slots) {
        this.parent = parent;
        this.slots = new AbstractSlot[slots.length];
        for (int i = 0; i < slots.length; i++) {
            this.slots[i] = parent.getSlot(slots[i]);
        }
    }

    @Override
    public CompoundTag serialize(CompoundTag tag) { return tag; }

    @Override
    public void deserialize(CompoundTag tag) {}

    @Override
    public AbstractSlot[] getItemSlots() {
        return slots;
    }
}
