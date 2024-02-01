package advent.of.code.year_2023.day17;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
public class ClumsyCrucible {

  private static final String INPUT_FILE = "/year2023/day_17_input.txt";

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new ClumsyCrucible().calculateMinimumHeatLoss());
      //log.info("The result for part two is: {}", new ClumsyCrucible().sumEnergizedBeamsOfBestScenario());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  private long calculateMinimumHeatLoss() throws IOException {
    long sum = 0;

    char[][] map = populateMap();

    

    return sum;
  }

  private char[][] populateMap() throws IOException {
    int ySize = 0;
    char[][] map = null;

    InputStream is = this.getClass().getResourceAsStream(INPUT_FILE);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      ySize = (int) br.lines().count();
    }

    int counter = 0;
    is = this.getClass().getResourceAsStream(INPUT_FILE);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        if (map == null) {
          map = new char[ySize][line.length()];
        }
        map[counter] = line.toCharArray();
        counter++;
      }
    }

    return map;
  }
}
