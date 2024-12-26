package org.advent2024.day6;

import org.advent2024.day3.Puzzle5;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Puzzle11 {
    private static final String updirection = "^";
    private static final String downdirection = "v";
    private static final String rightdirection = ">";
    private static final String leftdirection = "<";

    public static List<List<String>> map = new ArrayList<>();
    public static List<Integer> guardLocation = new ArrayList<>();

    public static Integer countXOnMap(List<List<String>> map) {
        int count = 0;
        for (List<String> strings : map) {
            for (String string : strings) {
                if (string.equals("X")) {
                    count++;
                }
            }
        }
            return count;
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
        System.out.println(countXOnMap(map));
    }
    public static int predictPath(List<String> input) {

        int answer = 0;
        map = buildmap(input);
        guardLocation = getGuardLocation(map, updirection);
        System.out.println(guardLocation);
        while (indexExists(map.getFirst(), guardLocation.get(1)) && indexExists(map, guardLocation.get(0))) {
            System.out.println("WALK!");
            guardWalk();
            System.out.println(guardLocation);
            answer++;
        }
        return answer;
    }

    private static void guardWalk() {
        String guardDirection = getFromMap(guardLocation.get(0),guardLocation.get(1));
        String targetLocationValue = "";
        try {
            switch (guardDirection) {
                case updirection:
                    System.out.println("entering switch");
                    while (!targetLocationValue.equals("#")) {
                        targetLocationValue = getFromMap(guardLocation.get(0) - 1, guardLocation.get(1));
                        updateMap(guardLocation.get(0), guardLocation.get(1), "X");
                        if (targetLocationValue.equals("#")) {
                            updateMap(guardLocation.get(0), guardLocation.get(1), rightdirection);
                        } else {
                            updateMap(guardLocation.get(0) - 1, guardLocation.get(1), updirection);
                            guardLocation.set(0, guardLocation.get(0) - 1);
                        }
                        //printMap(map);
                    }
                case downdirection:
                    System.out.println("entering switch");
                    while (!targetLocationValue.equals("#")) {
                        targetLocationValue = getFromMap(guardLocation.get(0) + 1, guardLocation.get(1));
                        updateMap(guardLocation.get(0), guardLocation.get(1), "X");
                        if (targetLocationValue.equals("#")) {
                            updateMap(guardLocation.get(0), guardLocation.get(1), leftdirection);
                        } else {
                            updateMap(guardLocation.get(0) + 1, guardLocation.get(1), downdirection);
                            guardLocation.set(0, guardLocation.get(0) + 1);
                        }
                        //printMap(map);
                    }
                case rightdirection:
                    while (!targetLocationValue.equals("#")) {
                        targetLocationValue = getFromMap(guardLocation.get(0), guardLocation.get(1) + 1);
                        updateMap(guardLocation.get(0), guardLocation.get(1), "X");
                        if (targetLocationValue.equals("#")) {
                            updateMap(guardLocation.get(0), guardLocation.get(1), downdirection);
                        } else {
                            updateMap(guardLocation.get(0), guardLocation.get(1) + 1, rightdirection);
                            guardLocation.set(1, guardLocation.get(1) + 1);
                        }
                        //printMap(map);
                    }
                case leftdirection:
                    while (!targetLocationValue.equals("#")) {
                        updateMap(guardLocation.get(0), guardLocation.get(1), "X");
                        targetLocationValue = getFromMap(guardLocation.get(0), guardLocation.get(1) - 1);
                        if (targetLocationValue.equals("#")) {
                            updateMap(guardLocation.get(0), guardLocation.get(1), updirection);
                        } else {
                            updateMap(guardLocation.get(0), guardLocation.get(1) - 1, leftdirection);
                            guardLocation.set(1, guardLocation.get(1) - 1);
                        }
                        //printMap(map);
                    }
            }
        } catch (IndexOutOfBoundsException e) {
            updateMap(guardLocation.get(0), guardLocation.get(1), "X");
            printMap(map);
            guardLocation.set(0,-1);
            guardLocation.set(1,-1);

        }
    }

    private static void printMap(List<List<String>> map){
        for (int i = 0; i < map.size() ; i++) {
            for (int j = 0; j < map.get(i).size() ; j++) {
                System.out.print(map.get(i).get(j));
            }
            System.out.print("\n");
        }
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
    public static boolean indexExists(final List list, final int index) {
        return index >= 0 && index < list.size();
    }
}
