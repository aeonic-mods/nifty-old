package design.aeonic.nifty.api.item;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

/**
 * A Container that defers to an existing ItemHandler for syncing.<br><br>
 * May only be used for item handlers you've created yourself, and not those obtained from another block.
 */
public final class ItemHandlerContainer extends SimpleContainer {

    private final ItemHandler handler;

    public ItemHandlerContainer(ItemHandler handler) {
        this.handler = handler;
    }

    @Override
    public int getContainerSize() {
        return handler.getNumSlots();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < handler.getNumSlots(); i++) {
            if (!handler.get(i).isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return handler.get(slot);
    }

    @Override
    public ItemStack addItem(ItemStack stack) {
        return handler.insert(stack, false);
    }

    @Override
    public ItemStack removeItem(int slot, int amt) {
        ItemStack stack = handler.extract(slot, amt, false);
        if (!stack.isEmpty()) setChanged();
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return handler.extract(slot, handler.get(slot).getMaxStackSize(), false);
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        handler.set(slot, itemStack);
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < handler.getNumSlots(); i++) {
            handler.set(i, ItemStack.EMPTY);
        }
    }

}
