package com.chris.chap1;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparing;

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

        // 对inventory中的苹果按照重量进行排序, 注: 通过Alt + Enter可以自动切换到lambda表达式.
        Collections.sort(inventory, new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return o1.getWeight() - o2.getWeight();
            }
        });
        System.out.println(inventory.toString());
        inventory.sort(comparing(Apple::getWeight));
        System.out.println(inventory.toString());
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
