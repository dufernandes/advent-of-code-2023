package advent.of.code.year_2023.day12;

import advent.of.code.year_2023.day11.CosmicExpansionOptimized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HotSprings {

  private static final String INPUT_FILE = "/year2023/day_12_input.txt";
  private static final char UNKNOWN_SPRING = '?';
  private static final char OPERATIONAL = '.';
  private static final char DAMAGED = '#';
  private static final char INFO_SEPARATOR = ',';

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new HotSprings().sumAllArrangements());
      log.info("The result for part two is: {}", new HotSprings().sumAllArrangementsUnfoldingRecords(5));
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  public int sumAllArrangementsUnfoldingRecords(int multiplier) throws IOException {

    int sum = 0;
    InputStream is = CosmicExpansionOptimized.class.getResourceAsStream(INPUT_FILE);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {

        String springInfoText = line.split("\\s+")[1];
        String springsText = line.split("\\s+")[0];

        StringBuilder expandedSpringInfoText = new StringBuilder(springInfoText);
        StringBuilder expandedSpringsText = new StringBuilder(springsText);
        for (int i = 0; i < multiplier - 1; i++) {
          expandedSpringInfoText.append(INFO_SEPARATOR).append(springInfoText);
          expandedSpringsText.append(UNKNOWN_SPRING).append(springsText);
        }

        int[] springInfos = Arrays.stream(expandedSpringInfoText.toString().split(",")).mapToInt(Integer::parseInt).toArray();
        String springs = expandedSpringsText.toString();
        sum += sumArrangements(springs, springInfos);
      }
    }

    return sum;
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
    cache.clear();
    return doSumArrangements(springs, springInfos);
  }

  private static final Map<String, Integer> cache = new HashMap<>();

  private int doSumArrangements(String springs, int[] springInfos) {
    if (!containsUnknowns(springs)) {
      int result = springInfosMatchSprings(springs, springInfos) ? 1 : 0;
      return result;
    }

    if (!areNumberOfElementsSound(springs, springInfos)) {
      return 0;
    }

    if (cache.containsKey(springs)) {
      return cache.get(springs);
    }

    int sum = 0;
    int unknownSpringIndex = springs.indexOf(UNKNOWN_SPRING);
    String nextSprings = replaceChartAt(springs, OPERATIONAL, unknownSpringIndex);
    if (cache.containsKey(nextSprings)) {
      sum += cache.get(nextSprings);
    } else {
      sum += doSumArrangements(nextSprings, springInfos);
      cache.put(nextSprings, sum);
    }

    nextSprings = replaceChartAt(springs, DAMAGED, unknownSpringIndex);
    if (cache.containsKey(nextSprings)) {
      sum += cache.get(nextSprings);
    } else {
      sum += doSumArrangements(nextSprings, springInfos);
      cache.put(nextSprings, sum);
    }

    cache.put(springs, sum);

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

  private boolean areNumberOfElementsSound(String springs, int[] springInfos) {
    String[] damagedSprings = springs.replace(UNKNOWN_SPRING, OPERATIONAL).split("\\.+");
    damagedSprings = Arrays.stream(damagedSprings).filter(d -> !d.isEmpty()).toList().toArray(new String[0]);

    int sumOperationalElements = Arrays.stream(springInfos).reduce(0, Integer::sum);
    int numberOfOperationalElements = Arrays.stream(damagedSprings).reduce(StringUtils.EMPTY, (subtotal, element) -> subtotal + element).length();

    return numberOfOperationalElements <= sumOperationalElements;
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
