package advent.util;

import java.util.*;
import java.util.stream.Collectors;

public final class StringUtils {
    private StringUtils() {}

    public static List<Integer> toInts(List<String> lines) {
        return lines.stream().map(Integer::parseInt).toList();
    }

    public static List<Integer> parseLineOfInts(String line, String delimiter) {
        return Arrays.stream(line.split(delimiter))
                .map(Integer::parseInt)
                .toList();
    }

    public static List<Long> parseLineOfLongs(String line, String delimiter) {
        return Arrays.stream(line.split(delimiter))
                .map(Long::parseLong)
                .toList();
    }

    public static List<Integer> stringToDigits(String s) {
        return s.chars().mapToObj(c -> c - '0').toList();
    }

    public static String repeat(String s, int times) {
        return String.join("", Collections.nCopies(times, s));
    }

    public static String join(List<?> parts, String separator) {
        return parts.stream().map(Object::toString).collect(Collectors.joining(separator));
    }

    public static boolean isNumeric(String s) {
        return s.matches("-?\\d+");
    }

    public static List<String> splitEvery(String s, int chunkSize) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < s.length(); i += chunkSize) {
            result.add(s.substring(i, Math.min(s.length(), i + chunkSize)));
        }
        return result;
    }

    public static List<String> split(String s, String separator) {
        return Arrays.asList(s.split(separator));
    }
}