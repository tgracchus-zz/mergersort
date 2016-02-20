package org.java.lang;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ulises on 20/02/16.
 */
public class QuickSort implements SortAlg<Lines, Lines> {
    @Override
    public Lines apply(Lines lines) {
        List<TString> tStrings = lines.getLines();
        quicksort(tStrings, 0, tStrings.size() - 1);
        return lines;
    }


    private void quicksort(List<TString> tStrings, int l, int h) {

        if (tStrings.size() < 0) {
            return;
        }

        Deque<StackElement<TString>> stack = new ArrayDeque<>(l - h + 1);

        stack.push(new StackElement(tStrings, l));
        stack.push(new StackElement(tStrings, h));

        // Keep popping from stack while is not empty
        while (!stack.isEmpty()) {
            // Pop h and l
            StackElement hs = stack.pop();
            h = hs.index;

            StackElement ls = stack.pop();
            l = ls.index;

            // Set pivot element at its correct position in sorted array
            int p = partition(tStrings, ls.index, hs.index);

            // If there are elements on left side of pivot, then push left
            // side to stack
            if (p - 1 > l) {
                stack.push(new StackElement(tStrings, l));
                stack.push(new StackElement(tStrings, p - 1));

            }

            // If there are elements on right side of pivot, then push right
            // side to stack
            if (p + 1 < h) {
                stack.push(new StackElement(tStrings, p + 1));
                stack.push(new StackElement(tStrings, h));

            }
        }
    }

    private static class StackElement<T> {

        private T element;
        private int index;

        private StackElement(List<T> elements, int index) {
            this.element = elements.get(index);
            this.index = index;
        }

        @Override
        public String toString() {
            return element.toString();
        }

    }

    private int randomPartition(List<TString> tStrings, int l, int h) {
        int i = ThreadLocalRandom.current().nextInt(l, h + 1);
        swap(tStrings, h, i);
        return partition(tStrings, l, h);
    }

    private int partition(List<TString> tStrings, int l, int h) {
        TString x = tStrings.get(h);
        int i = l - 1;

        for (int j = l; j <= h - 1; j++) {
            TString jj = tStrings.get(j);
            if (jj.compareTo(x) < 0) {
                i++;
                swap(tStrings, i, j);
            }
        }
        swap(tStrings, i + 1, h);
        return (i + 1);
    }

    private void swap(List<TString> tStrings, int x, int y) {
        Collections.swap(tStrings, x, y);
    }
}
