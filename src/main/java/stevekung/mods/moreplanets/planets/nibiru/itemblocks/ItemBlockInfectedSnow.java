package stevekung.mods.moreplanets.planets.nibiru.itemblocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import stevekung.mods.moreplanets.init.MPBlocks;
import stevekung.mods.moreplanets.utils.itemblocks.ItemBlockMP;
import stevekung.mods.stevekunglib.utils.BlockStateProperty;

public class ItemBlockInfectedSnow extends ItemBlockMP
{
    public ItemBlockInfectedSnow(Block block)
    {
        super(block);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        ItemStack itemStack = player.getHeldItem(hand);

        if (itemStack.getCount() != 0 && player.canPlayerEdit(pos, facing, itemStack))
        {
            IBlockState iblockstate = world.getBlockState(pos);
            Block block = iblockstate.getBlock();
            BlockPos blockpos = pos;

            if ((facing != EnumFacing.UP || block != this.block) && !block.isReplaceable(world, pos))
            {
                blockpos = pos.offset(facing);
                iblockstate = world.getBlockState(blockpos);
                block = iblockstate.getBlock();
            }

            if (block == this.block)
            {
                int i = iblockstate.getValue(BlockStateProperty.LAYERS);

                if (i <= 7)
                {
                    IBlockState iblockstate1 = iblockstate.withProperty(BlockStateProperty.LAYERS, i + 1);
                    AxisAlignedBB axisalignedbb = iblockstate1.getCollisionBoundingBox(world, blockpos);

                    if (axisalignedbb != null && world.checkNoEntityCollision(axisalignedbb.offset(blockpos)) && world.setBlockState(blockpos, iblockstate1, 10))
                    {
                        SoundType soundtype = this.block.getSoundType(iblockstate1, world, blockpos, player);
                        world.playSound(player, blockpos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                        itemStack.shrink(1);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
            return super.onItemUse(player, world, blockpos, hand, facing, hitX, hitY, hitZ);
        }
        else
        {
            return EnumActionResult.FAIL;
        }
    }

    @Override
    public String getTranslationKey(ItemStack itemStack)
    {
        return this.getBlock() == MPBlocks.PURIFIED_SNOW_LAYER ? "tile.purified_snow" : "tile.infected_snow";
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack)
    {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() != (this.getBlock() == MPBlocks.PURIFIED_SNOW_LAYER ? MPBlocks.PURIFIED_SNOW_LAYER : MPBlocks.INFECTED_SNOW_LAYER) || state.getValue(BlockStateProperty.LAYERS) > 7 ? super.canPlaceBlockOnSide(world, pos, side, player, stack) : true;
    }
}