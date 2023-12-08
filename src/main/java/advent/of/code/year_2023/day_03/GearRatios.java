package advent.of.code.year_2023.day_03;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Stack;

@Slf4j
public class GearRatios {

  private static final String INPUT_FILE = "/year2023/day_03_input.txt";

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new GearRatios().getPowerSum());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  public long getGearRatioSum() throws IOException {
    long sum = 0;

    return sum;
  }

  public long getPowerSum() throws IOException {

    long sum = 0;
    SchematicsPlan schematicsPlan = createSchematics();

    String[][] schematics = schematicsPlan.schematics();
    for (int y = 0; y < schematicsPlan.ySize(); y++) {

      Stack<Integer> numbers = new Stack<>();
      boolean hasAdjecentSymbol = false;
      for (int x = 0; x < schematicsPlan.xSize(); x++) {

        String currentElement = schematics[y][x];
        if (isDigit(schematics[y][x])) {

          numbers.push(Integer.parseInt(currentElement));
          if (hasAdjecentSymbol || hasAdjacentSymbol(y, x, schematicsPlan.ySize(), schematicsPlan.xSize(), schematics)) {
            hasAdjecentSymbol = true;
          }

          if (!hasElement(y, x + 1, schematicsPlan.ySize(), schematicsPlan.ySize()) ||
              !isDigit(schematics[y][x + 1])) {
            if (hasAdjecentSymbol) {
              int currentNumber = getCurrentNumber(numbers);
              sum += currentNumber;
              log.info("added number: {}, sum: {}", currentNumber, sum);
              hasAdjecentSymbol = false;
            } else if (!numbers.empty()) {
              int currentNumber = getCurrentNumber(numbers);
              log.info("NOT added number: {}", currentNumber);
              numbers.clear();
            }
          }
        }
      }
    }

    return sum;
  }

  private static int getCurrentNumber(Stack<Integer> numbers) {
    int decimal = 1;
    int currentNumber = 0;
    while (!numbers.isEmpty()) {
      currentNumber += numbers.pop() * decimal;
      decimal *= 10;
    }
    return currentNumber;
  }

  private boolean hasAdjacentSymbol(int y, int x, int yLength, int xLength, String[][] schematics) {
    return hasSymbolOnTop(y, x, yLength, xLength, schematics)
        || hasSymbolOnDiagonalTopLeft(y, x, yLength, xLength, schematics)
        || hasSymbolOnTheLeft(y, x, yLength, xLength, schematics)
        || hasSymbolOnDiagonalBottomLeft(y, x, yLength, xLength, schematics)
        || hasSymbolOnBottom(y, x, yLength, xLength, schematics)
        || hasSymbolOnDiagonalBottomRight(y, x, yLength, xLength, schematics)
        || hasSymbolOnTheRight(y, x, yLength, xLength, schematics)
        || hasSymbolOnDiagonalTopRight(y, x, yLength, xLength, schematics);
  }

  private boolean hasSymbolOnDiagonalTopRight(int y, int x, int yLength, int xLength, String[][] schematics) {
    return hasElement(y - 1, x + 1, yLength, xLength) && isSymbol(schematics[y - 1][x + 1]);
  }

  private boolean hasSymbolOnTheRight(int y, int x, int yLength, int xLength, String[][] schematics) {
    return hasElement(y, x + 1, yLength, xLength) && isSymbol(schematics[y][x + 1]);
  }

  private boolean hasSymbolOnDiagonalBottomRight(int y, int x, int yLength, int xLength, String[][] schematics) {
    return hasElement(y + 1, x + 1, yLength, xLength) && isSymbol(schematics[y + 1][x + 1]);
  }

  private boolean hasSymbolOnBottom(int y, int x, int yLength, int xLength, String[][] schematics) {
    return hasElement(y + 1, x, yLength, xLength) && isSymbol(schematics[y + 1][x]);
  }

  private boolean hasSymbolOnDiagonalBottomLeft(int y, int x, int yLength, int xLength, String[][] schematics) {
    return hasElement(y + 1, x - 1, yLength, xLength) && isSymbol(schematics[y + 1][x - 1]);
  }

  private boolean hasSymbolOnTheLeft(int y, int x, int yLength, int xLength, String[][] schematics) {
    return hasElement(y, x - 1, yLength, xLength) && isSymbol(schematics[y][x - 1]);
  }

  private boolean hasSymbolOnDiagonalTopLeft(int y, int x, int yLength, int xLength, String[][] schematics) {
    return hasElement(y - 1, x - 1, yLength, xLength) && isSymbol(schematics[y - 1][x - 1]);
  }

  private boolean hasSymbolOnTop(int y, int x, int yLength, int xLength, String[][] schematics) {
    return hasElement(y - 1, x, yLength, xLength) && isSymbol(schematics[y - 1][x]);
  }

  private static boolean hasElement(int y, int x, int yLength, int xLength) {
    return y >= 0 && y < yLength && x >= 0 && x < yLength;
  }

  private static SchematicsPlan createSchematics() throws IOException {
    InputStream is = GearRatios.class.getResourceAsStream(INPUT_FILE);

    String[][] schematics = null;
    int xSize = 0, ySize;
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      ySize = (int) br.lines().count();
    }

    is = GearRatios.class.getResourceAsStream(INPUT_FILE);
    int y = 0;
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (xSize == 0) {
          xSize = line.length();
        }

        if (schematics == null) {
          schematics = new String[xSize][ySize];
        }

        schematics[y] = line.split("");
        y++;
      }
    }
    return new SchematicsPlan(schematics, xSize, ySize);
  }

  private boolean isSymbol(String element) {
    return !isDigit(element) && !".".equals(element);
  }

  private boolean isDigit(String element) {
    try {
      Integer.parseInt(element);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  private record SchematicsPlan(String[][] schematics, int xSize, int ySize) {
  }
}
