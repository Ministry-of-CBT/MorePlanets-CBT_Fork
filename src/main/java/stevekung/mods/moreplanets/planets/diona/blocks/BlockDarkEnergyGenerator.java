package stevekung.mods.moreplanets.planets.diona.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.moreplanets.core.MorePlanetsMod;
import stevekung.mods.moreplanets.planets.diona.tileentity.TileEntityDarkEnergyGenerator;
import stevekung.mods.moreplanets.utils.BlocksItemsRegistry;
import stevekung.mods.moreplanets.utils.IDescription;
import stevekung.mods.moreplanets.utils.ItemDescription;
import stevekung.mods.moreplanets.utils.blocks.BlockTileMP;
import stevekung.mods.moreplanets.utils.blocks.EnumSortCategoryBlock;
import stevekung.mods.moreplanets.utils.itemblocks.IItemRarity;
import stevekung.mods.stevekunglib.utils.BlockStateProperty;
import stevekung.mods.stevekunglib.utils.ColorUtils;

public class BlockDarkEnergyGenerator extends BlockTileMP implements IDescription
{
    private static final AxisAlignedBB AABB = new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.75D, 0.9D);

    public BlockDarkEnergyGenerator(String name)
    {
        super(Material.IRON);
        this.setHardness(2.0F);
        this.setSoundType(SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockStateProperty.FACING_HORIZON, EnumFacing.NORTH));
        this.setTranslationKey(name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasCustomBreakingProgress(IBlockState state)
    {
        return true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return BlockDarkEnergyGenerator.AABB;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
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
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand)
    {
        float particlePosX = pos.getX() + 0.5F;
        float particlePosY = pos.getY() + 0.0F + rand.nextFloat() * 6.0F / 16.0F;
        float particlePosZ = pos.getZ() + 0.5F;
        float particleSize0 = 0.52F;
        float particleSize1 = rand.nextFloat() * 0.4F - 0.25F;
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileEntityDarkEnergyGenerator)
        {
            TileEntityDarkEnergyGenerator tileEntity = (TileEntityDarkEnergyGenerator) tile;

            if (!tileEntity.disabled && tileEntity.darkEnergyFuel > 0)
            {
                for (int i = 0; i < 16; i++)
                {
                    if (state.getValue(BlockStateProperty.FACING_HORIZON) == EnumFacing.NORTH || state.getValue(BlockStateProperty.FACING_HORIZON) == EnumFacing.SOUTH)
                    {
                        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particlePosX + particleSize0, particlePosY, particlePosZ + particleSize1, 0.0D, 0.0D, 0.0D);
                        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particlePosX - particleSize0, particlePosY, particlePosZ + particleSize1, 0.0D, 0.0D, 0.0D);
                    }
                    else
                    {
                        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particlePosX + particleSize1, particlePosY, particlePosZ - particleSize0, 0.0D, 0.0D, 0.0D);
                        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, particlePosX + particleSize1, particlePosY, particlePosZ + particleSize0, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack itemStack)
    {
        world.setBlockState(pos, this.getDefaultState().withProperty(BlockStateProperty.FACING_HORIZON, placer.getHorizontalFacing().getOpposite()));
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileEntityDarkEnergyGenerator)
        {
            TileEntityDarkEnergyGenerator energy = (TileEntityDarkEnergyGenerator) world.getTileEntity(pos);

            if (itemStack.hasTagCompound())
            {
                NBTTagCompound nbt = itemStack.getTagCompound();
                energy.storage.setEnergyStored(nbt.getFloat("EnergyStored"));
                energy.darkEnergyFuel = nbt.getInteger("DarkEnergyFuel");
                energy.inventory = NonNullList.withSize(energy.getSizeInventory(), ItemStack.EMPTY);
                ItemStackHelper.loadAllItems(nbt, energy.inventory);
            }
        }
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity tile, ItemStack heldStack)
    {
        if (tile instanceof TileEntityDarkEnergyGenerator)
        {
            TileEntityDarkEnergyGenerator electric = (TileEntityDarkEnergyGenerator) tile;
            ItemStack itemStack = new ItemStack(this);
            NBTTagCompound nbt = new NBTTagCompound();

            if (electric.getEnergyStoredGC() > 0)
            {
                nbt.setFloat("EnergyStored", electric.getEnergyStoredGC());
            }
            if (electric.darkEnergyFuel > 0)
            {
                nbt.setInteger("DarkEnergyFuel", electric.darkEnergyFuel);
            }
            ItemStackHelper.saveAllItems(nbt, electric.inventory);
            itemStack.setTagCompound(nbt);
            Block.spawnAsEntity(world, pos, itemStack);
        }
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        int change = world.getBlockState(pos).getValue(BlockStateProperty.FACING_HORIZON).rotateY().getHorizontalIndex();
        world.setBlockState(pos, this.getStateFromMeta(change), 3);
        return true;
    }

    @Override
    public boolean onMachineActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        player.openGui(MorePlanetsMod.INSTANCE, -1, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        if (this.hasTileEntity(state))
        {
            world.removeTileEntity(pos);
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityDarkEnergyGenerator();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(this);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta % 4);
        return this.getDefaultState().withProperty(BlockStateProperty.FACING_HORIZON, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(BlockStateProperty.FACING_HORIZON).getHorizontalIndex();
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockStateProperty.FACING_HORIZON);
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rotation)
    {
        return state.withProperty(BlockStateProperty.FACING_HORIZON, rotation.rotate(state.getValue(BlockStateProperty.FACING_HORIZON)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirror)
    {
        return state.withRotation(mirror.toRotation(state.getValue(BlockStateProperty.FACING_HORIZON)));
    }

    @Override
    public EnumSortCategoryBlock getBlockCategory()
    {
        return EnumSortCategoryBlock.MACHINE_NON_BLOCK;
    }

    @Override
    public ItemDescription getDescription()
    {
        return (itemStack, list) -> list.addAll(BlocksItemsRegistry.getDescription(this.getTranslationKey() + ".description"));
    }

    @Override
    public ColorUtils.RGB getRarity()
    {
        return ColorUtils.stringToRGB(IItemRarity.MACHINE);
    }
}