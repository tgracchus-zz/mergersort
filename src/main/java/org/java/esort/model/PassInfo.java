package org.java.esort.model;

/**
 * Created by ulises on 19/02/16.
 */
public class PassInfo {

    private final int number;
    private final int numberOfBuckets;

    public PassInfo(int number, int numberOfBuckets) {
        this.number = number;
        this.numberOfBuckets = numberOfBuckets;
    }

    public int number() {
        return number;
    }

    public int numberOfBuckets() {
        return numberOfBuckets;
    }

    @Override
    public String toString() {
        return "PassInfo{" +
                "number=" + number +
                ", numberOfBuckets=" + numberOfBuckets +
                '}';
    }
}
