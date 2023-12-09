package advent.of.code.year_2023.day_01;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TrebuchetTest {

  @Test
  void calibrationSumPartOne_whenExecuted_thenResultIs55172() throws IOException {
    assertEquals(55172, new Trebuchet().calibrationSumPartOne());
  }

  @Test
  void calibrationSumPartTwo_whenExecuted_thenResultIs54925() throws IOException {
    assertEquals(54925, new Trebuchet().calibrationSumPartTwo());
  }
}