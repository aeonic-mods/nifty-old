package design.aeonic.nifty.api.client.ui.template;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import design.aeonic.nifty.Nifty;
import design.aeonic.nifty.api.client.RenderHelper;
import design.aeonic.nifty.api.client.ui.Texture;
import design.aeonic.nifty.api.fluid.AbstractTank;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;

import java.util.List;

public class TankUiElementTemplate extends FillingUiElementTemplate<AbstractTank> {

    protected final StaticUiElementTemplate overlay;

    /**
     * @param texture     the texture map to draw from
     * @param width       the element's width
     * @param height      the element's height
     * @param backgroundU the fluid tank background's x offset in the texture map
     * @param backgroundV the fluid tank background's y offset in the texture map
     * @param overlayU    the fluid tank overlay's x offset in the texture map
     * @param overlayV    the fluid tank overlay's y offset in the texture map
     * @param direction   the fill direction
     */
    public TankUiElementTemplate(Texture texture, int width, int height, int backgroundU, int backgroundV, int overlayU, int overlayV, FillDirection direction) {
        super(texture, width, height, backgroundU, backgroundV, -1, -1, direction);
        this.overlay = new StaticUiElementTemplate(texture, width, height, overlayU, overlayV);
    }

    @Override
    protected void drawFill(PoseStack stack, int x, int y, int zOffset, AbstractTank tank) {
        if (tank.get().isEmpty()) return;

        // Fluid setup
        var fluidInfo = Nifty.RENDER_HELPER.getFluidSpriteAndColor(tank.get().getFluid().defaultFluidState());
        TextureAtlasSprite sprite = fluidInfo.getLeft();
        float[] color = RenderHelper.unpackARGB(fluidInfo.getRight());

        RenderSystem.setShaderColor(color[1], color[2], color[3], 1f);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, sprite.atlas().location());
        BufferBuilder builder = Tesselator.getInstance().getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        int width = this.width;
        int height = this.height;
        int startX = x;
        int startY = y;

        if (direction.isHorizontal()) {
            width = (int) (this.width * tank.getFillLevel());
            if (direction == FillDirection.RIGHT_TO_LEFT) startX = x + this.width - width;
        } else {
            height = (int) (this.height * tank.getFillLevel());
            if (direction == FillDirection.BOTTOM_TO_TOP) startY = y + this.height - height;
        }

        // Draw tiling fluid
        // https://github.com/SlimeKnights/TinkersConstruct/blob/1.18.2/src/main/java/slimeknights/tconstruct/library/client/GuiUtil.java#L154
        do {
            int drawHeight = Math.min(sprite.getHeight(), height);
            height -= drawHeight;
            float v1 = sprite.getV((16f * drawHeight) / sprite.getHeight());

            int x2 = startX;
            int widthLeft = width;
            Matrix4f pose = stack.last().pose();

            do {
                int drawWidth = Math.min(sprite.getWidth(), widthLeft);
                widthLeft -= drawWidth;
                float u1 = sprite.getU((16f * drawWidth) / sprite.getWidth());

                builder.vertex(pose, x2, startY + drawHeight, zOffset).uv(sprite.getU0(), v1).endVertex();
                builder.vertex(pose, x2 + drawWidth, startY + drawHeight, zOffset).uv(u1, v1).endVertex();
                builder.vertex(pose, x2 + drawWidth, startY, zOffset).uv(u1, sprite.getV0()).endVertex();
                builder.vertex(pose, x2, startY, zOffset).uv(sprite.getU0(), sprite.getV0()).endVertex();

                x2 += drawWidth;
            } while (widthLeft > 0);
            startY += drawHeight;
        } while (height > 0);

        builder.end();
        RenderSystem.enableDepthTest();
        BufferUploader.end(builder);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    protected void drawOverlay(PoseStack stack, int x, int y, int zOffset, AbstractTank fillLevel) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        overlay.draw(stack, x, y, zOffset, null);
    }

    @Override
    public boolean hasTooltip() {
        return true;
    }

    @Override
    public List<Component> getTooltip(AbstractTank ctx) {
        return ctx.get().getTooltip();
    }

}
