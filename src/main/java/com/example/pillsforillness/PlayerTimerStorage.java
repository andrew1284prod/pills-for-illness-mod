package com.example.pillsforillness;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class PlayerTimerStorage implements Capability.IStorage<IPlayerTimer> {
    @Override
    public NBTBase writeNBT(Capability<IPlayerTimer> capability, IPlayerTimer instance, EnumFacing side) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setLong("timer", instance.getTimer());
        tag.setLong("lastPillTime", instance.getLastPillTime());
        tag.setBoolean("permaDead", instance.isPermaDead());
        return tag;
    }

    @Override
    public void readNBT(Capability<IPlayerTimer> capability, IPlayerTimer instance, EnumFacing side, NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound) nbt;
        instance.setTimer(tag.getLong("timer"));
        instance.setLastPillTime(tag.getLong("lastPillTime"));
        instance.setPermaDead(tag.getBoolean("permaDead"));
    }
}
