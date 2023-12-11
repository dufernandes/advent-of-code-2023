package advent.of.code.year_2023.day06;

import advent.of.code.year_2023.day05.SeedFertilizer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

@Slf4j
public class WaitForIt {

  private static final String INPUT_FILE = "/year2023/day_06_input.txt";

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new WaitForIt().findNumberOfWaysToBeatTheRecord());
      log.info("The result of part two is: {}", new WaitForIt().findNumberOfWaysToBeatTheRecordInLongerRace());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  public long findNumberOfWaysToBeatTheRecordInLongerRace() throws IOException {
    long numberOfWaysToBeatTheRecord = 0;
    InputStream is = SeedFertilizer.class.getResourceAsStream(INPUT_FILE);

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

      String line = br.readLine();
      long time = getLongFromArray(line);

      line = br.readLine();
      long distance = getLongFromArray(line);

      long winnerPossibilities = 0;
      for (long buttonPressedTime = 0; buttonPressedTime <= time; buttonPressedTime++) {
        long speed = buttonPressedTime;
        long travelledDistanced = speed * (time - buttonPressedTime);
        if (travelledDistanced > distance) {
          numberOfWaysToBeatTheRecord++;
        }
      }
    }

    return numberOfWaysToBeatTheRecord;
  }

  public long findNumberOfWaysToBeatTheRecord() throws IOException {
    long numberOfWaysToBeatTheRecord = 0;
    InputStream is = SeedFertilizer.class.getResourceAsStream(INPUT_FILE);

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

      String line = br.readLine();
      int[] times = getIntsFromArray(line);

      line = br.readLine();
      int[] distances = getIntsFromArray(line);

      for (int raceNumber = 0; raceNumber < times.length; raceNumber++) {
        int time = times[raceNumber];
        int distance = distances[raceNumber];

        long winnerPossibilities = 0;
        for (int buttonPressedTime = 0; buttonPressedTime <= time; buttonPressedTime++) {
          int speed = buttonPressedTime;
          int travelledDistanced = speed * (time - buttonPressedTime);
          if (travelledDistanced > distance) {
            winnerPossibilities++;
          }
        }
        if (winnerPossibilities > 0) {
          if (numberOfWaysToBeatTheRecord == 0) {
            numberOfWaysToBeatTheRecord = winnerPossibilities;
          } else {
            numberOfWaysToBeatTheRecord *= winnerPossibilities;
          }
        }
      }
    }

    return numberOfWaysToBeatTheRecord;
  }

  private static int[] getIntsFromArray(String line) {
    return Arrays.stream(line.split(":\\s+")[1].split("\\s+")).mapToInt(Integer::parseInt).toArray();
  }

  private static long getLongFromArray(String line) {
    return Long.parseLong(String.join("", line.split(":\\s+")[1].split("\\s+")));
  }
}
