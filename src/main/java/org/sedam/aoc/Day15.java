package org.sedam.aoc;

import java.awt.Point;
import java.util.*;
import java.util.stream.Collectors;

public class Day15 extends Day {

    public String part1(List<String> input) {
        int rows = 0;
        for (String line : input) {
            if (line.isEmpty()) {
                break;
            }
            rows++;
        }

        int cols = input.getFirst().length();
        char[][] grid = new char[rows][cols];
        StringBuilder movementBuilder = new StringBuilder();
        for (int i = rows + 1; i < input.size(); i++) {
            movementBuilder.append(input.get(i).replaceAll("\\s", ""));
        }
        String movements = movementBuilder.toString();


        int robotRow = 0, robotCol = 0;
        Set<Point> boxes = new HashSet<>();

        for (int r = 0; r < rows; r++) {
            String line = input.get(r);
            for (int c = 0; c < cols; c++) {
                char ch = line.charAt(c);
                grid[r][c] = ch;
                if (ch == '@') {
                    robotRow = r;
                    robotCol = c;
                    grid[r][c] = '.';
                } else if (ch == 'O') {
                    boxes.add(new Point(c, r));
                }
            }
        }

        for (char move : movements.toCharArray()) {
            int[] dir = Utils.DIRECTIONS_MAP.get(move);
            int newRobotRow = robotRow + dir[0];
            int newRobotCol = robotCol + dir[1];

            if (grid[newRobotRow][newRobotCol] == '#') {
                continue;
            }

            Point newRobotPos = new Point(newRobotCol, newRobotRow);
            if (boxes.contains(newRobotPos)) {
                Set<Point> boxesToMove = new HashSet<>();
                boxesToMove.add(newRobotPos);

                if (!canPushBoxes(grid, boxes, newRobotRow, newRobotCol, dir, boxesToMove)) {
                    continue;
                }

                moveBoxes(boxes, boxesToMove, dir);
            }

            robotRow = newRobotRow;
            robotCol = newRobotCol;
        }

        int gpsSum = 0;
        for (Point box : boxes) {
            gpsSum += 100 * box.y + box.x;
        }

        return String.valueOf(gpsSum);
    }

    private boolean canPushBoxes(char[][] grid, Set<Point> boxes, int startRow, int startCol, int[] dir, Set<Point> boxesToMove) {
        int newRow = startRow + dir[0];
        int newCol = startCol + dir[1];

        if (newRow < 0 || newRow >= grid.length || newCol < 0 || newCol >= grid[0].length) {
            return false;
        }
        if (grid[newRow][newCol] == '#') {
            return false;
        }

        Point newPos = new Point(newCol, newRow);
        if (!boxes.contains(newPos)) {
            return true;
        }

        if (boxesToMove.contains(newPos)) {
            return false;
        }

        boxesToMove.add(newPos);
        return canPushBoxes(grid, boxes, newRow, newCol, dir, boxesToMove);
    }

    private void moveBoxes(Set<Point> boxes, Set<Point> boxesToMove, int[] dir) {
        List<Point> sortedBoxes = new ArrayList<>(boxesToMove);
        sortedBoxes.sort((p1, p2) -> {
            if (dir[1] != 0) {
                return Integer.compare(p2.x * dir[1], p1.x * dir[1]);
            } else {
                return Integer.compare(p2.y * dir[0], p1.y * dir[0]);
            }
        });

        for (Point box : sortedBoxes) {
            boxes.remove(box);
            boxes.add(new Point(box.x + dir[1], box.y + dir[0]));
        }
    }

