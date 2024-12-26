package org.advent2024.util;

import java.awt.*;
import java.util.*;
import java.util.List;

public class DijkstraPqueue {

            public static List<List<Integer>> getAllPathsViaCheckpoints(int startNode, List<Integer> checkpoints, List<List<NodeDist>> graph) {
                List<List<Integer>> allPaths = new ArrayList<>();
                List<Integer> currentPath = new ArrayList<>();
                getAllPathsHelper(startNode, checkpoints, 0, graph, currentPath, allPaths);
                return allPaths;
            }

            private static void getAllPathsHelper(int currentNode, List<Integer> checkpoints, int checkpointIndex, List<List<NodeDist>> graph, List<Integer> currentPath, List<List<Integer>> allPaths) {
                currentPath.add(currentNode);
                if (checkpointIndex == checkpoints.size()) {
                    allPaths.add(new ArrayList<>(currentPath));
                } else {
                    int nextCheckpoint = checkpoints.get(checkpointIndex);
                    List<Integer> pathToNextCheckpoint = getShortestPath(currentNode, nextCheckpoint, graph.size(), graph);
                    for (int i = 1; i < pathToNextCheckpoint.size(); i++) {
                        getAllPathsHelper(pathToNextCheckpoint.get(i), checkpoints, checkpointIndex + 1, graph, currentPath, allPaths);
                    }
                }
                currentPath.remove(currentPath.size() - 1);
            }
        public static Map<Integer, Integer> ShortestPath(int source_node, int node_count, List<List<NodeDist>> graph) {

            // Assume that the distance from the source_node to other nodes is infinite
            // in the beginnging, i.e initialize the distance list to a max value
            Long INF = (long) 999999999;
            List<Long> dist = new ArrayList<Long>(Collections.nCopies(node_count, INF));

            // Distance from the source vertex to itself is 0
            dist.set(source_node, (long) 0); // (Node, Dist)

            // Comparator lambda function that enables the priority queue to store the nodes
            // based on the distance in the ascending order.
            Comparator<NodeDist> NodeDistComparator = (obj1, obj2) -> {
                if (obj1.dist < obj2.dist)
                    return 1;
                if (obj1.dist > obj2.dist)
                    return -1;
                return 0;
            };

            // Priority queue stores the object node-distance into the queue with
            // the smallest distance node at the top.
            PriorityQueue<NodeDist> pq = new PriorityQueue<>(NodeDistComparator);

            pq.add(new NodeDist(source_node, 0));

            while (!pq.isEmpty()) {

                NodeDist obj = pq.peek();
                pq.remove();

                int current_source = obj.node;

                for (NodeDist obj_node_dist : graph.get(current_source)) {

                    int adj_node = obj_node_dist.node;
                    long length_to_adjnode = obj_node_dist.dist;

                    // Edge relaxation
                    if (dist.get(adj_node) > length_to_adjnode + dist.get(current_source)) {

                        // If the distance to the adjacent node is not INF, means the object <node, dist> is in the priority queue.
                        // Remove the object before updating it in the priority queue.
                        if (dist.get(adj_node) != INF) {
                            pq.remove(new NodeDist(adj_node, dist.get(adj_node)));
                        }
                        dist.set(adj_node, length_to_adjnode + dist.get(current_source));
                        pq.add(new NodeDist(adj_node, dist.get(adj_node)));
                    }
                }
            }
            Map<Integer, Integer> result = new HashMap<>();
            for (int i=0; i<node_count; i++) {
                result.put(i, dist.get(i).intValue());
                //System.out.println("Source Node(" + source_node + ") -> Destination Node(" + i + ") : " + dist.get(i));
            }
            return result;

        }

