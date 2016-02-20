package org.java.externalsort;

import org.java.nio.TFileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ulises on 17/02/16.
 * External Sorting algorithm
 * O(n log n)
 * K pass merge sort.
 */
public class KPassMergeSorter implements BigFileSorter {

    private final static Logger log = LoggerFactory.getLogger(TFileReader.class);

    private final PassesCalculator passesCalculator;
    private final Chunkenizer chunkenizer;


    public KPassMergeSorter(PassesCalculator passesCalculator, Chunkenizer chunkenizer) throws IOException {
        this.passesCalculator = passesCalculator;
        this.chunkenizer = chunkenizer;
    }

    public KPassMergeSorter() throws IOException {
        this(new PassesCalculator(), new Chunkenizer());
    }


    @Override
    public BigFile sort(BigFile bigTextFile) throws IOException {

        ChunksInfo chunksInfo = passesCalculator.calculatePasses(bigTextFile);
        TFileReader tFileReader = new TFileReader(bigTextFile);

        try {
            chunkenizer.chunks(tFileReader, chunksInfo);
        } finally {
            tFileReader.close();
        }
/*

        //Chunk and sort the chunks
        List<Chunk> textChunks = chunks(bigTextFile).map(chunk -> {
            try {
                return chunk.sort(new TFile(""));
            } catch (IOException e) {
                return null;
            }
        }).collect(Collectors.toList());

        //Merge the chunks in k chunksInfo
        for (int i = 0; i < chunksInfo; i++) {
            textChunks = classifyAndReduce(textChunks, i, chunksInfo);
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


    public Chunk reduce(List<Chunk> chunks, int pass, int maximumPasses) {
        return null;
    }

    public BigFile merge(List<Chunk> chunks) {
        return null;
    }

}

