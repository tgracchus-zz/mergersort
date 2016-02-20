package org.java;


import org.java.externalsort.*;
import org.java.nio.TFileReader;
import org.java.system.MemoryManager;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        chunkenizer = new Chunkenizer();
    }

    @Test
    public void testMoreChunks() throws Exception {
        TFileReader tFileReader = new TFileReader(inFile);
        MemoryManager memoryManager = mock(MemoryManager.class);

        when(memoryManager.availableMemory()).thenReturn(MemoryManager.MEGABYTE/2);

        PassesCalculator passesCalculator = new PassesCalculator(memoryManager);
        ChunksInfo chunksInfo = passesCalculator.calculatePasses(inFile);


        try {
            List<Chunk> chunkList = chunkenizer.chunks(tFileReader, chunksInfo).collect(Collectors.toList());
            assertEquals(chunksInfo.chunks(), chunkList.size());


        } finally {
            tFileReader.close();
        }

    }

    @Test
    public void testOneChunks() throws Exception {
        TFileReader tFileReader = new TFileReader(inFile);

        ChunksInfo chunksInfo = new ChunksInfo(1, inFile.size(), Arrays.asList(new PassInfo(1, 1)));

        try {
            List<Chunk> chunkList = chunkenizer.chunks(tFileReader, chunksInfo).collect(Collectors.toList());
            assertEquals(chunksInfo.chunks(), chunkList.size());

        } finally {
            tFileReader.close();
        }
    }
}
