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
      log.info("The result of part two is: {}", new SeedFertilizer().findLowestLocationForSeedRange());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  public long findLowestLocation() throws IOException {
    Almanac almanac = new Almanac();
    InputStream is = SeedFertilizer.class.getResourceAsStream(INPUT_FILE);

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        setSeeds(line, almanac.seeds);
        setCategoryMaps(line, br, almanac.maps);
      }
    }

    return getLowestLocation(almanac);
  }

  public long findLowestLocationForSeedRange() throws IOException {
    AlmanacWithSeedRange almanac = new AlmanacWithSeedRange();
    InputStream is = SeedFertilizer.class.getResourceAsStream(INPUT_FILE);

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        setRangeSeeds(line, almanac.seeds);
        setCategoryMaps(line, br, almanac.maps);
      }
    }

    return getLowestLocation(almanac);
  }

  private static void setRangeSeeds(String line, List<SeedRange> seeds) {
    if (line.contains("seeds:")) {
      long[] seedsArray = Arrays.stream(line.split(": ")[1].split("\\s+")).mapToLong(Long::parseLong).toArray();
      for (int i = 0; i < seedsArray.length; i+=2) {
        long offset = seedsArray[i];
        long range = seedsArray[i + 1];
        seeds.add(new SeedRange(offset, range));
      }
    }
  }

  private static long getLowestLocation(AlmanacWithSeedRange almanac) {
    long lowestLocation = Long.MIN_VALUE;
    for (SeedRange seedRange : almanac.seeds()) {
      long offset = seedRange.offset;
      long range = seedRange.range;
      for (long seed = offset; seed <= offset + range - 1; seed++) {
        long mappedElement = seed;
        for (CategoryMap categoryMap : almanac.maps()) {
          for (Converter converter : categoryMap.converters()) {
            if (mappedElement >= converter.source &&
                    mappedElement <= converter.source + converter.range - 1) {
              mappedElement = converter.destination +
                      (mappedElement - converter.source);
              break;
            }
          }
        }
        lowestLocation = lowestLocation == Long.MIN_VALUE ? mappedElement : min(lowestLocation, mappedElement);
      }
    }
    return lowestLocation;
  }

  private static long getLowestLocation(Almanac almanac) {
    long lowestLocation = Long.MIN_VALUE;
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

  private static void setSeeds(String line, List<Long> seeds) {
    long[] seedsArray;
    if (line.contains("seeds:")) {
      seedsArray = Arrays.stream(line.split(": ")[1].split("\\s+")).mapToLong(Long::parseLong).toArray();
      seeds.addAll(Arrays.stream(seedsArray).boxed().toList());
    }
  }

  private static void setCategoryMaps(String line, BufferedReader br, List<CategoryMap> maps) throws IOException {
    setCategoryMap("seed-to-soil map", line, br, maps);
    setCategoryMap("soil-to-fertilizer map", line, br, maps);
    setCategoryMap("fertilizer-to-water map", line, br, maps);
    setCategoryMap("water-to-light map", line, br, maps);
    setCategoryMap("light-to-temperature map", line, br, maps);
    setCategoryMap("temperature-to-humidity map", line, br, maps);
    setCategoryMap("humidity-to-location map", line, br, maps);
  }

  private static void setCategoryMap(String categoryMapName, String line, BufferedReader br, List<CategoryMap> maps) throws IOException {
    if (line.contains(categoryMapName)) {
      CategoryMap soilToFertilizerMap = new CategoryMap(categoryMapName);
      while ((line = br.readLine()) != null && !line.isEmpty()) {
        long[] converterArray = Arrays.stream(line.split("\\s+")).mapToLong(Long::parseLong).toArray();
        Converter converter = new Converter(converterArray[0], converterArray[1], converterArray[2]);
        soilToFertilizerMap.converters.add(converter);
      }
      maps.add(soilToFertilizerMap);
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

  private record SeedRange(long offset, long range) {}

  private record AlmanacWithSeedRange(List<SeedRange> seeds, List<CategoryMap> maps) {
    public AlmanacWithSeedRange() {
      this(new ArrayList<>(), new ArrayList<>());
    }
  }
}
