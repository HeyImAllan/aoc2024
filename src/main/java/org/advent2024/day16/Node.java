package org.advent2024.day16;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Node {

    private Point location;

    private List<Node> shortestPath = new ArrayList<>();

    private Integer distance = Integer.MAX_VALUE;

    Map<Node, Integer> adjacentNodes = new HashMap<>();

    public void addDestination(Node destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

    public Node(Point name) {
        this.location = name;
    }

    public Point getLocation() { return location; }
    public List<Node> getShortestPath() { return shortestPath; }

    public Integer getDistance() { return distance; }

    public void setDistance(int i) {
        this.distance = i;
    }

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public Map<Node, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }
}