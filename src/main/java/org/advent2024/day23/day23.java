package org.advent2024.day23;

import java.util.*;
import java.util.stream.Collectors;

import static org.advent2024.day6.Puzzle12.readFromFile;

public class day23 {
    public static void main(String[] args) {
        System.out.println("day23");
        List<String> input = readFromFile("src/main/resources/day23/input.txt");
        Map<String, List<String>> networks = new HashMap<>();
        for (String line : input) {
            String[] parts = line.split("-");
            String computer = parts[0];
            String otherComputer = parts[1];
            networks.computeIfAbsent(computer, k -> new ArrayList<>()).add(otherComputer);
            networks.computeIfAbsent(otherComputer, k -> new ArrayList<>()).add(computer);
        }
        Set<Set<String>> groups = findGroupsOf3withT(networks);
        System.out.println("part 1: " + groups.size());
        Set<Set<String>> allGroups = findGroups(networks);
        Set<String> largest = allGroups.stream().max(Comparator.comparingInt(Set::size)).orElseThrow();
        System.out.println("part 2: " + largest.stream().sorted().collect(Collectors.joining(",")));
    }

    public static Set<Set<String>> findGroupsOf3withT(Map<String, List<String>> networks) {
        Set<Set<String>> groups = new HashSet<>();
        for (String computer : networks.keySet()) {
            if (computer.startsWith("t")) {
                for (String otherComputer : networks.get(computer)) {
                    if (!otherComputer.equals(computer)) {
                        for (String otherComputerOtherComputer : networks.get(otherComputer)) {
                            if (!otherComputerOtherComputer.equals(computer) && networks.get(otherComputerOtherComputer).contains(computer)) {
                                groups.add(Set.of(computer, otherComputer, otherComputerOtherComputer));
                            }
                        }
                    }
                }
            }
        }
        return groups;
    }

    private static Set<Set<String>> findGroups(Map<String, List<String>> network) {
        Set<Set<String>> groups = new HashSet<>();
        for (String startComputer : network.keySet()) {
            Set<String> visitedNodes = new HashSet<>();
            Set<String> currentGroup = new HashSet<>();
            Queue<String> queue = new ArrayDeque<>();
            queue.add(startComputer);
            while (!queue.isEmpty()) {
                String currentComputer = queue.poll();
                if (!visitedNodes.contains(currentComputer) && new HashSet<>(network.get(currentComputer)).containsAll(currentGroup)) {
                    currentGroup.add(currentComputer);
                    visitedNodes.add(currentComputer);
                    for (String otherComputer : network.get(currentComputer)) {
                        if (!visitedNodes.contains(otherComputer)) {
                            queue.add(otherComputer);
                        }
                    }
                }
            }
            groups.add(currentGroup);
        }
        return groups;
    }







}

