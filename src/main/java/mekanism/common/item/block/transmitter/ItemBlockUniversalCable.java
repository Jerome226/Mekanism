package mekanism.common.item.block.transmitter;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import mekanism.api.text.EnumColor;
import mekanism.client.MekKeyHandler;
import mekanism.client.MekanismKeyHandler;
import mekanism.common.block.transmitter.BlockUniversalCable;
import mekanism.common.item.ITieredItem;
import mekanism.common.item.block.ItemBlockMultipartAble;
import mekanism.common.tier.CableTier;
import mekanism.common.util.text.EnergyDisplay;
import mekanism.common.util.text.TextComponentUtil;
import mekanism.common.util.text.Translation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ItemBlockUniversalCable extends ItemBlockMultipartAble<BlockUniversalCable> implements ITieredItem<CableTier> {

    public ItemBlockUniversalCable(BlockUniversalCable block) {
        super(block);
    }

    @Nullable
    @Override
    public CableTier getTier(@Nonnull ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ItemBlockUniversalCable) {
            return ((ItemBlockUniversalCable) item).getBlock().getTier();
        }
        return null;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(@Nonnull ItemStack itemstack, World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flag) {
        if (!MekKeyHandler.getIsKeyPressed(MekanismKeyHandler.sneakKey)) {
            CableTier tier = getTier(itemstack);
            if (tier != null) {
                tooltip.add(TextComponentUtil.build(EnumColor.INDIGO, Translation.of("mekanism.tooltip.capacity"), ": ", EnumColor.GRAY,
                      EnergyDisplay.of(tier.getCableCapacity()), "/t"));
            }
            tooltip.add(TextComponentUtil.build(Translation.of("mekanism.tooltip.hold"), " ", EnumColor.INDIGO, MekanismKeyHandler.sneakKey.getKey(),
                  EnumColor.GRAY, " ", Translation.of("mekanism.tooltip.for_details"), "."));
        } else {
            tooltip.add(TextComponentUtil.build(EnumColor.DARK_GRAY, Translation.of("mekanism.tooltip.capableTrans"), ":"));
            tooltip.add(TextComponentUtil.build("- ", EnumColor.PURPLE, "FE ", EnumColor.GRAY, "(MinecraftForge)"));
            tooltip.add(TextComponentUtil.build("- ", EnumColor.PURPLE, "EU ", EnumColor.GRAY, "(IndustrialCraft)"));
            tooltip.add(TextComponentUtil.build("- ", EnumColor.PURPLE, "Joules ", EnumColor.GRAY, "(Mekanism)"));
        }
    }

    //TODO: Multipart
    /*@Override
    @Optional.Method(modid = MekanismHooks.MCMULTIPART_MOD_ID)
    protected IMultipart getMultiPart() {
        //TODO
        return MultipartMekanism.TRANSMITTER_MP;
    }*/
}