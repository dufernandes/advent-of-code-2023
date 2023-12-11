package advent.of.code.year_2023.day_05;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SeedFertilizerWithArraysTest {

  @Test
  void findLowestLocation_whenExecuted_thenReturn51752125() throws IOException {
    assertEquals(51752125, new SeedFertilizerWithArrays().findLowestLocation());
  }

  @Test
  void findLowestLocationForSeedRange_whenExecuted_thenReturn12634632() throws IOException {
    assertEquals(12634632, new SeedFertilizerWithArrays().findLowestLocationForSeedRange());
  }
}