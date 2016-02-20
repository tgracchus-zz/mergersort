package org.java;

import org.java.externalsort.MergeSortInfo;
import org.java.externalsort.MergeSortInfoProvider;
import org.java.externalsort.PassInfo;
import org.java.nio.BigFile;
import org.java.system.MemoryManager;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by ulises on 19/02/16.
 */
public class MergeSortInfoCalculatorTest {


    @Test
    public void testInMemoryFit() throws Exception {
        MemoryManager memoryManager = mock(MemoryManager.class);
        when(memoryManager.availableMemory()).thenReturn(MemoryManager.MEGABYTE * 10);
        MergeSortInfoProvider calculator = new MergeSortInfoProvider(memoryManager);

        BigFile file = mock(BigFile.class);
        when(file.size()).thenReturn(MemoryManager.MEGABYTE);

        MergeSortInfo mergeSortInfo = calculator.buildMergeInfo(file, mock(BigFile.class));
        List<PassInfo> passInfos = mergeSortInfo.passes();

        Assert.assertEquals(1, mergeSortInfo.chunks());
        Assert.assertEquals(1, passInfos.size());
        Assert.assertEquals(1, passInfos.get(0).number());
        Assert.assertEquals(1, passInfos.get(0).numberOfBuckets());
    }

    @Test
    public void testBucketRoundUp() throws Exception {
        MemoryManager memoryManager = mock(MemoryManager.class);
        when(memoryManager.availableMemory()).thenReturn(MemoryManager.MEGABYTE);
        MergeSortInfoProvider calculator = new MergeSortInfoProvider(memoryManager);

        BigFile file = mock(BigFile.class);
        when(file.size()).thenReturn((long) (MemoryManager.MEGABYTE * 1.5));

        MergeSortInfo mergeSortInfo = calculator.buildMergeInfo(file, mock(BigFile.class));
        List<PassInfo> passInfos = mergeSortInfo.passes();

        Assert.assertEquals(2, mergeSortInfo.chunks());
        Assert.assertEquals(1, passInfos.size());
        Assert.assertEquals(1, passInfos.get(0).number());
        Assert.assertEquals(2, passInfos.get(0).numberOfBuckets());
    }

    @Test
    public void testOnePass() throws Exception {
        MemoryManager memoryManager = mock(MemoryManager.class);
        when(memoryManager.availableMemory()).thenReturn(MemoryManager.MEGABYTE);
        MergeSortInfoProvider calculator = new MergeSortInfoProvider(memoryManager);

        BigFile file = mock(BigFile.class);
        when(file.size()).thenReturn(MemoryManager.MEGABYTE * 10);

        MergeSortInfo mergeSortInfo = calculator.buildMergeInfo(file, mock(BigFile.class));
        List<PassInfo> passInfos = mergeSortInfo.passes();

        Assert.assertEquals(1, mergeSortInfo.maximumPasses());
        Assert.assertEquals(10, mergeSortInfo.chunks());
        Assert.assertEquals(1, passInfos.size());
        Assert.assertEquals(1, passInfos.get(0).number());
        Assert.assertEquals(10, passInfos.get(0).numberOfBuckets());
    }

    @Test
    public void testKPass() throws Exception {

        MemoryManager memoryManager = mock(MemoryManager.class);
        when(memoryManager.availableMemory()).thenReturn(MemoryManager.MEGABYTE * 1);

        IntStream.range(1, 2).forEach(k -> {

            MergeSortInfoProvider calculator = new MergeSortInfoProvider(memoryManager);

            BigFile file = mock(BigFile.class);
            when(file.size()).thenReturn(MemoryManager.MEGABYTE * 500 * k);

            MergeSortInfo mergeSortInfo = calculator.buildMergeInfo(file, mock(BigFile.class));
            List<PassInfo> passInfos = mergeSortInfo.passes();

            Assert.assertEquals(2, mergeSortInfo.maximumPasses());
            Assert.assertEquals(500, mergeSortInfo.chunks());
            Assert.assertEquals(2 * k, passInfos.size());
            Assert.assertEquals(k, passInfos.get(0).number());
        });


    }


}
