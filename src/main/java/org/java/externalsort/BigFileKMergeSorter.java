package org.java.externalsort;

import org.java.nio.BigFile;
import org.java.nio.TFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by ulises on 17/02/16.
 * External Sorting algorithm
 * O(n log n)
 * K pass merge sort.
 */
public class BigFileKMergeSorter implements BigFileSorter {

    private final static Logger log = LoggerFactory.getLogger(TFileReader.class);

    private final ChunkCalculator chunkCalculator;
    private final Chunkenizer chunkenizer;
    private final Merger merger;


    public BigFileKMergeSorter(ChunkCalculator chunkCalculator, Chunkenizer chunkenizer, Merger merger) throws IOException {
        this.chunkCalculator = chunkCalculator;
        this.chunkenizer = chunkenizer;
        this.merger = merger;
    }

    public BigFileKMergeSorter() throws IOException {
        this(new ChunkCalculator(), new Chunkenizer(), new Merger());
    }

    @Override
    public BigFile apply(BigFile bigFile) {
        try {
            return sort(bigFile);
        } catch (IOException e) {
            throw new FunctionException(e);
        }
    }

    private BigFile sort(BigFile bigTextFile) throws IOException {
        TFileReader tFileReader = null;
        try {
            MergeSortInfo mergeSortInfo = chunkCalculator.calculateChunks(bigTextFile);
            tFileReader = new TFileReader(bigTextFile);
            return new MergeBigFile(tFileReader, mergeSortInfo).map(chunkenizer).reduce(merger);

        } finally {
            if (tFileReader != null) {
                tFileReader.close();
            }
        }

    }


}

