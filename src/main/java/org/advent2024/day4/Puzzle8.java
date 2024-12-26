package org.advent2024.day4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.System.exit;

public class Puzzle8 {
    public static List<String> readFromFile(String fileName) {
        List<String> list = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            exit(1);
        }
        return list;
    }
    public static void main(String argsp[]) {
        List<String> input = readFromFile("src/main/resources/puzzle7/input.txt");
        findXmas(input);
    }
    public static void findXmas(List<String> list) {
        List<List<String>> map = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            map.add(Arrays.asList(list.get(i).split("")));
        }
        int answer = 0;
        int masanswer = 0;
        for (int y = 0; y < map.size(); y++) {
            for (int x = 0; x < map.get(y).size(); x++) {
                if (Objects.equals(map.get(y).get(x), "X")) {
                    answer += findHorizontal(map,x,y);
                    answer += findVertical(map,x,y);
                    answer += findDiagonal(map,x,y, "XMAS");
                }
                if (Objects.equals(map.get(y).get(x), "A")) {
                    int mascount = findStar(map,x,y, "MAS");
                    if (mascount == 2) {
                        System.out.println("HO HO HO MAS at X: " + (x+1) + " Y: " + (y+1));

                        masanswer++;
                    }
                }
            }
        }
        System.out.println(answer);
        System.out.println(masanswer);
    }

    public static int findVertical(List<List<String>> map, int x, int y) {
        int count = 0;
        String searchable = "XMAS";
        for (int i = 0; i < searchable.length(); i++) {
            try {
                if (Objects.equals(map.get(y + i).get(x), String.valueOf(searchable.toCharArray()[i]))) {
                    if (i == searchable.length() -1) {
                        System.out.println("HO HO HO VERTICAL XMAS at X: " + (x+1) + " Y: " + y);
                        count++;
                    }
                } else {
                    break;
                }

            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        for (int i = 0; i < searchable.length(); i++) {
            try {
                if (Objects.equals(map.get(y - i ).get(x), String.valueOf(searchable.toCharArray()[i]))) {
                    if (i == searchable.length() -1) {
                        System.out.println("HO HO HO VERTICAL XMAS at X: " + (x+1) + " Y: " + y);
                        count++;
                    }
                } else {
                    break;
                }

            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        return count;
    }

    public static int findHorizontal(List<List<String>> map, int x, int y) {
        String searchable = "XMAS";
        int count = 0;
        for (int i = 0; i < searchable.length(); i++) {
            try {
                if (Objects.equals(map.get(y).get(x + i), String.valueOf(searchable.toCharArray()[i]))) {
                    if (i == searchable.length() -1) {
                        count++;
                    }
                } else {
                    break;
                }

            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        for (int i = 0; i < searchable.length(); i++) {
            try {
                if (Objects.equals(map.get(y).get(x - i), String.valueOf(searchable.toCharArray()[i]))) {
                    if (i == searchable.length() -1) {
                        count++;
                    }
                } else {
                    break;
                }

            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        return count;
    }
    public static int findStar(List<List<String>> map, int x, int y, String searchable) {
        int count = 0;
        for (int i = 0; i < searchable.length(); i++) {
            try {
                // GOES NE
                if (Objects.equals(map.get(y - i + 1).get(x + i - 1), String.valueOf(searchable.toCharArray()[i]))) {
                    if (i == searchable.length() -1) {
                        count++;
                    }
                } else {
                    break;
                }

            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        for (int i = 0; i < searchable.length(); i++) {
            try {
                // GOES NW
                if (Objects.equals(map.get(y - i + 1).get(x - i + 1 ), String.valueOf(searchable.toCharArray()[i]))) {
                    if (i == searchable.length() -1) {
                        count++;
                    }
                } else {
                    break;
                }

            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        for (int i = 0; i < searchable.length(); i++) {
            try {
                // GOES SE
                if (Objects.equals(map.get(y + i - 1).get(x + i - 1), String.valueOf(searchable.toCharArray()[i]))) {
                    if (i == searchable.length() -1) {
                        count++;
                    }
                } else {
                    break;
                }

            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        for (int i = 0; i < searchable.length(); i++) {
            try {
                // GOES SW
                if (Objects.equals(map.get(y + i - 1).get(x - i + 1), String.valueOf(searchable.toCharArray()[i]))) {
                    if (i == searchable.length() -1) {
                        count++;
                    }
                } else {
                    break;
                }

            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }

        return count;
    }
    public static int findDiagonal(List<List<String>> map, int x, int y, String searchable) {
        int count = 0;
        for (int i = 0; i < searchable.length(); i++) {
            try {
                if (Objects.equals(map.get(y - i).get(x + i), String.valueOf(searchable.toCharArray()[i]))) {
                    if (i == searchable.length() -1) {

                        count++;
                    }
                } else {
                    break;
                }

            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        for (int i = 0; i < searchable.length(); i++) {
            try {
                if (Objects.equals(map.get(y - i).get(x - i), String.valueOf(searchable.toCharArray()[i]))) {
                    if (i == searchable.length() -1) {
                        count++;
                    }
                } else {
                    break;
                }

            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        for (int i = 0; i < searchable.length(); i++) {
            try {
                if (Objects.equals(map.get(y + i).get(x + i), String.valueOf(searchable.toCharArray()[i]))) {
                    if (i == searchable.length() -1) {
                        count++;
                    }
                } else {
                    break;
                }

            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        for (int i = 0; i < searchable.length(); i++) {
            try {
                if (Objects.equals(map.get(y + i).get(x - i), String.valueOf(searchable.toCharArray()[i]))) {
                    if (i == searchable.length() -1) {
                        count++;
                    }
                } else {
                    break;
                }

            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }

        return count;
    }
}
