package org.java;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.java.lang.TString;
import org.java.nio.TFile;
import org.java.nio.TFileReader;

import java.nio.charset.StandardCharsets;

/**
 * Created by ulises.olivenza on 18/02/16.
 */
public class TFileReaderTest {

    private TFileReader TFileReader;

    private String[] lines = {
            "dichroous counterobligation metaplastic inexpectedly Janus supersedeas osculiferous initial relativistic intraplant Hallstatt thoracograph unsaddling reef trimetrogon marigram\n",
            "Gi oxberry hud postique auriscope oothecal hygric statorhab pterosaurian unrelinquishing pithecometric androkinin unornamented barlafummil preinvestigate bibliopegy\n",
            "nonextracted monosyllable\n",
            "\n",
            "1\n",
            null};

    @Before
    public void setUp() throws Exception {
        TFile tFile = new TFile("src/test/resources/lineReaderTest.txt");
        TFileReader = new TFileReader(tFile, StandardCharsets.UTF_8, 60);

    }

    @After
    public void tearDown() throws Exception {
        TFileReader.close();
    }

    @Test
    public void testReadLine() throws Exception {

        for (int i = 0; i < 5; i++) {
            TString tString = TFileReader.readLine();
            Assert.assertEquals(new TString(lines[i].toCharArray()), tString);
        }

        Assert.assertNull(TFileReader.readLine());

    }

}
