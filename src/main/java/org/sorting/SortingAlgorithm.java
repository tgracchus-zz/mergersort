package org.sorting;

import org.externalsorting.mergesort.TextChunk;

/**
 * Created by ulises on 17/02/16.
 * Represents a in-memory sorting algorithm
 */
public interface SortingAlgorithm {

    TextChunk sort(TextChunk textChunk);
}
