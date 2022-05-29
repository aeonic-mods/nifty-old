package design.aeonic.nifty.api.client.ui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Supplier;

/**
 * A UI element to be rendered within a screen.
 */
public record UiElement<C>(UiElementTemplate<C> template, Supplier<C> contextGetter, int x, int y, int zOffset) {

    public void draw(PoseStack stack) {
        template.draw(stack, x, y, zOffset, contextGetter.get());
    }

    /**
     * Checks if a coordinate is within this element's screen area.
     */
    public boolean isWithin(int x, int y) {
        int right = this.x + template().getWidth() - 1;
        int bottom = this.y + template().getHeight() - 1;
        return this.x <= x && x <= right && this.y <= y && y <= bottom;
    }

    public List<Component> getTooltip() {
        return template.getTooltip(contextGetter.get());
    }

}
