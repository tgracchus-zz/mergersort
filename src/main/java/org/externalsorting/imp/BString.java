package org.externalsorting.imp;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by ulises.olivenza on 18/02/16.
 */
public class BString implements Comparable<BString> {

    private final char value[];
    private int hash; // Default to 0

    public BString(char[] value) {
        this.value = value;
    }

    /**
     * Case insensitive comparator, copy from String
     * @param anotherString
     * @return
     */
    @Override
    public int compareTo(BString anotherString) {
        int n1 = this.value.length;
        int n2 = anotherString.value.length;
        int min = Math.min(n1, n2);
        for (int i = 0; i < min; i++) {
            char c1 = this.value[i];
            char c2 = anotherString.value[i];
            if (c1 != c2) {
                c1 = Character.toUpperCase(c1);
                c2 = Character.toUpperCase(c2);
                if (c1 != c2) {
                    c1 = Character.toLowerCase(c1);
                    c2 = Character.toLowerCase(c2);
                    if (c1 != c2) {
                        // No overflow because of numeric promotion
                        return c1 - c2;
                    }
                }
            }
        }
        return n1 - n2;
    }


    public int hashCode() {
        int h = hash;
        if (h == 0 && value.length > 0) {
            char val[] = value;

            for (int i = 0; i < value.length; i++) {
                h = 31 * h + val[i];
            }
            hash = h;
        }
        return h;
    }

    public boolean equals(Object anObject) {
        if (this == anObject) {
            return true;
        }
        if (anObject instanceof BString) {
            BString anotherString = (BString) anObject;
            int n = value.length;
            if (n == anotherString.value.length) {
                char v1[] = value;
                char v2[] = anotherString.value;
                int i = 0;
                while (n-- != 0) {
                    if (v1[i] != v2[i])
                        return false;
                    i++;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.copyValueOf(value);
    }
}
