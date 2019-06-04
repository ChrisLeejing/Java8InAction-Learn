package com.chris.chap1;

import java.util.*;
import java.util.function.Predicate;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

/**
 * This is description.
 *
 * @author Chris Lee
 * @date 2019/6/3 22:52
 */
public class FilteringApples {
    public static void main(String[] args) {
        List<Apple> inventory = Arrays.asList(
                new Apple(80, "green"),
                new Apple(155, "green"),
                new Apple(120, "red")
        );

        // to sort a list of apples in inventory based on their weight, remark: "Alt + Enter" makes the code to the lambda expression.
        Collections.sort(inventory, new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return o1.getWeight() - o2.getWeight();
            }
        });
        // [Apple{weight=80, color='green'}, Apple{weight=120, color='red'}, Apple{weight=155, color='green'}]
        System.out.println(inventory.toString());

        inventory.sort(comparing(Apple::getWeight));
        // [Apple{weight=80, color='green'}, Apple{weight=120, color='red'}, Apple{weight=155, color='green'}]
        System.out.println(inventory.toString());

        List<Apple> greenApples = filterApples(inventory, Apple::isGreenApple);
        // [Apple{weight=80, color='green'}, Apple{weight=155, color='green'}]
        System.out.println(greenApples);
        List<Apple> heavyApples = filterApples(inventory, Apple::isHeavyApple);
        // [Apple{weight=155, color='green'}]
        System.out.println(heavyApples);
        System.out.println("------------------------");
        System.out.println(filterApples(inventory, (Apple a) -> a.getWeight() > 150));
        System.out.println(filterApples(inventory, (Apple a) -> "red".equals(a.getColor())));
        System.out.println(filterApples(inventory, (Apple a) -> a.getWeight() < 150 && "green".equals(a.getColor())));
        System.out.println("------------------------");
        // Sequential processing
        List<Apple> heavyApples2 = inventory.stream()
                .filter((Apple a) -> a.getWeight() > 150)
                .collect(toList());
        System.out.println(heavyApples2);

        // Parallel processing
        List<Apple> heavyApples3 = inventory.parallelStream()
                .filter((Apple a) -> a.getWeight() > 150)
                .collect(toList());
        System.out.println(heavyApples3);
    }

    public static List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if ("green".equals(apple.getColor())) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterHeavyApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getWeight() > 150) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterApples(List<Apple> inventory, Predicate<Apple> p) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (p.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    public static class Apple {

        private int weight = 0;
        private String color = "";

        public static boolean isGreenApple(Apple apple) {
            return "green".equals(apple.getColor());
        }

        public static boolean isHeavyApple(Apple apple) {
            return apple.getWeight() > 150;
        }

        @Override
        public String toString() {
            return "Apple{" +
                    "weight=" + weight +
                    ", color='" + color + '\'' +
                    '}';
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public Apple() {
        }

        public Apple(int weight, String color) {
            this.weight = weight;
            this.color = color;
        }
    }

}
