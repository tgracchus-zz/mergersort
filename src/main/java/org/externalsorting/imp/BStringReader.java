package org.externalsorting.imp;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Created by ulises.olivenza on 18/02/16.
 */
public class BStringReader {

    private final FileChannel fc;
    private long filePosition = 0;

    private MappedByteBuffer file;

    private char c = Character.MIN_VALUE;

    private boolean eol = false;

    private CharBuffer buffer;
    private final static int BUFFER_SIZE_MAX = 1000;
    private final CharsetDecoder charsetDecoder;

    public BStringReader(FileChannel fc, Charset charset) {
        this.fc = fc;
        this.charsetDecoder = charset.newDecoder();
    }

    public BString readLine() throws IOException {
        file = fc.map(FileChannel.MapMode.READ_ONLY, filePosition, fc.size());
        buffer = CharBuffer.allocate(BUFFER_SIZE_MAX);
        CharBuffer charFile = charsetDecoder.decode(file);

        System.out.println("LIMIT" + charFile.limit());
        for (long i = filePosition; i < charFile.limit(); i++) {
            c = charFile.get();

            // Update general file position
            filePosition = i;

            // Check buffer
            if (buffer.position() > BUFFER_SIZE_MAX) {
                expandBuffer();
            } else {
                // Check for eol
                if ((c == '\n') || (c == '\r')) {
                    eol = true;
                } else {
                    buffer.append(c);
                }
            }

            // Check if end line
            if (eol) {
                eol = false;
                // return line
                CharBuffer newString = CharBuffer.allocate(buffer.position());
                newString.put(buffer.array(), 0, buffer.position());
                return new BString(newString.array());
            }

        }

        return null;
    }

    private void expandBuffer() {
        buffer.limit(buffer.limit() + BUFFER_SIZE_MAX);
    }

}
