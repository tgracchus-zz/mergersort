import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.textstring.TextString;
import org.textstring.TextStringWriter;

/**
 * Created by ulises.olivenza on 18/02/16.
 */
public class TextStringWriterTest {

    private TextStringWriter textStringWriter;
    private FileChannel fc;
    private File file;

    private List<TextString> lines = Arrays.asList(new TextString(
            "dichroous counterobligation metaplastic inexpectedly Janus supersedeas osculiferous initial relativistic intraplant Hallstatt thoracograph unsaddling reef trimetrogon marigram\n"
                    .toCharArray()), new TextString(
                            "Gi oxberry hud postique auriscope oothecal hygric statorhab pterosaurian unrelinquishing pithecometric androkinin unornamented barlafummil preinvestigate bibliopegy\n"
                                    .toCharArray()), new TextString("nonextracted monosyllable\n".toCharArray()),
            new TextString("\n".toCharArray()), new TextString("1\n".toCharArray()), new TextString(null));

    @Before
    public void setUp() throws Exception {
        file = new File("src/test/resources/tmpWriterTest.txt");
        fc = new RandomAccessFile(file, "w").getChannel();
        textStringWriter = new TextStringWriter(fc, StandardCharsets.UTF_8, 60);

    }

    @After
    public void tearDown() throws Exception {
        fc.close();
        file.delete();
    }

    @Test
    public void testReadLine() throws Exception {
        textStringWriter.writeLines(lines);
        assertTrue(file.exists());
    }

}
