package org.advent2024.util;

import java.util.*;

public class StringTokenizer {
    public static void main(String[] args) {
        Set<String> tokens = new HashSet<>();
        tokens.add("ab");
        tokens.add("b");
        tokens.add("c");

        System.out.println(canTokenize("abbc", tokens)); // true
        System.out.println(canTokenize("ubbcc", tokens)); // false
    }

    public static boolean canTokenize(String inputString, Set<String> tokens) {
        return canTokenize(inputString, tokens, new HashMap<>());
    }
    public static boolean canTokenize(String inputString, Set<String> tokens, Map<String, Boolean> memo) {
        if (memo.containsKey(inputString)) {
            return memo.get(inputString);
        }

        for (String token : tokens) {
            if (token.equals(inputString)) {
                memo.put(inputString, true);
                return true;
            }
            if (inputString.startsWith(token)) {
                String remaining = inputString.substring(token.length());
                if (canTokenize(remaining, tokens)) {
                    memo.put(inputString, true);
                    return true;
                }
            }
        }
        memo.put(inputString, false);
        return false;
    }
    // Interestingly, this doesn't scale, and caching is faster. Keeping it for reference.
    public static boolean canTokenizeFaster(String inputString, Set<String> tokens) {
        int inputLength = inputString.length();
        boolean[] canTokenizeUpTo = new boolean[inputLength + 1];
        canTokenizeUpTo[0] = true;

        for (int i = 1; i <= inputLength; i++) {
            for (String token : tokens) {
                int tokenLength = token.length();
                if (i >= tokenLength && inputString.substring(i - tokenLength, i).equals(token)) {
                    if (canTokenizeUpTo[i - tokenLength]) {
                        canTokenizeUpTo[i] = true;
                    }
                }
            }
        }

        return canTokenizeUpTo[inputLength];
    }

    public static long countCanTokenize(String inputString, Set<String> tokens) {
        return countCanTokenize(inputString, tokens, new HashMap<>());
    }

    public static long countCanTokenize(String inputString, Set<String> tokens, Map<String, Long> memo) {
        if (memo.containsKey(inputString)) {
            return memo.get(inputString);
        }
        long count = 0L;
        for (String token : tokens) {
            if (token.equals(inputString)) {
                ++count;
            } else if (inputString.startsWith(token)) {
                String remaining = inputString.substring(token.length());
                count += countCanTokenize(remaining, tokens, memo);
            }
        }
        memo.put(inputString, count);
        return count;
    }
}
