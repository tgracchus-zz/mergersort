package org.java;

import org.java.lang.TString;
import org.java.nio.TFile;
import org.java.nio.TFileReader;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by ulises.olivenza on 18/02/16.
 */
public class TFileReaderTest {

    private TFileReader TFileReader;

    private TString[] lines = {
            new TString("dichroous counterobligation metaplastic inexpectedly Janus supersedeas osculiferous initial relativistic intraplant Hallstatt thoracograph unsaddling reef trimetrogon marigram\n".toCharArray()),
            new TString("Gi oxberry hud postique auriscope oothecal hygric statorhab pterosaurian unrelinquishing pithecometric androkinin unornamented barlafummil preinvestigate bibliopegy\n".toCharArray()),
            new TString("nonextracted monosyllable\n".toCharArray()),
            new TString("\n".toCharArray()),
            new TString("1\n".toCharArray()),
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
            Assert.assertEquals(lines[i], tString);
        }

        Assert.assertNull(TFileReader.readLine());

    }

    @Test
    public void testReadLines() throws Exception {

        List<TString> firstRead = TFileReader.readLines(200);
        List<TString> secondRead = TFileReader.readLines(200);

        Assert.assertEquals(2, firstRead.size());
        Assert.assertEquals(3, secondRead.size());

        IntStream.range(0, 2).forEach(i -> Assert.assertEquals(firstRead.get(i), lines[i]));
        IntStream.range(0, 3).forEach(i -> Assert.assertEquals(secondRead.get(i), lines[i + 2]));

    }


}
