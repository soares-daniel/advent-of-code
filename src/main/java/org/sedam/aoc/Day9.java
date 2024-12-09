package org.sedam.aoc;

import java.util.*;

public class Day9 extends Day {

    public String part1(List<String> input) {
        String line = input.getFirst();
        List<Character> disk = new ArrayList<>();
        int fileId = 0;

        for (int i = 0; i < line.length(); i++) {
            int size = Character.getNumericValue(line.charAt(i));
            if (i % 2 == 0) {
                for (int j = 0; j < size; j++) {
                    disk.add((char) ('0' + fileId));
                }
                fileId++;
            } else {
                for (int j = 0; j < size; j++) {
                    disk.add('.');
                }
            }
        }

        System.out.println(disk);

        while (true) {
            // right file to left space
            int rightFile = disk.size() - 1;
            while (rightFile >= 0 && disk.get(rightFile) == '.') {
                rightFile--;
            }
            if (rightFile < 0) break;

            int leftSpace = 0;
            while (leftSpace < rightFile && disk.get(leftSpace) != '.') {
                leftSpace++;
            }
            if (leftSpace >= rightFile) break;

            disk.set(leftSpace, disk.get(rightFile));
            disk.set(rightFile, '.');

            //System.out.println(disk);
        }

        long checksum = 0;
        for (int pos = 0; pos < disk.size(); pos++) {
            char block = disk.get(pos);
            if (block != '.') {
                checksum += (long) pos * (block - '0');
            }
        }

        return checksum + "";
    }

    public String part2(List<String> input) {
        String line = input.getFirst();
        List<Character> disk = new ArrayList<>();
        int fileId = 0;
        for (int i = 0; i < line.length(); i++) {
            int size = Character.getNumericValue(line.charAt(i));
            if (i % 2 == 0) {
                for (int j = 0; j < size; j++) {
                    disk.add((char) ('0' + fileId));
                }
                fileId++;
            } else {
                for (int j = 0; j < size; j++) {
                    disk.add('.');
                }
            }
        }

        int lastId = fileId - 1;

        // from first to last
        for (fileId = lastId; fileId >= 0; fileId--) {
            char fileChar = (char) ('0' + fileId);

            List<Integer> fileBlocks = new ArrayList<>();
            for (int i = 0; i < disk.size(); i++) {
                if (disk.get(i) == fileChar) {
                    fileBlocks.add(i);
                }
            }

            int fileSize = fileBlocks.size();
            int fileStart = fileBlocks.getFirst();

            // left space big enough
            int bestSpaceStart = -1;
            for (int i = 0; i < fileStart; i++) {
                boolean canFit = true;
                for (int j = 0; j < fileSize; j++) {
                    if (i + j >= disk.size() || disk.get(i + j) != '.') {
                        canFit = false;
                        break;
                    }
                }
                if (canFit) {
                    bestSpaceStart = i;
                    break;
                }
            }

            if (bestSpaceStart != -1) {
                for (int pos : fileBlocks) {
                    disk.set(pos, '.');
                }

                for (int i = 0; i < fileSize; i++) {
                    disk.set(bestSpaceStart + i, fileChar);
                }
            }
        }

        long checksum = 0;
        for (int pos = 0; pos < disk.size(); pos++) {
            char block = disk.get(pos);
            if (block != '.') {
                checksum += (long) pos * (block - '0');
            }
        }

        return checksum + "";
    }
}
