package org.externalsorting;

import org.externalsorting.mergesort.TextChunk;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by ulises on 17/02/16.
 */
public interface BigTextFile {

    /**
     * Sorts the file with the given sortingAlgorithm
     *
     * @return A new BigTextFile containing the resulting sorted file
     */
    BigTextFile sort(ExSortingAlgorithm sortingAlgorithm) throws FileNotFoundException;

    String name();

    URI toUri();

    String toAbsolutePath();


    Stream<TextChunk> chunks() throws FileNotFoundException;

    int passes();
}