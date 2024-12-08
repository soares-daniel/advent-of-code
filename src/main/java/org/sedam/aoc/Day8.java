package org.sedam.aoc;

import java.util.*;

public class Day8 extends Day {

    public String part1(List<String> grid) {
        Map<Character, List<int[]>> freqPositions = new HashMap<>();
        int rows = grid.size();
        int cols = grid.getFirst().length();

        for (int x = 0; x < rows; x++) {
            String line = grid.get(x);
            for (int y = 0; y < cols; y++) {
                char ch = line.charAt(y);
                if (ch != '.') {
                    freqPositions.computeIfAbsent(ch, k -> new ArrayList<>()).add(new int[]{x, y});
                }
            }
        }

        Set<String> antiNodes = new HashSet<>();

        for (Map.Entry<Character, List<int[]>> entry : freqPositions.entrySet()) {
            List<int[]> positions = entry.getValue();
            int n = positions.size();

            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {

                    int[] p1 = positions.get(i);
                    int[] p2 = positions.get(j);

                    for (int k = 0; k < 2; k++) {
                        int[] base = k == 0 ? p1 : p2;
                        int[] other = k == 0 ? p2 : p1;

                        int dx = other[0] - base[0];
                        int dy = other[1] - base[1];
                        double distance = Math.sqrt(dx * dx + dy * dy);

                        double ux = dx / distance;
                        double uy = dy / distance;

                        int ax = (int) Math.round(base[0] - ux * distance);
                        int ay = (int) Math.round(base[1] - uy * distance);

                        if (isValid(ax, ay, rows, cols)) {
                            antiNodes.add(ax + "," + ay);
                        }
                    }
                }
            }
        }

        return String.valueOf(antiNodes.size());
    }

    private boolean isValid(int x, int y, int rows, int cols) {
        return x >= 0 && x < rows && y >= 0 && y < cols;
    }

    public String part2(List<String> grid) {
        Map<Character, List<int[]>> freqPositions = new HashMap<>();
        int rows = grid.size();
        int cols = grid.getFirst().length();

        for (int x = 0; x < rows; x++) {
            String line = grid.get(x);
            for (int y = 0; y < cols; y++) {
                char ch = line.charAt(y);
                if (ch != '.') {
                    freqPositions.computeIfAbsent(ch, k -> new ArrayList<>()).add(new int[]{x, y});
                }
            }
        }

        Set<String> antiNodes = new HashSet<>();

        for (List<int[]> positions : freqPositions.values()) {
            if (positions.size() < 2) {
                continue;
            }

            for (int[] pos : positions) {
                antiNodes.add(pos[0] + "," + pos[1]); // antennas
            }

            for (int i = 0; i < positions.size(); i++) {
                for (int j = i + 1; j < positions.size(); j++) {
                    int[] p1 = positions.get(i);
                    int[] p2 = positions.get(j);

                    // every point in grid
                    for (int x = 0; x < rows; x++) {
                        for (int y = 0; y < cols; y++) {
                            // skip antennas
                            if (antiNodes.contains(x + "," + y)) {
                                continue;
                            }

                            if (isCollinear(p1, p2, x, y)) {
                                antiNodes.add(x + "," + y);
                            }
                        }
                    }
                }
            }
        }

        return String.valueOf(antiNodes.size());
    }

    // collinear if triangle of 3 points area is 0
    private boolean isCollinear(int[] p1, int[] p2, int x3, int y3) {
        int x1 = p1[0], y1 = p1[1];
        int x2 = p2[0], y2 = p2[1];

        int area = x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2);
        return area == 0;
    }
}
