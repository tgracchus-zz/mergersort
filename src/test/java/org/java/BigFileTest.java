package org.java;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.java.externalsort.BigFile;

/**
 * Created by ulises on 19/02/16.
 */
public class BigFileTest {

    private BigFile bigFile;


    @Before
    public void setUp() throws Exception {
        bigFile = new BigFile("src/test/resources/bigFileTest.txt");

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testReadLine() throws Exception {
        bigFile.sort();

    }
}
