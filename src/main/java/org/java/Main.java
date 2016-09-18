package org.java;

import org.java.externalsort.BigFileKMergeSorter;
import org.java.externalsort.ExternalSorter;
import org.java.nio.BigFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;


/**
 * Created by ulises on 20/02/16.
 */
public class Main {

    private final static Logger log = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        if (args.length == 3 || args.length == 2) {
            BigFile input = new BigFile(Paths.get(args[0]));
            Path outPut = Paths.get(args[1]);

            boolean deleteTemporalDirs = args.length == 3;

            log.info("Input file: " + input.toAbsolutePath());
            log.info("Output file: " + outPut.toAbsolutePath());
            log.info("Delete Temporal Dirs: " + deleteTemporalDirs);

            Path chunkDirectory = null;
            Path mergeDirectory = null;

            try {
                chunkDirectory = Files.createTempDirectory("chunks");
                mergeDirectory = Files.createTempDirectory("merge");

                ExternalSorter sorter = new BigFileKMergeSorter();
                sorter.sort(input, outPut, chunkDirectory, mergeDirectory)
                        .thenAccept(bigFile -> log.info("Result at " + bigFile.toAbsolutePath())).get();

            } finally {
                if (deleteTemporalDirs) {
                    chunkDirectory.toFile().delete();
                    mergeDirectory.toFile().delete();
                }
            }

        } else {
            log.info("Usage: .bin/mergesort.sh inputFile outFile deleteTmpDirectories");
            log.info("Usage: .bin/mergesort.sh inputFile outFile");
        }


    }
}
