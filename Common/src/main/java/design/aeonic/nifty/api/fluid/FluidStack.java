package design.aeonic.nifty.api.fluid;

import design.aeonic.nifty.Nifty;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents fluids in a tank or somethin. Up to you man.<br><br>
 * Similar to Forge's fluid stack, but somewhat simplified and with a few key differences,
 * namely the removal of some optionals for less checks.
 */
public class FluidStack {

    // General fluid amounts, following Forge & tconstruct's conventions
    public static final int BUCKET = 1000;
    public static final int MB = 1;
    public static final int BLOCK = 1296;
    public static final int INGOT = 144;
    public static final int NUGGET = 16;

    public static final FluidStack EMPTY_STACK = FluidStack.of(Fluids.EMPTY, 0);

    private boolean isEmptyCache = false;
    private Fluid fluid;
    private int amount;
    private CompoundTag tag;

    public static FluidStack of(Fluid fluid) {
        return of(fluid, BUCKET);
    }

    public static FluidStack of(Fluid fluid, int amount) {
        return new FluidStack(fluid, amount, new CompoundTag());
    }

    public static FluidStack of(Fluid fluid, int amount, CompoundTag tag) {
        return new FluidStack(fluid, amount, tag == null ? new CompoundTag() : tag.copy());
    }

    protected FluidStack(Fluid fluid, int amount, CompoundTag tag) {
        this.fluid = fluid == null ? Fluids.EMPTY : fluid;
        this.amount = amount; // Tag getInt defaults to 0
        this.tag = tag;
        checkEmpty();
    }

    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Fluid", Registry.FLUID.getKey(fluid).toString());
        tag.putInt("Amount", amount);
        tag.put("Data", this.tag);
        return tag;
    }

    public static FluidStack fromNbt(CompoundTag tag) {
        return new FluidStack(
                Registry.FLUID.get(new ResourceLocation(tag.getString("Fluid"))),
                tag.getInt("Amount"),
                tag.getCompound("Data"));
    }

    public void toNetwork(FriendlyByteBuf buf) {
        if (isEmpty()) {
            buf.writeBoolean(false);
            return;
        }
        buf.writeBoolean(true);
        buf.writeVarInt(Registry.FLUID.getId(fluid));
        buf.writeVarInt(amount);
        buf.writeNbt(tag);
    }

    public static FluidStack fromNetwork(FriendlyByteBuf buf) {
        if (buf.readBoolean()) {
            return of(Registry.FLUID.byId(buf.readVarInt()),
                    buf.readVarInt(), buf.readNbt());
        }
        return EMPTY_STACK;
    }

    public boolean is(Fluid fluid) {
        return fluid.isSame(fluid);
    }

    public boolean canStack(FluidStack other) {
        return isEmpty() || other.isEmpty() || (fluid.isSame(other.fluid) && (tag.isEmpty() ? other.tag.isEmpty() : tag.equals(other.tag)));
    }

    public FluidStack split(int amount) {
        int amt = Math.min(amount, this.amount);
        FluidStack ret = copy();
        ret.setAmount(amt);
        shrink(amt);
        return ret;
    }

    public FluidStack copy() {
        return new FluidStack(fluid, amount, tag.copy());
    }

    public void shrink(int amount) {
        grow(-amount);
    }

    public void grow(int amount) {
        if (amount != 0)
            setAmount(this.amount + amount);
    }

    public void setAmount(int amount) {
        if (amount >= 0) {
            this.amount = amount;
            checkEmpty();
        }
    }

    public boolean isEmpty() {
        return isEmptyCache;
    }

    public Fluid getFluid() {
        return fluid;
    }

    public int getAmount() {
        return amount;
    }

    public CompoundTag getTag() {
        return tag;
    }

    protected void checkEmpty() {
        if (this == FluidStack.EMPTY_STACK) isEmptyCache = true;
        else if (amount == 0 || fluid.isSame(Fluids.EMPTY)) {
            isEmptyCache = true;
            amount = 0;
            fluid = Fluids.EMPTY;
            if (!tag.isEmpty()) tag = new CompoundTag();
        }
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        FluidStack that = (FluidStack) other;

        if (amount != that.amount) return false;
        if (!fluid.equals(that.fluid)) return false;
        return tag.equals(that.tag);
    }

    public List<Component> getTooltip() {
        List<Component> tooltip = new ArrayList<>();

        ResourceLocation key = Registry.FLUID.getKey(fluid);
        CompoundTag compoundtag = tag.getCompound("display");
        if (compoundtag.contains("Name", 8)) {
            try {
                Component component = Component.Serializer.fromJson(compoundtag.getString("Name"));
                if (component != null) {
                    tooltip.add(component);
                }

                compoundtag.remove("Name");
            } catch (Exception exception) {
                compoundtag.remove("Name");
                tooltip.add(new TextComponent(StringUtils.capitalize(key.getPath())));
            }
        }
        else {
            tooltip.add(new TextComponent(StringUtils.capitalize(key.getPath())));
        }

        tooltip.add(new TextComponent(DecimalFormat.getIntegerInstance().format(amount) + " mb").withStyle(ChatFormatting.GRAY));

        // TODO: Add bucket item tooltips? Somewhere to register extra tooltip info?
        tooltip.add(new TextComponent(Nifty.PLATFORM.getModDisplayName(key.getNamespace()).orElse("Minecraft")).withStyle(ChatFormatting.ITALIC, ChatFormatting.BLUE));
        return tooltip;
    }

}
