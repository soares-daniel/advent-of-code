package org.sedam.aoc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Utils {

    public static final int[][] CROSSDIRECTIONS = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    public static final int[][] DIRECTIONS = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    public static final Map<Character, int[]> DIRECTIONS_MAP = Map.of(
            '^', new int[]{-1, 0}, // {y, x}
            'v', new int[]{1, 0},
            '<', new int[]{0, -1},
            '>', new int[]{0, 1}
    );

    public static List<String> readLines(String filename) {
        try {
            return Files.readAllLines(Paths.get("src/main/resources/" + filename));
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Parses the input into blocks separated by empty lines
     * @param input list of strings
     * @return list of blocks
     */
    public static List<List<String>> parseBlocks(List<String> input) {
        List<List<String>> blocks = new ArrayList<>();
        List<String> block = new ArrayList<>();
        for (String line : input) {
            if (line.isEmpty()) {
                if (!block.isEmpty()) {
                    blocks.add(block);
                }
                block = new ArrayList<>();
            } else {
                block.add(line);
            }
        }
        if (!block.isEmpty()) {
            blocks.add(block);
        }
        return blocks;
    }

    /**
     * Parse a line of comma-separated numbers into a list of integers
     * @param line comma-separated numbers
     * @param delimiter delimiter
     * @return list of integers
     */
    public static List<Integer> parseNumbers(String line, String delimiter) {
        return Arrays.stream(line.split(delimiter))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    /**
     * Parse the input into a 2d array
     * @param input list of strings
     * @return 2d array
     */
    public static String[][] parseGrid(List<String> input) {
        String[][] grid = new String[input.size()][input.getFirst().length()];
        for (int r = 0; r < input.size(); r++) {
            for (int c = 0; c < input.get(r).length(); c++) {
                grid[r][c] = String.valueOf(input.get(r).charAt(c));
            }
        }
        return grid;
    }

    /**
     * Parse the input into a 2d array of integers
     * @param input list of strings
     * @return 2d array of integers
     */
    public static int[][] parseGridInt(List<String> input) {
        int[][] grid = new int[input.size()][input.getFirst().length()];
        for (int r = 0; r < input.size(); r++) {
            for (int c = 0; c < input.get(r).length(); c++) {
                grid[r][c] = Integer.parseInt(String.valueOf(input.get(r).charAt(c)));
            }
        }
        return grid;
    }

    /**
     * Parse a line of ints separated by a delimiter
     * @param line line of ints
     * @param delimiter delimiter
     * @return list of integers
     */
    public static List<Integer> parseLineOfInts(String line, String delimiter) {
        return Arrays.stream(line.split(delimiter))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    /**
     * Parse a line of longs separated by a delimiter
     * @param line line of longs
     * @param delimiter delimiter
     * @return list of longs
     */
    public static List<Long> parseLineOfLongs(String line, String delimiter) {
        return Arrays.stream(line.split(delimiter))
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
