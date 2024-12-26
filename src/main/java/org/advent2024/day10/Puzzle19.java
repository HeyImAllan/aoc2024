package org.advent2024.day10;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.advent2024.day6.Puzzle12.readFromFile;
import static org.advent2024.day8.Puzzle16.buildmap;
import static org.advent2024.day8.Puzzle16.printMap;

public class Puzzle19 {
    private static List<List<String>> map;
    private static Map<List<Integer>, List<List<Integer>>> score = new HashMap<>();

    public static void main(String[] args) {
        List<String> input = readFromFile("src/main/resources/puzzle19/input.txt");
        System.out.println(input);
        map = buildmap(input);
        printMap(map);
        List<List<Integer>> coordinates = getCoordinates("0");
        System.out.println(coordinates);
        long answer = 0;
        findPaths(coordinates);
        for (List<Integer> coordinate: coordinates) {
            answer += score.get(coordinate).stream().distinct().count();
        }
        System.out.print(answer);
    }

    private static void findPaths(List<List<Integer>> coordinates) {
        for (List<Integer> coordinate : coordinates) {
            List<List<Integer>> path = new ArrayList<>();
            score.put(coordinate, new ArrayList<>());
            path.add(coordinate);
            traverse(path);
        }
    }

    public static void traverse(List<List<Integer>> path) {
        List<Integer> lastlocation = path.getLast();
        int currentValue = Integer.parseInt(getFromMap(lastlocation.get(0), lastlocation.get(1)));
        List<List<Integer>> neighbors = getNeighbors(lastlocation);
        for (List<Integer> neighbor : neighbors) {
            if (!outOfBounds(neighbor) && Integer.parseInt(getFromMap(neighbor.get(0), neighbor.get(1))) == currentValue + 1) {
                if (currentValue + 1 == 9) {
                    updateScore(path.getFirst(), neighbor);

                }
                List<List<Integer>> newPath = new ArrayList<>(path);
                newPath.add(neighbor);
                traverse(newPath);
            }
        }
    }

    private static void updateScore(List<Integer> trailhead, List<Integer> peak) {
        List<List<Integer>> peakList = new ArrayList<>(score.get(trailhead));
        peakList.add(peak);
        score.put(trailhead, peakList);
    }

    private static List<List<Integer>> getNeighbors(List<Integer> lastlocation) {
        int x = lastlocation.get(0), y = lastlocation.get(1);
        return new ArrayList<>(List.of(
                List.of(x - 1, y),
                List.of(x + 1, y),
                List.of(x, y - 1),
                List.of(x, y + 1)
        ));
    }

    public static List<List<Integer>> getCoordinates(String uniqueChar) {
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
    public static String getFromMap(int row, int col) {
        return map.get(row).get(col);
    }
    public static boolean outOfBounds(List<Integer> coordinate) {
        return coordinate.get(0) < 0 || coordinate.get(0) >= map.size() ||
                coordinate.get(1) < 0 || coordinate.get(1) >= map.get(coordinate.get(0)).size() ;
    }

}
