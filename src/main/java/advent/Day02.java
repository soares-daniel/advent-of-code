package advent;

import advent.runner.AbstractDay;
import advent.runner.NotImplementedException;
import advent.util.IOUtils;
import advent.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Day02 extends AbstractDay {
    @Override public String name() { return "Day02"; }

    @Override public String expectedTestResultPart1() {
        return "1227775554";
    }

    @Override public String expectedTestResultPart2() {
        return "4174379265";
    }

    @Override public String part1(String input) {
        long result = 0;
        List<String> ranges = StringUtils.split(input, ",");
        for (String range : ranges) {
            String[] minMax = range.split("-");
            long min = Long.parseLong(minMax[0]);
            long max = Long.parseLong(minMax[1]);

            for (long i=min; i<=max; i++) {
                if (hasRepetitiveSequenceTwice(String.valueOf(i))) {
                    result += i;
                }
            }
        }
        return result + "";
    }

    private boolean hasRepetitiveSequenceTwice(String str) {
        List<String> sequences = new ArrayList<>();
        for (int i = (str.length() / 2) - 1; i >= 0; i--) {
            String subString = str.substring(0, i + 1);
            if (str.length() % subString.length() == 0) {
                sequences.add(subString);
            }
        }
        for (String s : sequences) {
            StringBuilder joinedRep = new StringBuilder();
            joinedRep.append(s.repeat(2));
            if (joinedRep.toString().equals(str)) {
                return true;
            }
        }
        return false;
    }

    @Override public String part2(String input) {
        long result = 0;
        List<String> ranges = StringUtils.split(input, ",");
        for (String range : ranges) {
            String[] minMax = range.split("-");
            long min = Long.parseLong(minMax[0]);
            long max = Long.parseLong(minMax[1]);

            for (long i=min; i<=max; i++) {
                if (hasRepetitiveSequence(String.valueOf(i))) {
                    result += i;
                }
            }
        }
        return result + "";
    }

    private boolean hasRepetitiveSequence(String str) {
        List<String> sequences = new ArrayList<>();
        for (int i = (str.length() / 2) - 1; i >= 0; i--) {
            String subString = str.substring(0, i + 1);
            if (str.length() % subString.length() == 0) {
                sequences.add(subString);
            }
        }
        for (String s : sequences) {
            StringBuilder joinedRep = new StringBuilder();
            joinedRep.append(s.repeat(str.length() / s.length()));
            if (joinedRep.toString().equals(str)) {
                return true;
            }
        }
        return false;
    }
}
