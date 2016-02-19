import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.test.lang.TextString;
import org.test.nio.BufferedTextStringWriter;

/**
 * Created by ulises.olivenza on 18/02/16.
 */
public class BufferedTextStringWriterTest {

    private BufferedTextStringWriter bufferedTextStringWriter;
    private FileChannel fc;
    private File outFile;
    private File inFile;

    private List<TextString> lines = Arrays.asList(new TextString(
            "dichroous counterobligation metaplastic inexpectedly Janus supersedeas osculiferous initial relativistic intraplant Hallstatt thoracograph unsaddling reef trimetrogon marigram\n"
                    .toCharArray()), new TextString(
                            "Gi oxberry hud postique auriscope oothecal hygric statorhab pterosaurian unrelinquishing pithecometric androkinin unornamented barlafummil preinvestigate bibliopegy\n"
                                    .toCharArray()), new TextString("nonextracted monosyllable\n".toCharArray()),
            new TextString("\n".toCharArray()), new TextString("1\n".toCharArray()));

    @Before
    public void setUp() throws Exception {
        inFile = new File("src/test/resources/lineReaderTest.txt");
        outFile = new File("src/test/resources/tmpWriterTest.txt");
        fc = new RandomAccessFile(outFile, "rw").getChannel();
        bufferedTextStringWriter = new BufferedTextStringWriter(fc, StandardCharsets.UTF_8, 60);

    }

    @After
    public void tearDown() throws Exception {
        fc.close();
        outFile.delete();
    }

    @Test
    public void testReadLine() throws Exception {
        bufferedTextStringWriter.writeLines(lines);
        assertTrue(outFile.exists());

        byte[] in = Files.readAllBytes(inFile.toPath());
        byte[] out = Files.readAllBytes(outFile.toPath());

        assertTrue(Arrays.equals(in,out));
    }

}
