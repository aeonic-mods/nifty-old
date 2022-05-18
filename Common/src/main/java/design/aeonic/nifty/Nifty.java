package design.aeonic.nifty;

import design.aeonic.nifty.api.aspect.Aspects;
import design.aeonic.nifty.api.aspect.internal.item.ItemHandler;
import design.aeonic.nifty.api.core.Factories;
import design.aeonic.nifty.api.core.PlatformInfo;
import design.aeonic.nifty.api.core.Wrappers;
import design.aeonic.nifty.api.registry.GameObject;
import design.aeonic.nifty.api.registry.Registrar;
import design.aeonic.nifty.api.util.Constants;
import design.aeonic.nifty.api.util.Services;
import design.aeonic.nifty.test.TestBlock;
import design.aeonic.nifty.test.TestBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;

public class Nifty {

    /**
     * Information about the current platform and loaded mods.
     */
    public static final PlatformInfo PLATFORM = Services.load(PlatformInfo.class);
    /**
     * Registry hooks.
     */
    public static final Registrar REGISTRY = Services.load(Registrar.class);
    /**
     * Factories for Vanilla objects you might not be able to make without mixins/AWs, and for platform-specific
     * implementations of some Nifty things (ie item handlers, etc).
     */
    public static final Factories FACTORIES = Services.load(Factories.class);
    /**
     * The Aspect system, which bridges the gap between Forge caps and the Fabric API lookup system.
     */
    public static final Aspects ASPECTS = Services.load(Aspects.class);
    /**
     * Includes methods for wrapping certain Nifty objects in a platform-specific implementation for compatibility with
     * existing systems (ie item & other transfer handlers, etc)
     */
    public static final Wrappers WRAPPERS = Services.load(Wrappers.class);

    public static GameObject<TestBlock> TEST_BLOCK = REGISTRY.register(Registry.BLOCK,
            new ResourceLocation(Constants.NIFTY_ID, "test_block"),
            () -> new TestBlock(BlockBehaviour.Properties.of(Material.AMETHYST)));

    public static GameObject<BlockEntityType<TestBlockEntity>> TEST_BLOCK_ENTITY = REGISTRY.register(Registry.BLOCK_ENTITY_TYPE,
            new ResourceLocation(Constants.NIFTY_ID, "test_block_entity"),
            () -> Nifty.FACTORIES.blockEntityType(TestBlockEntity::new, TEST_BLOCK.get()));

    public static void init() {
        // Aspects
        ASPECTS.registerAspect(new ResourceLocation(Constants.NIFTY_ID, "item_handler"), ItemHandler.class);

        // Aspect callbacks
        ASPECTS.registerCallback(ItemHandler.class, (BlockEntity be, @Nullable Direction direction) -> be instanceof TestBlockEntity blockEntity ? blockEntity.itemHandler : null);
    }

    public static void clientInit() {

    }

}
