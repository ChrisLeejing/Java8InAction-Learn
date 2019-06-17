package com.chris.chap4;

import java.util.*;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

/**
 * This is description.
 *
 * @author Chris Lee
 * @date 2019/6/17 10:28
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

        // 4.1. What are streams?
        // Compare the following code to return the names of dishes that are low in calories, sorted by number of calories, first in Java 7 and then in Java 8 using streams.
        // Before (Java 7):
        List<Dish> lowCaloricDishes = new ArrayList<>();
        for (Dish d : menu) {
            if (d.getCalories() < 400) {
                lowCaloricDishes.add(d);
            }
        }

        Collections.sort(lowCaloricDishes, new Comparator<Dish>() {
            @Override
            public int compare(Dish o1, Dish o2) {
                return Integer.compare(o1.getCalories(), o2.getCalories());
            }
        });

        List<String> lowCaloricDishesName = new ArrayList<>();
        for (Dish d : lowCaloricDishes) {
            lowCaloricDishesName.add(d.getName());
        }
        // lowCaloricDishesName: [season fruit, prawns, rice]
        System.out.println("lowCaloricDishesName: " + lowCaloricDishesName);

        /*
            After (Java 8):
            * Declarative: More concise and readable
            * Composable: Greater flexibility
            * Parallelizable: Better performance
         */
        List<String> lowCaloricDishesName2 = menu.stream()
                .filter(dish -> dish.getCalories() < 400)
                .sorted(comparing(Dish::getCalories))
                .map(Dish::getName)
                .collect(toList());
        // lowCaloricDishesName2: [season fruit, prawns, rice]
        System.out.println("lowCaloricDishesName2: " + lowCaloricDishesName2);

        // To exploit a multicore architecture and execute this code in parallel, you need only change stream() to parallelStream():
        List<String> lowCaloricDishesName3 = menu.parallelStream()
                .filter(dish -> dish.getCalories() < 400)
                .sorted(comparing(Dish::getCalories))
                .map(Dish::getName)
                .collect(toList());
        // lowCaloricDishesName3: [season fruit, prawns, rice]
        System.out.println("lowCaloricDishesName3: " + lowCaloricDishesName3);

        // 4.2. Getting started with streams
        // No result is produced, and indeed no element from menu is even selected, until collect is invoked. You can think of it as if the method invocations in the chain are queued up until collect is called.
        // threeHighCaloricDishNames: [pork, beef, chicken]
        List<String> threeHighCaloricDishNames = menu.stream()
                .filter(dish -> dish.getCalories() > 300)
                .map(Dish::getName)
                .limit(3)
                .collect(toList());
        System.out.println("threeHighCaloricDishNames: " + threeHighCaloricDishNames);

        // 4.3. Streams vs. collections
        // 4.3.1. Traversable only once
        // For example, the following code would throw an exception indicating the stream has been consumed:
        List<String> list = Arrays.asList("Java 8", "In", "Action");
        Stream<String> stream = list.stream();
        /*
        Java 8
        In
        Action
        Exception in thread "main" java.lang.IllegalStateException: stream has already been operated upon or closed
            at java.util.stream.AbstractPipeline.sourceStageSpliterator(AbstractPipeline.java:279)
            at java.util.stream.ReferencePipeline$Head.forEach(ReferencePipeline.java:580)
            at com.chris.chap4.StreamDemo.main(StreamDemo.java:91)
         */
        stream.forEach(System.out::println);
        ///
        // stream.forEach(System.out::println);

        // 4.3.2. External vs. internal iteration
        // Listing 4.1. Collections: external iteration with a for-each loop
        ArrayList<String> names = new ArrayList<>();
        for (Dish dish : menu) {
            names.add(dish.getName());
        }

        // Listing 4.2. Collections: external iteration using an iterator behind the scenes
        ArrayList<String> names2 = new ArrayList<>();
        Iterator<Dish> iterator = menu.iterator();
        while (iterator.hasNext()) {
            Dish next = iterator.next();
            names2.add(next.getName());
        }

        // Listing 4.3. Streams: internal iteration
        List<String> names3 = menu.stream()
                .map(Dish::getName)
                .collect(toList());
        // names: [pork, beef, chicken, french fries, rice, season fruit, pizza, prawns, salmon]
        System.out.println("names: " + names);
        System.out.println("names2: " + names2);
        System.out.println("names3: " + names3);

        // 4.4. Stream operations
        // Quiz 4.1: Intermediate vs. terminal operations
        long count = menu.stream()
                .filter(dish -> {
                    System.out.println("filtering dish: " + dish);
                    return dish.getCalories() > 300;
                })
                .map(dish -> {
                    System.out.println("map dish name: " + dish.getName());
                    return dish.getName();
                })
                .distinct()
                .limit(3) // short-circuiting
                .count();
        System.out.println("count: " + count);
        /*
            filtering dish: pork
            map dish name: pork(loop fusion)
            filtering dish: beef
            map dish name: beef
            filtering dish: chicken
            map dish name: chicken
            count: 3
         */
    }
}
