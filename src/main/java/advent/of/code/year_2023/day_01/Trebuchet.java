package advent.of.code.year_2023.day_01;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
public class Trebuchet {

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}: ", new Trebuchet().calibrationSumPartOne());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  public long calibrationSumPartOne() throws IOException {

    long sum = 0, current = 0, lineNumber = 0;

    InputStream is = this.getClass().getResourceAsStream("/year2023_day_01/input.txt");

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        Digits digits = findDigits(line);

        if (isDigitValid(digits.leftDigit) && isDigitValid(digits.rightDigit)) {
          lineNumber++;
          current = digits.rightDigit + (digits.leftDigit * 10L);
          sum += current;
          log.info("{}: line: {}, current: {}, sum: {}", lineNumber, line, current, sum);
        } else if (isDigitValid(digits.leftDigit)) {
          lineNumber++;
          current = digits.leftDigit + (digits.leftDigit * 10L);
          sum += current;
          log.info("{}: line: {}, current: {}, sum: {}", lineNumber, line, current, sum);
        } else if (isDigitValid(digits.rightDigit)) {
          lineNumber++;
          current = digits.rightDigit + (digits.rightDigit * 10L);
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

  private Digits findDigits(String line) {
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
    return new Digits(leftDigit, rightDigit);
  }

  private boolean isDigitValid(int digit) {
    return digit > Integer.MIN_VALUE;
  }

  private boolean isDigitInvalid(int digit) {
    return digit == Integer.MIN_VALUE;
  }

  private static class Digits {
    public final int leftDigit;
    public final int rightDigit;

    public Digits(int leftDigit, int rightDigit) {
      this.leftDigit = leftDigit;
      this.rightDigit = rightDigit;
    }
  }
}
