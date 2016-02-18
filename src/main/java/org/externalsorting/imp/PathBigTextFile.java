package org.externalsorting.imp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.externalsorting.BigTextFile;
import org.externalsorting.ExSortingAlgorithm;
import org.externalsorting.mergesort.TextChunk;

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
    public Stream<TextChunk> chunks() throws FileNotFoundException {

return null;



    }

    @Override
    public int passes() {
        return 0;
    }
}
