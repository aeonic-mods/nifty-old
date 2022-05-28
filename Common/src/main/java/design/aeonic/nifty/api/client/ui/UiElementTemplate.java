package design.aeonic.nifty.api.client.ui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

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

    /**
     * Makes a ui element from this template.
     *
     * @param context a getter for the element's context object
     * @param x       the element's absolute x position
     * @param y       the element's absolute y position
     * @param zOffset the element's z position (blit offset)
     * @return the new element
     */
    default UiElement<C> make(Supplier<C> context, int x, int y, int zOffset) {
        return new UiElement<>(this, context, x, y, zOffset);
    }

    /**
     * Checks whether this component has a tooltip.
     */
    default boolean hasTooltip() {
        return false;
    }

    /**
     * If the component has a tooltip, return tooltip elements for the given context.
     */
    default List<Component> getTooltip(C ctx) {
        return Collections.emptyList();
    }

}
