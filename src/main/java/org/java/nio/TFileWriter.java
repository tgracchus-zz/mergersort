package org.java.nio;

import org.java.lang.Lines;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.java.lang.TString;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.List;

/**
 * Not Thread Safe Class !!!
 * Created by ulises on 18/02/16.
 */
public class TFileWriter {

    private static final Logger log = LoggerFactory.getLogger(TFileReader.class);

    private final FileChannel fc;
    private final CharsetEncoder charsetEncoder;

    public TFileWriter(TFile tFile, Charset charset) throws FileNotFoundException {
        this.fc = new RandomAccessFile(tFile.file(), "rw").getChannel();
        this.charsetEncoder = charset.newEncoder();
    }

    public TFileWriter(TFile tFile) throws FileNotFoundException {
        this(tFile, Charset.defaultCharset());
    }


    public void writeLines(Lines lines) throws IOException {
        for (TString tString : lines.getLines()) {
            writeLine(tString);
        }
    }


    public void writeLine(TString tString) throws IOException {
        ByteBuffer line = charsetEncoder.encode(tString.toCharBuffer());
        fc.write(line);
    }

    public void close() {
        closeFcSilently(fc);
    }

    private void closeFcSilently(FileChannel fc) {
        if (fc != null) {
            try {
                fc.close();
            } catch (IOException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
