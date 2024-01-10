package advent.of.code.year_2023.day12;

import advent.of.code.year_2023.day11.CosmicExpansionOptimized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

@Slf4j
public class HotSprings {

  private static final String INPUT_FILE = "/year2023/day_12_input.txt";
  private static final char UNKNOWN_SPRING = '?';
  public static final char OPERATIONAL = '.';
  public static final char DAMAGED = '#';

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new HotSprings().sumAllArrangements());
      //log.info("The result for part two is: {}", new HotSprings().sumOfLengths(100));
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  public int sumAllArrangements() throws IOException {

    int sum = 0;
    InputStream is = CosmicExpansionOptimized.class.getResourceAsStream(INPUT_FILE);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        int[] springInfos = Arrays.stream(line.split("\\s+")[1].split(",")).mapToInt(Integer::parseInt).toArray();
        String springs = line.split("\\s+")[0];
        sum += sumArrangements(springs, springInfos);
      }
    }

    return sum;
  }

  private int sumArrangements(String springs, int[] springInfos) {
    if (!containsUnknowns(springs)) {
      return springInfosMatchSprings(springs, springInfos) ? 1 : 0;
    }

    int sum = 0;
    int unknownSpringIndex = springs.indexOf(UNKNOWN_SPRING);
    sum += sumArrangements(replaceChartAt(springs, OPERATIONAL, unknownSpringIndex), springInfos);
    sum += sumArrangements(replaceChartAt(springs, DAMAGED, unknownSpringIndex), springInfos);

    return sum;
  }

  private String replaceChartAt(String text, char replacement, int index) {
    return text.substring(0, index) + replacement + text.substring(index + 1);
  }

  private boolean containsUnknowns(String springs) {
    if (StringUtils.isEmpty(springs)) {
      return false;
    }

    return springs.chars().anyMatch(s -> s == UNKNOWN_SPRING);
  }

  private boolean springInfosMatchSprings(String springs, int[] springInfos) {

    String[] damagedSprings = springs.split("\\.+");
    damagedSprings = Arrays.stream(damagedSprings).filter(d -> !d.isEmpty()).toList().toArray(new String[0]);

    if (damagedSprings.length != springInfos.length) {
      return false;
    }

    for (int i = 0; i < damagedSprings.length; i++) {
      if (damagedSprings[i].length() != springInfos[i]) {
        return false;
      }
    }

    return true;
  }
}
