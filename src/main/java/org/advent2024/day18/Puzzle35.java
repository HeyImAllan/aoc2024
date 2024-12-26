package org.advent2024.day18;


import org.advent2024.util.DijkstraPqueue;
import org.advent2024.util.NodeDist;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.advent2024.day6.Puzzle12.readFromFile;

public class Puzzle35 {

    static int mapHeight = 71;
    static int mapWidth = 71;
    static int fallingBytes = 1024;
    static LinkedList<Point> seenNodes = null;

    private static Map<Point, String> buildmap(List<Point> points, int mapHeight, int mapWidth) {
        Map<Point, String> map = new HashMap<>();
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                Point point = new Point(i, j);
                if (points.contains(point)) {
                    map.put(point, "#");
                } else {
                    map.put(point, ".");
                }
            }
        }
        return map;
    }
    public static List<List<NodeDist>> createGraphFromPointsList(List<Point> nodes, Map<Point, String> map) {
        Map<Point, Integer> pointToIndex = new HashMap<>();
        for (int i = 0; i < nodes.size(); i++) {
            pointToIndex.put(nodes.get(i), i);
        }

        List<List<NodeDist>> graph = new ArrayList<>(nodes.size());
        for (int i = 0; i < nodes.size(); i++) {
            graph.add(new ArrayList<>());
        }

        for (Point node : nodes) {
            if (node.equals(nodes.getLast())) {
                continue;
            }
            int nodeIndex = pointToIndex.get(node);
            List<Point> validNeighborsForPoint = getNeighbors(node,map);
            for (Point neighbor : validNeighborsForPoint) {
                int adjIndex = pointToIndex.get(neighbor);
                graph.get(nodeIndex).add(new NodeDist(adjIndex, 1));
            }
        }

        return graph;
    }

    private static List<Point> getNeighbors(Point node, Map<Point, String> map) {
        List<Point> tests = List.of(
                new Point(node.x - 1, node.y),
                new Point(node.x + 1, node.y),
                new Point(node.x, node.y - 1),
                new Point(node.x, node.y + 1));
        List<Point> neighbors = new ArrayList<>();
        for (Point test : tests) {
            if (map.containsKey(test) && (map.get(test).equals(".") || map.get(test).equals("E"))) {
                neighbors.add(test);
            }
        }
        return neighbors;
    }

    public static void printMap(Map<Point, String> map) {
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                Point point = new Point(i, j);
                System.out.print(map.get(point));
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        List<String> input = readFromFile("src/main/resources/puzzle35/input.txt");
        List<Point> points = new ArrayList<>();
        for (int i =0; i < fallingBytes; i++) {
            if (i < input.size()) {
                String line = input.get(i);
                int row = Integer.parseInt(line.split(",")[1]);
                int col = Integer.parseInt(line.split(",")[0]);
                points.add(new Point(row, col));
            } else {
                break;
            }
        }
        Map<Point, String> map = buildmap(points, mapHeight, mapWidth);
        printMap(map);
        List<Point> nodes = getAllPoints(map);
        //System.out.println(nodes);
        Point start = new Point(0,0);
        Point end = new Point(mapHeight-1,mapWidth-1);
        nodes.remove(start);
        nodes.remove(end);
        nodes.add(0, start);
        nodes.add(end);
        List<List<NodeDist>> graph = createGraphFromPointsList(nodes, map );
        //System.out.println(graph);
        Map<Integer, Integer> result = DijkstraPqueue.ShortestPath(0, nodes.size(), graph);
        System.out.println(result.get(nodes.size()-1));



    }
    public static List<Point> getAllPoints(Map<Point, String> map) {
        List<Point> allPoints = new ArrayList<>();
        for (int row = 0; row < mapHeight; row++ ) {
            for (int col = 0; col < mapWidth; col++) {
                if (map.get(new Point(row,col)).equals(".")) {
                        allPoints.add(new Point(row, col));
                    }
                }
            }
        return allPoints;
    }


}
