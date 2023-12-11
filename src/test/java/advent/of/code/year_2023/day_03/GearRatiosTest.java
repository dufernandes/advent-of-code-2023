package advent.of.code.year_2023.day_03;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GearRatiosTest {

  @Test
  void getPowerSum_whenExecuted_thenResultIs4361() throws IOException {
    assertEquals(4361, new GearRatios().getPowerSum());
  }

  @Test
  void getGearRatioSum_whenExecuted_thenResultIs467835() throws IOException {
    assertEquals(467835, new GearRatios().getGearRatioSum());
  }
}