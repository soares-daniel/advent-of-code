package advent.util;

public final class BitUtils {
    private BitUtils() {}

    public static long binaryToDecimal(String s) {
        return Long.parseLong(s, 2);
    }

    public static String decimalToBinary(long n, int size) {
        String s = Long.toBinaryString(n);
        return "0".repeat(Math.max(0, size - s.length())) + s;
    }

    public static int countBits(long n) {
        return Long.bitCount(n);
    }
}