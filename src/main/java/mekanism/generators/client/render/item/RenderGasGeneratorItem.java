package mekanism.generators.client.render.item;

import javax.annotation.Nonnull;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import mekanism.generators.client.model.ModelGasGenerator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderGasGeneratorItem {

    private static ModelGasGenerator gasGenerator = new ModelGasGenerator();

    public static void renderStack(@Nonnull ItemStack stack) {
        GlStateManager.rotate(180F, 0.0F, 1.0F, 1.0F);
        GlStateManager.rotate(90F, -1.0F, 0.0F, 0.0F);
        GL11.glTranslated(0.0F, -1.0F, 0.0F);
        GlStateManager.rotate(180F, 0.0F, 1.0F, 0.0F);
        MekanismRenderer.bindTexture(MekanismUtils.getResource(ResourceType.RENDER, "GasGenerator.png"));
        gasGenerator.render(0.0625F);
    }
}