package advent.of.code.year_2023.day11;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CosmicExpansionOptimizedTest {

  @Test
  void sumOfLengths_whenExpansionIs2_thenReturn374() throws IOException {
    assertEquals(374, new CosmicExpansionOptimized().sumOfLengths(2));
  }

  @Test
  void sumOfLengths_whenExpansionIs100_thenReturn8410() throws IOException {
    assertEquals(8410, new CosmicExpansionOptimized().sumOfLengths(100));
  }
}