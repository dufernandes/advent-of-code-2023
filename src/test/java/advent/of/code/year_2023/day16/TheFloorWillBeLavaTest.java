package advent.of.code.year_2023.day16;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TheFloorWillBeLavaTest {

  @Test
  void sumEnergizedBeams_whenExecuted_thenReturn46() throws IOException {
    assertEquals(46, new TheFloorWillBeLava().sumEnergizedBeams());
  }

  @Test
  void sumEnergizedBeamsOfBestScenario_whenExecuted_thenReturn51() throws IOException {
    assertEquals(51, new TheFloorWillBeLava().sumEnergizedBeamsOfBestScenario());
  }
}