package design.aeonic.nifty.api.client.ui.template;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.ui.Texture;
import design.aeonic.nifty.api.client.ui.UiElementTemplate;
import net.minecraft.client.gui.screens.Screen;

import java.util.Objects;

/**
 * A simple static UI element.
 *
 */
public class StaticUiElementTemplate implements UiElementTemplate<Void> {

    protected final Texture texture;
    protected final int width;
    protected final int height;
    protected final int u;
    protected final int v;

    /**
     * @param texture the texture map to draw from
     * @param width   the element's width
     * @param height  the element's height
     * @param u       the element's x offset in the texture map
     * @param v       the element's y offset in the texture map
     */
    public StaticUiElementTemplate(Texture texture, int width, int height, int u, int v) {
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.u = u;
        this.v = v;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void draw(PoseStack stack, int x, int y, int zOffset, Void unused) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        texture.setup();
        Screen.blit(stack, x, y, zOffset, u, v, width, height, texture.width(), texture.height());
    }

}
