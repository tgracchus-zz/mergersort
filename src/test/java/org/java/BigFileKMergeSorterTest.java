package org.java;


import org.java.externalsort.*;
import org.java.nio.BigFile;
import org.java.system.MemoryManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.file.Files;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ulises on 20/02/16.
 */
public class BigFileKMergeSorterTest {

    private BigFile inFile;
    private BigFile outFile;

    @Before
    public void setUp() throws Exception {
        inFile = new BigFile("/home/ulises/Documents/file.txt");
        outFile = new BigFile("/home/ulises/Documents/result.txt");

        //inFile = new BigFile("src/test/resources/file.txt");
        //outFile = new BigFile("src/test/resources/outFile.txt");

    }

    @After
    @Ignore
    public void tearDown() throws Exception {
       // outFile.delete();

    }

    @Test
    @Ignore
    public void testMoreChunks() throws Exception {
        MemoryManager memoryManager = mock(MemoryManager.class);

        when(memoryManager.availableMemory()).thenReturn(MemoryManager.MEGABYTE / 2);

        MergeSortInfoProvider mergeSortInfoProvider = new MergeSortInfoProvider(memoryManager);
        Chunkenizer chunkenizer = new Chunkenizer(Files.createTempDirectory("chunks"));
        Merger merger = new Merger(Files.createTempDirectory("merge"));

        BigFileSorter sorter = new BigFileKMergeSorter(mergeSortInfoProvider, chunkenizer, merger);

        sorter.sort(inFile, outFile, true);

        assertTrue(outFile.exists());

    }

    @Test
    public void testOneChunks() throws Exception {
        BigFileSorter sorter = new BigFileKMergeSorter();

        sorter.sort(inFile, outFile, true);

        System.out.println(outFile.path());
        assertTrue(outFile.exists());
    }
}
