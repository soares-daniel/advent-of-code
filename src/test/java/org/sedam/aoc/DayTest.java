package org.sedam.aoc;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class DayTest {

    @Test
    public void testDay() throws Exception {
        // Get the day number from the system property
        String dayNumber = System.getProperty("day");
        if (dayNumber == null || dayNumber.isEmpty()) {
            throw new IllegalArgumentException("Please provide the day number with -Dday (e.g. -Dday=1)");
        }
        int day = Integer.parseInt(dayNumber);

        // Dynamically load the corresponding Day object
        String className = "org.sedam.aoc.Day" + day;
        Class<?> dayClass;
        try {
            dayClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Day " + day + " not implemented yet");
        }
        Day dayObject = (Day) dayClass.getDeclaredConstructor().newInstance();

        // Execute the test
        testPart(day, dayObject, 1);
        testPart(day, dayObject, 2);
    }

    private void testPart(int day, Day dayObject, int part) throws Exception {
        // Prepare input files
        List<String> testInput = TestUtils.readInputFiles(day, part, "test-");
        List<String> realInput = TestUtils.readInputFiles(day, part, "");

        // Dynamically get the method corresponding to the part (part1, part2)
        String methodName = "part" + part;
        Method partMethod = dayObject.getClass().getMethod(methodName, List.class);

        // Test Part
        if (testInput != null) { // Omits the test if there is no test input
            String testPartResult = (String) partMethod.invoke(dayObject, testInput);
            String expectedTestResult = TestUtils.readExpectedResult(day, part);
            assertEquals(expectedTestResult, testPartResult, "\nDay " + day + " - Part " + part + " is not matching the expected result.");

        }
        // Real Part
        String partResult = (String) partMethod.invoke(dayObject, realInput);
        System.out.println("\n\n====== Day " + day + " - Part " + part + " Result: " + partResult + " ======\n\n");
    }

}
