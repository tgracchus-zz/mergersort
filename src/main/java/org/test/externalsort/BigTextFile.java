package org.test.externalsort;

import java.io.FileNotFoundException;
import java.net.URI;
import java.util.stream.Stream;

import org.test.externalsort.imp.ChunkFile;

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


    Stream<ChunkFile> chunks() throws FileNotFoundException;

    int passes();
}