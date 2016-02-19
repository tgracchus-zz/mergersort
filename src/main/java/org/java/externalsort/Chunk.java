package org.java.externalsort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.java.lang.TString;
import org.java.nio.TFile;
import org.java.nio.TFileReader;
import org.java.nio.TFileWriter;
import org.java.sort.MergeSort;
import org.java.sort.SortingAlgorithm;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by ulises on 17/02/16.
 * <p>
 * Not Thread Safe Class !!! Reference
 * http://mechanical-sympathy.blogspot.com.es/2011/12/java-sequential-io-performance.html
 */
public class Chunk extends TFile {

    private static final Logger log = LoggerFactory.getLogger(Chunk.class);

    private final int chunkgroup;
    private final SortingAlgorithm sortingAlgorithm;

    public Chunk(int chunkgroup, Path file, SortingAlgorithm sortingAlgorithm) {
        super(file);
        this.chunkgroup = chunkgroup;
        this.sortingAlgorithm = sortingAlgorithm;
    }

    public Chunk(int chunkgroup, Path file) {
        this(chunkgroup, file, new MergeSort());
    }


    public Chunk sort(TFile tFile) throws IOException {
        TFileReader TFileReader = new TFileReader(this);

        Chunk sortedChunk = new Chunk(chunkgroup, tFile.path(), sortingAlgorithm);
        TFileWriter tFileWriter = new TFileWriter(sortedChunk);
        List<TString> lines = TFileReader.readLines();
        sortingAlgorithm.sort(lines);
        tFileWriter.writeLines(lines);

        return sortedChunk;
    }


    public int chunkgroup() {
        return chunkgroup;
    }


}
