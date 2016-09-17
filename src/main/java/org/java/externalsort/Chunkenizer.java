package org.java.externalsort;

import org.java.lang.Lines;
import org.java.lang.QuickSort;
import org.java.lang.SortAlg;
import org.java.nio.TFileReader;
import org.java.nio.TFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ulises on 20/02/16.
 */
public class Chunkenizer {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final SortAlg inMemorySorting;

    public Chunkenizer(SortAlg inMemorySorting) {
        this.inMemorySorting = inMemorySorting;
    }

    public Chunkenizer() {
        this(new QuickSort());
    }


    public Chunks chunkenize(SortBigFile sortBigFile, Path workingFolder) throws RuntimeException {
        TFileReader tFileReader = new TFileReader(sortBigFile.bigFile());
        MergeSortInfo mergeSortInfo = sortBigFile.chunksInfo();

        int passNumber = 0;
        int chunkNumber = 0;
        List<Chunk> chunks = new ArrayList<>();
        int chunkgroup;

        try {
            while (chunkNumber < mergeSortInfo.chunks()) {
                Lines lines = tFileReader.readLines(mergeSortInfo.chunkSize());
                if (lines.isEmpty()) {
                    break;
                }

                lines = lines.map(inMemorySorting);

                TFileWriter tFileWriter = null;
                try {
                    chunkgroup = mergeSortInfo.bucketGroup(passNumber, chunkNumber);
                    Chunk chunk = new Chunk(Files.createTempFile(workingFolder, chunkName(chunkNumber), ".txt"), chunkgroup);
                    tFileWriter = new TFileWriter(chunk);
                    tFileWriter.writeLines(lines);
                    log.info("New Chunk " + chunk.toAbsolutePath());
                    chunks.add(chunk);
                } finally {
                    if (tFileWriter != null) {
                        tFileWriter.close();
                    }
                }

                chunkNumber++;
            }

            return new Chunks(chunks, mergeSortInfo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (tFileReader != null) {
                tFileReader.close();
            }
        }

    }


    private String chunkName(long chunkNumber) {
        return "chunk" + chunkNumber + "-";
    }

}
