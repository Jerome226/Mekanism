package mekanism.client.gui.element.tab;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.UUID;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mekanism.api.text.EnumColor;
import mekanism.client.MekanismClient;
import mekanism.client.SpecialColors;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.GuiInsetElement;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.Mekanism;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.lib.security.ISecurityItem;
import mekanism.common.lib.security.ISecurityObject;
import mekanism.common.lib.security.SecurityData;
import mekanism.common.lib.security.SecurityMode;
import mekanism.common.network.to_server.PacketGuiInteract;
import mekanism.common.network.to_server.PacketGuiInteract.GuiInteraction;
import mekanism.common.network.to_server.PacketGuiInteract.GuiInteractionEntity;
import mekanism.common.network.to_server.PacketSecurityMode;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.MekanismUtils.ResourceType;
import mekanism.common.util.SecurityUtils;
import mekanism.common.util.text.OwnerDisplay;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;

public class GuiSecurityTab extends GuiInsetElement<ISecurityObject> {

    private static final ResourceLocation PUBLIC = MekanismUtils.getResource(ResourceType.GUI, "public.png");
    private static final ResourceLocation PRIVATE = MekanismUtils.getResource(ResourceType.GUI, "private.png");
    private static final ResourceLocation PROTECTED = MekanismUtils.getResource(ResourceType.GUI, "protected.png");

    private static ISecurityObject getItemSecurityObject(@Nonnull InteractionHand hand) {
        return SecurityUtils.wrapSecurityItem(() -> {
            ItemStack stack = minecraft.player.getItemInHand(hand);
            if (stack.isEmpty() || !(stack.getItem() instanceof ISecurityItem)) {
                minecraft.player.closeContainer();
                return ItemStack.EMPTY;
            }
            return stack;
        });
    }

    @Nullable
    private final InteractionHand currentHand;

    public GuiSecurityTab(IGuiWrapper gui, ISecurityObject securityObject) {
        this(gui, securityObject, 34);
    }

    public GuiSecurityTab(IGuiWrapper gui, ISecurityObject securityObject, int y) {
        super(PUBLIC, gui, securityObject, gui.getWidth(), y, 26, 18, false);
        this.currentHand = null;
    }

    public GuiSecurityTab(IGuiWrapper gui, @Nonnull InteractionHand hand) {
        super(PUBLIC, gui, getItemSecurityObject(hand), gui.getWidth(), 34, 26, 18, false);
        currentHand = hand;
    }

    @Override
    protected void colorTab() {
        MekanismRenderer.color(SpecialColors.TAB_SECURITY);
    }

    @Override
    protected ResourceLocation getOverlay() {
        SecurityMode mode = getSecurity();
        UUID ownerUUID = dataSource.getOwnerUUID();
        SecurityData data = ownerUUID == null ? null : MekanismClient.clientSecurityMap.get(ownerUUID);
        if (data != null && data.override) {
            mode = data.mode;
        }
        if (mode == SecurityMode.PRIVATE) {
            return PRIVATE;
        } else if (mode == SecurityMode.TRUSTED) {
            return PROTECTED;
        }
        return super.getOverlay();
    }

    @Override
    public void renderToolTip(@Nonnull PoseStack matrix, int mouseX, int mouseY) {
        super.renderToolTip(matrix, mouseX, mouseY);
        Component securityComponent = MekanismLang.SECURITY.translateColored(EnumColor.GRAY, SecurityUtils.getSecurity(dataSource, Dist.CLIENT));
        Component ownerComponent = OwnerDisplay.of(minecraft.player, dataSource.getOwnerUUID(), dataSource.getOwnerName()).getTextComponent();
        if (SecurityUtils.isOverridden(dataSource, Dist.CLIENT)) {
            displayTooltips(matrix, mouseX, mouseY, securityComponent, ownerComponent, MekanismLang.SECURITY_OVERRIDDEN.translateColored(EnumColor.RED));
        } else {
            displayTooltips(matrix, mouseX, mouseY, securityComponent, ownerComponent);
        }
    }

    private SecurityMode getSecurity() {
        return MekanismConfig.general.allowProtection.get() ? dataSource.getSecurityMode() : SecurityMode.PUBLIC;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (MekanismConfig.general.allowProtection.get()) {
            UUID owner = dataSource.getOwnerUUID();
            if (owner != null && minecraft.player.getUUID().equals(owner)) {
                if (currentHand != null) {
                    Mekanism.packetHandler().sendToServer(new PacketSecurityMode(currentHand, getSecurity().getNext()));
                } else if (dataSource instanceof BlockEntity tile) {
                    Mekanism.packetHandler().sendToServer(new PacketGuiInteract(GuiInteraction.NEXT_SECURITY_MODE, tile));
                } else if (dataSource instanceof Entity entity) {
                    Mekanism.packetHandler().sendToServer(new PacketGuiInteract(GuiInteractionEntity.NEXT_SECURITY_MODE, entity));
                }
            }
        }
    }
}