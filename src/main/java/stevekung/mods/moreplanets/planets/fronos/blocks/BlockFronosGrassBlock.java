package stevekung.mods.moreplanets.planets.fronos.blocks;

import java.util.Locale;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.moreplanets.init.MPBlocks;
import stevekung.mods.moreplanets.utils.blocks.BlockGrassBlockMP;

public class BlockFronosGrassBlock extends BlockGrassBlockMP implements IGrowable
{
    private static final PropertyEnum<BlockType> HAS_LAYER = PropertyEnum.create("layer", BlockType.class);

    public BlockFronosGrassBlock(String name)
    {
        this.setTranslationKey(name);
        this.setDefaultState(this.getDefaultState().withProperty(HAS_LAYER, BlockType.NONE));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        Block block = world.getBlockState(pos.up()).getBlock();

        if (block == Blocks.SNOW || block == Blocks.SNOW_LAYER)
        {
            state = state.withProperty(HAS_LAYER, BlockType.SNOW);
        }
        else
        {
            state = state.withProperty(HAS_LAYER, BlockType.NONE);
        }
        return state;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
        if (!world.isRemote)
        {
            if (!world.isAreaLoaded(pos, 3))
            {
                return;
            }
            if (world.getLightFromNeighbors(pos.up()) < 4 && world.getBlockLightOpacity(pos.up()) > 2)
            {
                world.setBlockState(pos, MPBlocks.FRONOS_DIRT.getDefaultState());
            }
            else if (world.getLightFromNeighbors(pos.up()) >= 9)
            {
                for (int i = 0; i < 4; ++i)
                {
                    BlockPos pos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);

                    if (world.getBlockState(pos1) == MPBlocks.FRONOS_DIRT.getDefaultState())
                    {
                        if (world.getLightFromNeighbors(pos1.up()) >= 4 && world.getBlockState(pos1.up()).getLightOpacity(world, pos1.up()) <= 2)
                        {
                            world.setBlockState(pos1, MPBlocks.FRONOS_GRASS_BLOCK.getDefaultState());
                        }
                    }
                }
            }
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Item.getItemFromBlock(MPBlocks.FRONOS_DIRT);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, HAS_LAYER);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, net.minecraftforge.common.IPlantable plantable)
    {
        IBlockState plant = plantable.getPlant(world, pos.offset(direction));

        if (plant.getBlock() == Blocks.REEDS)
        {
            return true;
        }
        return false;
    }

    @Override
    public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient)
    {
        return true;
    }

    @Override
    public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state)
    {
        return true;
    }

    @Override
    public void grow(World world, Random rand, BlockPos pos, IBlockState state)
    {
        BlockPos blockpos = pos.up();

        for (int i = 0; i < 128; ++i)
        {
            BlockPos blockpos1 = blockpos;
            int j = 0;

            while (true)
            {
                if (j >= i / 16)
                {
                    if (world.isAirBlock(blockpos1))
                    {
                        if (rand.nextInt(8) == 0)
                        {
                            world.getBiome(blockpos1).plantFlower(world, rand, blockpos1);
                        }
                        else
                        {
                            if (MPBlocks.FRONOS_GRASS.canPlaceBlockAt(world, blockpos1))
                            {
                                world.setBlockState(blockpos1, MPBlocks.FRONOS_GRASS.getDefaultState(), 3);
                            }
                        }
                    }
                    break;
                }

                blockpos1 = blockpos1.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);

                if (world.getBlockState(blockpos1.down()).getBlock() != MPBlocks.FRONOS_GRASS_BLOCK || world.getBlockState(blockpos1).isNormalCube())
                {
                    break;
                }
                ++j;
            }
        }
    }

    public enum BlockType implements IStringSerializable
    {
        NONE,
        SNOW,
        CREAM;

        @Override
        public String toString()
        {
            return this.getName().toLowerCase(Locale.ROOT);
        }

        @Override
        public String getName()
        {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}