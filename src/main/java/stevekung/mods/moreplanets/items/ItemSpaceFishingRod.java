package stevekung.mods.moreplanets.items;

import micdoodle8.mods.galacticraft.planets.mars.items.MarsItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.moreplanets.core.MorePlanetsMod;
import stevekung.mods.moreplanets.entity.projectile.EntitySpaceFishHook;
import stevekung.mods.moreplanets.utils.client.renderer.IItemModelRender;
import stevekung.mods.moreplanets.utils.itemblocks.IItemRarity;
import stevekung.mods.moreplanets.utils.items.EnumSortCategoryItem;
import stevekung.mods.moreplanets.utils.items.ISortableItem;
import stevekung.mods.stevekunglib.utils.ColorUtils;

public class ItemSpaceFishingRod extends ItemFishingRod implements ISortableItem, IItemModelRender, IItemRarity
{
    public ItemSpaceFishingRod(String name)
    {
        this.setMaxDamage(127);
        this.setMaxStackSize(1);
        this.setTranslationKey(name);
        this.addPropertyOverride(new ResourceLocation("cast"), (itemStack, world, living) ->
        {
            if (living == null)
            {
                return 0.0F;
            }
            else
            {
                ItemStack mainStack = living.getHeldItemMainhand();
                boolean flag = mainStack.getItem() == itemStack.getItem() && itemStack.hasTagCompound() && itemStack.getTagCompound().getBoolean("Cast");
                boolean flag1 = living.getHeldItemOffhand().getItem() == itemStack.getItem() && itemStack.hasTagCompound() && itemStack.getTagCompound().getBoolean("Cast");

                if (mainStack.getItem() instanceof ItemSpaceFishingRod && mainStack.hasTagCompound() && mainStack.getTagCompound().getBoolean("Cast"))
                {
                    flag1 = false;
                }
                return flag || flag1 ? 1.0F : 0.0F;
            }
        });
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return MorePlanetsMod.ITEM_TAB;
    }

    @Override
    public EnumSortCategoryItem getItemCategory()
    {
        return EnumSortCategoryItem.FISHING_ROD;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldRotateAroundWhenRendering()
    {
        return true;
    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int slot, boolean isSelected)
    {
        if (entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) entity;
            isSelected = player.getHeldItemMainhand().getItem() == this && player.getHeldItemMainhand().hasTagCompound() && player.getHeldItemMainhand().getTagCompound().getBoolean("Cast") || player.getHeldItemOffhand().getItem() == this && player.getHeldItemOffhand().hasTagCompound() && player.getHeldItemOffhand().getTagCompound().getBoolean("Cast");

            if (!isSelected)
            {
                if (itemStack.hasTagCompound() && itemStack.getTagCompound().getBoolean("Cast"))
                {
                    itemStack.getTagCompound().setBoolean("Cast", false);
                }
            }
            else
            {
                if (!world.isRemote)
                {
                    if (player.fishEntity == null)
                    {
                        if (itemStack.hasTagCompound() && itemStack.getTagCompound().getBoolean("Cast"))
                        {
                            itemStack.getTagCompound().setBoolean("Cast", false);
                        }
                    }
                }
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack itemStack = player.getHeldItem(hand);

        if (player.fishEntity != null)
        {
            int i = player.fishEntity.handleHookRetraction();
            itemStack.damageItem(i, player);
            player.swingArm(hand);

            if (itemStack.hasTagCompound() && itemStack.getTagCompound().getBoolean("Cast"))
            {
                itemStack.getTagCompound().setBoolean("Cast", false);
            }
        }
        else
        {
            world.playSound(null, player.getPosition(), SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

            if (!world.isRemote)
            {
                EntitySpaceFishHook entityfishhook = new EntitySpaceFishHook(world, player);
                int lure = EnchantmentHelper.getFishingSpeedBonus(itemStack);
                int luck = EnchantmentHelper.getFishingLuckBonus(itemStack);

                if (lure > 0)
                {
                    entityfishhook.setLureSpeed(lure);
                }
                if (luck > 0)
                {
                    entityfishhook.setLuck(luck);
                }
                world.spawnEntity(entityfishhook);
            }
            if (itemStack.hasTagCompound())
            {
                itemStack.getTagCompound().setBoolean("Cast", true);
            }
            else
            {
                itemStack.setTagCompound(new NBTTagCompound());
                itemStack.getTagCompound().setBoolean("Cast", true);
            }
            player.swingArm(hand);
            player.addStat(StatList.getObjectUseStats(this));
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        return repair.getItem() == MarsItems.marsItemBasic && repair.getItemDamage() == 5;
    }

    @Override
    public int getItemEnchantability()
    {
        return 1;
    }

    @Override
    public String getName()
    {
        return "space_fishing_rod";
    }

    @Override
    public ColorUtils.RGB getRarity()
    {
        return ColorUtils.stringToRGB(IItemRarity.SPECIAL);
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack)
    {
        return this.getRarity().toColoredFont() + super.getItemStackDisplayName(itemStack);
    }
}