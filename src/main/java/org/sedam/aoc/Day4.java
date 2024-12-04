package org.sedam.aoc;

import java.util.List;

public class Day4 extends Day {
    String TARGET = "XMAS";
    int[][] DIRECTIONS = {
            {0, 1}, // right
            {0, -1}, // left
            {1, 0}, // bottom
            {-1, 0}, // top
            {1, 1}, // top right
            {-1, -1}, // top left
            {1, -1}, // bottom left
            {-1, 1} // top right
    };

    public String part1(List<String> input) {
        char[][] grid = input.stream().map(String::toCharArray).toArray(char[][]::new);
        int result = 0;
        int rows = grid.length;
        int cols = grid[0].length;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                for (int[] dir : DIRECTIONS) {
                    boolean match = true;
                    for (int i = 0; i < TARGET.length(); i++) {
                        int r = row + i * dir[0];
                        int c = col + i * dir[1];
                        if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] != TARGET.charAt(i)) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        result++;
                    }
                }
            }
        }
        return result + "";
    }

    public String part2(List<String> input) {
        char[][] grid = input.stream().map(String::toCharArray).toArray(char[][]::new);
        int result = 0;
        int rows = grid.length;
        int cols = grid[0].length;
        // Search for A and check diagonal if M and S
        for (int row = 1; row < rows - 1; row++) {
            for (int col = 1; col < cols - 1; col++) {
                if (grid[row][col] == 'A') {
                    if (isValid(grid, row, col)) {
                        result++;
                    }
                }
            }
        }
        return result + "";

    }

    private boolean isValid(char[][] grid, int row, int col) {
        boolean topLeftBottomRight = (grid[row - 1][col - 1] == 'M' &&
                grid[row][col] == 'A' &&
                grid[row + 1][col + 1] == 'S') ||
                (
                        grid[row - 1][col - 1] == 'S' &&
                        grid[row][col] == 'A' &&
                        grid[row + 1][col + 1] == 'M'
                );

        boolean topRightBottomLeft = (grid[row - 1][col + 1] == 'M' && grid[row][col] == 'A' && grid[row + 1][col - 1] == 'S') ||
                        (grid[row - 1][col + 1] == 'S' && grid[row][col] == 'A' && grid[row + 1][col - 1] == 'M');

        return topLeftBottomRight && topRightBottomLeft;
    }
}
