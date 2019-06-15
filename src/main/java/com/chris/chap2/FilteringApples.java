package com.chris.chap2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is description.
 *
 * @author Chris Lee
 * @date 2019/6/5 7:06
 */
public class FilteringApples {
    public static void main(String[] args) {
        List<Apple> inventory = Arrays.asList(
                new Apple(80, "green"),
                new Apple(155, "green"),
                new Apple(155, "red"),
                new Apple(120, "red")
        );
        // Fourth attempt: filtering by abstract criteria
        // [Apple{weight=80, color='green'}, Apple{weight=155, color='green'}]
        System.out.println(filterApplesByColor(inventory, "green"));
        // [Apple{weight=155, color='red'}, Apple{weight=120, color='red'}]
        System.out.println(filterApplesByColor(inventory, "red"));
        // [Apple{weight=155, color='red'}]
        System.out.println(filterApples(inventory, new AppleRedAndHeavyPredicate()));

        // Fifth attempt: using an anonymous class
        // [Apple{weight=155, color='red'}, Apple{weight=120, color='red'}]
        List<Apple> redApples = filterApples(inventory, new ApplePredicate() {
            @Override
            public boolean test(Apple apple) {
                return "red".equals(apple.getColor());
            }
        });
        System.out.println(redApples);

        // Sixth attempt: using a lambda expression
        List<Apple> redApples2 = filterApples(inventory, (Apple apple) -> "red".equals(apple.getColor()));
        System.out.println(redApples2);



    }

    /**
     * Seventh attempt: abstracting over List type
     * @param <T>
     */
    public interface Predicate<T> {
        boolean test(T t);
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T t : list) {
            if (predicate.test(t)) {
                result.add(t);
            }
        }
        return result;
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

    public static List<Apple> filterApplesByColor(List<Apple> inventory, String color) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getColor().equals(color)) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterApplesByWeight(List<Apple> inventory, int weight) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (apple.getWeight() > weight) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate p) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if (p.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    public static class AppleRedAndHeavyPredicate implements ApplePredicate {

        @Override
        public boolean test(Apple apple) {
            return "red".equals(apple.getColor()) && apple.getWeight() > 150;
        }
    }

    interface ApplePredicate {
        boolean test(Apple apple);
    }

    public static class Apple {

        private int weight = 0;
        private String color = "";

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
