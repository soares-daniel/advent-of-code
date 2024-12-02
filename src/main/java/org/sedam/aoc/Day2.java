package org.sedam.aoc;

import java.util.*;

public class Day2 extends Day {

    public String part1(List<String> input) {
        int result = 0;
        for (String report : input) {
            String[] levelsStr = report.split(" ");
            int[] levels = Arrays.stream(levelsStr).mapToInt(Integer::parseInt).toArray();
            if (isSafe(levels)) {
                result++;
            }
        }
        return result + "";
    }

    private static boolean isSafe(int[] levels) {
        boolean isIncreasing = true;
        boolean isDecreasing = true;

        for (int i = 1; i < levels.length; i++) {
            int diff = levels[i] - levels[i - 1];
            if (Math.abs(diff) < 1 || Math.abs(diff) > 3) {
                return false;
            }
            if (diff > 0) {
                isDecreasing = false;
            } else if (diff < 0) {
                isIncreasing = false;
            }
        }
        return isIncreasing || isDecreasing;
    }

    public String part2(List<String> input) {
        int result = 0;
        for (String report : input) {
            String[] levelsStr = report.split(" ");
            int[] levels = Arrays.stream(levelsStr).mapToInt(Integer::parseInt).toArray();
            if (isSafe2(levels)) {
                result++;
            }
        }
        return result + "";
    }

    private static boolean isSafe2(int[] levels) {
        // Report good
        if (isSafe(levels)) {
            return true;
        }

        // Remove one level each time
        for (int i = 0; i < levels.length; i++) {
            int[] reducedLevels = new int[levels.length - 1];
            for (int j = 0, k = 0; j < levels.length; j++) {
                if (j != i) {
                    reducedLevels[k++] = levels[j];
                }
            }
            if (isSafe(reducedLevels)) {
                return true;
            }
        }
        return false;
    }
}
