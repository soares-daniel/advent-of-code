package org.sedam.aoc;

import java.util.*;

public class Day7 extends Day {

    public String part1(List<String> input) {
        long total = 0;

        for (String line : input) {
            String[] parts = line.split(":");
            long testValue = Long.parseLong(parts[0].trim());
            String[] numsStr = parts[1].trim().split(" ");
            long[] nums = new long[numsStr.length];
            for (int i = 0; i < numsStr.length; i++) {
                nums[i] = Long.parseLong(numsStr[i]);
            }

            if (isPossible(nums, testValue)) {
                total += testValue;
            }
        }

        return String.valueOf(total);
    }

    private boolean isPossible(long[] nums, long testValue) {
        int n = nums.length;
        int numOperators = n - 1;
        int maxMask = (int) Math.pow(2, numOperators);

        for (int mask = 0; mask < maxMask; mask++) {
            if (evaluate(nums, mask) == testValue) {
                return true;
            }
        }

        return false;
    }

    private long evaluate(long[] nums, int mask) {
        int n = nums.length;
        long result = nums[0];
        for (int i = 0; i < n - 1; i++) {
            int operator = (mask % 2);
            mask /= 2;

            if (operator == 0) {
                result += nums[i + 1];
            } else if (operator == 1) {
                result *= nums[i + 1];
            }
        }

        return result;
    }

    public String part2(List<String> input) {
        long total = 0;

        for (String line : input) {
            String[] parts = line.split(":");
            long testValue = Long.parseLong(parts[0].trim());
            String[] numsStr = parts[1].trim().split(" ");
            long[] nums = new long[numsStr.length];
            for (int i = 0; i < numsStr.length; i++) {
                nums[i] = Long.parseLong(numsStr[i]);
            }

            if (isPossible2(nums, testValue)) {
                total += testValue;
            }
        }

        return String.valueOf(total);
    }

    private boolean isPossible2(long[] nums, long testValue) {
        int n = nums.length;
        int numOperators = n - 1;
        int maxMask = (int) Math.pow(3, numOperators);
        for (int mask = 0; mask < maxMask; mask++) {
            if (evaluate2(nums, mask) == testValue) {
                return true;
            }
        }

        return false;
    }

    private long evaluate2(long[] numbers, int mask) {
        int n = numbers.length;
        long result = numbers[0];
        for (int i = 0; i < n - 1; i++) {
            int operator = (mask % 3);
            mask /= 3;

            if (operator == 0) {
                result += numbers[i + 1];
            } else if (operator == 1) {
                result *= numbers[i + 1];
            } else if (operator == 2) {
                result = Long.parseLong(String.valueOf(result) + numbers[i + 1]);
            }
        }

        return result;
    }
}
