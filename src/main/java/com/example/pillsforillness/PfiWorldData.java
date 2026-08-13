package com.example.pillsforillness;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class PfiWorldData extends WorldSavedData {
    private static final String DATA_NAME = PillsForIllnessMod.MODID + "_world_data";

    private boolean systemEnabled = true;
    private boolean initialized = false;

    public PfiWorldData() {
        super(DATA_NAME);
    }

    public PfiWorldData(String name) {
        super(name);
    }

    public static PfiWorldData get(World world) {
        World mainWorld = (world.getMinecraftServer() != null) ? world.getMinecraftServer().getEntityWorld() : world;
        MapStorage storage = mainWorld.getMapStorage();
        if (storage == null) return new PfiWorldData();

        PfiWorldData instance = (PfiWorldData) storage.getOrLoadData(PfiWorldData.class, DATA_NAME);
        if (instance == null) {
            instance = new PfiWorldData();
            storage.setData(DATA_NAME, instance);
        }
        return instance;
    }

    public boolean isSystemEnabled() {
        return systemEnabled;
    }

    public void setSystemEnabled(boolean enabled) {
        this.systemEnabled = enabled;
        markDirty();
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
        markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        systemEnabled = nbt.getBoolean("systemEnabled");
        initialized = nbt.getBoolean("initialized");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setBoolean("systemEnabled", systemEnabled);
        compound.setBoolean("initialized", initialized);
        return compound;
    }
}
