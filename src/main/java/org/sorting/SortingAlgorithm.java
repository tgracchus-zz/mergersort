package org.sorting;

import org.textstring.TextString;

import java.util.List;

/**
 * Created by ulises on 17/02/16.
 * Represents a in-memory sorting algorithm
 */
public interface SortingAlgorithm {

    void sort(List<TextString> textChunk);
}
