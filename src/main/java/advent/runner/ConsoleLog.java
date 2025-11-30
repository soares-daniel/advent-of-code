package advent.runner;

public final class ConsoleLog {
    private static final String RESET  = "\u001B[0m";
    private static final String GREEN  = "\u001B[1;32m";
    private static final String YELLOW = "\u001B[1;33m";
    private static final String RED    = "\u001B[1;31m";
    private static final String BLUE = "\u001B[1;36m";

    private ConsoleLog() {}

    public static void success(String msg) {
        System.out.println(BLUE + "[OK]    " + RESET + msg);
    }
    public static void headline(String msg) {
        System.out.println(BLUE   + msg + RESET);
    }

    public static void info(String message) {
        System.out.println(GREEN + "[INFO] " + RESET + message);
    }

    public static void warn(String message) {
        System.out.println(YELLOW + "[WARN] " + RESET + message);
    }

    public static void error(String message) {
        System.err.println(RED + "[ERROR] " + RESET + message);
    }
}