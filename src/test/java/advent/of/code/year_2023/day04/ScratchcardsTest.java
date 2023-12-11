package advent.of.code.year_2023.day04;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ScratchcardsTest {

  @Test
  void getScratchcardsPoints_whenExecuted_thenReturn13() throws IOException {
    assertEquals(13, new Scratchcards().getScratchcardsPoints());
  }

  @Test
  void getNumberOfScoreCards_whenExecuted_thenReturn30() throws IOException {
    assertEquals(30, new Scratchcards().getNumberOfScoreCards());
  }
}