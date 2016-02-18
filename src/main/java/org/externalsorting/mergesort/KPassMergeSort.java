package org.externalsorting.mergesort;

import org.externalsorting.BigTextFile;
import org.externalsorting.ExSortingAlgorithm;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ulises on 17/02/16.
 * External Sorting algorithm
 * O(n log n)
 * K pass merge sort.
 */
public class KPassMergeSort implements ExSortingAlgorithm {


    private final Merger merger;

    public KPassMergeSort(Merger merger) {
        this.merger = merger;
    }

    @Override
    public BigTextFile sort(BigTextFile bigTextFile) throws FileNotFoundException {
        //Chunk and sort the chunks
        List<TextChunk> textChunks = bigTextFile.chunks().map(TextChunk::sort).collect(Collectors.toList());

        //Merge the chunks in k passe
        for (int i = 0; i < bigTextFile.passes(); i++) {
            textChunks = classifyAndReduce(textChunks, i, bigTextFile.passes());
        }

        return merger.merge(textChunks);
    }

    private List<TextChunk> classifyAndReduce(List<TextChunk> textChunksList, int pass, int maximumPasses) {

        //Classify chunks by groupNumber and reduce
        return textChunksList.stream()
                .collect(Collectors.groupingByConcurrent(TextChunk::gropNumber))
                .values().stream()
                .map(textChunks ->
                        merger.reduce(textChunks, pass, maximumPasses)
                ).collect(Collectors.toList());
    }


}