        public static List<Integer> getShortestPath(int source_node, int target_node, int node_count, List<List<NodeDist>> graph) {
            Long INF = (long) 999999999;
            List<Long> dist = new ArrayList<>(Collections.nCopies(node_count, INF));
            List<Integer> prev = new ArrayList<>(Collections.nCopies(node_count, -1));

            dist.set(source_node, (long) 0);

            Comparator<NodeDist> NodeDistComparator = (obj1, obj2) -> {
                if (obj1.dist < obj2.dist)
                    return 1;
                if (obj1.dist > obj2.dist)
                    return -1;
                return 0;
            };

            PriorityQueue<NodeDist> pq = new PriorityQueue<>(NodeDistComparator);
            pq.add(new NodeDist(source_node, 0));

            while (!pq.isEmpty()) {
                NodeDist obj = pq.peek();
                pq.remove();

                int current_source = obj.node;

                for (NodeDist obj_node_dist : graph.get(current_source)) {
                    int adj_node = obj_node_dist.node;
                    long length_to_adjnode = obj_node_dist.dist;

                    if (dist.get(adj_node) > length_to_adjnode + dist.get(current_source)) {
                        if (dist.get(adj_node) != INF) {
                            pq.remove(new NodeDist(adj_node, dist.get(adj_node)));
                        }
                        dist.set(adj_node, length_to_adjnode + dist.get(current_source));
                        prev.set(adj_node, current_source);
                        pq.add(new NodeDist(adj_node, dist.get(adj_node)));
                    }
                }
            }

            List<Integer> path = new ArrayList<>();
            for (int at = target_node; at != -1; at = prev.get(at)) {
                path.add(at);
            }
            Collections.reverse(path);

            if (path.get(0) == source_node) {
                return path;
            } else {
                return new ArrayList<>(); // return empty list if no path found
            }
        }

        public static final Map<Integer, List<List<Integer>>> ShortestPaths(int source_node, int node_count, List<List<NodeDist>> graph) {
        Long INF = (long) 999999999;
            List<Long> dist = new ArrayList<>(Collections.nCopies(node_count, INF));
            List<Integer> prev = new ArrayList<>(Collections.nCopies(node_count, -1));

            dist.set(source_node, 0L);

        Comparator<NodeDist> NodeDistComparator = (obj1, obj2) -> {
            if (obj1.dist < obj2.dist)
                return 1;
            if (obj1.dist > obj2.dist)
                return -1;
            return 0;
        };

        PriorityQueue<NodeDist> pq = new PriorityQueue<>(NodeDistComparator);
        pq.add(new NodeDist(source_node, 0));

        while (!pq.isEmpty()) {
            NodeDist obj = pq.peek();
            pq.remove();

            int current_source = obj.node;

            for (NodeDist obj_node_dist : graph.get(current_source)) {
                int adj_node = obj_node_dist.node;
                long length_to_adjnode = obj_node_dist.dist;

                if (dist.get(adj_node) > length_to_adjnode + dist.get(current_source)) {
                    if (dist.get(adj_node) != INF) {
                        pq.remove(new NodeDist(adj_node, dist.get(adj_node)));
                    }
                    dist.set(adj_node, length_to_adjnode + dist.get(current_source));
                        prev.set(adj_node, current_source);
                    pq.add(new NodeDist(adj_node, dist.get(adj_node)));
                }
            }
        }

        Map<Integer, List<List<Integer>>> result = new HashMap<>();
        for (int i = 0; i < node_count; i++) {
            List<List<Integer>> paths = new ArrayList<>();
            if (dist.get(i) != INF) {
                    findAllPaths(i, source_node, new ArrayList<>(), paths, dist, graph, prev);
            }
            result.put(i, paths);
        }
        return result;
    }


    private static void findAllPaths(int node, int source, List<Integer> currentPath, List<List<Integer>> paths, List<Long> dist, List<List<NodeDist>> graph, List<Integer> prev) {
        currentPath.add(node);
        if (node == source) {
            Collections.reverse(currentPath);
            paths.add(new ArrayList<>(currentPath));
            Collections.reverse(currentPath);
        } else {
            int previousNode = prev.get(node);
            if (previousNode != -1) {
                findAllPaths(previousNode, source, currentPath, paths, dist, graph, prev);
            }
        }
        currentPath.remove(currentPath.size() - 1);
    }


