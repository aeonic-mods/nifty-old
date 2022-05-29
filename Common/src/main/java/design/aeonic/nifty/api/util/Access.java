package design.aeonic.nifty.api.util;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.function.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Contains factories for objects that might need platform-specific implementations or Vanilla objects that
 * can't be constructed for some reason (hidden constructors, hidden parameters etc), as well as innaccessible methods.<br><br>
 * Avoids access wideners or needless mixins by using whatever existing mechanisms the current platform provides.
 */
public interface Access {

    /**
     * Creates a block entity type.
     */
    <T extends BlockEntity> BlockEntityType<T> blockEntityType(BiFunction<BlockPos, BlockState, T> constructor, Block... validBlocks);

    /**
     * Creates a menu type.
     */
    <T extends AbstractContainerMenu> MenuType<T> menuType(BiFunction<Integer, Inventory, T> constructor);

    /**
     * Registers a screen on the client.
     */
    <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>> void registerScreen(MenuType<M> menuType, TriFunction<M, Inventory, Component, S> constructor);

    /**
     * Creates and registers a new creative mode tab.
     */
    CreativeModeTab registerCreativeTab(ResourceLocation id, Supplier<ItemStack> icon);

}
