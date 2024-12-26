package org.advent2024.day12;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.advent2024.day6.Puzzle12.readFromFile;
import static org.advent2024.day8.Puzzle16.buildmap;
import static org.advent2024.day8.Puzzle16.printMap;

public class Puzzle23 {
    private static List<List<String>> map;
    static List<Point> seen = new ArrayList<>();
    static Map<Point, List<Point>> regions = new HashMap<>();

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        List<String> input = readFromFile("src/main/resources/puzzle23/input.txt");
        System.out.println(input);
        map = buildmap(input);
        printMap(map);
        for (int i = 0; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (!seen.contains(new Point(i,j))) {
                    regions.put(new Point(i, j), traverse(i, j));
                }
            }
        }
        long answer = 0L;
        for (Map.Entry<Point,List<Point>> entry : regions.entrySet()) {
            //System.out.println("Printing region " + getFromMap(entry.getKey()));
            //printOnMap(entry.getValue());
            long regioncost = calculateCost(entry.getValue());
            //System.out.println(regioncost);
            answer += regioncost;

        }
        System.out.println(answer);
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total runtime: " + totalTime + "ms");
    }

    private static long calculateCost(List<Point> region) {
        long totalFences = 0L;
        for (Point point : region) {
            long fences = 4L;
            List<Point> neighbors = getNeighbors(point);
            for (Point neighbor : neighbors) {
                if (region.contains(neighbor)){
                    fences--;
                }
            }
            totalFences += fences;
        }
        return totalFences * region.size();
    }

    public static List<Point> traverse (int row, int col) {
        List<Point> region = new ArrayList<>();
        Point current = new Point(row,col);
        region.add(current);

        seen.add(current);

        explore(current, region);
        return region;


    }

    private static void explore(Point current, List<Point> region) {
        String s = getFromMap(current);
        List<Point> neighbors = getNeighbors(current);
        for (Point neighbor : neighbors) {
            if (!outOfBounds(neighbor) && getFromMap(neighbor).equals(s) &&
                    !seen.contains(neighbor) && !region.contains(neighbor)) {
                seen.add(neighbor);
                region.add(neighbor);
                explore(neighbor, region);
            }
        }
    }

    private static List<Point> getNeighbors(Point current) {
        return List.of(new Point(current.x-1, current.y), new Point(current.x+1, current.y), new Point(current.x, current.y-1), new Point(current.x, current.y+1));
    }

    public static String getFromMap(Point point) {
        return map.get(point.x).get(point.y);
    }

    public static boolean outOfBounds(Point coordinate) {
        return coordinate.getX() < 0 || coordinate.getX() >= map.size() ||
                coordinate.getY() < 0 || coordinate.getY() >= map.get(coordinate.x).size() ;
    }

    public static void printOnMap(List<Point> region){
        for (int i = 0 ; i < map.size(); i++) {
            for (int j = 0; j < map.get(i).size(); j++) {
                if (region.contains(new Point(i, j))) {
                    System.out.print("?");
                } else {
                    System.out.print(getFromMap(new Point(i, j)));
                }
            }
            System.out.print( "\n");
        }
    }
}
