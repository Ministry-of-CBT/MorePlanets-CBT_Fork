package stevekung.mods.moreplanets.planets.nibiru.blocks;

import micdoodle8.mods.galacticraft.api.block.IPartialSealableBlock;
import micdoodle8.mods.galacticraft.api.transmission.tile.IConductor;
import micdoodle8.mods.galacticraft.api.transmission.tile.INetworkConnection;
import micdoodle8.mods.galacticraft.api.transmission.tile.ITransmitter;
import micdoodle8.mods.galacticraft.core.energy.EnergyUtil;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.moreplanets.planets.nibiru.tileentity.TileEntitySealableNuclearWasteRod;
import stevekung.mods.moreplanets.utils.BlocksItemsRegistry;
import stevekung.mods.moreplanets.utils.IDescription;
import stevekung.mods.moreplanets.utils.ItemDescription;
import stevekung.mods.moreplanets.utils.blocks.BlockBaseMP;
import stevekung.mods.moreplanets.utils.blocks.ISortableBlock;

public class BlockSealableNuclearWasteRod extends BlockBaseMP implements IPartialSealableBlock, ITileEntityProvider, IDescription, ISortableBlock
{
    public static PropertyBool UP = PropertyBool.create("up");
    public static PropertyBool DOWN = PropertyBool.create("down");
    public static PropertyBool NORTH = PropertyBool.create("north");
    public static PropertyBool EAST = PropertyBool.create("east");
    public static PropertyBool SOUTH = PropertyBool.create("south");
    public static PropertyBool WEST = PropertyBool.create("west");

    public BlockSealableNuclearWasteRod(String name)
    {
        super(Material.CLAY);
        this.setResistance(0.2F);
        this.setHardness(0.4F);
        this.setTranslationKey(name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
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
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof IConductor)
        {
            ((IConductor) tile).refresh();
        }
        if (tile instanceof INetworkConnection)
        {
            ((INetworkConnection) tile).refresh();
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntitySealableNuclearWasteRod();
    }

    @Override
    public boolean isSealed(World world, BlockPos pos, EnumFacing facing)
    {
        return true;
    }

    @Override
    public ItemDescription getDescription()
    {
        return (itemStack, list) -> list.addAll(BlocksItemsRegistry.getDescription(this.getTranslationKey() + ".description"));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, UP, DOWN, NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof ITransmitter)
        {
            TileEntity[] connectable = new TileEntity[6];
            connectable = EnergyUtil.getAdjacentPowerConnections(tileEntity);
            return state.withProperty(DOWN, connectable[EnumFacing.DOWN.ordinal()] != null).withProperty(UP, connectable[EnumFacing.UP.ordinal()] != null).withProperty(NORTH, connectable[EnumFacing.NORTH.ordinal()] != null).withProperty(EAST, connectable[EnumFacing.EAST.ordinal()] != null).withProperty(SOUTH, connectable[EnumFacing.SOUTH.ordinal()] != null).withProperty(WEST, connectable[EnumFacing.WEST.ordinal()] != null);
        }
        return state;
    }
}