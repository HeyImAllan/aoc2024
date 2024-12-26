package org.advent2024.day16;

import org.advent2024.util.DijkstraPqueue;
import org.advent2024.util.NodeDist;

import java.awt.*;
import java.util.List;
import java.util.*;

import static org.advent2024.day6.Puzzle12.readFromFile;

public class Puzzle32 {

    static Map<String, Point> directions = new HashMap<>();
    static List<List<String>> warehouseMap = new ArrayList<>();
    static List<Node> seenNodes =  new ArrayList<>();
    static boolean printMap = false;
    static List<Node> viableNodes = new ArrayList<>();

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        directions.put("^", new Point(-1,0));
        directions.put("v", new Point(+1,0));
        directions.put("<", new Point(0,-1));
        directions.put(">", new Point(0,+1));
        List<String> input = readFromFile("src/main/resources/puzzle31/input.txt");
        List<String> rawmap = new ArrayList<>();

        for (String line : input) {
            if (line.isEmpty()) {
                break;
            }
            rawmap.add(line);
        }

        warehouseMap = buildmap(rawmap);
        printMap(warehouseMap);
        List<Point> pointList = getAllPoints();
        Map<Point, Node> allNodes = constructBasicCorners(pointList);
        Node startNode = new Node(getFirstCoordinatesOf("S"));
        allNodes.put(getFirstCoordinatesOf("S"), startNode);
        Node endNode = new Node(getFirstCoordinatesOf("E"));
        allNodes.remove(endNode.getLocation());
        allNodes.put(getFirstCoordinatesOf("E"), endNode);
        processNodes(allNodes, startNode);

        viableNodes.remove(viableNodes.indexOf(endNode)); //  make sure the endnode is not processed twice.
        viableNodes.add(endNode);
        List<List<NodeDist>> graph = convertToGraph(viableNodes);
        // get the distance of the shortest path
        Map<Integer, Integer> result = DijkstraPqueue.ShortestPath(viableNodes.indexOf(startNode), viableNodes.size(), graph);
        System.out.println(result.get(viableNodes.indexOf(endNode)));



        Map<Integer, List<List<Integer>>> result2 = DijkstraPqueue.ShortestPaths(viableNodes.indexOf(startNode), viableNodes.size(), graph);

