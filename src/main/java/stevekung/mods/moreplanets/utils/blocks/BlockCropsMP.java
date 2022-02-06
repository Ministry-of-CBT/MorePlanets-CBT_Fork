package stevekung.mods.moreplanets.utils.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import stevekung.mods.stevekunglib.utils.BlockStateProperty;

public abstract class BlockCropsMP extends BlockBushMP implements IGrowable
{
    public BlockCropsMP()
    {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockStateProperty.AGE_7, 0));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (state.getValue(BlockStateProperty.AGE_7).intValue() == 7)
        {
            Block.spawnAsEntity(world, pos, new ItemStack(this.getCrop()));

            for (int i = 0; i < 1 + world.rand.nextInt(2); i++)
            {
                Block.spawnAsEntity(world, pos, new ItemStack(this.getSeed()));
            }
            world.setBlockState(pos, state.withProperty(BlockStateProperty.AGE_7, 0));
            return true;
        }
        return false;
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return null;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
        if (!world.isAreaLoaded(pos, 1))
        {
            return;
        }

        super.updateTick(world, pos, state, rand);

        if (world.getLightFromNeighbors(pos.up()) >= 9)
        {
            int i = state.getValue(BlockStateProperty.AGE_7);

            if (i < 7)
            {
                float f = this.getGrowthChance(this, world, pos, state);

                if (rand.nextInt((int)(25.0F / f) + 1) == 0)
                {
                    world.setBlockState(pos, state.withProperty(BlockStateProperty.AGE_7, i + 1), 2);
                }
            }
        }
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state)
    {
        Block block = world.getBlockState(pos.down()).getBlock();
        return this.validBlock(block) && world.getLight(pos) >= 8 && world.canSeeSky(pos);
    }

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(world, pos, state, chance, 0);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return state.getValue(BlockStateProperty.AGE_7).intValue() == 7 ? this.getCrop() : this.getSeed();
    }

    @Override
    public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient)
    {
        return state.getValue(BlockStateProperty.AGE_7).intValue() < 7;
    }

    @Override
    public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state)
    {
        return true;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this.getSeed());
    }

    @Override
    public void grow(World world, Random rand, BlockPos pos, IBlockState state)
    {
        this.grow(world, pos, state);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(BlockStateProperty.AGE_7, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(BlockStateProperty.AGE_7);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockStateProperty.AGE_7);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        int age = state.getValue(BlockStateProperty.AGE_7);
        Random rand = world instanceof World ? ((World)world).rand : RANDOM;

        if (age >= 7)
        {
            for (int i = 0; i < 3 + fortune; ++i)
            {
                if (rand.nextInt(15) <= age)
                {
                    drops.add(new ItemStack(this.getSeed(), 1, this.damageDropped(state)));
                }
            }
        }
    }

    public int getMaxAge()
    {
        return 7;
    }

    protected void grow(World world, BlockPos pos, IBlockState state)
    {
        int i = state.getValue(BlockStateProperty.AGE_7).intValue() + MathHelper.getInt(world.rand, 2, 5);

        if (i > 7)
        {
            i = 7;
        }
        world.setBlockState(pos, state.withProperty(BlockStateProperty.AGE_7, i), 2);
    }

    protected float getGrowthChance(Block block, World world, BlockPos pos, IBlockState state)
    {
        float f = 1.0F;
        BlockPos blockpos = pos.down();

        for (int i = -1; i <= 1; ++i)
        {
            for (int j = -1; j <= 1; ++j)
            {
                float f1 = 0.0F;
                IBlockState iblockstate = world.getBlockState(blockpos.add(i, 0, j));

                if (this.canBlockStay(world, blockpos.add(i, 0, j), state))
                {
                    f1 = 1.0F;

                    if (iblockstate.getBlock().isFertile(world, blockpos.add(i, 0, j)))
                    {
                        f1 = 3.0F;
                    }
                }
                if (i != 0 || j != 0)
                {
                    f1 /= 4.0F;
                }
                f += f1;
            }
        }

        BlockPos blockpos1 = pos.north();
        BlockPos blockpos2 = pos.south();
        BlockPos blockpos3 = pos.west();
        BlockPos blockpos4 = pos.east();
        boolean flag = block == world.getBlockState(blockpos3).getBlock() || block == world.getBlockState(blockpos4).getBlock();
        boolean flag1 = block == world.getBlockState(blockpos1).getBlock() || block == world.getBlockState(blockpos2).getBlock();

        if (flag && flag1)
        {
            f /= 2.0F;
        }
        else
        {
            boolean flag2 = block == world.getBlockState(blockpos3.north()).getBlock() || block == world.getBlockState(blockpos4.north()).getBlock() || block == world.getBlockState(blockpos4.south()).getBlock() || block == world.getBlockState(blockpos3.south()).getBlock();

            if (flag2)
            {
                f /= 2.0F;
            }
        }
        return f;
    }

    protected abstract Item getSeed();
    protected abstract Item getCrop();
}