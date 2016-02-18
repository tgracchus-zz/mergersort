import org.externalsorting.imp.BString;
import org.externalsorting.imp.BStringReader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * Created by ulises.olivenza on 18/02/16.
 */
public class BStringReaderTest {

    private BStringReader bStringReader;
    private FileChannel fc;

    private String[] lines = {
            "dichroous counterobligation metaplastic inexpectedly Janus supersedeas osculiferous initial relativistic intraplant Hallstatt thoracograph unsaddling reef trimetrogon marigram",
            "Gi oxberry hud postique auriscope oothecal hygric statorhab pterosaurian unrelinquishing pithecometric androkinin unornamented barlafummil preinvestigate bibliopegy",
            "nonextracted monosyllable",
            "",
            "1",
            null};

    @Before
    public void setUp() throws Exception {
        File file = new File("src/test/resources/lineReaderTest.txt");
        fc = new FileInputStream(file).getChannel();
        bStringReader = new BStringReader(fc, StandardCharsets.UTF_8,60);

    }

    @After
    public void tearDown() throws Exception {
        fc.close();
    }

    @Test
    public void testReadLine() throws Exception {

        for (int i = 0; i < 5; i++) {
            BString bString = bStringReader.readLine();
            Assert.assertEquals(new BString(lines[i].toCharArray()), bString);
        }

        Assert.assertNull(bStringReader.readLine());

    }

}
