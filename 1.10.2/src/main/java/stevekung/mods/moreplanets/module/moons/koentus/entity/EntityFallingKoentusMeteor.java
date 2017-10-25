package stevekung.mods.moreplanets.module.moons.koentus.entity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import stevekung.mods.moreplanets.module.moons.koentus.blocks.KoentusBlocks;

public class EntityFallingKoentusMeteor extends EntityFallingBlock
{
    public EntityFallingKoentusMeteor(World world)
    {
        super(world);
    }

    public EntityFallingKoentusMeteor(World world, double x, double y, double z, IBlockState fallingBlockState)
    {
        super(world, x, y, z, fallingBlockState);
    }

    @Override
    public void onUpdate()
    {
        Block block = KoentusBlocks.FALLEN_KOENTUS_METEOR;

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.fallTime++ == 0)
        {
            BlockPos blockpos = new BlockPos(this);

            if (this.worldObj.getBlockState(blockpos).getBlock() == block)
            {
                this.worldObj.setBlockToAir(blockpos);
            }
            else if (!this.worldObj.isRemote)
            {
                this.setDead();
                return;
            }
        }

        this.motionY -= 0.03999999910593033D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (!this.worldObj.isRemote)
        {
            if (this.onGround)
            {
                this.motionX *= 0.699999988079071D;
                this.motionZ *= 0.699999988079071D;
                this.motionY *= -0.5D;
                this.worldObj.playEvent(2001, this.getPosition(), Block.getStateId(KoentusBlocks.FALLEN_KOENTUS_METEOR.getDefaultState()));

                if (this.rand.nextInt(16) == 0)
                {
                    Block.spawnAsEntity(this.worldObj, this.getPosition(), new ItemStack(Items.REDSTONE));//TODO
                }

                if (this.worldObj.getBlockState(this.getPosition().down()).getBlock() == KoentusBlocks.GLOWING_HARDENED_ICE)
                {
                    this.worldObj.destroyBlock(this.getPosition().down(), false);
                    int chance = this.rand.nextInt(3);

                    for (int i = 0; i < chance; ++i)
                    {
                        Block.spawnAsEntity(this.worldObj, this.getPosition(), new ItemStack(Items.DIAMOND));//TODO
                    }
                }
                this.setDead();
            }
        }
    }

    @Override
    public void fall(float distance, float damageMultiplier) {}
}