    public String part2(List<String> input) {
        int rows = 0;
        for (String line : input) {
            if (line.isEmpty()) {
                break;
            }
            rows++;
        }

        int cols = input.getFirst().length();
        char[][] originalGrid = new char[rows][cols];
        StringBuilder movementBuilder = new StringBuilder();
        for (int i = rows + 1; i < input.size(); i++) {
            movementBuilder.append(input.get(i).replaceAll("\\s", ""));
        }
        String movements = movementBuilder.toString();

        for (int r = 0; r < rows; r++) {
            String line = input.get(r);
            for (int c = 0; c < cols; c++) {
                char ch = line.charAt(c);
                originalGrid[r][c] = ch;
            }
        }

        // Scale
        char[][] scaledGrid = scaleGrid(originalGrid);
        int robotRow = 0, robotCol = 0;
        Set<WideBox> wideBoxes = new HashSet<>();
        for (int r = 0; r < scaledGrid.length; r++) {
            for (int c = 0; c < scaledGrid[0].length; c++) {
                char ch = scaledGrid[r][c];
                if (ch == '@') {
                    robotRow = r;
                    robotCol = c;
                    scaledGrid[r][c] = '.';
                } else if (ch == '[') {
                    wideBoxes.add(new WideBox(c, c + 1, r));
                    scaledGrid[r][c] = '.';
                    scaledGrid[r][c + 1] = '.';
                }
            }
        }

        printState(scaledGrid, robotRow, robotCol, wideBoxes, ' ', 0);

        int moveCount = 0;
        for (char move : movements.toCharArray()) {
            moveCount++;
            int[] dir = Utils.DIRECTIONS_MAP.get(move);
            int newRobotRow = robotRow + dir[0];
            int newRobotCol = robotCol + dir[1];

            if (scaledGrid[newRobotRow][newRobotCol] == '#') {
                //System.out.println("Cannot move to (" + newRobotRow + "," + newRobotCol + ") because of wall");
                //printState(scaledGrid, robotRow, robotCol, wideBoxes, move, moveCount);
                continue;
            }

            if (wideBoxes.stream().anyMatch(box -> box.collidesWith(newRobotCol, newRobotRow))) {
                Set<WideBox> boxesToMove = new HashSet<>();
                boxesToMove.add(wideBoxes.stream().filter(box -> box.collidesWith(newRobotCol, newRobotRow)).findFirst().get());

                Set<WideBox> pushableBoxes = getPushableWideBoxes(scaledGrid, wideBoxes, dir, boxesToMove);

                if (pushableBoxes.isEmpty()) {
                    //System.out.println("Cannot push wide boxes to direction " + move);
                    //printState(scaledGrid, robotRow, robotCol, wideBoxes, move, moveCount);
                    continue;
                }

                moveWideBoxes(wideBoxes, pushableBoxes, dir);
            }

            robotRow = newRobotRow;
            robotCol = newRobotCol;
            //printState(scaledGrid, robotRow, robotCol, wideBoxes, move, moveCount);
        }

        int gpsSum = 0;
        for (WideBox box : wideBoxes) {
            gpsSum += 100 * box.y + box.x1;
        }

        return String.valueOf(gpsSum);
    }

