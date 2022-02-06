package stevekung.mods.moreplanets.planets.diona.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.moreplanets.core.MorePlanetsMod;
import stevekung.mods.moreplanets.planets.diona.entity.EntityAlienMiner;
import stevekung.mods.moreplanets.planets.diona.tileentity.TileEntityCrashedAlienProbe;
import stevekung.mods.moreplanets.utils.CompatibilityManagerMP;
import stevekung.mods.moreplanets.utils.blocks.BlockTileMP;
import stevekung.mods.moreplanets.utils.blocks.EnumSortCategoryBlock;
import stevekung.mods.moreplanets.utils.itemblocks.IItemRarity;
import stevekung.mods.stevekunglib.utils.ColorUtils;

public class BlockCrashedAlienProbe extends BlockTileMP
{
    public static final PropertyBool HAS_ALIEN = PropertyBool.create("has_alien");

    public BlockCrashedAlienProbe(String name)
    {
        super(Material.IRON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(HAS_ALIEN, false));
        this.setSoundType(SoundType.METAL);
        this.setHardness(5.0F);
        this.setResistance(12.0F);
        this.setTranslationKey(name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer()
    {
        return CompatibilityManagerMP.isCTMLoaded ? BlockRenderLayer.CUTOUT : BlockRenderLayer.SOLID;
    }

    @Override
    public void onPlayerDestroy(World world, BlockPos pos, IBlockState state)
    {
        if (!world.isRemote)
        {
            if (state.getValue(HAS_ALIEN))
            {
                EntityAlienMiner miner = new EntityAlienMiner(world);
                miner.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, world.rand.nextFloat() * 360.0F, 0.0F);
                miner.setHealth(5.0F + world.rand.nextInt(5));
                world.spawnEntity(miner);
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileEntityCrashedAlienProbe();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        player.openGui(MorePlanetsMod.INSTANCE, -1, world, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @Override
    public EnumSortCategoryBlock getBlockCategory()
    {
        return EnumSortCategoryBlock.MACHINE_NON_BLOCK;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(HAS_ALIEN, (meta & 1) == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(HAS_ALIEN) ? 1 : 0;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, HAS_ALIEN);
    }

    @Override
    public ColorUtils.RGB getRarity()
    {
        return ColorUtils.stringToRGB(IItemRarity.ALIEN);
    }
}