package org.java.esort.model;

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
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.List;

/**
 * Not Thread Safe Class !!!
 * Created by ulises.olivenza on 18/02/16.
 */
public class TFileReader {


    private final static Logger log = LoggerFactory.getLogger(TFileReader.class);
    private final static int DEFAULT_BUFFER_SIZE = 1024 * 4 * 1024;
    private final static int EXPECTED_LINE_SIZE = 160;

    private int bufferSize;
    private final CharsetDecoder charsetDecoder;

    private FileChannel fc;

    private CharBuffer buffer;
    private int bufferTo;
    private int bufferFrom;


    private CharBuffer line;
    private boolean eof;
    private boolean eol;

    private TFile tFile;


    public TFileReader(TFile tFile, Charset charset, int bufferSize) throws RuntimeException {
        try {
            fc = new RandomAccessFile(tFile.file(), "r").getChannel();
        }catch (FileNotFoundException e){
            new RuntimeException();
        }
        this.charsetDecoder = charset.newDecoder();
        this.charsetDecoder.onMalformedInput(CodingErrorAction.IGNORE);
        this.charsetDecoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
        this.bufferSize = bufferSize;
        eof = false;
        this.tFile = tFile;
        bufferTo = 0;
        bufferFrom = 0;
        this.buffer = CharBuffer.allocate(0);
    }

    public TFileReader(TFile tFile, Charset charset) throws RuntimeException {
        this(tFile, charset, DEFAULT_BUFFER_SIZE);
    }

    public TFileReader(TFile tFile) throws RuntimeException {
        this(tFile, Charset.defaultCharset(), DEFAULT_BUFFER_SIZE);
    }

    /**
     * Try to read k lines up to size
     *
     * @param atLeast the minimum numbers of bytes to read
     * @return Lines than weight at least the given size
     * @throws IOException
     */
    public Lines readLines(long atLeast) throws IOException {
        long readBytes = 0;
        List<TString> lines = new ArrayList<>((int) atLeast / EXPECTED_LINE_SIZE);

        TString line;
        while (readBytes < atLeast && null != (line = readLine())) {
            lines.add(line);
            readBytes += line.size();
        }
        return new Lines(lines);

    }


    public TString readLine() throws IOException {
        char c;
        line = CharBuffer.allocate(0);


        while (!eof && !eol) {

            if (!buffer.hasRemaining()) {
                fillBuffer();
            }

            // Check for eol
            while (buffer.hasRemaining() && !eol) {
                c = buffer.get();
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
        buffer = charsetDecoder.decode(byteBuffer);
        bufferTo = 0;
        bufferFrom = 0;
    }

    private void copyFromBufferToLine() {
        CharBuffer newLine = CharBuffer.allocate(line.position() + (bufferTo - bufferFrom));
        newLine.put(line.array(), 0, line.position());
        line = newLine;
        line.put(buffer.array(), bufferFrom, bufferTo - bufferFrom);

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TFileReader that = (TFileReader) o;

        return tFile != null ? tFile.equals(that.tFile) : that.tFile == null;

    }

    @Override
    public int hashCode() {
        return tFile != null ? tFile.hashCode() : 0;
    }
}
