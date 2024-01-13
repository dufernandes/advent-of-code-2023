package advent.of.code.year_2023.day12;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class HotSpringsTest {

  @Test
  void sumAllArrangements_whenExecuted_thenReturn21() throws IOException {
    assertEquals(21, new HotSprings().sumAllArrangements());
  }

  @Test
  void sumAllArrangementsUnfoldingRecords_whenExecutedWithMultiplier5_thenReturn525152() throws IOException {
    assertEquals(525152, new HotSprings().sumAllArrangementsUnfoldingRecords(5));
  }
}