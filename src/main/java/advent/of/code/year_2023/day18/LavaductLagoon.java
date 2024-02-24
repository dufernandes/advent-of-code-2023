package advent.of.code.year_2023.day18;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LavaductLagoon {

  private static final String INPUT_FILE = "/year2023/day_18_input.txt";

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new LavaductLagoon().calculateArea());
      //log.info("The result for part two is: {}", new LavaductLagoon().calculateArea());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  private long calculateArea() throws IOException {
    long area = 0;

    DigPlan digPlan = readDigPlan();
    Coordinate[] coordinates = digPlan.coordinates();

    for (int i = 0; i < coordinates.length; i++) {
      Coordinate coord = coordinates[i];
      Coordinate nextCoord;
      if (i == coordinates.length - 1) {
        nextCoord = coordinates[0];
      } else {
        nextCoord = coordinates[i + 1];
      }
      area += (long) coord.x * nextCoord.y - (long) nextCoord.x * coord.y;
    }

    return (Math.abs(area) / 2) + (digPlan.borderSize / 2) + 1;
  }

  private DigPlan readDigPlan() throws IOException {
    List<Coordinate> coordinates = new ArrayList<>();
    long borderSize = 0;

    InputStream is = this.getClass().getResourceAsStream(INPUT_FILE);

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      int x = 0, y = 0;
      while ((line = br.readLine()) != null) {
        String[] coordinateValues = line.split(" ");
        char direction = coordinateValues[0].charAt(0);
        int steps = Integer.parseInt(coordinateValues[1]);
        String color = coordinateValues[2].substring(1, coordinateValues[2].length() - 1);
        switch (direction) {
          case 'U' -> y += steps;
          case 'D' -> y -= steps;
          case 'R' -> x += steps;
          case 'L' -> x -= steps;
          default -> throw new RuntimeException("Invalid direction: " + direction);
        }
        borderSize += steps;
        coordinates.add(new Coordinate(y, x, color));
      }
    }

    return new DigPlan(borderSize, coordinates.toArray(new Coordinate[0]));
  }

  private record Coordinate(int y, int x, String color) {}
  private record DigPlan(long borderSize, Coordinate[] coordinates) {}
}
