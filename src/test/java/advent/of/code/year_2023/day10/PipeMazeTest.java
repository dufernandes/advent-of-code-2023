package advent.of.code.year_2023.day10;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PipeMazeTest {

  @Test
  void findFarthestPoint_whenExecuted_thenReturn7012() throws IOException {
    assertEquals(7012, new PipeMaze().findFarthestPoint());
  }

  @Test
  void getNumberOfEnclosedTiles_whenExecuted_thenReturn395() throws IOException {
    assertEquals(395, new PipeMaze().getNumberOfEnclosedTiles());
  }
}