package advent.util;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class MathUtils {
    private MathUtils() {}

    public static <T> Map<T, Long> frequency(Collection<T> list) {
        return list.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public static int sumInt(List<Integer> list) {
        return list.stream().mapToInt(Integer::intValue).sum();
    }

    public static long sumLong(List<Long> list) {
        return list.stream().mapToLong(Long::longValue).sum();
    }

    public static int productInt(List<Integer> list) {
        return list.stream().reduce(1, (a, b) -> a * b);
    }

    public static long productLong(List<Long> list) {
        return list.stream().reduce(1L, (a, b) -> a * b);
    }

    public static int gcd(int a, int b) {
        return b == 0 ? Math.abs(a) : gcd(b, a % b);
    }

    public static int gcd(List<Integer> list) {
        return list.stream().reduce(0, MathUtils::gcd);
    }

    public static long lcm(long a, long b) {
        return Math.abs(a * b) / gcd((int)a, (int)b);
    }

    public static long lcm(List<Long> list) {
        return list.stream().reduce(1L, MathUtils::lcm);
    }
}