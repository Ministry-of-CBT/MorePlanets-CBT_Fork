package stevekung.mods.moreplanets.planets.nibiru.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import stevekung.mods.moreplanets.init.MPBlocks;
import stevekung.mods.moreplanets.utils.blocks.BlockFluidFiniteMP;
import stevekung.mods.moreplanets.utils.blocks.material.MaterialsBase;

public class BlockGasHelium extends BlockFluidFiniteMP
{
    public BlockGasHelium(String name)
    {
        super(MPBlocks.HELIUM_GAS, MaterialsBase.GAS);
        this.setRenderLayer(BlockRenderLayer.TRANSLUCENT);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockFluidBase.LEVEL, 7));
        this.setLightOpacity(0);
        this.setTranslationKey(name);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack itemStack)
    {
        world.setBlockState(pos, this.getDefaultState().withProperty(BlockFluidBase.LEVEL, 7), 3);
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity)
    {
        if (entity instanceof EntityLivingBase)
        {
            EntityLivingBase living = (EntityLivingBase) entity;

            if (living instanceof EntityPlayer)
            {
                if (((EntityPlayer)living).capabilities.isFlying)
                {
                    return;
                }
            }
            entity.motionY = 0.28F;
            entity.fallDistance = 0.0F;
        }
        else if (entity instanceof EntityItem)
        {
            entity.motionY = 0.32F;
            entity.fallDistance = 0.0F;
        }
        else
        {
            entity.motionY = 0.28F;
            entity.fallDistance = 0.0F;
        }
    }
}