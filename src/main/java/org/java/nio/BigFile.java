package org.java.nio;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Function;

/**
 * Created by ulises on 17/02/16.
 */
public class BigFile extends TFile {


    public BigFile(Path file) {
        super(file);
    }

    public BigFile(String file) {
        super(file);
    }



}
