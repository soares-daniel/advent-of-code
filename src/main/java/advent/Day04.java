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
        String[][] grid = GridUtils.parseGrid(IOUtils.toLines(input));
        int rolls = 0;
        int h = grid.length;
        int w = grid[0].length;
        for (int r=0; r<h; r++) {
            for (int c=0; c < w; c++) {
                if (!grid[r][c].equals("@")) continue;

                GridUtils.Point p = new GridUtils.Point(r, c);
                List<GridUtils.Point> neighbours = GridUtils.neighbours8(p, h, w);
                long adjacentRolls = neighbours.stream()
                        .filter(np -> grid[np.row()][np.col()].equals("@"))
                        .count();

                if (adjacentRolls < 4) {
                    rolls++;
                }
            }
        }
        return rolls + "";
    }

    @Override public String part2(String input) {
        String[][] grid = GridUtils.parseGrid(IOUtils.toLines(input));
        int rolls = 0;
        int h = grid.length;
        int w = grid[0].length;

        do {
            int lastCount = 0;
            List<GridUtils.Point> rollsRemoved = new ArrayList<>();
            for (int r=0; r<h; r++) {
                for (int c=0; c < w; c++) {
                    if (!grid[r][c].equals("@")) continue;

                    GridUtils.Point p = new GridUtils.Point(r, c);
                    List<GridUtils.Point> neighbours = GridUtils.neighbours8(p, h, w);
                    long adjacentRolls = neighbours.stream()
                            .filter(np -> grid[np.row()][np.col()].equals("@"))
                            .count();

                    if (adjacentRolls < 4) {
                        lastCount++;
                        rollsRemoved.add(p);
                    }
                }
            }
            for (GridUtils.Point p : rollsRemoved) {
                grid[p.row()][p.col()] = ".";
            }
            if (lastCount == 0) break;
            rolls += lastCount;
        } while (true);
        return rolls + "";
    }
}

