package stevekung.mods.moreplanets.planets.nibiru.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.moreplanets.init.MPBlocks;
import stevekung.mods.moreplanets.init.MPItems;
import stevekung.mods.moreplanets.utils.blocks.BlockBushMP;
import stevekung.mods.stevekunglib.utils.BlockStateProperty;
import stevekung.mods.stevekunglib.utils.BlockUtils;

public class BlockInfectedSugarCane extends BlockBushMP
{
    private static final AxisAlignedBB AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);

    public BlockInfectedSugarCane(String name)
    {
        super(Material.PLANTS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockStateProperty.AGE_15, 0));
        this.setTranslationKey(name);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return BlockInfectedSugarCane.AABB;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
        if (world.getBlockState(pos.down()).getBlock() == MPBlocks.INFECTED_SUGAR_CANE || this.canBlockStay(world, pos, state))
        {
            if (world.isAirBlock(pos.up()))
            {
                int i;

                for (i = 1; world.getBlockState(pos.down(i)).getBlock() == this; ++i) {}

                if (i < 3)
                {
                    int j = state.getValue(BlockStateProperty.AGE_15);

                    if (j == 15)
                    {
                        world.setBlockState(pos.up(), this.getDefaultState());
                        world.setBlockState(pos, state.withProperty(BlockStateProperty.AGE_15, 0), 4);
                    }
                    else
                    {
                        world.setBlockState(pos, state.withProperty(BlockStateProperty.AGE_15, j + 1), 4);
                    }
                }
            }
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos)
    {
        Block block = world.getBlockState(pos.down()).getBlock();

        if (BlockUtils.isFluid(world, pos))
        {
            return false;
        }
        else if (block == this)
        {
            return true;
        }
        else
        {
            for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL)
            {
                if ((block == MPBlocks.INFECTED_GRASS_BLOCK || block == MPBlocks.INFECTED_DIRT || block == MPBlocks.INFECTED_COARSE_DIRT || block == MPBlocks.INFECTED_SAND) && world.getBlockState(pos.offset(facing).down()).getBlock() == MPBlocks.INFECTED_WATER_FLUID_BLOCK)
                {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state)
    {
        return this.canPlaceBlockAt(world, pos);
    }

    @Override
    protected boolean validBlock(Block block)
    {
        return block == this || block == MPBlocks.INFECTED_GRASS_BLOCK || block == MPBlocks.INFECTED_DIRT || block == MPBlocks.INFECTED_COARSE_DIRT || block == MPBlocks.INFECTED_SAND;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return MPItems.INFECTED_SUGAR_CANE;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(MPItems.INFECTED_SUGAR_CANE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(BlockStateProperty.AGE_15, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(BlockStateProperty.AGE_15);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockStateProperty.AGE_15);
    }
}