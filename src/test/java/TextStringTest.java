import static org.junit.Assert.assertEquals;

import org.test.lang.TextString;
import org.junit.Test;

/**
 * Created by ulises.olivenza on 18/02/16.
 */
public class TextStringTest {

    @Test
    public void testEquals() throws Exception {

        TextString textString = new TextString("testValue".toCharArray());
        TextString secondTextString = new TextString("testValue".toCharArray());

        assertEquals(textString, secondTextString);

    }

    @Test
    public void testSameHashCode() throws Exception {

        TextString textString = new TextString("testValue".toCharArray());
        TextString secondTextString = new TextString("testValue".toCharArray());

        assertEquals(textString.hashCode(), secondTextString.hashCode());

    }

    @Test
    public void testSameHashCodeThanString() throws Exception {

        String string = "testValue";
        TextString textString = new TextString("testValue".toCharArray());

        assertEquals(string.hashCode(), textString.hashCode());

    }

    @Test
    public void testCompareToEquals() throws Exception {

        TextString textString = new TextString("testValue".toCharArray());
        TextString secondTextString = new TextString("testValue".toCharArray());

        assertEquals(0, textString.compareTo(secondTextString));
        assertEquals(0, secondTextString.compareTo(textString));
    }

    @Test
    public void testCompareGreaterThan() throws Exception {

        TextString textString = new TextString("abc".toCharArray());
        TextString secondTextString = new TextString("ba".toCharArray());

        assertEquals(-1, textString.compareTo(secondTextString));
    }

    @Test
    public void testCompareLessThan() throws Exception {

        TextString textString = new TextString("ba".toCharArray());
        TextString secondTextString = new TextString("abc".toCharArray());

        assertEquals(1, textString.compareTo(secondTextString));
    }
}
