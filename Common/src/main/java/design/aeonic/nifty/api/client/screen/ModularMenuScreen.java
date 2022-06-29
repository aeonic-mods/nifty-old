package design.aeonic.nifty.api.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.ui.UiElementTemplate;
import design.aeonic.nifty.api.client.ui.UiElement;
import design.aeonic.nifty.api.client.ui.UiSets;
import design.aeonic.nifty.api.client.ui.template.StaticUiElementTemplate;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModularMenuScreen<M extends AbstractContainerMenu> extends AbstractContainerScreen<M> {

    protected @Nullable
    UiElement<?> background = null;
    protected final List<UiElement<?>> uiElements = new ArrayList<>();

    public ModularMenuScreen(M menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    /**
     * Adds an item slot with position based on the slot's index in the menu.
     *
     * @param index    the index of the menu slot
     * @param template the slot ui element template
     */
    public void addSlot(int index, StaticUiElementTemplate template) {
        // Figure out slot offset based on the ui element's width and height (menu slot size is 16x16)
        int offsetX = (template.getWidth() - 16) / 2;
        int offsetY = (template.getHeight() - 16) / 2;

        Slot slot = menu.slots.get(index);
        addUiElement(template, () -> null, slot.x - offsetX, slot.y - offsetY);
    }

    /**
     * Creates a UI element for this screen and adds it to the list of those to render.
     *
     * @param template the ui template
     * @param context  a getter for the element's context object
     * @param x        the element's x position relative to the screen
     * @param y        the element's y position relative to the screen
     */
    public <C> UiElement<C> addUiElement(UiElementTemplate<C> template, Supplier<C> context, int x, int y) {
        return addUiElement(template.make(context, x + leftPos, y + topPos, getBlitOffset()));
    }

    /**
     * Adds a UI element to the list of those to render. Should be called from or after {@link #init()} so that element
     * positioning will work properly.
     *
     * @param uiElement the element to add
     */
    public <C> UiElement<C> addUiElement(UiElement<C> uiElement) {
        uiElements.add(uiElement);
        return uiElement;
    }

    public void setBackground(UiElementTemplate<Void> background) {
        this.background = background.make(() -> null, leftPos, topPos, getBlitOffset());
    }

    public void setBackground(UiElement<Void> background) {
        this.background = background;
    }

    @Override
    protected void renderBg(@Nonnull PoseStack stack, float partialTick, int mouseX, int mouseY) {
        if (background == null) {
            UiSets.Vanilla.BACKGROUND.draw(stack, leftPos, topPos, getBlitOffset(), null);
        } else background.draw(stack);

        for (UiElement<?> element : uiElements) {
            element.draw(stack);
        }
    }

    @Override
    public void render(@Nonnull PoseStack stack, int mouseX, int mouseY, float partialTick) {
        super.render(stack, mouseX, mouseY, partialTick);
        renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(PoseStack stack, int mouseX, int mouseY) {
        super.renderTooltip(stack, mouseX, mouseY);
        for (UiElement<?> element : uiElements) {
            if (!element.getTooltip().isEmpty() && element.isWithin(mouseX, mouseY)) {
                renderComponentTooltip(stack, element.getTooltip(), mouseX, mouseY);
            }
        }
    }

}
