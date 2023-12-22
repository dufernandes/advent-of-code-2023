package advent.of.code.year_2023.day11;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class CosmicExpansion {

  private static final String INPUT_FILE = "/year2023/day_11_input.txt";
  public static final char GALIAXY = '#';
  public static final char SPACE = '.';

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new CosmicExpansion().sumOfLengths());
      //log.info("The result of part two is: {}", new CosmicExpansion().getNumberOfEnclosedTiles());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  private long sumOfLengths() throws IOException {
    char[][] imageTransition = createImageWithExpandedRows();
    print2D(imageTransition);
    System.out.println();System.out.println();

    char[][] imageTransitionRevertedExpanded = expandRevertedImage(imageTransition);

    char[][] image = reverseArray(imageTransitionRevertedExpanded[0].length, imageTransitionRevertedExpanded.length, imageTransitionRevertedExpanded);
    print2D(image);
    System.out.println();

    Set<Galaxy> galaxies = new HashSet<>();

    int galaxyName = 1;
    for (int y = 0; y < image.length; y++) {
      // add to the galaxies list, and update the image map with their name for easily debugging
      for (int x = 0; x < image[y].length; x++) {
        char element = image[y][x];
        if (isGalaxy(element)) {
          galaxies.add(new Galaxy(y, x, galaxyName++));
          image[y][x] = (char) (galaxyName - 1);
        }
      }
    }

    print2D(image);

    int sumOfLengths = 0;
    return sumOfLengths;
  }

  private static char[][] expandRevertedImage(char[][] imageTransition) {
    char[][] imageTransitionReverted = reverseArray(imageTransition[0].length, imageTransition.length, imageTransition);
    print2D(imageTransitionReverted);
    System.out.println();
    System.out.println();

    int yRevertedSize = imageTransitionReverted.length + (int) Arrays.stream(imageTransitionReverted).map(String::valueOf).filter(CosmicExpansion::containsNoGalaxies).count();

    char[][] imageTransitionRevertedExpanded = new char[yRevertedSize][imageTransitionReverted[0].length];
    int counter = 0;
    for (char[] galaxyElements : imageTransitionReverted) {

      imageTransitionRevertedExpanded[counter] = galaxyElements;

      String galaxyElementsAsString = String.valueOf(galaxyElements);
      if (containsNoGalaxies(galaxyElementsAsString)) {
        imageTransitionRevertedExpanded[++counter] = galaxyElementsAsString.toCharArray();
        counter++;
        continue;
      }

      counter++;
    }
    return imageTransitionRevertedExpanded;
  }

  private static char[][] createImageWithExpandedRows() throws IOException {
    char[][] imageTransition = null;
    int ySize, xSize = 0;

    InputStream is = CosmicExpansion.class.getResourceAsStream(INPUT_FILE);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      ySize = (int) br.lines().count();
    }

    is = CosmicExpansion.class.getResourceAsStream(INPUT_FILE);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      ySize += (int) br.lines().filter(CosmicExpansion::containsNoGalaxies).count();
    }

    is = CosmicExpansion.class.getResourceAsStream(INPUT_FILE);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      int counter = 0;
      while ((line = br.readLine()) != null) {

        // initialize image raw matrix
        if (imageTransition == null) {
          xSize = line.length();
          imageTransition = new char[ySize][xSize];
        }

        imageTransition[counter] = line.toCharArray();

        // add extra line in case there are no galaxies
        if (containsNoGalaxies(line)) {
          imageTransition[++counter] = line.toCharArray();
          counter++;
          continue;
        }

        counter++;
      }
    }
    return imageTransition;
  }

  private static char[][] reverseArray(int xSize, int ySize, char[][] imageTransition) {
    char[][] imageTransitionReverted = new char[xSize][ySize];
    for (int y = 0; y < ySize; y++) {
      for (int x = 0; x < xSize; x++) {
        imageTransitionReverted[x][y] = imageTransition[y][x];
      }
    }
    return imageTransitionReverted;
  }

  private static boolean isGalaxy(char element) {
    return element == GALIAXY;
  }

  private static boolean containsNoGalaxies(String line) {
    return line.chars().allMatch(c -> c == SPACE);
  }

  private static void print2D(char[][] mat) {
    for (char[] row : mat) {
      for (char element : row) {
        String print = element == SPACE ? "." : String.valueOf((int) element);
        System.out.print(print + " ");
      }
      System.out.println();
    }
  }

  private record Galaxy(int yCoordinate, int xCoordinate, int name) {
  }
}