    public static void main(String args[]) {

        int node_count = 6;
        List<List<NodeDist>> graph = new ArrayList<>(node_count);

        for (int i = 0; i < node_count; i++) {
            graph.add(new ArrayList<>());
        }

        // Node 0: <1,5> <2,1> <3,4>
        Collections.addAll(graph.get(0), new NodeDist(1, 5), new NodeDist(2, 1), new NodeDist(3, 4));

        // Node 1: <0,5> <2,3> <4,8>
        Collections.addAll(graph.get(1), new NodeDist(0, 5), new NodeDist(2, 3), new NodeDist(4, 8));

        // Node 2: <0,1> <1,3> <3,2> <4,1>
        Collections.addAll(graph.get(2), new NodeDist(0, 1), new NodeDist(1, 3), new NodeDist(3, 2), new NodeDist(4, 1));

        // Node 3: <0,4> <2,2> <4,2> <5,1>
        Collections.addAll(graph.get(3), new NodeDist(0, 4), new NodeDist(2, 2), new NodeDist(4, 2), new NodeDist(5, 1));

        // Node 4: <1,8> <2,1> <3,2> <5,3>
        Collections.addAll(graph.get(4), new NodeDist(1, 8), new NodeDist(2, 1), new NodeDist(3, 2), new NodeDist(5, 3));

        // Node 5: <3,1> <4,3>
        Collections.addAll(graph.get(5), new NodeDist(3, 1), new NodeDist(4, 3));

        int source_node = 0;
        DijkstraPqueue d = new DijkstraPqueue();
        d.ShortestPath(source_node, node_count, graph);

        System.out.println();
        source_node = 5;
        d.ShortestPath(source_node, node_count, graph);
    }

    public static List<List<NodeDist>> createGraphFromPointsList(List<Point> nodes, Map<Point, String> map, boolean unfiltered, List<Integer> weights) {
        Map<Point, Integer> pointToIndex = new HashMap<>();
        for (int i = 0; i < nodes.size(); i++) {
            pointToIndex.put(nodes.get(i), i);
        }

        List<List<NodeDist>> graph = new ArrayList<>(nodes.size());
        for (int i = 0; i < nodes.size(); i++) {
            graph.add(new ArrayList<>());
        }

        for (Point node : nodes) {
            if (map.get(node).equals("O")) {
                continue;
            }
            if (node.equals(nodes.getLast()) && !unfiltered) {
                continue;
            }
            int nodeIndex = pointToIndex.get(node);
            List<Point> validNeighborsForPoint;
            if (unfiltered) {
                validNeighborsForPoint = getNeighborsFromPointsList(node, nodes);
            } else {
                validNeighborsForPoint = getNeighborsFromMap(node, map);
            }
            for (Point neighbor : validNeighborsForPoint) {
                int adjIndex = pointToIndex.get(neighbor);
                int weight = 1;
                if (node.x == neighbor.x) {
                    // same row
                    // neighbor is to the left
                    if (neighbor.y < node.y) {
                        weight = weight + weights.getFirst();
                        // neighbor is to the right
                    } else {
                        weight = weight + weights.get(1);
                    }
                }
                if (node.y == neighbor.y) {
                    // same column
                    // neighbor is above
                    if (neighbor.x < node.x) {
                        weight = weight + weights.get(2);
                        // neighbor is below
                    } else {
                        weight = weight + weights.getLast();
                    }
                }

                if (map.get(neighbor).equals("O")) {
                    continue;
                }
                graph.get(nodeIndex).add(new NodeDist(adjIndex, weight));
            }
        }
        return graph;
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
            List<Point> validNeighborsForPoint = getNeighborsFromMap(node,map);
            //List<Point> validNeighborsForPoint = getNeighborsFromPointsList(node, nodes);
            for (Point neighbor : validNeighborsForPoint) {
                int adjIndex = pointToIndex.get(neighbor);
                graph.get(nodeIndex).add(new NodeDist(adjIndex, 1));
            }
        }
        return graph;
    }
    private static List<Point> getNeighborsFromMap(Point node, Map<Point, String> map) {
        List<Point> tests = List.of(
                new Point(node.x - 1, node.y),
                new Point(node.x + 1, node.y),
                new Point(node.x, node.y - 1),
                new Point(node.x, node.y + 1));
        List<Point> neighbors = new ArrayList<>();
        for (Point test : tests) {
            String value = map.get(test);
            if (value != null && (value.equals(".") || value.equals("E"))) {
                neighbors.add(test);
            }
        }
        return neighbors;
    }
    private static List<Point> getNeighborsFromPointsList(Point node, List<Point> points) {
        List<Point> tests = List.of(
                new Point(node.x - 1, node.y),
                new Point(node.x + 1, node.y),
                new Point(node.x, node.y - 1),
                new Point(node.x, node.y + 1));
        List<Point> neighbors = new ArrayList<>();
        for (Point test : tests) {
            if (points.contains(test)) {
                neighbors.add(test);
            }
        }
        return neighbors;
    }

    public static int manhattanDist(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }
}