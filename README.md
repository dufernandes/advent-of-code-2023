# Advent of Code 2023 — Java Solutions

![Build](https://github.com/dufernandes/advent-of-code-2023/actions/workflows/maven.yml/badge.svg?branch=master)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![Java](https://img.shields.io/badge/Java-21-blue?logo=openjdk)
![Maven](https://img.shields.io/badge/Build-Maven-red?logo=apachemaven)

Solutions to [Advent of Code 2023](https://adventofcode.com/2023) puzzles, written in Java. Each day is solved end-to-end, with an emphasis on understanding and applying the right algorithm for the problem — not just getting to the answer.

Several days include **two implementations**: a brute-force first attempt and an optimised second solution, left side-by-side to show the reasoning process and the cost of choosing the wrong approach at scale.

---

## Running

```bash
git clone https://github.com/dufernandes/advent-of-code-2023.git
cd advent-of-code-2023
mvn clean verify
```

Each day has a `main()` method. Run any class directly from your IDE or via Maven to see the results for both parts.

---

## Table of Contents

| Day | Title | Key Algorithm / Technique |
|---|---|---|
| [01](#day-01--trebuchet) | Trebuchet | Two-pointer scan, string search |
| [02](#day-02--cube-conundrum) | Cube Conundrum | Linear scan, min-max tracking |
| [03](#day-03--gear-ratios) | Gear Ratios | 2D grid, adjacency scan, custom data model |
| [04](#day-04--scratchcards) | Scratchcards | Set intersection, dynamic card multiplication |
| [05](#day-05--seed-fertilizer) | Seed Fertilizer | Interval mapping, pipeline traversal |
| [06](#day-06--wait-for-it) | Wait For It | Quadratic brute-force |
| [07](#day-07--camel-cards) | Camel Cards | Frequency analysis, custom comparator sort |
| [08](#day-08--haunted-wasteland) | Haunted Wasteland | Graph traversal, LCM |
| [09](#day-09--mirage-maintenance) | Mirage Maintenance | Finite differences, triangle extrapolation |
| [10](#day-10--pipe-maze) | Pipe Maze | DFS on a 2D graph, Even-Odd rule (ray casting) |
| [11](#day-11--cosmic-expansion) | Cosmic Expansion | Coordinate remapping, Manhattan distance |
| [12](#day-12--hot-springs) | Hot Springs | Permutation (brute-force) + top-down DP with memoisation |
| [13](#day-13--point-of-incidence) | Point of Incidence | Mirror detection, smudge correction |
| [14](#day-14--parabolic-reflector-dish) | Parabolic Reflector Dish | Simulation, cycle detection |
| [15](#day-15--lens-library) | Lens Library | Custom hash function, linked-list buckets |
| [16](#day-16--the-floor-will-be-lava) | The Floor Will Be Lava | Recursive DFS with directional memoisation |
| [17](#day-17--clumsy-crucible) | Clumsy Crucible | Modified Dijkstra with step constraints |
| [18](#day-18--lavaduct-lagoon) | Lavaduct Lagoon | Shoelace formula + Pick's theorem |
| [19](#day-19--aplenty) | Aplenty | Recursive rule evaluation |

---

## Day 01 — Trebuchet

**Problem:** Extract the first and last digit from each line of text (part 1), then repeat with spelled-out numbers also counting as digits (part 2). Sum all two-digit numbers.

**Part 1 — Two-pointer scan:** Starting pointers at both ends of each string, scan inward until a digit is found at each side. The two found digits form the two-digit number. This is `O(n)` per line where `n` is the line length — a single pass.

**Part 2 — String search with index comparison:** Spelled-out numbers (`"one"`, `"two"`, ...) are found using `indexOf` and `lastIndexOf` for each word. The results are then merged with the digit scan results by comparing character positions — whichever occurrence appears furthest left wins as the first digit, and furthest right wins as the last.

The subtlety is that overlapping words like `"eighthree"` must be handled correctly — `indexOf`/`lastIndexOf` naturally handles this since they find the earliest and latest occurrence independently.

**Complexity:** O(n × m) per line, where m is the number of spelled-out words (fixed at 10). Effectively linear.

---

## Day 02 — Cube Conundrum

**Problem:** A series of cube draws from a bag. Part 1: determine which games are possible given a fixed number of red, green, and blue cubes. Part 2: find the minimum number of cubes of each colour needed per game, and sum the products.

**Approach:** Single-pass linear scan over each game's draws. For part 1, a `break gameCheck` label exits nested loops immediately when a draw exceeds the available count — an early-exit optimisation. For part 2, three running maximums (`maxRed`, `maxGreen`, `maxBlue`) are maintained per game and multiplied at the end.

Java's `switch` expression with arrow syntax is used cleanly here — each colour maps to a comparison with a yield, making the logic compact without fall-through risk.

**Complexity:** O(n × g × c) where n = number of games, g = draws per game, c = cubes per draw. All effectively constant in practice.

---

## Day 03 — Gear Ratios

**Problem:** Parse a 2D schematic of digits, symbols, and dots. Part 1: sum all numbers adjacent to any symbol. Part 2: find all `*` symbols adjacent to exactly two numbers and sum the products of those pairs.

**Approach:** The grid is parsed into a typed data model: `PartNumber`, `Asterisk`, and `EmptyData` objects fill a `Data[][]` matrix. A `Stack<Integer>` accumulates digits left-to-right; when a digit sequence ends, the stack is popped and the number is reconstructed in O(digits) time.

**Part 1** checks all 8 neighbours of each digit cell for a non-digit, non-dot symbol.

**Part 2** iterates over stored `Asterisk` positions and checks a 3×3 neighbourhood. Crucially, multi-digit numbers that span several cells are represented by a **single `PartNumber` object** shared across all their cells — so inserting neighbours into a `HashSet<PartNumber>` deduplicates them automatically. If the set reaches size 2, a gear is found.

This shared-object trick is the key insight of the solution: it avoids double-counting digits from the same number without needing to track which cells belong to which number separately.

**Complexity:** O(n × m) for an n × m grid.

---

## Day 04 — Scratchcards

**Problem:** Each card has winning numbers and your numbers. Part 1: points double for each match (1, 2, 4, 8...). Part 2: matching numbers on card N win copies of cards N+1, N+2, ... rather than points.

**Part 1:** Straightforward intersection — for each winning number that appears in your numbers, double the points. O(w × y) per card where w = winning numbers, y = your numbers.

**Part 2:** A `HashMap<Integer, Integer>` tracks how many copies of each card exist. Processing cards in order, each copy of card N adds its count to the counts of cards N+1 through N+matches. This is a forward-propagation approach that processes each card exactly once regardless of how many copies exist — a key insight that avoids simulating each copy individually.

Without this approach, the naive simulation would explode combinatorially. With it: O(n × m) where n = cards and m = max matches per card.

---

## Day 05 — Seed Fertilizer

**Problem:** Seeds must be mapped through a seven-stage pipeline (seed → soil → fertilizer → water → light → temperature → humidity → location) using ranges. Part 1: map individual seeds. Part 2: seeds are given as ranges — find the lowest output location.

**Part 1 — Interval pipeline:** For each seed, walk it through each `CategoryMap` in sequence. Each map contains `Converter` ranges; if the value falls within a converter's source range, apply the offset to destination. O(seeds × stages × converters).

**Part 2 — Brute force over ranges:** Each seed range is enumerated individually and passed through the pipeline. This is the brute-force approach — it works but is slow for large ranges. An `O(seeds)` range-splitting approach would be more efficient (splitting ranges at converter boundaries rather than enumerating every value), but the brute-force solution is kept here alongside the single-seed solution to show the contrast.

This day also ships a `SeedFertilizerWithArrays.java` — an alternative implementation using arrays instead of records for comparison.

---

## Day 06 — Wait For It

**Problem:** A toy boat race: you hold a button for `t` milliseconds, then the boat travels at speed `t` for the remaining `(total_time - t)` milliseconds. Find how many values of `t` beat the record distance.

**Approach:** Brute-force enumeration over all button-press durations. The distance function `d(t) = t × (total - t)` is a downward parabola, so winning values form a contiguous range. Finding the range boundaries analytically with the quadratic formula would give O(1), but the brute-force O(n) loop over all milliseconds is fast enough for the input sizes given and clearly expresses the problem structure.

Part 2 concatenates all times and distances into single large numbers (removing spaces from the input), then applies the same loop with `long` arithmetic.

---

## Day 07 — Camel Cards

**Problem:** Rank poker-style hands by type (five-of-a-kind down to high card), then by card-by-card strength for ties. Total winnings = sum of bid × rank.

**Frequency analysis:** Each hand is converted to a frequency map (`char → count`). Hand type is determined by which frequency values appear: a `5` means five-of-a-kind, `4` means four-of-a-kind, `{3,2}` means full house, and so on.

**Custom comparator:** `HandBidComparator` sorts by hand type strength first, then walks cards left-to-right for tiebreaking, using a `Map<Character, Integer>` for card-strength lookup.

**Part 2 — Joker rule:** Jokers (`J`) are wildcards. The strategy: find the most frequent non-joker card in the hand and replace all jokers with it before computing the hand type. This is implemented in `replaceJoker()`, which handles the edge case where `J` itself is the most frequent card by finding the next most frequent.

The two strength maps (`CARD_STRENGTH` vs `CARD_STRENGTH_WITH_JOKER`) demote `J` from rank 9 to rank 0 for tiebreaking in part 2.

**Complexity:** O(n log n) for sorting.

---

## Day 08 — Haunted Wasteland

**Problem:** Navigate a graph using a repeating instruction string (L/R). Part 1: how many steps from AAA to ZZZ? Part 2: starting simultaneously from all nodes ending in A, how many steps until all paths simultaneously land on nodes ending in Z?

**Part 1 — Direct graph traversal:** Build a `HashMap<String, String[]>` (node → [left, right]). Walk the instruction string cyclically until ZZZ is reached.

**Part 2 — LCM insight:** Simulating all paths simultaneously would take astronomically long. The key observation is that each starting node cycles to its `Z`-node with a fixed period. The answer is therefore the **Least Common Multiple** of all individual cycle lengths.

LCM is computed using the identity `lcm(a,b) = a × (b / gcd(a,b))`, with GCD via Euclid's algorithm. Applied iteratively across all cycle lengths.

This is a significant step up in mathematical reasoning from the earlier days — the brute-force approach is technically correct but computationally infeasible, and recognising the cyclic structure is the insight that unlocks the solution.

**Complexity:** O(steps × instruction_length) per path, then O(n log max) for LCM.

---

## Day 09 — Mirage Maintenance

**Problem:** Given sequences of numbers, extrapolate the next value (part 1) and the previous value (part 2) by repeatedly computing differences until all zeros.

**Finite differences — a numerical analysis technique:** This is the method of finite differences, used in polynomial interpolation. For a sequence generated by a polynomial of degree k, taking differences k times produces all zeros. The next value is extrapolated by summing the last elements of each difference row; the previous value by alternating subtraction from the first elements.

The implementation uses a pre-allocated 2D array `predictionDataset[depth][length]` rather than lists, avoiding allocation overhead. The inner loop shortens by one each level (`innerLength--`), reflecting the shrinking difference sequences.

**Part 2** uses the same triangle but processes the first column instead of the last, with subtraction rather than addition: `prediction = row[0] - prediction` (accumulated from the bottom up).

**Complexity:** O(n²) per line where n is the sequence length. In practice sequences are short, so this is fast.

---

## Day 10 — Pipe Maze

**Problem:** A 2D grid of pipe characters (`|`, `-`, `L`, `J`, `7`, `F`, `S`, `.`). Part 1: find the farthest point from `S` in the pipe loop. Part 2: count tiles enclosed by the loop.

**Graph construction:** The grid is converted to an adjacency-list graph. Each non-dot cell becomes a vertex; edges are added based on which pipe types connect in which directions. The `S` tile's connections are inferred by checking which adjacent pipes point back toward it.

To avoid separate ID tracking, vertex names are stored directly in a `char[][][2]` array (one layer for pipe type, one for vertex index), encoding up to 65535 vertices using `char` as an integer.

**Part 1 — Non-recursive DFS:** A standard DFS using an explicit `Stack<Integer>` (to avoid stack overflow on large inputs) marks all reachable vertices. The cycle size is the number of marked vertices, and the farthest point is `cycleSize / 2`.

**Part 2 — Even-Odd rule (ray casting):** For each non-loop tile, cast a ray rightward and count how many loop edges it crosses. If the count is odd, the tile is inside the loop. This is the **Point-in-Polygon** theorem applied to a grid.

The crossing count must only count edges that actually cross the ray — vertical pipes (`|`) and certain bends (`F`, `7`, `S` inferred as appropriate) count, while horizontal pipes (`-`) and bends that don't cross (`J`, `L`) do not. The `S` tile's actual pipe type is inferred from its graph connections.

**Complexity:** O(n × m) for the grid, O(V + E) for DFS.

---

## Day 11 — Cosmic Expansion

**Problem:** A 2D image of galaxies (`#`) and space (`.`). Empty rows and columns expand (×2 for part 1, ×1,000,000 for part 2). Sum the Manhattan distances between all pairs of galaxies.

**Two implementations are included:**

`CosmicExpansionWithGraphAndExpandingMatrix.java` — the first approach. Actually expands the matrix by inserting rows/columns, then runs BFS on a graph to compute distances. Works for part 1 but is completely infeasible for part 2 (a 1,000,000× expansion would require a billion-row matrix).

`CosmicExpansionOptimized.java` — the correct approach. Instead of expanding the matrix, galaxy coordinates are **remapped** in a single pass. For each empty row/column encountered, the expansion multiplier is added to the running coordinate offset rather than physically inserting rows. This makes the expansion size a parameter: `sumOfLengths(2)` for part 1, `sumOfLengths(1_000_000)` for part 2 — same code, instant results.

After remapping, all pair distances are computed as Manhattan distance: `|x1 - x2| + |y1 - y2|`.

**Complexity:** O(n × m) for coordinate remapping, O(g²) for pair distances where g = number of galaxies.

---

## Day 12 — Hot Springs

**Problem:** Rows of springs: `.` (operational), `#` (damaged), `?` (unknown). A list of group sizes describes the contiguous runs of damaged springs. Count all valid arrangements of `?` characters. Part 2 unfolds each row ×5.

**Two implementations included — this is the most algorithmically rich day:**

**Permutation / brute-force** (`sumArrangementsUsingPermutation`): Replace each `?` recursively with `.` or `#`, check validity when no unknowns remain. Exponential in the number of `?`s — completely infeasible for part 2 where folding ×5 explodes the search space.

**Top-down dynamic programming with memoisation** (`sumArrangementsUsingDynamicProgrammingAndCache`): Two base cases drive the recursion:
- Empty spring string with no remaining groups → 1 valid arrangement
- Empty groups with no remaining damaged springs → 1 valid arrangement

Two recursive cases:
1. If current char is `.` or `?` (treated as `.`): skip it, recurse on the rest of the string with groups unchanged
2. If current char is `#` or `?` (treated as `#`): attempt to match the next group. The match succeeds if the next N characters contain no `.` and the character after the group is not `#`. If matched, recurse with the remaining string and remaining groups.

Results are memoised in a `HashMap<SpringsSpringInfos, Long>` keyed on (remaining string, remaining groups). The key requires a custom `equals`/`hashCode` using `Arrays.equals` for the int array component.

**Complexity:** Brute-force: O(2^k) where k = number of `?`s. DP: O(n × m) where n = string length, m = number of groups — polynomial thanks to memoisation.

---

## Day 13 — Point of Incidence

**Problem:** Grids of `#` and `.`. Find the line of reflection (horizontal or vertical mirror) in each grid. Part 2: exactly one character in each grid can be flipped (a "smudge") to reveal a different mirror line.

**Mirror detection:** For each candidate row (or column), verify that rows above and below it are equal, expanding outward until one side hits the boundary. A helper method extracts columns as `char[]` arrays for symmetric treatment of rows and columns.

**Part 2 — smudge correction:** The original mirror line is detected and stored to be excluded. Then every cell in the grid is flipped one at a time (`#` ↔ `.`), and mirror detection is re-run with the original line excluded. The first flip that reveals a new mirror line is the smudge.

This brute-force search is O(n × m × (n + m)) per grid — tolerable given typical AoC grid sizes.

---

## Day 14 — Parabolic Reflector Dish

**Problem:** A grid of rounded rocks (`O`), cube rocks (`#`), and spaces (`.`). Tilt the platform in a direction and rocks slide until blocked. Part 1: tilt north, calculate load. Part 2: run 1,000,000,000 tilt cycles (N/W/S/E), calculate final load.

**Tilt mechanics:** Rocks slide in the tilt direction until they hit a cube rock, another rounded rock, or the edge. A `Stack<Integer>` tracks the most recent "blocker" position per column (or row), and each rounded rock slides to just past the top of the stack. Four directional variants (`tiltNorth`, `tiltSouth`, `tiltEast`, `tiltWest`) are implemented symmetrically.

**Part 2 — Cycle detection:** Running 1,000,000,000 full cycles is infeasible. The key insight: the platform state must eventually repeat (finite grid, deterministic tiling). Cache each state → next state. When a cache hit occurs, the cycle length is measured. Then:

```
remaining_steps = (total_cycles - cycles_run - cycle_length) % cycle_length
```

This formula computes how many more steps through the cached cycle are needed to land at the final state without iterating further. A custom `CacheKey` record with `Arrays.deepEquals` and `Arrays.deepHashCode` handles 2D array equality correctly in the HashMap.

**Complexity:** O(n × m) per cycle, O(cycle_length) to detect the period, O(1) after detection.

---

## Day 15 — Lens Library

**Problem:** Part 1: compute a custom hash of comma-separated strings. Part 2: use the hash as a bucket index to implement a 256-slot hashmap of lenses, processing insert/replace (`=`) and remove (`-`) operations.

**The hash function:** For each character: `current = (current + ascii) * 17 % 256`. This is a simple polynomial rolling hash modulo 256.

**Part 2 — HashMap from scratch:** 256 `Box` objects, each containing a `LinkedList<LensSlot>`. The `-` operation removes a lens by label; the `=` operation replaces a matching lens in-place or appends it. The focusing power is summed across all boxes with `(1 + box_index) × slot_position × focal_length`.

This is a direct implementation of the underlying data structure that Java's `HashMap` is built on — open-addressing or chaining with a hash function — making it an instructive exercise.

**Complexity:** O(n × s) where n = number of steps, s = average lenses per box. Effectively linear.

---

## Day 16 — The Floor Will Be Lava

**Problem:** A grid of mirrors (`/`, `\`), splitters (`|`, `-`), and empty space (`.`). A light beam enters and bounces/splits. Count energised tiles. Part 2: try every possible entry point and direction; find the maximum.

**Recursive DFS with directional memoisation:** The beam is simulated recursively. Each cell has a pipe type and a visited flag stored in a `char[][][2]` array. Movement is handled by four methods (`visitAndMoveRight`, `visitAndMoveLeft`, `visitAndMoveUp`, `visitAndMoveDown`) that call each other based on the cell type.

Splitters spawn two recursive calls (one per direction). Without memoisation, beams cycling in loops would cause infinite recursion. A `HashSet<Tuple<y, x, Direction>>` caches every `(position, direction)` pair already visited — if a beam reaches the same cell going the same direction twice, it stops.

**Part 2** runs the full simulation from every edge cell in every inward direction, resetting the cache and visited flags between runs.

**Complexity:** O(n × m) per simulation (each cell visited at most once per direction = 4 times), O(n × m × perimeter) for part 2.

---

## Day 17 — Clumsy Crucible

**Problem:** Navigate a heat-loss grid from top-left to bottom-right, minimising total heat loss. Constraint: the crucible cannot move more than 3 steps in the same direction consecutively (part 1), or must move between 4 and 10 steps in the same direction (part 2).

**Modified Dijkstra's algorithm:** Classic Dijkstra finds shortest paths in weighted graphs. Here, state is not just `(row, column)` but `(row, column, direction, consecutive_steps)` — the step constraint makes direction and momentum part of the state.

A `PriorityQueue<State>` ordered by `heatLoss` serves as the min-heap. A `HashSet<Visited>` tracks processed states. On each poll:
- If at the destination, return the heat loss.
- If not yet visited, mark visited.
- Add neighbours: continue in current direction (if under step limit) or turn 90° (never reverse).

Part 2 changes the step limits: must continue at least 4 steps before turning, cannot exceed 10.

The state space is O(n × m × 4 × max_steps). The PriorityQueue ensures the minimum-cost state is always processed first, guaranteeing optimality.

**Complexity:** O((n × m × 4 × k) log(n × m × 4 × k)) where k = max consecutive steps.

---

## Day 18 — Lavaduct Lagoon

**Problem:** A sequence of dig instructions (direction + steps) traces a polygon boundary. Part 1: coordinates come from the instructions directly. Part 2: coordinates are encoded in the hex colour values, making the polygon enormous (tens of millions of units per side).

**Shoelace formula + Pick's theorem:** Naive approaches (flood fill, pixel counting) are completely infeasible for part 2's scale.

The **Shoelace formula** computes the area of any polygon given its vertices:

```
Area = |Σ (xᵢ × yᵢ₊₁ - xᵢ₊₁ × yᵢ)| / 2
```

This gives the interior area. **Pick's theorem** relates interior area (A), boundary points (B), and interior points (I):

```
A = I + B/2 - 1  →  I = A - B/2 + 1
```

The total enclosed area (interior + boundary) is `I + B = A + B/2 + 1`.

The implementation accumulates coordinates as the dig instructions are followed, then applies the formula. For part 2, hex strings are parsed: the first 5 hex digits are the step count, and the last digit encodes direction (`0=R`, `1=D`, `2=L`, `3=U`).

This collapses a trillion-cell problem to an O(n) calculation over just the vertices.

**Complexity:** O(n) where n = number of instructions.

---

## Day 19 — Aplenty

**Problem:** Parts described by four ratings (x, m, a, s) are processed through a system of named workflows. Each workflow is a list of conditional rules; the part follows the first matching rule to its next destination, ending in Accept or Reject. Part 1: sum the ratings of all accepted parts.

**Recursive rule evaluation:** Workflows are stored as `Map<String, List<String>>`. Starting from workflow `"in"`, each rule is evaluated in order:
- `A` → accept (return true)
- `R` → reject (return false)
- `name` → recurse into that workflow
- `x<1234:dest` → parse the condition, check against the part's rating, recurse into `dest` if matched

The recursive structure mirrors the problem description directly, making the code readable alongside the puzzle statement.

**Part 2** (commented out): the brute-force approach of iterating all combinations of x, m, a, s from 1 to 4000 is 256 trillion combinations — clearly infeasible. The correct approach would use range propagation: track ranges `[1,4000]` for each variable, split ranges at condition boundaries, and count combinations. This is left as an open exercise.

**Complexity (part 1):** O(parts × average_workflow_depth). Linear in the number of parts.

---

## Reflections

Nineteen days, a range of techniques from linear scanning to Dijkstra's algorithm to the Shoelace formula. A few themes emerge across the solutions:

**Brute force and then optimise.** Days 11, 12, and 14 each include a brute-force first implementation that works for part 1 but breaks on part 2's scale. They're kept in the repo deliberately — the contrast between the naive and optimised approaches is more instructive than the optimised solution alone.

**The right data structure often is the solution.** Day 3's shared `PartNumber` object, Day 10's `char[][][2]` encoding two layers of information, Day 15's explicit linked-list hashmap — in each case, choosing the representation carefully made the algorithm fall out naturally.

**Mathematical tools for scale.** Days 8, 11, 14, and 18 all required recognising that direct simulation was infeasible and reaching for a mathematical insight instead: LCM, coordinate remapping, cycle detection, and the Shoelace formula respectively.

---

## Author

**Eduardo Fernandes** · Berlin
[github.com/dufernandes](https://github.com/dufernandes)

---

## License

[Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0)
