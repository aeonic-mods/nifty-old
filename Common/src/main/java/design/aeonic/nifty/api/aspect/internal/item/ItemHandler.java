package design.aeonic.nifty.api.aspect.internal.item;

import design.aeonic.nifty.api.aspect.AspectProvider;
import design.aeonic.nifty.api.aspect.internal.item.slot.AbstractSlot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;

/**
 * An aspect that allows providers to contain items which can be inserted and extracted in a specified manner.<br><br>
 * This behavior is defined by the slot instances contained in {@link #getItemSlots()}.
 * @see design.aeonic.nifty.api.aspect.internal.item.slot
 */
public interface ItemHandler {

    /**
     * Writes this item handler's slot contents to a given compound tag.<br><br>
     * Assumes a tag without existing data.
     */
    default CompoundTag serialize(CompoundTag tag) {
        ListTag list = tag.getList("Slots", Tag.TAG_COMPOUND);
        AbstractSlot[] slots = getItemSlots();

        for (int i = 0; i < slots.length; i++) {
            CompoundTag item = list.getCompound(i);
            slots[i].get().save(item);
            list.add(item);
        }
        tag.put("Slots", list);
        return tag;
    }

    /**
     * Reads and reconstructs this item handler's slot contents from a given compound tag.
     */
    default void deserialize(CompoundTag tag) {
        ListTag list = tag.getList("Slots", Tag.TAG_COMPOUND);
        AbstractSlot[] slots = getItemSlots();

        for (int i = 0; i < slots.length; i++) {
            slots[i].set(ItemStack.of(list.getCompound(i)));
        }
    }

    /**
     * Gets the slots which describe this item handler's behavior.<br><br>
     * Cannot change between serialization/deserialization. Probably shouldn't ever need to change, but you do you.
     */
    AbstractSlot[] getItemSlots();

    /**
     * Attempts to insert an item stack, delegating to the specified slot. Returns the remainder of the inserted stack.
     * @param slot the slot index to insert to
     * @param stack the stack to insert
     * @return what's left of the inserted item stack - if nothing is inserted, will equal the passed stack
     *
     * @see AbstractSlot#insert(ItemStack)
     * @throws IndexOutOfBoundsException if the slot index is invalid.
     */
    default ItemStack insert(int slot, ItemStack stack) {
        return getSlot(slot).insert(stack);
    }

    /**
     * Attempts to extract the given amount from the given slot, returning the extracted stack. Delegates to the specified slot's extract method.<br><br>
     * Extracts at most the smaller of `amount` and the contained item's max stack size. And, of course, the amount contained.
     * @param slot the slot index to extract from
     * @param num the amount to extract
     * @return the extracted stack; an empty stack if extraction failed or the slot is empty
     *
     * @see AbstractSlot#extract(int)
     * @throws IndexOutOfBoundsException if the slot index is invalid.
     */
    default ItemStack extract(int slot, int num) {
        return getSlot(slot).extract(num);
    }

    /**
     * Returns... the number of slots. Thank me later.
     */
    default int getNumSlots() {
        return getItemSlots().length;
    }

    /**
     * @throws IndexOutOfBoundsException if the slot index is invalid.
     */
    default AbstractSlot getSlot(int index) {
        return getItemSlots()[index];
    }

}
