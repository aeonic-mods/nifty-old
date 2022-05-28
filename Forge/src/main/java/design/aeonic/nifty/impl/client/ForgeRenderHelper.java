package design.aeonic.nifty.impl.client;

import design.aeonic.nifty.api.client.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.ForgeHooksClient;
import org.apache.commons.lang3.tuple.Pair;

public class ForgeRenderHelper implements RenderHelper {

    @Override
    public Pair<TextureAtlasSprite, Integer> getFluidSpriteAndColor(FluidState state) {
        return Pair.of(ForgeHooksClient.getBlockMaterial(state.getType().getAttributes().getStillTexture()).sprite(),
                state.getType().getAttributes().getColor());
    }

}
