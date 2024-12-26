package org.advent2024.day7;

import java.util.ArrayList;
import java.util.List;

import static org.advent2024.day6.Puzzle12.readFromFile;

public class Puzzle13 {
    private static final String[] OPERATORS = {"+", "*", "||"};
    static long total = 0L;

    public static void main(String argsp[]) {
        int answer = 0;
        List<String> input = readFromFile("src/main/resources/puzzle13/input.txt");
        for (String line : input) {
            Long sum = Long.parseLong((line.split(":"))[0]);
            String rawListOfNumbers = (line.split(":"))[1];
            List<String> listOfNumbers = List.of(rawListOfNumbers.split(" "));
            List<Long> numbers = new ArrayList<>();
            for (String number : listOfNumbers) {
                if (!number.isEmpty()) {
                    numbers.add(Long.parseLong(number));
                }
            }
            System.out.println("Testing for: " + sum + " with: " + numbers);
            if (calculateAnswer(numbers, sum)) {
                answer++;
            }
        }

        System.out.println("Correct lines: " + answer);
        System.out.println("Answer " + total);
    }

    private static boolean calculateAnswer(List<Long> numbers, long testValue) {
        StringBuilder expression = new StringBuilder();

        for (int i = 0; i < numbers.size(); i++) {
            if (i == 0) {
                expression.append(numbers.getFirst());
                continue;
            }

            if (testNumbers(numbers, i, expression, testValue)) {
                return true;
            }

        }
        return false;

    }

    private static boolean testNumbers(List<Long> numbers, int i, StringBuilder expression, long testValue) {
        StringBuilder newExpression;
        if (i == numbers.size() - 1) {
            for (String operator : OPERATORS) {
                newExpression = new StringBuilder(expression);
                newExpression.append(' ').append(operator).append(' ').append(numbers.get(i));
                long result = evaluate(newExpression.toString());
                if (result == testValue) {
                    System.out.println("We have a match! " + newExpression + " = " + result);
                    total += result;
                    return true;
                }

            }
        }
        if (i <= numbers.size()) {
            for (String operator : OPERATORS) {
                newExpression = new StringBuilder(expression);
                newExpression.append(' ').append(operator).append(' ').append(numbers.get(i));

                if (i + 1 >= numbers.size()) {
                    return false;
                } else if (testNumbers(numbers, i + 1, newExpression, testValue)) {
                    return true;
                }
            }
        }
        if (i == numbers.size()) {
            return false;
        }
        return false;
    }

    public static Long evaluate(String expression) {
        String[] tokens = expression.split(" ");
        Long result = Long.parseLong(tokens[0]);
        for (int i = 1; i < tokens.length - 1; i += 2) {
            if (tokens[i].equals("+")) {
                result += Long.parseLong(tokens[i + 1]);
            } else if (tokens[i].equals("-")) {
                result -= Long.parseLong(tokens[i + 1]);
            } else if (tokens[i].equals("*")) {
                result *= Long.parseLong(tokens[i + 1]);
            }
        }
        return result;
        }
    }
