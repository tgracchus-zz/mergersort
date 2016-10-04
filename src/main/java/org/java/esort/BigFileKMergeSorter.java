package org.java.esort;

import org.java.esort.model.BigFile;
import org.java.esort.model.MergeSortInfo;
import org.java.esort.model.SortBigFile;
import org.java.esort.model.TFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

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


    public BigFileKMergeSorter(MergeSortInfoProvider mergeSortInfoProvider, Chunkenizer chunkenizer, Merger merger) {
        this.mergeSortInfoProvider = mergeSortInfoProvider;
        this.chunkenizer = chunkenizer;
        this.merger = merger;
    }

    public BigFileKMergeSorter() {
        this(new MergeSortInfoProvider(),
                new Chunkenizer(),
                new Merger()
        );
    }


    @Override
    public CompletableFuture<BigFile> sort(BigFile bigTextFile, Path outputFile, Path chunkDirectory, Path mergeDirectory) throws IOException {

        return CompletableFuture
                .supplyAsync(() -> {
                    MergeSortInfo mergeSortInfo = mergeSortInfoProvider.buildMergeInfo(bigTextFile, outputFile);
                    log.info("MergeSortInfo " + mergeSortInfo);
                    return new SortBigFile(bigTextFile, mergeSortInfo);
                })
                .thenApply(sortBigFile -> chunkenizer.chunkenize(sortBigFile, chunkDirectory))
                .thenApply(chunks -> merger.merge(chunks, mergeDirectory));


    }


}

