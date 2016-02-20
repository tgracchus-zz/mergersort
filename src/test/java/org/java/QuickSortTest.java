package org.java;

import org.java.lang.Lines;
import org.java.lang.QuickSort;
import org.java.lang.TString;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by ulises.olivenza on 18/02/16.
 */
public class QuickSortTest {
    private QuickSort mergeSort;

    private List<TString> lines = new ArrayList<>();


    private List<TString> expectedResult = new ArrayList();

    @Before
    public void setUp() {
        mergeSort = new QuickSort();
        lines.add(new TString("dichroous counterobligation metaplastic inexpectedly Janus supersedeas osculiferous initial relativistic intraplant Hallstatt thoracograph unsaddling reef trimetrogon marigram\n".toCharArray()));
        lines.add(new TString("Gi oxberry hud postique auriscope oothecal hygric statorhab pterosaurian unrelinquishing pithecometric androkinin unornamented barlafummil preinvestigate bibliopegy\n".toCharArray()));
        lines.add(new TString("nonextracted monosyllable\n".toCharArray()));
        lines.add(new TString("\n".toCharArray()));
        lines.add(new TString("1\n".toCharArray()));

        expectedResult.add(lines.get(3));
        expectedResult.add(lines.get(4));
        expectedResult.add(lines.get(0));
        expectedResult.add(lines.get(1));
        expectedResult.add(lines.get(2));


    }

    @Test
    public void testMergerSort() throws Exception {
        Lines sortedLines = mergeSort.apply(new Lines(lines));
        assertEquals(expectedResult, sortedLines.getLines());
    }
}
