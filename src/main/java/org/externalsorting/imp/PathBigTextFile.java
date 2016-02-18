package org.externalsorting.imp;

import org.externalsorting.BigTextFile;
import org.externalsorting.ExSortingAlgorithm;
import org.externalsorting.mergesort.TextChunk;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by ulises on 17/02/16.
 */
public class PathBigTextFile implements BigTextFile {

    private final Path file;

    public PathBigTextFile(Path filePath) {
        this.file = filePath;
    }

    @Override
    public BigTextFile sort(ExSortingAlgorithm sortingAlgorithm) {
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
    public List<TextChunk> chunks() {
        return null;
    }

    @Override
    public int passes() {
        return 0;
    }
}
