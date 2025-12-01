package advent;

import advent.runner.AbstractDay;
import advent.util.IOUtils;

import java.util.List;

public class Day01 extends AbstractDay {
    @Override public String name() { return "Day01"; }

    @Override public String expectedTestResultPart1() {
        return "3";
    }

    @Override public String expectedTestResultPart2() {
        return "6";
    }

    @Override
    public String part1(String input) {
        List<String> lines = IOUtils.toLines(input);
        int position = 50;
        int zeroCount = 0;

        for (String line : lines) {
            char direction = line.charAt(0);
            int value = Integer.parseInt(line.substring(1));
            if (direction == 'L') {
                position -= value;
            } else if (direction == 'R') {
                position += value;
            }
            position = (position % 100 + 100) % 100;
            if (position == 0) {
                zeroCount++;
            }
        }
        return String.valueOf(zeroCount);
    }

    @Override
    public String part2(String input) {
        List<String> lines = IOUtils.toLines(input);
        int position = 50;
        int zeroCount = 0;

        for (String line : lines) {
            char dir = line.charAt(0);
            int value = Integer.parseInt(line.substring(1));
            int crossings, newPos;

            if (dir == 'R') {
                int total = position + value;
                crossings = total / 100;
                newPos = total % 100;
            } else {
                int total = position - value;
                crossings = (value - position + 99) / 100;
                newPos = (total % 100 + 100) % 100;
            }

            zeroCount += crossings;
            position = newPos;
        }

        return String.valueOf(zeroCount);
    }
}

