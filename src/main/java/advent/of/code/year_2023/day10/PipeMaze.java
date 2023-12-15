package advent.of.code.year_2023.day10;

import advent.of.code.year_2023.day05.SeedFertilizer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
public class PipeMaze {

  private static final String INPUT_FILE = "/year2023/day_10_input.txt";

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new PipeMaze().findFarthestPoint());
      log.info("The result of part two is: {}", new PipeMaze().findFarthestPoint());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  public long findFarthestPoint() throws IOException {
    char[][] area = null;
    long numberOfVertexes = 0;
    long origin = 0;
    InputStream is = SeedFertilizer.class.getResourceAsStream(INPUT_FILE);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      int counter = 0;
      while ((line = br.readLine()) != null) {
        if (area == null) {
          area = new char[line.length()][line.length()];
        }
        for (int i = 0; i < line.length(); i++) {
          char element = line.charAt(i);
          if (element != '.') {
            numberOfVertexes++;
          }
          area[counter][i] = element;
        }
        counter++;
      }
    }



    return 0;
  }
}
