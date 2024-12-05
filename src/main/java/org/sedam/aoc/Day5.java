package org.sedam.aoc;

import java.util.*;
import java.util.stream.Collectors;

public class Day5 extends Day {

    public String part1(List<String> input) {
        List<List<String>> blocks = parseBlocks(input);

        Map<Integer, Set<Integer>> before = new HashMap<>();
        for (String line : blocks.get(0)) {
            String[] parts = line.split("\\|");
            int a = Integer.parseInt(parts[0]);
            int b = Integer.parseInt(parts[1]);
            before.computeIfAbsent(b, k -> new HashSet<>()).add(a);
        }

        int result = 0;
        for (String line : blocks.get(1)) {
            List<Integer> update = parseNumbers(line);
            if (isValid(update, before)) {
                result += update.get(update.size() / 2);
            }
        }

        return String.valueOf(result);
    }

    private List<List<String>> parseBlocks(List<String> input) {
        List<List<String>> blocks = new ArrayList<>();
        List<String> block = new ArrayList<>();
        for (String line : input) {
            if (line.isEmpty()) {
                if (!block.isEmpty()) {
                    blocks.add(block);
                }
                block = new ArrayList<>();
            } else {
                block.add(line);
            }
        }
        if (!block.isEmpty()) {
            blocks.add(block);
        }
        return blocks;
    }

    private List<Integer> parseNumbers(String line) {
        return Arrays.stream(line.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private boolean isValid(List<Integer> update, Map<Integer, Set<Integer>> before) {
        for (int i = 0; i < update.size(); i++) {
            int currentNum = update.get(i);

            if (before.containsKey(currentNum)) {
                for (int beforeNum : before.get(currentNum)) {
                    int beforeIndex = update.indexOf(beforeNum);
                    if (beforeIndex > i) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public String part2(List<String> input) {
        List<List<String>> blocks = parseBlocks(input);

        Map<Integer, Set<Integer>> before = new HashMap<>();
        for (String line : blocks.get(0)) {
            String[] parts = line.split("\\|");
            int a = Integer.parseInt(parts[0]);
            int b = Integer.parseInt(parts[1]);
            before.computeIfAbsent(b, k -> new HashSet<>()).add(a);
        }

        int result = 0;
        List<List<Integer>> updates = blocks.get(1).stream()
                .map(this::parseNumbers)
                .toList();

        for (List<Integer> update : updates) {
            if (!isValid(update, before)) {
                List<Integer> orderedUpdate = reorder(update, before);
                result += orderedUpdate.get(orderedUpdate.size() / 2);
            }
        }

        return String.valueOf(result);
    }


    private List<Integer> reorder(List<Integer> update, Map<Integer, Set<Integer>> before) {
        List<Integer> ordered = new ArrayList<>(update);

        ordered.sort((a, b) -> {
            if (before.containsKey(b) && before.get(b).contains(a)) {
                return -1;
            }
            if (before.containsKey(a) && before.get(a).contains(b)) {
                return 1;
            }
            return Integer.compare(a, b);
        });

        return ordered;
    }
}
