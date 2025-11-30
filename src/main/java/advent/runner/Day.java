package advent.runner;

public interface  Day {
    String name();
    String part1(String input);
    String part2(String input);
    String expectedTestResultPart1();
    String expectedTestResultPart2();
}
