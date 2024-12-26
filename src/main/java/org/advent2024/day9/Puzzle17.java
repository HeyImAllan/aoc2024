package org.advent2024.day9;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static org.advent2024.day6.Puzzle12.readFromFile;

public class Puzzle17 {
    public static void main(String[] args) {
        List<String> input = readFromFile("src/main/resources/puzzle17/input.txt");
        System.out.println(input);
        System.out.println(input.size());
        List<Integer> disk = convertToDisk(input);
        System.out.println(disk);
        printDisk(disk);
        List<Integer> compressed = compress(disk);
        printDisk(compressed);
        System.out.println(checksum(compressed));

    }

    private static List<Integer> compress(List<Integer> disk) {
        List<Integer> compressed = new ArrayList<>(disk);
        for (int i = disk.size()-1; i>=0; i--) {
            if (disk.get(i) != null) {
                AtomicInteger j = new AtomicInteger();
                int index = compressed.stream()
                        .peek(v -> j.incrementAndGet())
                        .anyMatch(Objects::isNull) ?
                        j.get() - 1 : -1;

                if (i > index) {
                    compressed.set(index, disk.get(i));
                    compressed.set(i, null);
                } else {
                    break;
                }
            }
        }
        return compressed;
    }

    private static List<Integer> convertToDisk(List<String> input) {
        List<Integer> disk = new ArrayList<>();
        String[] rawDisk = input.getFirst().split("");
        for (int i = 0; i < rawDisk.length; i++) {
            if (i % 2 == 0) {
                for (int j = 0; j < Integer.parseInt(rawDisk[i]); j++) {
                    disk.add((i+1)/2);
                }
            } else {
                for (int j = 0; j < Integer.parseInt(rawDisk[i]); j++) {
                    disk.add(null);
                }
            }
        }
        return disk;
    }
    private static void printDisk(List<Integer> disk) {
        for (int i = 0; i < disk.size(); i++) {
            if (disk.get(i) != null) {
                System.out.print(disk.get(i));
            } else {
                System.out.print(".");
            }
        }
        System.out.print("\n");
    }
    private static Long checksum(List<Integer> disk) {
        Long checksum = 0L;
        for (int i = 0; i < disk.size(); i++) {
            if (disk.get(i) != null) {
                checksum += (i*disk.get(i));
            }

        }
        return checksum;
    }
}
