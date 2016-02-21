package org.java.externalsort;

import org.java.lang.Lines;
import org.java.lang.QuickSort;
import org.java.lang.SortAlg;
import org.java.lang.TString;
import org.java.nio.BigFile;
import org.java.nio.TFile;
import org.java.nio.TFileReader;
import org.java.nio.TFileWriter;
import org.java.system.MemoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Created by ulises on 20/02/16.
 */
public class Merger implements Function<Chunks, BigFile> {

    private final static Logger log = LoggerFactory.getLogger(Merger.class);

    private final Path workingFolder;
    private final SortAlg inMemorySorting;

    public Merger(Path workingFolder, SortAlg inMemorySorting) {
        this.workingFolder = workingFolder;
        this.inMemorySorting = inMemorySorting;
    }

    public Merger(Path workingFolder) {
        this(workingFolder, new QuickSort());
    }

    @Override
    public BigFile apply(Chunks chunks) {
        try {
            return reduce(chunks);
        } catch (IOException e) {
            throw new FunctionException(e);
        }
    }

    private BigFile reduce(Chunks chunks) throws IOException {

        BigFile result;
        //Merge the map in k chunksInfo
        for (int i = 0; i < chunks.maximumPasses(); i++) {
            chunks = classifyAndReduce(chunks, i);
        }

        //When no more than one pass, the chunks size must be 1 at this point, simple return it
        if (chunks.size() == 1) {
            Chunk chunk = chunks.get(0);
            result = new BigFile(chunk.path());
        } else {

            //When more than k passes, need a final reduce
            result = chunks.reduce(chunks1 -> {

                Set<TFileReader> tFileReaders = new HashSet<>();
                TFileWriter writer = null;
                try {
                    tFileReaders = initReaders(chunks1.get());
                    BigFile bigFile = new BigFile(Files.createTempFile(workingFolder, "result", ".txt"));
                    writer = new TFileWriter(bigFile);
                    mergeLoop(tFileReaders, writer);

                    return bigFile;
                } catch (FileNotFoundException e) {
                    throw new FunctionException(e);
                } catch (IOException e) {
                    throw new FunctionException(e);
                } finally {
                    closeSet(tFileReaders);
                    writer.close();
                }
            });
        }

        Files.move(result.path(), chunks.outputFile().path(), REPLACE_EXISTING);
        log.info("Result File "+chunks.outputFile().toAbsolutePath());
        return result;
    }


    private Chunks classifyAndReduce(Chunks chunksParam, int pass) throws IOException {

        //Classify map by groupNumber and reduce
        Map<Integer, List<Chunk>> classification = chunksParam.stream()
                .collect(Collectors.groupingBy(Chunk::chunkgroup));


        List<Chunk> finalChunks = new ArrayList<>();
        int bucketNumber = 0;

        for (int key : classification.keySet()) {
            List<Chunk> chunksClassified = classification.get(key);
            int chunkgroup = chunksParam.chunksInfo().bucketGroup(pass + 1, bucketNumber);
            finalChunks.add(merge(chunksClassified, chunkgroup));
            bucketNumber++;

        }

        return new Chunks(finalChunks, chunksParam.chunksInfo());
    }


    private Chunk merge(List<Chunk> chunks, int chunkgroup) throws IOException {

        Set<TFileReader> tFileReaders = initReaders(chunks);
        TFileWriter writer = null;
        try {
            Chunk chunk = new Chunk(Files.createTempFile(workingFolder, "chunk", ".txt"), chunkgroup);
            writer = new TFileWriter(chunk);
            mergeLoop(tFileReaders, writer);
            return chunk;
        } finally {
            closeSet(tFileReaders);
            writer.close();
        }
    }

    private void mergeLoop(Set<TFileReader> tFileReaders, TFileWriter writer) throws IOException {

        Set<TFileReader> fileCopy = tFileReaders;

        while (!fileCopy.isEmpty()) {
            Set<TFileReader> toRemove = new HashSet<>();
            Lines.LinesBuilder linesBuilder = new Lines.LinesBuilder();
            //readLines
            for (TFileReader reader : fileCopy) {

                TString line = reader.readLine();
                if (line == null) {
                    toRemove.add(reader);
                } else {
                    linesBuilder.add(line);
                }

            }

            fileCopy.removeAll(toRemove);

            //Sort lines
            Lines lines = linesBuilder.build().map(inMemorySorting);

            //WriteLines
            writer.writeLines(lines);
        }
    }


    private Set<TFileReader> initReaders(List<Chunk> files) throws IOException {
        Set<TFileReader> tFileReaders = new HashSet<>(files.size());

        for (TFile file : files) {
            log.info("Reducing Chunk "+file.toAbsolutePath());
            TFileReader tFileReader = new TFileReader(file);
            tFileReaders.add(tFileReader);
        }

        return tFileReaders;
    }

    private void closeSet(Set<TFileReader> tFileReaders) {
        tFileReaders.forEach(tFileReader -> tFileReader.close());
    }


    public Path getWorkingFolder() {
        return workingFolder;
    }
}
