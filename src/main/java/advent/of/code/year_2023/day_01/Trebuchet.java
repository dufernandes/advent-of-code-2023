package advent.of.code.year_2023.day_01;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
public class Trebuchet {

  private static final String[] WRITTEN_NUMBERS = new String[]{"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
  private static final String INPUT_FILE_A = "/year2023/day_01_a_input.txt";
  private static final String INPUT_FILE_B = "/year2023/day_01_b_input.txt";

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new Trebuchet().calibrationSumPartOne());
      log.info("The result for part two is: {}", new Trebuchet().calibrationSumPartTwo());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  public long calibrationSumPartTwo() throws IOException {

    long sum = 0, current = 0, lineNumber = 0;

    InputStream is = this.getClass().getResourceAsStream(INPUT_FILE_B);

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        Digits numberDigits = findNumberDigits(line);
        Digits writtenDigits = findWrittenDigits(line);

        Digits digits = calculateFinalDigits(numberDigits, writtenDigits);

        if (isDigitValid(digits.leftDigit) && isDigitValid(digits.rightDigit)) {
          lineNumber++;
          current = digits.rightDigit + (digits.leftDigit * 10L);
          sum += current;
          log.info("{}: line: {}, current: {}, sum: {}", lineNumber, line, current, sum);
        } else {
          lineNumber++;
          log.error("{}: case skipped: line: {}, left number: {}, right number: {}", lineNumber, line, digits.leftDigit, digits.rightDigit);
        }
      }
    }


    return sum;
  }

  private Digits calculateFinalDigits(Digits numberDigits, Digits writtenDigits) {
    int lowerLeftIndex, higherRightIndex;
    int leftNumber, rightNumber;

    if (isDigitInvalid(numberDigits.leftDigitIndex)) {
      lowerLeftIndex = writtenDigits.leftDigitIndex;
      leftNumber = writtenDigits.leftDigit;
    } else if (isDigitInvalid(writtenDigits.leftDigitIndex)) {
      lowerLeftIndex = numberDigits.leftDigitIndex;
      leftNumber = numberDigits.leftDigit;
    } else if (numberDigits.leftDigitIndex < writtenDigits.leftDigitIndex) {
      lowerLeftIndex = numberDigits.leftDigitIndex;
      leftNumber = numberDigits.leftDigit;
    } else {
      lowerLeftIndex = writtenDigits.leftDigitIndex;
      leftNumber = writtenDigits.leftDigit;
    }

    if (isDigitInvalid(numberDigits.rightDigitIndex)) {
      higherRightIndex = writtenDigits.rightDigitIndex;
      rightNumber = writtenDigits.rightDigit;
    } else if (isDigitInvalid(writtenDigits.rightDigitIndex)) {
      higherRightIndex = numberDigits.rightDigitIndex;
      rightNumber = numberDigits.rightDigit;
    } else if (numberDigits.rightDigitIndex > writtenDigits.rightDigitIndex) {
      higherRightIndex = numberDigits.rightDigitIndex;
      rightNumber = numberDigits.rightDigit;
    } else {
      higherRightIndex = writtenDigits.rightDigitIndex;
      rightNumber = writtenDigits.rightDigit;
    }

    return new Digits(leftNumber, lowerLeftIndex, rightNumber, higherRightIndex);
  }

  private Digits findWrittenDigits(String line) {
    int leftDigit = Integer.MIN_VALUE, rightDigit = Integer.MIN_VALUE;
    int leftDigitIndex = Integer.MIN_VALUE, rightDigitIndex = Integer.MIN_VALUE;
    for (int number = 0; number < WRITTEN_NUMBERS.length; number++) {

      int leftOccurrenceIdex = 0;
      if ((leftOccurrenceIdex = line.toLowerCase().indexOf(WRITTEN_NUMBERS[number])) != -1) {
          if (isDigitInvalid(leftDigitIndex)) {
            leftDigit = number;
            leftDigitIndex = leftOccurrenceIdex;
          } else if (leftOccurrenceIdex < leftDigitIndex) {
            leftDigit = number;
            leftDigitIndex = leftOccurrenceIdex;
          }
      }

      int rightOccurrenceIndex = 0;
      if ((rightOccurrenceIndex = line.toLowerCase().lastIndexOf(WRITTEN_NUMBERS[number])) != -1) {
        if (isDigitInvalid(rightDigitIndex)) {
          rightDigit = number;
          rightDigitIndex = rightOccurrenceIndex;
        } else if (rightOccurrenceIndex > rightDigitIndex) {
          rightDigit = number;
          rightDigitIndex = rightOccurrenceIndex;
        }
      }
    }

    return new Digits(leftDigit, leftDigitIndex, rightDigit, rightDigitIndex);
  }

  public long calibrationSumPartOne() throws IOException {

    long sum = 0, current = 0, lineNumber = 0;

    InputStream is = this.getClass().getResourceAsStream(INPUT_FILE_A);

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        Digits digits = findNumberDigits(line);

        if (isDigitValid(digits.leftDigit) && isDigitValid(digits.rightDigit)) {
          lineNumber++;
          current = digits.rightDigit + (digits.leftDigit * 10L);
          sum += current;
          log.info("{}: line: {}, current: {}, sum: {}", lineNumber, line, current, sum);
        } else {
          lineNumber++;
          log.error("{}: case skipped: line: {}, left number: {}, right number: {}", lineNumber, line, digits.leftDigit, digits.rightDigit);
        }
      }
    }

    return sum;
  }

  private Digits findNumberDigits(String line) {
    int leftDigit = Integer.MIN_VALUE, rightDigit = Integer.MIN_VALUE;
    int i = 0, j = line.length() - 1;

    while (i <= j && (isDigitInvalid(leftDigit) || isDigitInvalid(rightDigit))) {
      if (Character.isDigit(line.charAt(i))) {
        leftDigit = Character.digit(line.charAt(i), 10);
      } else {
        i++;
      }
      if (Character.isDigit(line.charAt(j))) {
        rightDigit = Character.digit(line.charAt(j), 10);
      } else {
        j--;
      }
    }
    return new Digits(leftDigit, i, rightDigit, j);
  }

  private boolean isDigitValid(int digit) {
    return digit > Integer.MIN_VALUE;
  }

  private boolean isDigitInvalid(int digit) {
    return digit == Integer.MIN_VALUE;
  }

  private record Digits(int leftDigit, int leftDigitIndex, int rightDigit, int rightDigitIndex) {}
}
