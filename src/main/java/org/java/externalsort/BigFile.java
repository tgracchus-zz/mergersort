package org.java.externalsort;

import org.java.nio.TFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by ulises on 17/02/16.
 */
public class BigFile extends TFile {

    private final BigFileSorter sortingAlgorithm;

    public BigFile(Path file, BigFileSorter sortingAlgorithm) {
        super(file);
        this.sortingAlgorithm = sortingAlgorithm;
    }

    public BigFile(String file, BigFileSorter sortingAlgorithm) {
        super(file);
        this.sortingAlgorithm = sortingAlgorithm;
    }

    public BigFile(Path file) throws IOException {
        this(file, new KPassMergeSorter());
    }

    public BigFile(String file) throws IOException {
        this(file, new KPassMergeSorter());
    }


    public BigFile sort() throws IOException {
        return sortingAlgorithm.sort(this);
    }


}
