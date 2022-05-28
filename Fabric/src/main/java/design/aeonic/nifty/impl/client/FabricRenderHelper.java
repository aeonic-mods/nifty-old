package design.aeonic.nifty.impl.client;

import design.aeonic.nifty.api.client.RenderHelper;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.FluidState;
import org.apache.commons.lang3.tuple.Pair;

public class FabricRenderHelper implements RenderHelper {

    @Override
    public Pair<TextureAtlasSprite, Integer> getFluidSpriteAndColor(FluidState state) {
        var renderHandler = FluidRenderHandlerRegistry.INSTANCE.get(state.getType());
        return Pair.of(renderHandler.getFluidSprites(null, null, state)[0], renderHandler.getFluidColor(null, null, state));
    }

}
