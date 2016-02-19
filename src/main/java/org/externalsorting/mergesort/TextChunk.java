package org.externalsorting.mergesort;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sorting.SortingAlgorithm;
import org.textstring.TextString;
import org.textstring.TextStringReader;
import org.textstring.TextStringWriter;

/**
 * Created by ulises on 17/02/16.
 *
 * Not Thread Safe Class !!! Reference
 * http://mechanical-sympathy.blogspot.com.es/2011/12/java-sequential-io-performance.html
 *
 */
public class TextChunk {

    private static final Logger log = LoggerFactory.getLogger(TextChunk.class);

    private final int chunkgroup;
    private final Path file;
    private final SortingAlgorithm sortingAlgorithm;

    public TextChunk(int chunkgroup, Path file, SortingAlgorithm sortingAlgorithm) {
        this.chunkgroup = chunkgroup;
        this.file = file;
        this.sortingAlgorithm = sortingAlgorithm;
    }

    public TextChunk sort(Path destinationFile) throws IOException {

        List<TextString> lines = readLines();
        sortingAlgorithm.sort(lines);

        return writeLines(lines, destinationFile);
    }

    private List<TextString> readLines() throws IOException {
        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(file.toFile(), "r").getChannel();
            TextStringReader textStringReader = new TextStringReader(fc);
            List<TextString> lines = new ArrayList<>();

            TextString line;
            while (null != (line = textStringReader.readLine())) {
                lines.add(line);
            }
            return lines;
        } finally {
            closeFcSilently(fc);
        }
    }

    private TextChunk writeLines(List<TextString> lines, Path destinationPath) throws IOException {

        FileChannel fc = null;
        try {

            File destinationFile = destinationPath.toFile();
            destinationFile.delete();
            destinationFile.createNewFile();
            fc = new RandomAccessFile(destinationFile, "rw").getChannel();
            TextStringWriter textStringWriter = new TextStringWriter(fc);
            textStringWriter.writeLines(lines);
            return new TextChunk(this.chunkgroup, destinationPath, this.sortingAlgorithm);
        } finally {
            closeFcSilently(fc);
        }
    }

    private void closeFcSilently(FileChannel fc) {
        if (fc != null) {
            try {
                fc.close();
            } catch (IOException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    public int chunkgroup() {
        return chunkgroup;
    }

    public void delete() {
        file.toFile().delete();
    }
}
