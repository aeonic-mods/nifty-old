package design.aeonic.nifty.api.client.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

/**
 * Describes a texture, with width and height for drawing.
 */
public record Texture(ResourceLocation location, int width, int height) {

    public Texture(ResourceLocation location) {
        this(location, 256, 256);
    }

    public void setup() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, location);
    }

}
