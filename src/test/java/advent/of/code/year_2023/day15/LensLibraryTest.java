package advent.of.code.year_2023.day15;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class LensLibraryTest {

  @Test
  void getSumOfHashes_whenExecuted_thenReturn1320() throws IOException {
    assertEquals(1320, new LensLibrary().getSumOfHashes());
  }

  @Test
  void getSumOfFocusingPower_whenExecuted_thenReturn145() throws IOException {
    assertEquals(145, new LensLibrary().getSumOfFocusingPower());
  }
}