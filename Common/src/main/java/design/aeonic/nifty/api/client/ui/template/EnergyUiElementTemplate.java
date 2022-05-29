package design.aeonic.nifty.api.client.ui.template;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.nifty.api.client.ui.Texture;
import design.aeonic.nifty.api.energy.EnergyHandler;
import net.minecraft.network.chat.Component;

import java.util.List;

public class EnergyUiElementTemplate extends FillingUiElementTemplate<EnergyHandler> {

    protected final StaticUiElementTemplate overlay;
    protected final float[] rgba;

    /**
     * @param texture      the texture map to draw from
     * @param width        the element's width
     * @param height       the element's height
     * @param backgroundU  the energy display background's x offset in the texture map
     * @param backgroundV  the energy display background's y offset in the texture map
     * @param overlayU     the energy display overlay's x offset in the texture map
     * @param overlayV     the energy display overlay's y offset in the texture map
     * @param fillU        the energy fill's x offset in the texture map
     * @param fillV        the energy fill's y offset in the texture map
     * @param fillTintRgba an array of four floats from 0-1: the rgba values to tint the energy fill with
     * @param direction    the fill direction
     */
    public EnergyUiElementTemplate(Texture texture, int width, int height, int backgroundU, int backgroundV, int overlayU, int overlayV, int fillU, int fillV, float[] fillTintRgba, FillDirection direction) {
        super(texture, width, height, backgroundU, backgroundV, fillU, fillV, direction);
        this.overlay = new StaticUiElementTemplate(texture, width, height, overlayU, overlayV);
        this.rgba = fillTintRgba;
    }

    @Override
    protected void drawFill(PoseStack stack, int x, int y, int zOffset, EnergyHandler fillLevel) {
        RenderSystem.setShaderColor(rgba[0], rgba[1], rgba[2], rgba[3]);
        super.drawFill(stack, x, y, zOffset, fillLevel);
    }

    @Override
    protected void drawOverlay(PoseStack stack, int x, int y, int zOffset, EnergyHandler fillLevel) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        overlay.draw(stack, x, y, zOffset, null);
    }

    @Override
    public boolean hasTooltip() {
        return true;
    }

    @Override
    public List<Component> getTooltip(EnergyHandler ctx) {
        return ctx.getTooltip();
    }

}
