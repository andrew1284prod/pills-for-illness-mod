package com.example.pillsforillness;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;

public class CapabilityPlayerTimer {
    @CapabilityInject(IPlayerTimer.class)
    public static final Capability<IPlayerTimer> TIMER_CAP = null;

    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
        private IPlayerTimer instance = TIMER_CAP.getDefaultInstance();

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == TIMER_CAP;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == TIMER_CAP ? TIMER_CAP.cast(instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return (NBTTagCompound) TIMER_CAP.getStorage().writeNBT(TIMER_CAP, instance, null);
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            TIMER_CAP.getStorage().readNBT(TIMER_CAP, instance, null, nbt);
        }
    }
}
