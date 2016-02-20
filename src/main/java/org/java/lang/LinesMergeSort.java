package org.java.lang;

import java.util.Collections;
import java.util.List;

/**
 * Mutable implementation !!!
 * Created by ulises on 17/02/16.
 */
public class LinesMergeSort implements LinesSorter {

    @Override
    public Lines apply(Lines lines) {
        //No defensive copy for performance reasons
        List<TString> sortedList = lines.getLines();
        Collections.sort(sortedList);
        return lines;
    }
}
