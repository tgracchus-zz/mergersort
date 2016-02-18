import static org.junit.Assert.assertEquals;

import org.externalsorting.imp.BString;
import org.junit.Test;

/**
 * Created by ulises.olivenza on 18/02/16.
 */
public class BStringTest {

    @Test
    public void testEquals() throws Exception {

        BString bString = new BString("testValue".toCharArray());
        BString secondBString = new BString("testValue".toCharArray());

        assertEquals(bString, secondBString);

    }

    @Test
    public void testSameHashCode() throws Exception {

        BString bString = new BString("testValue".toCharArray());
        BString secondBString = new BString("testValue".toCharArray());

        assertEquals(bString.hashCode(), secondBString.hashCode());

    }

    @Test
    public void testSameHashCodeThanString() throws Exception {

        String string = "testValue";
        BString bString = new BString("testValue".toCharArray());

        assertEquals(string.hashCode(), bString.hashCode());

    }

    @Test
    public void testCompareToEquals() throws Exception {

        BString bString = new BString("testValue".toCharArray());
        BString secondBString = new BString("testValue".toCharArray());

        assertEquals(0, bString.compareTo(secondBString));
        assertEquals(0, secondBString.compareTo(bString));
    }

    @Test
    public void testCompareGreaterThan() throws Exception {

        BString bString = new BString("abc".toCharArray());
        BString secondBString = new BString("ba".toCharArray());

        assertEquals(1, bString.compareTo(secondBString));
    }

    @Test
    public void testCompareLessThan() throws Exception {

        BString bString = new BString("ba".toCharArray());
        BString secondBString = new BString("abc".toCharArray());

        assertEquals(-1, bString.compareTo(secondBString));
    }
}
