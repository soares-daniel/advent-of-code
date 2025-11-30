package advent.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public final class IOUtils {

    private IOUtils() {}

    public static List<String> readLines(String filename) {
        try {
            return Files.readAllLines(Paths.get("src/main/resources/" + filename));
        } catch (IOException e) {
            return List.of();
        }
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