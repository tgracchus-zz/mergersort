package org.java.esort.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Created by ulises on 20/02/16.
 */
public class Lines {

    private final List<TString> sortedList;


    public Lines(List<TString> sortedList) {
        //Mutable implementation!!! No copy for performance reasons
        this.sortedList = sortedList;
    }


    public boolean isEmpty() {
        return sortedList.isEmpty();
    }

    public Lines map(Function<Lines, Lines> mapper) {
        Objects.requireNonNull(mapper);
        return mapper.apply(this);
    }


    /**
     * Not Safe Copy!! for performance reason
     *
     * @return
     */
    public List<TString> getLines() {
        return sortedList;
    }


}
