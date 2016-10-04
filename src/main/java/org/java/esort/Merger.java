package org.java.esort;

import org.java.esort.model.*;
import org.java.sort.QuickSort;
import org.java.sort.SortAlg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

      List<TFileReader> tFileReaders = new ArrayList<>();
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
        close(tFileReaders);
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

    List<TFileReader> tFileReaders = initReaders(chunks);
    TFileWriter writer = null;
    try {
      Chunk chunk = new Chunk(Files.createTempFile(workingFolder, "chunk", ".txt"), chunkgroup);
      writer = new TFileWriter(chunk);
      mergeLoop(tFileReaders, writer);
      return chunk;
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      close(tFileReaders);
      if (writer != null) {
        writer.close();
      }
    }
  }

  private void mergeLoop(List<TFileReader> tFileReaders, TFileWriter writer) throws RuntimeException {
    List<MergeReader> toRemoveAndClose = new ArrayList<>();
    List<MergeReader> toReadFrom = new ArrayList<>();
    for (int i = 0; i < tFileReaders.size(); i++) {
      toReadFrom.add(new MergeReader(tFileReaders.get(i), i));
    }


    try {

      while (!toReadFrom.isEmpty()) {
        MergeReader minReader = toReadFrom.get(0);
        // readLines
        TString minLine = minReader.readLine();
        if (minLine == null) {
          toRemoveAndClose.add(minReader);
        } else {
          for (int i = 1; i < toReadFrom.size(); i++) {
            MergeReader actualReader = toReadFrom.get(i);
            TString line = actualReader.readLine();
            if (line == null) {
              toRemoveAndClose.add(actualReader);
            } else {
              if (line.compareTo(minLine) < 0) {
                minLine = line;
                minReader = actualReader;
              }
            }

          }

          // write the min value
          minReader.advanceLine();
          writer.writeLine(minLine);

        }

        //close and remove
        closeAndRemove(toRemoveAndClose, toReadFrom);

      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      //close and remove
      closeAndRemove(toRemoveAndClose, toReadFrom);
    }

  }

  private void closeAndRemove(List<MergeReader> toRemoveAndClose, List<MergeReader> toReadFrom) {
    toRemoveAndClose.forEach(mergeReader -> mergeReader.close());
    toReadFrom.removeAll(toRemoveAndClose);
  }


  private static class MergeReader {

    private final TFileReader tFileReader;
    private TString lastLine = null;
    private boolean advanceLine;
    private final int readerNumber;


    public MergeReader(TFileReader tFileReader, int readerNumber) {
      this.tFileReader = tFileReader;
      this.advanceLine = true;
      this.readerNumber = readerNumber;
    }

    public TString readLine() throws IOException {
      if (this.advanceLine) {
        lastLine = tFileReader.readLine();
        this.advanceLine = false;
      }
      return lastLine;

    }

    public void advanceLine() {
      this.advanceLine = true;
    }


    public void close() {
      tFileReader.close();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      MergeReader that = (MergeReader) o;

      return readerNumber == that.readerNumber;

    }

    @Override
    public int hashCode() {
      return readerNumber;
    }
  }


  private List<TFileReader> initReaders(List<Chunk> files) {
    List<TFileReader> tFileReaders = new ArrayList<>(files.size());

    for (Chunk file : files) {
      TFileReader tFileReader = new TFileReader(file);
      tFileReaders.add(tFileReader);
    }

    return tFileReaders;
  }

  private void close(List<TFileReader> tFileReaders) {
    tFileReaders.forEach(tFileReader -> tFileReader.close());
  }

}
