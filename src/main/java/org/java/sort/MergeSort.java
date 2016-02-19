package org.java.sort;

import org.java.lang.TString;

import java.util.Collections;
import java.util.List;

/**
 * Created by ulises on 17/02/16.
 */
public class MergeSort implements SortingAlgorithm {

    @Override
    public void sort(List<TString> lines) {
        Collections.sort(lines);
    }
}
