package org.textstring;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
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

    private char[] buffer;
    private int bufferFrom;
    private int bufferTo ;

    private CharBuffer line;
    private boolean eof;
    private boolean eol;

    public TextStringReader(FileChannel fc, Charset charset, int bufferSize) {
        this.fc = fc;
        this.charsetDecoder = charset.newDecoder();
        this.bufferSize = bufferSize;
        bufferTo = 0;
        bufferFrom = 0;
        buffer=null;
        eof = false;
    }

    public TextStringReader(FileChannel fc, Charset charset) {
        this(fc, charset, DEFAULT_BUFFER_SIZE);
    }

    public TextStringReader(FileChannel fc) {
        this(fc, Charset.defaultCharset(), DEFAULT_BUFFER_SIZE);
    }

    public TextString readLine() throws IOException {
        char c;
        line = CharBuffer.allocate(0);

        if(buffer==null){
            fillBuffer();
        }

        while (!eof && !eol) {

            if (bufferTo >= buffer.length) {
                fillBuffer();
            }

            // Check for eol
            while (bufferTo < buffer.length && !eol) {
                c = buffer[bufferTo];
                bufferTo++;
                if ((c == '\n') || (c == '\r')) {
                    eol = true;
                }
            }

            copyFromBufferToLine();
            if (eol) {
                eol = false;
                return new TextString(line.array());
            }
        }

        return null;
    }

    private void fillBuffer() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
        eof = fc.read(byteBuffer) == -1;
        byteBuffer.flip();
        buffer = charsetDecoder.decode(byteBuffer).array();
        bufferTo = 0;
        bufferFrom = 0;
    }

    private void copyFromBufferToLine() {
        CharBuffer newLine = CharBuffer.allocate(line.position() + bufferTo - bufferFrom);
        newLine.put(line.array(), 0, line.position());
        line = newLine;

        line.put(buffer, bufferFrom, bufferTo - bufferFrom);

        if (bufferTo < bufferSize) {
            bufferFrom = bufferTo;
        }

    }

}
