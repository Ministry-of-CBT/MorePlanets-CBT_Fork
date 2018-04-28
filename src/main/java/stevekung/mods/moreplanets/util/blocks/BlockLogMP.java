package stevekung.mods.moreplanets.util.blocks;

import java.util.Iterator;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import stevekung.mods.stevekunglib.utils.BlockStateProperty;

public class BlockLogMP extends BlockBaseMP
{
    public BlockLogMP(String name)
    {
        super(Material.WOOD);
        this.setDefaultState(this.getDefaultState().withProperty(BlockStateProperty.AXIS, BlockStateProperty.EnumAxis.Y));
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setSoundType(SoundType.WOOD);
        this.setUnlocalizedName(name);
    }

    public BlockLogMP(String name, Material material)
    {
        super(material);
        this.setDefaultState(this.getDefaultState().withProperty(BlockStateProperty.AXIS, BlockStateProperty.EnumAxis.Y));
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setSoundType(SoundType.WOOD);
        this.setUnlocalizedName(name);
    }

    public BlockLogMP(Material material)
    {
        super(material);
        this.setDefaultState(this.getDefaultState().withProperty(BlockStateProperty.AXIS, BlockStateProperty.EnumAxis.Y));
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setSoundType(SoundType.WOOD);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState state = this.getDefaultState();

        switch (meta & 12)
        {
        case 0:
            state = state.withProperty(BlockStateProperty.AXIS, BlockStateProperty.EnumAxis.Y);
            break;
        case 4:
            state = state.withProperty(BlockStateProperty.AXIS, BlockStateProperty.EnumAxis.X);
            break;
        case 8:
            state = state.withProperty(BlockStateProperty.AXIS, BlockStateProperty.EnumAxis.Z);
            break;
        default:
            state = state.withProperty(BlockStateProperty.AXIS, BlockStateProperty.EnumAxis.NONE);
        }
        return state;
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;

        switch (BlockStateProperty.SwitchEnumAxis.AXIS_LOOKUP[state.getValue(BlockStateProperty.AXIS).ordinal()])
        {
        case 1:
            i |= 4;
            break;
        case 2:
            i |= 8;
            break;
        case 3:
            i |= 12;
        }
        return i;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, BlockStateProperty.AXIS);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(BlockStateProperty.AXIS, BlockStateProperty.EnumAxis.fromFacingAxis(facing.getAxis()));
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        byte b0 = 4;
        int i = b0 + 1;

        if (world.isAreaLoaded(pos.add(-i, -i, -i), pos.add(i, i, i)))
        {
            Iterator iterator = BlockPos.getAllInBox(pos.add(-b0, -b0, -b0), pos.add(b0, b0, b0)).iterator();

            while (iterator.hasNext())
            {
                BlockPos blockpos1 = (BlockPos)iterator.next();
                IBlockState iblockstate1 = world.getBlockState(blockpos1);

                if (iblockstate1.getBlock().isLeaves(iblockstate1, world, blockpos1))
                {
                    iblockstate1.getBlock().beginLeavesDecay(iblockstate1, world, blockpos1);
                }
            }
        }
    }

    @Override
    public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return state.getMaterial() == Material.WOOD;
    }

    @Override
    public boolean isWood(IBlockAccess world, BlockPos pos)
    {
        return world.getBlockState(pos).getMaterial() == Material.WOOD;
    }
}