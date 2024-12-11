package org.sedam.aoc;

import java.util.*;

public class Day11 extends Day {

    public String part1(List<String> input) {
        List<Long> stones = Utils.parseLineOfLongs(input.getFirst(), " ");

        for (int i = 0; i < 25; i++) {
            stones = transform(stones);
        }

        return stones.size() + "";
    }

    private List<Long> transform(List<Long> stones) {
        List<Long> newStones = new ArrayList<>();

        for (Long stone : stones) {
            // rule 1 stone 0 replace with 1
            if (stone == 0) {
                newStones.add(1L);
                continue;
            }

            String stoneStr = stone + "";
            if (stoneStr.length() % 2 == 0) {
                // rule 2 even split digits in half
                int mid = stoneStr.length() / 2;
                long left = Long.parseLong(stoneStr.substring(0, mid));
                long right = Long.parseLong(stoneStr.substring(mid));
                newStones.add(left);
                newStones.add(right);
                continue;
            }

            newStones.add(stone*2024);
        }

        return newStones;
    }

    public String part2(List<String> input) {
        Map<Long, Long> counter = new HashMap<>();
        for (Long num : Utils.parseLineOfLongs(input.getFirst(), " ")) {
            counter.put(num, counter.getOrDefault(num, 0L) + 1L);
        }

        for (int t = 0; t < 75; t++) {
            Map<Long, Long> newCounter = new HashMap<>();

            for (Map.Entry<Long, Long> entry : counter.entrySet()) {
                long num = entry.getKey();
                long freq = entry.getValue();

                if (num == 0) {
                    newCounter.merge(1L, freq, Long::sum);
                }
                else {
                    String numStr = String.valueOf(num);
                    if (numStr.length() % 2 == 0) {
                        int mid = numStr.length() / 2;
                        long left = Long.parseLong(numStr.substring(0, mid));
                        long right = Long.parseLong(numStr.substring(mid));
                        newCounter.merge(left, freq, Long::sum);
                        newCounter.merge(right, freq, Long::sum);
                    }
                    else {
                        newCounter.merge(num * 2024, freq, Long::sum);
                    }
                }
            }
            counter = newCounter;
        }

        return counter.values().stream().mapToLong(Long::longValue).sum() + "";
    }
}
