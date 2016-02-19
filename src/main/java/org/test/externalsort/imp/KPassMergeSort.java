package org.test.externalsort.imp;

import java.io.FileNotFoundException;

import org.test.externalsort.BigTextFile;
import org.test.externalsort.ExSortingAlgorithm;

/**
 * Created by ulises on 17/02/16.
 * External Sorting algorithm
 * O(n log n)
 * K pass merge sort.
 */
public class KPassMergeSort implements ExSortingAlgorithm {

    @Override public BigTextFile sort(BigTextFile bigTextFile) throws FileNotFoundException {
        return null;
    }

    /*
    private final Merger merger;

    public KPassMergeSort(Merger merger) {
        this.merger = merger;
    }

    @Override
    public BigTextFile sort(BigTextFile bigTextFile) throws FileNotFoundException {
        //Chunk and sort the chunks
        List<TextChunk> textChunks = bigTextFile.chunks().map(textChunk -> textChunk.sort(new File(""))).collect(Collectors.toList());

        //Merge the chunks in k passe
        for (int i = 0; i < bigTextFile.passes(); i++) {
            textChunks = classifyAndReduce(textChunks, i, bigTextFile.passes());
        }

        return merger.merge(textChunks);
    }

    private List<TextChunk> classifyAndReduce(List<TextChunk> textChunksList, int pass, int maximumPasses) {

        //Classify chunks by groupNumber and reduce
        return textChunksList.stream()
                .collect(Collectors.groupingByConcurrent(TextChunk::chunkgroup))
                .values().stream()
                .map(textChunks ->
                        merger.reduce(textChunks, pass, maximumPasses)
                ).collect(Collectors.toList());
    }
*/

}

