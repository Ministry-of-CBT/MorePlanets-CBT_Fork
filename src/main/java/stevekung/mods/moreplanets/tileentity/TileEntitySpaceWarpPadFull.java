package stevekung.mods.moreplanets.tileentity;

import java.util.ArrayList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.blocks.BlockMulti.EnumBlockMultiType;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.moreplanets.init.MPBlocks;
import stevekung.mods.moreplanets.init.MPItems;
import stevekung.mods.stevekunglib.utils.LangUtils;

public class TileEntitySpaceWarpPadFull extends TileEntityDummy implements IMultiBlock
{
    private boolean initialised;

    public TileEntitySpaceWarpPadFull()
    {
        super("container.space_warp_pad.name");
        this.inventory = NonNullList.withSize(2, ItemStack.EMPTY);
        this.storage.setMaxExtract(75);
        this.storage.setCapacity(20000.0F);
    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        if (this.disableCooldown == 0)
        {
            this.disabled = disabled;
            this.disableCooldown = 0;
        }
    }

    @Override
    public int getPacketCooldown()
    {
        return 1;
    }

    @Override
    public void update()
    {
        if (!this.initialised)
        {
            if (!this.world.isRemote && this.world.getWorldType() != WorldType.DEBUG_ALL_BLOCK_STATES)
            {
                this.onCreate(this.world, this.getPos());
            }
            this.initialised = TileEntityDummy.initialiseMultiTiles(this.getPos(), this.world, this);
        }
        super.update();
    }

    @Override
    public boolean onActivated(EntityPlayer player)
    {
        return MPBlocks.SPACE_WARP_PAD_FULL.onBlockActivated(this.world, this.mainBlockPosition, MPBlocks.SPACE_WARP_PAD_FULL.getDefaultState(), player, player.getActiveHand(), player.getHorizontalFacing(), 0.0F, 0.0F, 0.0F);
    }

    @Override
    public void onCreate(World world, BlockPos placedPosition)
    {
        this.mainBlockPosition = placedPosition;
        this.markDirty();
        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(placedPosition, positions);
        MPBlocks.WARP_PAD_DUMMY.makeFakeBlock(world, positions, placedPosition);
    }

    @Override
    public void getPositions(BlockPos placedPosition, List<BlockPos> positions)
    {
        int y = placedPosition.getY();

        for (int x = -1; x < 2; x++)
        {
            for (int z = -1; z < 2; z++)
            {
                if (x == 0 && z == 0)
                {
                    continue;
                }
                positions.add(new BlockPos(placedPosition.getX() + x, y, placedPosition.getZ() + z));
            }
        }
    }

    @Override
    public void onDestroy(TileEntity callingBlock)
    {
        BlockPos thisBlock = this.getPos();
        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(thisBlock, positions);

        for (BlockPos pos : positions)
        {
            if (this.world.isRemote && this.world.rand.nextDouble() < 0.1D)
            {
                FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(pos, this.world.getBlockState(pos));
            }
            this.world.destroyBlock(pos, false);
        }
        this.world.destroyBlock(thisBlock, true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return this.hasWarpCore() && !this.disabled;
    }

    @Override
    public EnumFacing getElectricInputDirection()
    {
        return EnumFacing.DOWN;
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }

    @Override
    public EnumFacing getFront()
    {
        return EnumFacing.DOWN;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[] { 0 };
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemStack, EnumFacing side)
    {
        return slotID == 0;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int slotID, ItemStack itemStack)
    {
        if (slotID == 1)
        {
            return itemStack.getItem() == MPItems.SPACE_WARPER_CORE;
        }
        return slotID == 0 && ItemElectricBase.isElectricItem(itemStack.getItem());
    }

    @Override
    public EnumBlockMultiType getMultiType()
    {
        return null;
    }

    public String getGUIStatus()
    {
        if (this.getEnergyStoredGC() == 0)
        {
            return TextFormatting.DARK_RED + LangUtils.translate("gui.status.missingpower.name");
        }
        if (this.getDisabled(0))
        {
            return TextFormatting.GOLD + LangUtils.translate("gui.status.ready.name");
        }
        if (this.getEnergyStoredGC() < this.storage.getMaxExtract())
        {
            return TextFormatting.GOLD + LangUtils.translate("gui.status.missingpower.name");
        }
        if (!this.hasWarpCore())
        {
            return TextFormatting.DARK_RED + LangUtils.translate("gui.status.warp_core_required.name");
        }
        if (this.hasWarpCore() && !this.getInventory().get(1).hasTagCompound())
        {
            return TextFormatting.DARK_RED + LangUtils.translate("gui.status.empty_dimension_data.name");
        }
        return TextFormatting.DARK_GREEN + LangUtils.translate("gui.status.active.name");
    }

    public boolean hasWarpCore()
    {
        return !this.getInventory().get(1).isEmpty();
    }

    public BlockPos getDestinationPos()
    {
        if (this.hasWarpCore() && this.getInventory().get(1).hasTagCompound())
        {
            NBTTagCompound compound = this.getInventory().get(1).getTagCompound();
            return new BlockPos(compound.getInteger("X"), compound.getInteger("Y"), compound.getInteger("Z"));
        }
        return null;
    }

    public int getDimensionId()
    {
        if (this.hasWarpCore() && this.getInventory().get(1).hasTagCompound())
        {
            NBTTagCompound compound = this.getInventory().get(1).getTagCompound();
            return compound.getInteger("DimensionID");
        }
        return 0;
    }

    public String getDimensionName()
    {
        if (this.hasWarpCore() && this.getInventory().get(1).hasTagCompound())
        {
            NBTTagCompound compound = this.getInventory().get(1).getTagCompound();
            return compound.getString("DimensionName");
        }
        return null;
    }

    public float getRotationPitch()
    {
        if (this.hasWarpCore() && this.getInventory().get(1).hasTagCompound())
        {
            NBTTagCompound compound = this.getInventory().get(1).getTagCompound();
            return compound.getFloat("Pitch");
        }
        return 0.0F;
    }

    public float getRotationYaw()
    {
        if (this.hasWarpCore() && this.getInventory().get(1).hasTagCompound())
        {
            NBTTagCompound compound = this.getInventory().get(1).getTagCompound();
            return compound.getFloat("Yaw");
        }
        return 0.0F;
    }
}