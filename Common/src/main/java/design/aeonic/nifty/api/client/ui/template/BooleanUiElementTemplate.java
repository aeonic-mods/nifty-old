package design.aeonic.nifty.api.client.ui.template;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.ui.Texture;
import design.aeonic.nifty.api.client.ui.UiElementTemplate;
import net.minecraft.client.gui.screens.Screen;

/**
 * A simple static UI element.
 */
public class BooleanUiElementTemplate implements UiElementTemplate<Boolean> {

    protected final Texture texture;
    protected final int width;
    protected final int height;
    protected final int u;
    protected final int v;
    protected final int enabledU;
    protected final int enabledV;

    /**
     * @param texture  the texture map to draw from
     * @param width    the element's width
     * @param height   the element's height
     * @param u        the element's x offset in the texture map when the passed boolean is false
     * @param v        the element's y offset in the texture map when the passed boolean is false
     * @param enabledU the element's x offset in the texture map when the passed boolean is true
     * @param enabledV the element's y offset in the texture map when the passed boolean is true
     */
    public BooleanUiElementTemplate(Texture texture, int width, int height, int u, int v, int enabledU, int enabledV) {
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.u = u;
        this.v = v;
        this.enabledU = enabledU;
        this.enabledV = enabledV;
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
    public void draw(PoseStack stack, int x, int y, int zOffset, Boolean bool) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        texture.setup();
        Screen.blit(stack, x, y, zOffset, u, v, width, height, texture.width(), texture.height());
    }

}
