package advent.of.code.year_2023.day07;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CamelCardsTest {

  @Test
  void findTotalWinnings_whenExecuted_thenReturn6640() throws IOException {
    assertEquals(6440, new CamelCards().findTotalWinnings());
  }

  @Test
  void findTotalWinningsWithJoker_whenExecuted_thenReturn5905() throws  IOException {
    assertEquals(5905, new CamelCards().findTotalWinningsWithJoker());
  }
}