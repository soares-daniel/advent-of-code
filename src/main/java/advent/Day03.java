package advent;

import advent.runner.AbstractDay;
import advent.runner.NotImplementedException;
import advent.util.IOUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day03 extends AbstractDay {
    @Override public String name() { return "Day03"; }

    @Override public String expectedTestResultPart1() {
        return "357";
    }

    @Override public String expectedTestResultPart2() {
        return "3121910778619";
    }

    @Override public String part1(String input) {
        long res = 0;
        List<String> banks = IOUtils.toLines(input);
        for (String bank : banks) {
            long max = 0;
            char[] digits = bank.toCharArray();
            for (int i=0; i<digits.length; i++) {
                for (int j=i+1; j<digits.length; j++) {
                    int val = Integer.parseInt("" + digits[i] + digits[j]);
                    if (val > max) {
                        max = val;
                    }
                }
            }
            res += max;
        }

        return res + "";
    }

    @Override public String part2(String input) {
        long res = 0;
        List<String> banks = IOUtils.toLines(input);
        for (String bank : banks) {
            res += getLargestNumber(bank, 12);
        }

        return res + "";
    }

    private long getLargestNumber(String num, int digitAmount) {
        int n = num.length();
        Deque<Character> stack = new ArrayDeque<>();

        for (int i=0; i< n; i++) {
            char c = num.charAt(i);
            while (!stack.isEmpty() && stack.peekLast() < c && stack.size() + (n - i) > digitAmount) {
                stack.removeLast();
            }
            if (stack.size() < digitAmount) {
                stack.addLast(c);
            }
        }
        StringBuilder result = new StringBuilder();
        for (char c : stack) {
            result.append(c);
        }
        return Long.parseLong(result.toString());
    }
}

