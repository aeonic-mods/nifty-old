package design.aeonic.nifty.api.client;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.material.FluidState;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Helper functions for miscellaneous rendering.
 */
public interface RenderHelper {

    // Platform implementations

    Pair<TextureAtlasSprite, Integer> getFluidSpriteAndColor(FluidState state);

    // Platform-independent utils

    static float[] unpackARGB(int color) {
        return new float[] {
                ((color >> 24) & 0xff) / 255f,
                ((color >> 16) & 0xff) / 255f,
                ((color >> 8) & 0xff) / 255f,
                (color & 0xff) / 255f
        };
    }

}
