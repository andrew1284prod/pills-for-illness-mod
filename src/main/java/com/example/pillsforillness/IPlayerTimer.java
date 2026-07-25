package com.example.pillsforillness;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface IPlayerTimer {
    long getTimer();
    void setTimer(long ticks);
    long getLastPillTime();
    void setLastPillTime(long time);
    boolean isPermaDead();
    void setPermaDead(boolean dead);

    void tick(World world, EntityPlayer player);
    void onPillTaken(World world, EntityPlayer player);
    void onBufferedPillTaken(World world, EntityPlayer player);
}
