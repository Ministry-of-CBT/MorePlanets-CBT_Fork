package stevekung.mods.moreplanets.items;

import javax.annotation.Nullable;

import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.moreplanets.init.MPItems;
import stevekung.mods.moreplanets.module.planets.diona.items.DionaItems;
import stevekung.mods.moreplanets.module.planets.nibiru.items.NibiruItems;
import stevekung.mods.moreplanets.util.items.EnumSortCategoryItem;
import stevekung.mods.moreplanets.util.items.ItemBaseMP;

public class ItemSpaceBow extends ItemBaseMP
{
    public ItemSpaceBow(String name)
    {
        super();
        this.setMaxStackSize(1);
        this.setMaxDamage(511);
        this.setUnlocalizedName(name);

        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter()
        {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                if (entityIn == null)
                {
                    return 0.0F;
                }
                else
                {
                    ItemStack itemstack = entityIn.getActiveItemStack();
                    return itemstack != null && itemstack.getItem() == MPItems.SPACE_BOW ? (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F : 0.0F;
                }
            }
        });
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter()
        {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
    }

    //    @Override TODO Fix arrow
    //    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int timeLeft)
    //    {
    //        int useDuration = this.getMaxItemUseDuration(itemStack) - timeLeft;
    //        ArrowLooseEvent event = new ArrowLooseEvent(player, itemStack, useDuration);
    //        int power = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, itemStack);
    //        int punch = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, itemStack);
    //        ItemStack arrowStack = this.findAmmo(player);
    //        boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, itemStack) > 0;
    //        EntityArrowMP arrow = null;
    //        MinecraftForge.EVENT_BUS.post(event);
    //
    //        if (event.isCanceled())
    //        {
    //            return;
    //        }
    //
    //        useDuration = event.charge;
    //        float duration = useDuration / 20.0F;
    //        duration = (duration * duration + duration * 2.0F) / 3.0F;
    //
    //        if (duration < 0.1D)
    //        {
    //            return;
    //        }
    //        if (duration > 1.0F)
    //        {
    //            duration = 1.0F;
    //        }
    //        if (flag || arrowStack != null)
    //        {
    //            if (arrowStack == null)
    //            {
    //                arrowStack = new ItemStack(DionaItems.INFECTED_CRYSTALLIZE_ARROW);
    //            }
    //            if (arrowStack.getItem() == DionaItems.INFECTED_CRYSTALLIZE_ARROW)
    //            {
    //                arrow = new EntityInfectedCrystallizeArrow(world, player, duration * 2.0F);
    //
    //                if (duration == 1.0F)
    //                {
    //                    arrow.setIsCritical(true);
    //                }
    //                if (power > 0)
    //                {
    //                    arrow.setDamage(arrow.getDamage() + power * 0.5D + 0.5D);
    //                }
    //                if (punch > 0)
    //                {
    //                    arrow.setKnockbackStrength(punch);
    //                }
    //                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemStack) > 0)
    //                {
    //                    arrow.setFire(100);
    //                }
    //
    //                itemStack.damageItem(1, player);
    //                world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + duration * 0.5F);
    //
    //                if (flag)
    //                {
    //                    arrow.canBePickedUp = 2;
    //                }
    //                else
    //                {
    //                    player.inventory.consumeInventoryItem(DionaItems.INFECTED_CRYSTALLIZE_ARROW);
    //                }
    //                if (!world.isRemote)
    //                {
    //                    world.spawnEntityInWorld(arrow);
    //                }
    //            }
    //            else if (arrowStack.getItem() == NibiruItems.INFECTED_ARROW)
    //            {
    //                arrow = new EntityInfectedArrow(world, player, duration * 2.0F);
    //
    //                if (duration == 1.0F)
    //                {
    //                    arrow.setIsCritical(true);
    //                }
    //                if (power > 0)
    //                {
    //                    arrow.setDamage(arrow.getDamage() + power * 0.5D + 0.5D);
    //                }
    //                if (punch > 0)
    //                {
    //                    arrow.setKnockbackStrength(punch);
    //                }
    //                if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, itemStack) > 0)
    //                {
    //                    arrow.setFire(100);
    //                }
    //
    //                itemStack.damageItem(1, player);
    //                world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + duration * 0.5F);
    //
    //                if (flag)
    //                {
    //                    arrow.canBePickedUp = 2;
    //                }
    //                else
    //                {
    //                    player.inventory.consumeInventoryItem(NibiruItems.INFECTED_ARROW);
    //                }
    //                if (!world.isRemote)
    //                {
    //                    world.spawnEntityInWorld(arrow);
    //                }
    //            }
    //        }
    //    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityLivingBase living, int timeLeft)
    {
        if (living instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)living;
            boolean flag = player.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, itemStack) > 0;
            ItemStack arrowStack = this.findAmmo(player);
            int i = this.getMaxItemUseDuration(itemStack) - timeLeft;
            i = ForgeEventFactory.onArrowLoose(itemStack, world, (EntityPlayer)living, i, arrowStack != null || flag);

