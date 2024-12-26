package org.advent2024.day1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.advent2024.day1.Puzzle1.readListsFromFile;

public class Puzzle2 {

    public static void solve() {
        ArrayList<List<Integer>> puzzleInput = readListsFromFile("src/main/resources/puzzle1/input.txt");
        List<Integer> list1 = puzzleInput.get(0).stream().sorted().toList();
        List<Integer> list2 = puzzleInput.get(1).stream().sorted().toList();
        Long answer = 0L;
        for (int i = 0; i < list1.size() - 1; i++) {
            int integer1 = list1.get(i);
            System.out.println("Processing " + integer1);
            long count = list2.stream()
                    .filter(integer2 -> Objects.equals(integer2, integer1))
                    .count();
            answer += integer1 * count;
        }
        System.out.println(answer);
    }
}
