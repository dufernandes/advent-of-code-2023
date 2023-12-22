package advent.of.code.year_2023.day08;

import advent.of.code.year_2023.day05.SeedFertilizer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
public class HauntedWasteland {

  private static final String INPUT_FILE_A = "/year2023/day_08_a_input.txt";
  private static final String INPUT_FILE_B = "/year2023/day_08_b_input.txt";

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new HauntedWasteland().getNumberOfSteps());
      log.info("The result of part two is: {}", new HauntedWasteland().getNumberOfStepsUntilAllEndUpWithZ());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  public long getNumberOfStepsUntilAllEndUpWithZ() throws IOException {

    long steps = 0;
    String instructions;
    Map<String, String[]> map = new HashMap<>();
    List<String> startingNodes = new ArrayList<>();

    instructions = getInstructionsAndMapAndStaringNodes(map, startingNodes);

    String[] currentNodes = startingNodes.toArray(new String[0]);
    long[] calculatedSteps = new long[currentNodes.length];

    for (int i = 0; i < calculatedSteps.length; i++) {
      calculatedSteps[i] = getStepsEndingWithZ(instructions, currentNodes[i], map);
    }


    return leastCommonMultiple(calculatedSteps);
  }

  private static long greatestCommonDivisor(long a, long b)
  {
    while (b > 0)
    {
      long temp = b;
      b = a % b; // % is remainder
      a = temp;
    }
    return a;
  }


  private static long leastCommonMultiple(long a, long b)
  {
    return a * (b / greatestCommonDivisor(a, b));
  }

  private static long leastCommonMultiple(long[] input)
  {
    long result = input[0];
    for(int i = 1; i < input.length; i++) result = leastCommonMultiple(result, input[i]);
    return result;
  }

  private static boolean finishWithZ(String[] currentNodes) {
    return Arrays.stream(currentNodes).allMatch(n -> n.endsWith("Z"));
  }

  public long getNumberOfSteps() throws IOException {
    String instructions;
    Map<String, String[]> map = new HashMap<>();

    instructions = getInstructionsAndMap(map);

    String currentNode = "AAA";

    return getSteps(instructions, currentNode, map);
  }

  private static long getStepsEndingWithZ(String instructions, String currentNode, Map<String, String[]> map) {
    long steps = 0;
    zzzFound:
    while (true) {
      for (int i = 0; i < instructions.length(); i++) {
        if ((currentNode.endsWith("Z"))) {
          break zzzFound;
        }
        char instruction = instructions.charAt(i);
        currentNode = switch (instruction) {
          case 'L' -> map.get(currentNode)[0];
          case 'R' -> map.get(currentNode)[1];
          default -> throw new RuntimeException("instruction not expected");
        };
        steps++;
      }
    }
    return steps;
  }

  private static long getSteps(String instructions, String currentNode, Map<String, String[]> map) {
    long steps = 0;
    zzzFound:
    while (true) {
      for (int i = 0; i < instructions.length(); i++) {
        if ("ZZZ".equals(currentNode)) {
          break zzzFound;
        }
        char instruction = instructions.charAt(i);
        currentNode = switch (instruction) {
          case 'L' -> map.get(currentNode)[0];
          case 'R' -> map.get(currentNode)[1];
          default -> throw new RuntimeException("instruction not expected");
        };
        steps++;
      }
    }
    return steps;
  }

  private static String getInstructionsAndMapAndStaringNodes(Map<String, String[]> map, List<String> startingNodes) throws IOException {
    String instructions;
    InputStream is = HauntedWasteland.class.getResourceAsStream(INPUT_FILE_B);

    List<String> nodeList = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line = br.readLine();
      instructions = line;
      br.readLine();
      while ((line = br.readLine()) != null) {
        String node = line.split("\\s+=\\s+")[0];
        String leftInstruction = line.split("\\s+=\\s+")[1].split(",\\s+")[0].substring(1);
        String rightInstruction = line.split("\\s+=\\s+")[1].split(",\\s+")[1].substring(0, 3);
        map.put(node, new String[]{leftInstruction, rightInstruction});
        if (node.endsWith("A")) {
          startingNodes.add(node);
        }
      }
    }
    return instructions;
  }

  private static String getInstructionsAndMap(Map<String, String[]> map) throws IOException {
    String instructions;
    InputStream is = HauntedWasteland.class.getResourceAsStream(INPUT_FILE_A);

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line = br.readLine();
      instructions = line;
      br.readLine();
      while ((line = br.readLine()) != null) {
        String node = line.split("\\s+=\\s+")[0];
        String leftInstruction = line.split("\\s+=\\s+")[1].split(",\\s+")[0].substring(1);
        String rightInstruction = line.split("\\s+=\\s+")[1].split(",\\s+")[1].substring(0, 3);
        map.put(node, new String[]{leftInstruction, rightInstruction});
      }
    }
    return instructions;
  }
}
