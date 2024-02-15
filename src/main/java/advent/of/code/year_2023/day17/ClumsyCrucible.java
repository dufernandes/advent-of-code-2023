package advent.of.code.year_2023.day17;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
public class ClumsyCrucible {

  private static final String INPUT_FILE = "/year2023/day_17_input.txt";

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new ClumsyCrucible().calculateMinimumHeatLoss());
      log.info("The result for part two is: {}", new ClumsyCrucible().calculateMinimumHeatLossPart2());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  protected long calculateMinimumHeatLossPart2() throws IOException {
    long minPath = 0;

    int[][] map = populateMap();

    return calculatePathWithTheLeastHeatLossPart2(map, minPath);
  }

  protected long calculateMinimumHeatLoss() throws IOException {
    long minPath = 0;

    int[][] map = populateMap();

    return calculatePathWithTheLeastHeatLoss(map, minPath);
  }

  private static long calculatePathWithTheLeastHeatLossPart2(int[][] map, long minPath) {
    Set<Visited> visited = new HashSet<>();
    PriorityQueue<State> states = new PriorityQueue<>(createHeatLossComparator());

    states.add(new State(0, 0, 0, 0, 0, 0));

    while (!states.isEmpty()) {

      State state = states.poll();

      if (state.row == map.length - 1 && state.column == map[0].length - 1) {
        minPath = state.heatLoss;
        break;
      }

      if (visited.contains(state.toVisited())) {
        continue;
      }

      visited.add(state.toVisited());

      if (state.steps < 10 && (state.dirRow != 0 || state.dirColumn != 0)) {
        int nextRow = state.row + state.dirRow;
        int nextColumn = state.column + state.dirColumn;

        if (nextRow >= 0 && nextRow < map.length && nextColumn >= 0 && nextColumn < map[0].length) {
          states.add(new State(state.heatLoss + map[nextRow][nextColumn], nextRow, nextColumn, state.dirRow, state.dirColumn, state.steps + 1));
        }
      }

      if (state.steps >= 4 || (state.dirRow == 0 && state.dirColumn == 0)) {
        Set<Direction> allDirections = Set.of(new Direction(0, 1), new Direction(1, 0), new Direction(0, -1), new Direction(-1, 0));
        for (Direction direction : allDirections) {
          if (!new Direction(state.dirRow, state.dirColumn).equals(direction) && !new Direction(state.dirRow * (-1), state.dirColumn * (-1)).equals(direction)) {
            int nextRow = state.row + direction.row;
            int nextColumn = state.column + direction.column;

            if (nextRow >= 0 && nextRow < map.length && nextColumn >= 0 && nextColumn < map[0].length) {
              states.add(new State(state.heatLoss + map[nextRow][nextColumn], nextRow, nextColumn, direction.row, direction.column, 1));
            }
          }
        }
      }
    }

    return minPath;
  }

  private static long calculatePathWithTheLeastHeatLoss(int[][] map, long minPath) {
    Set<Visited> visited = new HashSet<>();
    PriorityQueue<State> states = new PriorityQueue<>(createHeatLossComparator());

    states.add(new State(0, 0, 0, 0, 0, 0));

    while (!states.isEmpty()) {

      State state = states.poll();

      if (state.row == map.length - 1 && state.column == map[0].length - 1) {
        minPath = state.heatLoss;
        break;
      }

      if (visited.contains(state.toVisited())) {
        continue;
      }

      visited.add(state.toVisited());

      if (state.steps < 3 && (state.dirRow != 0 || state.dirColumn != 0)) {
        int nextRow = state.row + state.dirRow;
        int nextColumn = state.column + state.dirColumn;

        if (nextRow >= 0 && nextRow < map.length && nextColumn >= 0 && nextColumn < map[0].length) {
          states.add(new State(state.heatLoss + map[nextRow][nextColumn], nextRow, nextColumn, state.dirRow, state.dirColumn, state.steps + 1));
        }
      }

      Set<Direction> allDirections = Set.of(new Direction(0, 1), new Direction(1, 0), new Direction(0, -1), new Direction(-1, 0));
      for (Direction direction : allDirections) {
        if (!new Direction(state.dirRow, state.dirColumn).equals(direction) && !new Direction(state.dirRow * (-1), state.dirColumn * (-1)).equals(direction)) {
          int nextRow = state.row + direction.row;
          int nextColumn = state.column + direction.column;

          if (nextRow >= 0 && nextRow < map.length && nextColumn >= 0 && nextColumn < map[0].length) {
            states.add(new State(state.heatLoss + map[nextRow][nextColumn], nextRow, nextColumn, direction.row, direction.column, 1));
          }
        }
      }
    }

    return minPath;
  }

  private static Comparator<State> createHeatLossComparator() {
    return (o1, o2) -> {

      if (o1.heatLoss == o2.heatLoss) {
        return 0;
      }

      return o1.heatLoss < o2.heatLoss ? -1 : 1;
    };
  }

  private int[][] populateMap() throws IOException {
    int ySize = 0;
    int[][] map = null;

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
          map = new int[ySize][line.length()];
        }
        map[counter] = Arrays.stream(line.split("")).mapToInt(Integer::valueOf).toArray();
        counter++;
      }
    }

    return map;
  }

  private record Direction(int row, int column) {
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Direction direction = (Direction) o;
      return row == direction.row && column == direction.column;
    }

    @Override
    public int hashCode() {
      return Objects.hash(row, column);
    }
  }

  private record State(int heatLoss, int row, int column, int dirRow, int dirColumn, int steps) {
    Visited toVisited() {
      return new Visited(row, column, dirRow, dirColumn, steps);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      State state = (State) o;
      return heatLoss == state.heatLoss && row == state.row && column == state.column && dirRow == state.dirRow && dirColumn == state.dirColumn && steps == state.steps;
    }

    @Override
    public int hashCode() {
      return Objects.hash(heatLoss, row, column, dirRow, dirColumn, steps);
    }
  }

  private record Visited(int row, int column, int dirRow, int dirColumn, int steps) {
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Visited visited = (Visited) o;
      return row == visited.row && column == visited.column && dirRow == visited.dirRow && dirColumn == visited.dirColumn && steps == visited.steps;
    }

    @Override
    public int hashCode() {
      return Objects.hash(row, column, dirRow, dirColumn, steps);
    }
  }
}
