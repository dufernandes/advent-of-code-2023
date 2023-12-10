package advent.of.code.year_2023.day_04;

import advent.of.code.year_2023.day_03.GearRatios;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
public class Scratchcards {

  private static final String INPUT_FILE = "/year2023/day_04_input.txt";

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new Scratchcards().getScratchcardsPoints());
      log.info("The result of part two is: {}", new Scratchcards().getNumberOfScoreCards());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  public long getNumberOfScoreCards() throws IOException {
    InputStream is = GearRatios.class.getResourceAsStream(INPUT_FILE);

    Map<Integer, Integer> numberOfCards = new HashMap<>();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        int currentCardNumber = Integer.parseInt(line.split(":")[0].split("\\s+")[1]);
        List<String> winningNumbers = Arrays.stream(line.split("\\s+\\|\\s+")[0].split(":\\s+")[1].split("\\s+")).toList();
        List<String> myNumbers = Arrays.stream(line.split("\\s+\\|\\s+")[1].split("\\s+")).toList();

        incrementNumberOfCardsInMap(numberOfCards, currentCardNumber);

        int points = winningNumbers.stream()
                .reduce(0, (partialSum, wNumber)
                        -> myNumbers.contains(wNumber) ? partialSum + 1 : partialSum,
                        Integer::sum);

        int numberOfCurrentCard = numberOfCards.get(currentCardNumber);

        for (int i = currentCardNumber + 1; i <= currentCardNumber + points; i++) {
          incrementNumberOfCardsInMap(numberOfCards, i, numberOfCurrentCard);
        }
      }
    }
    return numberOfCards.values().stream().reduce(0, Integer::sum);
  }

  private static void incrementNumberOfCardsInMap(Map<Integer, Integer> numberOfCards, int currentCard) {
    incrementNumberOfCardsInMap(numberOfCards, currentCard, 1);
  }

  private static void incrementNumberOfCardsInMap(Map<Integer, Integer> numberOfCards, int currentCard, int incrementAmount) {
    if (numberOfCards.containsKey(currentCard)) {
      numberOfCards.put(currentCard, numberOfCards.get(currentCard) + incrementAmount);
    } else {
      numberOfCards.put(currentCard, incrementAmount);
    }
  }

  public long getScratchcardsPoints() throws IOException {
    long sum = 0;
    InputStream is = GearRatios.class.getResourceAsStream(INPUT_FILE);

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        List<String> winningNumbers = Arrays.stream(line.split("\\s+\\|\\s+")[0].split(":\\s+")[1].split("\\s+")).toList();
        List<String> myNumbers = Arrays.stream(line.split("\\s+\\|\\s+")[1].split("\\s+")).toList();

        int points = 0;
        for (String winningNumber : winningNumbers) {
          if (myNumbers.contains(winningNumber)) {
            if (points == 0) {
              points = 1;
            } else {
              points *= 2;
            }
          }
        }

        sum += points;
      }
    }

    return sum;
  }
}
