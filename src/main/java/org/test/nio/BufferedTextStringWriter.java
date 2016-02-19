package org.test.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.List;

import org.test.lang.TextString;

/**
 * Not Thread Safe Class !!!
 * Created by ulises on 18/02/16.
 */
public class BufferedTextStringWriter {

    private final static int DEFAULT_BUFFER_SIZE = 1024 * 2;

    private final FileChannel fc;
    private final Charset charset;
    private final int bufferSize;
    private final CharsetEncoder charsetEncoder;

    public BufferedTextStringWriter(FileChannel fc, Charset charset, int bufferSize) {
        this.fc = fc;
        this.charset = charset;
        this.bufferSize = bufferSize;
        this.charsetEncoder = charset.newEncoder();
    }

    public BufferedTextStringWriter(FileChannel fc, Charset charset) {
        this(fc, charset, DEFAULT_BUFFER_SIZE);
    }

    public BufferedTextStringWriter(FileChannel fc) {
        this(fc, Charset.defaultCharset(), DEFAULT_BUFFER_SIZE);
    }

    public void writeLines(List<TextString> lines) throws IOException {
        ByteBuffer line;

        for (TextString textString : lines) {
            line = charsetEncoder.encode(textString.toCharBuffer());
            fc.write(line);

        }

    }
}
