package design.aeonic.nifty.impl.core;

import design.aeonic.nifty.api.util.Access;
import net.minecraft.client.gui.screens.MenuScreens;
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

public class ForgeAccess implements Access {

    @Override
    public <T extends BlockEntity> BlockEntityType<T> blockEntityType(BiFunction<BlockPos, BlockState, T> constructor, Block... validBlocks) {
        return BlockEntityType.Builder.of(constructor::apply, validBlocks).build(null);
    }

    @Override
    public <T extends AbstractContainerMenu> MenuType<T> menuType(BiFunction<Integer, Inventory, T> constructor) {
        return new MenuType<>(constructor::apply);
    }

    @Override
    public <M extends AbstractContainerMenu, S extends Screen & MenuAccess<M>> void registerScreen(MenuType<M> menuType, TriFunction<M, Inventory, Component, S> constructor) {
        MenuScreens.register(menuType, constructor::apply);
    }

    @Override
    public CreativeModeTab registerCreativeTab(ResourceLocation id, Supplier<ItemStack> icon) {
        return new CreativeModeTab(String.format("%s.%s", id.getNamespace(), id.getPath())) {
            @Override
            public ItemStack makeIcon() {
                return icon.get();
            }
        };
    }

}
