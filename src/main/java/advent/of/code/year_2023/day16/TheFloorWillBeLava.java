package advent.of.code.year_2023.day16;

import advent.of.code.year_2023.util.Tuple;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class TheDayWillBeLava {

  private static final String INPUT_FILE = "/year2023/day_16_input.txt";

  private static final Set<Tuple<Integer, Integer, Direction>> cache = new HashSet<>();

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new TheDayWillBeLava().sumEnergizedBeams());
      log.info("The result for part two is: {}", new TheDayWillBeLava().sumEnergizedBeamsOfBestScenario());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  long sumEnergizedBeamsOfBestScenario() throws IOException {
    long sum = 0;
    char[][][] contraption = populateContraption();

    for (int y = 0; y < contraption.length; y++) {
      cleanCache();
      visitAndMoveRight(contraption, y, 0);
      long currentSum = sumBeams(contraption);
      cleanContraption(contraption);
      if (currentSum > sum) {
        sum = currentSum;
      }
    }

    for (int y = 0; y < contraption.length; y++) {
      cleanCache();
      visitAndMoveLeft(contraption, y, contraption[y].length - 1);
      long currentSum = sumBeams(contraption);
      cleanContraption(contraption);
      if (currentSum > sum) {
        sum = currentSum;
      }
    }

    for (int x = 0; x < contraption[0].length; x++) {
      cleanCache();
      visitAndMoveDown(contraption, 0, x);
      long currentSum = sumBeams(contraption);
      cleanContraption(contraption);
      if (currentSum > sum) {
        sum = currentSum;
      }
    }

    for (int x = 0; x < contraption[0].length; x++) {
      cleanCache();
      visitAndMoveUp(contraption, contraption.length - 1, x);
      long currentSum = sumBeams(contraption);
      cleanContraption(contraption);
      if (currentSum > sum) {
        sum = currentSum;
      }
    }

    return sum;
  }

  long sumEnergizedBeams() throws IOException {
    char[][][] contraption = populateContraption();

    visitAndMoveRight(contraption, 0, 0);

    return sumBeams(contraption);
  }

  private static long sumBeams(char[][][] contraption) {
    long sum = 0;
    for (int y = 0; y < contraption.length; y++) {
      for (int x = 0; x < contraption[y].length; x++) {
        if (contraption[y][x][1] == '#') {
          sum++;
        }
      }
    }
    return sum;
  }

  private static void cleanContraption(char[][][] contraption) {
    for (int y = 0; y < contraption.length; y++) {
      for (int x = 0; x < contraption[y].length; x++) {
        contraption[y][x][1] = ' ';
      }
    }
  }

  void goRight(char[][][] contraption, int y, int x) {
    if (x + 1 >= contraption[y].length || isCached(y, x, Direction.Right)) {
      return;
    }

    cacheLocationAndDirection(y, x, Direction.Right);

    x++;
    visitAndMoveRight(contraption, y, x);
  }

  private void visitAndMoveRight(char[][][] contraption, int y, int x) {
    char value = markVisited(contraption[y], x);
    switch (value) {
      case '.' -> goRight(contraption, y, x);
      case '/' -> goUp(contraption, y, x);
      case '\\' -> goDown(contraption, y, x);
      case '-' -> goRight(contraption, y, x);
      case '|' -> {
        goUp(contraption, y, x);
        goDown(contraption, y, x);
      }
    }
  }

  private void goDown(char[][][] contraption, int y, int x) {
    if (y + 1 >= contraption.length || isCached(y, x, Direction.Down)) {
      return;
    }

    cacheLocationAndDirection(y, x, Direction.Down);

    y++;
    visitAndMoveDown(contraption, y, x);
  }

  private void visitAndMoveDown(char[][][] contraption, int y, int x) {
    char value = markVisited(contraption[y], x);
    switch (value) {
      case '.' -> goDown(contraption, y, x);
      case '/' -> goLeft(contraption, y, x);
      case '\\' -> goRight(contraption, y, x);
      case '|' -> goDown(contraption, y, x);
      case '-' -> {
        goLeft(contraption, y, x);
        goRight(contraption, y, x);
      }
    }
  }

  private void goLeft(char[][][] contraption, int y, int x) {
    if (x - 1 < 0 || isCached(y, x, Direction.Left)) {
      return;
    }

    cacheLocationAndDirection(y, x, Direction.Left);

    x--;
    visitAndMoveLeft(contraption, y, x);
  }

  private void visitAndMoveLeft(char[][][] contraption, int y, int x) {
    char value = markVisited(contraption[y], x);
    switch (value) {
      case '.' -> goLeft(contraption, y, x);
      case '/' -> goDown(contraption, y, x);
      case '\\' -> goUp(contraption, y, x);
      case '-' -> goLeft(contraption, y, x);
      case '|' -> {
        goUp(contraption, y, x);
        goDown(contraption, y, x);
      }
    }
  }

  private void goUp(char[][][] contraption, int y, int x) {
    if (y - 1 < 0 || isCached(y, x, Direction.Up)) {
      return;
    }

    cacheLocationAndDirection(y, x, Direction.Up);

    y--;
    visitAndMoveUp(contraption, y, x);
  }

  private void visitAndMoveUp(char[][][] contraption, int y, int x) {
    char value = markVisited(contraption[y], x);
    switch (value) {
      case '.' -> goUp(contraption, y, x);
      case '/' -> goRight(contraption, y, x);
      case '\\' -> goLeft(contraption, y, x);
      case '|' -> goUp(contraption, y, x);
      case '-' -> {
        goLeft(contraption, y, x);
        goRight(contraption, y, x);
      }
    }
  }

  private static void cacheLocationAndDirection(int y, int x, Direction direction) {
    cache.add(new Tuple<>(y, x, direction));
  }

  private boolean isCached(int y, int x, Direction direction) {
    return cache.contains(new Tuple<>(y, x, direction));
  }

  private void cleanCache() {
    cache.clear();
  }

  private static char markVisited(char[][] contraption, int x) {
    char value = contraption[x][0];
    contraption[x][1] = '#';
    return value;
  }

  private char[][][] populateContraption() throws IOException {
    int ySize = 0;
    char[][][] contraption = null;

    InputStream is = this.getClass().getResourceAsStream(INPUT_FILE);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      ySize = (int) br.lines().count();
    }

    int counter = 0;
    is = this.getClass().getResourceAsStream(INPUT_FILE);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (contraption == null) {
          contraption = new char[ySize][line.length()][2];
        }
        for (int i = 0; i < line.length(); i++) {
          contraption[counter][i][0] = line.charAt(i);
          contraption[counter][i][1] = ' ';
        }
        counter++;
      }
    }

    return contraption;
  }

  private enum Direction {
    Up,
    Down,
    Right,
    Left
  }
}
