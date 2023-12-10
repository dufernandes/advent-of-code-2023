package advent.of.code.year_2023.day_04;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ScratchcardsTest {

  @Test
  void getScratchcardsPoints_whenExecuted_thenReturn26443() throws IOException {
    assertEquals(26443, new Scratchcards().getScratchcardsPoints());
  }

  @Test
  void getNumberOfScoreCards_whenExecuted_thenReturn6284877() throws IOException {
    assertEquals(6284877, new Scratchcards().getNumberOfScoreCards());
  }
}