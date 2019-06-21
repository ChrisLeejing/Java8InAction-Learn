package com.chris.chap5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * This is description.
 *
 * @author Chris Lee
 * @date 2019/6/19 20:56
 */
public class StreamDemo {
    public static void main(String[] args) {
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

        // external iteration
        List<Dish> vegetarianDishes = new ArrayList<>();
        for (Dish dish : menu) {
            if (dish.isVegetarian()) {
                vegetarianDishes.add(dish);
            }
        }
        System.out.println("vegetarianDishes: " + vegetarianDishes);

        // internal iteration
        List<Dish> vegetarianDishes2 = menu.stream()
                .filter(Dish::isVegetarian)
                .collect(toList());
        System.out.println("vegetarianDishes2: " + vegetarianDishes2);

        // 5.1. Filtering and slicing
        // 5.1.1. Filtering with a predicate
        List<Dish> vegetarianMenu = menu.stream()
                .filter(Dish::isVegetarian)
                .collect(toList());
        System.out.println("vegetarianMenu: " + vegetarianMenu);

        // 5.1.2. Filtering unique elements
        List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        numbers.stream()
                .filter(number -> number % 2 == 0)
                .distinct()
                .forEach(System.out::println);

        // 5.1.3. Truncating a stream
        List<Dish> threeDishes = menu.stream()
                .filter(dish -> dish.getCalories() > 300)
                .limit(3)
                .collect(toList());
        System.out.println("threeDishes: " + threeDishes);

        // 5.1.4. Skipping elements
        List<Dish> skip2Dishes = menu.stream()
                .filter(dish -> dish.getCalories() > 300)
                .skip(2)
                .collect(toList());
        System.out.println("skip2Dishes: " + skip2Dishes);

        // Quiz 5.1: Filtering
        // How would you use streams to filter the first two meat dishes?
        List<Dish> firstTwoMeatDishes = menu.stream()
                .filter(dish -> dish.getType() == Dish.Type.MEAT)
                .limit(2)
                .collect(toList());
        System.out.println("firstTwoMeatDishes: " + firstTwoMeatDishes);

        // 5.2. Mapping
        // 5.2.1. Applying a function to each element of a stream
        List<Integer> lengthDishNames = menu.stream()
                .map(Dish::getName)
                .map(String::length)
                .collect(toList());
        System.out.println("lengthDishNames: " + lengthDishNames);

        // 5.2.2. Flattening streams
        String[] arrayOfWords = new String[]{"Goodbye", "World"};
        Stream<String> streamOfWords = Arrays.stream(arrayOfWords);
        ///
        // List<Stream<String>> streamList = streamOfWords
        //         .map(word -> word.split(""))
        //         .map(Arrays::stream)
        //         .distinct()
        //         .collect(toList());
        // // streamList: [java.util.stream.ReferencePipeline$Head@3941a79c, java.util.stream.ReferencePipeline$Head@506e1b77]
        // System.out.println("streamList: " + streamList);

        List<String> stringList = streamOfWords
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(toList());
        System.out.println("stringList: " + stringList);

        // Quiz 5.2: Mapping
        // 1. Given a list of numbers, how would you return a list of the square of each number? For
        // example, given [1, 2, 3, 4, 5] you should return [1, 4, 9, 16, 25].
        List<Integer> numbers2 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> numberSquares = numbers2.stream()
                .map(i -> i * i)
                .collect(toList());
        System.out.println("numberSquares: " + numberSquares);

        // 2. Given two lists of numbers, how would you return all pairs of numbers? For example, given a
        // list [1, 2, 3] and a list [3, 4] you should return [(1, 3), (1, 4), (2, 3), (2, 4), (3, 3), (3, 4)]. For
        // simplicity, you can represent a pair as an array with two elements.
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(3, 4);
        // list1 -> Stream<Integer> -> Stream<Stream<int[]>> -> Stream<int[]>(flatMap)
        List<int[]> numberPairs = list1.stream()
                .flatMap(i -> list2.stream()
                        .map(j -> new int[]{i, j}))
                .collect(toList());

        List<String> pairs = new ArrayList<>();
        for (int[] numberPair : numberPairs) {
            pairs.add("(" + numberPair[0] + ", " + numberPair[1] + ")");
        }
        System.out.println("pairs: " + pairs);

        // 3. How would you extend the previous example to return only pairs whose sum is divisible by 3?
        // For example, (2, 4) and (3, 3) are valid.
        // method 1:
        List<int[]> numberPairs2 = list1.stream()
                .flatMap(i -> list2.stream()
                        .filter(j -> (i + j) % 3 == 0)
                        .map(j -> new int[]{i, j}))
                .collect(toList());

        List<String> pairs2 = new ArrayList<>();
        for (int[] numberPair : numberPairs2) {
            pairs2.add("(" + numberPair[0] + ", " + numberPair[1] + ")");
        }
        System.out.println("pairs2: " + pairs2);

        // method 2:
        List<int[]> numberPairs3 = list1.stream()
                .flatMap(i -> list2.stream()
                        .map(j -> new int[]{i, j}))
                .collect(toList());

        List<String> pairs3 = new ArrayList<>();
        for (int[] numberPair : numberPairs3) {
            if ((numberPair[0] + numberPair[1]) % 3 == 0) {
                pairs3.add("(" + numberPair[0] + ", " + numberPair[1] + ")");
            }
        }
        System.out.println("pairs3: " + pairs3);

        // 5.3. Finding and matching
        // 5.3.1. Checking to see if a predicate matches at least one element
        if (menu.stream().anyMatch(Dish::isVegetarian)) {
            System.out.println("The menu is (somewhat) vegetarian friendly!");
        }

        // 5.3.2. Checking to see if a predicate matches all elements
        boolean isHealthy = menu.stream().allMatch(dish -> dish.getCalories() < 1000);
        boolean isHealthy2 = menu.stream().noneMatch(dish -> dish.getCalories() >= 1000);

        // 5.3.3. Finding an element
        Optional<Dish> dish = menu.stream().filter(Dish::isVegetarian).findAny();
        menu.stream().filter(Dish::isVegetarian).findAny().ifPresent(d -> System.out.println(d.getName()));

        // 5.3.4. Finding the first element
        // For example, the code that follows, given a list of numbers,
        // finds the first square that’s divisible by 3:
        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        integers.stream()
                .map(x -> x * x)
                .filter(x -> x % 3 == 0)
                .findFirst()
                .ifPresent(System.out::println);
    }
}
