package advent.of.code.year_2023.day11;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CosmicExpansionOptimized {

  private static final String INPUT_FILE = "/year2023/day_11_input.txt";
  public static final char SPACE = '.';

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new CosmicExpansionOptimized().sumOfLengths(2));
      log.info("The result for part two is: {}", new CosmicExpansionOptimized().sumOfLengths(1000000));
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  private long sumOfLengths(int expansionSize) throws IOException {
    ImageGalaxies imageGalaxies = createImageAndGalaxies();

    char[][] image = imageGalaxies.images;
    Map<Character, Galaxy> galaxies = imageGalaxies.galaxies;

    expandYCoordinates(image, galaxies, expansionSize);
    expandXCoordinates(image, galaxies, expansionSize);

    System.out.println("Galaxies");
    System.out.println(galaxies.values());

    return calculateSumOfLengths(galaxies, image);
  }

  private static long calculateSumOfLengths(Map<Character, Galaxy> galaxies, char[][] image) {
    Galaxy[] galaxiesArray = galaxies.values().toArray(new Galaxy[0]);
    long sumOfLengths = 0;
    for (int i = 0; i < galaxiesArray.length; i++) {
      for (int j = i + 1; j < galaxiesArray.length; j++) {
        Galaxy origin = galaxiesArray[i];
        Galaxy destiny = galaxiesArray[j];
        int distance = Math.abs(origin.xCoordinate - destiny.xCoordinate) + Math.abs(origin.yCoordinate - destiny.yCoordinate);
        if (distance == -1) {
          throw new RuntimeException("Problem distance not found. Origin: " + origin + " Destiny: " + destiny);
        }
        sumOfLengths += distance;
      }
    }
    return sumOfLengths;
  }

  private static char[] getColumn(char[][] from, int columnIndex) {
    char[] column = new char[from.length];
    for (int y = 0; y < column.length; y++) {
      column[y] = from[y][columnIndex];
    }
    return column;
  }

  private static void expandXCoordinates(char[][] image, Map<Character, Galaxy> galaxies, int expansionSize) {
    int xActual = 0;
    for (int x = 0; x < image[0].length; x++) {
      if (containsNoGalaxies(String.valueOf(getColumn(image, x)))) {
        xActual = xActual + expansionSize;
        continue;
      }
      for (int y = 0; y < image.length; y++) {
        char galaxyName = image[y][x];
        if (isGalaxy(galaxyName)) {
          Galaxy galaxy = galaxies.remove(galaxyName);
          galaxies.put(galaxyName, new Galaxy(galaxy.yCoordinate(), xActual, galaxyName));
        }
      }
      xActual++;
    }
  }

  private static void expandYCoordinates(char[][] image, Map<Character, Galaxy> galaxies, int expansionSize) {
    int yActual = 0;
    for (int y = 0; y < image.length; y++) {
      if (containsNoGalaxies(String.valueOf(image[y]))) {
        yActual = yActual + expansionSize;
        continue;
      }
      for (int x = 0; x < image[0].length; x++) {
        char galaxyName = image[y][x];
        if (isGalaxy(galaxyName)) {
          Galaxy galaxy = galaxies.remove(galaxyName);
          galaxies.put(galaxyName, new Galaxy(yActual, galaxy.xCoordinate(), galaxyName));
        }
      }
      yActual++;
    }
  }

  private static ImageGalaxies createImageAndGalaxies() throws IOException {
    char[][] image = null;
    int ySize, xSize = 0, galaxyName = 1;
    Map<Character, Galaxy> galaxies = new HashMap<>();
    InputStream is = CosmicExpansionOptimized.class.getResourceAsStream(INPUT_FILE);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      ySize = (int) br.lines().count();
    }

    is = CosmicExpansionOptimized.class.getResourceAsStream(INPUT_FILE);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      int yPosition = 0;
      while ((line = br.readLine()) != null) {

        // initialize image raw matrix
        if (image == null) {
          xSize = line.length();
          image = new char[ySize][xSize];
        }

        for (int x = 0; x < xSize; x++) {
          char element = line.charAt(x);
          if (isGalaxy(element)) {
            if (galaxyName == SPACE) {
              galaxyName++;
            }
            galaxies.put((char) galaxyName, new Galaxy(yPosition, x, galaxyName));
            image[yPosition][x] = (char) (galaxyName);
            galaxyName++;
          } else {
            image[yPosition][x] = element;
          }
        }
        yPosition++;
      }
    }
    return new ImageGalaxies(image, galaxies);
  }

  private static boolean isGalaxy(char element) {
    return element != SPACE;
  }

  private static boolean containsNoGalaxies(String line) {
    return line.chars().allMatch(c -> c == SPACE);
  }

  private static void print2D(char[][] mat) {
    for (char[] row : mat) {
      for (char element : row) {
        String print = element == SPACE ? String.valueOf(SPACE) : String.valueOf((int) element);
        System.out.print(print + " ");
      }
      System.out.println();
    }
  }

  private record Galaxy(int yCoordinate, int xCoordinate, int name) {
  }

  private record ImageGalaxies(char[][] images, Map<Character, Galaxy> galaxies) {
  }
}
