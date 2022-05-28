package design.aeonic.nifty.impl.fluid;

import design.aeonic.nifty.api.fluid.FluidStack;
import design.aeonic.nifty.api.item.FluidHandler;
import design.aeonic.nifty.impl.wrappers.WrappingFluidHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;

public class ForgeFluidHandler extends WrappingFluidHandler implements IFluidHandler {

    public ForgeFluidHandler(FluidHandler parent) {
        super(parent);
    }

    @Override
    public int getTanks() {
        return parent.getNumSlots();
    }

    @Nonnull
    @Override
    public net.minecraftforge.fluids.FluidStack getFluidInTank(int tank) {
        return forgeFromNifty(get(tank));
    }

    @Override
    public int getTankCapacity(int tank) {
        return parent.getCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull net.minecraftforge.fluids.FluidStack stack) {
        return parent.allowedInSlot(tank, niftyFromForge(stack));
    }

    @Override
    public int fill(net.minecraftforge.fluids.FluidStack resource, FluidAction action) {
        return resource.getAmount() - parent.insert(niftyFromForge(resource), action.simulate()).getAmount();
    }

    @Nonnull
    @Override
    public net.minecraftforge.fluids.FluidStack drain(net.minecraftforge.fluids.FluidStack resource, FluidAction action) {
        return forgeFromNifty(parent.extract((Fluid fluid, CompoundTag tag) -> resource.getFluid().isSame(fluid) &&
                ((tag.isEmpty() && (!resource.hasTag() || resource.getTag().isEmpty())) ||
                        tag.equals(resource.getTag())),
                resource.getAmount(), action.simulate()));
    }

    @Nonnull
    @Override
    public net.minecraftforge.fluids.FluidStack drain(int maxDrain, FluidAction action) {
        return forgeFromNifty(parent.extract(($, $$) -> true, maxDrain, action.simulate()));
    }

    public static FluidStack niftyFromForge(net.minecraftforge.fluids.FluidStack stack) {
        if (stack.isEmpty()) return FluidStack.EMPTY_STACK;
        return new ForgeFluidStack(stack);
    }

    public static net.minecraftforge.fluids.FluidStack forgeFromNifty(FluidStack stack) {
        return new net.minecraftforge.fluids.FluidStack(stack.getFluid(), stack.getAmount(), stack.getTag());
    }

}
