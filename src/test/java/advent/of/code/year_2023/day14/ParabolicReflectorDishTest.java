package advent.of.code.year_2023.day14;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ParabolicReflectorDishTest {

  @Test
  void getTotalLoadOfNorthSupportBeams_whenExecuted_thenReturn136() throws IOException {
    assertEquals(136, new ParabolicReflectorDish().getTotalLoadOfNorthSupportBeams());
  }

  @Test
  void getTotalLoadAfterCycles_whenInputIs1000000000_thenReturn64() throws IOException {
    assertEquals(64, new ParabolicReflectorDish().getTotalLoadAfterCycles(1000000000));
  }
}