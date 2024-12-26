package org.advent2024.day3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.exit;

public class Puzzle6 {
    public static List<String> readFromFile(String fileName) {
        List<String> list = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            exit(1);
        }
        return list;
    }
    public static void main(String[] args) {
        AtomicReference<Boolean> enableInstructions = new AtomicReference<>(true);
        List<String> input = readFromFile("src/main/resources/puzzle5/input.txt");
        AtomicReference<Integer> total = new AtomicReference<>(0);
        input.stream().forEach(line -> {
            String regex = "mul\\((\\d{1,3}),(\\d{1,3})\\)|(do|don't)\\(\\)";
            List<String> allMatches = new ArrayList<>();
            Matcher m = Pattern.compile(regex)
                    .matcher(line);
            while (m.find()) {
                if (m.group().startsWith("don't()")) {
                    System.out.println(m.group() + "disabling instructions");
                    enableInstructions.set(false);
                }
                if (m.group().startsWith("do()")) {
                    System.out.println(m.group() + "enabling instructions");
                    enableInstructions.set(true);
                }
                if (enableInstructions.get() && m.group().startsWith("mul")) {
                    allMatches.add(m.group());
                }
            }
            allMatches.stream().forEach(match -> {
                        // Compile the regex to match integers
                        Pattern pattern = Pattern.compile("\\d+");
                        Matcher matcher = pattern.matcher(match);

                        // Extract integers
                        List<Integer> n = new ArrayList<>();
                        while (matcher.find()) {
                            n.add(Integer.parseInt(matcher.group()));
                        }
                        total.updateAndGet(v -> v + (n.getFirst() * n.getLast()));
                        System.out.println(total.get());

                    }
            );
        });
    }
}
