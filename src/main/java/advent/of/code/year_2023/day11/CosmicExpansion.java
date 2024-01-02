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
  public static final char GALAXY = '#';
  public static final char SPACE = '.';

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new CosmicExpansion().sumOfLengths(2));
      log.info("The result for part two is: {}", new CosmicExpansion().sumOfLengths(100));
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  private long sumOfLengths(int expansionSize) throws IOException {
    char[][] imageTransition = createImageWithExpandedRows(expansionSize);
    //print2D(imageTransition);
    System.out.println();System.out.println();

    char[][] imageTransitionRevertedExpanded = expandRevertedImage(imageTransition, expansionSize);

    char[][] image = reverseArray(imageTransitionRevertedExpanded[0].length, imageTransitionRevertedExpanded.length, imageTransitionRevertedExpanded);
    //print2D(image);
    System.out.println();

    Set<Galaxy> galaxies = createGalaxyListAndAssignGalaxyNamesToImage(image);

    //print2D(image);

    return calculateSumOfLengths(galaxies, image);
  }

  private static int calculateSumOfLengths(Set<Galaxy> galaxies, char[][] image) {
    Galaxy[] galaxiesArray = galaxies.toArray(new Galaxy[0]);
    int sumOfLengths = 0;
    for (int i = 0; i < galaxiesArray.length; i++) {
      for (int j = i + 1; j < galaxiesArray.length; j++) {
        Galaxy origin = galaxiesArray[i];
        Galaxy destiny = galaxiesArray[j];
        int distance = ImageGraph.minDistance(image, origin.yCoordinate(), origin.xCoordinate(), destiny.yCoordinate(), destiny.xCoordinate());
        if (distance == -1) {
          throw new RuntimeException("Problem distance not found. Origin: " + origin + " Destiny: " + destiny);
        }
        sumOfLengths += distance;
      }
    }
    return sumOfLengths;
  }

  private static Set<Galaxy> createGalaxyListAndAssignGalaxyNamesToImage(char[][] image) {
    Set<Galaxy> galaxies = new HashSet<>();

    int galaxyName = 1;
    for (int y = 0; y < image.length; y++) {
      // add to the galaxies list, and update the image map with their name for easy debugging
      for (int x = 0; x < image[y].length; x++) {
        char element = image[y][x];
        if (isGalaxy(element)) {
          galaxies.add(new Galaxy(y, x, galaxyName++));
          image[y][x] = (char) (galaxyName - 1);
        }
      }
    }
    return galaxies;
  }

  private static char[][] expandRevertedImage(char[][] imageTransition, int expansionSize) {
    char[][] imageTransitionReverted = reverseArray(imageTransition[0].length, imageTransition.length, imageTransition);
    //print2D(imageTransitionReverted);
    imageTransition = new char[0][0];
    System.gc();
    System.out.println();
    System.out.println();

    int yRevertedSize = imageTransitionReverted.length + (int) Arrays.stream(imageTransitionReverted).map(String::valueOf).filter(CosmicExpansion::containsNoGalaxies).count() * (expansionSize - 1);

    char[][] imageTransitionRevertedExpanded = new char[yRevertedSize][imageTransitionReverted[0].length];
    int counter = 0;
    for (char[] galaxyElements : imageTransitionReverted) {

      imageTransitionRevertedExpanded[counter] = galaxyElements;

      String galaxyElementsAsString = String.valueOf(galaxyElements);
      if (containsNoGalaxies(galaxyElementsAsString)) {
        for (int i = counter + 1; i <= counter + (expansionSize - 1); i++) {
          imageTransitionRevertedExpanded[i] = galaxyElementsAsString.toCharArray();
        }
        counter += expansionSize;
        continue;
      }

      counter++;
    }
    return imageTransitionRevertedExpanded;
  }

  private static char[][] createImageWithExpandedRows(int expansionSize) throws IOException {
    char[][] imageTransition = null;
    int ySize, xSize = 0;

    InputStream is = CosmicExpansion.class.getResourceAsStream(INPUT_FILE);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      ySize = (int) br.lines().count();
    }

    is = CosmicExpansion.class.getResourceAsStream(INPUT_FILE);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      ySize += (int) br.lines().filter(CosmicExpansion::containsNoGalaxies).count() * (expansionSize - 1);
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
          for (int i = counter + 1; i <= counter + expansionSize - 1; i++) {
            imageTransition[i] = line.toCharArray();
          }
          counter += expansionSize;
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
    return element == GALAXY;
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
