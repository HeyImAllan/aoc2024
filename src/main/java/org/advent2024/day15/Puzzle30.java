package org.advent2024.day15;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.advent2024.day6.Puzzle12.readFromFile;

public class Puzzle30 {

    static Map<String, Point> directions = new HashMap<>();
    static List<List<String>> warehouseMap = new ArrayList<>();
    static boolean printMap = false;

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

        warehouseMap = buildBigMap(rawmap);

        printMap(warehouseMap);

        System.out.println(instructions);
        robotWalk(instructions);
        int answer = 0;
        List<Point> allBoxes = getAllCoordinatesOf("O", warehouseMap);
        for (int i = 0; i < allBoxes.size(); i+=2) {
            //if (Math.abs(allBoxes.get(i).y) <= Math.abs(allBoxes.get(i+1).y - warehouseMap.getFirst().size())) {
                answer += (100* allBoxes.get(i).x) + allBoxes.get(i).y;
            //} else {
                //answer += getBoxScore(allBoxes.get(i+1));
            //}
        }
        printMap = true;
        printMap(warehouseMap);
        System.out.println(answer);
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total runtime: " + totalTime + "ms");
    }

//    private static int getBoxScore(Point box) { // for historic reasons.
//        int x;
//        int y;
//        if (box.x >= warehouseMap.size()/2) {
//            x = Math.abs((box.x+1) - warehouseMap.size());
//        } else {
//            x = box.x;
//        }
//        if (box.y > (warehouseMap.getFirst().size()/2)-1) {
//            y = Math.abs((box.y+1) - warehouseMap.getFirst().size());
//        } else {
//            y = box.y;
//        }
//        return (100 * x) + y;
//    }

    private static void robotWalk(List<String> instructions) {
        Point currentPos;
        Point nextPoint;
        for (String instruction : instructions) {
            currentPos = getFirstCoordinatesOf("@", warehouseMap);
            nextPoint = new Point(currentPos.x + directions.get(instruction).x, currentPos.y + directions.get(instruction).y);
            List<Point> rowOfBoxesToMove = null;
            try {
                //System.out.println(instruction);
                rowOfBoxesToMove = lookAheadPart2(warehouseMap, instruction);
            } catch (Exception e) {
                continue;
            }
            if (rowOfBoxesToMove.isEmpty()) {
                if (getFromMap(nextPoint, warehouseMap).equals(".")) {
                    updateMap(currentPos, ".");
                    updateMap(nextPoint, "@");
                    printMap(warehouseMap);
                }
            } else {
                updateMap(currentPos, ".");
                for (Point box : rowOfBoxesToMove) {
                    updateMap(new Point(box.x, box.y), ".");
                }
                for (Point box : rowOfBoxesToMove) {
                    updateMap(new Point(box.x + directions.get(instruction).x, box.y + directions.get(instruction).y), "O");
                }
                updateMap(nextPoint, "@");

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


    private static List<Point> lookAhead(List<List<String>> warehouseMap, String instruction, Point currentPos, boolean sendAnyway) throws Exception {
        List<Point> rowOfBoxesToMove = new ArrayList<>();
        Point nextPoint = new Point(currentPos.x + directions.get(instruction).x, currentPos.y + directions.get(instruction).y);
        while (true) {
            if (getFromMap(nextPoint, warehouseMap).equals("O")) {
                rowOfBoxesToMove.add(nextPoint);
                currentPos = nextPoint;
                nextPoint = new Point(currentPos.x + directions.get(instruction).x, currentPos.y + directions.get(instruction).y);
            } else if (getFromMap(nextPoint, warehouseMap).equals(".")) {
                break;
            } else if (getFromMap(nextPoint, warehouseMap).equals("#")) {
                if (sendAnyway) {
                    return rowOfBoxesToMove;
                } else {
                    throw new Exception("Ran into wall.");
                }
            }
        }
        return rowOfBoxesToMove;
    }
    private static List<Point> lookAheadPart2(List<List<String>> warehouseMap, String instruction) throws Exception {
        Point currentPos = getFirstCoordinatesOf("@", warehouseMap);
        if (instruction.equals("<") || instruction.equals(">") ) {
            return lookAhead(warehouseMap, instruction, currentPos, false);
        }
        List<Point> rowOfBoxesToMove = new ArrayList<>();
        Point nextPoint = new Point(currentPos.x + directions.get(instruction).x, currentPos.y + directions.get(instruction).y);
        if (getFromMap(nextPoint, warehouseMap).equals("O")) {
            List<List<Point>> boxesToRecurse = new ArrayList<>();
            boxesToRecurse.add(completeBox(nextPoint));
            while (!boxesToRecurse.isEmpty()) {
                List<Point> box = boxesToRecurse.getFirst();
                rowOfBoxesToMove.addAll(box);
                boxesToRecurse.removeFirst();
                for (Point point : lookAhead(warehouseMap, instruction, box.getFirst(), false)) {
                    boxesToRecurse.add(completeBox(point));
                }
                for (Point point : lookAhead(warehouseMap, instruction, box.getLast(), false)) {
                    boxesToRecurse.add(completeBox(point));
                }

            }
        }
        return rowOfBoxesToMove;
    }

    private static List<Point> completeBox(Point nextPoint) {
        List<Point> box = new ArrayList<>();
        box.add(nextPoint);
        List<Point> boxesOnTheLeft = null;
        try {
            boxesOnTheLeft = lookAhead(warehouseMap, "<", nextPoint, true );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (boxesOnTheLeft.size() % 2 == 0) {
            box.add(new Point(nextPoint.x, nextPoint.y +1));
        } else {
            box.add(new Point(nextPoint.x, nextPoint.y- 1));
        }
        return box;
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
    public static List<List<String>> buildBigMap(List<String> input) {
        List<List<String>> map = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            List<String> row = new ArrayList<>(List.of(input.get(i).split("")));
            List<String> duplicateRow = new ArrayList<>();
            for (int j = 0; j < row.size(); j++) {
                if (Objects.equals(row.get(j), "@")) {
                    duplicateRow.add(row.get(j));
                    duplicateRow.add(".");
                } else {
                    duplicateRow.add(row.get(j));
                    duplicateRow.add(row.get(j));
                }
            }
            map.add(duplicateRow);
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
