package org.sedam.aoc;

import java.util.*;

public class Day1 extends Day {

    public String part1(List<String> input) {
        List<Integer> nums1 = new ArrayList<>();
        List<Integer> nums2 = new ArrayList<>();

        for (String line : input) {
            String[] parts = line.split("   ");
            nums1.add(Integer.parseInt(parts[0]));
            nums2.add(Integer.parseInt(parts[1]));
        }

        nums1.sort(Integer::compareTo);
        nums2.sort(Integer::compareTo);

        int result=0;
        for (int i = 0; i < nums1.size(); i++) {
            if (nums1.get(i) < nums2.get(i) ) {
                result += nums2.get(i) - nums1.get(i);
            } else {
                result += nums1.get(i) - nums2.get(i);
            }
        }

        return Integer.toString(result);
    }

    public String part2(List<String> input) {
        List<Integer> nums1 = new ArrayList<>();
        List<Integer> nums2 = new ArrayList<>();

        for (String line : input) {
            String[] parts = line.split("   ");
            nums1.add(Integer.parseInt(parts[0]));
            nums2.add(Integer.parseInt(parts[1]));
        }

        int result = nums1.stream()
                .mapToInt(integer -> Collections.frequency(nums2, integer) * integer)
                .sum();

        return result + "";
    }
}
