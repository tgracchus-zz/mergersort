package org.java.externalsort;

import org.java.system.MemoryManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ulises on 19/02/16.
 */
public class PassesCalculator {

    public final static int MAX_CHUNKS_NUMBER = 25;

    private final MemoryManager memoryManager;

    public PassesCalculator(MemoryManager memoryManager) {
        this.memoryManager = memoryManager;
    }

    public PassesCalculator() {
        this.memoryManager = new MemoryManager();
    }

    public Passes calculatePasses(BigFile bigTextFile) {
        long fileSize = bigTextFile.size();
        long availableMemory = memoryManager.availableMemory();
        int pass = 1;
        long chunkSize = bigTextFile.size();
        int numberOfBuckets = (int) (chunkSize / availableMemory);
        List<PassInfo> passInfos = new ArrayList<>();

        if (canBeSolvedByInMemorySort(fileSize, availableMemory)) {
            passInfos.add(new PassInfo(pass, 1));
            return new Passes(1, passInfos);
        }

        int totalBuckets = numberOfBuckets;
        while (numberOfBuckets > 1) {

            if (numberOfBuckets >= MAX_CHUNKS_NUMBER) {
                passInfos.add(new PassInfo(pass, MAX_CHUNKS_NUMBER));
                chunkSize = chunkSize / (MAX_CHUNKS_NUMBER * pass);
                pass++;
            } else {
                passInfos.add(new PassInfo(pass, numberOfBuckets));
                chunkSize = chunkSize / (numberOfBuckets * pass);
                pass++;
            }
            numberOfBuckets = (int) (chunkSize / availableMemory);

        }

        return new Passes(totalBuckets, passInfos);
    }


    private boolean canBeSolvedByInMemorySort(long fileSize, long availableMemory) {
        return availableMemory > fileSize;
    }
}
