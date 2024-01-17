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
      log.info("The result for part one is: {}", new ParabolicReflectorDish().getTotalLoadOfNorthSupportBeams());
      log.info("The result for part two is: {}", new ParabolicReflectorDish().getTotalLoadAfterCycles(1000000000));
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  long getTotalLoadOfNorthSupportBeams() throws IOException {
    long sum = 0;

    char[][] puzzle = readPuzzle();
    tiltNorth(puzzle);
    sum = calculateLoad(puzzle);

    return sum;
  }

  long getTotalLoadAfterCycles(int numberOfCycles) throws IOException {
    long sum = 0;

    Map<CacheKey, char[][]> cache = new HashMap<>();
    char[][] puzzle = readPuzzle();

    /*
      Spin the puzzle and cache each result.When a cash is hit, use it.
      One could ue the cache indefinitely, which would solve the problem
      for the statement in the problem at part 2. However, for the input given
      it would take forever to run. Instead, take advantage that there is
      a cycle within the cache and find th final puzzle.
     */
    int spins = 0;
    while (spins < numberOfCycles) {
      CacheKey key = new CacheKey(clone(puzzle));
      if (cache.containsKey(key)) {
        puzzle = cache.get(key);
        break;
      } else {
        tiltNorth(puzzle);
        tiltWest(puzzle);
        tiltSouth(puzzle);
        tiltEast(puzzle);
        cache.put(key, clone(puzzle));
      }
      spins++;
    }

    char[][] initialPuzzle = clone(puzzle);
    int cycleSize = calculateCycleSize(puzzle, cache, initialPuzzle);

    /*
    After detecting the cyclic dependence and its size, one may calculate
    how many steps are necessary to find the puzzle using the following approach.

    Suppose:
      - total spins to execute: 10
      - cyclic cycle has 3 elements
      - after 2 spins the cycle is found
      Therefore:
      1. the cycle starts at 2, meaning there are 8 spins to run
      2. running through the cache 3 times, the second element of
      it will be the answer. Think about it: the cycle is found at
      the 2nd spin, meaning there are 8 spins left. For that, 3 times
      running the cycle, one finds the answer at the 2nd element of the
      cycle.
      Therefore the formula is: (spins to execute - (spinx - cycle size)) mod cycle size

     */
    int counter = 1;
    puzzle = clone(initialPuzzle);
    while (counter < (numberOfCycles - spins - cycleSize) % cycleSize) {
      puzzle = cache.get(new CacheKey(puzzle));
      counter++;
    }

    return calculateLoad(puzzle);
  }

  /*
  Give the initial puzzle, find the size of the circular dependency.
  For that, simply search through the cache until the initial puzzle
  is found again.
   */
  private static int calculateCycleSize(char[][] puzzle, Map<CacheKey, char[][]> cache, char[][] initialPuzzle) {
    int cycleSize = 0;
    boolean initialPuzzleFound = false;
    while (!initialPuzzleFound) {
      cycleSize++;
      puzzle = cache.get(new CacheKey(puzzle));
      if (Arrays.deepEquals(initialPuzzle, puzzle)) {
        initialPuzzleFound = true;
      }
    }
    return cycleSize;
  }

  private static long calculateLoad(char[][] puzzle) {
    long sum = 0;
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

  public record CacheKey(char[][] puzzle) {
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CacheKey cacheKey = (CacheKey) o;
      return Arrays.deepEquals(this.puzzle, cacheKey.puzzle);
    }

    @Override
    public int hashCode() {
      return 31 + Arrays.deepHashCode(puzzle);
    }

    @Override
    public String toString() {
      return "CacheKey{" +
              "puzzle=" + Arrays.deepToString(puzzle) +
              '}';
    }
  }
}
