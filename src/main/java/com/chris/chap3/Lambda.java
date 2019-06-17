package com.chris.chap3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This is description.
 *
 * @author Chris Lee
 * @date 2019/6/15 13:45
 */
public class Lambda {

    public static void main(String[] args) throws IOException {
        List<Apple> inventory = Arrays.asList(
                new Apple(80, "green"),
                new Apple(155, "green"),
                new Apple(155, "red"),
                new Apple(120, "red")
        );
        // Before:
        Comparator<Apple> byWeight = new Comparator<Apple>() {
            @Override
            public int compare(Apple o1, Apple o2) {
                return o1.getWeight().compareTo(o2.getWeight());
            }
        };

        // After (with lambda expressions):
        Comparator<Apple> byWeight2 = (Apple o1, Apple o2) -> o1.getWeight().compareTo(o2.getWeight());

        // Comparator.comparing()
        Comparator<Apple> byWeight3 = Comparator.comparing(Apple::getWeight);

        Runnable r1 = () -> System.out.println("Hello world 1");
        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello world 2");
            }
        };

        process(r1);
        process(r2);
        process(() -> System.out.println("Hello world 3"));

        String oneLine = processFile(BufferedReader::readLine);
        System.out.println(oneLine);

        String twoLines = processFile(bufferedReader -> bufferedReader.readLine() + " " + bufferedReader.readLine());
        System.out.println(twoLines);

        // Predicate
        List<String> list = Arrays.asList("Java 8", "", "In", "", "Action");
        Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();
        List<String> nonEmpty = filter(list, nonEmptyStringPredicate);
        System.out.println(nonEmpty);

        // Consumer
        forEach(Arrays.asList(1, 2, 3, 4, 5), System.out::println);

        // Function
        List<Integer> integerList = map(list, String::length);
        System.out.println(integerList);

        // Method references
        List<String> stringList = Arrays.asList("a", "b", "A", "B");
        System.out.println(stringList);
        stringList.sort(String::compareToIgnoreCase);
        stringList.sort(String::compareTo);
        System.out.println(stringList);

        // 1. Composing Comparators
        Comparator.comparing(Apple::getWeight);
        // 2. Reversed order
        inventory.sort(Comparator.comparing(Apple::getWeight).reversed());
        // 3. Chaining Comparators
        inventory.sort(Comparator.comparing(Apple::getWeight).reversed().thenComparing(Apple::getColor));

        // Composing Predicates
        List<Apple> redApples = filter(inventory, apple -> "red".equals(apple.getColor()));
        List<Apple> notRedApples = filter(inventory, apple -> !"red".equals(apple.getColor()));

        Predicate<Apple> redPredicate = apple -> "red".equals(apple.getColor());
        List<Apple> redApples2 = filter(inventory, redPredicate);
        System.out.println(redApples2);
        Predicate<Apple> notRedPredicate = redPredicate.negate();
        List<Apple> notRedApples2 = filter(inventory, notRedPredicate);
        System.out.println(notRedApples2);
        Predicate<Apple> redAndHeavyPredicate = redPredicate.and(apple -> apple.getWeight() > 150);
        List<Apple> redAndHeavyApples = filter(inventory, redAndHeavyPredicate);
        System.out.println(redAndHeavyApples);
        Predicate<Apple> redAndHeavyOrGreenPredicate = redPredicate.and(apple -> apple.getWeight() > 150).or(apple -> "green".equals(apple.getColor()));
        // a.or(b).and(c) means (a || b) && c
        List<Apple> redAndHeavyOrGreenApples = filter(inventory, redAndHeavyOrGreenPredicate);
        System.out.println(redAndHeavyOrGreenApples);

        // Composing Functions: andThen(), compose
        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;
        Function<Integer, Integer> h = f.andThen(g);
        // andThen() means g(f(x))
        Integer result = h.apply(1);
        // result: 4
        System.out.println("result: " + result);

        Function<Integer, Integer> f2 = x -> x + 1;
        Function<Integer, Integer> g2 = x -> x * 2;
        Function<Integer, Integer> h2 = f2.compose(g2);
        // compose() means f(g(x))
        Integer result2 = h2.apply(1);
        // result2: 3
        System.out.println("result2: " + result2);

    }

    static class Letter {
        public static String addHeader(String text) {
            return "From Chris: " + text;
        }

        public static String addFooter(String text) {
            return text + " Kind regards";
        }

        public static String checkSpelling(String text) {
            return text.replaceAll("labda", "lambda");
        }

        public static void main(String[] args){
            Function<String, String> addHeader = Letter::addHeader;
            // pipeline 1: addHeader -> checkSpelling -> addFooter
            Function<String, String> transformationPipeline1 = addHeader.andThen(Letter::checkSpelling).andThen(Letter::addFooter);
            // pipeline 2: addHeader -> addFooter
            Function<String, String> transformationPipeline2 = addHeader.andThen(Letter::addHeader);
        }
    }
    public static <T, R> List<R> map(List<T> list, Function<T, R> function) {
        List<R> result = new ArrayList<>();
        for (T t : list) {
            R r = function.apply(t);
            result.add(r);
        }
        return result;
    }

    public static <T> void forEach(List<T> list, Consumer<T> consumer) {
        for (T t : list) {
            consumer.accept(t);
        }
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> result = new ArrayList<>();
        for (T t : list) {
            if (p.test(t)) {
                result.add(t);
            }
        }
        return result;
    }

    @FunctionalInterface
    public interface BufferedReaderProcessor {
        String process(BufferedReader bufferedReader) throws IOException;
    }

    public static String processFile(BufferedReaderProcessor p) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("E:\\Code\\Java\\Java8InAction-Learn\\src\\main\\java\\com\\chris\\chap3\\data.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return p.process(br);
    }

    private static void process(Runnable r) {
        r.run();
    }
}
