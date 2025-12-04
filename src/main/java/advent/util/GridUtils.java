package advent.util;

import java.util.*;

public final class GridUtils {

    private GridUtils() {}

    public static final int[][] CROSS = {{0,1},{1,0},{0,-1},{-1,0}};
    public static final int[][] EIGHT =
            {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};

    public static record Point(int row, int col) {
        public Point add(Point p) { return new Point(row + p.row, col + p.col); }
        public Point add(int dr, int dc) { return new Point(row + dr, col + dc); }
        public boolean inBounds(int h, int w) { return row >= 0 && row < h && col >= 0 && col < w; }
    }

    public static List<Point> neighbours4(Point p, int h, int w) {
        List<Point> n = new ArrayList<>();
        for (int[] d : CROSS) {
            Point np = p.add(d[0], d[1]);
            if (np.inBounds(h, w)) n.add(np);
        }
        return n;
    }

    public static List<Point> neighbours8(Point p, int h, int w) {
        List<Point> n = new ArrayList<>();
        for (int[] d : EIGHT) {
            Point np = p.add(d[0], d[1]);
            if (np.inBounds(h, w)) n.add(np);
        }
        return n;
    }

    public static String[][] parseGrid(List<String> input) {
        int rows = input.size();
        int cols = input.getFirst().length();
        String[][] grid = new String[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                grid[r][c] = String.valueOf(input.get(r).charAt(c));
        return grid;
    }

    public static char[][] parseGridChar(List<String> input) {
        int rows = input.size();
        int cols = input.getFirst().length();
        char[][] grid = new char[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                grid[r][c] = input.get(r).charAt(c);
        return grid;
    }

    public static int[][] parseGridInt(List<String> input) {
        int rows = input.size();
        int cols = input.getFirst().length();
        int[][] grid = new int[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                grid[r][c] = Character.digit(input.get(r).charAt(c), 10);
        return grid;
    }
}