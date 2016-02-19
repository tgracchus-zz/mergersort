package org.test.sort;

import org.test.lang.TString;

import java.util.List;

/**
 * Created by ulises on 17/02/16.
 * Represents a in-memory sorting algorithm
 */
public interface SortingAlgorithm {

    void sort(List<TString> textChunk);
}