            if (i < 0)
            {
                return;
            }
            if (arrowStack != null || flag)
            {
                if (arrowStack == null)
                {
                    arrowStack = new ItemStack(Items.ARROW);
                }

                float velocity = ItemBow.getArrowVelocity(i);
                int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, itemStack);
                int punch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, itemStack);

                if (velocity >= 0.1D)
                {
                    boolean flag1 = player.capabilities.isCreativeMode || (arrowStack.getItem() instanceof ItemArrow ? ((ItemArrow)arrowStack.getItem()).isInfinite(arrowStack, itemStack, player) : false);

                    if (!world.isRemote)
                    {
                        ItemArrow item = (ItemArrow)(arrowStack.getItem() instanceof ItemArrow ? arrowStack.getItem() : Items.ARROW);
                        EntityArrow arrow = item.createArrow(world, arrowStack, player);
                        arrow.setAim(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity * 3.0F, 1.0F);

                        if (velocity == 1.0F)
                        {
                            arrow.setIsCritical(true);
                        }
                        if (power > 0)
                        {
                            arrow.setDamage(arrow.getDamage() + power * 0.5D + 0.5D);
                        }
                        if (punch > 0)
                        {
                            arrow.setKnockbackStrength(punch);
                        }
                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, itemStack) > 0)
                        {
                            arrow.setFire(100);
                        }
                        if (flag1)
                        {
                            arrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                        }
                        itemStack.damageItem(1, player);
                        world.spawnEntityInWorld(arrow);
                    }

                    world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);

                    if (!flag1)
                    {
                        --arrowStack.stackSize;

                        if (arrowStack.stackSize == 0)
                        {
                            player.inventory.deleteStack(arrowStack);
                        }
                    }
                    player.addStat(StatList.getObjectUseStats(this));
                }
            }
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack)
    {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack)
    {
        return EnumAction.BOW;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player, EnumHand hand)
    {
        boolean flag = this.findAmmo(player) != null;
        ActionResult<ItemStack> ret = ForgeEventFactory.onArrowNock(itemStack, world, player, hand, flag);

        if (ret != null)
        {
            return ret;
        }
        if (!player.capabilities.isCreativeMode && !flag)
        {
            return !flag ? new ActionResult(EnumActionResult.FAIL, itemStack) : new ActionResult(EnumActionResult.PASS, itemStack);
        }
        else
        {
            player.setActiveHand(hand);
            return new ActionResult(EnumActionResult.SUCCESS, itemStack);
        }
    }

    @Override
    public int getItemEnchantability()
    {
        return 4;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return repair.getItem() == MarsItems.marsItemBasic && repair.getItemDamage() == 5;
    }

    @Override
    public EnumSortCategoryItem getItemCategory(int meta)
    {
        return EnumSortCategoryItem.BOW;
    }

    private ItemStack findAmmo(EntityPlayer player)
    {
        if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND)))
        {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND)))
        {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        else
        {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                ItemStack itemStack = player.inventory.getStackInSlot(i);

                if (this.isArrow(itemStack))
                {
                    return itemStack;
                }
            }
            return null;
        }
    }

    protected boolean isArrow(ItemStack itemStack)
    {
        return itemStack != null && (itemStack.getItem() instanceof ItemArrow || itemStack.getItem() == DionaItems.INFECTED_CRYSTALLIZE_ARROW || itemStack.getItem() == NibiruItems.INFECTED_ARROW);
    }
}