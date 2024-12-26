package org.advent2024.day14;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.advent2024.day6.Puzzle12.readFromFile;

public class Puzzle27 {
    public static int mapWidth = 101;
    public static int mapHeight = 103;
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        List<String> input = readFromFile("src/main/resources/puzzle27/input.txt");

        System.out.println(input);
        List<List<Point>> robots = new ArrayList<>();
        List<Point> robotEndPositions = new ArrayList<>();
        for (String line : input) {
            String[] parts = line.split(" ");
            Point robot;
            Point velocity;
            robot = new Point(Integer.parseInt(parts[0].split("=")[1].split(",")[0]),Integer.parseInt(parts[0].split("=")[1].split(",")[1]));
            velocity = new Point(Integer.parseInt(parts[1].split("=")[1].split(",")[0]),Integer.parseInt(parts[1].split("=")[1].split(",")[1]));
            robots.add(List.of(robot, velocity));
        }
        System.out.println(robots);
        for (List<Point> robot: robots) {
            Point robotPos = robotWalk(robot.getFirst(), robot.getLast(), 100);
            System.out.println(robotPos);
            robotEndPositions.add(robotPos);
        }

        System.out.println("robot beginpositions");
        System.out.println(robots.size());
        List<Point> robotList = new ArrayList<>();
        for (List<Point> robot : robots) {
                robotList.add(robot.getFirst());
            }
        printRobots(robotList);
        System.out.println("Robot endpositions");
        System.out.println(robotEndPositions.size());
        printRobots(robotEndPositions);


        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total runtime: " + totalTime + "ms");
    }

    private static Point robotWalk(Point robotPos, Point robotVelocity, int repeat) {
        for (int i = 0; i < repeat; i++) {
            int robotX = (int) (robotPos.getX() + robotVelocity.getX());
            if (robotX < 0) {
                robotX = robotX + mapWidth;
            } else if (robotX > mapWidth - 1) {
                robotX -= mapWidth;
            }
            int robotY = (int) (robotPos.getY() + robotVelocity.getY());
            if (robotY < 0) {
                robotY = robotY + mapHeight;
            } else if (robotY > mapHeight -1 ) {
                robotY -= mapHeight;
            }
            robotPos = new Point(robotX, robotY);
        }
        return robotPos;
    }

    public static void printRobots(List<Point> robots) {
        List<List<Integer>> map = new ArrayList<>();
        for (int i = 0; i < mapHeight; i++) {
            List<Integer> row = new ArrayList<>(mapWidth);
            for (int j = 0; j < mapWidth; j++) {
                row.add(0);
            }
            map.add(row);
        }
        for (Point robot : robots) {
            map.get(robot.y).set(robot.x, map.get(robot.y).get(robot.x) + 1);
        }
        for (List<Integer> row : map) {
            for (int i : row) {
                System.out.print(i);
            }
            System.out.print("\n");
        }
        int topleft=0;
        int topright=0;
        int bottomleft=0;
        int bottomright=0;
        for (int i = 0; i < (mapHeight-1)/2; i++) {
            for (int j = 0; j < (mapWidth-1)/2; j++) {
                topleft += map.get(i).get(j);
            }
        }
        System.out.println(topleft);
        for (int i = 0; i < (mapHeight-1)/2; i++) {
            for (int j = ((mapWidth-1)/2)+1; j < mapWidth; j++) {
                topright += map.get(i).get(j);
            }
        }
        System.out.println(topright);
        for (int i = ((mapHeight-1)/2)+1; i < mapHeight; i++) {
            for (int j = 0; j < (mapWidth-1)/2; j++) {
                bottomleft += map.get(i).get(j);
            }
        }
        System.out.println(bottomleft);
        for (int i = ((mapHeight-1)/2)+1; i < mapHeight; i++) {
            for (int j = ((mapWidth-1)/2)+1; j < mapWidth; j++) {
                bottomright += map.get(i).get(j);
            }
        }
        System.out.println(bottomright);
        System.out.println(topleft*topright*bottomleft*bottomright);
    }

}
