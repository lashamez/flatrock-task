package com.flatrock.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static List<String> wrapText(String sourceString, int maxCharactersLength) {
        List<String> result = new ArrayList<>();
        String[] words = sourceString.split(" ");
        int currentLen = 0;
        List<String> currentLine = new ArrayList<>();
        for (String word : words) {
            currentLen += word.length();
            if (currentLen + currentLine.size() > maxCharactersLength) {
                result.add(String.join(" ", currentLine));
                currentLine.clear();
                currentLen = word.length();
            }
            currentLine.add(word);

        }
        if (!currentLine.isEmpty()) {
            result.add(String.join(" ", currentLine));
        }
        return result;
    }
    public static List<String> wrapText2(String sourceString, int maxCharactersLength) {
        List<String> result = new ArrayList<>();
        List<Integer> spaces = IntStream.range(0, sourceString.length())
                .filter(i ->sourceString.charAt(i) == ' ')
                .boxed()
                .toList();
        int curInd = 0;
        for (int i = 0; i < spaces.size(); i++) {
            if (spaces.get(i) - curInd > maxCharactersLength) {
                result.add(sourceString.substring(curInd, spaces.get(i - 1)));
                curInd = spaces.get(i - 1) + 1;
            }
        }
        result.add(sourceString.substring(curInd));
        return result;
    }

    public static void main(String[] args) {
        String sourceString = "Once you free your mind about a concept of " + "Harmony and of music being correct " + "You can do whatever you want " + "So nobody told me what to do " + "And there was no preconception of what to do";
        List<String> list = wrapText2("", 13);
        List<String> result = Arrays.asList("Once you free", "your mind", "about a", "concept of", "Harmony and", "of music", "being correct", "You can do", "whatever you", "want So", "nobody told", "me what to do", "And there was", "no", "preconception", "of what to do");
        boolean equals = (list.size() == result.size());
        if (equals) {
            for (int i = 0; i < list.size() && equals; i++) {
                equals = list.get(i).equals(result.get(i));
            }
        }
        if (equals) {
            System.out.println("Test Passed");
        } else {
            System.out.println("Test Failed");
            System.out.println("\n");
            System.out.println("Expected: ");
            System.out.println(result);
            System.out.println("\n");
            System.out.println("Actual: ");
            System.out.println(list);
        }
    }
}
