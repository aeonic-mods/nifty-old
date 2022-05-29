package design.aeonic.nifty.api.client.ui.template;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.ui.Texture;
import design.aeonic.nifty.api.client.ui.UiElementTemplate;
import net.minecraft.client.gui.screens.Screen;

import java.util.function.Supplier;

/**
 * Describes a "filling" UI element that draws based on a percentage passed to ctx, such as a Furnace progress arrow.
 */
public class FillingUiElementTemplate<T extends FillingUiElementTemplate.FillLevel> implements UiElementTemplate<T> {

    protected final Texture texture;
    protected final StaticUiElementTemplate background;
    protected final int width;
    protected final int height;
    protected final int fillU;
    protected final int fillV;
    protected final FillDirection direction;

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
        this(texture, width, height, new StaticUiElementTemplate(texture, width, height, u, v), fillU, fillV, direction);
    }

    public FillingUiElementTemplate(Texture texture, int width, int height, StaticUiElementTemplate background, int fillU, int fillV, FillDirection direction) {
        this.texture = texture;
        this.background = background;
        this.width = width;
        this.height = height;
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
    public void draw(PoseStack stack, int x, int y, int zOffset, T ctx) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        background.draw(stack, x, y, zOffset, null);
        drawFill(stack, x, y, zOffset, ctx);
        drawOverlay(stack, x, y, zOffset, ctx);
    }

    protected void drawFill(PoseStack stack, int x, int y, int zOffset, T fillLevel) {
        int fillWidth = (int) (width * fillLevel.getFillLevel());
        int fillHeight = (int) (height * fillLevel.getFillLevel());
        texture.setup();
        switch (direction) {
            case LEFT_TO_RIGHT -> Screen.blit(stack, x, y, zOffset, fillU, fillV, fillWidth, height, texture.width(), texture.height());
            case RIGHT_TO_LEFT -> Screen.blit(stack, x + width - fillWidth, y, zOffset, fillU + width - fillWidth, fillV, fillWidth, height, texture.width(), texture.height());
            case TOP_TO_BOTTOM -> Screen.blit(stack, x, y, zOffset, fillU, fillV, width, fillHeight, texture.width(), texture.height());
            case BOTTOM_TO_TOP -> Screen.blit(stack, x, y + height - fillHeight, zOffset, fillU, fillV + height - fillHeight, width, fillHeight, texture.width(), texture.height());
        }
    }

    protected void drawOverlay(PoseStack stack, int x, int y, int zOffset, T fillLevel) {}

    public enum FillDirection {
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT,
        TOP_TO_BOTTOM,
        BOTTOM_TO_TOP;

        boolean isVertical() {
            return !isHorizontal();
        }

        boolean isHorizontal() {
            return this == LEFT_TO_RIGHT || this == RIGHT_TO_LEFT;
        }
    }

    /**
     * A functional interface that can return a fill level from 0-1.<br>
     * Used instead of a float to allow for fancier inherited ui elements.
     */
    @FunctionalInterface
    public interface FillLevel {
        float getFillLevel();
    }

}
