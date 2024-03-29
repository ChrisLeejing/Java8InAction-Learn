package com.chris.chap4;

/**
 * This is description.
 *
 * @author Chris Lee
 * @date 2019/6/17 10:29
 */
public class Dish {
    private final String name;
    private final boolean vegetarian;
    private final int calories;
    private final Type type;

    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public int getCalories() {
        return calories;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return name;
    }

    public enum Type { MEAT, FISH, OTHER }
    public enum Type1 { MEAT, FISH, OTHER }

    public static void main(String[] args) {
        System.out.println(Type.MEAT.equals(Type1.MEAT));
        System.out.println(Type.MEAT.equals(Type.MEAT));
        System.out.println(Type.MEAT==Type.MEAT);
        // System.out.println(Type.MEAT==Type1.MEAT);
        /*
        false
        true
        true
        Operator '==' cannot be applied to 'com.chris.chap4.Dish.Type', 'com.chris.chap4.Dish.Type1'
         */
    }
}
