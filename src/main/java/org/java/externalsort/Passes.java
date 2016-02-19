package org.java.externalsort;

import java.util.List;

/**
 * Created by ulises on 19/02/16.
 */
public class Passes {

    private final int chunks;
    private final List<PassInfo> passInfos;

    public Passes(int chunks, List<PassInfo> passInfos) {
        this.chunks = chunks;
        this.passInfos = passInfos;
    }


    public List<PassInfo> getPassInfos() {
        return passInfos;
    }

    public int chunks() {
        return chunks;
    }
}
