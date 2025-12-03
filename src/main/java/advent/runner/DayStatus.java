package advent.runner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DayStatus {

    private boolean part1Accepted;
    private boolean part2Accepted;

    public boolean isPart1Accepted() { return part1Accepted; }
    public boolean isPart2Accepted() { return part2Accepted; }

    public void markPart1Accepted() { this.part1Accepted = true; }
    public void markPart2Accepted() { this.part2Accepted = true; }

    public static DayStatus load(int day, int year) {
        Path file = statusFile(day, year);
        if (!Files.exists(file)) {
            return new DayStatus();
        }
        try {
            String content = Files.readString(file).trim();
            DayStatus status = new DayStatus();
            status.part1Accepted = content.contains("part1=true");
            status.part2Accepted = content.contains("part2=true");
            return status;
        } catch (IOException e) {
            ConsoleLog.warn("Could not read status file for day " + day);
            return new DayStatus();
        }
    }

    public void save(int day, int year) {
        Path file = statusFile(day, year);
        try {
            Files.createDirectories(file.getParent());
            String text = String.format("part1=%s%spart2=%s%n",
                    part1Accepted, System.lineSeparator(), part2Accepted);
            Files.writeString(file, text);
        } catch (IOException e) {
            ConsoleLog.warn("Could not write status file for day " + day + "and year " + year);
        }
    }

    private static Path statusFile(int day, int year) {
        return Path.of("src/main/resources/" + year + "/day" + String.format("%02d", day), ".aoc-status");
    }
}