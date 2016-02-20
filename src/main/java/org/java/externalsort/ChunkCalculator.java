package org.java.externalsort;

import org.java.nio.BigFile;
import org.java.system.MemoryManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ulises on 19/02/16.
 */
public class ChunkCalculator {

    public final static int MAX_CHUNKS_NUMBER = 25;

    private final MemoryManager memoryManager;

    public ChunkCalculator(MemoryManager memoryManager) {
        this.memoryManager = memoryManager;
    }

    public ChunkCalculator() {
        this.memoryManager = new MemoryManager();
    }

    public MergeSortInfo calculateChunks(BigFile bigTextFile) {
        long fileSize = bigTextFile.size();
        long availableMemory = memoryManager.availableMemory();
        int pass = 1;

        int numberOfBuckets = (int)Math.ceil(((double)bigTextFile.size()) / availableMemory);
        long chunkSize = bigTextFile.size() / ((numberOfBuckets<=0) ? 1 : numberOfBuckets);
        List<PassInfo> passes = new ArrayList<>();

        if (canBeSolvedByInMemorySort(fileSize, availableMemory)) {
            passes.add(new PassInfo(pass, 1));
            return new MergeSortInfo(1,chunkSize, passes);
        }

        int iterativeNumberOfBuckets = numberOfBuckets;
        long decreasingSize = bigTextFile.size();
        while (iterativeNumberOfBuckets > 1) {

            if (iterativeNumberOfBuckets >= MAX_CHUNKS_NUMBER) {
                passes.add(new PassInfo(pass, MAX_CHUNKS_NUMBER));
                decreasingSize = decreasingSize / (MAX_CHUNKS_NUMBER * pass);
                pass++;
            } else {
                passes.add(new PassInfo(pass, iterativeNumberOfBuckets));
                decreasingSize = decreasingSize / (iterativeNumberOfBuckets * pass);
                pass++;
            }
            iterativeNumberOfBuckets = (int) (decreasingSize / availableMemory);

        }

        return new MergeSortInfo(numberOfBuckets,chunkSize, passes);
    }


    private boolean canBeSolvedByInMemorySort(long fileSize, long availableMemory) {
        return availableMemory > fileSize;
    }
}
