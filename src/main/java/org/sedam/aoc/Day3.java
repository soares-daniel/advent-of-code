package org.sedam.aoc;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 extends Day {

    public String part1(List<String> input) {

        Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
        Matcher matcher = pattern.matcher(String.join("", input));
        int result = 0;

        while (matcher.find()) {
            int a = Integer.parseInt(matcher.group(1));
            int b = Integer.parseInt(matcher.group(2));
            result += a * b;
        }

        return result + "";
    }

    public String part2(List<String> input) {

        Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)|do\\(\\)|don't\\(\\)");
        Matcher matcher = pattern.matcher(String.join("", input));
        int result = 0;
        boolean enabled = true;

        while (matcher.find()) {
            if (matcher.group().equals("do()")) {
                enabled = true;
            } else if (matcher.group().equals("don't()")) {
                enabled = false;
            } else if (enabled) {
                int a = Integer.parseInt(matcher.group(1));
                int b = Integer.parseInt(matcher.group(2));
                result += a * b;
            }
        }

        return result + "";
    }
}
