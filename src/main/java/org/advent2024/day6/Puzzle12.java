package org.advent2024.day6;

import org.advent2024.day3.Puzzle5;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Puzzle12 {
    private static final String updirection = "^";
    private static final String downdirection = "v";
    private static final String rightdirection = ">";
    private static final String leftdirection = "<";

    public static List<List<String>> map = new ArrayList<>();
    public static List<List<String>> mapBackup = new ArrayList<>();
    public static List<Integer> guardLocation = new ArrayList<>();
    public static String guardDirection = updirection;

    public static void updateGuardLocation(int row, int col) {
        List<Integer> newGuardLocation = new ArrayList<>();
        newGuardLocation.add(row);
        newGuardLocation.add(col);
        guardLocation = newGuardLocation;
    }

    public static void updateMap(int row, int col, String value) {
        List<List<String>> newMap = new ArrayList<>();
        for (int i = 0; i < map.size(); i++) {
            List<String> rowList = new ArrayList<>(map.get(i));
            if (row == i) {
                rowList.set(col, value);
            }
            newMap.add(rowList);
        }
        map = newMap;
    }
    public static String getFromMap(int row, int col) {
        return map.get(row).get(col);
    }

    public static List<String> readFromFile(String fileName) {
        return Puzzle5.readFromFile(fileName);
    }


    public static void main(String argsp[]) {
        List<String> input = readFromFile("src/main/resources/puzzle11/input.txt");
        int answer = predictPath(input);
        System.out.println(answer);
    }
    public static int predictPath(List<String> input) {

        map = buildmap(input);
        mapBackup = new ArrayList<>(map);
        guardLocation = getGuardLocation(map, updirection);
        List<Integer> originalGuardLocation = guardLocation;
        String originalGuarDirection = guardDirection;
        System.out.println(guardLocation);
        int count = 0;
        int attempt = 0;
        for (int i = 0; i < map.size() ; i++) {
            for (int j = 0; j < map.get(i).size() ; j++) {
                attempt++;
                if (!getFromMap(i,j).equals("^") && !getFromMap(i,j).equals("X")) {
                    updateMap(i, j, "#");
                    Boolean result = guardWalk();

                    if (result) {
                        System.out.println(attempt);
                        count++;
                    }
                    map = new ArrayList<>(mapBackup);
                    guardLocation = originalGuardLocation;
                    guardDirection = originalGuarDirection;
                }
            }
        }
        return count;
    }

    private static boolean guardWalk() {
        String targetLocationValue = "";
        List<String> visitedLocations = new ArrayList<>();
        while (!visitedLocations.contains(String.format("%s-%s-%s",guardLocation.get(0), guardLocation.get(1), guardDirection)))
        {
            visitedLocations.add(String.format("%s-%s-%s",guardLocation.get(0), guardLocation.get(1), guardDirection));
            try {
                switch (guardDirection) {
                    case updirection:
                        targetLocationValue = getFromMap(guardLocation.get(0) - 1, guardLocation.get(1));
                        if (targetLocationValue.equals("#")) {
                            guardDirection = rightdirection;
                            visitedLocations.removeLast();
                        } else {
                            updateGuardLocation(guardLocation.get(0) - 1, guardLocation.get(1));
                        }
                        break;
                    case downdirection:
                        targetLocationValue = getFromMap(guardLocation.get(0) + 1, guardLocation.get(1));
                        if (targetLocationValue.equals("#")) {
                            visitedLocations.removeLast();
                            guardDirection = leftdirection;
                        } else {
                            updateGuardLocation(guardLocation.get(0) + 1, guardLocation.get(1));
                        }
                        break;
                    case rightdirection:
                        targetLocationValue = getFromMap(guardLocation.get(0), guardLocation.get(1) + 1);
                        if (targetLocationValue.equals("#")) {
                            visitedLocations.removeLast();
                            guardDirection = downdirection;
                        } else {
                            updateGuardLocation(guardLocation.getFirst(), guardLocation.get(1) + 1);
                        }
                        break;
                    case leftdirection:
                        targetLocationValue = getFromMap(guardLocation.get(0), guardLocation.get(1) - 1);
                        if (targetLocationValue.equals("#")) {
                            visitedLocations.removeLast();
                            guardDirection = updirection;
                        } else {
                            updateGuardLocation(guardLocation.getFirst(), guardLocation.get(1) - 1);
                        }
                        break;
                }
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }
        return true;
    }
    private static List<Integer> getGuardLocation(List<List<String>> map, String direction) {
        List<Integer> guardLocation = new ArrayList<>(2);
        for (int i = 0; i < map.size() ; i++) {
            for (int j = 0; j < map.get(i).size() ; j++) {
                if (Objects.equals(map.get(i).get(j), direction)) {
                    guardLocation.add(i);
                    guardLocation.add(j);
                }
            }
        }
        return guardLocation;
    }

    private static List<List<String>> buildmap(List<String> input) {
        List<List<String>> map = new ArrayList<>();
        for (String line : input) {
            map.add(List.of(line.split("")));
        }
        return map;
    }
}
