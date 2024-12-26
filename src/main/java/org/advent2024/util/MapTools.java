package org.advent2024.util;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapTools {
    public static java.util.List<Point> getAllPoints(Map<Point, String> map, String character) {
        List<Point> allPoints = new ArrayList<>();
        for (Map.Entry<Point,String> entry : map.entrySet()) {
                if (entry.getValue().equals(character)) {
                    allPoints.add(entry.getKey());
                }
            }
        return allPoints;
    }
    public static Point getfirstcoordinateof(String s, Map<Point, String> map) {
        for (Map.Entry<Point, String> entry : map.entrySet()) {
            if (entry.getValue().equals(s)) {
                return entry.getKey();
            }
        }
        return null;
    }
    public static Map<Point, String> buildmap(List<String> input) {
        Map<Point, String> map = new HashMap<>();
        for (int i = 0; i < input.size(); i++) {
            for (int j = 0; j < List.of(input.get(i).split("")).size(); j++) {
                Point point = new Point(i, j);
                map.put(point, String.valueOf(input.get(i).charAt(j)));
            }
        }
        return map;
    }

    public static void printMap(Map<Point, String> map, int mapHeight, int mapWidth){
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                System.out.print(map.get(new Point(i,j)));
            }
            System.out.print("\n");
        }

    }
    public static void printPointsOnMap(Map<Point, String> map, int mapHeight, int mapWidth, List<Point> list){
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                if (list.contains(new Point(i,j))) {
                    System.out.print("O");
                } else {
                    System.out.print(map.get(new Point(i,j)));
                }
            }
            System.out.print("\n");
        }

    }
    public static String getFromMap(Point point, Map<Point, String> map) {
        return map.get(point);
    }
    public static Map<Point, String> updateMap(Point p, String value, Map<Point, String> oldmap) {
        Map<Point, String> newMap = new HashMap<>(oldmap);
        newMap.put(p, value);
        return newMap;
    }
}
