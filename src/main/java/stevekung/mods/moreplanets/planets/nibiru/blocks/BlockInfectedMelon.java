package stevekung.mods.moreplanets.planets.nibiru.blocks;

import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import stevekung.mods.moreplanets.init.MPItems;
import stevekung.mods.moreplanets.utils.blocks.BlockBaseMP;

public class BlockInfectedMelon extends BlockBaseMP
{
    public BlockInfectedMelon(String name)
    {
        super(Material.GOURD);
        this.setTranslationKey(name);
        this.setHardness(1.0F);
        this.setSoundType(SoundType.WOOD);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return MPItems.INFECTED_MELON_SLICE;
    }

    @Override
    public int quantityDropped(Random rand)
    {
        return 3 + rand.nextInt(5);
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random rand)
    {
        return Math.min(9, this.quantityDropped(rand) + rand.nextInt(1 + fortune));
    }
}