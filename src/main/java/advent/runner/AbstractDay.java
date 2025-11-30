package advent.runner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;

public abstract class AbstractDay implements Day {

    protected String loadFile(String fileName) {
        Path path = Path.of("src/main/resources", fileName);
        if (!Files.exists(path)) {
            ConsoleLog.warn("File not found: " + fileName);
            return null;
        }
        try {
            String content = Files.readString(path).trim();
            return content.isEmpty() ? null : content;
        } catch (IOException e) {
            ConsoleLog.error("Failed to read file " + fileName + ": " + e.getMessage());
            return null;
        }
    }

    protected String loadTestInput() {
        return loadFile(name().toLowerCase() + "/test.txt");
    }

    protected String loadRealInput() {
        return loadFile(name().toLowerCase() + "/input.txt");
    }

    protected void printResult(String label, String result, Instant start) {
        long ms = Duration.between(start, Instant.now()).toMillis();
        System.out.printf("  %-10s : %-20s (%d ms)%n", label, result, ms);
    }

    protected boolean verifyAndPrint(String label, String actual, String expected) {
        if (expected == null || expected.isBlank()) {
            System.out.printf("  %-10s [SKIP] got: %-15s expected: (none)%n", label, actual);
            return true;
        }
        boolean ok = actual.equals(expected);
        String status = ok ? "[OK]" : "[FAIL]";
        System.out.printf("  %-10s %-6s got: %-15s expected: %s%n",
                label, status, actual, expected);
        return ok;
    }

    public void run(boolean testsOnly, int year) {
        ConsoleLog.headline("\n=== " + name() + " ===");

        String testInput = loadTestInput();
        if (testInput == null) {
            ConsoleLog.warn("No test input found or file is empty. Skipping.");
            return;
        }
        String realInput = loadRealInput();

        AocClient client = new AocClient(year);
        int dayNum = extractDayNumber();
        DayStatus status = DayStatus.load(dayNum);

        // ----------------- PART 1 -----------------
        try {
            ConsoleLog.info("Running Part 1 test ...");
            var start = Instant.now();
            String testRes = part1(testInput);
            printResult("Part 1 (test)", testRes, start);
            boolean ok = verifyAndPrint("Part 1 (test)", testRes, expectedTestResultPart1());
            if (!ok) {
                ConsoleLog.warn("Part 1 test failed; aborting.");
                return;
            }

            if (!testsOnly && realInput != null) {
                ConsoleLog.info("Running Part 1 real input ...");
                start = Instant.now();
                String realRes = part1(realInput);
                printResult("Part 1 (real)", realRes, start);

                if (!status.isPart1Accepted()) {
                    ConsoleLog.info("Submitting Part 1 result ...");
                    boolean accepted = client.submitAnswer(dayNum, 1, realRes);
                    if (accepted) {
                        status.markPart1Accepted();
                        status.save(dayNum);
                        client.fetchInput(extractDayNumber(), "input-2");
                    }
                } else {
                    ConsoleLog.info("Skipping Part 1 submission (already accepted).");
                }
            }
        } catch (NotImplementedException e) {
            ConsoleLog.warn("Part 1 not implemented; stopping.");
            return;
        } catch (Exception e) {
            ConsoleLog.error("Error during Part 1: " + e.getMessage());
            return;
        }

        // ----------------- PART 2 -----------------
        try {
            ConsoleLog.info("Running Part 2 test ...");
            var start = Instant.now();
            String testRes = part2(testInput);
            printResult("Part 2 (test)", testRes, start);
            boolean ok = verifyAndPrint("Part 2 (test)", testRes, expectedTestResultPart2());
            if (!ok) {
                ConsoleLog.warn("Part 2 test failed; skipping real run.");
                return;
            }

            if (!testsOnly && realInput != null) {
                ConsoleLog.info("Running Part 2 real input ...");
                start = Instant.now();
                String realRes = part2(realInput);
                printResult("Part 2 (real)", realRes, start);
                if (!status.isPart2Accepted()) {
                    ConsoleLog.info("Submitting Part 2 result ...");
                    boolean accepted = client.submitAnswer(dayNum, 2, realRes);
                    if (accepted) {
                        status.markPart2Accepted();
                        status.save(dayNum);
                    }
                } else {
                    ConsoleLog.info("Skipping Part 2 submission (already accepted).");
                }
            }
        } catch (NotImplementedException e) {
            ConsoleLog.warn("Part 2 not implemented.");
        } catch (Exception e) {
            ConsoleLog.error("Error during Part 2: " + e.getMessage());
        }
    }

    private int extractDayNumber() {
        String digits = name().replaceAll("\\D+", "");
        return digits.isEmpty() ? 0 : Integer.parseInt(digits);
    }
}