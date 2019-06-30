package com.chris.chap6;

import com.chris.chap5.Dish;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.*;

/**
 * This is description.
 * Chapter 6. Collecting data with streams
 * This chapter covers
 * 1. Creating and using a collector with the Collectors class
 * 2. Reducing streams of data to a single value
 * 3. Summarization as a special case of reduction
 * 4. Grouping and partitioning data
 * 5. Developing your own custom collectors
 *
 * @author Chris Lee
 * @date 2019/6/26 7:40
 */
public class CollectorsDemo {
    public static void main(String[] args) {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");
        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );

        List<Dish> menu = Arrays.asList(
                new Dish("pork", false, 800, Dish.Type.MEAT),
                new Dish("beef", false, 700, Dish.Type.MEAT),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("season fruit", true, 120, Dish.Type.OTHER),
                new Dish("pizza", true, 550, Dish.Type.OTHER),
                new Dish("prawns", false, 300, Dish.Type.FISH),
                new Dish("salmon", false, 450, Dish.Type.FISH));

        // 6.1. Collectors in a nutshell
        List<Transaction> transactionList = transactions.stream().collect(Collectors.toList());
        // 6.2. Reducing and summarizing
        Long howManyDishes = menu.stream().collect(Collectors.counting());
        long howManyDishes2 = menu.stream().count();
        // 6.2.1. Finding maximum and minimum in a stream of values
        Optional<Dish> maxCalorieDish = menu.stream().collect(maxBy(comparingInt(Dish::getCalories)));
        System.out.println("maxCalorieDish: " + maxCalorieDish);
        // 6.2.2. Summarization
        Integer totalCalories = menu.stream().collect(Collectors.summingInt(Dish::getCalories));
        System.out.println("totalCalories: " + totalCalories);

        Double avgCalories = menu.stream().collect(Collectors.averagingInt(Dish::getCalories));
        System.out.println("avgCalories: " + avgCalories);

        IntSummaryStatistics summaryCalories = menu.stream().collect(Collectors.summarizingInt(Dish::getCalories));
        System.out.println("summaryCalories: " + summaryCalories);

        // 6.2.3. Joining Strings
        String names = menu.stream().map(Dish::getName).collect(Collectors.joining());
        String names2 = menu.stream().map(Dish::getName).collect(Collectors.joining(", "));
        String names3 = menu.stream().map(Dish::getName).collect(Collectors.joining(", ", "(", ")"));
        System.out.println("names: " + names);
        System.out.println("names2: " + names2);
        System.out.println("names3: " + names3);

        // 6.2.4. Generalized summarization with reduction
        // Collection framework flexibility: doing the same operation in different
        // ways
        Integer calories = menu.stream().collect(Collectors.summingInt(Dish::getCalories));
        // calories2: this method is the best one.
        int calories2 = menu.stream().mapToInt(Dish::getCalories).sum();
        Integer calories3 = menu.stream().collect(Collectors.reducing(0, Dish::getCalories, (a, b) -> a + b));
        Integer calories4 = menu.stream().map(Dish::getCalories).reduce(Integer::sum).get();
        System.out.println("calories: " + calories);
        System.out.println("calories2: " + calories2);
        System.out.println("calories3: " + calories3);
        System.out.println("calories4: " + calories4);

        Map<Dish.Type, Optional<Dish>> mostCaloricByType =
                menu.stream()
                        .collect(groupingBy(Dish::getType,
                                maxBy(comparingInt(Dish::getCalories))));
        System.out.println("mostCaloricByType: " + mostCaloricByType);

        // 6.3. Grouping
        Map<Dish.Type, List<Dish>> dishesByType = menu.stream().collect(groupingBy(Dish::getType));
        System.out.println("dishesByType: " + dishesByType);
        // grouping by yourself(public enum CaloricLevel { DIET, NORMAL, FAT })
        Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream().collect(groupingBy(dish -> {
            if (dish.getCalories() <= 400) {
                return CaloricLevel.DIET;
            } else if (dish.getCalories() <= 700) {
                return CaloricLevel.NORMAL;
            } else {
                return CaloricLevel.FAT;
            }
        }));
        System.out.println("dishesByCaloricLevel: " + dishesByCaloricLevel);

        // 6.3.1. Multilevel grouping
        Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel = menu.stream().collect(groupingBy(Dish::getType, groupingBy(dish -> {
            if (dish.getCalories() <= 400) {
                return CaloricLevel.DIET;
            } else if (dish.getCalories() <= 700) {
                return CaloricLevel.NORMAL;
            } else {
                return CaloricLevel.FAT;
            }
        })));
        System.out.println("dishesByTypeCaloricLevel: " + dishesByTypeCaloricLevel);

        // 6.3.2. Collecting data in subgroups
        Map<Dish.Type, Long> typeCount = menu.stream().collect(groupingBy(Dish::getType, counting()));
        System.out.println("typeCount: " + typeCount);
        Map<Dish.Type, Optional<Dish>> maxCaloricByType = menu.stream().collect(groupingBy(Dish::getType, maxBy(comparingInt(Dish::getCalories))));
        System.out.println("maxCaloricByType: " + maxCaloricByType);

        // Adapting the collector result to a different type(delete the Optional by collectingAndThen(...))
        Map<Dish.Type, Dish> maxCaloricByType2 = menu.stream().collect(groupingBy(Dish::getType, collectingAndThen(maxBy(comparingInt(Dish::getCalories)), Optional::get)));
        System.out.println("maxCaloricByType2: " + maxCaloricByType2);

        // Other examples of collectors used in conjunction with groupingBy
        Map<Dish.Type, Set<CaloricLevel>> caloricByTypeSet = menu.stream().collect(groupingBy(Dish::getType, mapping(dish -> {
            if (dish.getCalories() <= 400) {
                return CaloricLevel.DIET;
            } else if (dish.getCalories() <= 700) {
                return CaloricLevel.NORMAL;
            } else {
                return CaloricLevel.FAT;
            }
        }, toSet())));
        System.out.println("caloricByTypeSet: " + caloricByTypeSet);

        Map<Dish.Type, HashSet<CaloricLevel>> caloricByTypeHashSet = menu.stream().collect(groupingBy(Dish::getType, mapping(dish -> {
            if (dish.getCalories() <= 400) {
                return CaloricLevel.DIET;
            } else if (dish.getCalories() <= 700) {
                return CaloricLevel.NORMAL;
            } else {
                return CaloricLevel.FAT;
            }
        }, toCollection(HashSet::new))));
        System.out.println("caloricByTypeHashSet: " + caloricByTypeHashSet);
    }

// Trader and Transaction are classes defined as follows:

    static class Trader {
        private final String name;
        private final String city;

        public Trader(String n, String c) {
            this.name = n;
            this.city = c;
        }

        public String getName() {
            return this.name;
        }

        public String getCity() {
            return this.city;
        }

        @Override
        public String toString() {
            return "Trader:" + this.name + " in " + this.city;
        }
    }

    static class Transaction {
        private final Trader trader;
        private final int year;
        private final int value;

        public Transaction(Trader trader, int year, int value) {
            this.trader = trader;
            this.year = year;
            this.value = value;
        }

        public Trader getTrader() {
            return this.trader;
        }

        public int getYear() {
            return this.year;
        }

        public int getValue() {
            return this.value;
        }

        @Override
        public String toString() {
            return "{" + this.trader + ", " +
                    "year: " + this.year + ", " +
                    "value:" + this.value + "}";
        }
    }

    public enum CaloricLevel { DIET, NORMAL, FAT }
}
