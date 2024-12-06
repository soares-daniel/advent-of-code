package org.sedam.aoc;

import java.util.*;

public class Day6 extends Day {

    public String part1(List<String> input) {
        int rows = input.size();
        int cols = input.get(0).length();
        Set<String> visited = new HashSet<>();

        int[] pos = findGuard(input);
        char initialGuard = input.get(pos[0]).charAt(pos[1]);
        int[] dir = getDirection(initialGuard);

        System.out.println("Map dimensions: " + rows + " rows x " + cols + " columns");
        System.out.println("Starting at position: (" + pos[0] + "," + pos[1] + ")");

        while (true) {
            String posKey = pos[0] + "," + pos[1];
            visited.add(posKey);

            int nextX = pos[0] + dir[0];
            int nextY = pos[1] + dir[1];

            // outside
            if (nextX < 0 || nextX >= rows || nextY < 0 || nextY >= cols) {
                System.out.println("Guard left the map at position: (" + pos[0] + "," + pos[1] + ")");
                break;
            }

            // blocked
            if (input.get(nextX).charAt(nextY) == '#') {
                dir = turnRight(dir);
                System.out.println("Hit obstacle at (" + nextX + "," + nextY + ") - turning right to face (" + dir[0] + "," + dir[1] + ")");
            } else {
                pos[0] = nextX;
                pos[1] = nextY;
            }
        }

        return String.valueOf(visited.size());
    }

    private int[] getDirection(char guard) {
        return switch (guard) {
            case '^' -> new int[]{-1, 0};
            case 'v' -> new int[]{1, 0};
            case '<' -> new int[]{0, -1};
            case '>' -> new int[]{0, 1};
            default -> throw new IllegalArgumentException("Invalid guard direction: " + guard);
        };
    }

    private int[] findGuard(List<String> grid) {
        for (int r = 0; r < grid.size(); r++) {
            for (int c = 0; c < grid.get(r).length(); c++) {
                char cell = grid.get(r).charAt(c);
                if (cell == '^' || cell == 'v' || cell == '<' || cell == '>') {
                    return new int[]{r, c};
                }
            }
        }
        throw new IllegalArgumentException("Guard position not found");
    }

    public static int[] turnRight(int[] direction) {
        // up -> right -> down -> left -> up
        return new int[]{direction[1], -direction[0]};
    }

    public String part2(List<String> input) {
        int rows = input.size();
        int cols = input.get(0).length();
        int[] guardStart = findGuard(input);
        int validPositions = 0;

        List<String> modifiableInput;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // skip if:
                // - current pos guard's start
                // - current pos already an obstacle (#)
                // - current pos contains the guard
                if ((r == guardStart[0] && c == guardStart[1]) ||
                        input.get(r).charAt(c) == '#' ||
                        isGuard(input.get(r).charAt(c))) {
                    continue;
                }

                // obstacle at current pos
                modifiableInput = new ArrayList<>(input);
                StringBuilder row = new StringBuilder(modifiableInput.get(r));
                row.setCharAt(c, '#');
                modifiableInput.set(r, row.toString());

                // check creates cycle
                if (createsLoop(modifiableInput)) {
                    validPositions++;
                    System.out.println("Valid position at (" + r + "," + c + ")");
                }
            }
        }

        return String.valueOf(validPositions);
    }

    private boolean isGuard(char c) {
        return c == '^' || c == 'v' || c == '<' || c == '>';
    }

    private boolean createsLoop(List<String> grid) {
        int rows = grid.size();
        int cols = grid.get(0).length();

        int[] pos = findGuard(grid);
        char initialGuard = grid.get(pos[0]).charAt(pos[1]);
        int[] dir = getDirection(initialGuard);

        Set<String> positionDirectionHistory = new HashSet<>();

        while (true) {
            String stateKey = pos[0] + "," + pos[1] + "," + dir[0] + "," + dir[1];

            // same state, cycle
            if (!positionDirectionHistory.add(stateKey)) {
                return true;
            }

            int nextX = pos[0] + dir[0];
            int nextY = pos[1] + dir[1];

            if (nextX < 0 || nextX >= rows || nextY < 0 || nextY >= cols) {
                return false;
            }

            // next blocked
            if (grid.get(nextX).charAt(nextY) == '#') {
                dir = turnRight(dir);
            } else {
                pos[0] = nextX;
                pos[1] = nextY;
            }

            // limit steps
            if (positionDirectionHistory.size() > rows * cols * 4) {
                return false;
            }
        }
    }
}
