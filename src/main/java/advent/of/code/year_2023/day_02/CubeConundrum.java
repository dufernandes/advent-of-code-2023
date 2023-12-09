package advent.of.code.year_2023.day_02;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
public class CubeConundrum {

  private static final String INPUT_FILE = "/year2023/day_02_input.txt";
  private static final String COLUMN_DELIMITER = ":";
  private static final String SEMICOLON_DELIMITER = ";";
  private static final String COMMA_DELIMITER = ",";
  private static final String SPACE_DELIMITER = " ";

  static final int RED_CUBES_AVAILABLE = 12;
  static final int GREEN_CUBES_AVAILABLE = 13;
  static final int BLUE_CUBES_AVAILABLE = 14;

  private static final String RED = "red";
  private static final String GREEN = "green";
  private static final String BLUE = "blue";


  private static final int GAME_ID_INDEX = 1;
  private static final int ID_TEXT_INDEX = 0;
  private static final int GAME_INDEX = 1;
  public static final int CUBE_AMOUNT_INDEX = 1;
  public static final int CUBE_COLOR_INDEX = 2;

  public static void main(String[] args) {
    try {
      log.info("Part 1 result: {}", new CubeConundrum().getPossibleGamesSum(RED_CUBES_AVAILABLE, GREEN_CUBES_AVAILABLE, BLUE_CUBES_AVAILABLE));
      log.info("Part 2 result: {}", new CubeConundrum().getPowerSum());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  public long getPowerSum() throws IOException {

    long sum = 0;
    InputStream is = this.getClass().getResourceAsStream(INPUT_FILE);

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] idGames = line.split(COLUMN_DELIMITER);
        int gameId = getGameId(idGames[ID_TEXT_INDEX]);

        int maxRed = 0, maxGreen = 0, maxBlue = 0;
        String[] gamesText = idGames[GAME_INDEX].split(SEMICOLON_DELIMITER);
        for (String gameText : gamesText) {
          String[] cubesText = gameText.split(COMMA_DELIMITER);
          for (String cubeText : cubesText) {
            int cubeAmount = Integer.parseInt(cubeText.split(SPACE_DELIMITER)[CUBE_AMOUNT_INDEX]);
            String cubeColor = cubeText.split(SPACE_DELIMITER)[CUBE_COLOR_INDEX];
            switch (cubeColor) {
              case RED -> {
                if (cubeAmount > maxRed) {
                  maxRed = cubeAmount;
                }
              }
              case GREEN -> {
                if (cubeAmount > maxGreen) {
                  maxGreen = cubeAmount;
                }
              }
              case BLUE -> {
                if (cubeAmount > maxBlue) {
                  maxBlue = cubeAmount;
                }
              }
              default -> {
                log.error("no predicted column found {}", line);
              }
            }
            ;
          }
        }

        sum += (long) maxBlue * maxGreen * maxRed;

      }
    }

    return sum;
  }

  public long getPossibleGamesSum(int redCubesAvailable, int greenCubesAvailable, int blueCubesAvailable) throws IOException {

    long sum = 0;
    InputStream is = this.getClass().getResourceAsStream(INPUT_FILE);

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        boolean shouldSum = true;
        String[] idGames = line.split(COLUMN_DELIMITER);
        int gameId = getGameId(idGames[ID_TEXT_INDEX]);

        String[] gamesText = idGames[GAME_INDEX].split(SEMICOLON_DELIMITER);
        gameCheck:
        for (String gameText : gamesText) {
          String[] cubesText = gameText.split(COMMA_DELIMITER);
          for (String cubeText : cubesText) {
            int cubeAmount = Integer.parseInt(cubeText.split(SPACE_DELIMITER)[CUBE_AMOUNT_INDEX]);
            String cubeColor = cubeText.split(SPACE_DELIMITER)[CUBE_COLOR_INDEX];
            shouldSum = switch (cubeColor) {
              case RED -> cubeAmount <= redCubesAvailable;
              case GREEN -> cubeAmount <= greenCubesAvailable;
              case BLUE -> cubeAmount <= blueCubesAvailable;
              default -> {
                log.error("no predicted column found {}", line);
                yield false;
              }
            };

            if (!shouldSum) {
              break gameCheck;
            }
          }
        }

        if (shouldSum) {
          sum += gameId;
        }

      }
    }

    return sum;
  }

  private int getGameId(String idText) {
    return Integer.parseInt(idText.split(SPACE_DELIMITER)[GAME_ID_INDEX]);
  }
}
