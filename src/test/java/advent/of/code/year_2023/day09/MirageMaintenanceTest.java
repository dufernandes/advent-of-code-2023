package advent.of.code.year_2023.day09;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MirageMaintenanceTest {

  @Test
  void getSumOfExtrapolatedValues_whenExecuted_thenReturn114() throws IOException {
    assertEquals(114, new MirageMaintenance().getSumOfExtrapolatedValues());
  }

  @Test
  void getSumOfExtrapolatedValuesFromTheLeft_whenExecuted_thenReturn2() throws IOException {
    assertEquals(2, new MirageMaintenance().getSumOfExtrapolatedValuesFromTheLeft());
  }
}