package design.aeonic.nifty.api.client.ui.element;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import design.aeonic.nifty.Nifty;
import design.aeonic.nifty.api.client.RenderHelper;
import design.aeonic.nifty.api.client.ui.Texture;
import design.aeonic.nifty.api.client.ui.UiElementTemplate;
import design.aeonic.nifty.api.fluid.AbstractTank;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class TankUiElementTemplate implements UiElementTemplate<AbstractTank> {

    public final StaticUiElementTemplate tankBackground;
    public final StaticUiElementTemplate tankOverlay;
    public final int width;
    public final int height;
    public final boolean horizontal;

    /**
     * @param texture     the texture map to draw the tank overlay from
     * @param width       the tank's width
     * @param height      the tank's height
     * @param backgroundU the tank background's x offset in the texture map
     * @param backgroundV the tank background's y offset in the texture map
     * @param overlayU    the tank overlay's x offset in the texture map
     * @param overlayV    the tank overlay's y offset in the texture map
     * @param horizontal  true if the tank fills horizontally, false if it fills vertically
     */
    public TankUiElementTemplate(Texture texture, int width, int height, int backgroundU, int backgroundV, int overlayU, int overlayV, boolean horizontal) {
        this.tankBackground = new StaticUiElementTemplate(texture, width, height, backgroundU, backgroundV);
        this.tankOverlay = new StaticUiElementTemplate(texture, width, height, overlayU, overlayV);
        this.width = width;
        this.height = height;
        this.horizontal = horizontal;
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
    public void draw(PoseStack stack, int x, int y, int zOffset, AbstractTank ctx) {
        // Draw the tank background
        tankBackground.draw(stack, x, y, zOffset, null);

        // Fluid setup
        var fluidInfo = Nifty.RENDER_HELPER.getFluidSpriteAndColor(ctx.get().getFluid().defaultFluidState());
        TextureAtlasSprite sprite = fluidInfo.getLeft();
        float[] color = RenderHelper.unpackARGB(fluidInfo.getRight());

        RenderSystem.setShaderColor(color[1], color[2], color[3], 1f);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, sprite.atlas().location());
        BufferBuilder builder = Tesselator.getInstance().getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        float fill = ctx.get().getAmount() / (float) ctx.getCapacity();
        int width = horizontal ? (int) (this.width * fill) : this.width;
        int height = horizontal ? this.height : (int) (this.height * fill);
        int startY = horizontal ? y : y + this.height - height;

        // Draw tiling fluid
        // https://github.com/SlimeKnights/TinkersConstruct/blob/1.18.2/src/main/java/slimeknights/tconstruct/library/client/GuiUtil.java#L154
        do {
            int drawHeight = Math.min(sprite.getHeight(), height);
            height -= drawHeight;
            float v1 = sprite.getV((16f * drawHeight) / sprite.getHeight());

            int x2 = x;
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

        // Draw the tank overlay
        tankOverlay.draw(stack, x, y, zOffset, null);
    }

}
