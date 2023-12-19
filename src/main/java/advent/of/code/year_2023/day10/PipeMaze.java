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

@Slf4j
public class PipeMaze {

  private static final String INPUT_FILE = "/year2023/day_10_input.txt";
  private static final char DOT = '.';
  private static final char S = 'S';
  private static final int VERTEX_NAME_INDEX = 1;
  private static final int PIPE_TYPE_INDEX = 0;
  private static final int INVALID_NUMBER = Integer.MIN_VALUE;

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new PipeMaze().findFarthestPoint());
      log.info("The result of part two is: {}", new PipeMaze().findFarthestPoint());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  public long findFarthestPoint() throws IOException {
    Area areaAndVertexCount = mapInputIntoAreaArray();
    char[][][] area = areaAndVertexCount.area();

    log.info("area");
    print2D(area);

    Graph graph = new Graph(areaAndVertexCount.numberOfVertices);
    int sVertexName = populateGraph(area, graph);

    DepthFirstPaths depthFirstPaths = new DepthFirstPaths(graph, sVertexName);

    log.info("graph " + graph);
    log.info("path " + depthFirstPaths);

    return depthFirstPaths.cycleSize / 2;
  }

  private static int populateGraph(char[][][] area, Graph graph) {
    int sVertexName = 0;
    int yLength = area.length;
    int xLength = area[0].length;
    for (int i = 0; i < yLength; i++) {
      for (int j = 0; j < xLength; j++) {
        char element = area[i][j][PIPE_TYPE_INDEX];
        if (element != DOT) {
          int vertex = area[i][j][VERTEX_NAME_INDEX];
          int[] adjacents = switch (element) {
            case '|' -> { // is a vertical pipe connecting north and south
              int adjA = i - 1 >= 0 ? area[i - 1][j][VERTEX_NAME_INDEX] : INVALID_NUMBER;
              int adjB = i + 1 < yLength ? area[i + 1][j][VERTEX_NAME_INDEX] : INVALID_NUMBER;
              yield new int[]{adjA, adjB};
            }
            case '-' -> { // is a horizontal pipe connecting east and west.
              int adjA = j - 1 >= 0 ? area[i][j - 1][VERTEX_NAME_INDEX] : INVALID_NUMBER;
              int adjB = j + 1 < xLength ? area[i][j + 1][VERTEX_NAME_INDEX] : INVALID_NUMBER;
              yield new int[]{adjA, adjB};
            }
            case 'L' -> { // is a 90-degree bend connecting north and east.
              int adjA = i - 1 >= 0 ? area[i - 1][j][VERTEX_NAME_INDEX] : INVALID_NUMBER;
              int adjB = j + 1 < xLength ? area[i][j + 1][VERTEX_NAME_INDEX] : INVALID_NUMBER;
              yield new int[]{adjA, adjB};
            }
            case 'J' -> { // is a 90-degree bend connecting north and west.
              int adjA = i - 1 >= 0 ? area[i - 1][j][VERTEX_NAME_INDEX] : INVALID_NUMBER;
              int adjB = j - 1 >= 0 ? area[i][j - 1][VERTEX_NAME_INDEX] : INVALID_NUMBER;
              yield new int[]{adjA, adjB};
            }
            case '7' -> { // is a 90-degree bend connecting south and west.
              int adjA = i + 1 < yLength ? area[i + 1][j][VERTEX_NAME_INDEX] : INVALID_NUMBER;
              int adjB = j - 1 >= 0 ? area[i][j - 1][VERTEX_NAME_INDEX] : INVALID_NUMBER;
              yield new int[]{adjA, adjB};
            }
            case 'F' -> { // is a 90-degree bend connecting south and east.
              int adjA = i + 1 < yLength ? area[i + 1][j][VERTEX_NAME_INDEX] : INVALID_NUMBER;
              int adjB = j + 1 < xLength ? area[i][j + 1][VERTEX_NAME_INDEX] : INVALID_NUMBER;
              yield new int[]{adjA, adjB};
            }
            case S -> { // is the starting position of the animal; there is a pipe on this
              sVertexName = area[i][j][VERTEX_NAME_INDEX];
              int adjA = INVALID_NUMBER, adjB = INVALID_NUMBER;
              for (int y = i - 1; y <= i + 1; y++) {
                for (int x = j - 1; x <= j + 1; x++) {
                  if ((x < 0 || y < 0)
                          || (y == i - 1 && x == j - 1)
                          || (y == i + 1 && x == j - 1)
                          || (y == i + 1 && x == j + 1)
                          || (y == i - 1 && x == j + 1)) {
                    continue;
                  }
                  if (area[y][x][PIPE_TYPE_INDEX] != DOT && area[y][x][PIPE_TYPE_INDEX] != S) {
                    if (adjA == INVALID_NUMBER) {
                      adjA = area[y][x][VERTEX_NAME_INDEX];
                    } else {
                      adjB = area[y][x][VERTEX_NAME_INDEX];
                    }
                  }
                }
              }

              yield new int[]{adjA, adjB};
            }
            case DOT -> { // is ground; there is no pipe in this tile.
              yield new int[]{INVALID_NUMBER, INVALID_NUMBER};
            }
            default -> {
              throw new RuntimeException("invalid graph element: " + element);
            }
          };

          if (adjacents[0] != INVALID_NUMBER) {
            graph.addEdge(vertex, adjacents[0]);
          }
          if (adjacents[1] != INVALID_NUMBER) {
            graph.addEdge(vertex, adjacents[1]);
          }
        }
      }
    }
    return sVertexName;
  }

  private static void print2D(char[][][] mat)
  {
    for (char[][] rows : mat) {
      StringBuilder builder = new StringBuilder();
      for (char[] innerRow : rows) {

        builder.append("(").append(innerRow[0]).append(" ").append((int) innerRow[1]).append(")");
      }
      // System.out.println(builder);
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
            area[counter][i][VERTEX_NAME_INDEX] = (char) numberOfVertexes++;
          } else {
            area[counter][i][VERTEX_NAME_INDEX] = (char) INVALID_NUMBER;
          }

          area[counter][i][PIPE_TYPE_INDEX] = element;
        }
        counter++;
      }
    }
    return new Area(area, numberOfVertexes);
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
