package org.advent2024.day13;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.advent2024.day6.Puzzle12.readFromFile;

public class Puzzle26 {
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
        BigDecimal x1 = new BigDecimal(0);
        BigDecimal y1 = new BigDecimal(0);
        BigDecimal x2 = new BigDecimal(0);
        BigDecimal y2 = new BigDecimal(0);
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
                x1 = new BigDecimal(button.get(0));
                y1 = new BigDecimal(button.get(1));
            }
            if (count == 1 ){
                Matcher m = Pattern.compile(buttonRegex)
                        .matcher(line);
                List<Integer> button = new ArrayList<>();
                while (m.find()) {
                    button.add(Integer.valueOf(m.group()));
                }
                x2 = new BigDecimal(button.get(0));
                y2 = new BigDecimal(button.get(1));
            }
            if (count == 2 ){
                Matcher m = Pattern.compile(buttonRegex)
                        .matcher(line);
                List<Integer> button = new ArrayList<>();
                while (m.find()) {
                    button.add(Integer.valueOf(m.group()));
                }
                BigDecimal p1 = new BigDecimal(button.get(0));
                p1 = p1.add(new BigDecimal(10000000000000L));

                BigDecimal p2 = new BigDecimal(button.get(1));
                p2 = p2.add(new BigDecimal(10000000000000L));
                System.out.println(x1 + " " + y1 + " " + x2 + " " +  y2 + " " + p1 +" "+p2);
                BigDecimal m1 = p1.multiply(y2);
                BigDecimal m2 = p2.multiply(x2);
                BigDecimal m3 = x1.multiply(y2);
                BigDecimal m4 = y1.multiply(x2);
                BigDecimal left = m1.subtract(m2);
                BigDecimal right = m3.subtract(m4);

                BigDecimal a = left.divide(right, 2, RoundingMode.HALF_UP);
                BigDecimal m5 = p2.multiply(x1);
                BigDecimal m6 = p1.multiply(y1);
                BigDecimal m7 = x1.multiply(y2);
                BigDecimal m8 = y1.multiply(x2);
                BigDecimal left2 = m5.subtract(m6);
                BigDecimal right2 = m7.subtract(m8);

                BigDecimal b = left2.divide(right2, 2, RoundingMode.HALF_UP);

                System.out.println(a + " " + b);
                if (BigDecimal.ZERO.compareTo(a.remainder(new BigDecimal(1))) == 0 &&
                        BigDecimal.ZERO.compareTo(b.remainder(new BigDecimal(1))) == 0){
                    answer += (long) (3 * a.doubleValue() + b.doubleValue());
                }
            }
            count++;

        }
        return answer;
    }

}
