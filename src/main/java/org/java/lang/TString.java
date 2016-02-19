package org.java.lang;

import java.nio.CharBuffer;

/**
 * Created by ulises.olivenza on 18/02/16.
 * 
 * Mutable, Not Thread Safe String!! to be used instead of String for performance reasons
 * 
 */
public class TString implements Comparable<TString> {

    private final char value[];
    private int hash; // Default to 0

    public TString(char[] value) {
        this.value = value;
    }

    /**
     * Case insensitive comparator, copy from String
     * 
     * @param anotherString
     * @return
     */
    @Override
    public int compareTo(TString anotherString) {
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
        if (anObject instanceof TString) {
            TString anotherString = (TString) anObject;
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

    public int size() {
        return value.length;
    }

    public CharBuffer toCharBuffer() {
        return CharBuffer.wrap(value);
    }

    @Override
    public String toString() {
        return String.copyValueOf(value);
    }
}
