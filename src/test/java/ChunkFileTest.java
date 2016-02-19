import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.test.externalsort.imp.ChunkFile;
import org.junit.Before;
import org.junit.Test;
import org.test.sort.MergeSort;

/**
 * Created by ulises on 18/02/16.
 */
public class ChunkFileTest {

    private Path outFile;
    private Path inFile;
    private Path checkFile;
    private ChunkFile chunkFile;

    @Before
    public void setUp() throws Exception {
        inFile = Paths.get("src/test/resources/lineReaderTest.txt");
        chunkFile = new ChunkFile(1, inFile, new MergeSort());
        checkFile = Paths.get("src/test/resources/testChunkSortedTest.txt");
        outFile = Paths.get("src/test/resources/tmpWriterTest.txt");

    }

    @Test
    public void testReadLine() throws Exception {
        ChunkFile sortedChunk = chunkFile.sort(outFile);
        assertEquals(chunkFile.chunkgroup(), sortedChunk.chunkgroup());

        byte[] in = Files.readAllBytes(outFile);
        byte[] out = Files.readAllBytes(checkFile);

        sortedChunk.delete();

        assertTrue(Arrays.equals(in, out));
    }
}
