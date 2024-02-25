package advent.of.code.year_2023.day19;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
public class Aplenty {

  private static final String INPUT_FILE = "/year2023/day_19_input.txt";

  public static void main(String[] args) {
    try {
      log.info("The result for part one is: {}", new Aplenty().calculateSum());
      //log.info("The result for part two is: {}", new Aplenty().calculateSumWithPartsRange());
    } catch (IOException ioe) {
      log.error("error while opening input file", ioe);
    }
  }

  private long calculateSum() throws IOException {
    WorkflowParts workflowParts = readWorkflowParts();

    Map<String, List<String>> workflows = workflowParts.workflows;
    List<Map<String, Long>> parts = workflowParts.parts;

    return calculateSum(parts, workflows);
  }

  private long calculateSumWithPartsRange() throws IOException {
    WorkflowParts workflowParts = readWorkflowParts();

    Map<String, List<String>> workflows = workflowParts.workflows;

    List<Map<String, Long>> parts = new ArrayList<>();

    for (long x = 1; x <= 4000; x++) {
      for (long m = 1; m <= 4000; m++) {
        for (long a = 1; a <= 4000; a++) {
          for (long s = 1; s <= 4000; s++) {

          }
        }
      }
    }

    return calculateSum(parts, workflows);
  }

  private long calculateSum(List<Map<String, Long>> parts, Map<String, List<String>> workflows) {
    long sum = 0;
    for (Map<String, Long> part : parts) {

      if (processWorkflow("in", part, workflows)) {
        sum += part.values().stream().mapToLong(Long::longValue).sum();
      }
    }

    return sum;
  }

  private boolean processWorkflow(String workflowName, Map<String, Long> parts, Map<String, List<String>> workflows) {

    List<String> workflow = workflows.get(workflowName);

    for (String rule : workflow) {
      if ("A".equals(rule)) {
        return true;
      }

      if ("R".equals(rule)) {
        return false;
      }

      if (!rule.contains(":")) {
        return processWorkflow(rule, parts, workflows);
      }

      String result = rule.split(":")[1];
      String equation = rule.split(":")[0];
      String rate = String.valueOf(equation.charAt(0));
      String operation = String.valueOf(equation.charAt(1));
      Long grade = Long.parseLong(equation.substring(2));
      Long gradeActual = parts.get(rate);

      if (">".equals(operation)) {
        if (gradeActual > grade) {
          return processResult(result, parts, workflows);
        }
      } else if ("<".equals(operation)) {
        if (gradeActual < grade) {
          return processResult(result, parts, workflows);
        }
      }
    }

    throw new RuntimeException("Something wrong with the workflow");
  }

  private boolean processResult(String result, Map<String, Long> parts, Map<String, List<String>> workflows) {
    if ("A".equals(result)) {
      return true;
    }

    if ("R".equals(result)) {
      return false;
    }

    return processWorkflow(result, parts, workflows);
  }

  private WorkflowParts readWorkflowParts() throws IOException {
    Map<String, List<String>> workflows = new HashMap<>();
    List<Map<String, Long>> parts = new ArrayList<>();
    WorkflowParts workflowParts = new WorkflowParts(workflows, parts);

    InputStream is = this.getClass().getResourceAsStream(INPUT_FILE);

    try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
      String line;
      boolean isReadingWorkflows = true;
      while ((line = br.readLine()) != null) {
        if (isReadingWorkflows) {
          if (line.isEmpty()) {
            isReadingWorkflows = false;
            continue;
          }

          String name = line.split("\\{")[0];
          List<String> rules = Arrays.stream(line.split("\\{")[1].replace("}", "").split(",")).toList();
          workflows.put(name, rules);
        } else {

          Map<String, Long> rateGrade = new HashMap<>();
          String[] rateGradeStrings = line.replace("{", "").replace("}", "").split(",");
          for (String rateGradeString : rateGradeStrings) {
            String rate = rateGradeString.split("=")[0];
            long grade = Long.parseLong(rateGradeString.split("=")[1]);
            rateGrade.put(rate, grade);
          }
          parts.add(rateGrade);
        }
      }
    }

    return  workflowParts;
  }

  private record WorkflowParts(Map<String, List<String>> workflows, List<Map<String, Long>> parts) {}
}
