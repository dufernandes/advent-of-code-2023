package advent.of.code.year_2023.day_06;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class WaitForItTest {

  @Test
  void findNumberOfWaysToBeatTheRecordInLongerRace_whenExecuted_thenReturn71503() throws IOException {
    assertEquals(71503, new WaitForIt().findNumberOfWaysToBeatTheRecordInLongerRace());
  }

  @Test
  void findNumberOfWaysToBeatTheRecord_whenExecuted_thenReturn288() throws IOException {
    assertEquals(288, new WaitForIt().findNumberOfWaysToBeatTheRecord());
  }
}