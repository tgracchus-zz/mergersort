package org.java.esort.model;

import java.nio.file.Path;
import java.util.List;

/**
 * Created by ulises on 19/02/16.
 */
public class MergeSortInfo {

    private final int chunks;
    private final long chunkSize;
    private final List<PassInfo> passes;
    private final Path outputFile;

    public MergeSortInfo(int chunks, long chunkSize, List<PassInfo> passes, Path outputFile) {
        this.chunks = chunks;
        this.chunkSize = chunkSize;
        this.passes = passes;
        this.outputFile = outputFile;
    }

    public Path outputFile() {
        return outputFile;
    }

    public int chunks() {
        return chunks;
    }

    public long chunkSize() {
        return chunkSize;
    }


    public int bucketGroup(int pass, int bucketNumber) {
        if (pass < passes.size()) {
            return bucketNumber / passes.get(pass).numberOfBuckets();
        } else {
            return 1;
        }
    }

    public List<PassInfo> passes() {
        return passes;
    }

    public int maximumPasses() {
        if (passes.size() > 1) {
            return passes.size() - 1;
        } else {
            return passes.size();
        }

    }

    @Override
    public String toString() {
        return "MergeSortInfo{" +
                "chunks=" + chunks +
                ", chunkSize=" + chunkSize +
                ", passes=" + passes +
                ", outputFile=" + outputFile +
                '}';
    }
}
