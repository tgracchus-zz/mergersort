import org.test.lang.TextString;
import org.junit.Before;
import org.junit.Test;
import org.test.sort.MergeSort;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by ulises.olivenza on 18/02/16.
 */
public class MergeSortTest {
    private MergeSort mergeSort;

    private List<TextString> lines = Arrays.asList(
            new TextString("dichroous counterobligation metaplastic inexpectedly Janus supersedeas osculiferous initial relativistic intraplant Hallstatt thoracograph unsaddling reef trimetrogon marigram\n".toCharArray()),
            new TextString("Gi oxberry hud postique auriscope oothecal hygric statorhab pterosaurian unrelinquishing pithecometric androkinin unornamented barlafummil preinvestigate bibliopegy\n".toCharArray()),
            new TextString("nonextracted monosyllable\n".toCharArray()),
            new TextString("\n".toCharArray()),
            new TextString("1\n".toCharArray()));

    private List<TextString> expectedResult = Arrays.asList(
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
