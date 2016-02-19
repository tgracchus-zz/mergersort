package org.java.nio;

import org.java.externalsort.Chunk;
import org.java.lang.TString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Not Thread Safe Class !!!
 * Created by ulises.olivenza on 18/02/16.
 */
public class TFileReader {


    private final static Logger log = LoggerFactory.getLogger(TFileReader.class);
    private final static int DEFAULT_BUFFER_SIZE = 1024 * 2;

    private final int bufferSize;
    private final CharsetDecoder charsetDecoder;

    private FileChannel fc;

    private char[] buffer;
    private int bufferFrom;
    private int bufferTo;

    private CharBuffer line;
    private boolean eof;
    private boolean eol;

    public TFileReader(TFile tFile, Charset charset, int bufferSize) throws FileNotFoundException {
        fc = new RandomAccessFile(tFile.file(), "r").getChannel();
        this.charsetDecoder = charset.newDecoder();
        this.bufferSize = bufferSize;
        bufferTo = 0;
        bufferFrom = 0;
        buffer = null;
        eof = false;
    }

    public TFileReader(Chunk chunk, Charset charset) throws FileNotFoundException {
        this(chunk, charset, DEFAULT_BUFFER_SIZE);
    }

    public TFileReader(Chunk chunk) throws FileNotFoundException {
        this(chunk, Charset.defaultCharset(), DEFAULT_BUFFER_SIZE);
    }

    public List<TString> readLines() throws IOException {

        List<TString> lines = new ArrayList<>();

        TString line;
        while (null != (line = readLine())) {
            lines.add(line);
        }
        return lines;

    }


    public TString readLine() throws IOException {
        char c;
        line = CharBuffer.allocate(0);

        if (buffer == null) {
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
                return new TString(line.array());
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
