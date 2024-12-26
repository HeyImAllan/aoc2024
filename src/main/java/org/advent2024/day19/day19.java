package org.advent2024.day19;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.advent2024.day6.Puzzle12.readFromFile;
import static org.advent2024.util.StringTokenizer.*;

public class day19 {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        List<String> input = readFromFile("src/main/resources/day19/input.txt");
        Set<String> tokens = new HashSet<>(List.of(Arrays.asList(input.get(0).split(",")).stream().map(String::trim).toArray(String[]::new)));
        System.out.println(canTokenize("brwrrb", tokens)); // true
        int part1 = 0;
        for (int i = 2; i < input.size(); i++) {
            //System.out.println("Checking " + input.get(i));
            if (canTokenizeFaster(input.get(i), tokens)) {
                part1++;
            }
        }
        System.out.println(part1);
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total runtime p1 :" + totalTime + "ms");
        startTime = System.currentTimeMillis();
        long part2 = 0L;
        for (int i = 2; i < input.size(); i++) {

            if (canTokenizeFaster(input.get(i), tokens)) {
                //System.out.println("Checking " + input.get(i));
                part2 += countCanTokenize(input.get(i), tokens);
            }
        }
        System.out.println(part2);
        endTime   = System.currentTimeMillis();
        totalTime = endTime - startTime;
        System.out.println("Total runtime p2 :" + totalTime + "ms");

    }
}
