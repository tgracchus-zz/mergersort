import org.test.lang.TextString;
import org.test.nio.BufferedTextStringReader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * Created by ulises.olivenza on 18/02/16.
 */
public class BufferedTextStringReaderTest {

    private BufferedTextStringReader bufferedTextStringReader;
    private FileChannel fc;

    private String[] lines = {
            "dichroous counterobligation metaplastic inexpectedly Janus supersedeas osculiferous initial relativistic intraplant Hallstatt thoracograph unsaddling reef trimetrogon marigram\n",
            "Gi oxberry hud postique auriscope oothecal hygric statorhab pterosaurian unrelinquishing pithecometric androkinin unornamented barlafummil preinvestigate bibliopegy\n",
            "nonextracted monosyllable\n",
            "\n",
            "1\n",
            null};

    @Before
    public void setUp() throws Exception {
        File file = new File("src/test/resources/lineReaderTest.txt");
        fc = new RandomAccessFile(file,"r").getChannel();
        bufferedTextStringReader = new BufferedTextStringReader(fc, StandardCharsets.UTF_8,60);

    }

    @After
    public void tearDown() throws Exception {
        fc.close();
    }

    @Test
    public void testReadLine() throws Exception {

        for (int i = 0; i < 5; i++) {
            TextString textString = bufferedTextStringReader.readLine();
            Assert.assertEquals(new TextString(lines[i].toCharArray()), textString);
        }

        Assert.assertNull(bufferedTextStringReader.readLine());

    }

}
