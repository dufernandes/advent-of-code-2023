package advent.of.code.year_2023.day15;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;

@Slf4j
public class LensLibrary {

  private static final String INPUT_FILE = "/year2023/day_15_input.txt";

  private static final char DASH_OPERATION = '-';
  private static final char EQUALS_OPERATION = '=';

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new LensLibrary().getSumOfHashes());
      log.info("The result for part two is: {}", new LensLibrary().getSumOfFocusingPower());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  long getSumOfHashes() throws IOException {
    long sum = 0;
    InputStream is = this.getClass().getResourceAsStream(INPUT_FILE);

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] steps = Arrays.stream(line.split(",")).filter(s -> !"\n".equals(s)).toList().toArray(new String[0]);
        for (String step : steps) {
          int hash = calculateHash(step);
          sum += hash;
        }
      }
    }
    return sum;
  }

  long getSumOfFocusingPower() throws IOException {
    long sum = 0;
    Box[] boxes = instantiateBoxes();

    InputStream is = this.getClass().getResourceAsStream(INPUT_FILE);

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] steps = Arrays.stream(line.split(",")).filter(s -> !"\n".equals(s)).toList().toArray(new String[0]);
        for (String step : steps) {

          if (step.contains(String.valueOf(DASH_OPERATION))) {
            executeDashOperation(step, boxes);
          } else {
            executeEqualsOperation(step, boxes);
          }
        }
      }
    }

    sum = sumFocusingPower(boxes, sum);

    return sum;
  }

  /*
  For the sum of focusing power, the following applies:

    To confirm that all of the lenses are installed correctly, add up the focusing power of
    all of the lenses. The focusing power of a single lens is the result of multiplying together:

    - One plus the box number of the lens in question.
    - The slot number of the lens within the box: 1 for the first lens, 2 for the second lens, and so on.
    - The focal length of the lens.

   */
  private static long sumFocusingPower(Box[] boxes, long sum) {
    for (int i = 0; i < boxes.length; i++) {
      LinkedList<LensSlot> lensSlots = boxes[i].lensSlots;
      for (int j = 0; j < lensSlots.size(); j++) {
        LensSlot lensSlot = lensSlots.get(j);
        sum += (long) (1 + i) * (j + 1) * lensSlot.focalLens;
      }
    }
    return sum;
  }

  /*
  For the equals operation, the following rules apply:

  If the operation character is an equals sign (=),
  it will be followed by a number indicating the focal length of the lens that needs to go into the relevant box; be sure to use the label maker to mark the lens with the label given in the beginning of the step so you can find it later.
  There are two possible situations:

    - If there is already a lens in the box with the same label,
    replace the old lens with the new lens: remove the old lens and put the new lens in its place,
    not moving any other lenses in the box.
    - If there is not already a lens in the box with the same label,
    add the lens to the box immediately behind any lenses already in the box. Don't move any of the other
     lenses when you do this. If there aren't any lenses in the box, the new lens goes all the way to the front of the box.

   */
  private static void executeEqualsOperation(String step, Box[] boxes) {
    String label = step.split(String.valueOf(EQUALS_OPERATION))[0];
    int hash = calculateHash(label);
    int focalLens = Character.getNumericValue(step.charAt(step.length() - 1));

    LinkedList<LensSlot> lensSlots = boxes[hash].lensSlots;
    findLensByLabel(lensSlots, label).
            ifPresentOrElse(
                    lens -> lens.focalLens = focalLens,
                    () -> lensSlots.add(new LensSlot(label, focalLens)));
  }

  /*
  For the dash operation, the following applies:

  If the operation character is a dash (-), go to the relevant box and remove the lens with the given label
  if it is present in the box. Then, move any remaining lenses as far forward in the box as they can go
  without changing their order, filling any space made by removing the indicated lens. (If no lens in
  that box has the given label, nothing happens.)


   */
  private static void executeDashOperation(String step, Box[] boxes) {
    String label = step.split(String.valueOf(DASH_OPERATION))[0];
    int hash = calculateHash(label);

    LinkedList<LensSlot> lensSlots = boxes[hash].lensSlots;
    findLensByLabel(lensSlots, label).ifPresent(lensSlots::remove);
  }

  private static Optional<LensSlot> findLensByLabel(LinkedList<LensSlot> lensSlots, String label) {
   return lensSlots.stream().filter(lens -> lens.label.equals(label)).findFirst();
  }

  private static Box[] instantiateBoxes() {
    Box[] boxes = new Box[256];
    for (int i = 0; i < boxes.length; i++) {
      boxes[i] = new Box(new LinkedList<>());
    }
    return boxes;
  }

  /*
    Rules for calculating the hash

    - Determine the ASCII code for the current character of the string.
    - Increase the current value by the ASCII code you just determined.
    - Set the current value to itself multiplied by 17.
    - Set the current value to the remainder of dividing itself by 256.

   */
  private static int calculateHash(String step) {
    int hash = 0;
    for (char value : step.toCharArray()) {
      hash += (int) value;
      hash *= 17;
      hash = hash % 256;
    }
    return hash;
  }

  private record Box(LinkedList<LensSlot> lensSlots) {}

  @AllArgsConstructor
  @NoArgsConstructor
  private static class LensSlot {
    private String label;
    private int focalLens;
  }
}
