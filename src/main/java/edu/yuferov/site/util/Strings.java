package edu.yuferov.site.util;

import java.util.Collections;

public class Strings {
    public static String repeated(String s, int times) {
        if (times <= 0) {
            throw new IllegalArgumentException();
        }
        return String.join("", Collections.nCopies(times, s));
    }
}
