package org.externalsorting.mergesort;

import org.textstring.TextString;
import org.textstring.TextStringReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sorting.SortingAlgorithm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ulises on 17/02/16.
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

    public TextChunk sort() {
        FileChannel fc = null;
        try {
            fc = new FileInputStream(file).getChannel();
            TextStringReader textStringReader = new TextStringReader(fc, StandardCharsets.UTF_8, 60);

            List<TextString> lines = new ArrayList<>();
            TextString line = textStringReader.readLine();
            while (line != null) {
                lines.add(line);
            }

            sortingAlgorithm.sort(lines);

        } catch (FileNotFoundException e) {
            log.error(e.getLocalizedMessage(), e);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        } finally {
            if (fc != null) {
                try {
                    fc.close();
                } catch (IOException e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        }
        return this;
    }

    int chunkgroup() {
        return chunkgroup;
    }


}
