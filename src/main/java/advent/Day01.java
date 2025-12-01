package advent;

import advent.runner.AbstractDay;
import advent.runner.NotImplementedException;
import advent.util.IOUtils;

import java.util.List;

public class Day01 extends AbstractDay {
    @Override public String name() { return "Day01"; }

    @Override public String expectedTestResultPart1() { return "24000"; }

    @Override public String expectedTestResultPart2() { return "45000"; }

    @Override public String part1(String input) {
        List<List<String>> blocks = IOUtils.parseBlocks(IOUtils.toLines(input));
        List<List<Integer>> caloriesPerElf = blocks.stream()
                .map(block -> block.stream().map(Integer::parseInt).toList())
                .toList();
        List<Integer> totalCaloriesPerElf = caloriesPerElf.stream()
                .map(elfCalories -> elfCalories.stream().mapToInt(Integer::intValue).sum())
                .toList();
        int maxCalories = totalCaloriesPerElf.stream().mapToInt(Integer::intValue).max().orElse(0);
        return Integer.toString(maxCalories);
    }

    @Override public String part2(String input) {
        List<List<String>> blocks = IOUtils.parseBlocks(IOUtils.toLines(input));
        List<List<Integer>> caloriesPerElf = blocks.stream()
                .map(block -> block.stream().map(Integer::parseInt).toList())
                .toList();
        List<Integer> totalCaloriesPerElf = caloriesPerElf.stream()
                .map(elfCalories -> elfCalories.stream().mapToInt(Integer::intValue).sum())
                .toList();
        List<Integer> sortedTotals = totalCaloriesPerElf.stream()
                .sorted((a, b) -> Integer.compare(b, a))
                .toList();
        int topThreeSum = sortedTotals.stream().limit(3).mapToInt(Integer::intValue).sum();
        return Integer.toString(topThreeSum);
    }
}

