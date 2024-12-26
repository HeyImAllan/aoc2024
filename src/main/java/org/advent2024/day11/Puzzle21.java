package org.advent2024.day11;

import java.util.ArrayList;
import java.util.List;

import static org.advent2024.day6.Puzzle12.readFromFile;

public class Puzzle21 {
    static List<Long> puzzle = new ArrayList<>();
    public static void main(String[] args) {
        List<String> input = readFromFile("src/main/resources/puzzle21/input.txt");
        puzzle = covertToLongList(input);
        System.out.println(puzzle);
        List<Long> singleElePuzzle = List.of(125L);
        singleElePuzzle = blink(singleElePuzzle, 6);

        System.out.println(singleElePuzzle.size());
        List<Long> singleElePuzzle2 = List.of(17L);
        singleElePuzzle2 = blink(singleElePuzzle2,6);

        System.out.println(singleElePuzzle2.size());

        puzzle = blink(puzzle,25);
        System.out.println(puzzle.size());


    }
    private static List<Long> covertToLongList(List<String> input) {
        List<Long> disk = new ArrayList<>();
        String[] rawDisk = input.getFirst().split(" ");
        for (int i = 0; i < rawDisk.length; i++) {
            disk.add(Long.parseLong(rawDisk[i]));
        }
        return disk;
    }
    public static List<Long> blink(List<Long> puzzle, int loops) {

        if (loops == 0) {
            return puzzle;
        }
        List<Long> newlist = new ArrayList<>(puzzle);
        int j = 0;
        for (int i = 0; i < puzzle.size(); i++) {
            if (puzzle.get(i) == 0) {
                newlist.set(j, 1L);
                j++;
            } else if (String.valueOf(puzzle.get(i)).length() % 2 == 0) {
                String tempString = String.valueOf(puzzle.get(i));
                newlist.set(j, Long.parseLong(tempString.substring(0, (tempString.length() / 2))));
                newlist.add(j + 1, Long.parseLong(tempString.substring((tempString.length() / 2))));
                j += 2;
            } else {
                newlist.set(j, (puzzle.get(i) * 2024L));
                j++;
            }

        }
        return blink(newlist, loops -1);

    }
}
