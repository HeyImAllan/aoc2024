package org.advent2024.day11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.advent2024.day6.Puzzle12.readFromFile;

public class Puzzle22 {
    static List<Long> puzzle = new ArrayList<>();
    static Map<Map<Long, Integer>, Long> puzzleCache = new HashMap<>();
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        List<String> input = readFromFile("src/main/resources/puzzle21/input.txt");
        puzzle = covertToLongList(input);
//        System.out.println(puzzle);
//
//        System.out.println(blink(175L, 6));
//        System.out.println(blink(17L, 6));

        long answer = 0;
        for (Long element: puzzle) {
            answer += blink(element, 1500);
        }
        System.out.println(answer);
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);

    }
    private static List<Long> covertToLongList(List<String> input) {
        List<Long> disk = new ArrayList<>();
        String[] rawDisk = input.getFirst().split(" ");
        for (int i = 0; i < rawDisk.length; i++) {
            disk.add(Long.parseLong(rawDisk[i]));
        }
        return disk;
    }
    public static long blink(Long puzzle, int loops) {
        if (puzzleCache.containsKey(Map.of(puzzle, loops))) {
            return puzzleCache.get(Map.of(puzzle, loops));
        }

        List<Long> newlist = new ArrayList<>();
        long answer = 0;
        if (loops == 0) {
            return 1;
        }
        if (puzzle == 0) {
            newlist.add(1L);
        } else if (String.valueOf(puzzle).length() % 2 == 0) {
            String tempString = String.valueOf(puzzle);
            newlist.add(Long.parseLong(tempString.substring(0, (tempString.length() / 2))));
            newlist.add(Long.parseLong(tempString.substring((tempString.length() / 2))));
        } else {
            newlist.add(puzzle * 2024L);
        }

        for (Long element : newlist) {
            long output = blink(element, loops -1);
            puzzleCache.putIfAbsent(Map.of(element, loops -1 ), output);
            answer+=output;
        }
        puzzleCache.putIfAbsent(Map.of(puzzle, loops), answer);
        return answer;

    }
}
