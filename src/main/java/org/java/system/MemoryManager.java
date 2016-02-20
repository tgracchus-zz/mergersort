package org.java.system;

/**
 * Created by ulises on 19/02/16.
 */
public class MemoryManager {

    public static final long MEGABYTE = 1024 * 1024;

    public static final long MINIMUM_MEMORY_TO_RUN = MEGABYTE * 50;

    public long availableMemory() throws OutOfMemoryError {
        System.gc();
        long freeMemory = Runtime.getRuntime().totalMemory();
        if (freeMemory < MINIMUM_MEMORY_TO_RUN) {
            throw new OutOfMemoryError();
        }
        return (long) (freeMemory * 0.8);
    }


}
