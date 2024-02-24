package advent.of.code.year_2023.day18;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LavaductLagoonTest {

  @Test
  void calculateArea_whenExecuted_thenReturn62() throws IOException {
    assertEquals(62, new LavaductLagoon().calculateArea());
  }

  @Test
  void calculateAreaFixingBug_whenExecuted_thenReturn952408144115() throws IOException {
    assertEquals(952408144115L, new LavaductLagoon().calculateAreaFixingBug());
  }
}