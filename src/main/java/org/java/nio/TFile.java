package org.java.nio;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by ulises on 19/02/16.
 */
public class TFile {

    private final Path file;

    public TFile(Path file) {
        this.file = file;
    }

    public TFile(String file) {
        this.file = Paths.get(file);
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


    public String name() {
        return file.getFileName().toString();
    }


    public URI toUri() {
        return file.toUri();
    }


    public String toAbsolutePath() {
        return file.toAbsolutePath().toString();
    }

    public long size() {
        return file().length();
    }
}
