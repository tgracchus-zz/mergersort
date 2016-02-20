package org.java;

import org.java.lang.Lines;
import org.java.lang.LinesMergeSort;
import org.java.lang.TString;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by ulises.olivenza on 18/02/16.
 */
public class MergeSortTest {
    private LinesMergeSort mergeSort;

    private List<TString> lines = Arrays.asList(
            new TString("dichroous counterobligation metaplastic inexpectedly Janus supersedeas osculiferous initial relativistic intraplant Hallstatt thoracograph unsaddling reef trimetrogon marigram\n".toCharArray()),
            new TString("Gi oxberry hud postique auriscope oothecal hygric statorhab pterosaurian unrelinquishing pithecometric androkinin unornamented barlafummil preinvestigate bibliopegy\n".toCharArray()),
            new TString("nonextracted monosyllable\n".toCharArray()),
            new TString("\n".toCharArray()),
            new TString("1\n".toCharArray()));

    private List<TString> expectedResult = Arrays.asList(
            lines.get(3),
            lines.get(4),
            lines.get(0),
            lines.get(1),
            lines.get(2));

    @Before
    public void setUp() {
        mergeSort = new LinesMergeSort();
    }

    @Test
    public void testMergerSort() throws Exception {
        Lines sortedLines = mergeSort.apply(new Lines(lines));
        assertEquals(expectedResult, sortedLines);
    }
}
