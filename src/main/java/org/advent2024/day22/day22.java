package org.advent2024.day22;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.advent2024.day6.Puzzle12.readFromFile;

public class day22 {

    public static void main(String[] args) {
        //List<Integer> initialSecrets = List.of(1, 10, 100, 2024);
        List<String> input = readFromFile("src/main/resources/day22/input.txt");
        List<Integer> initialSecrets = new ArrayList<>();
        for (String line : input) {
            initialSecrets.add(Integer.parseInt(line));
        }
        long result = calculateSumOf2000thSecrets(initialSecrets);
        System.out.println("Sum of 2000th secret numbers: " + result);
        Map<List<Long>, Long> sequences = new HashMap<>();
        for (long secret : initialSecrets) {
            generateSequencesPer4(secret)
                    .forEach(
                            (key, value) ->
                                    sequences.compute(key, (k, v) -> v == null ? value : v + value)
                    );
        }
        long max = sequences.values().stream().mapToLong(Long::longValue).max().orElseThrow();
        System.out.println(max);
    }
    private static Map<List<Long>, Long> generateSequencesPer4(long secret) {
        Map<List<Long>, Long> result = new HashMap<>();
        long prevSecret = secret;
        long prevPrice = secret % 10L;
        List<Long> collector = new ArrayList<>();
        for (int i = 0; i < 2000; i++) {
            long nextSecret = evolveSecret(prevSecret);
            long price = nextSecret % 10L;
            long diff = price - prevPrice;
            collector.add(diff);
            if (collector.size() == 4) {
                result.putIfAbsent(new ArrayList<>(collector), price);
                collector.removeFirst();
            }
            prevPrice = price;
            prevSecret = nextSecret;
        }
        return result;
    }

    private static long calculateSumOf2000thSecrets(List<Integer> initialSecrets) {
        long sum = 0;
        for (long secret : initialSecrets) {
            long secret2000 = get2000thSecret(secret);
            System.out.println("Secret 2000 for secret: " + secret + " is: " + secret2000);
            sum += get2000thSecret(secret2000);
        }
        return sum;
    }

    private static long get2000thSecret(long secret) {
        for (int i = 0; i < 2000; i++) {
            secret = evolveSecret(secret);
        }
        return secret;
    }

    private static long evolveSecret(long secret) {
        Map<Long, Long> cache = new HashMap<>();
        return evolveSecret(secret, cache);
        
    }
    private static long evolveSecret(long secret, Map<Long, Long> cache) {
        if (cache.containsKey(secret)) {
            return cache.get(secret);
        }
        long originalsecret = secret;
        secret = mixAndPrune(secret, secret * 64);
        secret = mixAndPrune(secret, secret / 32);
        secret = mixAndPrune(secret, secret * 2048);
        cache.put(originalsecret, secret);
        return secret;
    }

    private static long mixAndPrune(long secret, long value) {
        secret ^= value;
        secret %= 16777216;
        return secret;
    }
}
