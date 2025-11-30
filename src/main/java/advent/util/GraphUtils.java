package advent.util;

import java.util.*;

public final class GraphUtils {
    private GraphUtils() {}

    /** Basic BFS that returns shortest distances from a start node. */
    public static <T> Map<T, Integer> bfsShortestPath(Map<T, List<T>> graph, T start) {
        Map<T, Integer> dist = new HashMap<>();
        Deque<T> q = new ArrayDeque<>();
        q.add(start);
        dist.put(start, 0);

        while (!q.isEmpty()) {
            T node = q.poll();
            int d = dist.get(node);
            for (T next : graph.getOrDefault(node, List.of())) {
                if (dist.containsKey(next)) continue;
                dist.put(next, d + 1);
                q.add(next);
            }
        }
        return dist;
    }
}