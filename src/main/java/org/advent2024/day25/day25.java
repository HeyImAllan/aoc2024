package org.advent2024.day25;

import java.util.*;

import static org.advent2024.day6.Puzzle12.readFromFile;

public class day25 {
    private static int schemaSize;

    public static void main(String[] args) {
        List<String> input = readFromFile("src/main/resources/day25/input.txt");
        Queue<Map<Integer, List<String>>> schematics = new ArrayDeque<>();
        Map<Integer, List<String>> schematic = new HashMap<>();
        int i =0;
        for (String line : input) {
            if (line.isEmpty()) {
                schematics.add(schematic);
                schematic = new HashMap<>();
                i = 0;
                continue;
            }
            schematic.put(i, List.of(line.split("")));
            i++;
        }
        schemaSize = schematic.size()-2;
        schematics.add(schematic);
        List<List<Integer>> keys = new ArrayList<>();
        List<List<Integer>> locks = new ArrayList<>();
        while (!schematics.isEmpty()) {
            Map<Integer, List<String>> keyOrLock = schematics.poll();
            if (keyOrLock.get(0).contains("#")) {
                locks.add(parseSchematic(keyOrLock));
            } else {
                keys.add(parseSchematic(keyOrLock));
            }
        }
        processAnswer(keys,locks);
    }

    private static void processAnswer(List<List<Integer>> keys, List<List<Integer>> locks) {
        int answerp1 = 0;
        for (List<Integer> lock: locks) {
            for (List<Integer> key: keys) {
                boolean matches = true;
                for (int i = 0; i < lock.size(); i++) {
                    if ((lock.get(i) + key.get(i)) > schemaSize) {
                        //System.out.println("Lock " + lock + " Key " + key + " don't fit on col: " + i);
                        matches = false;
                        break;
                    }
                }
                if (matches) {
                    answerp1++;
                }
            }
        }
        System.out.println(answerp1);
    }

    private static List<Integer> parseSchematic(Map<Integer, List<String>> keyOrLock) {
        List<Integer> lockOrKey = new ArrayList<>();
        for (int i = 0; i < keyOrLock.get(0).size(); i++) {
            lockOrKey.add(i, 0);
        }
        for (int j = 1; j < keyOrLock.size()-1; j++) {
            for (int k = 0; k < keyOrLock.get(j).size(); k++) {
                if (keyOrLock.get(j).get(k).contains("#")) {
                    lockOrKey.set(k, lockOrKey.get(k) + 1);
                }
            }
        }
        return lockOrKey;
    }
}