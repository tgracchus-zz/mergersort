package org.test.externalsort.imp;

import org.test.externalsort.BigTextFile;
import org.test.externalsort.ExSortingAlgorithm;

import java.io.FileNotFoundException;
import java.net.URI;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Created by ulises on 17/02/16.
 */
public class PathBigTextFile implements BigTextFile {

    private final Path file;

    public PathBigTextFile(Path filePath) {
        this.file = filePath;
    }

    @Override
    public BigTextFile sort(ExSortingAlgorithm sortingAlgorithm) throws FileNotFoundException {
        return sortingAlgorithm.sort(this);
    }

    @Override
    public String name() {
        return file.getFileName().toString();
    }

    @Override
    public URI toUri() {
        return file.toUri();
    }

    @Override
    public String toAbsolutePath() {
        return file.toAbsolutePath().toString();
    }

    @Override
    public Stream<ChunkFile> chunks() throws FileNotFoundException {

        return null;


    }

    @Override
    public int passes() {
        return 0;
    }
}
