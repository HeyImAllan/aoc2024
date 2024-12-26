package org.advent2024.day15;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.advent2024.day6.Puzzle12.readFromFile;

public class Puzzle29 {

    static Map<String, Point> directions = new HashMap<>();
    static List<List<String>> warehouseMap = new ArrayList<>();
    static boolean printMap = true;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        directions.put("^", new Point(-1,0));
        directions.put("v", new Point(+1,0));
        directions.put("<", new Point(0,-1));
        directions.put(">", new Point(0,+1));
        List<String> input = readFromFile("src/main/resources/puzzle29/input.txt");
        List<String> rawmap = new ArrayList<>();


        for (String line : input) {
            if (line.isEmpty()) {
                break;
            }
            rawmap.add(line);
        }

        List<String> instructions = new ArrayList<>();
        boolean startRecording = false;
        for (String line : input) {
            if (line.isEmpty()) {
                startRecording = true;
                continue;
            }
            if (startRecording) {
                instructions.addAll(List.of(line.split("")));
            }
        }

        warehouseMap = buildmap(rawmap);
        printMap(warehouseMap);

        System.out.println(instructions);
        robotWalk(instructions);
        int answer = 0;
        List<Point> allBoxes = getAllCoordinatesOf("O", warehouseMap);
        for (Point box : allBoxes) {
            answer += 100 * box.x + box.y;
        }
        System.out.println(answer);
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total runtime: " + totalTime + "ms");
    }

    private static void robotWalk(List<String> instructions) {
        Point currentPos;
        Point nextPoint;
        for (String instruction : instructions) {
            currentPos = getFirstCoordinatesOf("@", warehouseMap);
            nextPoint = new Point(currentPos.x + directions.get(instruction).x, currentPos.y + directions.get(instruction).y);
            List<Point> rowOfBoxesToMove = lookAhead(warehouseMap, instruction);
            if (rowOfBoxesToMove.isEmpty()) {
                if (getFromMap(nextPoint, warehouseMap).equals(".")) {
                    updateMap(currentPos, ".");
                    updateMap(nextPoint, "@");
                    printMap(warehouseMap);
                }
            } else {
                updateMap(currentPos, ".");
                updateMap(nextPoint, "@");
                for (Point box : rowOfBoxesToMove) {
                    updateMap(new Point(box.x + directions.get(instruction).x, box.y + directions.get(instruction).y), "O");
                }
                printMap(warehouseMap);
            }
        }
    }
    public static void updateMap(Point point, String value) {
        List<List<String>> newMap = new ArrayList<>();
        for (int i = 0; i < warehouseMap.size(); i++) {
            List<String> cols = new ArrayList<>(warehouseMap.get(i));
            if (i == point.x) {
                cols.set(point.y, value);
            }
            newMap.add(cols);
        }
        warehouseMap = newMap;
    }


    private static List<Point> lookAhead(List<List<String>> warehouseMap, String instruction) {
        List<Point> rowOfBoxesToMove = new ArrayList<>();
        Point currentPos = getFirstCoordinatesOf("@", warehouseMap);
        Point nextPoint = new Point(currentPos.x + directions.get(instruction).x, currentPos.y + directions.get(instruction).y);
        while (true) {
            if (getFromMap(nextPoint, warehouseMap).equals("O")) {
                rowOfBoxesToMove.add(nextPoint);
                currentPos = nextPoint;
                nextPoint = new Point(currentPos.x + directions.get(instruction).x, currentPos.y + directions.get(instruction).y);
            } else if (getFromMap(nextPoint, warehouseMap).equals(".")) {
                break;
            } else if (getFromMap(nextPoint, warehouseMap).equals("#")) {
                rowOfBoxesToMove = new ArrayList<>();
                break;
            }
        }
        return rowOfBoxesToMove;
    }

    private static Point getFirstCoordinatesOf(String robot, List<List<String>> warehouseMap) {
            Point coordinates = new Point();
            for (int i = 0; i < warehouseMap.size(); i++) {
                for (int j = 0; j < warehouseMap.get(i).size(); j++) {
                    if (warehouseMap.get(i).get(j).equals(robot)) {
                        coordinates.setLocation(i,j);
                    }
                }
            }
            return coordinates;
        }
    private static List<Point> getAllCoordinatesOf(String box, List<List<String>> warehouseMap) {
        List<Point> coordinates = new ArrayList<>();
        for (int i = 1; i < warehouseMap.size()-1; i++) {
            for (int j = 1; j < warehouseMap.get(i).size()-1; j++) {
                if (warehouseMap.get(i).get(j).equals(box)) {
                    coordinates.add(new Point(i,j));
                }
            }
        }
        return coordinates;
    }


    public static List<List<String>> buildmap(List<String> input) {
        List<List<String>> map = new ArrayList<>();
        for (String line : input) {
            map.add(List.of(line.split("")));
        }
        return map;
    }

    public static void printMap(List<List<String>> map){
        if (printMap) {
            for (int i = 0; i < map.size(); i++) {
                for (int j = 0; j < map.get(i).size(); j++) {
                    System.out.print(map.get(i).get(j));
                }
                System.out.print("\n");
            }
        }
    }
    public static String getFromMap(Point point, List<List<String>> map) {
        return map.get(point.x).get(point.y);
    }

}
