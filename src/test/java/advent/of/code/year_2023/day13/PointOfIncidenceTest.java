package advent.of.code.year_2023.day13;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PointOfIncidenceTest {

  @Test
  void getSummary_whenExecuted_thenReturn405() throws IOException {
    assertEquals(405, new PointOfIncidence().getSummary());
  }

  @Test
  void getSummaryWithSmudge_whenExecuted_thenReturn400() throws  IOException {
    assertEquals(400, new PointOfIncidence().getSummaryWithSmudge());
  }
}