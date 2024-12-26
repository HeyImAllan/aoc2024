package org.advent2024.day14;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.advent2024.day6.Puzzle12.readFromFile;

public class Puzzle28 {
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
            robot = new Point(Integer.parseInt(parts[0].split("=")[1].split(",")[0]), Integer.parseInt(parts[0].split("=")[1].split(",")[1]));
            velocity = new Point(Integer.parseInt(parts[1].split("=")[1].split(",")[0]), Integer.parseInt(parts[1].split("=")[1].split(",")[1]));
            robots.add(List.of(robot, velocity));
        }
        System.out.println(robots);
        for (int i = 0; i < 10000; i++) {
            List<Point> robotPositions = new ArrayList<>();
            for (int j = 0; j< robots.size(); j++) {
                List<Point> robot = robots.get(j);
                Point robotPos = robotWalk(robot.getFirst(), robot.getLast(), 1);
                robotPositions.add(robotPos);
                robots.set(j, List.of(robotPos, robot.getLast()));
            }
            printRobots(robotPositions, i);
            if (i == 99) {
                robotEndPositions = robotPositions;
            }
        }



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

    public static void printRobots(List<Point> robots, int k) {
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

        int countrow;
        for(int row  = 0; row < mapHeight; row++) {
            countrow = 0;
            for (int col = 0; col < map.get(row).size(); col++) {
                if (map.get(row).get(col) != 0) {
                    countrow++;
                }
                if (countrow == 32) {
                    System.out.println(k+1);
                    for (List<Integer> row2 : map) {
                        for (int i : row2) {
                            if (i == 0) {
                                System.out.print(".");
                            } else {
                                System.out.print("#");
                            }
                        }
                        System.out.print("\n");
                    }
                    System.out.print("\n");
                    System.exit(0);
                }
            }
        }



//        int total = topleft * topright * bottomleft * bottomright; (THis helped me solve the p2).
//        if (total < 161000000 && total > 159168240) {
//            File file = new File("append.txt");
//            FileWriter fr = null;
//            try {
//                fr = new FileWriter(file, true);
//                BufferedWriter br = new BufferedWriter(fr);
//                br.write(k + " " + total + "\n");
//                for (List<Integer> row : map) {
//                    for (int i : row) {
//                        if (i == 0) {
//                            br.write(".");
//                        } else {
//                            br.write("#");
//                        }
//
//                    }
//                    br.write("\n");
//                }
//                br.write("\n");
//                br.close();
//                fr.close();
//
//        } catch(IOException e){
//            throw new RuntimeException(e);
//        }
//    }


    }


}
