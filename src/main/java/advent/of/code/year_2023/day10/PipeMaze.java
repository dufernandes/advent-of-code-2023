package advent.of.code.year_2023.day10;

import advent.of.code.year_2023.day05.SeedFertilizer;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
public class PipeMaze {

  private static final String INPUT_FILE = "/year2023/day_10_input.txt";
  private static final char DOT = '.';
  private static final char S = 'S';
  private static final int VERTEX_NAME_INDEX = 1;
  private static final int PIPE_TYPE_INDEX = 0;
  private static final int INVALID_NUMBER = Character.MAX_VALUE;

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new PipeMaze().findFarthestPoint());
      log.info("The result of part two is: {}", new PipeMaze().getNumberOfEnclosedTiles());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  public long getNumberOfEnclosedTiles() throws IOException {
    Area areaAndVertexCount = mapInputIntoAreaArray();
    char[][][] area = areaAndVertexCount.area();

    Graph graph = new Graph(areaAndVertexCount.numberOfVertices);
    int sVertexName = populateGraph(area, graph);

    DepthFirstPaths depthFirstPaths = new DepthFirstPaths(graph, sVertexName);

    int enclosedTiles = 0;
    int yLength = area.length;
    int xLength = area[0].length;
    for (int i = 0; i < yLength; i++) {
      for (int j = 0; j < xLength; j++) {
        if (depthFirstPaths.hasPathTo(getAreaElementVertexName(i, j, area))) {
          continue;
        }

        int numberOfVertexesInRow = 0;
        for (int x = j + 1; x < xLength; x++) {
          char element = getAreaElementPipeType(i, x, area);
          if (element == DOT) {
            continue;
          }

          int vertex = getAreaElementVertexName(i, x, area);
          if (element == S) {
            int vertexA = getAreaElementVertexNameOnTopIfExists(area, i, x);
            int vertexB = getAreaElementVertexNameOnTheRightIfExists(area, i, x, xLength);
            if (isValidNumber(vertexA)
                    && StreamSupport.stream(graph.adj(sVertexName).spliterator(), false)
                      .anyMatch(v -> v == vertexA)
                    && isValidNumber(vertexB) && StreamSupport
                      .stream(graph.adj(sVertexName).spliterator(), false)
                      .anyMatch(v -> v == vertexB)) {
              element = 'L';
            }

            int vertexA2 = getAreaElementVertexNameOnTopIfExists(area, i, x);
            int vertexB2 = getAreaElementVertexNameOnTheLeftIfExists(area, i, x);
            if (isValidNumber(vertexA2)
                    && StreamSupport.stream(graph.adj(sVertexName).spliterator(), false)
                      .anyMatch(v -> v == vertexA2)
                    && isValidNumber(vertexB2)
                    && StreamSupport
                      .stream(graph.adj(sVertexName).spliterator(), false)
                      .anyMatch(v -> v == vertexB2)) {
              element = 'J';
            }

            int vertexA3 = getAreaElementVertexNameOnTheRightIfExists(area, i, x, xLength);
            int vertexB3 = getAreaElementVertexNameOnTheLeftIfExists(area, i, x);
            if (isValidNumber(vertexA3)
                    && StreamSupport.stream(graph.adj(sVertexName).spliterator(), false)
                    .anyMatch(v -> v == vertexA3)
                    && isValidNumber(vertexB3)
                    && StreamSupport
                    .stream(graph.adj(sVertexName).spliterator(), false)
                    .anyMatch(v -> v == vertexB3)) {
              element = '-';
            }
          }

          if (depthFirstPaths.hasPathTo(vertex)) {
            if (element != '-'
                    && element != 'J'
                    && element != 'L') {
              numberOfVertexesInRow++;
            }
          }
        }
        if (numberOfVertexesInRow % 2 != 0) {
          enclosedTiles++;
          // log.info("added element position {}, {}", i, j);
        }
      }
    }

    return enclosedTiles;
  }

  public long findFarthestPoint() throws IOException {
    Area areaAndVertexCount = mapInputIntoAreaArray();
    char[][][] area = areaAndVertexCount.area();

    Graph graph = new Graph(areaAndVertexCount.numberOfVertices);
    int sVertexName = populateGraph(area, graph);

    DepthFirstPaths depthFirstPaths = new DepthFirstPaths(graph, sVertexName);

    return depthFirstPaths.cycleSize / 2;
  }

  private static int populateGraph(char[][][] area, Graph graph) {
    int sVertexName = 0;
    int yLength = area.length;
    int xLength = area[0].length;
    for (int i = 0; i < yLength; i++) {
      for (int j = 0; j < xLength; j++) {
        char element = getAreaElementPipeType(i, j, area);
        if (element != DOT) {
          int vertex = getAreaElementVertexName(i, j, area);
          int[] adjacents = switch (element) {
            case '|' -> { // is a vertical pipe connecting north and south
              int adjA = getAreaElementVertexNameOnTopIfExists(area, i, j);
              int adjB = getAreaElementVertexNameOnTheBottomIfExists(area, i, yLength, j);
              yield new int[]{adjA, adjB};
            }
            case '-' -> { // is a horizontal pipe connecting east and west.
              int adjA = getAreaElementVertexNameOnTheLeftIfExists(area, i, j);
              int adjB = getAreaElementVertexNameOnTheRightIfExists(area, i, j, xLength);
              yield new int[]{adjA, adjB};
            }
            case 'L' -> { // is a 90-degree bend connecting north and east.
              int adjA = getAreaElementVertexNameOnTopIfExists(area, i, j);
              int adjB = getAreaElementVertexNameOnTheRightIfExists(area, i, j, xLength);
              yield new int[]{adjA, adjB};
            }
            case 'J' -> { // is a 90-degree bend connecting north and west.
              int adjA = getAreaElementVertexNameOnTopIfExists(area, i, j);
              int adjB = getAreaElementVertexNameOnTheLeftIfExists(area, i, j);
              yield new int[]{adjA, adjB};
            }
            case '7' -> { // is a 90-degree bend connecting south and west.
              int adjA = getAreaElementVertexNameOnTheBottomIfExists(area, i, yLength, j);
              int adjB = getAreaElementVertexNameOnTheLeftIfExists(area, i, j);
              yield new int[]{adjA, adjB};
            }
            case 'F' -> { // is a 90-degree bend connecting south and east.
              int adjA = getAreaElementVertexNameOnTheBottomIfExists(area, i, yLength, j);
              int adjB = getAreaElementVertexNameOnTheRightIfExists(area, i, j, xLength);
              yield new int[]{adjA, adjB};
            }
            case S -> { // is the starting position of the animal; there is a pipe on this
              sVertexName = getAreaElementVertexName(i, j, area);
              yield inferSAdjacentElements(area, i, j, yLength, xLength);
            }
            case DOT -> // is ground; there is no pipe in this tile.
                    returnInvalidVertexes();
            default -> {
              throw new RuntimeException("invalid graph element: " + element);
            }
          };

          if (isValidNumber(adjacents[0])) {
            graph.addEdge(vertex, adjacents[0]);
          }
          if (isValidNumber(adjacents[1])) {
            graph.addEdge(vertex, adjacents[1]);
          }
        }
      }
    }
    return sVertexName;
  }

  private static int[] inferSAdjacentElements(char[][][] area, int i, int j, int yLength, int xLength) {
    int adjA = INVALID_NUMBER, adjB = INVALID_NUMBER;

    int vertexName;
    char pipe;
    if (j + 1 < xLength) {
      pipe = getAreaElementPipeType(i, j + 1, area);
      if (pipe == '-'
              || pipe == 'J'
              || pipe == '7') {
        adjA = getAreaElementVertexName(i, j + 1, area);
      }
    }

    if (j > 0) {
      pipe = getAreaElementPipeType(i, j - 1, area);
      if (pipe == '-'
              || pipe == 'L'
              || pipe == 'F') {

        vertexName = getAreaElementVertexName(i, j - 1, area);
        if (!isValidNumber(adjA)) {
          adjA = vertexName;
        } else {
          adjB = vertexName;
        }
      }
    }

    if (i > 0) {
      pipe = getAreaElementPipeType(i - 1, j, area);
      if (pipe == '|'
              || pipe == '7'
              || pipe == 'F') {

        vertexName = getAreaElementVertexName(i - 1, j, area);
        if (!isValidNumber(adjA)) {
          adjA = vertexName;
        } else {
          adjB = vertexName;
        }
      }
    }

    if (i + 1 < yLength) {
      pipe = getAreaElementPipeType(i + 1, j, area);
      if (pipe == '|'
              || pipe == 'L'
              || pipe == 'J') {

        vertexName = getAreaElementVertexName(i + 1, j, area);
        if (!isValidNumber(adjA)) {
          adjA = vertexName;
        } else {
          adjB = vertexName;
        }
      }
    }

    return new int[]{adjA, adjB};
  }

  private static void print2D(char[][][] mat)
  {
    for (char[][] rows : mat) {
      StringBuilder builder = new StringBuilder();
      for (char[] innerRow : rows) {

        builder.append("(").append(innerRow[0]).append(" ").append((int) innerRow[1]).append(")");
      }
      System.out.println(builder);
    }
  }

  private static Area mapInputIntoAreaArray() throws IOException {
    char[][][] area = null;
    int ySize, xSize;
    int numberOfVertexes = 0;

    InputStream is = SeedFertilizer.class.getResourceAsStream(INPUT_FILE);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      ySize = (int) br.lines().count();
    }

    is = SeedFertilizer.class.getResourceAsStream(INPUT_FILE);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      int counter = 0;
      while ((line = br.readLine()) != null) {
        xSize = line.length();
        if (area == null) {
          area = new char[ySize][xSize][2];
        }
        for (int i = 0; i < xSize; i++) {
          char element = line.charAt(i);
          if (element != DOT) {
            setAreaVertexName(counter, i, area, (char) numberOfVertexes++);
          } else {
            setAreaVertexName(counter, i, area, (char) INVALID_NUMBER);
          }
          setAreaPipeType(counter, i, area, element);
        }
        counter++;
      }
    }
    return new Area(area, numberOfVertexes);
  }

  private static boolean isValidNumber(int number) {
    return number != INVALID_NUMBER;
  }

  private static int[] returnInvalidVertexes() {
    return new int[]{INVALID_NUMBER, INVALID_NUMBER};
  }

  private static int getAreaElementVertexNameOnTheRightIfExists(char[][][] area, int i, int j, int xLength) {
    return j + 1 < xLength ? getAreaElementVertexNameOnTheRight(area, i, j) : INVALID_NUMBER;
  }

  private static int getAreaElementVertexNameOnTheLeftIfExists(char[][][] area, int i, int j) {
    return j - 1 >= 0 ? getAreaElementVertexNameOnTheLeft(area, i, j) : INVALID_NUMBER;
  }

  private static int getAreaElementVertexNameOnTheBottomIfExists(char[][][] area, int i, int yLength, int j) {
    return i + 1 < yLength ? getAreaElementVertexNameOnTheBottom(area, i, j) : INVALID_NUMBER;
  }

  private static int getAreaElementVertexNameOnTopIfExists(char[][][] area, int i, int j) {
    return i - 1 >= 0 ? getAreaElementVertexNameOnTop(area, i, j) : INVALID_NUMBER;
  }

  private static int getAreaElementVertexNameOnTheRight(char[][][] area, int i, int j) {
    return getAreaElementVertexName(i, j + 1, area);
  }

  private static int getAreaElementVertexNameOnTheLeft(char[][][] area, int i, int j) {
    return getAreaElementVertexName(i, j - 1, area);
  }

  private static int getAreaElementVertexNameOnTheBottom(char[][][] area, int i, int j) {
    return getAreaElementVertexName(i + 1, j, area);
  }

  private static int getAreaElementVertexNameOnTop(char[][][] area, int i, int j) {
    return getAreaElementVertexName(i - 1, j, area);
  }

  private static int getAreaElementVertexName(int y, int x, char[][][] area) {
    return area[y][x][VERTEX_NAME_INDEX];
  }

  private static void setAreaVertexName(int y, int x, char[][][] area, char vertexName) {
    area[y][x][VERTEX_NAME_INDEX] = vertexName;
  }

  private static char getAreaElementPipeType(int y, int x, char[][][] area) {
    return area[y][x][PIPE_TYPE_INDEX];
  }

  private static void setAreaPipeType(int y, int x, char[][][] area, char pipeType) {
    area[y][x][PIPE_TYPE_INDEX] = pipeType;
  }

  @ToString
  private class DepthFirstPaths {
    private final boolean[] marked;
    private final int[] edgeTo;
    private final int originVertex;
    private int cycleSize = 1;

    public DepthFirstPaths(Graph G, int originVertex) {
      marked = new boolean[G.numberOfVertexes];
      edgeTo = new int[G.numberOfVertexes];
      this.originVertex = originVertex;
      dfsNonRecursive(G, originVertex);
      for (boolean isMarked : marked) {
        if (isMarked) {
          cycleSize++;
        }
      }
    }

    public boolean hasPathTo(int v) {
      if (v < 0 || v >= marked.length) {
        return false;
      }
      return marked[v];
    }

    private void dfsNonRecursive(Graph G, int v) {
      Stack<Integer> stack = new Stack<>();
      stack.push(v);
      while (!stack.empty()) {
        int current = stack.pop();
        if (!marked[current]) {
          marked[current] = true;
          for (int adjacent : G.adj(current)) {
            if (!marked[adjacent]) {
              edgeTo[current] = adjacent;
              stack.push(adjacent);
            }
          }
        }
      }
    }
  }

  @ToString
  private class Graph {
    @Getter
    private final int numberOfVertexes;
    private final Set<Integer>[] adjacent;

    public Graph(int numberOfVertexes) {
      this.numberOfVertexes = numberOfVertexes;
      adjacent = new Set[numberOfVertexes];
      for (int i = 0; i < numberOfVertexes; i++) {
        adjacent[i] = new HashSet<>();
      }
    }

    public void addEdge(int vertexV, int vertexW) {
      adjacent[vertexV].add(vertexW);
    }

    public Iterable<Integer> adj(int vertex) {
      return adjacent[vertex];
    }
  }

  private record Area(char[][][] area, int numberOfVertices) {}
}
