package org.java.externalsort;

import org.java.lang.TString;
import org.java.nio.TFileReader;
import org.java.nio.TFileWriter;
import org.java.sort.MergeSort;
import org.java.sort.SortingAlgorithm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by ulises on 20/02/16.
 */
public class Chunkenizer {


    private final SortingAlgorithm inMemorySorting;
    private final Path workingFolder;

    public Chunkenizer(SortingAlgorithm inMemorySorting, Path workingFolder) {
        this.inMemorySorting = inMemorySorting;
        this.workingFolder = workingFolder;
    }

    public Chunkenizer() throws IOException {
        this(new MergeSort(), Files.createTempDirectory("sort"));
    }

    /**
     * Chunk and sort the big file, before merge
     *
     * @param tFileReader
     * @param chunksInfo
     * @return
     * @throws IOException
     */
    public Stream<Chunk> chunks(TFileReader tFileReader, ChunksInfo chunksInfo) throws IOException {

        int passNumber = 0;
        int chunkNumber = 0;
        List<Chunk> chunks = new ArrayList<>();
        int chunkgroup;


        while (chunkNumber < chunksInfo.chunks()) {
            List<TString> lines = tFileReader.readLines(chunksInfo.chunkSize());
            if (lines.isEmpty()) {
                break;
            }
            chunkgroup = chunksInfo.bucketGroup(passNumber, chunkNumber);
            inMemorySorting.sort(lines);
            Chunk chunk = new Chunk(chunkgroup, Files.createTempFile(workingFolder, chunkName(passNumber, chunkNumber), ".txt"));

            TFileWriter tFileWriter = new TFileWriter(chunk);
            try {
                tFileWriter.writeLines(lines);
            } finally {
                tFileWriter.close();
            }

            chunks.add(chunk);
            chunkNumber++;
        }

        return chunks.stream();

    }


    private String chunkName(long passNumber, long chunkNumber) {
        return "p" + passNumber + "c" + chunkNumber + "-";
    }
}
