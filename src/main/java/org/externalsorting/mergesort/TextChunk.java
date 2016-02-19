package org.externalsorting.mergesort;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
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
 * Reference http://mechanical-sympathy.blogspot.com.es/2011/12/java-sequential-io-performance.html
 *
 */
public class TextChunk {

    private static final Logger log = LoggerFactory.getLogger(TextChunk.class);

    private final int chunkgroup;
    private final File file;
    private final SortingAlgorithm sortingAlgorithm;

    public TextChunk(int chunkgroup, File file, SortingAlgorithm sortingAlgorithm) {
        this.chunkgroup = chunkgroup;
        this.file = file;
        this.sortingAlgorithm = sortingAlgorithm;
    }

    public TextChunk sort(File destinationFile) throws IOException {

        List<TextString> lines = readLines();
        sortingAlgorithm.sort(lines);

        return writeLines(lines, destinationFile);
    }

    private List<TextString> readLines() throws IOException {
        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(file, "r").getChannel();
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

    private TextChunk writeLines(List<TextString> lines, File destinationFile) throws IOException {

        FileChannel fc = null;
        try {

            destinationFile.delete();
            destinationFile.createNewFile();
            fc = new RandomAccessFile(destinationFile, "rw").getChannel();
            TextStringWriter textStringWriter = new TextStringWriter(fc);
            textStringWriter.writeLines(lines);
            return new TextChunk(this.chunkgroup, destinationFile, this.sortingAlgorithm);
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

}
