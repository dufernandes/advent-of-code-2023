package advent.of.code.year_2023.day07;

import advent.of.code.year_2023.day05.SeedFertilizer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static java.util.Map.entry;

@Slf4j
public class CamelCards {

  private static final String INPUT_FILE = "/year2023/day_07_input.txt";

  private static final Map<Character, Integer> CARD_STRENGTH = Map.ofEntries(
          entry('2', 0),
          entry('3', 1),
          entry('4', 2),
          entry('5', 3),
          entry('6', 4),
          entry('7', 5),
          entry('8', 6),
          entry('9', 7),
          entry('T', 8),
          entry('J', 9),
          entry('Q', 10),
          entry('K', 11),
          entry('A', 12)
  );

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new CamelCards().findLowestLocation());
      //log.info("The result of part two is: {}", new CamelCards().findLowestLocationForSeedRange());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  public long findLowestLocation() throws IOException {
    List<HandBid> handBids = new ArrayList<>();
    InputStream is = SeedFertilizer.class.getResourceAsStream(INPUT_FILE);

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        String hand = line.split("\\s+")[0];
        long bid = Long.parseLong(line.split("\\s+")[1]);
        HandType handType = getHandType(hand);
        handBids.add(new HandBid(handType, hand, bid));
      }
    }

    handBids.sort(new HandBidComparator(CARD_STRENGTH));

    long winnings = 0;
    int counter = 1;
    for (HandBid handBid : handBids) {
      winnings += counter * handBid.bid;
      counter++;
    }

    return winnings;
  }

  private static class HandBidComparator implements Comparator<HandBid> {

    private final Map<Character, Integer> cardStrength;

    public HandBidComparator(Map<Character, Integer> cardStrength) {
      this.cardStrength = cardStrength;
    }

    @Override
    public int compare(HandBid o1, HandBid o2) {

      if (o1.handType.strength == o2.handType.strength) {
        for (int i = 0; i < o1.hand.length(); i++) {
          int cardNumber1Strength = cardStrength.get(o1.hand.charAt(i));
          int cardNumber2Strength = cardStrength.get(o2.hand.charAt(i));
          if (cardNumber1Strength == cardNumber2Strength) {
            continue;
          }
          return cardNumber1Strength > cardNumber2Strength ? 1 : -1;
        }
        return 0;
      }

      return o1.handType.strength > o2.handType.strength ? 1 : -1;
    }
  }

  private HandType getHandType(String hand) {
    Map<Character, Integer> cards = getCardsFrequency(hand);

    if (cards.containsValue(5)) {
      return HandType.FiveOfKing;
    }

    if (cards.containsValue(4)) {
      return HandType.FourOfKing;
    }

    if (cards.containsValue(3) && cards.containsValue(2)) {
      return HandType.FullHouse;
    }

    if (cards.containsValue(3) && cards.containsValue(1)) {
      return HandType.ThreeOfKind;
    }

    if (cards.keySet().size() == 3 && cards.containsValue(2) && cards.containsValue(1)) {
      return HandType.TwoPair;
    }

    if (cards.keySet().size() == 4 && cards.containsValue(2) && cards.containsValue(1)) {
      return HandType.OnePair;
    }

    if (cards.keySet().size() == 5) {
      return HandType.HighCard;
    }

    throw new RuntimeException("hand not defined: " + hand);
  }

  private static Map<Character, Integer> getCardsFrequency(String hand) {
    Map<Character, Integer> cards = new HashMap<>();
    for (int i = 0; i < hand.length(); i++) {
      char card = hand.charAt(i);

      if (cards.containsKey(card)) {
        int count = cards.get(card);
        cards.put(card, ++count);
      } else {
        cards.put(card, 1);
      }
    }

    return cards;
  }

  @Getter
  private enum HandType {
    HighCard(0),
    OnePair(1),
    TwoPair(2),
    ThreeOfKind(3),
    FullHouse(4),
    FourOfKing(5),
    FiveOfKing(6);

    private final int strength;

    HandType(int strength) {
      this.strength = strength;
    }
  }

  private record HandBid(HandType handType, String hand, long bid) {}
}
