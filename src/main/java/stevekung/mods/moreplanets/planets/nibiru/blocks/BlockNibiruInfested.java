package stevekung.mods.moreplanets.planets.nibiru.blocks;

import java.util.Locale;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import stevekung.mods.moreplanets.init.MPBlocks;
import stevekung.mods.moreplanets.planets.nibiru.entity.EntityInfectedWorm;
import stevekung.mods.moreplanets.utils.blocks.BlockBaseMP;
import stevekung.mods.moreplanets.utils.blocks.EnumSortCategoryBlock;

public class BlockNibiruInfested extends BlockBaseMP
{
    private BlockType type;

    public BlockNibiruInfested(String name, BlockType type)
    {
        super(Material.CLAY);
        this.type = type;
        this.setTranslationKey(name);
    }

    @Override
    public int quantityDropped(Random rand)
    {
        return 0;
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state)
    {
        return new ItemStack(BlockNibiruInfested.getInfested(state.getBlock()));
    }

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops"))
        {
            EntityInfectedWorm worm = new EntityInfectedWorm(world);
            worm.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
            world.spawnEntity(worm);
            worm.spawnExplosionParticle();
        }
    }

    @Override
    public EnumSortCategoryBlock getBlockCategory()
    {
        return EnumSortCategoryBlock.DECORATION_BLOCK;
    }

    @Override
    public String getName()
    {
        return this.type.toString();
    }

    public static Block getInfested(Block block)
    {
        if (block == MPBlocks.NIBIRU_COBBLESTONE)
        {
            return MPBlocks.INFESTED_NIBIRU_COBBLESTONE;
        }
        else if (block == MPBlocks.NIBIRU_VEIN_COBBLESTONE)
        {
            return MPBlocks.INFESTED_NIBIRU_VEIN_COBBLESTONE;
        }
        else if (block == MPBlocks.INFECTED_STONE_BRICKS)
        {
            return MPBlocks.INFESTED_INFECTED_STONE_BRICKS;
        }
        else if (block == MPBlocks.INFECTED_VEIN_STONE_BRICKS)
        {
            return MPBlocks.INFESTED_INFECTED_VEIN_STONE_BRICKS;
        }
        else if (block == MPBlocks.INFECTED_CRACKED_STONE_BRICKS)
        {
            return MPBlocks.INFESTED_INFECTED_CRACKED_STONE_BRICKS;
        }
        else if (block == MPBlocks.INFECTED_CHISELED_STONE_BRICKS)
        {
            return MPBlocks.INFESTED_INFECTED_CHISELED_STONE_BRICKS;
        }
        else
        {
            return MPBlocks.INFESTED_NIBIRU_ROCK;
        }
    }

    public static Block getParent(Block block)
    {
        if (block == MPBlocks.INFESTED_NIBIRU_COBBLESTONE)
        {
            return MPBlocks.NIBIRU_COBBLESTONE;
        }
        else if (block == MPBlocks.INFESTED_NIBIRU_VEIN_COBBLESTONE)
        {
            return MPBlocks.NIBIRU_VEIN_COBBLESTONE;
        }
        else if (block == MPBlocks.INFESTED_INFECTED_STONE_BRICKS)
        {
            return MPBlocks.INFECTED_STONE_BRICKS;
        }
        else if (block == MPBlocks.INFESTED_INFECTED_VEIN_STONE_BRICKS)
        {
            return MPBlocks.INFECTED_VEIN_STONE_BRICKS;
        }
        else if (block == MPBlocks.INFESTED_INFECTED_CRACKED_STONE_BRICKS)
        {
            return MPBlocks.INFECTED_CRACKED_STONE_BRICKS;
        }
        else if (block == MPBlocks.INFESTED_INFECTED_CHISELED_STONE_BRICKS)
        {
            return MPBlocks.INFECTED_CHISELED_STONE_BRICKS;
        }
        else
        {
            return MPBlocks.NIBIRU_ROCK;
        }
    }

    public enum BlockType
    {
        NIBIRU_ROCK,
        NIBIRU_COBBLESTONE,
        NIBIRU_VEIN_COBBLESTONE,
        INFECTED_STONE_BRICKS,
        INFECTED_VEIN_STONE_BRICKS,
        INFECTED_CRACKED_STONE_BRICKS,
        INFECTED_CHISELED_STONE_BRICKS;

        @Override
        public String toString()
        {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}