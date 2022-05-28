package design.aeonic.nifty.api.client.ui;

import com.mojang.blaze3d.vertex.PoseStack;

/**
 * Describes a UI element that can be drawn to the screen.
 *
 * @param <C> extra context for the draw function
 */
public interface UiElementTemplate<C> {

    /**
     * Draws the element to the screen.
     *
     * @param stack   the pose stack
     * @param x       the x position to draw at (absolute; add leftPos if calling from a screen)
     * @param y       the y position to draw at (absolute; add topPos if calling from a screen)
     * @param zOffset the z position to draw at (blit offset)
     * @param ctx     extra context as defined by the interface type parameter
     */
    void draw(PoseStack stack, int x, int y, int zOffset, C ctx);

    /**
     * Gets the element's width in pixels for drawing tooltips.
     */
    int getWidth();

    /**
     * Gets the element's height in pixels for drawing tooltips.
     */
    int getHeight();

    default UiElement<C> at(int x, int y, int zOffset) {
        return new UiElement<>(this, x, y, zOffset);
    }

    record UiElement<C>(UiElementTemplate<C> template, int x, int y, int zOffset) {

        public void draw(PoseStack stack, C ctx) {
            template.draw(stack, x, y, zOffset, ctx);
        }

        /**
         * Checks if a coordinate is within this element's screen area.
         */
        public boolean isWithin(int x, int y) {
            int right = this.x + template().getWidth();
            int bottom = this.y + template().getHeight();
            return this.x <= x && x <= right && this.y <= y && y <= bottom;
        }

    }

}
