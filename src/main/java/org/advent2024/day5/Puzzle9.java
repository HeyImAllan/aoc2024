package org.advent2024.day5;

import org.advent2024.day3.Puzzle5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class Puzzle9 {
    public static List<String> readFromFile(String fileName) {
        return Puzzle5.readFromFile(fileName);
    }

    public static void main(String argsp[]) {
        List<String> input = readFromFile("src/main/resources/puzzle9/input.txt");
        List<List<Integer>> darules = getRules(input);
        System.out.println(darules);
        List<List<Integer>> printjobs = getPrintJobs(input);
        System.out.println(printjobs);
        int answer = 0;
        for (int i = 0; i < printjobs.size() - 1; ++i) {
            answer += reindeerPrint(printjobs.get(i), darules);
        }
        System.out.println(answer);

     }

    private static int reindeerPrint(List<Integer> printJob, List<List<Integer>> darules) {
        boolean complete = false;
        List<List<Integer>> filteredrules = new ArrayList<>();
        for (int i = 0; i < darules.size(); ++i) {
            if (new HashSet<>(printJob).containsAll(darules.get(i))) {
                filteredrules.add(darules.get(i));
            }
        }
        List<Integer> inputOrder = new ArrayList<>(printJob);
        boolean changedOrder;
        while (!complete) {
            changedOrder = false;
            for (int i = 0; i < filteredrules.size(); ++i) {
                for (int j = 0; j < inputOrder.size(); j++){
                    if (filteredrules.get(i).get(0).equals(inputOrder.get(j))) {
                        if (!inputOrder.subList(j+1, inputOrder.size()).contains(filteredrules.get(i).get(1)) && (j+1 <= inputOrder.size()))  {
                            List<Integer> swap =  new ArrayList<>();
                            swap.add(inputOrder.get(j));
                            swap.add(inputOrder.get(inputOrder.indexOf(filteredrules.get(i).get(1))));
                            inputOrder.set(j,swap.get(1));
                            inputOrder.set(inputOrder.indexOf(filteredrules.get(i).get(1)),swap.get(0));
                            changedOrder = true;

                        }
                    }
                }
            }
            if (!changedOrder || (inputOrder.equals(printJob))) {
                complete = true;
            }
        }
        if (inputOrder.equals(printJob)) {
            int middleNumber = inputOrder.get(inputOrder.size() / 2);
            return middleNumber;
        }
        return 0;

    }


    private static List<List<Integer>> getPrintJobs(List<String> input) {
        String filter = "|";
        String separator = ",";
        List<String> stringJobs = input.stream().filter(entry -> !entry.contains(filter)).filter(entry -> !entry.isEmpty()).toList();
        List<List<Integer>> intJobs = new ArrayList<>();
        for (String stringJob : stringJobs) {
            String[] split = stringJob.split(separator);
            List<Integer> job = new ArrayList<>();
            for (String number : split) {
                job.add(Integer.parseInt(number));
            }
            intJobs.add(job);
        }
        return intJobs;
    }

    private static List<List<Integer>> getRules(List<String> input) {
        String filter = "|";
        String separator = "\\|";
        List<String> rawRules = input.stream().filter(entry -> entry.contains(filter)).toList();
        List<List<Integer>> darules = new ArrayList<>(List.of());
        for (int i = 0; i < rawRules.size(); i++) {
            List<String> rule = Arrays.asList(rawRules.get(i).split(separator));
            darules.add(List.of(Integer.parseInt(rule.get(0)), Integer.parseInt(rule.get(1))));
        }
        return darules;
    }
}
