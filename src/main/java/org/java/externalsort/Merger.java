package org.java.externalsort;

import org.java.lang.Lines;
import org.java.lang.QuickSort;
import org.java.lang.SortAlg;
import org.java.lang.TString;
import org.java.nio.BigFile;
import org.java.nio.TFileReader;
import org.java.nio.TFileWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Created by ulises on 20/02/16.
 */
public class Merger {

    private final static Logger log = LoggerFactory.getLogger(Merger.class);

    private final SortAlg inMemorySorting;

    public Merger(SortAlg inMemorySorting) {
        this.inMemorySorting = inMemorySorting;
    }

    public Merger() {
        this(new QuickSort());
    }

    public BigFile merge(Chunks chunks, Path workingFolder) throws RuntimeException {

        final BigFile result;
        // Merge the map in k chunksInfo
        for (int i = 0; i < chunks.maximumPasses(); i++) {
            chunks = classifyAndReduce(chunks, i, workingFolder);
        }

        // When no more than one pass, the chunks size must be 1 at this point, simple return it
        if (chunks.size() == 1) {
            Chunk chunk = chunks.get(0);
            result = new BigFile(chunk.path());
        } else {

            chunks.stream().forEach(file -> {
                log.info("Reducing Chunk " + file.toAbsolutePath());
            });

            // When more than k passes, need a final reduce

            Set<TFileReader> tFileReaders = new HashSet<>();
            TFileWriter writer = null;
            try {
                tFileReaders = initReaders(chunks.get());
                BigFile bigFile = new BigFile(Files.createTempFile(workingFolder, "result", ".txt"));
                writer = new TFileWriter(bigFile);
                mergeLoop(tFileReaders, writer);

                result = bigFile;
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                closeSet(tFileReaders);
                writer.close();
            }

        }
        try {
            Files.move(result.path(), chunks.outputFile(), REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("Result File " + chunks.outputFile().toAbsolutePath());
        return result;

    }

    private Chunks classifyAndReduce(Chunks chunksParam, int pass, Path workingFolder) throws RuntimeException {

        // Classify map by groupNumber and reduce
        Map<Integer, List<Chunk>> classification = chunksParam.stream().collect(Collectors.groupingBy(
                Chunk::chunkgroup));

        List<Chunk> finalChunks = new ArrayList<>();
        int bucketNumber = 0;

        log.info("Reducing pass " + pass);
        for (int key : classification.keySet()) {
            List<Chunk> chunksClassified = classification.get(key);

            int chunkgroup = chunksParam.chunksInfo().bucketGroup(pass + 1, bucketNumber);
            log.info("Next group for " + chunkgroup + " chunkgroup " + " and pass " + pass);
            chunksClassified.stream().forEach(file -> {
                log.info("Reducing Chunk " + file.toAbsolutePath() + " with pass/group " + pass + "/" + file
                        .chunkgroup());
            });

            Chunk chunk = merge(chunksClassified, chunkgroup, workingFolder);
            finalChunks.add(chunk);
            bucketNumber++;
        }

        return new Chunks(finalChunks, chunksParam.chunksInfo());
    }

    private Chunk merge(List<Chunk> chunks, int chunkgroup, Path workingFolder) throws RuntimeException {

        Set<TFileReader> tFileReaders = initReaders(chunks);
        TFileWriter writer = null;
        try {
            Chunk chunk = new Chunk(Files.createTempFile(workingFolder, "chunk", ".txt"), chunkgroup);
            writer = new TFileWriter(chunk);
            mergeLoop(tFileReaders, writer);
            return chunk;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeSet(tFileReaders);
            if (writer != null) {
                writer.close();
            }
        }
    }

    private void mergeLoop(Set<TFileReader> tFileReaders, TFileWriter writer) throws RuntimeException {
        Set<TFileReader> toRemoveAndClose = new HashSet<>();
        try {

            while (!tFileReaders.isEmpty()) {
                Lines.LinesBuilder linesBuilder = new Lines.LinesBuilder(tFileReaders.size());

                // readLines
                for (TFileReader reader : tFileReaders) {
                    TString line = reader.readLine();
                    if (line == null) {
                        toRemoveAndClose.add(reader);
                    } else {
                        linesBuilder.add(line);
                    }
                }

                disposeIfReaderEmpty(toRemoveAndClose, tFileReaders);

                // Sort lines
                Lines lines = linesBuilder.build().map(inMemorySorting);

                // WriteLines
                writer.writeLines(lines);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            closeSet(toRemoveAndClose);
        }

    }

    private void disposeIfReaderEmpty(Set<TFileReader> toRemoveAndClose, Set<TFileReader> tFileReaders) {
        if (!toRemoveAndClose.isEmpty()) {
            tFileReaders.removeAll(toRemoveAndClose);
            closeSet(toRemoveAndClose);
            toRemoveAndClose.clear();
        }
    }

    private Set<TFileReader> initReaders(List<Chunk> files) {
        Set<TFileReader> tFileReaders = new HashSet<>(files.size());

        for (Chunk file : files) {
            TFileReader tFileReader = new TFileReader(file);
            tFileReaders.add(tFileReader);
        }

        return tFileReaders;
    }

    private void closeSet(Set<TFileReader> tFileReaders) {
        tFileReaders.forEach(tFileReader -> tFileReader.close());
    }

}
