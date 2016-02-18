package org.externalsorting.mergesort;

/**
 * Created by ulises on 17/02/16.
 */
public class TextChunk {

    private final int chunkNumber;

    public TextChunk(int chunkNumber) {
        this.chunkNumber = chunkNumber;
    }

    public TextChunk sort() {
        return this;
    }

    int gropNumber() {
        return chunkNumber;
    }


}
