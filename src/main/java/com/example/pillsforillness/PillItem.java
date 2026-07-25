package com.example.pillsforillness;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PillItem extends ItemFood {
    public PillItem() {
        super(0, 0, false); // не утоляет голод, не корм для животных
        setAlwaysEdible();
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
        if (!worldIn.isRemote) {
            IPlayerTimer timer = player.getCapability(CapabilityPlayerTimer.TIMER_CAP, null);
            if (timer != null) {
                timer.onPillTaken(worldIn, player);
            }
        }
    }
}
