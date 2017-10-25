package stevekung.mods.moreplanets.itemblocks;

import java.util.List;

import micdoodle8.mods.galacticraft.core.energy.EnergyDisplayHelper;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.moreplanets.util.blocks.IBlockDescription;
import stevekung.mods.moreplanets.util.helper.CommonRegisterHelper;

public class ItemBlockSpaceWarpPad extends ItemBlock
{
    public ItemBlockSpaceWarpPad(Block block)
    {
        super(block);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> list, boolean advanced)
    {
        if (this.getBlock() instanceof IBlockDescription)
        {
            if (CommonRegisterHelper.isShiftKeyDown())
            {
                ((IBlockDescription)this.block).getDescription().addDescription(itemStack, list);
            }
            else
            {
                list.add(TextFormatting.GREEN + GCCoreUtil.translateWithFormat("item_desc.powerdraw.name", EnergyDisplayHelper.getEnergyDisplayS(75 * 20)));
                list.add(GCCoreUtil.translate("desc.shift_info.name"));
            }
        }
    }
}