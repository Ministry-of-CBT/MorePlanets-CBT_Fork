package stevekung.mods.moreplanets.planets.nibiru.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.moreplanets.init.MPBlocks;
import stevekung.mods.moreplanets.planets.nibiru.client.model.ModelNuclearWasteTank;
import stevekung.mods.moreplanets.utils.ClientRendererUtils;

@SideOnly(Side.CLIENT)
public class NuclearWasteGeneratorMultiblockRenderer
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("moreplanets:textures/model/nuclear_waste_tank.png");
    private static final ResourceLocation GLOW = new ResourceLocation("moreplanets:textures/model/nuclear_waste_tank_glow.png");
    private static final ModelNuclearWasteTank MODEL = new ModelNuclearWasteTank();

    public static void render(double x, double y, double z)
    {
        GlStateManager.disableDepth();
        renderTank(x, y, z - 3);
        renderTank(x + 2, y, z - 2);
        renderTank(x - 2, y, z - 2);

        renderTank(x, y, z + 3);
        renderTank(x - 2, y, z + 2);
        renderTank(x + 2, y, z + 2);

        renderTank(x + 3, y, z);
        renderTank(x - 3, y, z);

        GlStateManager.enableBlend();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 1, y - 1, z);
        ClientRendererUtils.renderModel(MPBlocks.NUCLEAR_WASTE_FLUID_BLOCK.getDefaultState());
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 1, y - 1, z + 1);
        ClientRendererUtils.renderModel(MPBlocks.NUCLEAR_WASTE_FLUID_BLOCK.getDefaultState());
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x + 1, y - 1, z + 2);
        ClientRendererUtils.renderModel(MPBlocks.NUCLEAR_WASTE_FLUID_BLOCK.getDefaultState());
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y - 1, z);
        ClientRendererUtils.renderModel(MPBlocks.NUCLEAR_WASTE_FLUID_BLOCK.getDefaultState());
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y - 1, z + 2);
        ClientRendererUtils.renderModel(MPBlocks.NUCLEAR_WASTE_FLUID_BLOCK.getDefaultState());
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x - 1, y - 1, z);
        ClientRendererUtils.renderModel(MPBlocks.NUCLEAR_WASTE_FLUID_BLOCK.getDefaultState());
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x - 1, y - 1, z + 1);
        ClientRendererUtils.renderModel(MPBlocks.NUCLEAR_WASTE_FLUID_BLOCK.getDefaultState());
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x - 1, y - 1, z + 2);
        ClientRendererUtils.renderModel(MPBlocks.NUCLEAR_WASTE_FLUID_BLOCK.getDefaultState());
        GlStateManager.popMatrix();
        GlStateManager.enableDepth();
    }

    private static void renderTank(double x, double y, double z)
    {
        float lightMapSaveX = OpenGlHelper.lastBrightnessX;
        float lightMapSaveY = OpenGlHelper.lastBrightnessY;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
        GlStateManager.scale(-1.0F, -1.0F, 1.0F);
        GlStateManager.pushMatrix();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
        GlStateManager.disableLighting();
        Minecraft.getMinecraft().getTextureManager().bindTexture(GLOW);
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.4F);
        MODEL.renderWaste();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.4F);
        GlStateManager.enableLighting();
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightMapSaveX, lightMapSaveY);
        GlStateManager.popMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
        MODEL.renderBase();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        MODEL.renderGlass();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}