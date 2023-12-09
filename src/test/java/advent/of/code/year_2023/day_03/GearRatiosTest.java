package advent.of.code.year_2023.day_03;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GearRatiosTest {

  @Test
  void getPowerSum_whenExecuted_thenResultIs535078() throws IOException {
    assertEquals(535078, new GearRatios().getPowerSum());
  }

  @Test
  void getGearRatioSum_whenExecuted_thenResultIs75312571() throws IOException {
    assertEquals(75312571, new GearRatios().getGearRatioSum());
  }
}