package stevekung.mods.moreplanets.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import stevekung.mods.moreplanets.planets.nibiru.entity.EntityShlime;
import stevekung.mods.moreplanets.utils.items.ItemBaseMP;

public class ItemDyeMP extends ItemBaseMP
{
    public ItemDyeMP(String name)
    {
        this.setTranslationKey(name);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack itemStack, EntityPlayer player, EntityLivingBase target, EnumHand hand)
    {
        if (target instanceof EntitySheep)
        {
            EntitySheep entitysheep = (EntitySheep)target;
            EnumDyeColor enumdyecolor = EnumDyeColor.BLUE;

            if (!entitysheep.getSheared() && entitysheep.getFleeceColor() != enumdyecolor)
            {
                entitysheep.setFleeceColor(enumdyecolor);
                itemStack.shrink(1);
            }
            return true;
        }
        if (target instanceof EntityShlime)
        {
            EntityShlime entitysheep = (EntityShlime)target;
            EnumDyeColor enumdyecolor = EnumDyeColor.BLUE;

            if (!entitysheep.getSheared() && entitysheep.getFleeceColor() != enumdyecolor)
            {
                entitysheep.setFleeceColor(enumdyecolor);
                itemStack.shrink(1);
            }
            return true;
        }
        else
        {
            return false;
        }
    }
}