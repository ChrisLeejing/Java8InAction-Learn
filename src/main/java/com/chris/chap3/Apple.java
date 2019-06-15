package com.chris.chap3;

/**
 * This is description.
 *
 * @author Chris Lee
 * @date 2019/6/15 13:48
 */
public class Apple {
    private Integer weight = 0;
    private String color = "";

    @Override
    public String toString() {
        return "Apple{" +
                "weight=" + weight +
                ", color='" + color + '\'' +
                '}';
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
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

    public Apple(Integer weight, String color) {
        this.weight = weight;
        this.color = color;
    }
}
