package advent.of.code.year_2023.day_05;


import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import static java.lang.Math.min;

@Slf4j
public class SeedFertilizerWithArrays {

  private static final String INPUT_FILE = "/year2023/day_05_input.txt";

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new SeedFertilizerWithArrays().findLowestLocation());
      log.info("The result of part two is: {}", new SeedFertilizerWithArrays().findLowestLocationForSeedRange());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  public long findLowestLocation() throws IOException {
    Almanac almanac = new Almanac();
    InputStream is = SeedFertilizerWithArrays.class.getResourceAsStream(INPUT_FILE);

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
    InputStream is = SeedFertilizerWithArrays.class.getResourceAsStream(INPUT_FILE);

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        setRangeSeeds(line, almanac.seeds);
        setCategoryMaps(line, br, almanac.maps);
      }
    }

    return getLowestLocation(almanac);
  }

  private static void setRangeSeeds(String line, SeedRange[] seeds) {
    if (line.contains("seeds:")) {
      long[] seedsArray = Arrays.stream(line.split(": ")[1].split("\\s+")).mapToLong(Long::parseLong).toArray();
      int counter = 0;
      for (int i = 0; i < seedsArray.length; i += 2) {
        long offset = seedsArray[i];
        long range = seedsArray[i + 1];
        seeds[counter++] = new SeedRange(offset, range);
      }
    }
  }

  private static long getLowestLocation(AlmanacWithSeedRange almanac) {
    long lowestLocation = Long.MIN_VALUE;
    for (SeedRange seedRange : almanac.seeds()) {
      if (seedRange == null) {
        break;
      }
      long offset = seedRange.offset;
      long range = seedRange.range;
      for (long seed = offset; seed <= offset + range - 1; seed++) {
        long mappedElement = seed;
        for (CategoryMap categoryMap : almanac.maps()) {
          for (Converter converter : categoryMap.converters()) {
            if (converter == null) {
              break;
            }
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
      if (seed == 0L) {
        break;
      }
      long mappedElement = seed;
      log.info("current seed: {}", seed);
      for (CategoryMap categoryMap : almanac.maps()) {
        log.info("current mapper: {}", categoryMap.name());
        long oldMappedElement = mappedElement;
        for (Converter converter : categoryMap.converters()) {
          if (converter == null) {
            break;
          }
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

  private static void setSeeds(String line, long[] seeds) {
    long[] seedsArray;
    if (line.contains("seeds:")) {
      seedsArray = Arrays.stream(line.split(": ")[1].split("\\s+")).mapToLong(Long::parseLong).toArray();
      System.arraycopy(seedsArray, 0, seeds, 0, seedsArray.length);
    }
  }

  private static void setCategoryMaps(String line, BufferedReader br, CategoryMap[] maps) throws IOException {
    setCategoryMap("seed-to-soil map", line, br, maps, 0);
    setCategoryMap("soil-to-fertilizer map", line, br, maps, 1);
    setCategoryMap("fertilizer-to-water map", line, br, maps, 2);
    setCategoryMap("water-to-light map", line, br, maps, 3);
    setCategoryMap("light-to-temperature map", line, br, maps, 4);
    setCategoryMap("temperature-to-humidity map", line, br, maps, 5);
    setCategoryMap("humidity-to-location map", line, br, maps, 6);
  }

  private static void setCategoryMap(String categoryMapName, String line, BufferedReader br, CategoryMap[] maps, int mapsIndex) throws IOException {
    if (line.contains(categoryMapName)) {
      CategoryMap soilToFertilizerMap = new CategoryMap(categoryMapName);
      int counter = 0;
      while ((line = br.readLine()) != null && !line.isEmpty()) {
        long[] converterArray = Arrays.stream(line.split("\\s+")).mapToLong(Long::parseLong).toArray();
        Converter converter = new Converter(converterArray[0], converterArray[1], converterArray[2]);
        soilToFertilizerMap.converters[counter++] = converter;
      }
      maps[mapsIndex] = soilToFertilizerMap;
    }
  }

  private record Converter(long destination, long source, long range) {
  }

  private record CategoryMap(String name, Converter[] converters) {
    public CategoryMap(String name) {
      this(name, new Converter[100]);
    }
  }

  private record Almanac(long[] seeds, CategoryMap[] maps) {
    public Almanac() {
      this(new long[100], new CategoryMap[7]);
    }
  }

  private record SeedRange(long offset, long range) {
  }

  private record AlmanacWithSeedRange(SeedRange[] seeds, CategoryMap[] maps) {
    public AlmanacWithSeedRange() {
      this(new SeedRange[100], new CategoryMap[7]);
    }
  }
}
