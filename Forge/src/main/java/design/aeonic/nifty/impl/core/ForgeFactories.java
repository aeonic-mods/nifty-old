package design.aeonic.nifty.impl.core;

import design.aeonic.nifty.api.core.Factories;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public class ForgeFactories implements Factories {

    @Override
    public <T extends BlockEntity> BlockEntityType<T> blockEntityType(BiFunction<BlockPos, BlockState, T> constructor, Block... validBlocks) {
        return BlockEntityType.Builder.of(constructor::apply, validBlocks).build(null);
    }

}
