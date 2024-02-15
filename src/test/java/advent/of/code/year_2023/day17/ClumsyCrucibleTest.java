package advent.of.code.year_2023.day17;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ClumsyCrucibleTest {

  @Test
  void calculateMinimumHeatLoss_whenExecuted_thenReturn102() throws IOException {
    assertEquals(102, new ClumsyCrucible().calculateMinimumHeatLoss());
  }

  @Test
  void calculateMinimumHeatLossPart2_whenExecuted_thenReturn94() throws IOException {
    assertEquals(94, new ClumsyCrucible().calculateMinimumHeatLossPart2());
  }
}