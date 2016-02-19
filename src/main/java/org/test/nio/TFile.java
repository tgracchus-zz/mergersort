package org.test.nio;

import java.io.File;
import java.nio.file.Path;

/**
 * Created by ulises on 19/02/16.
 */
public class TFile {

    private final Path file;

    public TFile(Path file) {
        this.file = file;
    }

    public Path path() {
        return file;
    }


    public File file() {
        return file.toFile();
    }


    public void delete() {
        file.toFile().delete();
    }

}
