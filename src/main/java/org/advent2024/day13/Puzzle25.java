package org.advent2024.day13;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.advent2024.day6.Puzzle12.readFromFile;

public class Puzzle25 {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        List<String> input = readFromFile("src/main/resources/puzzle25/input.txt");

        System.out.println(process(input));

        long endTime   = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Total runtime: " + totalTime + "ms");
    }

    private static long process(List<String> input) {
        int count = 0;
        long answer = 0L;
        String buttonRegex = "\\d+";
        double x1 = 0;
        double y1 = 0;
        double x2 = 0;
        double y2 = 0;
        double p1 = 0;
        double p2 = 0;
        for (String line: input) {
            if (count == 4) {

                count = 0;
            }

            if (count == 0 ){
                Matcher m = Pattern.compile(buttonRegex)
                        .matcher(line);
                List<Integer> button = new ArrayList<>();
                while (m.find()) {
                    button.add(Integer.valueOf(m.group()));
                }
                x1 = button.get(0);
                y1 = button.get(1);
            }
            if (count == 1 ){
                Matcher m = Pattern.compile(buttonRegex)
                        .matcher(line);
                List<Integer> button = new ArrayList<>();
                while (m.find()) {
                    button.add(Integer.valueOf(m.group()));
                }
                x2 = button.get(0);
                y2 = button.get(1);
            }
            if (count == 2 ){
                Matcher m = Pattern.compile(buttonRegex)
                        .matcher(line);
                List<Integer> button = new ArrayList<>();
                while (m.find()) {
                    button.add(Integer.valueOf(m.group()));
                }
                p1 = button.get(0);
                p2 = button.get(1);
                System.out.println(x1 + " " + y1 + " " + x2 + " " +  y2 + " " + p1 +" "+p2);
                //Button A: X+94, Y+34
                //Button B: X+22, Y+67
                //Prize: X=8400, Y=5400
                // a * x1 + b * x2 = p1
                // a * y1 + b * y2 = p2

                // determine a
                // a * x1 * y2 + (b * x2 * y2) = p1 * y2
                // a * y1 * x2 + (b * y2 * x2) = x2 * p2

                // a * x1 * y2 - a * x2 * y1 = p1 * y2 - x2 * p2
                // a = (p1 * y2 - x2 * p2) / (x1 * y2 - x2 * y1)

                // determina b
                // (a * x1 * y1) + b * x2 * y1 = p1 * y1
                // (a * y1 * x1) + b * y2 * x1 = p2 * x1

                // b * x2 * y1 - b * y2 * x1 = p1 * y1 - p2 * x1
                // b * (x2 * y1 - y2 * x1) = (p1 * y1 - p2 * x1)
                // b = (p1 * y1 - p2 * x1) / (x2 * y1 - x1 * y2)

                // Simplified:
                // a * 3 + b * 2 = 8
                // a * 4 + b * 6 = 14

                // If we want to know a, we eliminate b by cancelling it out for both equations:
                // a * 3 * 6 + (b * 2 * 6) = 8 * 6
                // a * 4 * 2 + (b * 6 * 2) = 14 * 2

                // Now we can eliminate B:
                // a * 3 * 6 = 8 * 6
                // a * 4 * 2 = 14 * 2

                // And because both calculations are equally solving for a.
                // a * 3 * 6 - a * 4 * 2 = 8 * 6 - 14 * 2
                // simplify a(x * x) - a(x* x) = a (x * x - x * x)
                // a * (3 * 6 - 4 * 2) = (8 * 6 - 14 * 2)
                // a = (8 * 6 - 14 * 2) / (3 * 6 - 4 * 2)

                // And solve:
                // a = 20 / 10
                // a = 2


                double a = (p1*y2 - p2*x2) / (x1*y2 - y1*x2);
                double b = (p2*x1 - p1*y1) / (x1*y2 - y1*x2);
                System.out.println(a + " " + b);
                if (a % 1 == 0 && b % 1 == 0){
                    answer += (long) (3 * a + b);
                }





            }
            count++;

        }
        return answer;
    }

}
