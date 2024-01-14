package advent.of.code.year_2023.day13;

import advent.of.code.year_2023.day11.CosmicExpansionOptimized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PointOfIncidence {

  private static final String INPUT_FILE = "/year2023/day_13_input.txt";

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new PointOfIncidence().getSummary());
      //log.info("The result for part two is: {}", new PointOfIncidence().getSummary());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  private long getSummary() throws IOException {

    List<char[]> notes = new ArrayList<>();

    long sum = 0;
    char[][] notesArray = null;
    InputStream is = CosmicExpansionOptimized.class.getResourceAsStream(INPUT_FILE);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (StringUtils.isNotEmpty(line)) {
          notes.add(line.toCharArray());
        } else {
          notesArray = notes.toArray(new char[0][0]);
          notes.clear();

          sum = sumForMirrorRow(notesArray, sum);
        }
      }
    }

    // process last note
    notesArray = notes.toArray(new char[0][0]);
    sum = sumForMirrorRow(notesArray, sum);


    return sum;
  }

  private static long sumForMirrorRow(char[][] notesArray, long sum) {
    int mirrorRow = getMirrorRow(notesArray);
    if (isNumberValid(mirrorRow)) {
      sum += (mirrorRow + 1) * 100L;
    }
    return sum;
  }

  private static int getMirrorRow(char[][] notesArray) {
    int mirrorRow = getInvalidNumber();
    for (int y = 0; y < notesArray.length; y++) {
      if (y + 1 < notesArray.length) {

        if (areRowsEqual(notesArray[y], notesArray[y + 1])) {
          int topRow = y - 1;
          int bottomRow = y + 2;

          boolean mirrorFound = true;
          while (topRow >= 0 && bottomRow < notesArray.length && mirrorFound) {
            if (!areRowsEqual(notesArray[topRow], notesArray[bottomRow])) {
              mirrorFound = false;
            }
            topRow--;
            bottomRow++;
          }
          if (mirrorFound) {
            mirrorRow = y;
            break;
          }
        }
      }
    }

    return mirrorRow;
  }

  private static boolean isNumberValid(int mirrorRow) {
    return mirrorRow != Integer.MIN_VALUE;
  }

  private static int getInvalidNumber() {
    return Integer.MIN_VALUE;
  }

  private static boolean areRowsEqual(char[] rowA, char[] rowB) {
    for (int i = 0; i < rowA.length; i++) {
      if (rowA[i] != rowB[i]) {
        return false;
      }
    }
    return true;
  }
}
