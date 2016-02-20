package org.java.externalsort;

import org.java.nio.BigFile;
import org.java.nio.TFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by ulises on 17/02/16.
 * External Sorting algorithm
 * O(n log n)
 * K pass merge sort.
 */
public class BigFileKMergeSorter implements ExternalSorter {

    private final static Logger log = LoggerFactory.getLogger(TFileReader.class);

    private final MergeSortInfoProvider mergeSortInfoProvider;
    private final Chunkenizer chunkenizer;
    private final Merger merger;

    private final Path chunkDirectory;
    private final Path mergeDirectory;


    public BigFileKMergeSorter(MergeSortInfoProvider mergeSortInfoProvider, Chunkenizer chunkenizer, Merger merger) {
        this.mergeSortInfoProvider = mergeSortInfoProvider;
        this.chunkenizer = chunkenizer;
        this.merger = merger;
        this.chunkDirectory = chunkenizer.getWorkingFolder();
        this.mergeDirectory = merger.getWorkingFolder();
    }

    public BigFileKMergeSorter() throws IOException {
        this(new MergeSortInfoProvider(),
                new Chunkenizer(Files.createTempDirectory("chunks")),
                new Merger(Files.createTempDirectory("merge")));
    }


    @Override
    public void sort(BigFile bigTextFile, BigFile outputFile, boolean deleteTemporalDirs) throws IOException {
        TFileReader tFileReader = null;
        try {
            MergeSortInfo mergeSortInfo = mergeSortInfoProvider.buildMergeInfo(bigTextFile, outputFile);
            tFileReader = new TFileReader(bigTextFile);
            new SortBigFile(tFileReader, mergeSortInfo).map(chunkenizer).reduce(merger);

        } finally {
            if (tFileReader != null) {
                tFileReader.close();
            }
            if (deleteTemporalDirs) {
                chunkDirectory.toFile().delete();
                mergeDirectory.toFile().delete();
            }
        }

    }


}

