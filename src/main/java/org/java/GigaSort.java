import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class GigaSort {

  private final static String TMP_CHUNK_PREFIX = "ch";
  private final static String TMP_CHUNK_DIR = "/home/ulises/playground/mergersort/tmp/";
  private final static int BUFFER_SIZE = 100000;
  private final static String OUTPUT_FILE = "sorted";
  private int chunks = 0;
  private boolean EOF = false;
  private BufferedReader inputBufferedReader;

  public GigaSort(String filename) throws IOException {
    this.chunks = 0;
    // Start with empty files
    // FileWriter out = new FileWriter(OUTPUT_FILE);
    // out.close();
    File tmpdir = new File(TMP_CHUNK_DIR);

    tmpdir.mkdir();
    inputBufferedReader = new BufferedReader(new FileReader(filename));

    chunkSort();

    while (!EOF) {
      chunkSort();
    }

    System.out.println(chunks);

    inputBufferedReader.close();

    mergeSort();
  }

  private void chunkSort() throws IOException {
    FileWriter fileWriter = new FileWriter(TMP_CHUNK_DIR + TMP_CHUNK_PREFIX + "_" + chunks);
    List<String> sortBuffer = new ArrayList<String>();

    String line = null;

    // Fill in the buffer
    for (int i = 0; i < BUFFER_SIZE; i++) {
      if ((line = inputBufferedReader.readLine()) != null) {
        sortBuffer.add(line);
      } else {
        EOF = true;
      }
    }

    // Flush it to the chunk file
    Collections.sort(sortBuffer);
    for (String str : sortBuffer) {
      fileWriter.write(str + "\n");
    }

    fileWriter.close();
    chunks += 1;
  }

  private void mergeSort() throws IOException {
        /*
         * Magic goes here
         *
         * Open all the chunks, and read one line of each per iteration. Since
         * they are already sorted, the line being read will be the local
         * minimum. Put that into a priority queue and pick the minimum of all
         * minimums. Update each file's local minimum on each iteration.
         *
         * Finally, flush the buffer once it fills up.
         */

    final int MERGE_BUFFER_SIZE = BUFFER_SIZE / chunks;

    BufferedWriter outputBufferedWriter = new BufferedWriter(new FileWriter(OUTPUT_FILE));
    File tmpdir = new File(TMP_CHUNK_DIR);
    File[] chunkList = tmpdir.listFiles();

    PriorityQueue<FileSortMap> pq = new PriorityQueue<FileSortMap>();

    int currentBuffersize = 0;

    // Open the chunks && load initial data
    for (File f : chunkList) {
      FileSortMap map = new FileSortMap(f);
      if (map.minimum != null) {
        pq.add(new FileSortMap(f));
      }
    }

    // Put the minimum in the output buffer && update local minimums
    while (!pq.isEmpty()) {
      // Check if the output buffer is full && flush it
      if (currentBuffersize < MERGE_BUFFER_SIZE) {
        FileSortMap map = pq.poll();
        outputBufferedWriter.write(map.minimum);
        outputBufferedWriter.newLine();
        currentBuffersize += 1;
        map.fetchNext();
        if (map.minimum != null) {
          pq.add(map);
        }
      } else {
        currentBuffersize = 0;
        outputBufferedWriter.flush();
      }
    }

    outputBufferedWriter.close();

    // Cleanup
    // TODO more cleanup:
    // surround stuff in try catch & finally cleanup
    // In the current setup if the program fails it won't start in an empty
    // workspace
    for (File f : chunkList) {
      f.delete();
    }
    tmpdir.delete();

  }

  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.out.println("Please provide exactly one file to be sorted.");
      return;
    }

    String inputFilename = args[0];

    new GigaSort(inputFilename);
  }

  private class FileSortMap implements Comparable<FileSortMap> {
    private String minimum;
    private String filename;
    private BufferedReader inputBufferedReader;

    public FileSortMap(File f) throws IOException {
      this.filename = f.getName();
      this.inputBufferedReader = new BufferedReader(new FileReader(f));
      String line = null;
      if ((line = inputBufferedReader.readLine()) != null) {
        this.minimum = line;
      }
    }

    // Update the current minimum
    public void fetchNext() throws IOException {
      String line = null;
      if ((line = inputBufferedReader.readLine()) != null) {
        this.minimum = line;
      } else {
        inputBufferedReader.close();
        this.minimum = null;
      }
    }

    @Override
    public String toString() {
      return "FileSortMapping [sortable=" + minimum + ", filename=" + filename + "]";
    }

    @Override
    public int compareTo(FileSortMap other) {
      return minimum.compareTo(other.minimum);
    }

  }

}