package advent.of.code.year_2023.day09;

import advent.of.code.year_2023.day05.SeedFertilizer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

@Slf4j
public class MirageMaintenance {

  private static final String INPUT_FILE = "/year2023/day_09_input.txt";

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new MirageMaintenance().getSumOfExtrapolatedValues());
      log.info("The result of part two is: {}", new MirageMaintenance().getSumOfExtrapolatedValuesFromTheLeft());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  public long getSumOfExtrapolatedValuesFromTheLeft() throws IOException {
    long sum = 0;

    InputStream is = MirageMaintenance.class.getResourceAsStream(INPUT_FILE);

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        long[] input = Arrays.stream(line.split("\\s+")).mapToLong(Long::parseLong).toArray();
        long[][] predictionDataset = new long[input.length + 1][input.length];
        System.arraycopy(input, 0, predictionDataset[0], 0, input.length);
        int innerLength = input.length - 1;
        int i, j = 0;
        for (i = 1; i < input.length + 1; i++) {
          boolean areAllZeros = true;
          for (j = 0; j < innerLength; j++) {
            predictionDataset[i][j] = predictionDataset[i - 1][j + 1] - predictionDataset[i - 1][j];
            if (predictionDataset[i][j] != 0) {
              areAllZeros = false;
            }
          }
          if (areAllZeros) {
            break;
          }
          innerLength--;
        }

        long prediction = 0;
        for (i = i - 1; i >= 0; i--) {
          prediction = predictionDataset[i][0] - prediction;
          j++;
        }

        sum += prediction;
      }
    }

    return sum;
  }

  public long getSumOfExtrapolatedValues() throws IOException {
    long sum = 0;

    InputStream is = SeedFertilizer.class.getResourceAsStream(INPUT_FILE);

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        long[] input = Arrays.stream(line.split("\\s+")).mapToLong(Long::parseLong).toArray();
        long[][] predictionDataset = new long[input.length + 1][input.length];
        System.arraycopy(input, 0, predictionDataset[0], 0, input.length);
        int innerLength = input.length - 1;
        int i, j = 0;
         for (i = 1; i < input.length + 1; i++) {
          boolean areAllZeros = true;
          for (j = 0; j < innerLength; j++) {
            predictionDataset[i][j] = predictionDataset[i - 1][j + 1] - predictionDataset[i - 1][j];
            if (predictionDataset[i][j] != 0) {
              areAllZeros = false;
            }
          }
          if (areAllZeros) {
            break;
          }
          innerLength--;
        }

        long prediction = 0;
        for (i = i - 1; i >= 0; i--) {
          prediction += predictionDataset[i][j];
          j++;
        }

        sum += prediction;
      }
    }

    return sum;
  }
}