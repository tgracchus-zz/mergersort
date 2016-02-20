package org.java;


import org.java.externalsort.*;
import org.java.nio.BigFile;
import org.java.nio.TFileReader;
import org.java.system.MemoryManager;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ulises on 20/02/16.
 */
public class ChunkenizerTest {

    private BigFile inFile;
    private Chunkenizer chunkenizer;

    @Before
    public void setUp() throws Exception {
        inFile = new BigFile("src/test/resources/file.txt");
        chunkenizer = new Chunkenizer(Files.createTempDirectory("chunks"));
    }

    @Test
    public void testMoreChunks() throws Exception {
        TFileReader tFileReader = new TFileReader(inFile);
        MemoryManager memoryManager = mock(MemoryManager.class);

        when(memoryManager.availableMemory()).thenReturn(MemoryManager.MEGABYTE / 2);

        MergeSortInfoProvider mergeSortInfoProvider = new MergeSortInfoProvider(memoryManager);
        MergeSortInfo mergeSortInfo = mergeSortInfoProvider.buildMergeInfo(inFile, mock(BigFile.class));


        try {
            Chunks chunkList = chunkenizer.apply(new SortBigFile(tFileReader, mergeSortInfo));
            assertEquals(mergeSortInfo.chunks(), chunkList.size());


        } finally {
            tFileReader.close();
        }

    }

    @Test
    public void testOneChunks() throws Exception {
        TFileReader tFileReader = new TFileReader(inFile);

        MergeSortInfo mergeSortInfo = new MergeSortInfo(1, inFile.size(), Arrays.asList(new PassInfo(1, 1)), mock(BigFile.class));

        try {
            Chunks chunkList = chunkenizer.apply(new SortBigFile(tFileReader, mergeSortInfo));
            assertEquals(mergeSortInfo.chunks(), chunkList.size());

        } finally {
            tFileReader.close();
        }
    }
}
