package design.aeonic.nifty;

import design.aeonic.nifty.api.aspect.Aspects;
import design.aeonic.nifty.api.core.Constants;
import design.aeonic.nifty.api.core.Factories;
import design.aeonic.nifty.api.core.PlatformInfo;
import design.aeonic.nifty.api.core.Services;
import design.aeonic.nifty.api.registry.GameObject;
import design.aeonic.nifty.api.registry.Registrar;
import design.aeonic.nifty.test.TestBlock;
import design.aeonic.nifty.test.TestBlockEntity;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class Nifty {

    public static final PlatformInfo PLATFORM = Services.load(PlatformInfo.class);
    public static final Registrar REGISTRY = Services.load(Registrar.class);
    public static final Aspects ASPECTS = Services.load(Aspects.class);
    public static final Factories FACTORIES = Services.load(Factories.class);

    public static GameObject<TestBlock> TEST_BLOCK = REGISTRY.register(Registry.BLOCK,
            new ResourceLocation(Constants.NIFTY_ID, "test_block"),
            () -> new TestBlock(BlockBehaviour.Properties.of(Material.AMETHYST)));

    public static void init() {

    }    public static GameObject<BlockEntityType<TestBlockEntity>> TEST_BLOCK_ENTITY = REGISTRY.register(Registry.BLOCK_ENTITY_TYPE,
            new ResourceLocation(Constants.NIFTY_ID, "test_block_entity"),
            () -> Nifty.FACTORIES.blockEntityType(TestBlockEntity::new, TEST_BLOCK.get()));


}
