package org.shirakawatyu.filesystem.service;

import org.shirakawatyu.filesystem.pojo.DiskBlock;

import java.util.ArrayList;
import java.util.Iterator;

public class DiskService {
    private final ArrayList<DiskBlock> freeBlocks;
    private final int maxSize;
    private int nowSize;

    public DiskService(int maxSize) {
        this.maxSize = maxSize;
        this.nowSize = 0;
        this.freeBlocks = new ArrayList<>();
        initBlocks(512);
    }

    private void initBlocks(int blockSize) {
        int initSize = 0, no = 0;
        while (initSize < maxSize) {
            freeBlocks.add(new DiskBlock(no, blockSize));
            initSize += blockSize;
            no += 1;
        }
    }

    public ArrayList<DiskBlock> allocate(int size) {
        ArrayList<DiskBlock> diskBlocks = new ArrayList<>();
        int allocSize = 0;
        Iterator<DiskBlock> iterator = freeBlocks.iterator();
        while (allocSize < size) {
            if (!iterator.hasNext()) {
                return null;
            }
            DiskBlock block = iterator.next();
            allocSize += block.size;
            iterator.remove();
            diskBlocks.add(block);
        }
        nowSize += allocSize;
        return diskBlocks;
    }

    public void free(ArrayList<DiskBlock> blocks) {
        for (DiskBlock block : blocks) {
            freeBlocks.add(block);
            nowSize -= block.size;
        }
        blocks.clear();
    }

    public boolean isNotEnough(int size) {
        return nowSize + size > maxSize;
    }

    public int getFreeSize() {
        return (maxSize - nowSize);
    }
}
