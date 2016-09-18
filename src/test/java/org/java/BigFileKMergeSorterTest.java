package org.java;


import org.java.externalsort.*;
import org.java.nio.BigFile;
import org.java.system.MemoryManager;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ulises on 20/02/16.
 */
public class BigFileKMergeSorterTest {

    private BigFile inFile;
    private Path outFile;

    @Before
    public void setUp() throws Exception {
        inFile = new BigFile("src/test/resources/file.txt");
        outFile = Paths.get("src/test/resources/outFile.txt");

    }


    @Test
    @Ignore
    public void testMoreChunks() throws Exception {
        MemoryManager memoryManager = mock(MemoryManager.class);

        when(memoryManager.availableMemory()).thenReturn(MemoryManager.MEGABYTE / 2);

        MergeSortInfoProvider mergeSortInfoProvider = new MergeSortInfoProvider(memoryManager);
        Chunkenizer chunkenizer = new Chunkenizer();
        Merger merger = new Merger();

        ExternalSorter sorter = new BigFileKMergeSorter(mergeSortInfoProvider, chunkenizer, merger);

        CompletableFuture<BigFile> result = sorter.sort(inFile, outFile, Files.createTempDirectory("chunks"), Files.createTempDirectory("merge"));
        result.thenAccept(bigFile -> {
            assertTrue(bigFile.exists());
            bigFile.delete();
        });


    }

    @Test
    public void testOneChunks() throws Exception {
        ExternalSorter sorter = new BigFileKMergeSorter();

        CompletableFuture<BigFile> result = sorter.sort(inFile, outFile, Files.createTempDirectory("chunks"), Files.createTempDirectory("merge"));
        result.thenAccept(bigFile -> {
            System.out.println(bigFile.path());
            assertTrue(bigFile.exists());
            bigFile.delete();
        });

    }
}
