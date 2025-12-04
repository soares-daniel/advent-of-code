package advent;

import advent.runner.AbstractDay;
import advent.runner.NotImplementedException;
import advent.util.GridUtils;
import advent.util.IOUtils;

import java.util.ArrayList;
import java.util.List;

public class Day04 extends AbstractDay {
    @Override public String name() { return "Day04"; }

    @Override public String expectedTestResultPart1() {
        return "13";
    }

    @Override public String expectedTestResultPart2() {
        return "43";
    }

    @Override public String part1(String input) {
        char[][] grid = GridUtils.parseGridChar(IOUtils.toLines(input));
        int rolls = 0;
        int h = grid.length;
        int w = grid[0].length;
        for (int r=0; r<h; r++) {
            for (int c=0; c < w; c++) {
                if (grid[r][c] != '@') continue;
                int count = 0;
                for (int[] d : GridUtils.EIGHT) {
                    int nr = r + d[0];
                    int nc = c + d[1];
                    if (nr < 0 || nr >= h || nc < 0 || nc >= w) continue;
                    if (grid[nr][nc] == '@') count++;
                }

                if (count < 4) {
                    rolls++;
                }
            }
        }
        return rolls + "";
    }

    @Override public String part2(String input) {
        char[][] grid = GridUtils.parseGridChar(IOUtils.toLines(input));
        int h = grid.length;
        int w = grid[0].length;
        int totalRemoved = 0;

        boolean[][] candidate = new boolean[h][w];
        for (int r = 0; r < h; r++) {
            for (int c = 0; c < w; c++) {
                candidate[r][c] = grid[r][c] == '@';
            }
        }

        boolean changed;
        do {
            changed = false;
            boolean[][] nextCandidate = new boolean[h][w];
            for (int r = 0; r < h; r++) {
                for (int c = 0; c < w; c++) {
                    if (!candidate[r][c] || grid[r][c] != '@') continue;

                    int count = 0;
                    for (int[] d : GridUtils.EIGHT) {
                        int nr = r + d[0];
                        int nc = c + d[1];
                        if (nr < 0 || nr >= h || nc < 0 || nc >= w) continue;
                        if (grid[nr][nc] == '@') count++;
                    }

                    if (count < 4) {
                        grid[r][c] = '.';
                        totalRemoved++;
                        changed = true;

                        for (int[] d : GridUtils.EIGHT) {
                            int nr = r + d[0];
                            int nc = c + d[1];
                            if (nr >= 0 && nr < h && nc >= 0 && nc < w) {
                                nextCandidate[nr][nc] = true;
                            }
                        }
                    }
                }
            }

            candidate = nextCandidate;
        } while (changed);

        return totalRemoved + "";
    }
}

