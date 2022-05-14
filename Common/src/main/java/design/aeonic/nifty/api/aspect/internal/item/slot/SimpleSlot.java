package design.aeonic.nifty.api.aspect.internal.item.slot;

import design.aeonic.nifty.api.aspect.AspectProvider;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;
import java.util.function.Predicate;

public class SimpleSlot extends AbstractSlot {

    protected Predicate<ItemStack> allowedPredicate;
    protected Function<ItemStack, Integer> maxStack;
    protected int x;
    protected int y;

    public SimpleSlot(AspectProvider provider) {
        this(provider, -1, -1);
    }

    /**
     * @param x this slot's x position in a screen
     * @param y this slot's y position in a screen
     */
    public SimpleSlot(AspectProvider provider, int x, int y) {
        this(provider, x, y, $ -> true, ItemStack::getMaxStackSize);
    }

    /**
     * @param allowedPredicate a predicate for checking whether an item is allowed in this slot
     * @param x                this slot's x position in a screen
     * @param y                this slot's y position in a screen
     */
    public SimpleSlot(AspectProvider provider, int x, int y, Predicate<ItemStack> allowedPredicate) {
        this(provider, x, y, allowedPredicate, ItemStack::getMaxStackSize);
    }

    /**
     * @param allowedPredicate a predicate for checking whether an item is allowed in this slot
     * @param maxStack         a function for getting the max stack size in this slot of a given item
     * @param x                this slot's x position in a screen
     * @param y                this slot's y position in a screen
     */
    public SimpleSlot(AspectProvider provider, int x, int y, Predicate<ItemStack> allowedPredicate, Function<ItemStack, Integer> maxStack) {
        super(provider);
        this.x = x;
        this.y = y;
        this.allowedPredicate = allowedPredicate;
        this.maxStack = maxStack;
    }

    @Override
    public boolean allowedInSlot(ItemStack stack) {
        return allowedPredicate.test(stack);
    }

    @Override
    public int maxStackSize(ItemStack stack) {
        return maxStack.apply(stack);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}
