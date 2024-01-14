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
      log.info("The result for part two is: {}", new PointOfIncidence().getSummaryWithSmudge());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  long getSummary() throws IOException {

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

          sum += sumForMirrorRow(notesArray) + sumForMirrorColumn(notesArray);
        }
      }
    }

    // process last note
    notesArray = notes.toArray(new char[0][0]);
    sum += sumForMirrorRow(notesArray) + sumForMirrorColumn(notesArray);


    return sum;
  }

  long getSummaryWithSmudge() throws IOException {

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
          sum += calculateSumConsideringSludge(notesArray);
        }
      }
    }

    // process last note
    notesArray = notes.toArray(new char[0][0]);
    sum += calculateSumConsideringSludge(notesArray);


    return sum;
  }

  private static long calculateSumConsideringSludge(char[][] notesArray) {
    long sumCandidate = 0;

    int columnMirrorToBeIgnored = (int) sumForMirrorColumn(notesArray) - 1;
    int rowMirrorToBeIgnored = (int) (sumForMirrorRow(notesArray) / 100) - 1;

    sumCandidateFound:
    for (int y = 0; y < notesArray.length; y++) {
      for (int x = 0; x < notesArray[y].length; x++) {
        switchValue(notesArray, y, x);
        long candidate = sumForMirrorRow(notesArray, rowMirrorToBeIgnored) + sumForMirrorColumn(notesArray, columnMirrorToBeIgnored);
        if (candidate > 0) {
          sumCandidate = candidate;
          break sumCandidateFound;
        }

        switchValue(notesArray, y, x);
      }
    }

    return sumCandidate;
  }

  private static void switchValue(char[][] notesArray, int y, int x) {
    if (notesArray[y][x] == '#') {
      notesArray[y][x] = '.';
    } else {
      notesArray[y][x] = '#';
    }
  }

  private static long sumForMirrorRow(char[][] notesArray) {
    return sumForMirrorRow(notesArray, getInvalidNumber());
  }

  private static long sumForMirrorRow(char[][] notesArray, int mirrorToBeIgnored) {
    int mirrorRow = getMirrorRow(notesArray, mirrorToBeIgnored);
    if (isNumberValid(mirrorRow)) {
      return (mirrorRow + 1) * 100L;
    }
    return 0;
  }

  private static long sumForMirrorColumn(char[][] notesArray) {
    return sumForMirrorColumn(notesArray, getInvalidNumber());
  }

  private static long sumForMirrorColumn(char[][] notesArray, int mirrorToBeIgnored) {
    int mirrorColumn = getMirrorColumn(notesArray, mirrorToBeIgnored);
    if (isNumberValid(mirrorColumn)) {
      return mirrorColumn + 1;
    }
    return 0;
  }

  private static int getMirrorColumn(char[][] notesArray, int mirrorToBeIgnored) {
    int mirrorColumn = getInvalidNumber();
    for (int x = 0; x < notesArray[0].length; x++) {
      if (x + 1 < notesArray[0].length) {

        if (isNumberValid(mirrorToBeIgnored) && mirrorToBeIgnored == x) {
          continue;
        }

        if (areArraysEqual(getColumn(notesArray, x), getColumn(notesArray, x + 1))) {
          int leftColumn = x - 1;
          int rightColumn = x + 2;

          boolean mirrorFound = true;
          while (leftColumn >= 0 && rightColumn < notesArray[0].length && mirrorFound) {
            if (!areArraysEqual(getColumn(notesArray, leftColumn), getColumn(notesArray, rightColumn))) {
              mirrorFound = false;
            }
            leftColumn--;
            rightColumn++;
          }
          if (mirrorFound) {
            mirrorColumn = x;
            break;
          }
        }
      }
    }

    return mirrorColumn;
  }

  private static int getMirrorColumn(char[][] notesArray) {
    return getMirrorColumn(notesArray, getInvalidNumber());
  }

  private static int getMirrorRow(char[][] notesArray, int mirrorToBeIgnored) {
    int mirrorRow = getInvalidNumber();
    for (int y = 0; y < notesArray.length; y++) {
      if (y + 1 < notesArray.length) {

        if (isNumberValid(mirrorToBeIgnored) && y == mirrorToBeIgnored) {
          continue;
        }

        if (areArraysEqual(notesArray[y], notesArray[y + 1])) {
          int topRow = y - 1;
          int bottomRow = y + 2;

          boolean mirrorFound = true;
          while (topRow >= 0 && bottomRow < notesArray.length && mirrorFound) {
            if (!areArraysEqual(notesArray[topRow], notesArray[bottomRow])) {
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

  private static int getMirrorRow(char[][] notesArray) {
    return getMirrorRow(notesArray, getInvalidNumber());
  }

  private static boolean isNumberValid(int mirrorRow) {
    return mirrorRow != Integer.MIN_VALUE;
  }

  private static int getInvalidNumber() {
    return Integer.MIN_VALUE;
  }

  private static char[] getColumn(char[][] from, int columnIndex) {
    char[] column = new char[from.length];
    for (int y = 0; y < column.length; y++) {
      column[y] = from[y][columnIndex];
    }
    return column;
  }

  private static boolean areArraysEqual(char[] rowA, char[] rowB) {
    for (int i = 0; i < rowA.length; i++) {
      if (rowA[i] != rowB[i]) {
        return false;
      }
    }
    return true;
  }
}
