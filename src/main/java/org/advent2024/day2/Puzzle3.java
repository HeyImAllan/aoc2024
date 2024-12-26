package org.advent2024.day2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.exit;

public class Puzzle3 {
    public static List<List<Integer>> readListsFromFile(String fileName) {
        List<List<Integer>> list = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] listOfStrings = line.split(" ");
                List<Integer> listOfIntegers = Arrays.stream(listOfStrings).map(Integer::parseInt).collect(Collectors.toList());
                list.add(listOfIntegers);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            exit(1);
        }
        return list;
    }

    public static void solve() {
        List<List<Integer>> list = readListsFromFile("src/main/resources/puzzle3/input.txt");

        Long answer = list.stream().filter(Puzzle3::checkSafe).count();

        System.out.println(answer);
    }

    public static boolean checkSafe(List<Integer> report) {
//        if (!checkIfAllPositive(report) && !checkIfAllNegative(report)) {
//            System.out.println("Unsafe report, not all same: " + report);
//            return false;
//        }
        if (!isSequentiallyDecreasing(report) && !isSequentiallyIncreasing(report)) {
            System.out.println("Unsafe report, not all increasing/decreasing: " + report);
            return false;
        }
        for (int i = 0; i < report.size() - 1; i++) {
            Integer change = Math.abs(report.get(i + 1) - report.get(i));
            if  (!(change <= 3 && change >= 1)) {
                System.out.println("Unsafe report: " + report);
                return false;
            }
        }
        return true;
    }
    public static boolean checkIfAllPositive(List<Integer> report){
        for (int i = 0; i < report.size(); i++) {
            if (report.get(i) < 0) {
                return false;
            }
        }
        return true;
    }
    public static boolean checkIfAllNegative(List<Integer> report){
        for (int i = 0; i < report.size(); i++) {
            if (report.get(i) > 0) {
                return false;
            }
        }
        return true;
    }
    public static Boolean isSequentiallyIncreasing(List<Integer> items) {
        for (Integer i = 1; i < items.size(); i++) {
            Boolean incremental = items.get(i - 1) < items.get(i);
            if (!incremental) return false;
        }
        return true;
    }
    public static Boolean isSequentiallyDecreasing(List<Integer> items) {
        for (Integer i = 1; i < items.size(); i++) {
            Boolean incremental = items.get(i - 1) > items.get(i);
            if (!incremental) return false;
        }
        return true;
    }
}