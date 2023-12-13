package advent.of.code.year_2023.day08;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HauntedWastelandTest {

  @Test
  void getNumberOfSteps_whenExecuted_thenReturn6() throws IOException {
    assertEquals(6, new HauntedWasteland().getNumberOfSteps());
  }

  @Test
  void getNumberOfStepsUntilAllEndUpWithZ_whenExecuted_thenReturn6() throws IOException {
    assertEquals(6, new HauntedWasteland().getNumberOfStepsUntilAllEndUpWithZ());
  }
}