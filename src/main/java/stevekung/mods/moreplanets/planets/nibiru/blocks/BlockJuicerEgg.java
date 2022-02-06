package stevekung.mods.moreplanets.planets.nibiru.blocks;

import java.util.Random;

import micdoodle8.mods.galacticraft.planets.venus.entities.EntityJuicer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import stevekung.mods.moreplanets.init.MPSounds;
import stevekung.mods.moreplanets.planets.nibiru.tileentity.TileEntityJuicerEgg;
import stevekung.mods.moreplanets.utils.blocks.BlockBaseMP;
import stevekung.mods.moreplanets.utils.blocks.EnumSortCategoryBlock;
import stevekung.mods.moreplanets.utils.itemblocks.IItemRarity;
import stevekung.mods.stevekunglib.utils.ColorUtils;

public class BlockJuicerEgg extends BlockBaseMP implements ITileEntityProvider
{
    public BlockJuicerEgg(String name)
    {
        super(Material.GROUND);
        this.setResistance(0.0F);
        this.setHardness(0.5F);
        this.setTranslationKey(name);
        this.setSoundType(MPSounds.ALIEN_EGG);
        this.setDefaultSlipperiness(0.8F);
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
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        return true;
    }

    @Override
    public void onPlayerDestroy(World world, BlockPos pos, IBlockState state)
    {
        if (!world.isRemote)
        {
            if (world.rand.nextInt(5) == 0)
            {
                EntityJuicer juicer = new EntityJuicer(world);
                juicer.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D, 0.0F, 0.0F);
                world.spawnEntity(juicer);
            }
        }
    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance)
    {
        if (entity.isSneaking())
        {
            super.onFallenUpon(world, pos, entity, fallDistance);
        }
        else
        {
            entity.fall(fallDistance, 0.0F);
        }
    }

    @Override
    public void onLanded(World world, Entity entity)
    {
        if (entity.isSneaking())
        {
            super.onLanded(world, entity);
        }
        else if (entity.motionY < 0.0D)
        {
            entity.motionY = -entity.motionY;

            if (!(entity instanceof EntityLivingBase))
            {
                entity.motionY *= 0.8D;
            }
        }
    }

    @Override
    public void onEntityWalk(World world, BlockPos pos, Entity entity)
    {
        if (Math.abs(entity.motionY) < 0.1D && !entity.isSneaking())
        {
            double d0 = 0.4D + Math.abs(entity.motionY) * 0.2D;
            entity.motionX *= d0;
            entity.motionZ *= d0;
        }
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public int quantityDropped(Random rand)
    {
        return 0;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityJuicerEgg();
    }

    @Override
    public EnumSortCategoryBlock getBlockCategory()
    {
        return EnumSortCategoryBlock.DECORATION_NON_BLOCK;
    }

    @Override
    public ColorUtils.RGB getRarity()
    {
        return ColorUtils.stringToRGB(IItemRarity.COMMON);
    }
}