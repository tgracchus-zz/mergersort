import java.io.File;
import java.util.List;

import org.externalsorting.mergesort.Chunkenizer;
import org.externalsorting.mergesort.TextChunk;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by ulises on 18/02/16.
 */
public class ChunkenizerTest {

    private File bigFile;
    private Chunkenizer chunkenizer;

    @Before
    public void setUp() throws Exception {
        bigFile = new File("src/test/resources/bigFileTest.txt");
        chunkenizer = new Chunkenizer();

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testReadLine() throws Exception {
        List<TextChunk> textChunks = chunkenizer.chunk(bigFile);


        textChunks.stream().forEach(textChunk -> textChunk.delete());
    }
}
