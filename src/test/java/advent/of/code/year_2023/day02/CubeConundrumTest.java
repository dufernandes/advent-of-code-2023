package advent.of.code.year_2023.day02;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static advent.of.code.year_2023.day02.CubeConundrum.*;
import static org.junit.jupiter.api.Assertions.*;

class CubeConundrumTest {

  @Test
  void getPossibleGamesSum_whenExecuted_thenResultIs8() throws IOException {
    assertEquals(8, new CubeConundrum().getPossibleGamesSum(RED_CUBES_AVAILABLE, GREEN_CUBES_AVAILABLE, BLUE_CUBES_AVAILABLE));
  }

  @Test
  void getPowerSum_whenExecuted_thenResultIs2286() throws IOException {
    assertEquals(2286, new CubeConundrum().getPowerSum());
  }
}