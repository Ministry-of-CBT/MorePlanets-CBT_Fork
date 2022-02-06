package stevekung.mods.moreplanets.planets.nibiru.world.gen.feature;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import stevekung.mods.moreplanets.init.MPBlocks;

public class WorldGenInfectedRoofedTree extends WorldGenAbstractTree
{
    private boolean genLeaves;

    public WorldGenInfectedRoofedTree(boolean genLeaves)
    {
        super(false);
        this.genLeaves = genLeaves;
    }

    @Override
    public boolean generate(World world, Random rand, BlockPos pos)
    {
        int i = rand.nextInt(3) + rand.nextInt(2) + 6;
        int j = pos.getX();
        int k = pos.getY();
        int l = pos.getZ();

        if (k >= 1 && k + i + 1 < 256)
        {
            BlockPos blockpos = pos.down();
            IBlockState state = world.getBlockState(blockpos);
            Block block = state.getBlock();
            boolean isSoil = block == MPBlocks.INFECTED_GRASS_BLOCK || block == MPBlocks.INFECTED_DIRT || block == MPBlocks.INFECTED_COARSE_DIRT || block == MPBlocks.INFECTED_FARMLAND;

            if (!(isSoil && pos.getY() < world.getHeight() - i - 1))
            {
                return false;
            }
            else if (!this.placeTreeOfHeight(world, pos, i))
            {
                return false;
            }
            else
            {
                this.onPlantGrow(world, blockpos, pos);
                this.onPlantGrow(world, blockpos.east(), pos);
                this.onPlantGrow(world, blockpos.south(), pos);
                this.onPlantGrow(world, blockpos.south().east(), pos);
                EnumFacing enumfacing = EnumFacing.Plane.HORIZONTAL.random(rand);
                int i1 = i - rand.nextInt(4);
                int j1 = 2 - rand.nextInt(3);
                int k1 = j;
                int l1 = l;
                int i2 = k + i - 1;

                for (int j2 = 0; j2 < i; ++j2)
                {
                    if (j2 >= i1 && j1 > 0)
                    {
                        k1 += enumfacing.getXOffset();
                        l1 += enumfacing.getZOffset();
                        --j1;
                    }

                    int k2 = k + j2;
                    BlockPos blockpos1 = new BlockPos(k1, k2, l1);
                    state = world.getBlockState(blockpos1);

                    if (state.getBlock().isAir(state, world, blockpos1) || state.getBlock().isLeaves(state, world, blockpos1))
                    {
                        this.placeLogAt(world, blockpos1);
                        this.placeLogAt(world, blockpos1.east());
                        this.placeLogAt(world, blockpos1.south());
                        this.placeLogAt(world, blockpos1.east().south());
                    }
                }

                for (int i3 = -2; i3 <= 0; ++i3)
                {
                    for (int l3 = -2; l3 <= 0; ++l3)
                    {
                        int k4 = -1;
                        this.placeLeafAt(world, k1 + i3, i2 + k4, l1 + l3);
                        this.placeLeafAt(world, 1 + k1 - i3, i2 + k4, l1 + l3);
                        this.placeLeafAt(world, k1 + i3, i2 + k4, 1 + l1 - l3);
                        this.placeLeafAt(world, 1 + k1 - i3, i2 + k4, 1 + l1 - l3);

                        if ((i3 > -2 || l3 > -1) && (i3 != -1 || l3 != -2))
                        {
                            k4 = 1;
                            this.placeLeafAt(world, k1 + i3, i2 + k4, l1 + l3);
                            this.placeLeafAt(world, 1 + k1 - i3, i2 + k4, l1 + l3);
                            this.placeLeafAt(world, k1 + i3, i2 + k4, 1 + l1 - l3);
                            this.placeLeafAt(world, 1 + k1 - i3, i2 + k4, 1 + l1 - l3);
                        }
                    }
                }

                if (rand.nextBoolean())
                {
                    this.placeLeafAt(world, k1, i2 + 2, l1);
                    this.placeLeafAt(world, k1 + 1, i2 + 2, l1);
                    this.placeLeafAt(world, k1 + 1, i2 + 2, l1 + 1);
                    this.placeLeafAt(world, k1, i2 + 2, l1 + 1);
                }

                for (int j3 = -3; j3 <= 4; ++j3)
                {
                    for (int i4 = -3; i4 <= 4; ++i4)
                    {
                        if ((j3 != -3 || i4 != -3) && (j3 != -3 || i4 != 4) && (j3 != 4 || i4 != -3) && (j3 != 4 || i4 != 4) && (Math.abs(j3) < 3 || Math.abs(i4) < 3))
                        {
                            this.placeLeafAt(world, k1 + j3, i2, l1 + i4);
                        }
                    }
                }

                for (int k3 = -1; k3 <= 2; ++k3)
                {
                    for (int j4 = -1; j4 <= 2; ++j4)
                    {
                        if ((k3 < 0 || k3 > 1 || j4 < 0 || j4 > 1) && rand.nextInt(3) <= 0)
                        {
                            int l4 = rand.nextInt(3) + 2;

                            for (int i5 = 0; i5 < l4; ++i5)
                            {
                                this.placeLogAt(world, new BlockPos(j + k3, i2 - i5 - 1, l + j4));
                            }
                            for (int j5 = -1; j5 <= 1; ++j5)
                            {
                                for (int l2 = -1; l2 <= 1; ++l2)
                                {
                                    this.placeLeafAt(world, k1 + k3 + j5, i2, l1 + j4 + l2);
                                }
                            }
                            for (int k5 = -2; k5 <= 2; ++k5)
                            {
                                for (int l5 = -2; l5 <= 2; ++l5)
                                {
                                    if (Math.abs(k5) != 2 || Math.abs(l5) != 2)
                                    {
                                        this.placeLeafAt(world, k1 + k3 + k5, i2 - 1, l1 + j4 + l5);
                                    }
                                }
                            }
                        }
                    }
                }
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    private boolean placeTreeOfHeight(World world, BlockPos pos, int height)
    {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int l = 0; l <= height + 1; ++l)
        {
            int i1 = 1;

            if (l == 0)
            {
                i1 = 0;
            }
            if (l >= height - 1)
            {
                i1 = 2;
            }

            for (int j1 = -i1; j1 <= i1; ++j1)
            {
                for (int k1 = -i1; k1 <= i1; ++k1)
                {
                    if (!this.isReplaceable(world, mutablePos.setPos(i + j1, j + l, k + k1)))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void placeLogAt(World world, BlockPos pos)
    {
        if (this.canGrowInto(world.getBlockState(pos).getBlock()))
        {
            this.setBlockAndNotifyAdequately(world, pos, this.genLeaves ? MPBlocks.INFECTED_OAK_LOG.getDefaultState() : MPBlocks.INFECTED_DEADWOOD_LOG.getDefaultState());
        }
    }

    private void placeLeafAt(World world, int x, int y, int z)
    {
        if (!this.genLeaves)
        {
            return;
        }
        BlockPos blockpos = new BlockPos(x, y, z);
        IBlockState state = world.getBlockState(blockpos);

        if (state.getBlock().isAir(state, world, blockpos))
        {
            this.setBlockAndNotifyAdequately(world, blockpos, MPBlocks.INFECTED_OAK_LEAVES.getDefaultState());
        }
    }

    private void onPlantGrow(World world, BlockPos pos, BlockPos source)
    {
        IBlockState state = world.getBlockState(pos);
        state.getBlock().onPlantGrow(state, world, pos, source);
    }
}