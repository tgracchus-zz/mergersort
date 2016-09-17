package org.java.externalsort;

import org.java.nio.BigFile;

/**
 * Created by ulises on 20/02/16.
 */
public class SortBigFile {

    private final BigFile bigFile;
    private final MergeSortInfo mergeSortInfo;

    public SortBigFile(BigFile bigFile, MergeSortInfo mergeSortInfo) {
        this.bigFile = bigFile;
        this.mergeSortInfo = mergeSortInfo;
    }

    public MergeSortInfo chunksInfo() {
        return mergeSortInfo;
    }

    public BigFile bigFile() {
        return bigFile;
    }

}
