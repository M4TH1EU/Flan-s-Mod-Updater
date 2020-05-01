package ch.m4th1eu.flansupdater;

public class Logger {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String RED_BOLD = "\033[1;31m";    // RED

    public static void error(String message) {
        System.out.println(ANSI_RED + "[ERROR] " + message + ANSI_RESET);
    }

    public static void info(String message) {
        System.out.println(ANSI_GREEN + "[INFO] " + message + ANSI_RESET);
    }

    public static void warn(String message) {
        System.out.println(ANSI_YELLOW + "[WARN] " + message + ANSI_RESET);
    }

    public static void fatal(String message) {
        System.out.println(RED_BOLD + "[FATAL] " + message + ANSI_RESET);
    }
}
