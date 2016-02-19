package org.textstring;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Not Thread Safe Class !!!
 * Created by ulises.olivenza on 18/02/16.
 */
public class TextStringReader {

    private final static int DEFAULT_BUFFER_SIZE = 120;

    private final FileChannel fc;
    private final int bufferSize;
    private long position = 0;
    private long size;

    private final CharsetDecoder charsetDecoder;
    private MappedByteBuffer file;
    private int bufferPosition=0;
    private char[] buffer;

    public TextStringReader(FileChannel fc, Charset charset, int bufferSize) {
        this.fc = fc;
        this.charsetDecoder = charset.newDecoder();
        this.bufferSize = bufferSize;
        this.buffer = CharBuffer.allocate(1).array();
    }

    public TextStringReader(FileChannel fc, Charset charset) {
        this(fc, charset, DEFAULT_BUFFER_SIZE);
    }

    public TextStringReader(FileChannel fc) {
        this(fc, Charset.defaultCharset(), DEFAULT_BUFFER_SIZE);
    }


    public TextString readLine() throws IOException {
        CharBuffer line = CharBuffer.allocate(0);
        calculateSize();
        char c;

        while (size > 0) {

            if (bufferPosition<buffer.length) {
                fillBuffer();
            }

            if (line.remaining() == 0) {
                line = expandLine(line);
            }

            c = buffer[bufferPosition];
            bufferPosition++;
            line.put(c);
            position++;

            // Check for eol
            if ((c == '\n') || (c == '\r')) {
                return new TextString(trimLine(line));
            }

        }

        return null;
    }

    private void fillBuffer() throws IOException {
        calculateSize();
        file = fc.map(FileChannel.MapMode.READ_ONLY, position, size);
        buffer = charsetDecoder.decode(file).array();
        bufferPosition=0;
    }

    private void calculateSize() throws IOException {
        long edgeSize = fc.size() - position;
        size = (edgeSize < bufferSize) ? edgeSize : bufferSize;
    }


    private CharBuffer expandLine(CharBuffer line) {
        CharBuffer newCharBuffer = CharBuffer.allocate(line.limit() + bufferSize);
        return newCharBuffer.put(line.array(), 0, line.limit());
    }

    private char[] trimLine(CharBuffer line) {
        CharBuffer newCharBuffer = CharBuffer.allocate(line.position() - 1);
        return newCharBuffer.put(line.array(), 0, line.position() - 1).array();
    }


}
