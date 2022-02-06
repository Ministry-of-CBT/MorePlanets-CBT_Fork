package stevekung.mods.moreplanets.blocks;

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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.moreplanets.core.MorePlanetsMod;
import stevekung.mods.moreplanets.tileentity.TileEntityShieldGenerator;
import stevekung.mods.moreplanets.utils.BlocksItemsRegistry;
import stevekung.mods.moreplanets.utils.IDescription;
import stevekung.mods.moreplanets.utils.ItemDescription;
import stevekung.mods.moreplanets.utils.blocks.BlockAdvancedTileMP;
import stevekung.mods.moreplanets.utils.blocks.EnumSortCategoryBlock;
import stevekung.mods.moreplanets.utils.itemblocks.IItemRarity;
import stevekung.mods.stevekunglib.utils.BlockStateProperty;
import stevekung.mods.stevekunglib.utils.ColorUtils;

public class BlockShieldGenerator extends BlockAdvancedTileMP implements IDescription
{
    public BlockShieldGenerator(String name)
    {
        super(Material.IRON);
        this.setHardness(5.0F);
        this.setTranslationKey(name);
        this.setSoundType(SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockStateProperty.FACING_HORIZON, EnumFacing.NORTH));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasCustomBreakingProgress(IBlockState state)
    {
        return true;
    }

    @Override
    public boolean onMachineActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        player.openGui(MorePlanetsMod.INSTANCE, -1, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public boolean onUseWrench(World world, BlockPos pos, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        int change = world.getBlockState(pos).getValue(BlockStateProperty.FACING_HORIZON).rotateY().getHorizontalIndex();
        world.setBlockState(pos, this.getStateFromMeta(change), 3);
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack itemStack)
    {
        world.setBlockState(pos, this.getDefaultState().withProperty(BlockStateProperty.FACING_HORIZON, placer.getHorizontalFacing().getOpposite()));
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileEntityShieldGenerator)
        {
            TileEntityShieldGenerator shield = (TileEntityShieldGenerator) tile;
            shield.onCreate(world, pos);

            if (itemStack.hasTagCompound())
            {
                NBTTagCompound nbt = itemStack.getTagCompound();
                shield.storage.setEnergyStored(nbt.getFloat("EnergyStored"));
                shield.shieldSize = nbt.getFloat("ShieldSize");
                shield.maxShieldSize = nbt.getInteger("MaxShieldSize");
                shield.shieldDamage = nbt.getInteger("ShieldDamage");
                shield.maxShieldDamage = nbt.getInteger("MaxShieldDamage");
                shield.shieldCapacity = nbt.getInteger("ShieldCapacity");
                shield.maxShieldCapacity = nbt.getInteger("MaxShieldCapacity");
                shield.shieldChargeCooldown = nbt.getInteger("ShieldChargeCooldown");
                shield.needCharged = nbt.getBoolean("NeedCharged");
                shield.enableShield = nbt.getBoolean("EnableShield");
                shield.enableDamage = nbt.getBoolean("EnableDamage");
                ItemStackHelper.loadAllItems(itemStack.getTagCompound(), shield.inventory);
            }
            if (placer instanceof EntityPlayer)
            {
                EntityPlayer player = (EntityPlayer) placer;
                shield.ownerUUID = player.getGameProfile().getId().toString();
            }
        }
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity tile, ItemStack heldStack)
    {
        player.addExhaustion(0.025F);

        if (tile instanceof TileEntityShieldGenerator)
        {
            ItemStack machine = new ItemStack(this);
            TileEntityShieldGenerator shield = (TileEntityShieldGenerator) tile;
            NBTTagCompound nbt = new NBTTagCompound();

            nbt.setFloat("ShieldSize", shield.shieldSize);
            nbt.setInteger("MaxShieldSize", shield.maxShieldSize);
            nbt.setInteger("ShieldDamage", shield.shieldDamage);
            nbt.setInteger("MaxShieldDamage", shield.maxShieldDamage);
            nbt.setInteger("ShieldCapacity", shield.shieldCapacity);
            nbt.setInteger("MaxShieldCapacity", shield.maxShieldCapacity);
            nbt.setInteger("ShieldChargeCooldown", shield.shieldChargeCooldown);
            nbt.setBoolean("NeedCharged", shield.needCharged);
            nbt.setBoolean("EnableShield", shield.enableShield);
            nbt.setBoolean("EnableDamage", shield.enableDamage);
            ItemStackHelper.saveAllItems(nbt, shield.inventory);

            if (shield.getEnergyStoredGC() > 0)
            {
                nbt.setFloat("EnergyStored", shield.getEnergyStoredGC());
            }
            machine.setTagCompound(nbt);
            Block.spawnAsEntity(world, pos, machine);
        }
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
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileEntityShieldGenerator)
        {
            ((TileEntityShieldGenerator) tile).onDestroy(tile);
        }
        if (this.hasTileEntity(state))
        {
            world.removeTileEntity(pos);
        }
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityShieldGenerator();
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
        return ColorUtils.stringToRGB(IItemRarity.SPECIAL);
    }
}