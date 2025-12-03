package advent.util;

import java.util.*;

public final class IOUtils {

    private IOUtils() {}

    public static List<String> toLines(String input) {
        return Arrays.asList(input.split("\\R"));
    }

    /** Split input into blocks separated by empty lines. */
    public static List<List<String>> parseBlocks(List<String> input) {
        List<List<String>> blocks = new ArrayList<>();
        List<String> current = new ArrayList<>();
        for (String line : input) {
            if (line.isEmpty()) {
                if (!current.isEmpty()) blocks.add(current);
                current = new ArrayList<>();
            } else current.add(line);
        }
        if (!current.isEmpty()) blocks.add(current);
        return blocks;
    }
}