    private char[][] scaleGrid(char[][] originalGrid) {
        int rows = originalGrid.length;
        int cols = originalGrid[0].length;
        char[][] scaledGrid = new char[rows][cols * 2];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char ch = originalGrid[r][c];
                if (ch == '#') {
                    scaledGrid[r][c * 2] = '#';
                    scaledGrid[r][c * 2 + 1] = '#';
                } else if (ch == '.') {
                    scaledGrid[r][c * 2] = '.';
                    scaledGrid[r][c * 2 + 1] = '.';
                } else if (ch == 'O') {
                    scaledGrid[r][c * 2] = '[';
                    scaledGrid[r][c * 2 + 1] = ']';
                } else if (ch == '@') {
                    scaledGrid[r][c * 2] = '@';
                    scaledGrid[r][c * 2 + 1] = '.';
                }
            }
        }

        return scaledGrid;
    }

    private void printState(char[][] scaledGrid, int robotRow, int robotCol, Set<WideBox> wideBoxes, char move, int moveCount) {
        if (moveCount > 0) System.out.println("\nMove " + move + ":");
        else System.out.println("\nInitial state:");
        boolean endOfBoxNext = false;
        for (int r = 0; r < scaledGrid.length; r++) {
            for (int c = 0; c < scaledGrid[0].length; c++) {
                if (endOfBoxNext) {
                    endOfBoxNext = false;
                    if (robotRow == r && robotCol == c) {
                        System.out.print('@');
                    }
                    continue;
                }
                if (robotRow == r && robotCol == c) {
                    System.out.print('@');
                } else {
                    int finalC = c;
                    int finalR = r;
                    if (wideBoxes.stream().anyMatch(box -> box.collidesWith(finalC, finalR))) {
                        WideBox box = wideBoxes.stream().filter(b -> b.collidesWith(finalC, finalR)).findFirst().get();
                        if (box.x1 == c) {
                            System.out.print('[');
                            System.out.print(']');
                            endOfBoxNext = true;
                        }
                    } else {
                        System.out.print(scaledGrid[r][c]);
                    }
                }
            }
            System.out.println();
        }
        System.out.println("Robot position:" + robotRow + "," + robotCol);
    }

    private Set<WideBox> getPushableWideBoxes(char[][] grid, Set<WideBox> boxes, int[] dir, Set<WideBox> boxesToMove) {
        Set<WideBox> pushableBoxes = new HashSet<>();
        Set<WideBox> visited = new HashSet<>(boxesToMove);

        for (WideBox box : boxesToMove) {
            int newRow = box.y + dir[0];
            int newCol1 = box.x1 + dir[1];
            int newCol2 = box.x2 + dir[1];

            if (grid[newRow][newCol1] == '#' || grid[newRow][newCol2] == '#') {
                return Collections.emptySet();
            }

            Set<WideBox> collidingBoxes = boxes.stream()
                    .filter(b -> b != box && (b.collidesWith(newCol1, newRow) || b.collidesWith(newCol2, newRow)))
                    .collect(Collectors.toSet());

            for (WideBox collidingBox : collidingBoxes) {
                if (!visited.contains(collidingBox)) {
                    visited.add(collidingBox);
                    Set<WideBox> newBoxesToMove = new HashSet<>(visited);
                    newBoxesToMove.add(collidingBox);
                    Set<WideBox> deeperPushable = getPushableWideBoxes(grid, boxes, dir, newBoxesToMove);

                    if (deeperPushable.isEmpty()) {
                        return Collections.emptySet();
                    }
                    pushableBoxes.addAll(deeperPushable);
                }
            }

            pushableBoxes.add(box);
        }

        return pushableBoxes;
    }

    private void moveWideBoxes(Set<WideBox> boxes, Set<WideBox> boxesToMove, int[] dir) {
        List<WideBox> sortedBoxes = new ArrayList<>(boxesToMove);
        sortedBoxes.sort((p1, p2) -> {
            if (dir[1] > 0) {
                return Integer.compare(p2.x1, p1.x1);  // Moving right
            } else if (dir[1] < 0) {
                return Integer.compare(p1.x1, p2.x1);  // Moving left
            } else if (dir[0] > 0) {
                return Integer.compare(p2.y, p1.y);    // Moving down
            } else {
                return Integer.compare(p1.y, p2.y);    // Moving up
            }
        });

        for (WideBox box : sortedBoxes) {
            boxes.remove(box);
            boxes.add(new WideBox(
                    box.x1 + dir[1],
                    box.x2 + dir[1],
                    box.y + dir[0]
            ));
        }
    }

    private static class WideBox {
        public int x1, x2, y;

        public boolean collidesWith(int x, int y) {
            return this.y == y && (x == this.x1 || x == this.x2);
        }

        public WideBox(int x1, int x2, int y) {
            this.x1 = x1;
            this.x2 = x2;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof WideBox other)) {
                return false;
            }
            return x1 == other.x1 && x2 == other.x2 && y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x1, x2, y);
        }
    }
}