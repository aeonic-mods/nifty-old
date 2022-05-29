package design.aeonic.nifty.api.energy;

import design.aeonic.nifty.Nifty;
import design.aeonic.nifty.api.client.ui.template.FillingUiElementTemplate;
import design.aeonic.nifty.api.item.ItemHandler;
import design.aeonic.nifty.api.core.Platform;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.text.DecimalFormat;
import java.util.List;

/**
 * An aspect that allows providers to contain and transfer energy.<br><br>
 * Much like {@link ItemHandler} and {@link design.aeonic.nifty.api.item.FluidHandler}, but vastly simplified
 * as is the nature of power.<br><br>
 * On Forge, energy is in terms of FE - on Fabric, who knows, man?
 */
public interface EnergyHandler extends FillingUiElementTemplate.FillLevel {

    /**
     * Gets the standard energy units for the current platform.
     */
    static String getUnits() {
        return Nifty.PLATFORM.getPlatform() == Platform.FORGE ? "FE" : "E";
    }

    /**
     * Gets the capacity of this handler.
     */
    long getCapacity();

    /**
     * Gets the amount of currently contained energy in this handler.
     */
    long getAmount();

    /**
     * A preliminary check for whether this handler should allow inserting energy.<br><br>
     * If this method returns false, any calls to {@link #insert} should return 0.
     */
    boolean allowInsertion();

    /**
     * A preliminary check for whether this handler should allow extracting energy.<br><br>
     * If this method returns false, any calls to {@link #extract} should return 0.
     */
    boolean allowExtraction();

    /**
     * Inserts up to a specified amount of energy.
     *
     * @param amount   the max amount to insert
     * @param simulate if true, only simulates insertion and doesn't modify the handler
     * @return the amount inserted
     */
    long insert(long amount, boolean simulate);

    /**
     * Extracts up to a specified amount of energy.
     *
     * @param amount   the max amount to extract
     * @param simulate if true, only simulates insertion and doesn't modify the handler
     * @return the amount extracted
     */
    long extract(long amount, boolean simulate);

    /**
     * Writes this energy handler's storage to NBT.
     */
    CompoundTag serialize();

    /**
     * Reads and reconstructs this energy handler's storage from NBT.
     */
    void deserialize(CompoundTag tag);

    /**
     * Writes this energy handler to a network packet.
     */
    void toNetwork(FriendlyByteBuf buf);

    /**
     * Reads and reconstructs this energy handler from a network packet.
     */
    void fromNetwork(FriendlyByteBuf buf);

    @Override
    default float getFillLevel() {
        return getAmount() / (float) getCapacity();
    }

    default List<Component> getTooltip() {
        return List.of(
                new TextComponent(String.format("%s/%s %s", DecimalFormat.getIntegerInstance().format(
                        getAmount()), DecimalFormat.getIntegerInstance().format(getCapacity()), getUnits())));
    }

}
