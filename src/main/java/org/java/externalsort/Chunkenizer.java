package org.java.externalsort;

import org.java.lang.Lines;
import org.java.lang.QuickSort;
import org.java.lang.SortAlg;
import org.java.nio.TFileReader;
import org.java.nio.TFileWriter;
import org.java.system.MemoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by ulises on 20/02/16.
 */
public class Chunkenizer implements Function<SortBigFile, Chunks> {

    private final static Logger log = LoggerFactory.getLogger(Chunkenizer.class);

    @Override
    public Chunks apply(SortBigFile sortBigFile) {
        try {
            return chunkenize(sortBigFile.fileReader(), sortBigFile.chunksInfo());
        } catch (IOException e) {
            throw new FunctionException(e);
        }
    }

    private final SortAlg inMemorySorting;
    private final Path workingFolder;


    public Chunkenizer(SortAlg inMemorySorting, Path workingFolder) {
        this.inMemorySorting = inMemorySorting;
        this.workingFolder = workingFolder;
    }

    public Chunkenizer(Path workingFolder) {
        this(new QuickSort(), workingFolder);
    }

    /**
     * Chunk and sort the big file, before merge
     *
     * @param tFileReader
     * @param mergeSortInfo
     * @return
     * @throws IOException
     */
    private Chunks chunkenize(TFileReader tFileReader, MergeSortInfo mergeSortInfo) throws IOException {

        int passNumber = 0;
        int chunkNumber = 0;
        List<Chunk> chunks = new ArrayList<>();
        int chunkgroup;


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
                log.info("New Chunk "+chunk.toAbsolutePath());
                chunks.add(chunk);
            } finally {
                if (tFileWriter != null) {
                    tFileWriter.close();
                }
            }

            chunkNumber++;
        }

        return new Chunks(chunks, mergeSortInfo);

    }


    private String chunkName(long chunkNumber) {
        return "chunk" + chunkNumber + "-";
    }

    public Path getWorkingFolder() {
        return workingFolder;
    }
}
