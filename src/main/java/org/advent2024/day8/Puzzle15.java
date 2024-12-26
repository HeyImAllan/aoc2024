package org.advent2024.day8;

import java.util.*;

import static org.advent2024.day6.Puzzle12.readFromFile;


public class Puzzle15 {
    public static List<List<String>> map = new ArrayList<>();
    static List<List<Integer>> allAntiNodes = new ArrayList<>();

    public static void main(String[] args) {
        List<String> input = readFromFile("src/main/resources/puzzle15/input.txt");
        map = buildmap(input);
        printMap(map);
        int answer = 0;
        List<String> uniqueChars = getUniqueChars(map);
        for (String uniqueChar : uniqueChars) {
            List<List<Integer>> coordinates = getCoordinates(uniqueChar);
            System.out.println("Coordinates for " + uniqueChar + ": " + coordinates);
            List<List<Integer>> antiNodes = getAntinodes(coordinates);
            System.out.println("Antinodes for " + uniqueChar + ": " + antiNodes);
            answer += antiNodes.size();
        }
        System.out.println(answer);
        printMapWithAntinodes(map, allAntiNodes);
    }

    private static List<List<Integer>> getAntinodes(List<List<Integer>> coordinates) {
        List<List<Integer>> antiNodes = new ArrayList<>();
        for (int i = 0; i < coordinates.size() - 1; i++) {
            for (int j = i+1; j < coordinates.size(); j++) {
                System.out.println(i + ", " + j );
                List<Integer> distance = new ArrayList<>();
                distance.add(Math.abs(coordinates.get(i).get(0) - coordinates.get(j).get(0)));
                distance.add(Math.abs(coordinates.get(i).get(1) - coordinates.get(j).get(1)));
                List<Integer> antiNode1 = new ArrayList<>();
                List<Integer> antiNode2 = new ArrayList<>();
                //System.out.println("Distance: " + distance);
                antiNode1.add(coordinates.get(i).get(0) - distance.get(0));
                antiNode2.add(coordinates.get(j).get(0) + distance.get(0));
                if (coordinates.get(i).get(1) > coordinates.get(j).get(1)) {
                    antiNode1.add(coordinates.get(i).get(1) + distance.get(1));
                    antiNode2.add(coordinates.get(j).get(1) - distance.get(1));
                } else {
                    antiNode1.add(coordinates.get(i).get(1) - distance.get(1));
                    antiNode2.add(coordinates.get(j).get(1) + distance.get(1));
                }

                if (isValidNode(antiNode1)) {
                    //System.out.println("AntiNode1: " + antiNode1);
                    antiNodes.add(antiNode1);
                    allAntiNodes.add(antiNode1);
                }
                if (isValidNode(antiNode2)) {
                    //System.out.println("AntiNode2: " + antiNode2);
                    antiNodes.add(antiNode2);
                    allAntiNodes.add(antiNode2);
                }

            }
        }
        return antiNodes;
    }

    private static boolean isValidNode(List<Integer> antiNode) {
        if (!outOfBounds(antiNode)) {
            if (!allAntiNodes.contains(antiNode)) {
                return true;
            }

        }
        return false;
    }

    private static List<List<Integer>> getCoordinates(String uniqueChar) {
        List<List<Integer>> coordinates = new ArrayList<>();
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (map.get(i).get(j).equals(uniqueChar)) {
                    List<Integer> coordinate = new ArrayList<>();
                    coordinate.add(i);
                    coordinate.add(j);
                    coordinates.add(coordinate);
                }
            }
        }
        return coordinates;
    }

    public static boolean outOfBounds(List<Integer> coordinate) {
                return coordinate.get(0) < 0 || coordinate.get(0) >= map.size() ||
                    coordinate.get(1) < 0 || coordinate.get(1) >= map.get(coordinate.get(0)).size() ;
    }

    private static List<String> getUniqueChars(List<List<String>> map) {
        Set<String> treeSet = new TreeSet<>();
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (!map.get(i).get(j).equals(".")) {
                    treeSet.add(map.get(i).get(j));
                }
            }
        }
        System.out.println(treeSet);
        return new ArrayList<>(treeSet);
    }


    private static List<List<String>> buildmap(List<String> input) {
        List<List<String>> map = new ArrayList<>();
        for (String line : input) {
            map.add(List.of(line.split("")));
        }
        return map;
    }
    private static void printMap(List<List<String>> map){
        for (int i = 0; i < map.size() ; i++) {
            for (int j = 0; j < map.get(i).size() ; j++) {
                System.out.print(map.get(i).get(j));
            }
            System.out.print("\n");
        }
    }
    private static void printMapWithAntinodes(List<List<String>> map, List<List<Integer>> list){
        for (int i = 0; i < map.size() ; i++) {
            for (int j = 0; j < map.get(i).size() ; j++) {
                List<Integer> coordinates = new ArrayList<>();
                coordinates.add(i);
                coordinates.add(j);

                if (list.contains(coordinates)) {
                    System.out.print("#");
                } else {
                    System.out.print(map.get(i).get(j));
                }
            }
            System.out.print("\n");
        }
    }

    public static String getFromMap(int row, int col) {
        return map.get(row).get(col);
    }
}
