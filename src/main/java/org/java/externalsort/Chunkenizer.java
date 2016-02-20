package org.java.externalsort;

import org.java.lang.Lines;
import org.java.lang.LinesMergeSort;
import org.java.lang.LinesSorter;
import org.java.nio.TFileReader;
import org.java.nio.TFileWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by ulises on 20/02/16.
 */
public class Chunkenizer implements Function<MergeBigFile, Chunks> {


    @Override
    public Chunks apply(MergeBigFile mergeBigFile) {
        try {
            return chunkenize(mergeBigFile.fileReader(), mergeBigFile.chunksInfo());
        } catch (IOException e) {
            throw new FunctionException(e);
        }
    }

    private final LinesSorter inMemorySorting;
    private final Path workingFolder;


    public Chunkenizer(LinesSorter inMemorySorting, Path workingFolder) {
        this.inMemorySorting = inMemorySorting;
        this.workingFolder = workingFolder;
    }

    public Chunkenizer() throws IOException {
        this(new LinesMergeSort(), Files.createTempDirectory("chunks"));
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
                Chunk chunk = new Chunk(Files.createTempFile(workingFolder, chunkName(chunkNumber), ".txt"),chunkgroup);
                tFileWriter = new TFileWriter(chunk);
                tFileWriter.writeLines(lines);
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
}
