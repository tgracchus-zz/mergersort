package org.externalsorting.mergesort;

import org.externalsorting.BigTextFile;
import org.externalsorting.ExSortingAlgorithm;

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
    public BigTextFile sort(BigTextFile bigTextFile) {
        //Chunk the file
        List<TextChunk> textChunks = bigTextFile.chunks();

        //Sort the chunks
        for (int i = 0; i < bigTextFile.passes(); i++) {
            textChunks = sort(textChunks, i, bigTextFile.passes());
        }

        return merger.merge(textChunks);
    }

    private List<TextChunk> sort(List<TextChunk> textChunksList, int pass, int maximumPasses) {

        //Sort the chunks, classify chunks by groupNumber and mergeReduce
        return textChunksList.stream()
                .map(TextChunk::sort)
                .collect(Collectors.groupingByConcurrent(TextChunk::gropNumber))
                .values().stream()
                .map(textChunks ->
                        merger.reduce(textChunks, pass, maximumPasses)
                ).collect(Collectors.toList());

    }


}

