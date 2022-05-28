package design.aeonic.nifty.api.client.ui.template;

import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.ui.Texture;
import design.aeonic.nifty.api.client.ui.UiElementTemplate;
import net.minecraft.client.gui.screens.Screen;

/**
 * Describes a "filling" UI element that draws based on a percentage passed to ctx, such as a Furnace progress arrow.
 */
public class FillingUiElementTemplate implements UiElementTemplate<Float> {

    public final Texture texture;
    public final int width;
    public final int height;
    public final int u;
    public final int v;
    public final int fillU;
    public final int fillV;
    public final FillDirection direction;

    /**
     * @param texture    the texture map to draw from
     * @param width      the element's width
     * @param height     the element's height
     * @param u          the element's x offset in the texture map
     * @param v          the element's y offset in the texture map
     * @param fillU      the fill overlay's x offset in the texture map
     * @param fillV      the fill overlay's y offset in the texture map
     * @param direction the fill direction
     */
    public FillingUiElementTemplate(Texture texture, int width, int height, int u, int v, int fillU, int fillV, FillDirection direction) {
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.u = u;
        this.v = v;
        this.fillU = fillU;
        this.fillV = fillV;
        this.direction = direction;
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
    public void draw(PoseStack stack, int x, int y, int zOffset, Float ctx) {
        texture.setup();
        Screen.blit(stack, x, y, zOffset, u, v, width, height, texture.width(), texture.height());
        int fillWidth = (int) (width * ctx);
        int fillHeight = (int) (height * ctx);
        switch (direction) {
            case LEFT_TO_RIGHT -> Screen.blit(stack, x, y, zOffset, fillU, fillV, fillWidth, height, texture.width(), texture.height());
            case RIGHT_TO_LEFT -> Screen.blit(stack, x + fillWidth, y, zOffset, fillU + fillWidth, fillV, fillWidth, height, texture.width(), texture.height());
            case TOP_TO_BOTTOM -> Screen.blit(stack, x, y, zOffset, fillU, fillV, width, fillHeight, texture.width(), texture.height());
            case BOTTOM_TO_TOP -> Screen.blit(stack, x, y + fillHeight, zOffset, fillU, fillV + fillHeight, width, fillHeight, texture.width(), texture.height());
        }
    }

    public enum FillDirection {
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT,
        TOP_TO_BOTTOM,
        BOTTOM_TO_TOP
    }

}
