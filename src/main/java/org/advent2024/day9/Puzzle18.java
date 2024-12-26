package org.advent2024.day9;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static org.advent2024.day6.Puzzle12.readFromFile;

public class Puzzle18 {
    public static void main(String[] args) {
        List<String> input = readFromFile("src/main/resources/puzzle17/input.txt");
        List<Integer> disk = convertToDisk(input);
        printDisk(disk);
        List<Integer> compactedDisk;
        compactedDisk = compact(disk);
        printDisk(compactedDisk);
        System.out.println(checksum(compactedDisk));




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
    private static List<Integer> compact(List<Integer> disk) {
        List<Integer> compressed = new ArrayList<>(disk);
        for (int i = disk.size()-1; i>0; i--) {
            if (disk.get(i) != null) {
                int initialValue = disk.get(i);
                int valueLength = 0;
                int j = i;
                while(disk.get(j) != null && disk.get(j) == initialValue && j > 0) {
                    j--;
                    valueLength++;
                }
                // If I don't do anything with this value, it'll loop.
                i = i - Math.abs(i - j) +1;
                for (int k = 0; k < compressed.size(); k++) {
                    // I don't want to look for null space to the right of the compacted value.
                    if (k >= i ) {
                        break;
                    }
                    if (compressed.get(k) == null) {
                        int nullSpaceLength = 0;
                        while(compressed.get(k) == null ) {
                            nullSpaceLength++;
                            k++;
                            if ( k >= compressed.size()) {
                                break;
                            }
                        }
                        if (valueLength <= nullSpaceLength) {
                            for (int l = 0; l < valueLength; l++) {
                                compressed.set(k - nullSpaceLength + l, initialValue);
                                compressed.set(i + l, null);
                            }
                            break;
                        }
                    }
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
