package design.aeonic.nifty.api.item;

import net.minecraft.world.item.ItemStack;

import java.util.function.Function;
import java.util.function.Predicate;

public class SimpleSlot extends AbstractSlot {

    protected final Runnable onChanged;
    protected final Predicate<ItemStack> allowedPredicate;
    protected final Function<ItemStack, Integer> maxStack;

    /**
     * @param onChanged a runnable to run when this slot's contents have changed
     */
    public SimpleSlot(Runnable onChanged) {
        this(onChanged, $ -> true, ItemStack::getMaxStackSize);
    }

    /**
     * @param onChanged        a runnable to run when this slot's contents have changed
     * @param allowedPredicate a predicate for checking whether an item is allowed in this slot
     */
    public SimpleSlot(Runnable onChanged, Predicate<ItemStack> allowedPredicate) {
        this(onChanged, allowedPredicate, ItemStack::getMaxStackSize);
    }

    /**
     * @param onChanged        a runnable to run when this slot's contents have changed
     * @param allowedPredicate a predicate for checking whether an item is allowed in this slot
     * @param maxStack         a function for getting the max stack size in this slot of a given item
     */
    public SimpleSlot(Runnable onChanged, Predicate<ItemStack> allowedPredicate, Function<ItemStack, Integer> maxStack) {
        super();
        this.onChanged = onChanged;
        this.allowedPredicate = allowedPredicate;
        this.maxStack = maxStack;
    }

    @Override
    public void onChanged() {
        onChanged.run();
    }

    @Override
    public boolean allowedInSlot(ItemStack stack) {
        return allowedPredicate.test(stack);
    }

    @Override
    public int maxStackSize(ItemStack stack) {
        return maxStack.apply(stack);
    }

}
