import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

import org.externalsorting.imp.BString;
import org.externalsorting.imp.BStringReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by ulises.olivenza on 18/02/16.
 */
public class BStreingReaderTest {

    private BStringReader bStringReader;

    private String[] lines = {
            "dichroous counterobligation metaplastic inexpectedly Janus supersedeas osculiferous initial relativistic intraplant Hallstatt thoracograph unsaddling reef trimetrogon marigram",
            "Gi oxberry hud postique auriscope oothecal hygric statorhab pterosaurian unrelinquishing pithecometric androkinin unornamented barlafummil preinvestigate bibliopegy",
            "nonextracted monosyllable enchantingly schoolmaster laxative pfui zonulet Oscinis unmoor ziganka grossify parget Padus ecumenical decaesarize imperspicuity",
            null};

    @Before
    public void setUp() throws Exception {
        File file = new File("src/test/resources/lineReaderTest.txt");
        FileChannel fc = new FileInputStream(file).getChannel();
        bStringReader = new BStringReader(fc, StandardCharsets.UTF_8);

    }

    @Test
    public void readLine() throws Exception {

        for (int i = 0; i < 4; i++) {
            BString bString = bStringReader.readLine();
            Assert.assertEquals(new BString(lines[i].toCharArray()), bString);
        }

    }
}
