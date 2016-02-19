package org.test.externalsort.imp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.lang.TString;
import org.test.nio.TFile;
import org.test.nio.TStreamReader;
import org.test.nio.TStreamWriter;
import org.test.sort.MergeSort;
import org.test.sort.SortingAlgorithm;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by ulises on 17/02/16.
 * <p>
 * Not Thread Safe Class !!! Reference
 * http://mechanical-sympathy.blogspot.com.es/2011/12/java-sequential-io-performance.html
 */
public class ChunkFile extends TFile {

    private static final Logger log = LoggerFactory.getLogger(ChunkFile.class);

    private final int chunkgroup;
    private final SortingAlgorithm sortingAlgorithm;

    public ChunkFile(int chunkgroup, Path file, SortingAlgorithm sortingAlgorithm) {
        super(file);
        this.chunkgroup = chunkgroup;
        this.sortingAlgorithm = sortingAlgorithm;
    }

    public ChunkFile(int chunkgroup, Path file) {
        this(chunkgroup, file, new MergeSort());
    }


    public ChunkFile sort(Path destinationFile) throws IOException {
        TStreamReader TStreamReader = new TStreamReader(this);

        ChunkFile sortedChunk = new ChunkFile(chunkgroup, destinationFile, sortingAlgorithm);
        TStreamWriter tStreamWriter = new TStreamWriter(sortedChunk);
        List<TString> lines = TStreamReader.readLines();
        sortingAlgorithm.sort(lines);
        tStreamWriter.writeLines(lines);

        return sortedChunk;
    }


    public int chunkgroup() {
        return chunkgroup;
    }


}
