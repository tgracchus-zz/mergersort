package org.java.externalsort;

import org.java.nio.BigFile;
import org.java.system.MemoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ulises on 19/02/16.
 */
public class MergeSortInfoProvider {

    public final static int MAX_CHUNKS_NUMBER = 25;
    private final static Logger log = LoggerFactory.getLogger(MergeSortInfoProvider.class);

    private final MemoryManager memoryManager;

    public MergeSortInfoProvider(MemoryManager memoryManager) {
        this.memoryManager = memoryManager;
    }

    public MergeSortInfoProvider() {
        this.memoryManager = new MemoryManager();
    }

    public MergeSortInfo buildMergeInfo(BigFile bigTextFile, BigFile outputFile) {
        long fileSize = bigTextFile.size();
        long availableMemory = memoryManager.availableMemory();
        int pass = 1;

        int numberOfBuckets = (int) Math.ceil(((double) bigTextFile.size()) / availableMemory);
        long chunkSize = bigTextFile.size() / ((numberOfBuckets <= 0) ? 1 : numberOfBuckets);

        log.info("Available Memory "+availableMemory/MemoryManager.MEGABYTE+" MB");
        log.info("Number Of Buckets "+numberOfBuckets);
        log.info("Bucket Size "+chunkSize/MemoryManager.MEGABYTE+" MB");

        List<PassInfo> passes = new ArrayList<>();

        if (canBeSolvedByInMemorySort(fileSize, availableMemory)) {
            return new MergeSortInfo(1, chunkSize, passes, outputFile);
        }

        calculateKpasses(bigTextFile, availableMemory, pass, numberOfBuckets, passes);

        return new MergeSortInfo(numberOfBuckets, chunkSize, passes, outputFile);
    }

    private void calculateKpasses(BigFile bigTextFile, long availableMemory, int pass, int numberOfBuckets, List<PassInfo> passes) {
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
    }


    private boolean canBeSolvedByInMemorySort(long fileSize, long availableMemory) {
        return availableMemory > fileSize;
    }
}
