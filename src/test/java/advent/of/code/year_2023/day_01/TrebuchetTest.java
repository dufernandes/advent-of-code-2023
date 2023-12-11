package advent.of.code.year_2023.day_01;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TrebuchetTest {

  @Test
  void calibrationSumPartOne_whenExecuted_thenResultIs142() throws IOException {
    assertEquals(142, new Trebuchet().calibrationSumPartOne());
  }

  @Test
  void calibrationSumPartTwo_whenExecuted_thenResultIs281() throws IOException {
    assertEquals(281, new Trebuchet().calibrationSumPartTwo());
  }
}