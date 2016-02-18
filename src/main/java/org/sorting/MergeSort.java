package org.sorting;

import org.externalsorting.imp.BString;

import java.util.Collections;
import java.util.List;

/**
 * Created by ulises on 17/02/16.
 */
public class MergeSort implements SortingAlgorithm {

    @Override
    public void sort(List<BString> lines) {
        Collections.sort(lines);
    }
}
