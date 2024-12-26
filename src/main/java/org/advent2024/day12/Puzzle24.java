package org.advent2024.day12;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.advent2024.day6.Puzzle12.readFromFile;
import static org.advent2024.day8.Puzzle16.buildmap;
import static org.advent2024.day8.Puzzle16.printMap;

public class Puzzle24 {
    private static List<List<String>> map;
    static List<Point> seen = new ArrayList<>();
    static Map<String, List<Point>> directionsSeen = new HashMap<>();
    static Map<Point, List<Point>> regions = new HashMap<>();
    static Map<Point, List<Point>> antiRegions = new HashMap<>();


    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        List<String> directions = List.of("NORTH", "SOUTH", "EAST", "WEST");
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
        for (String direction: directions) {
            directionsSeen.putIfAbsent(direction, new ArrayList<>());
        }
        int answerp2 = 0;
        for (List<Point> region : regions.values()) {
            int regionSides = 0;
            for (Point point : region) {
                for (String direction: directions) {
                    Point oldPoint = point;
                    while (!directionsSeen.get(direction).contains(oldPoint)) {
                        //System.out.println("Checking direction" + direction + " for " + point + " letter " + getFromMap(point));
                        for (Point p : region) {
                            // get furthest point on the left.
                            if (direction.equals("NORTH") || direction.equals("SOUTH")) {
                                if (p.getX() == point.getX() && p.getY() < point.getY() && !directionsSeen.get(direction).contains(p)) {
                                    point = p;
                                }
                            }
                            if (direction.equals("EAST") || direction.equals("WEST")) {
                                if (p.getY() == point.getY() && p.getX() < point.getX() && !directionsSeen.get(direction).contains(p)) {
                                    point = p;
                                }
                            }
                        }

                        if (!directionsSeen.get(direction).contains(point)) {
                            if (!antiTraverse(point.x, point.y, direction).isEmpty()) {
                                System.out.println("Adding direction " + direction + " for " + point + " letter " + getFromMap(point));
                                regionSides += 1;
                            }
                        }
                        point = oldPoint;
                    }
                }
            }
            System.out.println("Cost for region " + getFromMap(region.getFirst()) + " is " + regionSides + " * " + region.size() + " = " + regionSides * region.size());
            answerp2 += (regionSides * region.size());
        }

        System.out.println(antiRegions);
        for (Map.Entry<Point,List<Point>> entry : antiRegions.entrySet()) {
            System.out.println("Printing region " + getFromMap(entry.getKey()));
            System.out.println(entry.getValue());
        }

//        long answer = 0L;
//        for (Map.Entry<Point,List<Point>> entry : regions.entrySet()) {
//            System.out.println("Printing region " + getFromMap(entry.getKey()));
//            //printOnMap(entry.getValue());
//            long regioncost = calculateCost(entry.getValue());
//            System.out.println(regioncost);
//            answer += regioncost;
//
//        }
//        System.out.println(answer);
        System.out.println(answerp2);
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
    public static List<Point> antiTraverse (int row, int col, String direction) {
        List<Point> antiRegion = new ArrayList<>();
        Point current = new Point(row,col);
        antiExplore(current, antiRegion, direction);
        return antiRegion;
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
    private static void antiExplore(Point current, List<Point> antiRegion, String direction) {
        String s = getFromMap(current);
        int antiRegionSize = antiRegion.size();
        Point neighbor = getNeighborByDirection(current, direction);
        Point next = getNextPositionByDirection(current, direction);
        if (outOfBounds(neighbor) &&
                !directionsSeen.get(direction).contains(current) && !antiRegion.contains(neighbor)) {
            antiRegion.add(neighbor);
        } else if (!(getFromMap(neighbor).equals(s)) &&
            !directionsSeen.get(direction).contains(current) && !antiRegion.contains(neighbor)) {
            antiRegion.add(neighbor);
            }
        directionsSeen.get(direction).add(current);

        if (!outOfBounds(next) && getFromMap(next).equals(s) && (antiRegion.size() != antiRegionSize)) {
            antiExplore(next, antiRegion, direction);
        }
    }


    private static Point getNeighborByDirection(Point current, String direction) {
        Map<String, Point> neighbors = new HashMap<>();
        neighbors.put("NORTH", new Point(current.x-1, current.y));
        neighbors.put("SOUTH", new Point(current.x+1, current.y));
        neighbors.put("WEST", new Point(current.x, current.y-1));
        neighbors.put("EAST", new Point(current.x, current.y+1));
        return neighbors.get(direction);
    }
    private static Point getNextPositionByDirection(Point current, String direction) {
        Map<String, Point> neighbors = new HashMap<>();
        neighbors.put("NORTH", new Point(current.x, current.y+1));
        neighbors.put("SOUTH", new Point(current.x, current.y+1));
        neighbors.put("WEST", new Point(current.x+1, current.y));
        neighbors.put("EAST", new Point(current.x+1, current.y));
        return neighbors.get(direction);
    }

    private static List<Point> getNeighbors(Point current) {
        return List.of(new Point(current.x-1, current.y), new Point(current.x+1, current.y), new Point(current.x, current.y-1), new Point(current.x, current.y+1));
    }
    private static List<Point> getMoreNeighbors(Point current) {
        return List.of(new Point(current.x-1, current.y-1), new Point(current.x-1, current.y+1), new Point(current.x+1, current.y-1), new Point(current.x+1, current.y+1) );
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
