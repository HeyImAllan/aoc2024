package org.advent2024.day17;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.advent2024.day6.Puzzle12.readFromFile;

public class Puzzle34{
    private static int regA = 0;
    private static int regB = 0;
    private static int regC = 0;
    private static List<Integer> instructions = List.of();

    public static String part1() {
        Computer computer = new Computer(regA, regB, regC, instructions);
        computer.execute();
        return computer.printOutput();
    }

    public static String part2() {
        List<Integer> remainingProgram = new ArrayList<>(instructions);
        List<Integer> program = new ArrayList<>();
        long registerA = 0L;
        while (!remainingProgram.isEmpty()) {
            --registerA;
            program.addFirst(remainingProgram.removeLast());
            String programString = program.stream().map(Long::toString).collect(Collectors.joining(","));
            Computer computer;
            do {
                ++registerA;
                computer = new Computer(registerA, regB, regC, instructions);
                computer.execute(true, program);
            } while (!computer.printOutput().equals(programString));
            if (!remainingProgram.isEmpty()) {
                registerA = registerA << 3;
            }
        }
        return Long.toString(registerA);
    }

    private static class Computer {
        private long registerA;
        private long registerB;
        private long registerC;
        private final List<Integer> opcodes;
        private final List<Integer> output = new ArrayList<>();
        private int instructionPointer;

        Computer(long registerA, int registerB, int registerC, List<Integer> opcodes) {
            this.registerA = registerA;
            this.registerB = registerB;
            this.registerC = registerC;
            this.opcodes = opcodes;
            this.instructionPointer = 0;
        }

        void execute() {
            execute(false, List.of());
        }

        void execute(boolean earlyStop, List<Integer> expectedOutput) {
            while (instructionPointer < opcodes.size()) {
                int literalOperand = opcodes.get(instructionPointer + 1);
                long combinedOperand = getCombinedOperand(opcodes.get(instructionPointer + 1));
                boolean skipIncrement = false;
                switch (opcodes.get(instructionPointer)) {
                    case 0 -> {
                        long denominator = (long) Math.pow(2, combinedOperand);
                        registerA = registerA / denominator;
                    }
                    case 1 -> registerB = registerB ^ ((long) literalOperand);
                    case 2 -> registerB = combinedOperand % 8;
                    case 3 -> {
                        if (registerA != 0) {
                            instructionPointer = literalOperand;
                            skipIncrement = true;
                            if (earlyStop && !outputMatches(expectedOutput)) {
                                return;
                            }
                        }
                    }
                    case 4 -> registerB = registerB ^ registerC;
                    case 5 -> output.add((int) (combinedOperand % 8L));
                    case 6 -> {
                        long denominator = (long) Math.pow(2, combinedOperand);
                        registerB = registerA / denominator;
                    }
                    case 7 -> {
                        long denominator = (long) Math.pow(2, combinedOperand);
                        registerC = registerA / denominator;
                    }
                }
                if (!skipIncrement) {
                    instructionPointer += 2;
                }
            }
        }

        boolean outputMatches(List<Integer> expectedOutput) {
            if (output.size() > expectedOutput.size()) {
                return false;
            }
            for (int i = 0; i < output.size(); i++) {
                if (!output.get(i).equals(expectedOutput.get(i))) {
                    return false;
                }
            }
            return true;
        }

        long getCombinedOperand(int value) {
            return switch (value % 8) {
                case 0, 1, 2, 3 -> value;
                case 4 -> registerA;
                case 5 -> registerB;
                case 6 -> registerC;
                case 7 -> Long.MIN_VALUE;
                default -> throw new IllegalStateException("Impossible to reach");
            };
        }

        String printOutput() {
            return String.join(",", output.stream().map(Long::toString).toList());
        }
    }

    public static void main(String[] args) {
        List<String> input = readFromFile("src/main/resources/puzzle33/input.txt");
        regA = Integer.parseInt(input.get(0).split("Register A: ")[1]);
        regB = Integer.parseInt(input.get(1).split("Register B: ")[1]);
        regC = Integer.parseInt(input.get(2).split("Register C: ")[1]);

        String instructionRegex = "\\d+";
        Matcher matcher = Pattern.compile(instructionRegex)
                .matcher(input.get(4).split("Program: ")[1]);
        List<Integer> instructionsList = new ArrayList<>();
        while (matcher.find()) {
            instructionsList.add(Integer.valueOf(matcher.group()));
        }
        instructions = Collections.unmodifiableList(instructionsList);
        System.out.println(part1());
        System.out.println(part2());
    }
}