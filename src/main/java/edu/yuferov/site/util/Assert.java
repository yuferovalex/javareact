package edu.yuferov.site.util;

public class Assert {
    public static void requireNonNull(Object object) {
        if (object == null) {
            throw new IllegalArgumentException();
        }
    }

    public static void requireNonNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requireNonBlank(String s) {
        if (s == null || s.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public static void requireNonBlank(String s, String message) {
        if (s == null || s.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requireMatchTemplate(String s, String regex, String message) {
        if (!s.matches(regex)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void requireMatchTemplate(String s, String regex) {
        if (!s.matches(regex)) {
            throw new IllegalArgumentException();
        }
    }

    public static void requiredLength(String s, int from, int to) {
        int len = s.length();
        if (len < from || len > to) {
            throw new IllegalArgumentException();
        }
    }

    public static void requiredLength(String s, int from, int to, String message) {
        int len = s.length();
        if (len < from || len > to) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertTrue(boolean value) {
        if (!value) {
            throw new RuntimeException();
        }
    }

    public static void assertTrue(boolean value, String message) {
        if (!value) {
            throw new RuntimeException(message);
        }
    }
}
