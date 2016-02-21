package org.java.externalsort;

import org.java.nio.BigFile;

import java.util.List;

/**
 * Created by ulises on 19/02/16.
 */
public class MergeSortInfo {

    private final int chunks;
    private final long chunkSize;
    private final List<PassInfo> passes;
    private final BigFile outputFile;

    public MergeSortInfo(int chunks, long chunkSize, List<PassInfo> passes, BigFile outputFile) {
        this.chunks = chunks;
        this.chunkSize = chunkSize;
        this.passes = passes;
        this.outputFile = outputFile;
    }

    public BigFile outputFile() {
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
