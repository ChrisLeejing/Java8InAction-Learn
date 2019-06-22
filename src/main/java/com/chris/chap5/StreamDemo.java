package com.chris.chap5;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Math.sqrt;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

/**
 * This is description.
 * Chapter 5. Working with streams
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
        List<Stream<String>> streamList = streamOfWords
                .map(word -> word.split(""))
                .map(Arrays::stream)
                .distinct()
                .collect(toList());
        // streamList: [java.util.stream.ReferencePipeline$Head@3941a79c, java.util.stream.ReferencePipeline$Head@506e1b77]
        System.out.println("streamList: " + streamList);

        Stream<String> streamOfWords2 = Arrays.stream(arrayOfWords);
        List<String> stringList = streamOfWords2
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

        // 5.4. Reducing
        // 5.4.1. Summing the elements
        List<Integer> integers1 = Arrays.asList(1, 2, 3, 4, 5);
        int sum = 0;
        for (Integer integer : integers1) {
            sum += integer;
        }
        System.out.println("sum: " + sum);

        Integer sum2 = integers1.stream().reduce(0, Integer::sum);
        System.out.println("sum2: " + sum2);

        // in case of the stream without number.
        Optional<Integer> sumOptional = integers1.stream().reduce(Integer::sum);
        Optional<Integer> maxOptional = integers1.stream().reduce(Integer::max);
        Optional<Integer> minOptional = integers1.stream().reduce(Integer::min);

        Integer product = integers1.stream().reduce(1, (a, b) -> a * b);
        System.out.println("product: " + product);

        sumOptional.ifPresent(System.out::println);
        maxOptional.ifPresent(System.out::println);
        minOptional.ifPresent(System.out::println);

        // Quiz 5.3: Reducing
        // How would you count the number of dishes in a stream using the map and reduce methods?
        // A chain of map and reduce is commonly known as the map-reduce pattern, made famous by
        // Google’s use of it for web searching because it can be easily parallelized.
        Integer reduce = menu.stream()
                .map(d -> 1)
                .reduce(0, Integer::sum);
        System.out.println("reduce: " + reduce);

        long count = menu.stream()
                .count();
        System.out.println("count: " + count);

        // You’ll also see in chapter 7 that to sum all the elements in parallel using
        // streams, there’s almost no modification to your code: stream() becomes parallelStream():
        Integer parallelSum = integers1.parallelStream()
                .reduce(0, Integer::sum);
        System.out.println("parallelSum: " + parallelSum);

        // 5.5. Putting it all into practice

        // 5.5.1. The domain: Traders and Transactions
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

        // 5.5.2. Solutions
        // 1. Find all transactions in the year 2011 and sort them by value (small to high).
        // transactions1: [{Trader:Brian in Cambridge, year: 2011, value:300}, {Trader:Raoul in Cambridge, year: 2011, value:400}]
        List<Transaction> transactions1 = transactions.stream()
                .filter(transaction -> transaction.getYear() == 2011)
                .sorted(comparing(Transaction::getValue))
                .collect(toList());
        System.out.println("transactions1: " + transactions1);

        // 2. What are all the unique cities where the traders work?
        // cities: [Cambridge, Milan]
        List<String> cities = transactions.stream()
                .map(Transaction::getTrader)
                .map(Trader::getCity)
                .distinct()
                .collect(toList());
        System.out.println("cities: " + cities);

        Set<String> cities2 = transactions.stream()
                .map(transaction -> transaction.getTrader().getCity())
                .collect(toSet());
        System.out.println("cities2: " + cities2);

        // 3. Find all traders from Cambridge and sort them by name.
        // traders: [Trader:Alan in Cambridge, Trader:Brian in Cambridge, Trader:Raoul in Cambridge, Trader:Raoul in Cambridge]
        List<Trader> traders = transactions.stream()
                .map(Transaction::getTrader)
                .filter(trader -> "Cambridge".equals(trader.getCity()))
                .sorted(comparing(Trader::getName))
                .collect(toList());
        System.out.println("traders: " + traders);

        // 4. Return a string of all traders’ names sorted alphabetically.
        // traderNames: AlanBrianMarioRaoul
        String traderNames = transactions.stream()
                .map(transaction -> transaction.getTrader().getName())
                .distinct()
                .sorted()
                .reduce("", (name1, name2) -> name1 + name2);
        System.out.println("traderNames: " + traderNames);

        // Note that this solution isn’t very efficient (all Strings are repeatedly concatenated, which creates
        // a new String object at each iteration). In the next chapter, you’ll see a more efficient solution
        // that uses joining() as follows (which internally makes use of a StringBuilder):
        String traderNames2 = transactions.stream()
                .map(transaction -> transaction.getTrader().getName())
                .distinct()
                .sorted()
                .collect(Collectors.joining());
        System.out.println("traderNames2: " + traderNames2);

        // 5. Are any traders based in Milan?
        // trader: Optional[{Trader:Mario in Milan, year: 2012, value:710}]
        Optional<Transaction> trader = transactions.stream()
                .filter(transaction -> "Milan".equals(transaction.getTrader().getCity()))
                .findAny();
        System.out.println("trader: " + trader);

        boolean milanBased = transactions.stream()
                .anyMatch(transaction -> "Milan".equals(transaction.getTrader().getCity()));
        System.out.println("milanBased: " + milanBased);


        // 6. Print all transactions’ values from the traders living in Cambridge.
        // collect: [300, 1000, 400, 950]
        List<Integer> collect = transactions.stream()
                .filter(transaction -> "Cambridge".equals(transaction.getTrader().getCity()))
                .map(Transaction::getValue)
                .collect(toList());
        System.out.println("collect: " + collect);

        transactions.stream()
                .filter(transaction -> "Cambridge".equals(transaction.getTrader().getCity()))
                .map(Transaction::getValue)
                .forEach(System.out::println);

        // 7. What’s the highest value of all the transactions?
        // maxValue: Optional[1000]
        Optional<Integer> maxValue = transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer::max);
        System.out.println("maxValue: " + maxValue);

        // 8. Find the transaction with the smallest value.
        // minValue: Optional[300]
        Optional<Integer> minValue = transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer::min);
        System.out.println("minValue: " + minValue);

        // 5.6. Numeric streams
        // 5.6.1. Primitive stream specializations
        Integer calories = menu.stream()
                .map(Dish::getCalories)
                .reduce(0, Integer::sum);
        System.out.println("calories: " + calories);

        int calories2 = menu.stream()
                .mapToInt(Dish::getCalories)
                .sum();
        System.out.println("calories2: " + calories2);

        // Converting back to a stream of objects
        IntStream intStream = menu.stream()
                .mapToInt(Dish::getCalories);
        Stream<Integer> boxed = intStream.boxed();

        // Default values: OptionalInt
        OptionalInt optionalInt = menu.stream()
                .mapToInt(Dish::getCalories)
                .max();
        int maxCalorie = optionalInt.orElse(1);
        System.out.println("maxCalorie: " + maxCalorie);

        // 5.6.2. Numeric ranges
        long evenNumbers = IntStream.rangeClosed(1, 100)
                .filter(n -> n % 2 == 0)
                .count();
        System.out.println("evenNumbers: " + evenNumbers);

        // 5.6.3. Putting numerical streams into practice: Pythagorean triples
        IntStream.rangeClosed(1, 100)
                .boxed()// very important
                .flatMap(
                        a -> IntStream.rangeClosed(a, 100)
                                .filter(b -> sqrt(a * a + b * b) % 1 == 0 && sqrt(a * a + b * b) <= 100)
                                .mapToObj(b -> new int[]{a, b, (int) sqrt(a * a + b * b)})
                )
                .forEach(n -> System.out.println(n[0] + ", " + n[1] + ", " + n[2]));
        System.out.println("--------------------------------");

        // Can you do better?
        IntStream.rangeClosed(1, 100)
                .boxed()
                .flatMap(a -> IntStream.rangeClosed(a, 100)
                        .mapToObj(b -> new double[]{a, b, sqrt(a * a + b * b)})
                        .filter(c -> c[2] % 1 == 0 && c[2] <= 100)
                )
                .forEach(n -> System.out.println((int) n[0] + ", " + (int) n[1] + ", " + (int) n[2]));

        // 5.7. Building streams
        // 5.7.1. Streams from values
        Stream<String> stringStream = Stream.of("Java 8", "in", "action");
        stringStream.map(String::toUpperCase).forEach(System.out::println);
        Stream<String> empty = Stream.empty();

        // 5.7.2. Streams from arrays
        int[] ints = {1, 2, 3, 4, 5};
        int sum1 = Arrays.stream(ints).sum();
        System.out.println("sum1: " + sum1);

        // 5.7.3. Streams from files
        // try-with-resource
        try (Stream<String> lines = Files.lines(Paths.get("E:\\Code\\Java\\Java8InAction-Learn\\src\\main\\java\\com\\chris\\chap5\\test.txt"), Charset.defaultCharset())) {
            long count1 = lines.flatMap(line -> Arrays.stream(line.split(" "))).distinct().count();
            System.out.println("count1: " + count1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 5.7.4. Streams from functions: creating infinite streams!
        // Iterate
        Stream.iterate(0, t -> t + 2).limit(5).forEach(System.out::println);
        // Quiz 5.4: Fibonacci tuples series
        Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
                .limit(10)
                .forEach(n -> System.out.println("(" + n[0] + ", " + n[1] + ")"));

        Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]})
                .limit(10)
                .forEach(n -> System.out.println(n[0]));

        // Generate
        // :: means @FunctionalInterface
        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);
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
