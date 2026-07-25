package com.example.pillsforillness;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import java.util.HashSet;
import java.util.Set;

public class PlayerPlacedBlocksData extends WorldSavedData {
    private static final String DATA_NAME = PillsForIllnessMod.MODID + "_placed_blocks";
    private final Set<BlockPos> placedBlocks = new HashSet<>();

    public PlayerPlacedBlocksData() {
        super(DATA_NAME);
    }

    public PlayerPlacedBlocksData(String name) {
        super(name);
    }

    public static PlayerPlacedBlocksData get(World world) {
        PlayerPlacedBlocksData instance = (PlayerPlacedBlocksData) world.getPerWorldStorage().getOrLoadData(PlayerPlacedBlocksData.class, DATA_NAME);
        if (instance == null) {
            instance = new PlayerPlacedBlocksData();
            world.getPerWorldStorage().setData(DATA_NAME, instance);
        }
        return instance;
    }

    public void addPlacedBlock(BlockPos pos) {
        placedBlocks.add(pos.toImmutable());
        markDirty();
    }

    public boolean isPlacedByPlayer(BlockPos pos) {
        return placedBlocks.contains(pos);
    }

    public void removePlacedBlock(BlockPos pos) {
        if (placedBlocks.remove(pos)) {
            markDirty();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        placedBlocks.clear();
        NBTTagList list = nbt.getTagList("blocks", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            placedBlocks.add(new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z")));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList();
        for (BlockPos pos : placedBlocks) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("x", pos.getX());
            tag.setInteger("y", pos.getY());
            tag.setInteger("z", pos.getZ());
            list.appendTag(tag);
        }
        compound.setTag("blocks", list);
        return compound;
    }
}
