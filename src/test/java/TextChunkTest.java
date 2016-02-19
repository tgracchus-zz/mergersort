import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.test.externalsort.imp.TextChunk;
import org.junit.Before;
import org.junit.Test;
import org.test.sort.MergeSort;

/**
 * Created by ulises on 18/02/16.
 */
public class TextChunkTest {

    private Path outFile;
    private Path inFile;
    private Path checkFile;
    private TextChunk textChunk;

    @Before
    public void setUp() throws Exception {
        inFile = Paths.get("src/test/resources/lineReaderTest.txt");
        textChunk = new TextChunk(1, inFile, new MergeSort());
        checkFile = Paths.get("src/test/resources/testChunkSortedTest.txt");
        outFile = Paths.get("src/test/resources/tmpWriterTest.txt");

    }

    @Test
    public void testReadLine() throws Exception {
        TextChunk sortedChunk = textChunk.sort(outFile);
        assertEquals(textChunk.chunkgroup(), sortedChunk.chunkgroup());

        byte[] in = Files.readAllBytes(outFile);
        byte[] out = Files.readAllBytes(checkFile);

        sortedChunk.delete();

        assertTrue(Arrays.equals(in, out));
    }
}
