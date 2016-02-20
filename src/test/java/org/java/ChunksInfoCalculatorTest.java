package org.java;

import org.java.externalsort.BigFile;
import org.java.externalsort.ChunksInfo;
import org.java.externalsort.PassInfo;
import org.java.externalsort.PassesCalculator;
import org.java.system.MemoryManager;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.Matchers.longThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ulises on 19/02/16.
 */
public class ChunksInfoCalculatorTest {


    @Test
    public void testInMemoryFit() throws Exception {
        MemoryManager memoryManager = mock(MemoryManager.class);
        when(memoryManager.availableMemory()).thenReturn(MemoryManager.MEGABYTE * 10);
        PassesCalculator calculator = new PassesCalculator(memoryManager);

        BigFile file = mock(BigFile.class);
        when(file.size()).thenReturn(MemoryManager.MEGABYTE);

        ChunksInfo chunksInfo = calculator.calculatePasses(file);
        List<PassInfo> passInfos = chunksInfo.passes();

        Assert.assertEquals(1, chunksInfo.chunks());
        Assert.assertEquals(1, passInfos.size());
        Assert.assertEquals(1, passInfos.get(0).number());
        Assert.assertEquals(1, passInfos.get(0).numberOfBuckets());
    }

    @Test
    public void testBucketRoundUp() throws Exception {
        MemoryManager memoryManager = mock(MemoryManager.class);
        when(memoryManager.availableMemory()).thenReturn(MemoryManager.MEGABYTE);
        PassesCalculator calculator = new PassesCalculator(memoryManager);

        BigFile file = mock(BigFile.class);
        when(file.size()).thenReturn((long)(MemoryManager.MEGABYTE*1.5));

        ChunksInfo chunksInfo = calculator.calculatePasses(file);
        List<PassInfo> passInfos = chunksInfo.passes();

        Assert.assertEquals(2, chunksInfo.chunks());
        Assert.assertEquals(1, passInfos.size());
        Assert.assertEquals(1, passInfos.get(0).number());
        Assert.assertEquals(2, passInfos.get(0).numberOfBuckets());
    }

    @Test
    public void testOnePass() throws Exception {
        MemoryManager memoryManager = mock(MemoryManager.class);
        when(memoryManager.availableMemory()).thenReturn(MemoryManager.MEGABYTE);
        PassesCalculator calculator = new PassesCalculator(memoryManager);

        BigFile file = mock(BigFile.class);
        when(file.size()).thenReturn(MemoryManager.MEGABYTE * 10);

        ChunksInfo chunksInfo = calculator.calculatePasses(file);
        List<PassInfo> passInfos = chunksInfo.passes();

        Assert.assertEquals(10, chunksInfo.chunks());
        Assert.assertEquals(1, passInfos.size());
        Assert.assertEquals(1, passInfos.get(0).number());
        Assert.assertEquals(10, passInfos.get(0).numberOfBuckets());
    }

    @Test
    public void testKPass() throws Exception {

        MemoryManager memoryManager = mock(MemoryManager.class);
        when(memoryManager.availableMemory()).thenReturn(MemoryManager.MEGABYTE * 1);

        IntStream.range(1, 2).forEach(k -> {

            PassesCalculator calculator = new PassesCalculator(memoryManager);

            BigFile file = mock(BigFile.class);
            when(file.size()).thenReturn(MemoryManager.MEGABYTE * 500 * k);

            ChunksInfo chunksInfo = calculator.calculatePasses(file);
            List<PassInfo> passInfos = chunksInfo.passes();

            Assert.assertEquals(500, chunksInfo.chunks());
            Assert.assertEquals(2 * k, passInfos.size());
            Assert.assertEquals(k, passInfos.get(0).number());
        });


    }


}