        Set<Point> answer2 = new HashSet<>(Set.of());
        for (List<Integer> path : result2.get(viableNodes.indexOf(endNode))) {
            List<Point> points = new ArrayList<>();
            for (Integer index : path) {
                points.add(viableNodes.get(index).getLocation());
            }

            answer2.addAll(highLightPathOnMap(points, "X"));
        }
        System.out.println(answer2.size());

        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total runtime: " + totalTime + "ms");

    }
    private static List<List<NodeDist>> convertToGraph(List<Node> nodes) {
        Map<Point, Integer> pointToIndex = new HashMap<>();
        for (int i = 0; i < nodes.size(); i++) {
            pointToIndex.put(nodes.get(i).getLocation(), i);
        }

        List<List<NodeDist>> graph = new ArrayList<>(nodes.size());
        for (int i = 0; i < nodes.size(); i++) {
            graph.add(new ArrayList<>());
        }

        for (Node node : nodes) {
            int nodeIndex = pointToIndex.get(node.getLocation());
            for (Map.Entry<Node, Integer> entry : node.getAdjacentNodes().entrySet()) {
                int adjIndex = pointToIndex.get(entry.getKey().getLocation());
                graph.get(nodeIndex).add(new NodeDist(adjIndex, entry.getValue()));
            }
        }

        return graph;
    }
    private static Map<Point, Node> constructBasicCorners(List<Point> corners) {
        Map<Point, Node> allCorners = new HashMap<>();
        for (Point p : corners) {
            Node node = new Node(p);
            allCorners.put(p, node);
        }
        return allCorners;
    }

    private static void processNodes(Map<Point, Node> allNodes, Node startNode) {
        processNode(startNode, allNodes);
    }

    private static void processNode(Node node, Map<Point, Node>  allNodes) {
        if (seenNodes.contains(node)) {
            return;
        }
        seenNodes.add(node);
        for (Map.Entry<Point, Node> adjNode : allNodes.entrySet()) {
            if (node != adjNode.getValue()) {
                if (node.getLocation().getX() == adjNode.getKey().getX() || node.getLocation().getY() == adjNode.getKey().getY()) {
                    int distance = calculateDistanceBetweenPoints(node.getLocation(), adjNode.getKey());
                    if (distance != 0) {
                        if (!viableNodes.contains(node)) {
                            viableNodes.add(node);
                        }
                        node.addDestination(adjNode.getValue(), (distance));

                        processNode(adjNode.getValue(), allNodes);

                    }
                }
            }
        }


    }

    private static int calculateDistanceBetweenPoints(Point startCorner, Point edge) {
        double distanceBetweenPoints = 0;
        String instruction = "";

        if (startCorner.getY() == edge.getY()) {
            distanceBetweenPoints = Math.abs(startCorner.getX() - edge.getX());
            if (startCorner.getX() > edge.getX()) {
                instruction = "^";
            } else {
                instruction = "v";
            }
        }
        if (startCorner.getX() == edge.getX()) {
            distanceBetweenPoints = Math.abs(startCorner.getY() - edge.getY());
            if (startCorner.getY() > edge.getY()) {
                instruction = "<";
            } else {
                instruction = ">";
            }
        }
        Point nextPoint = new Point(startCorner.x + directions.get(instruction).x, startCorner.y + directions.get(instruction).y);
        for (int i = 0; i < distanceBetweenPoints; i++) {
            if (!outOfBounds(nextPoint) && Objects.equals(getFromMap(nextPoint), "#")) {
                return 0;
            }
            nextPoint = new Point(nextPoint.x + directions.get(instruction).x, nextPoint.y + directions.get(instruction).y);
        }
        return (int) distanceBetweenPoints + 1000;

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
//    public static void highLightOnMap(List<Point> points, String character){
//        if (printMap) {
//            for (int i = 0; i < warehouseMap.size(); i++) {
//                for (int j = 0; j < warehouseMap.get(i).size(); j++) {
//                    if (points.contains(new Point(i, j))) {
//                        System.out.print(character);
//                    } else {
//                        System.out.print(warehouseMap.get(i).get(j));
//                    }
//                }
//                System.out.print("\n");
//            }
//        }
//    }


    public static List<Point> getAllPoints() {
        List<Point> corners = new ArrayList<>();
        for (int row = 0; row < warehouseMap.size(); row++ ) {
            for (int col = 0; col < warehouseMap.getFirst().size(); col++) {
                if (warehouseMap.get(row).get(col).equals(".")) {
                    if (isCorner(new Point(row,col))) {
                        corners.add(new Point(row, col));
                    }
                }
            }
        }
        return corners;
    }

    public static boolean outOfBounds(Point coordinate) {
        return coordinate.getX() < 0 || coordinate.getX() >= warehouseMap.size() ||
                coordinate.getY() < 0 || coordinate.getY() >= warehouseMap.get(coordinate.x).size() ;
    }

    private static List<Point> getNeighbors(Point location) {
        return new ArrayList<>(List.of(
                new Point(location.x - 1, location.y),
                new Point(location.x + 1, location.y),
                new Point(location.x, location.y - 1),
                new Point(location.x, location.y + 1)
        ));
    }
    private static List<Point> getAdjNeighbors(Point location) {
        return new ArrayList<>(List.of(
                new Point(location.x - 1, location.y - 1),
                new Point(location.x + 1, location.y + 1),
                new Point(location.x + 1, location.y - 1),
                new Point(location.x - 1, location.y + 1)
        ));
    }
    public static String getFromMap(Point p) {
        return warehouseMap.get(p.x).get(p.y);
    }
    private static Point getFirstCoordinatesOf(String character) {
        Point coordinates = new Point();
        for (int i = 0; i < warehouseMap.size(); i++) {
            for (int j = 0; j < warehouseMap.get(i).size(); j++) {
                if (warehouseMap.get(i).get(j).equals(character)) {
                    coordinates.setLocation(i,j);
                }
            }
        }
        return coordinates;
    }

    public static Set<Point> highLightPathOnMap(List<Point> points, String character) {
        Set<Point> allPoints = new HashSet<>(points);
        for (int i = 0; i < points.size() - 1; i++) {
            Point start = points.get(i);
            Point end = points.get(i + 1);
            allPoints.addAll(getLinePoints(start, end));
        }
        for (int i = 0; i < warehouseMap.size(); i++) {
            for (int j = 0; j < warehouseMap.get(i).size(); j++) {
                if (allPoints.contains(new Point(i, j))) {
                    if (printMap)
                        System.out.print(character);
                } else {
                    if (printMap)
                        System.out.print(warehouseMap.get(i).get(j));
                }
            }
            if (printMap)
                System.out.print("\n");
        }

        return allPoints;
    }

    private static List<Point> getLinePoints(Point start, Point end) {
    List<Point> points = new ArrayList<>();
    int x1 = start.x;
    int y1 = start.y;
    int x2 = end.x;
    int y2 = end.y;
    int dx = Math.abs(x2 - x1);
    int dy = Math.abs(y2 - y1);
    int sx = x1 < x2 ? 1 : -1;
    int sy = y1 < y2 ? 1 : -1;
    int err = dx - dy;

    while (true) {
        points.add(new Point(x1, y1));
        if (x1 == x2 && y1 == y2) break;
        int e2 = 2 * err;
        if (e2 > -dy) {
            err -= dy;
            x1 += sx;
        }
        if (e2 < dx) {
            err += dx;
            y1 += sy;
        }
    }
    return points;
}
    public static boolean isCorner(Point point) {
        List<Point> neighborsOfPoint = getNeighbors(point);
        List<Point> corridors = new ArrayList<>();
        int score = 0;
        for (Point neighbor : neighborsOfPoint) {
            if (!outOfBounds(neighbor) && Objects.equals(getFromMap(neighbor), ".")) {
                corridors.add(neighbor);
                score++;
            }
        }
        if (score <= 1) {
            return false;
        } else if (score == 2){
            if (corridors.getFirst().getX() == corridors.get(1).getX() ||
                    corridors.getFirst().getY() == corridors.get(1).getY() ) {
                return false;
            } else {
                return true;
            }
        } else if (score == 3) {
            return true;
        } else if (score == 4) {
            List<Point> adjNeighbors = getAdjNeighbors(point);
            for (Point adjNeighbor : adjNeighbors) {
                if (!outOfBounds(adjNeighbor) && Objects.equals(getFromMap(adjNeighbor), ".")) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }

    }
}
