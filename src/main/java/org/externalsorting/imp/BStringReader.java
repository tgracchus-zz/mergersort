package org.externalsorting.imp;

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
public class BStringReader {

    private final static int DEFAULT_BUFFER_SIZE = 120;

    private final FileChannel fc;
    private final int bufferSize;
    private long position = 0;
    private long size;

    private final CharsetDecoder charsetDecoder;
    private MappedByteBuffer file;
    private CharBuffer buffer;

    public BStringReader(FileChannel fc, Charset charset, int bufferSize) {
        this.fc = fc;
        this.charsetDecoder = charset.newDecoder();
        this.bufferSize = bufferSize;
    }

    public BStringReader(FileChannel fc, Charset charset) {
        this.fc = fc;
        this.charsetDecoder = charset.newDecoder();
        this.bufferSize = DEFAULT_BUFFER_SIZE;
    }

    public BStringReader(FileChannel fc) {
        this.fc = fc;
        this.charsetDecoder = Charset.defaultCharset().newDecoder();
        this.bufferSize = DEFAULT_BUFFER_SIZE;
    }

    public BString readLine() throws IOException {
        CharBuffer line = CharBuffer.allocate(bufferSize);
        fillBuffer();
        char c;

        while (size > 0) {
            c = buffer.get();
            line.put(c);
            position++;

            // Check for eol
            if ((c == '\n') || (c == '\r')) {
                return new BString(trimLine(line));
            }

            if (buffer.remaining() == 0) {
                fillBuffer();
                line = expandLine(line);
            }

        }

        return null;


    }

    private void fillBuffer() throws IOException {
        long edgeSize = fc.size() - position;
        size = (edgeSize < bufferSize) ? edgeSize : bufferSize;
        file = fc.map(FileChannel.MapMode.READ_ONLY, position, size);
        buffer = charsetDecoder.decode(file);
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
