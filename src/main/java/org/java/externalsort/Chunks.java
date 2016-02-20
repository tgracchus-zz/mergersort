package org.java.externalsort;

import org.java.nio.BigFile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

/**
 * Created by ulises on 20/02/16.
 */
public class Chunks {

    private final List<Chunk> chunks;
    private final MergeSortInfo mergeSortInfo;

    public Chunks(List<Chunk> chunks, MergeSortInfo mergeSortInfo) {
        this.chunks = chunks;
        this.mergeSortInfo = mergeSortInfo;
    }

    public Chunks(MergeSortInfo mergeSortInfo) {
        this.chunks = new ArrayList<>();
        this.mergeSortInfo = mergeSortInfo;
    }


    public List<Chunk> get() {
        return chunks;
    }

    public MergeSortInfo chunksInfo() {
        return mergeSortInfo;
    }


    public Stream<Chunk> stream() {
        return chunks.stream();
    }

    public int size() {
        return chunks.size();
    }


    public Chunks reduce(UnaryOperator<Chunks> chunkBinaryOperator) {
        return chunkBinaryOperator.apply(this);
    }

    public BigFile reduce(Function<Chunks, BigFile> reducer) {
        return reducer.apply(this);
    }


    public Chunk get(int i) {
        return chunks.get(i);
    }
}
