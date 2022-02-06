package stevekung.mods.moreplanets.blocks;

import java.util.Collection;
import java.util.Locale;
import java.util.Random;

import javax.annotation.Nullable;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.core.util.FluidUtil;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.moreplanets.init.MPBlocks;
import stevekung.mods.moreplanets.init.MPItems;
import stevekung.mods.moreplanets.planets.nibiru.tileentity.TileEntityNuclearWasteTank;
import stevekung.mods.moreplanets.tileentity.TileEntityDarkEnergyReceiver;
import stevekung.mods.moreplanets.tileentity.TileEntityDummy;
import stevekung.mods.moreplanets.utils.blocks.BlockContainerMP;

public class BlockDummy extends BlockContainerMP implements IPartialSealableBlock
{
    private static final AxisAlignedBB AABB_WARP_PAD = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D);
    private static final AxisAlignedBB AABB_DER_SOLAR1 = new AxisAlignedBB(-0.25D, 0.3D, 0.3D, 0.5D, 0.6D, 0.7D);
    private static final AxisAlignedBB AABB_DER_SOLAR2 = new AxisAlignedBB(0.5D, 0.3D, 0.3D, 1.25D, 0.6D, 0.7D);
    private static final AxisAlignedBB AABB_DER_SOLAR3 = new AxisAlignedBB(0.3D, 0.3D, -0.25D, 0.7D, 0.6D, 0.5D);
    private static final AxisAlignedBB AABB_DER_SOLAR4 = new AxisAlignedBB(0.3D, 0.3D, 0.5D, 0.7D, 0.6D, 1.25D);
    private final BlockType type;

    public BlockDummy(String name, BlockType type)
    {
        super(Material.IRON);
        this.setHardness(1.0F);
        this.setSoundType(SoundType.METAL);
        this.setTranslationKey(name);
        this.type = type;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        if (this.type == BlockType.WARP_PAD)
        {
            return BlockDummy.AABB_WARP_PAD;
        }
        else if (this.type == BlockType.DARK_ENERGY_SOLAR1)
        {
            return BlockDummy.AABB_DER_SOLAR1;
        }
        else if (this.type == BlockType.DARK_ENERGY_SOLAR2)
        {
            return BlockDummy.AABB_DER_SOLAR2;
        }
        else if (this.type == BlockType.DARK_ENERGY_SOLAR3)
        {
            return BlockDummy.AABB_DER_SOLAR3;
        }
        else if (this.type == BlockType.DARK_ENERGY_SOLAR4)
        {
            return BlockDummy.AABB_DER_SOLAR4;
        }
        else
        {
            return Block.FULL_BLOCK_AABB;
        }
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosion)
    {
        return false;
    }

    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos)
    {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileEntityDummy)
        {
            BlockPos mainBlockPosition = ((TileEntityDummy) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
            {
                TileEntity tileOther = world.getTileEntity(mainBlockPosition);

                if (tileOther instanceof TileEntityDarkEnergyReceiver)
                {
                    TileEntityDarkEnergyReceiver dark = (TileEntityDarkEnergyReceiver) tileOther;

                    if ((dark.activated || dark.failed) && !dark.successful)
                    {
                        return -1.0F;
                    }
                }
                return world.getBlockState(mainBlockPosition).getBlockHardness(world, mainBlockPosition);
            }
        }
        return this.blockHardness;
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion)
    {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileEntityDummy)
        {
            BlockPos mainBlockPosition = ((TileEntityDummy) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
            {
                return world.getBlockState(mainBlockPosition).getBlock().getExplosionResistance(world, mainBlockPosition, exploder, explosion);
            }
        }
        return super.getExplosionResistance(world, pos, exploder, explosion);
    }

    @Override
    public boolean isSealed(World world, BlockPos pos, EnumFacing facing)
    {
        if (this.type == BlockType.WARP_PAD)
        {
            return facing == EnumFacing.DOWN;
        }
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileEntityDummy)
        {
            ((TileEntityDummy) tileEntity).onBlockRemoval();
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        TileEntityDummy tileEntity = (TileEntityDummy) world.getTileEntity(pos);
        ItemStack heldStack = player.getHeldItem(hand);

        if (tileEntity == null)
        {
            return false;
        }
        return this.onNuclearTankActivated(world, pos, this.type == BlockType.NUCLEAR_WASTE_TANK_MIDDLE ? pos.down() : pos.down(2), player, heldStack) || tileEntity.onBlockActivated(world, pos, player);
    }

    @Override
    public int quantityDropped(Random rand)
    {
        return 0;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityDummy();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileEntityDummy)
        {
            BlockPos mainBlockPosition = ((TileEntityDummy) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null && !mainBlockPosition.equals(pos))
            {
                Block mainBlock = world.getBlockState(mainBlockPosition).getBlock();

                if (Blocks.AIR != mainBlock)
                {
                    return mainBlock.getPickBlock(state, target, world, mainBlockPosition, player);
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean addHitEffects(IBlockState state, World world, RayTraceResult target, ParticleManager manager)
    {
        TileEntity tileEntity = world.getTileEntity(target.getBlockPos());

        if (tileEntity instanceof TileEntityDummy)
        {
            BlockPos mainBlockPosition = ((TileEntityDummy) tileEntity).mainBlockPosition;

            if (mainBlockPosition != null && !mainBlockPosition.equals(target.getBlockPos()))
            {
                manager.addBlockHitEffects(mainBlockPosition, target);
            }
        }
        return super.addHitEffects(state, world, target, manager);
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing facing)
    {
        return this.type == BlockType.NUCLEAR_WASTE_TANK_TOP ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    public void makeFakeBlock(World world, Collection<BlockPos> posList, BlockPos mainBlock)
    {
        for (BlockPos pos : posList)
        {
            world.setBlockState(pos, this.getDefaultState(), Constants.BlockFlags.DEFAULT);
            world.setTileEntity(pos, new TileEntityDummy(mainBlock));
        }
    }

    public void makeFakeBlock(World world, BlockPos pos, BlockPos mainBlock)
    {
        world.setBlockState(pos, this.getDefaultState(), 3);
        world.setTileEntity(pos, new TileEntityDummy(mainBlock));
    }

    private boolean onNuclearTankActivated(World world, BlockPos pos, BlockPos detectPos, EntityPlayer player, ItemStack heldStack)
    {
        if (world.getTileEntity(detectPos) instanceof TileEntityNuclearWasteTank && world.getBlockState(detectPos) == MPBlocks.NUCLEAR_WASTE_TANK.getDefaultState())
        {
            TileEntityNuclearWasteTank tank = (TileEntityNuclearWasteTank) world.getTileEntity(detectPos);

            if (!heldStack.isEmpty())
            {
                if (tank.hasRod && !tank.createRod)
                {
                    if (heldStack.getItem() == MPItems.WASTE_ROD_PICKER)
                    {
                        if (!player.capabilities.isCreativeMode)
                        {
                            heldStack.damageItem(1, player);
                        }
                        Block.spawnAsEntity(world, pos, new ItemStack(MPItems.NUCLEAR_WASTE_ROD));
                        tank.hasRod = false;
                        return true;
                    }
                }
                else
                {
                    int slot = player.inventory.currentItem;
                    FluidActionResult result = FluidUtil.interactWithFluidHandler(player.inventory.getCurrentItem(), tank.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null), player);

                    if (result.isSuccess())
                    {
                        tank.createRod = true;
                        tank.setTime();
                        player.inventory.setInventorySlotContents(slot, result.result);

                        if (player.inventoryContainer != null)
                        {
                            player.inventoryContainer.detectAndSendChanges();
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public enum BlockType
    {
        WARP_PAD,
        DARK_ENERGY_SOLAR1,
        DARK_ENERGY_SOLAR2,
        DARK_ENERGY_SOLAR3,
        DARK_ENERGY_SOLAR4,
        NUCLEAR_WASTE_TANK_MIDDLE,
        NUCLEAR_WASTE_TANK_TOP,
        SHIELD_GENERATOR_TOP;

        @Override
        public String toString()
        {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}