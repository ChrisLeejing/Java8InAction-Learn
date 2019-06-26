package com.chris.chap6;

import com.chris.chap5.Dish;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This is description.
 * Chapter 6. Collecting data with streams
 * This chapter covers
 * 1. Creating and using a collector with the Collectors class
 * 2. Reducing streams of data to a single value
 * 3. Summarization as a special case of reduction
 * 4. Grouping and partitioning data
 * 5. Developing your own custom collectors
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
        Optional<Dish> maxCalorieDish = menu.stream().collect(Collectors.maxBy(Comparator.comparingInt(Dish::getCalories)));
        System.out.println("maxCalorieDish: " + maxCalorieDish);
        // 6.2.2. Summarization
        Integer totalCalories = menu.stream().collect(Collectors.summingInt(Dish::getCalories));
        System.out.println("totalCalories: " + totalCalories);

        Double avgCalories = menu.stream().collect(Collectors.averagingInt(Dish::getCalories));
        System.out.println("avgCalories: " + avgCalories);

        IntSummaryStatistics summaryCalories = menu.stream().collect(Collectors.summarizingInt(Dish::getCalories));
        System.out.println("summaryCalories: " + summaryCalories);

        // 6.2.3. Joining Strings
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
}
