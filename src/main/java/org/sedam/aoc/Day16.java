package org.sedam.aoc;

import java.util.*;

public class Day16 extends Day {
    private static final int[] DX = {0, 1, 0, -1}; // E S W N
    private static final int[] DY = {1, 0, -1, 0};

    private static class State {
        int x, y, dir, cost;
        List<String> path;

        State(int x, int y, int dir, int cost, List<String> path) {
            this.x = x;
            this.y = y;
            this.dir = dir;
            this.cost = cost;
            this.path = path;
        }
    }

    private static class Result {
        int cost;
        Set<String> tiles;

        Result(int cost, Set<String> tiles) {
            this.cost = cost;
            this.tiles = tiles;
        }
    }

    public String part1(List<String> input) {
        Result result = findBestPath(input);
        return String.valueOf(result.cost);
    }

    public String part2(List<String> input) {
        Result result = findBestPath(input);
        return String.valueOf(result.tiles.size());
    }

    private Result findBestPath(List<String> input) {
        int rows = input.size();
        int cols = input.getFirst().length();
        char[][] map = new char[rows][cols];
        int startX = 0, startY = 0, endX = 0, endY = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                map[i][j] = input.get(i).charAt(j);
                if (map[i][j] == 'S') {
                    startX = i;
                    startY = j;
                } else if (map[i][j] == 'E') {
                    endX = i;
                    endY = j;
                }
            }
        }

        int finalEndX = endX;
        int finalEndY = endY;
        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> s.cost + heuristic(s.x, s.y, finalEndX, finalEndY)));
        Map<String, Integer> bestCosts = new HashMap<>();
        Set<String> bestPathTiles = new HashSet<>();
        int minCost = Integer.MAX_VALUE;

        List<String> initialPath = new ArrayList<>();
        initialPath.add(startX + "," + startY);
        pq.add(new State(startX, startY, 0, 0, initialPath));

        while (!pq.isEmpty()) {
            State current = pq.poll();

            String stateKey = current.x + "," + current.y + "," + current.dir;
            Integer previousBestCost = bestCosts.get(stateKey);

            if (previousBestCost != null && previousBestCost < current.cost) {
                continue;
            }

            if (minCost != Integer.MAX_VALUE && current.cost > minCost) {
                continue;
            }

            bestCosts.put(stateKey, current.cost);

            if (current.x == endX && current.y == endY) {
                if (current.cost <= minCost) {
                    if (current.cost < minCost) {
                        minCost = current.cost;
                        bestPathTiles.clear();
                    }
                    bestPathTiles.addAll(current.path);
                }
                continue;
            }

            int nx = current.x + DX[current.dir];
            int ny = current.y + DY[current.dir];
            if (isValid(nx, ny, rows, cols, map)) {
                List<String> newPath = new ArrayList<>(current.path);
                newPath.add(nx + "," + ny);
                pq.add(new State(nx, ny, current.dir, current.cost + 1, newPath));
            }

            for (int newDir : new int[]{(current.dir + 1) % 4, (current.dir + 3) % 4}) {
                List<String> newPath = new ArrayList<>(current.path);
                pq.add(new State(current.x, current.y, newDir, current.cost + 1000, newPath));
            }
        }

        if (minCost == Integer.MAX_VALUE) {
            return new Result(-1, Collections.emptySet());
        }

        return new Result(minCost, bestPathTiles);
    }

    private boolean isValid(int x, int y, int rows, int cols, char[][] map) {
        return x >= 0 && x < rows && y >= 0 && y < cols && map[x][y] != '#';
    }

    private int heuristic(int x, int y, int targetX, int targetY) {
        return Math.abs(x - targetX) + Math.abs(y - targetY);
    }
}