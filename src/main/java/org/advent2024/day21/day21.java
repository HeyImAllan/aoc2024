package org.advent2024.day21;

import java.util.*;

import static org.advent2024.day6.Puzzle12.readFromFile;

public class day21 {
    private static final Coord NUM_PAD_START = new Coord(2, 3);
    private static final Coord REMOTE_PAD_START = new Coord(2, 0);
    private static final char[][] NUM_PAD = new char[][]{
            new char[]{'7', '8', '9'},
            new char[]{'4', '5', '6'},
            new char[]{'1', '2', '3'},
            new char[]{'O', '0', 'A'}
    };
    private static final char[][] REMOTE_PAD = new char[][]{
            new char[]{'O', '^', 'A'},
            new char[]{'<', 'v', '>'}
    };

    private static final Map<Character, Coord> COORD_OF = Map.of(
            '^', new Coord(1, 0),
            'A', new Coord(2, 0),
            '<', new Coord(0, 1),
            'v', new Coord(1, 1),
            '>', new Coord(2, 1)
    );

    public static void main(String[] args) {
        List<String> input = readFromFile("src/main/resources/day21/input.txt");
        System.out.println("Part1: " + solve(2, input));
        System.out.println("Part2: " + solve(25, input));
    }

    private static final Map<PathDepth, Long> remoteInstrMemo = new HashMap<>();

    private static List<Path> getInstructionsForRemote(Coord start, char goal, char[][] pad) {
        if (isValidButton(start, pad) && getButton(start, pad) == goal) {
            return List.of(new Path(start, ""));
        }
        Queue<Path> pathsQueue = new ArrayDeque<>(List.of(new Path(start, "")));
        List<Path> shortestPaths = new ArrayList<>();
        int shortestLength = Integer.MAX_VALUE;
        while (!pathsQueue.isEmpty()) {
            Path currentPath = pathsQueue.poll();
            for (Coord neighbor : currentPath.end.next()) {
                if (isValidButton(neighbor, pad)) {
                    Path newPath = new Path(neighbor, currentPath.steps + currentPath.end.asDir(neighbor));
                    if (getButton(neighbor, pad) == goal) {
                        if (newPath.steps.length() < shortestLength) {
                            shortestLength = newPath.steps.length();
                            shortestPaths.clear();
                            shortestPaths.add(newPath);
                        } else if (newPath.steps.length() == shortestLength) {
                            shortestPaths.add(newPath);
                        }
                    } else if (newPath.steps.length() < shortestLength) {
                        pathsQueue.add(newPath);
                    }
                }
            }
        }
        return shortestPaths;
    }

    private static List<String> getInstructionsForNumpad(String code, Coord start, String core, char[][] pad) {
        if (code.isEmpty()) {
            return List.of(core);
        }
        char endPoint = code.charAt(0);
        List<String> collect = new ArrayList<>();
        for (Path path : getInstructionsForRemote(start, endPoint, pad)) {
            collect.addAll(getInstructionsForNumpad(code.substring(1), path.end, core + path.steps + "A", pad));
        }
        int minLength = collect.stream().mapToInt(String::length).min().orElseThrow();
        return collect.stream().filter(s -> s.length() == minLength).distinct().toList();
    }

    private static char getButton(Coord coord, char[][] pad) {
        return pad[coord.y][coord.x];
    }

    private static boolean isValidButton(Coord coord, char[][] pad) {
        return 0 <= coord.y && coord.y < pad.length && 0 <= coord.x && coord.x < pad[0].length && pad[coord.y][coord.x] != 'O';
    }

    private static long solve(int robotCount, List<String> input) {
        long sum = 0;
        for (String code : input) {
            long remoteInstrCount = Long.MAX_VALUE;
            List<String> remoteInstructions = getInstructionsForNumpad(code, NUM_PAD_START, "", NUM_PAD);
            for (String remoteInstr : remoteInstructions) {
                long instrCount = calculateMinimumPushes(remoteInstr, robotCount);
                remoteInstrCount = Math.min(remoteInstrCount, instrCount);
            }
            sum += remoteInstrCount * codeToNumber(code);
        }
        return sum;
    }

    private static long calculateMinimumPushes(String remoteInstructions, int robotCount) {
        if (robotCount == 0) {
            return remoteInstructions.length();
        }
        PathDepth key = new PathDepth(remoteInstructions, robotCount);
        if (remoteInstrMemo.containsKey(key)) {
            return remoteInstrMemo.get(key);
        }
        Coord startPosition = REMOTE_PAD_START;
        long sum = 0L;
        for (char endpoint : remoteInstructions.toCharArray()) {
            long min = Long.MAX_VALUE;
            for (String remoteInstr : remoteInstructions(endpoint, startPosition)) {
                min = Long.min(min, calculateMinimumPushes(remoteInstr, robotCount - 1));
            }
            sum += min;
            startPosition = COORD_OF.get(endpoint);
        }
        remoteInstrMemo.put(key, sum);
        return sum;
    }

    static List<String> remoteInstructions(char to, Coord at) {
        return getInstructionsForRemote(at, to, REMOTE_PAD).stream().map(p -> p.steps() + "A").toList();
    }

    private record Coord(int x, int y) {
        List<Coord> next() {
            return List.of(
                    new Coord(x + 1, y), new Coord(x, y + 1),
                    new Coord(x - 1, y), new Coord(x, y - 1));
        }

        char asDir(Coord other) {
            if (x < other.x()) {
                return '>';
            } else if (x > other.x()) {
                return '<';
            } else if (y < other.y()) {
                return 'v';
            } else if (y > other.y()) {
                return '^';
            } else {
                throw new IllegalArgumentException("Same coord was given: " + this + " to: " + other);
            }
        }
    }

    private record Path(Coord end, String steps) {
    }

    private record PathDepth(String path, int robotCount) {
    }

    private static int codeToNumber(String code) {
        return Integer.parseInt(code.substring(0, code.length() - 1));
    }

}
