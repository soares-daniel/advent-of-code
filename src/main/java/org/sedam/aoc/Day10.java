
package org.sedam.aoc;

import java.util.*;

public class Day10 extends Day {

    public String part1(List<String> input) {
        int result = 0;
        int[][] grid = Utils.parseGridInt(input);

        int rows = grid.length;
        int cols = grid[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 0) {
                    result += bfs(grid, i, j);
                }
            }
        }

        return result + "";
    }

    private int bfs(int[][] grid, int i, int j) {
        int rows = grid.length;
        int cols = grid[0].length;
        Set<String> reachableNines = new HashSet<>();

        Queue<int[]> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.offer(new int[]{i, j, 0});
        visited.add(i + "," + j);

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int row = curr[0];
            int col = curr[1];
            int currHeight = curr[2];

            if (grid[row][col] == 9) {
                reachableNines.add(row + "," + col);
            }

            for (int[] dir : Utils.CROSSDIRECTIONS) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];

                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {

                    if (grid[newRow][newCol] == currHeight + 1 ) {
                        String key = newRow + "," + newCol;
                        if (!visited.contains(key)) {
                            queue.offer(new int[]{newRow, newCol, currHeight + 1});
                            visited.add(key);
                        }
                    }
                }
            }
        }

        return reachableNines.size();
    }
    public String part2(List<String> input) {
        int result = 0;
        int[][] grid = Utils.parseGridInt(input);

        int rows = grid.length;
        int cols = grid[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 0) {
                    result += dfs(grid, i, j, 0, new HashSet<>());
                }
            }
        }

        return result + "";
    }

    private int dfs(int[][] grid, int row, int col, int currHeight, Set<String> visited) {
        if (grid[row][col] == 9) {
            return 1;
        }

        int rows = grid.length;
        int cols = grid[0].length;
        int result = 0;

        String pos = row + "," + col;
        visited.add(pos);

        for (int[] dir : Utils.CROSSDIRECTIONS) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                if (grid[newRow][newCol] == currHeight + 1) {
                    String newPos = newRow + "," + newCol;
                    if (grid[newRow][newCol] == currHeight + 1 && !visited.contains(newPos)) {
                        result += dfs(grid, newRow, newCol, currHeight + 1, visited);
                    }
                }
            }
        }

        visited.remove(pos);
        return result;
    }
}
