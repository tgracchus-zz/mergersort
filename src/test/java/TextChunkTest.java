import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.externalsorting.mergesort.TextChunk;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sorting.MergeSort;
import org.textstring.TextString;

/**
 * Created by ulises on 18/02/16.
 */
public class TextChunkTest {

    private File outFile;
    private File inFile;
    private File checkFile;
    private TextChunk textChunk;

    private List<TextString> lines = Arrays.asList(new TextString(
            "dichroous counterobligation metaplastic inexpectedly Janus supersedeas osculiferous initial relativistic intraplant Hallstatt thoracograph unsaddling reef trimetrogon marigram\n"
                    .toCharArray()), new TextString(
                            "Gi oxberry hud postique auriscope oothecal hygric statorhab pterosaurian unrelinquishing pithecometric androkinin unornamented barlafummil preinvestigate bibliopegy\n"
                                    .toCharArray()), new TextString("nonextracted monosyllable\n".toCharArray()),
            new TextString("\n".toCharArray()), new TextString("1\n".toCharArray()));

    @Before
    public void setUp() throws Exception {
        inFile = new File("src/test/resources/lineReaderTest.txt");
        textChunk = new TextChunk(1,inFile,new MergeSort());
        checkFile = new File("src/test/resources/testChunkSortedTest.txt");
        outFile = new File("src/test/resources/tmpWriterTest.txt");

    }

    @After
    public void tearDown() throws Exception {
        outFile.delete();
    }

    @Test
    public void testReadLine() throws Exception {
        TextChunk sortedChunk = textChunk.sort(outFile);
        assertEquals(textChunk.chunkgroup(),sortedChunk.chunkgroup());

        byte[] in = Files.readAllBytes(outFile.toPath());
        byte[] out = Files.readAllBytes(checkFile.toPath());

        assertTrue(Arrays.equals(in, out));
    }
}
