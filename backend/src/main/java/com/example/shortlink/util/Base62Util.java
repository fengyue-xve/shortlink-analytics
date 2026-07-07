package com.example.shortlink.util;

public class Base62Util {
    private static final char[] CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    public static String encode(long value) {
        if (value == 0) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        while (value > 0) {
            sb.append(CHARS[(int) (value % 62)]);
            value = value / 62;
        }
        return sb.reverse().toString();
    }
}
