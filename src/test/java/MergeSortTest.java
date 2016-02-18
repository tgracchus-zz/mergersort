import org.externalsorting.imp.BString;
import org.junit.Before;
import org.junit.Test;
import org.sorting.MergeSort;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by ulises.olivenza on 18/02/16.
 */
public class MergeSortTest {
    private MergeSort mergeSort;

    private List<BString> lines = Arrays.asList(
            new BString("dichroous counterobligation metaplastic inexpectedly Janus supersedeas osculiferous initial relativistic intraplant Hallstatt thoracograph unsaddling reef trimetrogon marigram".toCharArray()),
            new BString("Gi oxberry hud postique auriscope oothecal hygric statorhab pterosaurian unrelinquishing pithecometric androkinin unornamented barlafummil preinvestigate bibliopegy".toCharArray()),
            new BString("nonextracted monosyllable".toCharArray()),
            new BString("".toCharArray()),
            new BString("1".toCharArray()));

    private List<BString> expectedResult = Arrays.asList(
            lines.get(3),
            lines.get(4),
            lines.get(0),
            lines.get(1),
            lines.get(2));

    @Before
    public void setUp() {
        mergeSort = new MergeSort();
    }

    @Test
    public void testMergerSort() throws Exception {
        mergeSort.sort(lines);
        assertEquals(expectedResult, lines);
    }
}
