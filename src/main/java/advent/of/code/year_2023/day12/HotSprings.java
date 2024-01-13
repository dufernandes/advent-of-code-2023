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
import java.util.Objects;

@Slf4j
public class HotSprings {

  private static final String INPUT_FILE = "/year2023/day_12_input.txt";
  private static final char UNKNOWN_SPRING = '?';
  private static final char OPERATIONAL = '.';
  private static final char DAMAGED = '#';
  private static final char INFO_SEPARATOR = ',';

  private static final Map<SpringsSpringInfos, Long> cache = new HashMap<>();

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new HotSprings().sumAllArrangements());
      log.info("The result for part two is: {}", new HotSprings().sumAllArrangementsUnfoldingRecords(5));
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  public long sumAllArrangementsUnfoldingRecords(int multiplier) throws IOException {

    long sum = 0;
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
        cache.clear();
        sum += sumArrangementsUsingDynamicProgrammingAndCache(springs, springInfos);
      }
    }

    return sum;
  }

  public long sumAllArrangements() throws IOException {

    long sum = 0;
    InputStream is = CosmicExpansionOptimized.class.getResourceAsStream(INPUT_FILE);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        int[] springInfos = Arrays.stream(line.split("\\s+")[1].split(",")).mapToInt(Integer::parseInt).toArray();
        String springs = line.split("\\s+")[0];
        sum += sumArrangementsUsingPermutation(springs, springInfos);
      }
    }

    return sum;
  }

  /*
    This solution uses dynamic programming, where a top-bottom approach
    is used. The explanation is within the code.
   */
  private long sumArrangementsUsingDynamicProgrammingAndCache(String springs, int[] springInfos) {

    /*
      First base case: when there are no springs left, if there are also not numbers / info left,
      it means all springs and numbers were matched, thus return 1, as it is a valid combination.
      Otherwise return 0.
     */
    if (StringUtils.isEmpty(springs)) {
      if (springInfos == null || springInfos.length == 0) {
        return 1L;
      } else {
        return 0L;
      }
    }

    /*
      Second base case: If the info / numbers is empty, it means all matches are done. Therefore, only
      damaged / dots springs can be left. In this case, return 1, otherwise 0.
     */
    if (springInfos == null || springInfos.length == 0) {
      if (containsDamaged(springs)) {
        return 0L;
      } else {
        return 1L;
      }
    }

    // if any previous cache result was found, return it
    SpringsSpringInfos cacheKey = new SpringsSpringInfos(springs, springInfos);
    if (cache.containsKey(cacheKey)) {
      return cache.get(cacheKey);
    }

    long sum = 0;

    /*
      The first generic case for recursion states as follows: if "." of "?" are found, no match will
      be attempted, because "?" can be replaced by ".". Thus this method is called again removing the first
      char (either "." or "?"). The numbers / info are kept, because no match was attempted.
     */
    char currentElement = springs.charAt(0);
    if (currentElement == OPERATIONAL || currentElement == UNKNOWN_SPRING) {
      sum += sumArrangementsUsingDynamicProgrammingAndCache(springs.substring(1), springInfos);
    }

    /*
      Ths second case of the recursion is where a match is attempted. For that the first char must be either
      "#" or "?" (which can be replaced by a "#"). In order ot have a match, the following conditions must
      be met:
      1. the match number must be equals or less than the size of the sprigs. If it is bigger, than no match
      is possible. For instance, if hte number is 3 but the springs are "##", no match is possible.
      2. In the interval of the number to be matched, there cannot be any "."s. If there were, no match is possible
      because the number represents contiguous broken springs.
      3. Either (OR) two things must e true
        - the length of the info / remaining springs is the size of the number to be matched - meaning this the last
        match of the sequence of springs
        OR
        - the next character of springs being must be a ".", because this defines the end of a matching of broken
        springs

       Note that this covers all cases of the recursion
     */
    if (currentElement == DAMAGED || currentElement == UNKNOWN_SPRING) {
      if (springInfos[0] <= springs.length() &&
          containsNoOperational(springs.substring(0, springInfos[0])) &&
          (springs.length() == springInfos[0] || springs.charAt(springInfos[0]) != DAMAGED)) {

        int[] sInfos = new int[springInfos.length - 1];
        System.arraycopy(springInfos, 1, sInfos, 0, springInfos.length - 1);

        String nextInfo = springs.length() == springInfos[0] ? StringUtils.EMPTY : springs.substring(springInfos[0] + 1);
        sum += sumArrangementsUsingDynamicProgrammingAndCache(nextInfo, sInfos);
      }
    }

    cache.put(cacheKey, sum);

    return sum;
  }

  /*
    The implementation using permutation and recursion, does not scale
    since cache cannot be implemented, because the recursion only create
    unique elements.
   */
  private long sumArrangementsUsingPermutation(String springs, int[] springInfos) {
    // the base case is: when there are not "?" it means a match will all numbers and springs can be tried
    if (!containsUnknowns(springs)) {
      return springInfosMatchSprings(springs, springInfos) ? 1 : 0;
    }

    // small optimization
    if (!areNumberOfElementsSound(springs, springInfos)) {
      return 0L;
    }

    long sum = 0L;
    int unknownSpringIndex = springs.indexOf(UNKNOWN_SPRING);
    // given the current unknown char "?" call this very method with a "."
    sum += sumArrangementsUsingPermutation(replaceChartAt(springs, OPERATIONAL, unknownSpringIndex), springInfos);
    // given the current unknown char "?" call this very method with a "?"
    sum += sumArrangementsUsingPermutation(replaceChartAt(springs, DAMAGED, unknownSpringIndex), springInfos);

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

  private boolean containsDamaged(String springs) {
    if (StringUtils.isEmpty(springs)) {
      return false;
    }

    return springs.chars().anyMatch(s -> s == DAMAGED);
  }

  private boolean containsNoOperational(String springs) {
    if (StringUtils.isEmpty(springs)) {
      return false;
    }

    return springs.chars().allMatch(s -> s != OPERATIONAL);
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

  private record SpringsSpringInfos(String springs, int[] springInfos) {
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      SpringsSpringInfos that = (SpringsSpringInfos) o;
      return Objects.equals(springs, that.springs) && Arrays.equals(springInfos, that.springInfos);
    }

    @Override
    public int hashCode() {
      int result = Objects.hash(springs);
      result = 31 * result + Arrays.hashCode(springInfos);
      return result;
    }
  }
}
