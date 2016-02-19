package org.java.externalsort;

import org.java.nio.TFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ulises on 17/02/16.
 * External Sorting algorithm
 * O(n log n)
 * K pass merge sort.
 */
public class KPassMergeSorter implements BigFileSorter {

    private final static Logger log = LoggerFactory.getLogger(TFileReader.class);

    private final PassesCalculator passesCalculator;


    public KPassMergeSorter(PassesCalculator passesCalculator) {
        this.passesCalculator = passesCalculator;
    }

    public KPassMergeSorter() {
        this.passesCalculator = new PassesCalculator();
    }


    @Override
    public BigFile sort(BigFile bigTextFile) throws FileNotFoundException {

        Passes passes = passesCalculator.calculatePasses(bigTextFile);
/*

        //Chunk and sort the chunks
        List<Chunk> textChunks = chunks(bigTextFile).map(chunk -> {
            try {
                return chunk.sort(new TFile(""));
            } catch (IOException e) {
                return null;
            }
        }).collect(Collectors.toList());

        //Merge the chunks in k passes
        for (int i = 0; i < passes; i++) {
            textChunks = classifyAndReduce(textChunks, i, passes);
        }

        return merge(textChunks);*/
        return null;

    }


    private List<Chunk> classifyAndReduce(List<Chunk> textChunksList, int pass, int maximumPasses) {

        //Classify chunks by groupNumber and reduce
        return textChunksList.stream()
                .collect(Collectors.groupingByConcurrent(Chunk::chunkgroup))
                .values().stream()
                .map(textChunks ->
                        reduce(textChunks, pass, maximumPasses)
                ).collect(Collectors.toList());
    }

    private Stream<Chunk> chunks(BigFile bigTextFile) {


        return Stream.of(new Chunk(1, bigTextFile.path()));

    }


    public Chunk reduce(List<Chunk> chunks, int pass, int maximumPasses) {
        return null;
    }

    public BigFile merge(List<Chunk> chunks) {
        return null;
    }

}

