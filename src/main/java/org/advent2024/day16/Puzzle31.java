package org.advent2024.day16;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.advent2024.day6.Puzzle12.readFromFile;

public class Puzzle31 {

    static Map<String, Point> directions = new HashMap<>();
    static List<List<String>> warehouseMap = new ArrayList<>();
    static List<Node> seenNodes =  new ArrayList<>();
    static boolean printMap = true;
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
        //System.out.println(getFirstCoordinatesOf("S"));
        //ystem.out.println(getFirstCoordinatesOf("E"));
        //System.out.println(pointList);
        Map<Point, Node> allNodes = constructBasicCorners(pointList);
        Node startNode = new Node(getFirstCoordinatesOf("S"));
        allNodes.put(getFirstCoordinatesOf("S"), startNode);
        Node endNode = new Node(getFirstCoordinatesOf("E"));
        allNodes.put(getFirstCoordinatesOf("E"), endNode);
        seenNodes.add(endNode);
        processNodes(allNodes, startNode);

        //highLightOnMap(pointList, "O");
//        List<Node> puzzleInput = new ArrayList<>();
//        for (Map.Entry<Point, Node> entry: allNodes.entrySet() ) {
//            if (!entry.getValue().getAdjacentNodes().isEmpty()) {
//                puzzleInput.add(entry.getValue());
//            }
//        }
        viableNodes.add(endNode);
        List<Node> shortestPath = calculateShortestPathFromSource(viableNodes, startNode);
        //System.out.println(shortestPath);
        List<Node> finalPath = shortestPath.getLast().getShortestPath();
        highLightOnMap(finalPath.stream().map(Node::getLocation).toList(), "O");
        System.out.println(shortestPath.getLast().getDistance() + 1000);
        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total runtime: " + totalTime + "ms");

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
        if (distanceBetweenPoints > 1) {
            return 0;
        }
        Point nextPoint = new Point(startCorner.x + directions.get(instruction).x, startCorner.y + directions.get(instruction).y);;
        if (!outOfBounds(nextPoint) && Objects.equals(getFromMap(nextPoint), "#")) {
            return 0;
        }
        return 1;

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
    public static void highLightOnMap(List<Point> points, String character){
        if (printMap) {
            for (int i = 0; i < warehouseMap.size(); i++) {
                for (int j = 0; j < warehouseMap.get(i).size(); j++) {
                    if (points.contains(new Point(i, j))) {
                        System.out.print(character);
                    } else {
                        System.out.print(warehouseMap.get(i).get(j));
                    }
                }
                System.out.print("\n");
            }
        }
    }


    public static List<Point> getAllPoints() {
        List<Point> corners = new ArrayList<>();
        for (int row = 0; row < warehouseMap.size(); row++ ) {
            for (int col = 0; col < warehouseMap.getFirst().size(); col++) {
                if (warehouseMap.get(row).get(col).equals(".")) {
                    //if (isCorner(new Point(row,col))) {
                        corners.add(new Point(row, col));
                    //}
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
    public static List<Node> calculateShortestPathFromSource(List<Node> graph, Node source) {
        source.setDistance(0);

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);

        while (unsettledNodes.size() != 0) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry<Node, Integer> adjacencyPair:
                    currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    CalculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }
    private static Node getLowestDistanceNode(Set<Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node: unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }
    private static void CalculateMinimumDistance(Node evaluationNode,
                                                 Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (!sourceNode.getShortestPath().isEmpty()) {
            Node previousEvaluatedNode = sourceNode.getShortestPath().getLast();
            Point oldDirection = new Point((sourceNode.getLocation().x - previousEvaluatedNode.getLocation().x), (sourceNode.getLocation().y - previousEvaluatedNode.getLocation().y));
            Point newDirection = new Point((evaluationNode.getLocation().x - sourceNode.getLocation().x), (evaluationNode.getLocation().y - sourceNode.getLocation().y));
            if (!oldDirection.equals(newDirection)) {
                edgeWeigh = edgeWeigh + 1000;
            }
        }
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }
    private static boolean isCorner(Point point) {
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
