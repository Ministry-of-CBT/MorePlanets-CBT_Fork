package stevekung.mods.moreplanets.utils.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import stevekung.mods.moreplanets.core.MorePlanetsMod;
import stevekung.mods.moreplanets.utils.client.renderer.IItemModelRender;
import stevekung.mods.moreplanets.utils.itemblocks.IItemRarity;
import stevekung.mods.stevekunglib.utils.ColorUtils;

public class BlockFenceMP extends BlockFence implements ISortableBlock, IItemModelRender, IItemRarity
{
    private String name;

    public BlockFenceMP(String name)
    {
        super(Material.WOOD, null);
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setSoundType(SoundType.WOOD);
        this.setTranslationKey(name);
    }

    @Override
    public Block setTranslationKey(String name)
    {
        this.name = name;
        return super.setTranslationKey(name);
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return MorePlanetsMod.BLOCK_TAB;
    }

    @Override
    public EnumSortCategoryBlock getBlockCategory()
    {
        return EnumSortCategoryBlock.FENCE;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public ColorUtils.RGB getRarity()
    {
        return this.name.equals("infected_purlonite_fence") ? ColorUtils.stringToRGB(IItemRarity.ALIEN) : null;
    }
}