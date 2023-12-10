package advent.of.code.year_2023.day_05;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.min;

@Slf4j
public class SeedFertilizer {

  private static final String INPUT_FILE = "/year2023/day_05_input.txt";

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new SeedFertilizer().findLowestLocation());
      //log.info("The result of part two is: {}", new Scratchcards().getNumberOfScoreCards());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  public long findLowestLocation() throws IOException {
    long lowestLocation = Long.MIN_VALUE;
    Almanac almanac = new Almanac();
    InputStream is = SeedFertilizer.class.getResourceAsStream(INPUT_FILE);

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      long[] seeds = null;
      while ((line = br.readLine()) != null) {
        setSeeds(line, almanac);
        setCategoryMaps(line, br, almanac);
      }
    }

    for (long seed : almanac.seeds()) {
      long mappedElement = seed;
      log.info("current seed: {}", seed);
      for (CategoryMap categoryMap : almanac.maps()) {
        log.info("current mapper: {}", categoryMap.name());
        long oldMappedElement = mappedElement;
        for (Converter converter : categoryMap.converters()) {
          log.info("current mapped element: {}", mappedElement);
          log.info("current converter: destination - {}, source: {}, range: {}", converter.destination, converter.source, converter.range);
          if (mappedElement >= converter.source &&
              mappedElement <= converter.source + converter.range - 1) {
            mappedElement = converter.destination +
                    (mappedElement - converter.source);
            log.info("convert range found - math - new mapped element {} = destination {} + (mapped element {} - source {})", mappedElement, converter.destination, oldMappedElement, converter.source);
            log.info("convert range found - new mapped element: {}", mappedElement);
            break;
          }
        }
        if (mappedElement == oldMappedElement) {
          log.info("no mapping found, thus mapped element keeps the same: {}", mappedElement);
        }
      }
      lowestLocation = lowestLocation == Long.MIN_VALUE ? mappedElement : min(lowestLocation, mappedElement);
    }

    return lowestLocation;
  }

  private static void setSeeds(String line, Almanac almanac) {
    long[] seeds;
    if (line.contains("seeds:")) {
      seeds = Arrays.stream(line.split(": ")[1].split("\\s+")).mapToLong(Long::parseLong).toArray();
      almanac.seeds.addAll(Arrays.stream(seeds).boxed().toList());
    }
  }

  private static void setCategoryMaps(String line, BufferedReader br, Almanac almanac) throws IOException {
    setCategoryMap("seed-to-soil map", line, br, almanac);
    setCategoryMap("soil-to-fertilizer map", line, br, almanac);
    setCategoryMap("fertilizer-to-water map", line, br, almanac);
    setCategoryMap("water-to-light map", line, br, almanac);
    setCategoryMap("light-to-temperature map", line, br, almanac);
    setCategoryMap("temperature-to-humidity map", line, br, almanac);
    setCategoryMap("humidity-to-location map", line, br, almanac);
  }

  private static void setCategoryMap(String categoryMapName, String line, BufferedReader br, Almanac almanac) throws IOException {
    if (line.contains(categoryMapName)) {
      CategoryMap soilToFertilizerMap = new CategoryMap(categoryMapName);
      while ((line = br.readLine()) != null && !line.isEmpty()) {
        long[] converterArray = Arrays.stream(line.split("\\s+")).mapToLong(Long::parseLong).toArray();
        Converter converter = new Converter(converterArray[0], converterArray[1], converterArray[2]);
        soilToFertilizerMap.converters.add(converter);
      }
      almanac.maps.add(soilToFertilizerMap);
    }
  }

  private record Converter(long destination, long source, long range) {}

  private record CategoryMap(String name, List<Converter> converters) {
    public CategoryMap(String name) {
      this(name, new ArrayList<>());
    }
  }

  private record Almanac(List<Long> seeds, List<CategoryMap> maps) {
    public Almanac() {
      this(new ArrayList<>(), new ArrayList<>());
    }
  }
}
