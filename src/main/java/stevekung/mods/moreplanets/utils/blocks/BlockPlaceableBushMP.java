package stevekung.mods.moreplanets.utils.blocks;

import java.util.Locale;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import stevekung.mods.moreplanets.init.MPBlocks;
import stevekung.mods.moreplanets.init.MPItems;
import stevekung.mods.moreplanets.planets.nibiru.world.gen.feature.WorldGenTerrashroom;
import stevekung.mods.moreplanets.utils.CompatibilityManagerMP;
import stevekung.mods.moreplanets.utils.itemblocks.IItemRarity;
import stevekung.mods.stevekunglib.utils.ColorUtils;

public class BlockPlaceableBushMP extends BlockBushMP implements IShearable, IGrowable
{
    private static final AxisAlignedBB GRASS = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);
    private static final AxisAlignedBB FLOWER = new AxisAlignedBB(0.3D, 0.0D, 0.3D, 0.7D, 0.8D, 0.7D);
    private static final AxisAlignedBB PURE_HERB = new AxisAlignedBB(0.3D, 0.0D, 0.3D, 0.7D, 0.6D, 0.7D);
    private static final AxisAlignedBB PHILIPY = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.6D, 0.75D);
    private static final AxisAlignedBB WHITE_TAIL = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 0.9D, 0.75D);
    private static final AxisAlignedBB VEALIUM_VINES = new AxisAlignedBB(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
    private static final AxisAlignedBB TERRASHROOM = new AxisAlignedBB(0.3D, 0.0D, 0.3D, 0.7D, 0.6D, 0.7D);
    private static final AxisAlignedBB PURPLE_BUSH = new AxisAlignedBB(3.0D, 0.0D, 3.0D, 13.0D, 8.0D, 13.0D);
    private BlockType type;

    public BlockPlaceableBushMP(String name, BlockType type)
    {
        super(Material.PLANTS);
        this.setTranslationKey(name);
        this.type = type;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return !this.type.isFlower() ? Items.AIR : Item.getItemFromBlock(this);
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random rand)
    {
        return 1 + rand.nextInt(fortune * 2 + 1);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        if (!this.type.isFlower())
        {
            return BlockPlaceableBushMP.GRASS;
        }
        else if (this.type == BlockType.PURE_HERB)
        {
            return BlockPlaceableBushMP.PURE_HERB;
        }
        else if (this.type == BlockType.PHILIPY)
        {
            return BlockPlaceableBushMP.PHILIPY;
        }
        else if (this.type == BlockType.WHITE_TAIL)
        {
            return BlockPlaceableBushMP.WHITE_TAIL;
        }
        else if (this.type == BlockType.VEALIUM_VINES)
        {
            return BlockPlaceableBushMP.VEALIUM_VINES;
        }
        else if (this.type == BlockType.TERRASHROOM)
        {
            return BlockPlaceableBushMP.TERRASHROOM;
        }
        else if (this.type == BlockType.CREEP_VINES)
        {
            return Block.FULL_BLOCK_AABB;
        }
        else if (this.type == BlockType.PURPLE_BUSH)
        {
            return PURPLE_BUSH;
        }
        return BlockPlaceableBushMP.FLOWER;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
        if (this.type == BlockType.TERRASHROOM && rand.nextInt(25) == 0)
        {
            int i = 5;

            for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-4, -1, -4), pos.add(4, 1, 4)))
            {
                if (world.getBlockState(blockpos).getBlock() == this)
                {
                    --i;

                    if (i <= 0)
                    {
                        return;
                    }
                }
            }

            BlockPos blockpos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);

            for (int k = 0; k < 4; ++k)
            {
                if (world.isAirBlock(blockpos1) && this.canPlaceBlockAt(world, blockpos1))
                {
                    pos = blockpos1;
                }
                blockpos1 = pos.add(rand.nextInt(3) - 1, rand.nextInt(2) - rand.nextInt(2), rand.nextInt(3) - 1);
            }
            if (world.isAirBlock(blockpos1) && this.canPlaceBlockAt(world, blockpos1))
            {
                world.setBlockState(blockpos1, this.getDefaultState(), 2);
            }
        }
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        if (this.type == BlockType.PURE_HERB)
        {
            return 2;
        }
        else if (this.type == BlockType.TERRAPUFF_HERB || this.type == BlockType.VEALIUM_VINES || this.type == BlockType.CREEP_VINES)
        {
            return 4;
        }
        else if (this.type == BlockType.TERRASHROOM)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        if (RANDOM.nextInt(8) == 0)
        {
            if (this.type == BlockType.CHEESE_GRASS)
            {
                drops.add(new ItemStack(MPItems.CHEESE_SPORE_SEED));
            }
            else if (this.type == BlockType.INFECTED_GRASS || this.type == BlockType.INFECTED_FERN)
            {
                drops.add(new ItemStack(MPItems.INFECTED_WHEAT_SEEDS));
            }
            else if (this.type == BlockType.FRONOS_GRASS || this.type == BlockType.PURPLE_BUSH || this.type == BlockType.FRONOS_FERN)
            {
                drops.add(new ItemStack(Items.WHEAT_SEEDS));
            }
        }
        if (this.type == BlockType.GREEN_VEIN_GRASS && RANDOM.nextInt(24) == 0)
        {
            drops.add(new ItemStack(MPItems.TERRABERRY));
        }
        if (this.type.isFlower())
        {
            drops.add(new ItemStack(this));
        }
    }

    @Override
    public boolean isReplaceable(IBlockAccess world, BlockPos pos)
    {
        return !this.type.isFlower();
    }

    @Override
    public EnumOffsetType getOffsetType()
    {
        return this.type.isFlower() ? EnumOffsetType.XZ : EnumOffsetType.XYZ;
    }

    @Override
    public boolean isShearable(ItemStack itemStack, IBlockAccess world, BlockPos pos)
    {
        return !this.type.isFlower();
    }

    @Override
    public NonNullList<ItemStack> onSheared(ItemStack itemStack, IBlockAccess world, BlockPos pos, int fortune)
    {
        return !this.type.isFlower() ? NonNullList.withSize(1, new ItemStack(this)) : NonNullList.create();
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
    {
        if (CompatibilityManagerMP.isCTMLoaded)
        {
            if (this.type == BlockType.TERRAPUFF_HERB || this.type == BlockType.NEMOPHILA)
            {
                return layer == BlockRenderLayer.CUTOUT;
            }
        }
        return this.type == BlockType.TERRASHROOM ? layer == BlockRenderLayer.TRANSLUCENT : super.canRenderInLayer(state, layer);
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos)
    {
        Block blockUp = world.getBlockState(pos.up()).getBlock();

        if (this.type == BlockType.VEALIUM_VINES)
        {
            return blockUp == MPBlocks.INFECTED_JUNGLE_LEAVES || blockUp == this;
        }
        else if (this.type == BlockType.CREEP_VINES)
        {
            return blockUp == MPBlocks.CREEP_BLOCK || blockUp == MPBlocks.GRAVITY_CREEP_BLOCK || blockUp == MPBlocks.GRAVITY_CREEP_EXTRACTOR;
        }
        else if (this.type == BlockType.TERRASHROOM)
        {
            return super.canPlaceBlockAt(world, pos) || world.getLight(pos) < 13 && world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.DOWN);
        }
        else
        {
            return super.canPlaceBlockAt(world, pos);
        }
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state)
    {
        Block blockUp = world.getBlockState(pos.up()).getBlock();

        if (this.type == BlockType.TERRASHROOM)
        {
            return super.canBlockStay(world, pos, state) || world.getLight(pos) < 13 && world.getBlockState(pos.down()).isSideSolid(world, pos.down(), EnumFacing.DOWN);
        }
        else if (this.type == BlockType.VEALIUM_VINES)
        {
            return blockUp == MPBlocks.INFECTED_JUNGLE_LEAVES || blockUp == this;
        }
        else if (this.type == BlockType.CREEP_VINES)
        {
            return blockUp == MPBlocks.CREEP_BLOCK || blockUp == MPBlocks.GRAVITY_CREEP_BLOCK || blockUp == MPBlocks.GRAVITY_CREEP_EXTRACTOR;
        }
        return super.canBlockStay(world, pos, state);
    }

    @Override
    protected boolean validBlock(Block block)
    {
        if (this.type == BlockType.CHEESE_GRASS)
        {
            return block == MPBlocks.CHEESE_GRASS_BLOCK || block == MPBlocks.CHEESE_DIRT || block == MPBlocks.CHEESE_COARSE_DIRT || block == MPBlocks.CHEESE_FARMLAND;
        }
        else if (this.type == BlockType.GREEN_VEIN_GRASS || this.type == BlockType.TERRAPUFF_HERB)
        {
            return block == MPBlocks.GREEN_VEIN_GRASS_BLOCK || block == MPBlocks.INFECTED_DIRT || block == MPBlocks.INFECTED_COARSE_DIRT || block == MPBlocks.INFECTED_FARMLAND;
        }
        else if (this.type == BlockType.INFECTED_GRASS || this.type == BlockType.INFECTED_FERN || this.type == BlockType.PURE_HERB || this.type == BlockType.PYOLONIA || this.type == BlockType.PHILIPY || this.type == BlockType.WHITE_TAIL)
        {
            return block == MPBlocks.INFECTED_GRASS_BLOCK || block == MPBlocks.INFECTED_DIRT || block == MPBlocks.INFECTED_COARSE_DIRT || block == MPBlocks.INFECTED_FARMLAND;
        }
        else if (this.type == BlockType.BATASIA_DANDELION)
        {
            return block == MPBlocks.INFECTED_SAND;
        }
        else if (this.type == BlockType.TERRASHROOM)
        {
            return block == MPBlocks.GREEN_VEIN_GRASS_BLOCK || block == MPBlocks.INFECTED_DIRT || block == MPBlocks.INFECTED_COARSE_DIRT || block == MPBlocks.TERRASTONE || block == MPBlocks.PURIFIED_GRAVEL;
        }
        else if (this.type == BlockType.FRONOS_GRASS || this.type == BlockType.NEMOPHILA || this.type == BlockType.PINK_BLECHNUM || this.type == BlockType.PURPLE_BUSH || this.type == BlockType.FRONOS_FERN)
        {
            return block == MPBlocks.FRONOS_GRASS_BLOCK || block == MPBlocks.FRONOS_DIRT || block == MPBlocks.FRONOS_COARSE_DIRT || block == MPBlocks.FRONOS_FARMLAND;
        }
        return false;
    }

    @Override
    public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient)
    {
        if (this.type == BlockType.CHEESE_GRASS)
        {
            return MPBlocks.CHEESE_TALL_GRASS.canPlaceBlockAt(world, pos);
        }
        else if (this.type == BlockType.INFECTED_GRASS)
        {
            return MPBlocks.INFECTED_TALL_GRASS.canPlaceBlockAt(world, pos);
        }
        else if (this.type == BlockType.INFECTED_FERN)
        {
            return MPBlocks.INFECTED_LARGE_FERN.canPlaceBlockAt(world, pos);
        }
        else if (this.type == BlockType.GREEN_VEIN_GRASS)
        {
            return MPBlocks.GREEN_VEIN_TALL_GRASS.canPlaceBlockAt(world, pos);
        }
        else if (this.type == BlockType.FRONOS_GRASS)
        {
            return MPBlocks.FRONOS_TALL_GRASS.canPlaceBlockAt(world, pos);
        }
        return this.type == BlockType.TERRASHROOM;
    }

    @Override
    public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state)
    {
        if (this.type == BlockType.CHEESE_GRASS)
        {
            return MPBlocks.CHEESE_TALL_GRASS.canPlaceBlockAt(world, pos);
        }
        else if (this.type == BlockType.INFECTED_GRASS)
        {
            return MPBlocks.INFECTED_TALL_GRASS.canPlaceBlockAt(world, pos);
        }
        else if (this.type == BlockType.INFECTED_FERN)
        {
            return MPBlocks.INFECTED_LARGE_FERN.canPlaceBlockAt(world, pos);
        }
        else if (this.type == BlockType.GREEN_VEIN_GRASS)
        {
            return MPBlocks.GREEN_VEIN_TALL_GRASS.canPlaceBlockAt(world, pos);
        }
        else if (this.type == BlockType.FRONOS_GRASS)
        {
            return MPBlocks.FRONOS_TALL_GRASS.canPlaceBlockAt(world, pos);
        }
        return this.type == BlockType.TERRASHROOM && rand.nextFloat() < 0.4D;
    }

    @Override
    public void grow(World world, Random rand, BlockPos pos, IBlockState state)
    {
        if (this.type == BlockType.CHEESE_GRASS)
        {
            if (MPBlocks.CHEESE_TALL_GRASS.canPlaceBlockAt(world, pos))
            {
                MPBlocks.CHEESE_TALL_GRASS.placeAt(world, pos, MPBlocks.CHEESE_TALL_GRASS, 2);
            }
        }
        else if (this.type == BlockType.INFECTED_GRASS)
        {
            if (MPBlocks.INFECTED_TALL_GRASS.canPlaceBlockAt(world, pos))
            {
                MPBlocks.INFECTED_TALL_GRASS.placeAt(world, pos, MPBlocks.INFECTED_TALL_GRASS, 2);
            }
        }
        else if (this.type == BlockType.INFECTED_FERN)
        {
            if (MPBlocks.INFECTED_LARGE_FERN.canPlaceBlockAt(world, pos))
            {
                MPBlocks.INFECTED_LARGE_FERN.placeAt(world, pos, MPBlocks.INFECTED_LARGE_FERN, 2);
            }
        }
        else if (this.type == BlockType.GREEN_VEIN_GRASS)
        {
            if (MPBlocks.GREEN_VEIN_TALL_GRASS.canPlaceBlockAt(world, pos))
            {
                MPBlocks.GREEN_VEIN_TALL_GRASS.placeAt(world, pos, MPBlocks.GREEN_VEIN_TALL_GRASS, 2);
            }
        }
        else if (this.type == BlockType.FRONOS_GRASS)
        {
            if (MPBlocks.FRONOS_TALL_GRASS.canPlaceBlockAt(world, pos))
            {
                MPBlocks.FRONOS_TALL_GRASS.placeAt(world, pos, MPBlocks.FRONOS_TALL_GRASS, 2);
            }
        }
        else if (this.type == BlockType.TERRASHROOM)
        {
            this.generateBigMushroom(world, pos, state, rand);
        }
    }

    @Override
    public ColorUtils.RGB getRarity()
    {
        return this.type == BlockType.CREEP_VINES ? ColorUtils.stringToRGB(IItemRarity.ALIEN) : null;
    }

    private boolean generateBigMushroom(World world, BlockPos pos, IBlockState state, Random rand)
    {
        world.setBlockToAir(pos);
        WorldGenTerrashroom worldGen = new WorldGenTerrashroom();

        if (worldGen.generate(world, rand, pos))
        {
            return true;
        }
        else
        {
            world.setBlockState(pos, state, 3);
            return false;
        }
    }

    public enum BlockType
    {
        CHEESE_GRASS(false),
        INFECTED_GRASS(false),
        INFECTED_FERN(false),
        GREEN_VEIN_GRASS(false),
        PURE_HERB(true),
        TERRAPUFF_HERB(true),
        BATASIA_DANDELION(true),
        PYOLONIA(true),
        PHILIPY(true),
        WHITE_TAIL(true),
        VEALIUM_VINES(true),
        TERRASHROOM(true),
        CREEP_VINES(true),
        FRONOS_GRASS(false),
        NEMOPHILA(true),
        PINK_BLECHNUM(true),
        PURPLE_BUSH(false),
        FRONOS_FERN(false),
        ;

        private boolean isFlower;

        BlockType(boolean isFlower)
        {
            this.isFlower = isFlower;
        }

        @Override
        public String toString()
        {
            return this.name().toLowerCase(Locale.ROOT);
        }

        public boolean isFlower()
        {
            return this.isFlower;
        }
    }
}