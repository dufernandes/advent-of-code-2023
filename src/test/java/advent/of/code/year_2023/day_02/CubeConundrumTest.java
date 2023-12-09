package advent.of.code.year_2023.day_02;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static advent.of.code.year_2023.day_02.CubeConundrum.*;
import static org.junit.jupiter.api.Assertions.*;

class CubeConundrumTest {

  @Test
  void getPossibleGamesSum_whenExecuted_thenResultIs2795() throws IOException {
    assertEquals(2795, new CubeConundrum().getPossibleGamesSum(RED_CUBES_AVAILABLE, GREEN_CUBES_AVAILABLE, BLUE_CUBES_AVAILABLE));
  }

  @Test
  void getPowerSum_whenExecuted_thenResultIs75561() throws IOException {
    assertEquals(75561, new CubeConundrum().getPowerSum());
  }
}