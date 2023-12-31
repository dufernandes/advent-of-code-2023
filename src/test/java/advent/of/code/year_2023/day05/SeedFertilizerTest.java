package advent.of.code.year_2023.day05;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SeedFertilizerTest {

  @Test
  void findLowestLocation_whenExecuted_thenReturn35() throws IOException {
    assertEquals(35, new SeedFertilizer().findLowestLocation());
  }

  @Test
  void findLowestLocationForSeedRange_whenExecuted_thenReturn46() throws IOException {
    assertEquals(46, new SeedFertilizer().findLowestLocationForSeedRange());
  }
}