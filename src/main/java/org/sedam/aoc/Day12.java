package org.sedam.aoc;

import java.util.*;

public class Day12 extends Day {

    public String part1(List<String> input) {
        int result = 0;
        int rows = input.size();
        int cols = input.getFirst().length();
        String[][] grid = Utils.parseGrid(input);
        boolean[][] visited = new boolean[rows][cols];

        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!visited[i][j]) {
                    String plantType = grid[i][j];
                    Queue<int[]> queue = new LinkedList<>();
                    queue.offer(new int[]{i, j});
                    visited[i][j] = true;

                    int area = 0;
                    int perimeter = 0;

                    while (!queue.isEmpty()) {
                        int[] current = queue.poll();
                        int x = current[0];
                        int y = current[1];
                        area++;

                        for (int k = 0; k < 4; k++) {
                            int nx = x + dx[k];
                            int ny = y + dy[k];

                            if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && !visited[nx][ny] && grid[nx][ny].equals(plantType)) {
                                queue.offer(new int[]{nx, ny});
                                visited[nx][ny] = true;
                            } else if (nx < 0 || nx >= rows || ny < 0 || ny >= cols || !grid[nx][ny].equals(plantType)) {
                                perimeter++;
                            }
                        }
                    }

                    result += area * perimeter;
                }
            }
        }

        return result + "";
    }

    static int R, C;
    static String[][] m;
    static boolean[][] done;
    static int[][] d4 = {{1,0}, {-1,0}, {0,1}, {0,-1}};

    static class Edge {
        int x, y, dx, dy;
        Edge(int x, int y, int dx, int dy) {
            this.x = x; this.y = y; this.dx = dx; this.dy = dy;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Edge)) return false;
            Edge e = (Edge)o;
            return x == e.x && y == e.y && dx == e.dx && dy == e.dy;
        }
        @Override
        public int hashCode() {
            return Objects.hash(x, y, dx, dy);
        }
    }

    static Result go(int x, int y) {
        done[x][y] = true;
        Set<Edge> p = new HashSet<>();
        int a = 1;
        String cur = m[x][y];

        for (int[] dir : d4) {
            int dx = dir[0], dy = dir[1];
            int nx = x + dx, ny = y + dy;
            if (nx >= 0 && nx < R && ny >= 0 && ny < C && Objects.equals(m[x][y], m[nx][ny]) && !done[nx][ny]) {
                Result sub = go(nx, ny);
                p.addAll(sub.p);
                a += sub.a;
            }
            if (!(nx >= 0 && nx < R && ny >= 0 && ny < C && m[x][y] == (nx >= 0 && nx < R && ny >= 0 && ny < C ? m[nx][ny] : ' '))) {

                if (nx < 0 || nx >= R || ny < 0 || ny >= C || !Objects.equals(m[nx][ny], cur)) {
                    p.add(new Edge(x, y, dx, dy));
                }
            }
        }

        return new Result(p, a);
    }

    static class Result {
        Set<Edge> p;
        int a;
        Result(Set<Edge> p, int a) {
            this.p = p; this.a = a;
        }
    }

    public String part2(List<String> inputs) {
        R = inputs.size();
        C = inputs.getFirst().length();
        m = Utils.parseGrid(inputs);
        done = new boolean[R][C];
        int cost = 0;

        for (int x = 0; x < R; x++) {
            for (int y = 0; y < C; y++) {
                if (!done[x][y]) {
                    Result r = go(x, y);
                    Set<Edge> P = r.p;
                    int a = r.a;
                    int np = 0;

                    // for (nx, ny, dx, dy) in p:
                    for (Edge ed : P) {
                        int nx = ed.x, ny = ed.y, dx = ed.dx, dy = ed.dy;
                        if (dx != 0) {
                            Edge neighbor = new Edge(nx, ny - 1, dx, dy);
                            if (!P.contains(neighbor)) {
                                np += 1;
                            }
                        }
                        if (dy != 0) {
                            Edge neighbor = new Edge(nx - 1, ny, dx, dy);
                            if (!P.contains(neighbor)) {
                                np += 1;
                            }
                        }
                    }

                    cost += np * a;
                }
            }
        }

        return cost + "";
    }
}
