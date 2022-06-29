package design.aeonic.nifty.api.client.ui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A UI element to be rendered within a screen.
 */
public class UiElement<C> {

    protected final UiElementTemplate<C> template;
    protected final Supplier<C> contextGetter;
    protected final int x;
    protected final int y;
    protected final int zOffset;

    protected Supplier<List<Component>> tooltip;

    public UiElement(UiElementTemplate<C> template, Supplier<C> contextGetter, int x, int y, int zOffset) {
        this.template = template;
        this.contextGetter = contextGetter;
        this.x = x;
        this.y = y;
        this.zOffset = zOffset;

        this.tooltip = () -> template.getTooltip(contextGetter.get());
    }

    public void draw(PoseStack stack) {
        template.draw(stack, x, y, zOffset, contextGetter.get());
    }

    /**
     * Checks if a coordinate is within this element's screen area.
     */
    public boolean isWithin(int x, int y) {
        int right = this.x + template.getWidth() - 1;
        int bottom = this.y + template.getHeight() - 1;
        return this.x <= x && x <= right && this.y <= y && y <= bottom;
    }

    public void setTooltip(Supplier<List<Component>> tooltip) {
        this.tooltip = tooltip;
    }

    public List<Component> getTooltip() {
        return tooltip.get();
    }

}
