package design.aeonic.nifty.aspect.internal;

import design.aeonic.nifty.api.aspect.internal.item.ItemHandler;
import design.aeonic.nifty.api.aspect.internal.item.slot.AbstractSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

/**
 * An Item Handler Aspect implementation that defers to an existing {@link net.minecraftforge.items.IItemHandler}.
 * Essentially the inverse of {@link AspectIItemHandler}.
 */
public class ForgeItemHandler implements ItemHandler {

    protected final IItemHandler parent;
    protected final AbstractSlot[] slots;

    public ForgeItemHandler(IItemHandler parent) {
        this.parent = parent;
        slots = new AbstractSlot[parent.getSlots()];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = new Slot(i);
        }
    }

    @Override
    public AbstractSlot[] getItemSlots() {
        return slots;
    }

    /**
     * A slot that delegates all methods to the Forge item handler.
     */
    public class Slot extends AbstractSlot {
        private final int index;

        public Slot(int index) {
            super();
            this.index = index;
        }

        @Override
        public boolean allowedInSlot(ItemStack stack) {
            return parent.isItemValid(index, stack);
        }

        @Override
        public ItemStack simulateInsert(ItemStack stack) {
            return parent.insertItem(index, stack, true);
        }

        @Override
        public ItemStack insert(ItemStack stack) {
            return parent.insertItem(index, stack, false);
        }

        @Override
        public ItemStack forceInsert(ItemStack stack) {
            return insert(stack);
        }

        @Override
        public ItemStack simulateExtract(int amount) {
            return parent.extractItem(index, amount, true);
        }

        @Override
        public ItemStack extract(int amount) {
            return parent.extractItem(index, amount, false);
        }

        @Override
        public ItemStack get() {
            return parent.getStackInSlot(index);
        }

        @Override
        public int maxStackSize(ItemStack stack) {
            return parent.getStackInSlot(index).getMaxStackSize();
        }
    }
}
