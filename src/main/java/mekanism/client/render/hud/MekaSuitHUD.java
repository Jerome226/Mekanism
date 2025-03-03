package mekanism.client.render.hud;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import mekanism.client.MekanismClient;
import mekanism.client.render.HUDRenderer;
import mekanism.common.config.MekanismConfig;
import mekanism.common.item.gear.ItemMekaSuitArmor;
import mekanism.common.item.interfaces.IItemHUDProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;

public class MekaSuitHUD implements IIngameOverlay {

    private static final EquipmentSlot[] EQUIPMENT_ORDER = new EquipmentSlot[]{EquipmentSlot.OFFHAND, EquipmentSlot.MAINHAND,
                                                                               EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS,
                                                                               EquipmentSlot.FEET};

    private static final HUDRenderer hudRenderer = new HUDRenderer();

    @Override
    public void render(ForgeIngameGui gui, PoseStack poseStack, float partialTicks, int width, int height) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!minecraft.options.hideGui && !minecraft.player.isSpectator() && MekanismConfig.client.enableHUD.get() && MekanismClient.renderHUD) {
            int count = 0;
            Map<EquipmentSlot, List<Component>> renderStrings = new LinkedHashMap<>();
            for (EquipmentSlot slotType : EQUIPMENT_ORDER) {
                ItemStack stack = minecraft.player.getItemBySlot(slotType);
                if (stack.getItem() instanceof IItemHUDProvider hudProvider) {
                    List<Component> list = new ArrayList<>();
                    hudProvider.addHUDStrings(list, minecraft.player, stack, slotType);
                    int size = list.size();
                    if (size > 0) {
                        renderStrings.put(slotType, list);
                        count += size;
                    }
                }
            }
            if (count > 0) {
                int start = (renderStrings.size() * 2) + (count * 9);
                boolean alignLeft = MekanismConfig.client.alignHUDLeft.get();
                float hudScale = MekanismConfig.client.hudScale.get();
                int yScale = (int) ((1 / hudScale) * height);
                poseStack.pushPose();
                poseStack.scale(hudScale, hudScale, hudScale);
                for (Map.Entry<EquipmentSlot, List<Component>> entry : renderStrings.entrySet()) {
                    for (Component text : entry.getValue()) {
                        drawString(minecraft.font, width, poseStack, text, alignLeft, yScale - start, 0xC8C8C8);
                        start -= 9;
                    }
                    start -= 2;
                }
                poseStack.popPose();
            }
            if (minecraft.player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof ItemMekaSuitArmor) {
                hudRenderer.renderHUD(poseStack, partialTicks);
            }
        }
    }

    private void drawString(Font font, int windowWidth, PoseStack matrix, Component text, boolean leftSide, int y, int color) {
        // Note that we always offset by 2 pixels when left or right aligned
        if (leftSide) {
            font.drawShadow(matrix, text, 2, y, color);
        } else {
            int width = font.width(text) + 2;
            font.drawShadow(matrix, text, windowWidth - width, y, color);
        }
    }
}