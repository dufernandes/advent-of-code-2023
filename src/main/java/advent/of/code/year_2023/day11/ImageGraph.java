package advent.of.code.year_2023.day11;

/*package whatever //do not write package name here */
// Java Code implementation for above problem
import java.util.LinkedList;
import java.util.Queue;

// QItem for current location and distance
// from source location
class QItem {
  int row;
  int col;
  int dist;
  public QItem(int row, int col, int dist)
  {
    this.row = row;
    this.col = col;
    this.dist = dist;
  }
}

public class ImageGraph {
  static int minDistance(char[][] grid, int rowOrigin, int colOrigin, int rowDestiny, int colDestiny)
  {
    // assign source
    QItem source = new QItem(0, 0, 0);
    source.row = rowOrigin;
    source.col = colOrigin;


    // applying BFS on matrix cells starting from source
    Queue<QItem> queue = new LinkedList<>();
    queue.add(new QItem(source.row, source.col, 0));

    boolean[][] visited
            = new boolean[grid.length][grid[0].length];
    visited[source.row][source.col] = true;

    while (!queue.isEmpty()) {
      QItem p = queue.remove();

      // Destination found;
      if (p.row == rowDestiny && p.col == colDestiny) {
        return p.dist;
      }

      // moving up
      if (isValid(p.row - 1, p.col, grid, visited)) {
        queue.add(new QItem(p.row - 1, p.col,
                p.dist + 1));
        visited[p.row - 1][p.col] = true;
      }

      // moving down
      if (isValid(p.row + 1, p.col, grid, visited)) {
        queue.add(new QItem(p.row + 1, p.col,
                p.dist + 1));
        visited[p.row + 1][p.col] = true;
      }

      // moving left
      if (isValid(p.row, p.col - 1, grid, visited)) {
        queue.add(new QItem(p.row, p.col - 1,
                p.dist + 1));
        visited[p.row][p.col - 1] = true;
      }

      // moving right
      if (isValid(p.row, p.col + 1, grid,
              visited)) {
        queue.add(new QItem(p.row, p.col + 1,
                p.dist + 1));
        visited[p.row][p.col + 1] = true;
      }
    }
    return -1;
  }

  // checking where it's valid or not
  private static boolean isValid(int x, int y,
                                 char[][] grid,
                                 boolean[][] visited)
  {
    return x >= 0 && y >= 0 && x < grid.length
            && y < grid[0].length
            && !visited[x][y];
  }

  // Driver code
  public static void main(String[] args)
  {
    char[][] grid = { { '0', '*', '0', 's' },
            { '*', '0', '*', '*' },
            { '0', '*', '*', '*' },
            { 'd', '*', '*', '*' } };

    System.out.println(minDistance(grid, 0, 3, 3, 0));
  }
}
