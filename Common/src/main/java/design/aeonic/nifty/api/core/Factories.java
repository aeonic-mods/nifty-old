package design.aeonic.nifty.api.core;

import design.aeonic.nifty.api.aspect.internal.item.ItemHandler;
import design.aeonic.nifty.api.aspect.internal.item.slot.AbstractSlot;
import design.aeonic.nifty.api.aspect.internal.item.slot.SimpleSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

/**
 * Contains factories for objects that might need platform-specific implementations or Vanilla objects that
 * can't be constructed for some reason (hidden constructors, hidden parameters etc).<br><br>
 * Avoids access wideners or needless mixins by using whatever existing mechanisms the current platform provides.
 */
public interface Factories {

    /**
     * Creates a block entity type, avoiding mixins or AWs in common code as Forge and Fabric both provide their own factories.
     */
    <T extends BlockEntity> BlockEntityType<T> blockEntityType(BiFunction<BlockPos, BlockState, T> constructor, Block... validBlocks);

}
