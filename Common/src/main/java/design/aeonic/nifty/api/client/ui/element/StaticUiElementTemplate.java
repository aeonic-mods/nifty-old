package design.aeonic.nifty.api.client.ui.element;

import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.ui.Texture;
import design.aeonic.nifty.api.client.ui.UiElementTemplate;
import net.minecraft.client.gui.screens.Screen;

/**
 * A simple static UI element.
 *
 * @param texture the texture map to draw from
 * @param width   the element's width
 * @param height  the element's height
 * @param u       the element's x offset in the texture map
 * @param v       the element's y offset in the texture map
 */
public record StaticUiElementTemplate(Texture texture, int width, int height, int u, int v) implements UiElementTemplate<Void> {

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
        texture.setup();
        Screen.blit(stack, x, y, zOffset, u, v, width, height, texture.width(), texture.height());
    }

}
