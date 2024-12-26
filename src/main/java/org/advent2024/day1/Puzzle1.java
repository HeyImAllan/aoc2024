package org.advent2024.day1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class Puzzle1 {
    public static ArrayList<List<Integer>> readListsFromFile(String fileName) {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("   ");
                list1.add(Integer.valueOf(parts[0]));
                list2.add(Integer.valueOf(parts[1]));
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            exit(1);
        }
        ArrayList<List<Integer>> returnList = new ArrayList<>();
        returnList.add(list1);
        returnList.add(list2);
        return returnList;
    }
    public static void solve() {
        ArrayList<List<Integer>> puzzleInput = readListsFromFile("src/main/resources/puzzle1/input.txt");
        List<Integer> list1 = puzzleInput.get(0).stream().sorted().toList();
        List<Integer> list2 = puzzleInput.get(1).stream().sorted().toList();
        Integer answer = 0;
        for (int i = 0; i < list1.size(); i++) {
            Integer difference = list2.get(i) - list1.get(i);
            if (difference < 0) {
                difference = difference * -1;
            }
            answer += difference;
        }
        System.out.println("Answer: " + answer);
    }
}
