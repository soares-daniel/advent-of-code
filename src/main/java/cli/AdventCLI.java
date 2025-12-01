package cli;

import advent.runner.AbstractDay;
import advent.runner.AocClient;
import advent.runner.ConsoleLog;
import advent.runner.Day;
import picocli.CommandLine;
import picocli.CommandLine.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Command(
        name = "advent",
        mixinStandardHelpOptions = true,
        subcommands = { AdventCLI.RunCommand.class, AdventCLI.NewCommand.class, AdventCLI.SubmitCommand.class }
)
public class AdventCLI implements Runnable {

    public static void main(String[] args) {
        int exit = new CommandLine(new AdventCLI()).execute(args);
        System.exit(exit);
    }

    @Override
    public void run() {
        System.out.println("Use `advent run` or `advent new` (see --help).");
    }

    @Command(name = "run", description = "Run an Advent of Code day")
    static class RunCommand implements Runnable {
        @Option(names = {"-d", "--day"}, description = "Day number to run", required = true)
        int day;

        @Option(names = {"-y", "--year"}, description = "Year (default: current year)")
        Integer year;

        @Option(names = "--test", description = "Run only test inputs")
        boolean testOnly;

        @Override
        public void run() {
            int y = (year != null) ? year : java.time.Year.now().getValue();
            Day instance = createDayInstance(day);
            if (instance == null) {
                System.err.printf("[ERROR] Day %02d not found or could not be instantiated.%n", day);
                return;
            }
            ((AbstractDay) instance).run(testOnly, y);
        }

        private Day createDayInstance(int day) {
            String className = String.format("advent.Day%02d", day);
            try {
                Class<?> clazz = Class.forName(className);
                return (Day) clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                System.err.printf("[ERROR] Failed to load %s: %s%n", className, e.getMessage());
                return null;
            }
        }
    }

    @Command(name = "new", description = "Create a new day template")
    static class NewCommand implements Runnable {
        @Option(names = {"-d", "--day"}, description = "Day number to create", required = true)
        int day;

        @Option(names = {"-y", "--year"}, description = "Year (default: current year)")
        Integer year;

        @Override
        public void run() {
            generateDay(day);
        }

        private void generateDay(int day) {
            int y = (year != null) ? year : java.time.Year.now().getValue();
            String dayName = String.format("Day%02d", day);
            String classFileName = "src/main/java/advent/" + dayName + ".java";
            String resourcesPath = "src/main/resources/";

            try {
                String template = """
                    package advent;

                    import advent.runner.AbstractDay;
                    import advent.runner.NotImplementedException;

                    public class %s extends AbstractDay {
                        @Override public String name() { return "%s"; }

                        @Override public String expectedTestResultPart1() {
                            return "";
                        }

                        @Override public String expectedTestResultPart2() {
                            return "";
                        }

                        @Override public String part1(String input) {
                            // TODO implement part 1
                            throw new NotImplementedException();
                        }

                        @Override public String part2(String input) {
                            // TODO implement part 2
                            throw new NotImplementedException();
                        }
                    }
                    """.formatted(dayName, dayName);

                Path javaFile = Path.of(classFileName);
                if (Files.exists(javaFile)) {
                    ConsoleLog.warn("Class file " + classFileName + " already exists; skipping creation.");
                } else {
                    Files.createDirectories(javaFile.getParent());
                    Files.writeString(javaFile, template + System.lineSeparator(), StandardOpenOption.CREATE_NEW);
                    ConsoleLog.info("Created " + classFileName);
                }

                Path testPath = Path.of(resourcesPath, year.toString(), dayName.toLowerCase(), "test-1.txt");
                Path testPath2 = Path.of(resourcesPath, year.toString(), dayName.toLowerCase(), "test-2.txt");
                Path inputPath = Path.of(resourcesPath, year.toString(), dayName.toLowerCase(), "input-1.txt");
                Files.createDirectories(testPath.getParent());

                if (!Files.exists(testPath)) {
                    Files.writeString(testPath, System.lineSeparator(), StandardOpenOption.CREATE_NEW);
                    ConsoleLog.info("Created " + testPath);
                } else {
                    ConsoleLog.warn("Test file already exists: " + testPath);
                }

                if (!Files.exists(testPath2)) {
                    Files.writeString(testPath2, System.lineSeparator(), StandardOpenOption.CREATE_NEW);
                    ConsoleLog.info("Created " + testPath2);
                } else {
                    ConsoleLog.warn("Test file already exists: " + testPath2);
                }

                if (!Files.exists(inputPath)) {
                    AocClient client = new AocClient(y);
                    if (client.fetchInput(day, "input-1")) {
                        ConsoleLog.info("Input for day " + day + " ready.");
                    } else {
                        ConsoleLog.error("Unable to retrieve input for day " + day + ".");
                    }
                } else {
                    ConsoleLog.warn("Input file already exists: " + inputPath);
                }

            } catch (Exception e) {
                ConsoleLog.error("Error creating day " + day + ": " + e.getMessage());
                e.printStackTrace(System.err);
            }
        }
    }

    @Command(name = "submit", description = "Submit an answer for a specific day/part")
    static class SubmitCommand implements Runnable {
        @Option(names = {"-d", "--day"}, required = true, description = "Day number")
        int day;

        @Option(names = {"-p", "--part"}, required = true, description = "Part number (1 or 2)")
        int part;

        @Option(names = {"-a", "--answer"}, required = true, description = "Answer to submit")
        String answer;

        @Option(names = {"-y", "--year"}, description = "Year (default: current year)")
        Integer year;

        @Override
        public void run() {
            int y = (year != null) ? year : java.time.Year.now().getValue();
            AocClient client = new AocClient(y);
            boolean ok = client.submitAnswer(day, part, answer);
            if (ok) {
                ConsoleLog.info("Submission accepted. You may now fetch input-2 once the site unlocks it.");
            } else {
                ConsoleLog.warn("Submission unsuccessful. See above messages.");
            }
        }
    }
}