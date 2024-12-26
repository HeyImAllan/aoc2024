package org.advent2024.day24;

import java.util.*;

import static org.advent2024.day6.Puzzle12.readFromFile;

public class day24 {
    static Map<String, Integer> values = new HashMap<>();
    static Map<String, Integer> p1answers = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("day24");
        List<String> input = readFromFile("src/main/resources/day24/input.txt");
        Queue<List<String>> calculations = new ArrayDeque<>();
        Set<String> wrong = new HashSet<>();
        boolean part1 = true;
        for (String line : input) {
            if (part1) {
                if (line.isEmpty()) {
                    part1 = false;
                    continue;
                }
                //System.out.println(line);
                values.put(line.split(":")[0], Integer.parseInt(line.split(":")[1].trim()));

            } else {
                //System.out.println(line);
                String[] parts = line.split(" ");

                if (!values.containsKey(parts[0]) || !values.containsKey(parts[2])) {
                    calculations.add(List.of(parts[4], parts[0], parts[1], parts[2]));
                    p1answers.put(parts[4], 0);
                } else {
                    values.put(parts[4], calculate(parts[0], parts[1], parts[2]));
                    p1answers.put(parts[4], values.get(parts[4]));
                }
                // Checking for correctness.
                if (parts[4].startsWith("z") && !parts[1].equals("XOR") && !parts[4].equals("z45")) {
                    //System.out.println("WRONG0! " + line);
                    wrong.add(parts[4]);
                }
                if (parts[1].equals("XOR") &&
                        !List.of("x", "y" , "z").contains(parts[4].split("")[0]) &&
                        !List.of("x","y","z").contains(parts[0].split("")[0]) &&
                        !List.of("x","y","z").contains(parts[2].split("")[0])
                ) {
                    //System.out.println("WRONG2! " + line);
                    wrong.add(parts[4]);
                }
                if (parts[1].equals("AND") && (!parts[0].equals("x00")) && (!parts[2].equals("x00"))) {
                    for (String subline: input) {
                        String[] subparts = subline.split(" ");
                        if (subparts.length > 2) {
                            if ((parts[4].equals(subparts[0]) || parts[4].equals(subparts[2])) && !subparts[1].equals("OR")) {
                                //System.out.println("WRONG3! " + line);
                                wrong.add(parts[4]);
                            }
                        }
                    }
                }
                if (parts[1].equals("XOR")) {
                    for (String subline : input) {
                        String[] subparts = subline.split(" ");
                        if (subparts.length > 2) {
                            if ((parts[4].equals(subparts[0]) || parts[4].equals(subparts[2])) && subparts[1].equals("OR")) {
                                //System.out.println("WRONG4! " + line);
                                wrong.add(parts[4]);
                            }
                        }
                    }
                }
            }

        }
        while (!calculations.isEmpty()) {
            List<String> calculation = calculations.poll();
            if (!values.containsKey(calculation.get(1)) || !values.containsKey(calculation.get(3))) {
                calculations.add(calculation);
            } else {
                values.put(calculation.get(0), calculate(calculation.get(1), calculation.get(2), calculation.get(3)));
                p1answers.put(calculation.get(0), values.get(calculation.get(0)));
            }
        }
        List<String> p1values = p1answers.keySet().stream().sorted().filter(e -> e.startsWith("z")).map(p1answers::get).map(String::valueOf).toList();
        //System.out.println(p1values);
        System.out.println("part1: " + Long.parseLong(
                String.join("", p1values.reversed()), 2));
        System.out.println("part2: " + String.join( ",", wrong.stream().sorted().toList()));

    }

    private static Integer calculate(String part, String operand, String part2) {
        if (operand.equals("AND")) {
            return values.get(part) & values.get(part2);
        }
        if (operand.equals("OR")) {
            return values.get(part) | values.get(part2);
        }
        if (operand.equals("XOR")) {
            return values.get(part) ^ values.get(part2);
        }
        return 0;
    }
}
