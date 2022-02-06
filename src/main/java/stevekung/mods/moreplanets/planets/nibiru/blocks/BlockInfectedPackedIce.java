package stevekung.mods.moreplanets.planets.nibiru.blocks;

import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import stevekung.mods.moreplanets.utils.blocks.BlockBaseMP;
import stevekung.mods.moreplanets.utils.blocks.IIce;

public class BlockInfectedPackedIce extends BlockBaseMP implements IIce
{
    public BlockInfectedPackedIce(String name)
    {
        super(Material.PACKED_ICE);
        this.setDefaultSlipperiness(0.98F);
        this.setHardness(0.5F);
        this.setSoundType(SoundType.GLASS);
        this.setTranslationKey(name);
    }

    @Override
    public int quantityDropped(Random rand)
    {
        return 0;
    }
}