package com.arcaryx.cobblemoninfo.util;

import java.util.ArrayList;
import java.util.List;

public class TextUtils {

    public static String basicPluralize(String input, int count){
        return "%s%s".formatted(input, count > 1 ? "s" : "");
    }

    public static List<String> wrapString(String input, int maxLength) {
        List<String> wrappedLines = new ArrayList<>();
        String[] words = input.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (currentLine.length() + word.length() + 1 > maxLength) {
                wrappedLines.add(currentLine.toString());
                currentLine = new StringBuilder();
            }
            if (currentLine.length() > 0) {
                currentLine.append(" ");
            }
            currentLine.append(word);
        }

        if (currentLine.length() > 0) {
            wrappedLines.add(currentLine.toString());
        }

        return wrappedLines;
    }
}
