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

    private final static int DEFAULT_BUFFER_SIZE = 1024 * 2;

    private final FileChannel fc;
    private final int bufferSize;
    private final CharsetDecoder charsetDecoder;

    private MappedByteBuffer file;
    private int copyToBuffer = 0;
    private int copyFromBuffer = 0;
    private char[] buffer;

    private CharBuffer line;

    private long position = 0;
    private long size;

    public TextStringReader(FileChannel fc, Charset charset, int bufferSize) {
        this.fc = fc;
        this.charsetDecoder = charset.newDecoder();
        this.bufferSize = bufferSize;
    }

    public TextStringReader(FileChannel fc, Charset charset) {
        this(fc, charset, DEFAULT_BUFFER_SIZE);
    }

    public TextStringReader(FileChannel fc) {
        this(fc, Charset.defaultCharset(), DEFAULT_BUFFER_SIZE);
    }


    public TextString readLine() throws IOException {
        line = CharBuffer.allocate(0);
        calculateSize();
        char c;

        if (buffer == null) {
            fillBuffer();
        }

        while (size > 0) {

            if (copyToBuffer >= buffer.length) {
                copyFromBufferToLine(copyToBuffer);
                fillBuffer();
            }

            c = buffer[copyToBuffer];
            copyToBuffer++;
            position++;

            // Check for eol
            if ((c == '\n') || (c == '\r')) {
                if (copyToBuffer > 0) {
                    copyFromBufferToLine(copyToBuffer - 1);
                    copyToBuffer = copyToBuffer -1;
                }
                copyFromBuffer = copyToBuffer;
                return new TextString(line.array());
            }

        }

        return null;
    }

    private void fillBuffer() throws IOException {
        calculateSize();
        file = fc.map(FileChannel.MapMode.READ_ONLY, position, size);
        buffer = charsetDecoder.decode(file).array();
        copyToBuffer = 0;
    }

    private void calculateSize() throws IOException {
        long edgeSize = fc.size() - position;
        size = (edgeSize < bufferSize) ? edgeSize : bufferSize;
    }


    private void copyFromBufferToLine(int to) {
        CharBuffer newLine = CharBuffer.allocate(line.position() + to);
        newLine.put(line.array(), 0, line.position());
        line = newLine;
        line.put(buffer, copyFromBuffer, to);
    }


}
