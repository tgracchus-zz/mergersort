package org.java;

import org.java.externalsort.BigFileKMergeSorter;
import org.java.externalsort.ExternalSorter;
import org.java.nio.BigFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;


/**
 * Created by ulises on 20/02/16.
 */
public class Main {

    private final static Logger log = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) throws IOException {
        if (args.length == 3 || args.length == 2) {
            BigFile input = new BigFile(Paths.get(args[0]));
            BigFile outPut = new BigFile(Paths.get(args[1]));

            boolean deleteTemporalDirs = args.length == 3;

            log.info("Input file: " + input.toAbsolutePath());
            log.info("Output file: " + outPut.toAbsolutePath());
            log.info("Delete Temporal Dirs: " + deleteTemporalDirs);

            ExternalSorter sorter = new BigFileKMergeSorter();

            sorter.sort(input, outPut, deleteTemporalDirs);


        } else {
            log.info("Usage: .bin/mergesort.sh inputFile outFile deleteTmpDirectories");
            log.info("Usage: .bin/mergesort.sh inputFile outFile");
        }


    }
}
