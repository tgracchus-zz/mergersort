package org.java;

import static org.junit.Assert.assertEquals;

import org.java.lang.TString;
import org.junit.Test;

/**
 * Created by ulises.olivenza on 18/02/16.
 */
public class TStringTest {

    @Test
    public void testEquals() throws Exception {

        TString tString = new TString("testValue".toCharArray());
        TString secondTString = new TString("testValue".toCharArray());

        assertEquals(tString, secondTString);

    }

    @Test
    public void testSameHashCode() throws Exception {

        TString tString = new TString("testValue".toCharArray());
        TString secondTString = new TString("testValue".toCharArray());

        assertEquals(tString.hashCode(), secondTString.hashCode());

    }

    @Test
    public void testSameHashCodeThanString() throws Exception {

        String string = "testValue";
        TString tString = new TString("testValue".toCharArray());

        assertEquals(string.hashCode(), tString.hashCode());

    }

    @Test
    public void testCompareToEquals() throws Exception {

        TString tString = new TString("testValue".toCharArray());
        TString secondTString = new TString("testValue".toCharArray());

        assertEquals(0, tString.compareTo(secondTString));
        assertEquals(0, secondTString.compareTo(tString));
    }

    @Test
    public void testCompareGreaterThan() throws Exception {

        TString tString = new TString("abc".toCharArray());
        TString secondTString = new TString("ba".toCharArray());

        assertEquals(-1, tString.compareTo(secondTString));
    }

    @Test
    public void testCompareLessThan() throws Exception {

        TString tString = new TString("ba".toCharArray());
        TString secondTString = new TString("abc".toCharArray());

        assertEquals(1, tString.compareTo(secondTString));
    }
}
