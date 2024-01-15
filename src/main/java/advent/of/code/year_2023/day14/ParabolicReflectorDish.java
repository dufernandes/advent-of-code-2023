package advent.of.code.year_2023.day14;

import advent.of.code.year_2023.day11.CosmicExpansionOptimized;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
public class ParabolicReflectorDish {

  private static final char ROUNDED_ROCK = 'O';
  private static final char CUBE_ROCK = '#';
  private static final char SPACE = '.';
  private static final String INPUT_FILE = "/year2023/day_14_input.txt";

  public static void main(String[] args) {
    try {
      //log.info("The result for part one is: {}", new ParabolicReflectorDish().getTotalLoadOfNorthSupportBeams());
      log.info("The result for part two is: {}", new ParabolicReflectorDish().getTotalLoadAfterCycles(1000000000));
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  long getTotalLoadOfNorthSupportBeams() throws IOException {
    long sum = 0;

    char[][] puzzle = readPuzzle();

    tiltNorth(puzzle);

    for (int y = 0; y < puzzle.length; y++) {
      int rowSum = 0;
      for (int x = 0; x < puzzle[0].length; x++) {
        if (puzzle[y][x] == ROUNDED_ROCK) {
          rowSum++;
        }
      }
      sum += (long) rowSum * (puzzle.length - y);
    }

    return sum;
  }

  long getTotalLoadAfterCycles(int numberOfCycles) throws IOException {
    long sum = 0;

    Map<CacheKey, char[][]> cache = new HashMap<>();
    char[][] puzzle = readPuzzle();

    int counter = 0;
    char[][] oldPuzzle = null;
    while (counter < numberOfCycles) {
      if (cache.containsKey(new CacheKey(Direction.North, puzzle))) {
        puzzle = cache.get(new CacheKey(Direction.North, puzzle));
      } else {
        oldPuzzle = clone(puzzle);
        tiltNorth(puzzle);
        cache.put(new CacheKey(Direction.North, oldPuzzle), puzzle);
      }

      if (cache.containsKey(new CacheKey(Direction.West, puzzle))) {
        puzzle = cache.get(new CacheKey(Direction.West, puzzle));
      } else {
        oldPuzzle = clone(puzzle);
        tiltWest(puzzle);
        cache.put(new CacheKey(Direction.West, oldPuzzle), puzzle);
      }

      if (cache.containsKey(new CacheKey(Direction.South, puzzle))) {
        puzzle = cache.get(new CacheKey(Direction.South, puzzle));
      } else {
        oldPuzzle = clone(puzzle);
        tiltSouth(puzzle);
        cache.put(new CacheKey(Direction.South, oldPuzzle), puzzle);
      }

      if (cache.containsKey(new CacheKey(Direction.East, puzzle))) {
        puzzle = cache.get(new CacheKey(Direction.East, puzzle));
      } else {
        oldPuzzle = clone(puzzle);
        tiltEast(puzzle);
        cache.put(new CacheKey(Direction.East, oldPuzzle), puzzle);
      }
      System.out.println(counter);
      counter++;
    }

    print2D(puzzle);

    for (int y = 0; y < puzzle.length; y++) {
      int rowSum = 0;
      for (int x = 0; x < puzzle[0].length; x++) {
        if (puzzle[y][x] == ROUNDED_ROCK) {
          rowSum++;
        }
      }
      sum += (long) rowSum * (puzzle.length - y);
    }

    return sum;
  }

  private char[][] clone(char[][] array) {
    char[][] cloned = new char[array.length][array[0].length];
    for (int y = 0; y < array.length; y++) {
      for (int x = 0; x < array[y].length; x++) {
        cloned[y][x] = array[y][x];
      }
    }
    return cloned;
  }

  private static void tiltEast(char[][] puzzle) {
    Stack<Integer> fixedElements = new Stack<>();
    fixedElements.push(puzzle[0].length);

    for (int y = 0; y < puzzle.length; y++) {
      for (int x = puzzle[0].length - 1; x >= 0; x--) {
        switch (puzzle[y][x]) {
          case ROUNDED_ROCK -> {
            // move rounded rock until the last element - 1
            int mostRecentFixedElement = fixedElements.peek();
            if (mostRecentFixedElement > x) {
              puzzle[y][x] = SPACE;
              puzzle[y][mostRecentFixedElement - 1] = ROUNDED_ROCK;
              fixedElements.push(mostRecentFixedElement - 1);
            }
          }
          case CUBE_ROCK -> {
            fixedElements.push(x);
          }
        }
      }
      fixedElements.clear();
      fixedElements.push(puzzle[0].length);
    }
  }

  private static void tiltWest(char[][] puzzle) {
    Stack<Integer> fixedElements = new Stack<>();
    fixedElements.push(-1);

    for (int y = 0; y < puzzle.length; y++) {
      for (int x = 0; x < puzzle[0].length; x++) {
        switch (puzzle[y][x]) {
          case ROUNDED_ROCK -> {
            // move rounded rock until the last element + 1
            int mostRecentFixedElement = fixedElements.peek();
            if (mostRecentFixedElement < x) {
              puzzle[y][x] = SPACE;
              puzzle[y][mostRecentFixedElement + 1] = ROUNDED_ROCK;
              fixedElements.push(mostRecentFixedElement + 1);
            }
          }
          case CUBE_ROCK -> {
            fixedElements.push(x);
          }
        }
      }
      fixedElements.clear();
      fixedElements.push(-1);
    }
  }

  private static void tiltSouth(char[][] puzzle) {
    Stack<Integer> fixedElements = new Stack<>();
    fixedElements.push(puzzle.length);

    for (int x = 0; x < puzzle[0].length; x++) {
      for (int y = puzzle.length - 1; y >= 0; y--) {
        switch (puzzle[y][x]) {
          case ROUNDED_ROCK -> {
            // move rounded rock until the last element - 1
            int mostRecentFixedElement = fixedElements.peek();
            if (mostRecentFixedElement > y) {
              puzzle[y][x] = SPACE;
              puzzle[mostRecentFixedElement - 1][x] = ROUNDED_ROCK;
              fixedElements.push(mostRecentFixedElement - 1);
            }
          }
          case CUBE_ROCK -> {
            fixedElements.push(y);
          }
        }
      }
      fixedElements.clear();
      fixedElements.push(puzzle.length);
    }
  }

  private static void tiltNorth(char[][] puzzle) {
    Stack<Integer> fixedElements = new Stack<>();
    fixedElements.push(-1);

    for (int x = 0; x < puzzle[0].length; x++) {
      for (int y = 0; y < puzzle.length; y++) {
        switch (puzzle[y][x]) {
          case ROUNDED_ROCK -> {
            // move rounded rock until the last element + 1
            int mostRecentFixedElement = fixedElements.peek();
            if (mostRecentFixedElement < y) {
              puzzle[y][x] = SPACE;
              puzzle[mostRecentFixedElement + 1][x] = ROUNDED_ROCK;
              fixedElements.push(mostRecentFixedElement + 1);
            }
          }
          case CUBE_ROCK -> {
            fixedElements.push(y);
          }
        }
      }
      fixedElements.clear();
      fixedElements.push(-1);
    }
  }

  private static char[][] readPuzzle() throws IOException {
    List<char[]> puzzleList = new ArrayList<>();

    InputStream is = CosmicExpansionOptimized.class.getResourceAsStream(INPUT_FILE);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        puzzleList.add(line.toCharArray());
      }
    }

    return puzzleList.toArray(new char[0][0]);
  }

  private static void print2D(char[][] mat) {
    for (char[] row : mat) {
      for (char element : row) {
        System.out.print(element + " ");
      }
      System.out.println();
    }
    System.out.println();
  }

  private enum Direction {
    North,
    West,
    South,
    East
  }

  public record CacheKey(Direction direction, char[][] puzzle) {
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CacheKey cacheKey = (CacheKey) o;
      return direction == cacheKey.direction && Arrays.deepEquals(puzzle, cacheKey.puzzle);
    }

    @Override
    public int hashCode() {
      int result = Objects.hash(direction);
      result = 31 * result + Arrays.deepHashCode(puzzle);
      return result;
    }
  }
}
