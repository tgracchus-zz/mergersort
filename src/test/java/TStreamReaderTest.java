import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.test.externalsort.imp.ChunkFile;
import org.test.lang.TString;
import org.test.nio.TFile;
import org.test.nio.TStreamReader;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * Created by ulises.olivenza on 18/02/16.
 */
public class TStreamReaderTest {

    private TStreamReader TStreamReader;

    private String[] lines = {
            "dichroous counterobligation metaplastic inexpectedly Janus supersedeas osculiferous initial relativistic intraplant Hallstatt thoracograph unsaddling reef trimetrogon marigram\n",
            "Gi oxberry hud postique auriscope oothecal hygric statorhab pterosaurian unrelinquishing pithecometric androkinin unornamented barlafummil preinvestigate bibliopegy\n",
            "nonextracted monosyllable\n",
            "\n",
            "1\n",
            null};

    @Before
    public void setUp() throws Exception {
        TFile tFile = new TFile(Paths.get("src/test/resources/lineReaderTest.txt"));
        TStreamReader = new TStreamReader(tFile, StandardCharsets.UTF_8, 60);

    }

    @After
    public void tearDown() throws Exception {
        TStreamReader.close();
    }

    @Test
    public void testReadLine() throws Exception {

        for (int i = 0; i < 5; i++) {
            TString tString = TStreamReader.readLine();
            Assert.assertEquals(new TString(lines[i].toCharArray()), tString);
        }

        Assert.assertNull(TStreamReader.readLine());

    }

}
